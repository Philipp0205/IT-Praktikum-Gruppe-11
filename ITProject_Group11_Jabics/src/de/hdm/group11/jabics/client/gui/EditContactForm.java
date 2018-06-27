package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
	Editor e;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Grid grid;

	JabicsUser u;
	Contact contact;
	Boolean isNewContact;

	Button deleteContactButton = new Button("Kontakt löschen");
	Button saveButton = new Button("Änderungen speichern");
	Button existingSharedContactButton;

	VerticalPanel pPanel;
	HorizontalPanel buttonPanel;
	HorizontalPanel addPPanel;

	ArrayList<PValue> allPV;

	ArrayList<PropForm> val;
	ArrayList<Property> p;

	ListBox formattype = new ListBox();
	TextBox propertyName = new TextBox();

	public void onLoad() {

		GWT.log("EditCont");
		pPanel = new VerticalPanel();
		buttonPanel = new HorizontalPanel();
		addPPanel = new HorizontalPanel();

		if (isNewContact) {
			saveButton.setText("Neuen Kontakt speichern");
		} else {
			saveButton.setText("Änderungen speichern");
		}

		buttonPanel.add(deleteContactButton);
		buttonPanel.add(saveButton);

		buttonPanel.addStyleName("buttonPanel");

		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				save();
			}
		});

		// GRID-ZEILE 4: Optionen zum hinzufügen einer Eigenschaft
		// Die gesamte Zeile (4) wird ein HorizontalPanel
		Grid propertyAddBox = new Grid(2, 5);

		// in diesem Horizontal Panel gibt es 4 Felder
		// 1. eine Textbox zum Benennen des Eigenschafts-Typs (z.B. "Haarfarbe")
		Label z1l = new Label("Eigenschaftsname:");
		propertyAddBox.setWidget(0, 0, z1l);
		propertyAddBox.setWidget(1, 0, propertyName);
		// (Die TextBox muss für die Clickhandler verfügbar sein und wurde als Attribut
		// deklariert.)

		// 1.1 eine Listbox zum Setzen des Formats
		formattype.addItem("Text");
		formattype.addItem("Datum");
		formattype.addItem("Kommazahl");
		formattype.addItem("Zahl");
		Label z2l = new Label("Art:");
		propertyAddBox.setWidget(0, 1, z2l);
		propertyAddBox.setWidget(1, 1, formattype);

		Button addPropertyButton = new Button("Hinzufügen");
		addPropertyButton.addClickHandler(new AddPropertyClickHandler());
		propertyAddBox.setWidget(1, 2, addPropertyButton);
		addPPanel.add(propertyAddBox);

		p = new ArrayList<Property>();
		// Die notwendigen Standardeigenschaften erstellen, damit PValues eingeordnet
		// werden können
		p.add(new Property("Name", Type.STRING, true, 1));
		p.add(new Property("Vorname", Type.STRING, true, 2));
		p.add(new Property("Mail", Type.STRING, true, 3));
		p.add(new Property("Telefon", Type.STRING, true, 4));
		p.add(new Property("Geburtstag", Type.DATE, true, 5));
		p.add(new Property("Straße", Type.STRING, true, 6));
		p.add(new Property("Haus", Type.STRING, true, 7));
		p.add(new Property("PLZ", Type.STRING, true, 8));
		p.add(new Property("Test", Type.STRING, true, 9));
		p.add(new Property("Test", Type.STRING, true, 10));

		GWT.log("EditContPErstellt");

		this.insert(pPanel, 0);
		this.insert(buttonPanel, 1);
		if (isNewContact) {
			addPPanel.setVisible(false);
		}
		this.insert(addPPanel, 2);
		renderContact(this.contact.getValues());

	}

	public void renderContact(ArrayList<PValue> values) {
		GWT.log("EditContRender6");
		val = new ArrayList<PropForm>();
		for (Property pl : p) {
			val.add(new PropForm(pl));
			GWT.log("EditContRenderfuu2");
		}

		GWT.log("EditContRender7");

		// PValues, die Standardeigenschaften sind, den entsprechenden PropForms
		// zuordnen, ansonsten eine neue PropForm erstellen
		for (PValue pv : values) {
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
				PropForm pnew = new PropForm(pv);
				val.add(pnew);
			}
		}
		GWT.log("EditContRender8");

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

	public void save() {
		GWT.log("6.1 Save Contact");

		// Alle PValues aus der Tabelle ziehen
		ArrayList<PValue> allPV = new ArrayList<PValue>();
		for (PropForm p : val) {
			for (PValue pv : p.getPV()) {
				GWT.log("6.1 Saved PValue " + pv.toString());
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
		for (PValue pv : allPV) {
			if (pv.getProperty().getId() == 1 || pv.getProperty().getId() == 2)
				GWT.log("6.2 Name vorhanden");
			nameExistent = true;
		}

		// alle pv aus dem PRopArray rausziehen und hier speichern

		if (nameExistent) {
			// überprüfen, ob Kontakt ein neuer oder ein bereits bestehender ist
			if (isNewContact) {

				// Es werden alle befüllten PValues übergeben
				editorService.createContact(filledPV, u, new AsyncCallback<Contact>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Neuer Kontakt konnte nicht angelegt werden!" + caught.getMessage());
					}

					@Override
					public void onSuccess(Contact result) {

						GWT.log("5.1 onSuccess");
						GWT.log("5.1" + result.getName());

						ArrayList<PValue> values = result.getValues();
						for (PValue pv : values) {
							GWT.log("5.1" + pv.toString());
						}

						GWT.log("Kontakt erfolgreich gespeichert mit diesen PV:");
						for (PValue pv : result.getValues()) {
							GWT.log(pv.toString());
						}
						setContact(result);
						e.addContactToTree(result);
						addPPanel.setVisible(true);
					}
				});
			} else if (!isNewContact) {

				contact.setValues(filledPV);
				editorService.updateContact(contact, new AsyncCallback<Contact>() {

					@Override
					public void onFailure(Throwable caught) {

						Window.alert("Konnte nicht gespeichert werden!" + caught.getMessage());

						GWT.log("6.1 onFailure" + contact.getName());

					}

					@Override
					public void onSuccess(Contact result) {

						GWT.log("6.1 onSuccess");
						GWT.log("6.1 " + result.getName());

						ArrayList<PValue> values = result.getValues();
						for (PValue pv : values) {
							GWT.log("6.1 " + pv.toString());
						}

						GWT.log("Kontakt erfolgreich gespeichert mit diesen PV:");
						for (PValue pv : result.getValues()) {
							GWT.log(pv.toString());
						}

						e.updateContactInTree(result);
						GWT.log("Show Contact aufrufen");
						e.showContact(result);
					}
				});
			}
		} else if (!nameExistent) {
			Window.alert("Kontakt muss einen Namen haben! Bitte Name und Nachname eingeben.");
		} else
			Window.alert("Massiver Fehler! Das sollte nicht passieren. Kontakt ist weder neu noch alt");
		// editorService.updatePValue(val, new UpdatePValueCallback());
	}

	public void setContact(Contact c) {
		this.contact = c;
	}

	public void setNewContact(Boolean bol) {
		this.isNewContact = bol;
	}

	public void setEditor(Editor e) {
		this.e = e;
	}

	public void setUser(JabicsUser user) {
		GWT.log("usersetz");
		this.u = user;
		GWT.log("usergesetzt: " + u.getEmail());
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

	class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		public void onSuccess(ArrayList<PValue> result) {
			renderContact(result);
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
			GWT.log(result.getTypeInString() + "Hinzufügen neue Property zur Tabelle");
			PropForm pform = new PropForm(result);
			pform.show();
			val.add(pform);
			pPanel.add(pform);
		}
	}

	/**
	 * Diese Callback-Klasse aktualisiert die Ansicht nach der Änderung einer
	 * Eigenschafts- ausprägung.
	 */
	private class UpdatePValueCallback implements AsyncCallback<PValue> {

		public void onFailure(Throwable caugth) {
			Window.alert("Die Änderung ist fehlgeschlagen.");
		}

		@Override
		public void onSuccess(PValue result) {
			// Contacttree muss aktualisiert werden .
			// Conacttree.refresh();
			Window.alert("Wert geändert");
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
				GWT.log("PValue erstellt ");
			}
		}
	}

	/**
	 * Diese Callback-Klasse aktualisiert die Ansicht nach der Löschung einer
	 * Eigenschafts- ausprägung.
	 */
	class DeletePValueCallback implements AsyncCallback<Void> {
		private PValue pvalue = null;
		PVForm pvform;

		public DeletePValueCallback(PVForm pv) {
			this.pvform = pv;
		}

		public void onFailure(Throwable caugth) {
			Window.alert("PValue konnte nicht gelöscht werden");
		}

		@Override
		public void onSuccess(Void result) {
			Window.alert("Erfolgreich gelöscht");
			pvform.setVisible(false);
		}
	}

	/**
	 * ############################# Klassen, die die Tabellenstruktur
	 * realisieren########################################
	 */
	private class PropForm extends HorizontalPanel {
		Property p;
		ArrayList<PVForm> pvForms = new ArrayList<PVForm>();
		VerticalPanel pvPanel = new VerticalPanel();
		Label property;
		Button addButton = new Button("hinzufügen");

		/**
		 * Anzeige der PForm, bzw dieser sagen, sich zu zeigen. Fügt alle Widgets in der
		 * Form dem Horizontal Panel hinzu. Durch das Hinzufügen der PVForms zum pvPanel
		 * werden auch alle PVForms angezeigt.
		 */
		void show() {
			GWT.log(property.getText() + " zeigt sich");
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
			property.setWidth("80px");

			this.add(property);
			pvPanel.setWidth("200px");
			this.insert(pvPanel, 1);
			addButton.setWidth("100px");
			this.add(addButton);

		}

		PropForm(Property pp) {
			GWT.log("newPropForm");
			this.p = pp;
			property = new Label(p.getLabel());
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
		 * @param PValue
		 *            pv
		 */
		void addPValue(PValue pv) {
			PVForm pvform = new PVForm(pv);
			pvForms.add(pvform);
			pvPanel.add(pvform);
		}

		/**
		 * Löschen des initial erstellten PVForms und ersetzen durch ein befülltes. Darf
		 * nur von renderContact aufgerufen werden
		 * 
		 * @param PValue
		 *            pv
		 */
		void replacePValue(PValue pv) {
			PVForm pvform = new PVForm(pv);
			pvForms = new ArrayList<PVForm>();
			pvForms.add(pvform);
			pvPanel.add(pvform);
		}

		PropForm(PValue pv) {
			this(pv.getProperty());
			addPValue(pv);
		}

		Property getProperty() {
			return this.p;
		}

		ArrayList<PVForm> getPVForms() {
			return this.pvForms;
		}

		ArrayList<PValue> getPV() {
			ArrayList<PValue> res = new ArrayList<PValue>();
			for (PVForm pf : pvForms) {
				res.add(pf.getPV());
			}
			return res;
		}
	}

	private class PVForm extends HorizontalPanel {
		PValue pval;
		Button delete = new Button("löschen");
		TextBox val = new TextBox();
		DatePicker dp;

		public void show() {
			this.add(val);
			this.add(delete);
		}

		PVForm(PValue pv) {
			create(pv);
			if (pv.getProperty().getType() == Type.DATE) {
				GWT.log("Datum!");
				dp = new DatePicker();
				dp.setVisible(false);
				dp.addValueChangeHandler(new ValChange());
				dp.setValue(new Date(), true);
				this.add(dp);
				GWT.log("DatumEnde");
			}
			show();
		}

		class ValChange implements ValueChangeHandler<Date> {
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (pval != null) {
					pval.setDateValue(event.getValue());
					val.setText(pval.toString());
				}
			}
		}

		PValue getPV() {
			return this.pval;
		}

		void create(PValue pv) {
			GWT.log("createPValueForm für ");
			GWT.log(pv.toString());
			this.pval = pv;
			val.setText(pv.toString());
			val.addClickHandler(new DateClickHandler(pv));
			val.addValueChangeHandler(new PValueChangeHandler<String>(pv));
			delete.addClickHandler(new DeleteClickHandler(this));
		}

		class PValueChangeHandler<String> implements ValueChangeHandler {
			PValue pv;

			PValueChangeHandler(PValue pv) {
				this.pv = pv;
			}

			@Override
			public void onValueChange(ValueChangeEvent event) {
				GWT.log("Änderungen in pValue: " + event.getValue());
				try {
					GWT.log("Pointer: " + pv.getPointer());
					switch (pv.getPointer()) {
					case 1:
						pv.setIntValue(Integer.parseInt((java.lang.String) event.getValue()));
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

			DateClickHandler(PValue pv) {
				this.pv = pv;
			}

			@Override
			public void onClick(ClickEvent event) {
				if (pv.getPointer() == 3) {
					dp.setVisible(true);
				}
			}
		}
	}

	class DeleteClickHandler implements ClickHandler {
		PVForm pv;

		DeleteClickHandler(PVForm pv) {
			this.pv = pv;
		}

		@Override
		public void onClick(ClickEvent event) {
			editorService.deletePValue(pv.getPV(), contact, new DeletePValueCallback(pv));

		}
	}
}
