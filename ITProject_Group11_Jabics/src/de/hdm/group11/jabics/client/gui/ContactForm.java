package de.hdm.group11.jabics.client.gui;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Jabics;
import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.shared.EditorService;
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
	Editor e;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	JabicsUser u = null;
	Contact contactToDisplay = null;
	PValue selectedPValue = null;
	TreeViewMenu contacttree = null;

	// Widgets deren Inhalte variabel sind werden als Attribute angelegt.

	Button deleteContactButton = new Button("Kontakt löschen");
	Button shareContactButton;
	Grid contactGrid = new Grid();
	ArrayList<PValue> checkedPV = new ArrayList<PValue>();
	ListBox formattype = new ListBox();
	TextBox propertyName = new TextBox();
	TextBox pValueName = new TextBox();
	Label contactName = new Label();

	public void onLoad() {

		super.onLoad();

		// Erstellen des Haupt-Grids
		Grid userInformationGrid = new Grid(6, 1);

		// GRID-ZEILE 1: Vergabe des Fensternamens
		Label formName = new Label("Kontakt-Editor");
		userInformationGrid.setWidget(0, 0, formName);

		// GRID-ZEILE 2: Holen des Kontakt-Namens

		userInformationGrid.setWidget(1, 0, contactName);

		// GRID-ZEILE 3: Einfügen des 'Kontakt-Grids'
		userInformationGrid.setWidget(2, 0, contactGrid);

		// GRID-ZEILE 4: Optionen zum hinzufügen einer Eigenschaft
		// Die gesamte Zeile (4) wird ein HorizontalPanel
		HorizontalPanel propertyAddBox = new HorizontalPanel();
		// in diesem Horizontal Panel gibt es 4 Felder
		// 1. eine Textbox zum Benennen des Eigenschafts-Typs (z.B. "Haarfarbe")
		propertyAddBox.add(propertyName);
		// (Die TextBox muss für die Clickhandler verfügbar sein und wurde als Attribut
		// deklariert.)

		// 1.1 eine Listbox zum Setzen des Formats
		formattype.addItem("Text");
		formattype.addItem("Datum");
		formattype.addItem("Kommazahl");
		formattype.addItem("Zahl");
		propertyAddBox.add(formattype);

		// 2. ein Eingabefeld, um die konkrete Eigenschaftsausprägung anzugeben (z.B.
		// "blond")
		propertyAddBox.add(pValueName);

		// 3. einen Button zum Hinzufügen
		Button addPropertyButton = new Button("Eigenschaft hinzufügen");
		addPropertyButton.addClickHandler(new AddPropertyClickHandler());
		propertyAddBox.add(addPropertyButton);

		// hinzufügen von Zeile 4 zum Hauptgrid
		userInformationGrid.setWidget(3, 0, propertyAddBox);

		// GRID-ZEILE 5:

		shareContactButton = new Button("Kontakt teilen");
		shareContactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				e.showContactCollab(contactToDisplay);
			}
		});

		userInformationGrid.setWidget(4, 0, shareContactButton);
		// GRID-ZEILE 5.1:
		deleteContactButton.addClickHandler(new DeleteContactClickHandler());
		userInformationGrid.setWidget(4, 1, deleteContactButton);
	}

	public void setEditor(Editor e) {
		this.e = e;
	}
	
	/**
	 * Im Folgenden Code werden Clickhandler und Asynchrone Methodenaufrufe für die
	 * Operationen Editieren, Löschen oder Teilen eines <code>Contact</code> Objekts
	 * implementiert.
	 * 
	 * @author Brase
	 * @author Ilg
	 */

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

			case "Datum":
				editorService.createProperty(propertyName.getText(), Type.DATE, new CreatePropertyCallback());

			case "Kommazahl":
				editorService.createProperty(propertyName.getText(), Type.FLOAT, new CreatePropertyCallback());

			case "Zahl":
				editorService.createProperty(propertyName.getText(), Type.INT, new CreatePropertyCallback());
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
			editorService.createPValue(result, pValueName.getText(), contactToDisplay, u, new CreatePValueCallback());
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
	 * private class DeletePValueClickHandler implements ClickHandler {
	 * 
	 * @Override public void onClick(ClickEvent event) {
	 * 
	 *           if(contactToDisplay == null) { Window.alert("Kein Kontakt
	 *           ausgewählt"); }else { editorService.deletePValue(selectedPValue,
	 *           new deletePValueCallback(selectedPValue)); } } }
	 * 
	 */
	/**
	 * Diese Methode wird im TreeViewMenu aufgerufen und übergibt der ContactForm
	 * den anzuzeigenden Kontakt.
	 * 
	 * @param u
	 *            das <code>User</code> Objekt, zu welchem der Kontkt gehört.
	 * @param c
	 *            der Kontakt welcher angezeigt werden soll.
	 */
	public void setCurrentContact(Contact c) { // JabicsUser u) {
		if (c != null) {
			this.contactToDisplay = c;
			// this.u = u;
			deleteContactButton.setEnabled(true);
			contactName.setText(contactToDisplay.getName());

			editorService.getPValueOf(c, u, new GetPValuesCallback());
		} else {
			contactToDisplay = null;
			deleteContactButton.setEnabled(false);
			shareContactButton.setEnabled(false);
		}
	}

	/**
	 * Diese Callback-Klasse erstellt ein sich dynamisch anpassendes Grid. Labels,
	 * Textboxen, Buttons und Checkboxen, sowie deren Clickhandler werden dynamisch
	 * für jede Eigenschaftsausprägung eines <code>Contact</code> Objekts erstellt.
	 */
	class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>> {
		public void onFailure(Throwable caught) {
			Window.alert("Fehler in GetPValuesCallback");

		}

		public void onSuccess(ArrayList<PValue> result) {

			// Die ArrayList mit ausgewählten PValues wird zurückgesetzt
			checkedPV.clear();

			Label[] propertyLabels = new Label[result.size()];
			TextBox[] pValueTextBox = new TextBox[result.size()];
			Button[] saveButton = new Button[result.size()];
			Button[] deleteButton = new Button[result.size()];

			for (int i = result.size(); i > 0; i--) {

				int pointer = i;

				PValue currentPV = result.get(pointer);

				propertyLabels[pointer] = new Label(result.get(pointer).getProperty().toString());
				pValueTextBox[pointer] = new TextBox();
				pValueTextBox[pointer].setText(result.get(pointer).toString());
				saveButton[pointer] = new Button("Save");

				saveButton[pointer].addClickHandler(new ClickHandler() {
					// TODO Bisher noch nicht funktional
					public void onClick(ClickEvent event) {

						int currentID = currentPV.getPropertyId();
						PValue newPV = new PValue(result.get(pointer).getProperty());

						switch (currentPV.getPointer()) {
						case 1:
							newPV.setIntValue(Integer.parseInt(pValueTextBox[pointer].getValue()));
						case 2:
							newPV.setStringValue(pValueTextBox[pointer].getValue().toString());
						case 3:
							// Das Datum muss folgendermaßen eingegeben werden: 2015-08-04T10:11:30
							newPV.setDateValue(LocalDateTime.parse(pValueTextBox[pointer].getValue()));
						case 4:
							newPV.setFloatValue(Float.parseFloat(pValueTextBox[pointer].getValue()));
						default:
						}
						editorService.updatePValue(newPV, new UpdatePValueCallback());

						Window.alert("Wert" + pValueTextBox[pointer].getValue().toString() + "gespeichert");
					}
				});

				deleteButton[pointer] = new Button("Delete");

				deleteButton[pointer].addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {

						if (contactToDisplay == null) {
							Window.alert("Kein Kontakt ausgewählt");
						} else {
							editorService.deletePValue(currentPV, new deletePValueCallback(currentPV));
						}
					}
				});

				contactGrid.resize(result.size(), 4);
			}
			for (int j = propertyLabels.length; j > 0; j--) {

				contactGrid.setWidget(j, 0, propertyLabels[j]);
				contactGrid.setWidget(j, 1, pValueTextBox[j]);
				contactGrid.setWidget(j, 2, saveButton[j]);
				contactGrid.setWidget(j, 3, deleteButton[j]);
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
			// Contacttree muss aktualisiert werden .
			// Conacttree.refresh();
			Window.alert("Wert geändert");
		}
	}

}
