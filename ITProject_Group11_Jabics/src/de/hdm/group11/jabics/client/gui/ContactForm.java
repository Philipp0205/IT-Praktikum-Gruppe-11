package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.User;

/**
 * 
 * Dieses Formular realisiert die Darstellung von <code>Contact</code> Objekten eines Nutzers auf der grafischen BenutzeroberflÃ¤che.
 * Sie stellt fÃ¼r den Nutzer alle notwendigen Methoden zur Verwaltung von Kontakten zur verfÃ¼gung. 
 * 
 * @author Brase
 * @author Ilg
 *
 */

public class ContactForm extends VerticalPanel {
	/**
	 * Struktur von
	 * @author Christian Rathke
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Ilg
     * 
     * 
     * @see
	 */

	EditorServiceAsync editorAdmin = ClientsideSettings.getEditorService();
	
	User userToDisplay = null;
	Contact contactToDisplay = null;
	
	public void onLoad() {
		
		super.onLoad();

		createForm(contactToDisplay);
		
		editorAdmin.getUserById(userToDisplay, new CreateOwnerCallback()); //Zeile von Alex G.
		
	}
	
	
	private void createForm(Contact contact) {
		
		/** 
		 * Ziel ist es, ein Grid mit 5 Zeilen und nur einer Spalte zu erstellen
		 * Die erste Zeile [des Grids] enthält den Namen des Fensters ("Kontakt-Editor")
		 * 
		 * Die zweite Zeile enthält den Namen des Kontakts
		 * 
		 * Die dritte Zeile enthält wiederum ein Grid, das
		 * 		für jede Eigenschaft eine Zeile enthält, die jeweils Spalten für
		 *			A) den Eigenschaftstyp (z.B. 'E-Mail'),
		 *			B) die Ausprägung (z.B. 'xyz@hdm.de'),
		 *			C) einen Button zum Löschen der Eigenschaft,
		 *			D) einen Button zum Speichern von Änderungen
		 *		enthält. Zeilen des Grids = Anzahl der Eigenschaften (Name und Modified / Create-Date abgezogen)
		 *
		 *Die vierte Zeile enthält wiederum ein Grid, das die Möglichkeit zum
		 *Anlegen einer neuen Eigenschaft enthält. Dieses Grid hat eine Zeile und je eine Spalte
		 *		- zur Auswahl des Eigenschaftstyps (s.o.)
		 *		- zur Eingabe der Ausprägung	(s.o.)
		 *		- für einen Button zum hinzufügen der Eingaben
		 *
		 *Die fünfte Zeile enthält einen Button zum löschen des Kontaktes 
		 *TODO (ggf. auch einen Button zum erstellen eines neuen Kontaktes???)
		 */
		
		//Erstellen des Haupt-Grids
		Grid userInformationGrid = new Grid(5, 1);
		
		//GRID-ZEILE 3: Vergabe des Fensternamens
		Label formName = new Label("Kontakt-Editor");
		userInformationGrid.setWidget(0, 0, formName);
		
		//GRID-ZEILE 2: Holen des Kontakt-Namens
		Label contactName = new Label(contact.getName());
		userInformationGrid.setWidget(1, 0, contactName);		

		//GRID-ZEILE 3: Erstellen des 'Eigenschafts-Grids'
		Grid propertyGrid = createPropertyGrid(contact);
		userInformationGrid.setWidget(2, 0, propertyGrid);		

		//GRID-ZEILE 4: Optionen zum hinzufügen einer Eigenschaft
		//Die gesamte Zeile (4) wird ein HorizontalPanel
			HorizontalPanel propertyAddBox = new HorizontalPanel();
			//in diesem Horizontal Panel gibt es 4 Felder
			// 1. eine Listbox zum Auswählen des Eigenschafts-Typs (z.B. "Mail")
			ListBox selectProperty = new ListBox();
			propertyAddBox.add(selectProperty);
			// 2. ein Eingabefeld, um die Eigenschaftsausprägung festzulegen (z.B. "xyz@hdm...")
			TextBox propertyValue = new TextBox();
			propertyAddBox.add(propertyValue);
			// 3. einen Button zum Hinzufügen
		    Button addPropertyButton = new Button("Eigenschaft hinzufügen") 
		    /*, new ClickHandler() {
		        public void onClick(ClickEvent event) {
		          Window.alert("TODO Clickhandler")
		    }; */
		    propertyAddBox.add(addPropertyButton);
		    
		    //hinzufügen von Zeile 4 zum Hauptgrid
		    userInformationGrid.setWidget(3, 0, propertyAddBox);
		
		    
		//GRID-ZEILE 5: 
		    HorizontalPanel contactDeleteBox = new HorizontalPanel();
		    Label deleteQuestion = new Label("Wollen Sie diesen Kontakt löschen?");
		    contactDeleteBox.add(deleteQuestion);
		    
		    Button deleteContactButton = new Button("Kontakt löschen") 
		    /*, new ClickHandler() {
		        public void onClick(ClickEvent event) {
		          Window.alert("TODO Clickhandler")
		    }; */
		    contactDeleteBox.add(deleteContactButton);
		    
		    //hinzufügen von Zeile 4 zum Hauptgrid
		    userInformationGrid.setWidget(4, 0, contactDeleteBox);	
		
	}

	/**
	 * Methode, die alle Properties eines Kontakts anzeigen soll
	 * 
	 * TODO ACHTUNG, bisher inkorrekte Implementierung ohne ListDataProvider
	 * 
	 * 
	 * @param contact
	 */
	private Grid createPropertyGrid(Contact contact) {
		//Erstellen der Buttons
		Button deletePropertyButton = new Button("Eintrag loeschen");
		Button savePropertyButton = new Button("Eintrag speichern");
		
		//Erstellen des Eigenschafts-DataGrids
		DataGrid propertyDataGrid = new DataGrid();
		
		//Alle Eigenschaften des Kontakts werden 'geladen'
		ArrayList<PValue> contactProperties = contact.getValues();
		
		//Für jede Eigenschaft wird nun eine Zeile angelegt
	    for (i=0; i<contactProperties.size(); i++) {
	        //TODO Hier ListDataProvider integrieren??
	    	
	    	//Textbox erstellen, die den Wert der Eigenschaft hat
	    	TextBox textBox = new TextBox(contactProperties.get(i));
	    	
	    	//Textbox dem DatenGrid hinzufügen
	    	Column propColumn = new Column(textBox);

	    }
	    
	    	//abschließend der Zeile die zwei Buttons hinzufügen
	        propertyDataGrid.addColumn(deletePropertyButton, "");
	        propertyDataGrid.addColumn(savePropertyButton, "");
	        
	        //Datenmodell aktualisieren
	        propertyDataGrid.refresh();
	    }
		
		
	}
	
	
	private class CreateOwnerCallback implements AsyncCallback<User> {
		@Override
		public void onFailure(Throwable caugth) {
		}
		
		@Override
		public void onSuccess(User user) {
			if(user != null) {
				editorAdmin.getUserById(userToDisplay.getId(), new updateUserByIdCallback());
			}
		}
	}
	
	private class updateUserByIdCallback {
		
	}
	
	
	
	
}
