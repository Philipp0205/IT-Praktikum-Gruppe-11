package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;

public class EditContactForm extends VerticalPanel {
	EditorAdmin e;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Grid grid;

	JabicsUser u;
	Contact contact;
	Boolean isNewContact;
	Boolean userIsOwner;
	// True = gerade am editieren, Buttons anzeigen, False = keine Buttons anzeigen
	Boolean editContactState = false;

	Button deleteContactButton = new Button("Kontakt löschen");
	Button saveButton = new Button("Änderungen speichern");
	Button editButton = new Button("Ausprägungen hinzufügen/löschen");
	Button exitButton = new Button("Abbruch");
	Button existingSharedContactButton;

	VerticalPanel pPanel;
	HorizontalPanel buttonPanel;
	HorizontalPanel addPPanel;

	ArrayList<PValue> allPV;

	ArrayList<PropForm> val;

	ArrayList<Property> standardProperties;

	ListBox formattype = new ListBox();
	TextBox propertyName = new TextBox();
	TextBox pValueTextBox = new TextBox();
	DatePicker dp;
	DatePicker dp2;
	Button done2;
	Date tempDate;

	DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd");

	/**
	 * Wird beim erstellen der <code>EditContactForm</code> aufgerufen. Initalisiert alle Objekte, welche die 
	 * <code>EditContactform</code> benötigt. Vor allem GWT-Panels und Objekte der Klasse <code>Button</code> 
	 * Des Weiteren werden <code>ClickHandler<c/ode> für die Buttons gesetzt.
	 */
	public void onLoad() {
		if (contact != null) {
			GWT.log("EditCont");
			pPanel = new VerticalPanel();
			buttonPanel = new HorizontalPanel();
			addPPanel = new HorizontalPanel();

			buttonPanel.add(exitButton);
			buttonPanel.add(deleteContactButton);
			buttonPanel.add(saveButton);
			buttonPanel.addStyleName("buttonPanel");

			// Checks, die beim Laden der Form durchgeführt werrden müssen, um die richtige
			// UI anzuzeigen

			if (isNewContact) {
				saveButton.setText("Neuen Kontakt anlegen");
				deleteContactButton.setVisible(false);
			} else {
				saveButton.setText("Änderungen speichern");
				deleteContactButton.setVisible(true);
			}

			saveButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					save();
				}
			});
			editButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent e) {
					editContact();
					if (!editContactState) {
						editContactState = true;
						editButton.setText("Fertig");
					} else {
						editContactState = false;
						editButton.setText("Ausprägungen hinzufügen/löschen");
					}
				}
			});
			exitButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (!isNewContact) {
						e.showContact(contact);
					} else {
						e.showMenuOnly();
					}
				}
			});

			// Optionen zum Hinzufügen einer Eigenschaft
			Grid propertyAddBox = new Grid(3, 5);

			// Eine Textbox zum Benennen des Eigenschafts-Typs (z.B. "Haarfarbe")
			Label prop = new Label("Eigenschaftsname:");

			// Eine Listbox zum Setzen des Formats
			formattype.addItem("Text");
			formattype.addItem("Datum");
			formattype.addItem("Kommazahl");
			formattype.addItem("Zahl");
			dp2 = new DatePicker();
			dp2.setVisible(false);
			done2 = new Button("Fertig");
			done2.setVisible(false);
			done2.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent e) {
					dp2.setVisible(false);
					done2.setVisible(false);
				}
			});
			dp2.addValueChangeHandler(new ValueChangeHandler<Date>() {
				public void onValueChange(ValueChangeEvent<Date> event) {
					tempDate = event.getValue();
					pValueTextBox.setText(dateTimeFormat.format(event.getValue()).toString());
				}
			});
			pValueTextBox.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (formattype.getSelectedValue() == "Datum") {
						tempDate = new Date();
						dp2.setVisible(true);
						done2.setVisible(true);
					}
				}
			});

			Label type = new Label("Art:");
			Label pvaluelabel = new Label("Wert:");
			Button addPropertyButton = new Button("Hinzufügen");
			addPropertyButton.setStyleName("paddbutton");
			addPropertyButton.addClickHandler(new AddPropertyClickHandler());

			propertyAddBox.setWidget(0, 0, prop);
			propertyAddBox.setWidget(1, 0, propertyName);
			propertyAddBox.setWidget(0, 1, type);
			propertyAddBox.setWidget(1, 1, formattype);
			propertyAddBox.setWidget(0, 2, pvaluelabel);
			propertyAddBox.setWidget(1, 2, pValueTextBox);
			propertyAddBox.setWidget(1, 3, addPropertyButton);
			propertyAddBox.setWidget(2, 4, dp2);
			propertyAddBox.setWidget(1, 4, done2);
			addPPanel.add(propertyAddBox);

			prop.setStyleName("editclabel");
			type.setStyleName("editclabel");
			pvaluelabel.setStyleName("editclabel");
			pValueTextBox.setStyleName("TextBox");
			propertyName.setStyleName("TextBox");
			formattype.setStyleName("datatypeMenu");

			this.insert(pPanel, 0);
			this.insert(addPPanel, 1);
			this.insert(buttonPanel, 2);
			if (isNewContact) {
				addPPanel.setVisible(false);
			}

			standardProperties = new ArrayList<Property>();

			// Im Callback dieser Methode geht das Rendern des Kontakts los
			editorService.getStandardProperties(new GetStandardPropertiesCallback());

		} else {
			Label errorLabel = new Label("Kein Kontakt anzuzeigen");
			this.add(errorLabel);
		}

	}
	
	/**
	 *  Bezieht alle <code>PValue</code>s des <code>Contact</code>s und zeigt diese in einem <code>VerticalPanel</code> 
	 *  an.
	 */
	public void renderContact() {
		val = new ArrayList<PropForm>();
		for (Property pl : standardProperties) {
			val.add(new PropForm(pl));
		}

		if (this.contact != null) {
			// PValues, die Standardeigenschaften sind, den entsprechenden PropForms
			// zuordnen, ansonsten eine neue PropForm erstellen
			for (PValue pv : this.contact.getValues()) {
				if (pv.getProperty().isStandard()) {
					GWT.log("Standardeigenschaft : " + pv.getPropertyId());
					for (PropForm p : val) {
						GWT.log("+++++++Suche nach richtigem+++++++++");
						if (p.getProperty().getId() == pv.getProperty().getId()) {
							GWT.log("RichtigeGefunden!");
							p.replacePValue(pv);
							GWT.log("PValue zugeordnet");
						}
					}
				} else {
					Boolean bol = true;
					// Wenn dieses PValue zu einer bereits vorhandenen Property gehört, füge es an
					// der richtigen Stellen ein
					for (PropForm p : val) {
						if (p.getProperty().getId() == pv.getProperty().getId()) {
							GWT.log("RichtigeGefunden!");
							bol = false;
							// Neue PVForm hinzufügen
							p.addPValue(pv);
							GWT.log("PValue zugeordnet");
						}

					}
					// Wenn dieses PValue alleine ist, mache neue PropForm
					if (bol) {
						PropForm pnew = new PropForm(pv);
						val.add(pnew);
					}
				}
			}
		}

		// Alle PropForms mit allen PVForms anzeigen lassen
		for (PropForm p : val) {
			p.show();
			for (PVForm pv : p.getPVForms()) {
				p.setStyleName("propForm");
				pv.show();
			}
			pPanel.add(p);

			pPanel.setStyleName("pPanel");
		}

	}
	
	/**
	 * Speichert die vom Nutzer vorgenommenen ändernungen, indem der Kontakt gespeichert wird.
	 * Dabei wird zwischen einem neuen <code>Contact</code> und einem bereits exisiterenden <code>Contact</code>
	 * unterschieden.
	 */
	public void save() {
		GWT.log("6.1 Save Contact");

		// Alle PValues aus der Tabelle ziehen
		ArrayList<PValue> allPV = new ArrayList<PValue>();
		for (PropForm p : val) {
			for (PValue pv : p.getPV()) {
				pv.setProperty(p.getProperty());
				allPV.add(pv);
			}
		}

		// Alle befüllten PValues erstellen
		ArrayList<PValue> filledPV = new ArrayList<PValue>();
		for (PValue pv : allPV) {
			if (pv.containsValue()) {
				contact.getValues().add(pv);
				filledPV.add(pv);
			}
		}

		// Überprüfen, ob der Name vollständig gesetzt ist
		boolean nameExistent = false;
		for (PValue pv : filledPV) {
			if (pv.getProperty().getId() == 1 || pv.getProperty().getId() == 2) {
				nameExistent = true;
			}
		}

		// alle pv aus dem PRopArray rausziehen und hier speichern

		if (nameExistent) {
			// überprüfen, ob Kontakt ein neuer oder ein bereits bestehender ist
			if (isNewContact) {

				// Es werden alle befüllten PValues übergeben
				GWT.log("6.4 " + filledPV.toString());
				editorService.createContact(filledPV, u, new AsyncCallback<Contact>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Neuer Kontakt konnte nicht angelegt werden!" + caught.getMessage());
					}

					@Override
					public void onSuccess(Contact result) {

						if (result != null) {
							setContact(result);
							e.addContactToTree(result);
							addPPanel.setVisible(true);
							exitButton.setText("Kontakt anzeigen");
							saveButton.setText("Änderungen speichern");
							setNewContact(false);
							deleteContactButton.setVisible(true);
						}

					}
				});
			} else if (!isNewContact) {

				contact.setValues(filledPV);
				editorService.updateContact(contact, u, new UpdateContactCallback());
				exitButton.setVisible(false);
			}
		} else if (!nameExistent) {
			Window.alert("Kontakt muss einen Namen haben! Bitte Name und Nachname eingeben.");
		} else
			Window.alert("Massiver Fehler! Das sollte nicht passieren. Kontakt ist weder neu noch alt");
		// editorService.updatePValue(val, new UpdatePValueCallback());
	}

	/**
	 * Blendet <code>Button</code>s zum hinzufügen und löschen von Ausprägungen ein.
	 */
	public void editContact() {
		if (editContactState) {
			for (PropForm p : val) {
				for (PVForm pv : p.getPVForms()) {
					pv.showDeleteButton();
				}
				p.showAddButton();
			}
		} else {
			for (PropForm p : val) {
				for (PVForm pv : p.getPVForms()) {
					pv.hideDeleteButton();
				}
				p.hideAddButton();
			}
		}
	}
	
	/**
	 * Setzt den <code>Contact</code> der Instanz.
	 * 
	 * @param c
	 * 			<code>Contact</code> welcher gesetzt werden soll.
	 */
	public void setContact(Contact c) {
		this.contact = c;
	}
	
	/**
	 * Setzten des Indiktator, ob es sich um einen neuen oder bereits exisiterenden <code>Contact</code>
	 * handelt.
	 * 
	 * @param bol
	 * 			Status der gesetzt weden soll.
	 */
	public void setNewContact(Boolean bol) {
		this.isNewContact = bol;
	}

	public void setEditor(EditorAdmin e) {
		this.e = e;
	}

	public void setUser(JabicsUser user) {
		this.u = user;
	}

	/**
	 * Diese Klasse realisiert einen Clickhandler für den addPropertyButton. Beim
	 * aktivieren des Buttons <code>addPropertyButton</code> wird zunächst ein
	 * Eigenschaftsobjekt erstellt, welchem die vom Nutzer angegebenen Attribute
	 * zugewiesen werden. Anschließend wird auf dem Server die Methode
	 * <code>createPValue()</code> aufgerufen.
	 */
	private class AddPropertyClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			switch (formattype.getSelectedItemText()) {
			case "Text":
				editorService.createProperty(propertyName.getText(), Type.STRING, new CreatePropertyCallback());
				break;
			case "Datum":
				editorService.createProperty(propertyName.getText(), Type.DATE, new CreatePropertyCallback());
				break;
			case "Kommazahl":
				editorService.createProperty(propertyName.getText(), Type.FLOAT, new CreatePropertyCallback());
				break;
			case "Zahl":
				editorService.createProperty(propertyName.getText(), Type.INT, new CreatePropertyCallback());
				break;
			}
		}
	}
	
	/**
	 * Callback welcher welcher ausgelöst wer wenn <code>PValue<code>s bezogen werden.
	 *
	 */
	class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		public void onSuccess(ArrayList<PValue> result) {
			if (result != null) {
				contact.setValues(result);
				renderContact();
			}
		}
	}

	/**
	 * Diese Callback-Klasse veranlasst die Erstellung eines neuen
	 * <code>Property</code> Objekts auf dem Server.
	 */
	public class CreatePropertyCallback implements AsyncCallback<Property> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Anlegen der neuen Eigenschaft ist leider fehlgeschlagen.");
		}

		@Override
		public void onSuccess(Property result) {
			if (result != null) {
				GWT.log(result.getTypeInString() + "Hinzufügen neue Property zur Tabelle");

			}

			if (result != null) {
				GWT.log(result.getTypeInString());
				switch (formattype.getSelectedItemText()) {
				case "Text":
					editorService.createPValue(result, pValueTextBox.getText(), contact, u, new CreatePValueCallback());
					break;
				case "Datum":
					editorService.createPValue(result, tempDate, contact, u, new CreatePValueCallback());
					break;
				case "Kommazahl":
					editorService.createPValue(result, Float.valueOf(pValueTextBox.getText()), contact, u,
							new CreatePValueCallback());
					break;
				case "Zahl":
					editorService.createPValue(result, Integer.valueOf(pValueTextBox.getText()), contact, u,
							new CreatePValueCallback());
					break;
				}
			}
		}
	}

	/**
	 * Diese Callback-Klasse aktualisiert die Ansicht nach der Änderung einer
	 * Eigenschafts-ausprägung.
	 */
	private class UpdateContactCallback implements AsyncCallback<Contact> {

		public void onFailure(Throwable caught) {
			Window.alert("Konnte nicht gespeichert werden!" + caught.getMessage());
		}

		public void onSuccess(Contact result) {
			if (result != null) {

				GWT.log("Kontakt " + result.getName() + " erfolgreich gespeichert mit diesen PV:");
				for (PValue pv : result.getValues()) {
					GWT.log(pv.toString());
				}
				setContact(result);
				exitButton.setText("Zurück");
				exitButton.setVisible(true);
				e.updateContactInTree(result);
				e.showContact(result);
			}
		}
	}

	/**
	 * Diese Callback-Klasse aktualisiert die Ansicht nach erfolgreichem Erstellen
	 * einer Eigenschaftsausprägung..
	 */
	private class CreatePValueCallback implements AsyncCallback<PValue> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Anlegen der neuen Eigenschaftsausprägung ist leider fehlgeschlagen.");
		}

		@Override
		public void onSuccess(PValue result) {
			if (result != null) {
				PropForm pform = new PropForm(result);
				pform.show();
				val.add(pform);
				pPanel.add(pform);
				pValueTextBox.setText("");
				propertyName.setText("");
			}
		}
	}

	/**
	 * Diese Callback-Klasse aktualisiert die Ansicht nach der Löschung einer
	 * Eigenschafts- ausprägung.
	 */
	class DeletePValueCallback implements AsyncCallback<Void> {
		PVForm pvForm;

		public DeletePValueCallback(PVForm pv) {
			this.pvForm = pv;
		}

		public void onFailure(Throwable caugth) {
			Window.alert("PValue konnte nicht gelöscht werden");
		}

		@Override
		public void onSuccess(Void result) {

			Window.alert("Erfolgreich gelöscht");
			pvForm.setVisible(false);
			pvForm.setPV(null);
		}
	}

	/**
	 * Diese Callback-Klasse erhält alle Standardeigenschaften und setzt diese im
	 * p-Array
	 */
	class GetStandardPropertiesCallback implements AsyncCallback<ArrayList<Property>> {
		public void onFailure(Throwable caugth) {
			Window.alert("Standardeigenschaften wurden nicht geladen");
		}

		public void onSuccess(ArrayList<Property> result) {
			if (result != null) {
				standardProperties = result;
				renderContact();
			}
		}
	}

	/**
	 *  Realisiert die Anzeige einer Property wenn der Nutzer einen Kontakt bearbeitet.
	 *
	 */
	private class PropForm extends HorizontalPanel {
		Property p;
		ArrayList<PVForm> pvForms = new ArrayList<PVForm>();
		VerticalPanel pvPanel = new VerticalPanel();
		Label property;
		Button addButton = new Button("+");

		public void showAddButton() {
			this.addButton.setVisible(true);
		}

		public void hideAddButton() {
			this.addButton.setVisible(false);
		}

		/**
		 * Anzeige der PForm, bzw dieser sagen, sich zu zeigen. Fügt alle Widgets in der
		 * Form dem Horizontal Panel hinzu. Durch das Hinzufügen der PVForms zum pvPanel
		 * werden auch alle PVForms angezeigt.
		 */
		void show() {
			/**
			 * Wenn kein PValue vorliegt, leeres erstellen
			 */
			if (pvForms.isEmpty()) {
				addPValue(new PValue(p, u));
			} else {
				for (PVForm pvForm : pvForms) {
					pvPanel.add(pvForm);
				}
			}
			// Styling Labels
			property.setWidth("100px");
			this.add(property);
			pvPanel.setWidth("200px");
			this.insert(pvPanel, 1);
			this.add(addButton);
			addButton.setStyleName("addButton");

		}
		
		/**
		 * Konstruktor welcher ein Objekt vom Datentyp <code>PropForm</code> 
		 * @param pp
		 */
		PropForm(Property pp) {
			this.p = pp;
			property = new Label(p.getLabel() + ":");
			property.setStyleName("propertylabel");
			addButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					addPValue(new PValue(p, u));
				}
			});
		}

		/**
		 * Hinzufügen eines PValues zur Form.
		 * 
		 * @param PValue pv
		 */
		void addPValue(PValue pv) {
			PVForm pvform = new PVForm(pv);
			pvForms.add(pvform);
			pvPanel.add(pvform);
		}

		/**
		 * Löschen des initial erstellten PVForms und ersetzen durch ein befülltes. Darf
		 * nur von renderContact() aufgerufen werden
		 * 
		 * @param PValue pv
		 */
		void replacePValue(PValue pv) {
			PVForm pvform = new PVForm(pv);
			pvForms = new ArrayList<PVForm>();
			pvForms.add(pvform);
			pvPanel.add(pvform);
		}
		
		/**
		 * Konstruktor welcher eine Instanz der Klasse <code>PValue</code> erzeugt.
		 * @param pv
		 */
		PropForm(PValue pv) {
			this(pv.getProperty());
			addPValue(pv);
		}
		
		/**
		 * Bezieht die aktuelle <code>Property</code>
		 * 
		 * @return die Property
		 */
		Property getProperty() {
			return this.p;
		}

		ArrayList<PVForm> getPVForms() {
			return this.pvForms;
		}

		ArrayList<PValue> getPV() {
			ArrayList<PValue> res = new ArrayList<PValue>();
			for (PVForm pf : pvForms) {
				if (pf.getPV() != null) {
					res.add(pf.getPV());
				}
			}
			return res;
		}
	}
	
	/**
	 * Realisiert die Anzeige der PValues beim Editieren eines Kontaktes.
	 *
	 */
	private class PVForm extends HorizontalPanel {
		PValue pval;
		Button delete = new Button("x");
		Button done = new Button("Fertig");
		TextBox val = new TextBox();

		public void show() {
			val.setStyleName("pvBox");
			this.insert(val, 0);
			delete.setStyleName("deleteBtn");
			this.insert(delete, 1);
		}

		public void showDeleteButton() {
			this.delete.setVisible(true);
		}

		public void hideDeleteButton() {
			this.delete.setVisible(false);
		}
		
		/**
		 *  Konstruktor welcher eine Instanz von <code>PVForm</code> erzeugt.
		 */
		PVForm(PValue pv) {
			create(pv);
			if (pv.getProperty().getType() == Type.DATE) {
				GWT.log("Datum!");
				dp = new DatePicker();
				dp.setVisible(false);
				done.setVisible(false);
				dp.addValueChangeHandler(new ValChange());
				done.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent e) {
						dp.setVisible(false);
						done.setVisible(false);
					}
				});
				try {
					val.setText(pv.getDateValue().toString());
				} catch (Exception e) {
					GWT.log("Fehler: " + e.toString());
				}
				this.add(dp);
				this.add(done);
				GWT.log("DatumEnde");
			}
			show();
		}
		
		/**
		 * Klasse welche neue Werte		 für Daten und den Text einer eines PropertyValues.
		 *
		 */
		class ValChange implements ValueChangeHandler<Date> {
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (pval != null) {
					pval.setDateValue(event.getValue());
					val.setText(dateTimeFormat.format(event.getValue()).toString());
				}
			}
		}
		
		/**
		 *  Bezieht das aktuell gesezte PeoprtyValue.
		 */
		PValue getPV() {
			if (this.pval != null) {
				return this.pval;
			} else
				return null;
		}
		
		/**
		 * Setzt ein <code>PValue</code>
		 * 
		 * @param pv
		 */
		void setPV(PValue pv) {
			this.pval = pv;
		}
		
		/**
		 *  Setzt das PValue der Instant neu und setzt ClickHandler.
		 * 
		 * @param pv
		 */
		void create(PValue pv) {
			this.pval = pv;
			val.setText(pv.toString());
			val.addClickHandler(new DateClickHandler(pv, done));
			val.addValueChangeHandler(new PValueChangeHandler<String>(pv));
			delete.addClickHandler(new DeleteClickHandler(this));
		}

		class PValueChangeHandler<String> implements ValueChangeHandler {
			PValue pv;

			PValueChangeHandler(PValue pv) {
				this.pv = pv;
			}
			
			/**
			 *  TODO
			 */
			@Override
			public void onValueChange(ValueChangeEvent event) {
				GWT.log("Änderungen in pValue: " + event.getValue());
				try {
					GWT.log("Pointer: " + pv.getPointer());
					switch (pv.getPointer()) {
					case 1:
						pv.setIntValue(Integer.parseInt((java.lang.String) event.getValue()));
						Window.alert("Int registriert");
						break;
					case 2:
						pv.setStringValue((java.lang.String) event.getValue());
						break;
					case 3:
						GWT.log("Datum wird durch DatePicker gesetzt");
						break;
					case 4:
						pv.setFloatValue(Float.parseFloat((java.lang.String) event.getValue()));
						break;
					default:
						Window.alert("Wert" + pv.toString() + "gespeichert");
					}
				} catch (Exception e) {
					Window.alert("Konnte Wert nicht lesen, bitte im richtigen Format eingeben! " + e.toString());
				}
			}
		}

		class DateClickHandler implements ClickHandler {
			PValue pv;
			Button done;
			
			/**
			 * TODO
			 * @param pv
			 * @param done
			 */
			DateClickHandler(PValue pv, Button done) {
				this.pv = pv;
				this.done = done;
			}

			@Override
			public void onClick(ClickEvent event) {
				if (pv.getPointer() == 3) {
					dp.setVisible(true);
					done.setVisible(true);
				}
			}
		}
	}

	class DeleteClickHandler implements ClickHandler {
		PVForm pv;
		
		/**
		 * TODO
		 * @param pv
		 */
		DeleteClickHandler(PVForm pv) {
			this.pv = pv;
		}

		@Override
		public void onClick(ClickEvent event) {
			editorService.deletePValue(pv.getPV(), contact, new DeletePValueCallback(pv));

		}
	}
}
