package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

	Button deleteContact = new Button("Kontakt löschen");
	Button save = new Button("Änderungen speichern");
	Button existingSharedContactButton;

	VerticalPanel pPanel;
	HorizontalPanel buttonPanel;
	HorizontalPanel addPPanel;

	ArrayList<PropForm> val;
	ArrayList<Property> p;

	ListBox formattype = new ListBox();
	TextBox propertyName = new TextBox();

	public void onLoad() {

		GWT.log("Hallo");
		pPanel = new VerticalPanel();
		buttonPanel = new HorizontalPanel();
		addPPanel = new HorizontalPanel();

		buttonPanel.add(new Button("Kontakt löschen"));
		buttonPanel.add(save);

		save.addClickHandler(new ClickHandler() {
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

		Button addPropertyButton = new Button("Eigenschaft hinzufügen");
		addPropertyButton.addClickHandler(new AddPropertyClickHandler());
		propertyAddBox.setWidget(1, 2, addPropertyButton);
	//	addPanel.add(propertyAddBox);

		// Die notwendigen Standardeigenschaften erstellen, damit PValues eingeordnet
		// werden können

		p.add(new Property("Vorname", Type.STRING, true, 1));
		p.add(new Property("Nachname", Type.STRING, true, 2));
		p.add(new Property("Noch benennen", Type.STRING, true, 3));
		p.add(new Property("Noch benennen", Type.STRING, true, 4));
		p.add(new Property("Noch benennen (und typ zu datum machen)", Type.STRING, true, 5));
		p.add(new Property("Noch benennen", Type.STRING, true, 6));
		p.add(new Property("Noch benennen", Type.STRING, true, 7));
		p.add(new Property("Noch benennen", Type.STRING, true, 8));
		p.add(new Property("Noch benennen", Type.STRING, true, 9));
		p.add(new Property("Noch benennen", Type.STRING, true, 10));

		this.add(pPanel);
		this.add(buttonPanel);
		this.add(addPPanel);

	}

	public void renderContact(ArrayList<PValue> values) {
		for (Property p : p) {
			val.add(new PropForm(p));
		}
		// PValues, die standardeigenschaften sind, den entsprechenden PropForms
		// zuordnen
		for (PValue pv : values) {
			if (pv.getProperty().isStandard()) {
				for (PropForm p : val) {
					if (p.getP().getId() == pv.getProperty().getId()) {
						p.addPValue(pv);
					}
				}
			} else {
				PropForm pnew = new PropForm(pv);
				val.add(pnew);
			}
		}
		// Alle PropForms mit allen PVForms anzeigen lassen
		for (PropForm p : val) {
			pPanel.add(p);
		}

	}

	public void save() {
		// alle pv aus dem PRopArray rausziehen und hier speichern
		ArrayList<PValue> allPV = new ArrayList<PValue>();
		for (PropForm p : val) {
			for (PValue pv : p.getPV()) {
				// neu erstellte pv von alten trennen und inserten(id standardwert oder nicht)
				if (pv.getId() == 0) {
					switch (p.getP().getType()) {
					case STRING:
						editorService.createPValue(p.getP(), pv.getStringValue(), contact, u,
								new CreatePValueCallback());
						break;
					case DATE:
						editorService.createPValue(p.getP(), pv.getDateValue(), contact, u, new CreatePValueCallback());
						break;
					case FLOAT:
						editorService.createPValue(p.getP(), pv.getFloatValue(), contact, u,
								new CreatePValueCallback());
						break;
					case INT:
						editorService.createPValue(p.getP(), pv.getIntValue(), contact, u, new CreatePValueCallback());
						break;
					}
				}
				allPV.add(pv);
			}
		}

		contact.setValues(allPV);
		editorService.updateContact(contact, new AsyncCallback<Contact>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Konnte nicht gespeichert werden!" + caught.getMessage());
			}

			@Override
			public void onSuccess(Contact result) {
				GWT.log("Kontakt erfolgreich gespeichert mit diesen PV:");
				for (PValue pv : result.getValues()) {
					GWT.log(pv.toString());
				}
				e.showContact(result);
			}
		});

		// editorService.updatePValue(val, new UpdatePValueCallback());

	}

	public void setContact(Contact c) {
		this.contact = c;
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
			val.add(pform);
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
				// contactTree.refresh();
			}
		}
	}

	/**
	 * ############################# Klassen, die die Tabellenstruktur
	 * realisieren########################################
	 * 
	 */
	private class PropForm extends HorizontalPanel {
		Property p;
		ArrayList<PVForm> pvForms;
		VerticalPanel pvs;
		Label property;
		Button add = new Button("Ausprägung hinzufügen");

		PropForm(Property p) {
			this.p = p;
			property.setText(p.getLabel());
			this.add(property);
			this.add(pvs);
			this.add(add);
		}

		void addPValue(PValue pv) {
			PVForm p = new PVForm(pv);
			pvForms.add(p);
			pvs.add(p);
		}

		PropForm(PValue pv) {
			this(pv.getProperty());
			addPValue(pv);
			this.add(property);
			this.add(pvs);
			this.add(add);
		}

		Property getP() {
			return this.p;
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
		Button delete = new Button("Ausprägung löschen");
		TextBox val;
		DatePicker dp;

		PVForm(PValue pv){
			dp = new DatePicker();
			create(pv);
			this.add(delete);
			this.add(val);
			this.add(dp);
		}

		PValue getPV() {
			return this.pval;
		}

		//dp.setVisible(false);
//	    dp.addValueChangeHandler(new ValueChangeHandler<Date>() {
//	      public void onValueChange(ValueChangeEvent<Date> event) {
//	        pval.setDateValue(event.getValue());
//	        val.setText(pval.toString());
//	      }
//	    });

		void create(PValue pv) {
			dp = new DatePicker();
			this.pval = pv;
			val.setText(pv.toString());
			CreateClickHandler cch = new CreateClickHandler();
			cch.setPV(pv);
			val.addClickHandler(cch);
			
			
		}
		class CreateClickHandler implements ClickHandler {
			PValue pv;
			void setPV(PValue pv) {
				this.pv=pv;
			}
			@Override
			public void onClick(ClickEvent event) {
				if (pv.getPointer() == 3) {
					dp.setVisible(true);
				}
			}
		}
			/*
			 * val.addChangeHandler(new ChangeHandler() {
			
				@Override
				public void onChange(ChangeEvent event) {
					GWT.log("Änderungen");
					try {
						switch (pv.getPointer()) {
						case 1:
							pv.setIntValue(Integer.parseInt(val.getValue()));
							break;
						case 2:
							pv.setStringValue(val.getValue());
							break;
						case 3:
							Window.alert("Datum auf Standardwert gesetzt, DatePicker noch einfügen");
							pv.setDateValue(new Date(01, 01, 01));
							break;
						case 4:
							pv.setFloatValue(Float.parseFloat(val.getValue()));
							break;
						default:
							Window.alert("Wert" + pv.toString() + "gespeichert");
						}
					} catch (Exception e) {
						Window.alert("Konnte Wert nicht lesen, bitte im richtigen Format eingeben! " + e.toString());
					}
				}
			}); */

		}

	}

