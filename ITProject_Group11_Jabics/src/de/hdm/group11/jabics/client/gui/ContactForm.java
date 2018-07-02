package de.hdm.group11.jabics.client.gui;

import java.util.Date;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.*;

/**
 * 
 * Dieses Formular realisiert die Darstellung von <code>Contact</code> Objekten
 * eines Nutzers auf der grafischen Benutzeroberfläche. Sie stellt für den
 * Nutzer alle notwendigen Methoden zur Verwaltung von Kontakten zur verfügung.
 * 
 * @author Brase
 * @author Ilg
 *
 */
public class ContactForm extends VerticalPanel {

	/**
	 * Struktur von
	 * 
	 * @author Christian Rathke
	 * 
	 *         Angepasst von
	 * @author Brase
	 * @author Ilg
	 * 
	 * 
	 * @see
	 */
	EditorAdmin e;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	// TODO USER richtig setzen (wird hier nur instanziiert, dass er nicht
	// undefinded ist)
	JabicsUser u = new JabicsUser();
	Contact contactToDisplay = null;
	PValue selectedPValue = null;
	TreeViewMenu contacttree = null;

	// Widgets deren Inhalte variabel sind werden als Attribute angelegt.

	Button deleteContactButton = new Button("Kontakt löschen");
	Button shareContactButton;
	Button existingSharedContactButton;
	Grid contactGrid = new Grid();
	ArrayList<PValue> checkedPV = new ArrayList<PValue>();
	ListBox formattype = new ListBox();
	TextBox propertyName = new TextBox();
	TextBox pValueName = new TextBox();
	Label contactName = new Label();
	DatePicker datePicker = new DatePicker();
	Date selectedDate = new Date();

	public void onLoad() {

		super.onLoad();
		// Erstellen des Haupt-Grids
		Grid userInformationGrid = new Grid(6, 3);
		this.add(userInformationGrid);

		// GRID-ZEILE 1: Vergabe des Fensternamens
		Label formName = new Label("Kontakt-Bearbeiten");

		userInformationGrid.setWidget(0, 0, formName);

		// GRID-ZEILE 2: Holen des Kontakt-Namens
		userInformationGrid.setWidget(1, 0, contactName);

		// GRID-ZEILE 3: Einfügen des 'Kontakt-Grids'
		userInformationGrid.setWidget(2, 0, contactGrid);

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

		// 2. ein Eingabefeld, um die konkrete Eigenschaftsausprägung anzugeben (z.B.
		// "blond")
		Label z3l = new Label("Wert:");
		propertyAddBox.setWidget(0, 2, z3l);
		propertyAddBox.setWidget(1, 2, pValueName);
		// 3. einen Button zum Hinzufügen
		Button addPropertyButton = new Button("Eigenschaft hinzufügen");
		addPropertyButton.addClickHandler(new AddPropertyClickHandler());
		propertyAddBox.setWidget(1, 3, addPropertyButton);

		// 4 einen Datepicker
		// Set the value in the text box when the user selects a date
		propertyAddBox.setWidget(1, 4, datePicker);
		datePicker.setVisible(false);
		formattype.addClickHandler(new FormatClickHandler());
		if (formattype.getSelectedItemText() == "Datum") {
			datePicker.setVisible(true);
		}
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				selectedDate = event.getValue();
			}
		});
		// Set the default value
		datePicker.setValue(new Date(), true);

		// hinzufügen von Zeile 4 zum Hauptgrid
		userInformationGrid.setWidget(3, 0, propertyAddBox);

		// GRID-ZEILE 5.1:
		deleteContactButton.addClickHandler(new DeleteContactClickHandler());

		userInformationGrid.setWidget(4, 1, deleteContactButton);

		userInformationGrid.setStyleName("userInformationGrid");

		// GRID-Zeile 5.2
		existingSharedContactButton = new Button("Freigegeben an");
		userInformationGrid.setWidget(5, 0, existingSharedContactButton);
		existingSharedContactButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				GWT.log(contactToDisplay.getName());
				e.showExistingContactCollab(contactToDisplay);
			}
		});
	}

	public void setEditor(EditorAdmin e) {
		this.e = e;

	}

	public void setUser(JabicsUser user) {
		GWT.log("usersetz");
		// GWT.log("usergesetzt: " + user.getEmail());
		JabicsUser currentUser = new JabicsUser();
		currentUser.setEmail("stahl.alexander@live.de");
		currentUser.setId(1);
		currentUser.setUsername("AlexanderStahl");
		this.u = currentUser;
		GWT.log("usergesetzt:");
		GWT.log("usergesetzt2: " + u.getEmail());
	}

	/**
	 * Im Folgenden Code werden Clickhandler und Asynchrone Methodenaufrufe für die
	 * Operationen Editieren, Löschen oder Teilen eines <code>Contact</code> Objekts
	 * implementiert.
	 * 
	 * @author Brase
	 * @author Ilg
	 */

	private class FormatClickHandler implements ClickHandler {
		@Override

		public void onClick(ClickEvent event) {

			if (formattype.getSelectedItemText() == "Datum") {
				datePicker.setVisible(true);
			} else {
				datePicker.setVisible(false);
			}
		}
	}

	/**
	 * Diese Klasse realisiert einen Clickhandler für den DeleteContactButton. Beim
	 * aktivieren des Buttons <code>deleteContactButton</code> wird auf dem Server
	 * die Methode <code>deleteContact</code> aufgerufen.
	 */
	private class DeleteContactClickHandler implements ClickHandler {
		@Override

		public void onClick(ClickEvent event) {
			if (contactToDisplay == null) {
				Window.alert("Kein Kontakt ausgewählt");
			} else {

				editorService.deleteContact(contactToDisplay, u, new deleteContactCallback(contactToDisplay));
			}
		}
	}

	/**
	 * Diese Callback-Klasse speichert den übergebenen Kontakt mit einem
	 * Konstruktor. Nach erfolgreichem Methodenablauf aktualisiert sich die Ansicht.
	 */
	class deleteContactCallback implements AsyncCallback<Void> {

		private Contact contact = null;

		deleteContactCallback(Contact c) {
			contact = c;

		}

		public void onFailure(Throwable caugth) {
		}

		@Override
		public void onSuccess(Void result) {
			if (contact != null) {
				// Contacttree.removeContact(contact);
			}
		}
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
	// TODO Erst Property erstellen dann PValue: Überlegen ob erstellend er PValue
	// in der
	// OnSuccess der Property sinnvoll ist.

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
				GWT.log(result.getTypeInString());
				switch (formattype.getSelectedItemText()) {
				case "Text":
					editorService.createPValue(result, pValueName.getText(), contactToDisplay, u,
							new CreatePValueCallback());
					break;
				case "Datum":
					Window.alert("Datum auf Standardwert gesetzt, DatePicker noch einfügen");
					Date ld = selectedDate;
					// Datum muss im folgenden Format eingegeben werden: 2018-06-15;
					editorService.createPValue(result, ld, contactToDisplay, u, new CreatePValueCallback());
					break;
				case "Kommazahl":
					editorService.createPValue(result, Float.valueOf(pValueName.getText()), contactToDisplay, u,
							new CreatePValueCallback());
					break;
				case "Zahl":
					editorService.createPValue(result, Integer.valueOf(pValueName.getText()), contactToDisplay, u,
							new CreatePValueCallback());
					break;
				}

			}

		}
	}

	/**
	 * Diese Callback-Klasse aktualisiert die Ansicht nach erfolgreichem Erstellen
	 * einer Eigenschaftsausprägung..
	 */
	public class CreatePValueCallback implements AsyncCallback<PValue> {

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

	public void setCurrentContact(Contact c) { // JabicsUser u) {
		if (c != null) {
			this.contactToDisplay = c;
			// this.u = u;
			deleteContactButton.setEnabled(true);
			contactName.setText(contactToDisplay.getName());
			GWT.log("currentUser: " + u.getEmail() + u.getId());
			editorService.getPValueOf(c, u, new GetPValuesCallback());

		} else {
			contactToDisplay = null;
			deleteContactButton.setEnabled(false);
			shareContactButton.setEnabled(false);
			existingSharedContactButton.setEnabled(false);
		}
	}

	/**
	 * Diese Callback-Klasse erstellt ein sich dynamisch anpassendes Grid. Labels,
	 * Textboxen, Buttons und Checkboxen, sowie deren Clickhandler werden dynamisch
	 * für jede Eigenschaftsausprägung eines <code>Contact</code> Objekts erstellt.
	 */

	TextBox[] pValueTextBox;

	public class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		public void onSuccess(ArrayList<PValue> result) {

			if (result != null) {

				GWT.log("huhu1");
				// Die ArrayList mit ausgewählten PValues wird zurückgesetzt
				checkedPV.clear();

				Label[] propertyLabels = new Label[result.size()];
				pValueTextBox = new TextBox[result.size()];
				Button[] saveButton = new Button[result.size()];
				Button[] deleteButton = new Button[result.size()];
				DeleteClickHandler[] cl = new DeleteClickHandler[result.size()];
				SaveClickHandler[] scl = new SaveClickHandler[result.size()];

				for (int i = 0; i < result.size(); i++) {
					propertyLabels[i] = new Label(result.get(i).getProperty().getLabel() + ":");
					pValueTextBox[i] = new TextBox();
					pValueTextBox[i].setText(result.get(i).getStringValue());
					saveButton[i] = new Button("Save");
					deleteButton[i] = new Button("Delete");
					contactGrid.resize(result.size() + 1, 5);
					cl[i] = new DeleteClickHandler();
					cl[i].seti(i);
					deleteButton[i].addClickHandler(cl[i]);
					scl[i] = new SaveClickHandler();
					scl[i].seti(i);
					scl[i].settb(pValueTextBox);
					saveButton[i].addClickHandler(scl[i]);

					for (int j = 0; j < propertyLabels.length; j++) {

						contactGrid.setWidget(j, 0, propertyLabels[j]);
						contactGrid.setWidget(j, 1, pValueTextBox[j]);
						contactGrid.setWidget(j, 2, saveButton[j]);
						contactGrid.setWidget(j, 3, deleteButton[j]);
					}
				}

			}

		}

		class DeleteClickHandler implements ClickHandler {
			int i;
			ArrayList<PValue> result;

			void seti(int i) {
				this.i = i;
			}

			void setal(ArrayList<PValue> result) {
				this.result = result;
			}

			@Override
			public void onClick(ClickEvent event) {
				editorService.deletePValue(result.get(i), contactToDisplay, new deletePValueCallback(result.get(i)));
			}
		}

		class SaveClickHandler implements ClickHandler {
			int i;
			ArrayList<PValue> result;
			TextBox[] tb;

			void seti(int i) {
				this.i = i;
			}

			void setal(ArrayList<PValue> result) {
				this.result = result;
			}

			void settb(TextBox[] tb) {
				this.tb = tb;
			}

			@Override
			public void onClick(ClickEvent event) {
				PValue newPV = result.get(i);

				switch (result.get(i).getPointer()) {
				case 1:
					newPV.setIntValue(Integer.valueOf(tb[i].getText()));
					break;
				case 2:
					// newPV.setStringValue(pValueTextBox.get(pointer).getText());
					GWT.log(tb[i].getText());
					break;
				case 3:
					Date ld = selectedDate;
					// Datum muss im folgenden Format eingegeben werden: 2018-06-15;
					// neuer Datepicker
					newPV.setDateValue(null);
					break;
				case 4:
					newPV.setFloatValue(Float.parseFloat(tb[i].getValue()));
					break;
				default:
				}
				editorService.updatePValue(newPV, new UpdatePValueCallback());
			}
		}
	}

	/**
	 * Diese Callback-Klasse aktualisiert die Ansicht nach der Löschung einer
	 * Eigenschafts- ausprägung.
	 */
	class deletePValueCallback implements AsyncCallback<Void> {

		private PValue pvalue = null;

		deletePValueCallback(PValue pv) {
			pvalue = pv;
		}

		public void onFailure(Throwable caugth) {
		}

		@Override
		public void onSuccess(Void result) {
			if (pvalue != null) {
				// update Contact bzw. Contacttree
			}
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
			
			if (result != null) {
				
				// Contacttree muss aktualisiert werden .
				// Conacttree.refresh();
				Window.alert("Wert geändert");
				
			}

		}
	}
}
