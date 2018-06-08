package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;

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

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Jabics;
import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.shared.EditorService;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.User;

/**
 * 
 * Dieses Formular realisiert die Darstellung von <code>Contact</code> Objekten eines Nutzers auf der grafischen Benutzeroberfläche.
 * Sie stellt für den Nutzer alle notwendigen Methoden zur Verwaltung von Kontakten zur verfügung. 
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

	EditorServiceAsync editorService = ClientsideSettings.getEditorService();
	
	User userToDisplay = null;
	Contact contactToDisplay = null;

	//CustomDetailsViewModal cdvm = null;
	
	TreeViewMenu Contacttree = new TreeViewMenu();
	
	/** 
	 * Im folgenden Code werden die Namen der Standardeigenschaften zur Anzeige gebracht.
	 */
//	Label userInformationLabel = new Label("User: ");
	Label contactFirstNameLabel = new Label("Vorname: ");
	Label contactLastNameLabel = new Label("Nachname: ");
	Label contactBirthdayLabel = new Label("Geburtstag: ");
	Label contactEmailLabel = new Label("E-Mail: ");
	Label contactTelefonLabel = new Label("Telefon: ");
	Label contactStreetLabel = new Label("Straße: ");
	Label contactZipLabel = new Label("Postleitzahl: ");
	Label contactOrtLabel = new Label("Ort: ");
	
	/**
	 * Im nächsten Schritt werden die Labels für die konkreten Eigenschaftsausprägungen instanziiert. 
	 */
	
//	Label userSetInformationLabel = new Label();
	Label contactSetFirstNameLabel = new Label();
	Label contactSetLastNameLabel = new Label();
	Label contactSetBirthdayLabel = new Label();
	Label contactSetEmailLabel = new Label();
	Label contactSetTelefonLabel = new Label();
	Label contactSetStreetLabel = new Label();
	Label contactSetZipLabel = new Label();
	Label contactSetOrtLabel = new Label();
	
//	Button createContactButton = new Button("Kontakt erstellen");
	Button deleteContactButton = new Button("Kontakt löschen");
	Button editContactButton = new Button("Kontakt editieren");
	Button shareContactButton = new Button("Kontakt teilen");
	

	
	public void onLoad() {
		
		super.onLoad();

		createForm(contactToDisplay);
		
		editorAdmin.getUserById(userToDisplay, new CreateOwnerCallback()); //Zeile von Alex G.
		
	}
	
	
	private void createForm(Contact contact) {
		
		/** 
		 * Ziel ist es, ein Grid mit 5 Zeilen und nur einer Spalte zu erstellen
		 * Die erste Zeile [des Grids] enth�lt den Namen des Fensters ("Kontakt-Editor")
		 * 
		 * Die zweite Zeile enth�lt den Namen des Kontakts
		 * 
		 * Die dritte Zeile enth�lt wiederum ein Grid, das
		 * 		f�r jede Eigenschaft eine Zeile enth�lt, die jeweils Spalten f�r
		 *			A) den Eigenschaftstyp (z.B. 'E-Mail'),
		 *			B) die Auspr�gung (z.B. 'xyz@hdm.de'),
		 *			C) einen Button zum L�schen der Eigenschaft,
		 *			D) einen Button zum Speichern von �nderungen
		 *		enth�lt. Zeilen des Grids = Anzahl der Eigenschaften (Name und Modified / Create-Date abgezogen)
		 *
		 *Die vierte Zeile enth�lt wiederum ein Grid, das die M�glichkeit zum
		 *Anlegen einer neuen Eigenschaft enth�lt. Dieses Grid hat eine Zeile und je eine Spalte
		 *		- zur Auswahl des Eigenschaftstyps (s.o.)
		 *		- zur Eingabe der Auspr�gung	(s.o.)
		 *		- f�r einen Button zum hinzuf�gen der Eingaben
		 *
		 *Die f�nfte Zeile enth�lt einen Button zum l�schen des Kontaktes 
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

		//GRID-ZEILE 4: Optionen zum hinzuf�gen einer Eigenschaft
		//Die gesamte Zeile (4) wird ein HorizontalPanel
			HorizontalPanel propertyAddBox = new HorizontalPanel();
			//in diesem Horizontal Panel gibt es 4 Felder
			// 1. eine Listbox zum Ausw�hlen des Eigenschafts-Typs (z.B. "Mail")
			ListBox selectProperty = new ListBox();
			propertyAddBox.add(selectProperty);
			// 2. ein Eingabefeld, um die Eigenschaftsauspr�gung festzulegen (z.B. "xyz@hdm...")
			TextBox propertyValue = new TextBox();
			propertyAddBox.add(propertyValue);
			// 3. einen Button zum Hinzuf�gen
		    Button addPropertyButton = new Button("Eigenschaft hinzuf�gen") 
		    /*, new ClickHandler() {
		        public void onClick(ClickEvent event) {
		          Window.alert("TODO Clickhandler")
		    }; */
		    propertyAddBox.add(addPropertyButton);
		    
		    //hinzuf�gen von Zeile 4 zum Hauptgrid
		    userInformationGrid.setWidget(3, 0, propertyAddBox);
		
		    
		//GRID-ZEILE 5: 
		    HorizontalPanel contactDeleteBox = new HorizontalPanel();
		    Label deleteQuestion = new Label("Wollen Sie diesen Kontakt l�schen?");
		    contactDeleteBox.add(deleteQuestion);
		    
		    Button deleteContactButton = new Button("Kontakt l�schen") 
		    /*, new ClickHandler() {
		        public void onClick(ClickEvent event) {
		          Window.alert("TODO Clickhandler")
		    }; */
		    contactDeleteBox.add(deleteContactButton);
		    
		    //hinzuf�gen von Zeile 4 zum Hauptgrid
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
		
		//F�r jede Eigenschaft wird nun eine Zeile angelegt
	    for (i=0; i<contactProperties.size(); i++) {
	        //TODO Hier ListDataProvider integrieren??
	    	
	    	//Textbox erstellen, die den Wert der Eigenschaft hat
	    	TextBox textBox = new TextBox(contactProperties.get(i));
	    	
	    	//Textbox dem DatenGrid hinzuf�gen
	    	Column propColumn = new Column(textBox);

	    }
	    
	    	//abschlie�end der Zeile die zwei Buttons hinzuf�gen
	        propertyDataGrid.addColumn(deletePropertyButton, "");
	        propertyDataGrid.addColumn(savePropertyButton, "");
	        
	        //Datenmodell aktualisieren
	        propertyDataGrid.refresh();
	    }
		

		/**
		 * Erstellen und Befüllen eines Grids.
		 */
		Grid contactGrid = new Grid(9, 1);
		this.add(contactGrid);
		
		contactGrid.setWidget(1, 0, contactFirstNameLabel);
		contactGrid.setWidget(1, 1, contactSetFirstNameLabel);
		
		contactGrid.setWidget(2, 0, contactLastNameLabel);
		contactGrid.setWidget(2, 1, contactSetLastNameLabel);
		
		contactGrid.setWidget(3, 0, contactBirthdayLabel);
		contactGrid.setWidget(3, 1, contactSetBirthdayLabel);
		
		contactGrid.setWidget(4, 0, contactEmailLabel);
		contactGrid.setWidget(4, 1, contactSetEmailLabel);
		
		contactGrid.setWidget(5, 0, contactTelefonLabel);
		contactGrid.setWidget(5, 1, contactSetTelefonLabel);
		
		contactGrid.setWidget(6, 0, contactStreetLabel);
		contactGrid.setWidget(6, 1, contactSetStreetLabel);
		
		contactGrid.setWidget(7, 0, contactZipLabel);
		contactGrid.setWidget(7, 1, contactSetZipLabel);
		
		contactGrid.setWidget(8, 0, contactOrtLabel);
		contactGrid.setWidget(8, 1, contactSetOrtLabel);
		
		/**
		 * Den Buttons werden Clickhandler hinzugefügt. Zudem werden die Buttons in ein Horizontal Panel abgelegt,
		 * welches wiederum in das Grid eingefügt wird.
		 */
		deleteContactButton.addClickHandler(new DeleteClickHandler());
		deleteContactButton.setEnabled(false);
		
		editContactButton.addClickHandler(new EditClickHandler());
		editContactButton.setEnabled(false);

		
		shareContactButton.addClickHandler(new ShareClickhandler());
		shareContactButton.setEnabled(false);
		
		HorizontalPanel contactButtonsPanel = new HorizontalPanel();
		contactGrid.setWidget(9, 0, contactButtonsPanel);
		
		contactButtonsPanel.add(editContactButton);
		contactButtonsPanel.add(deleteContactButton);
		contactButtonsPanel.add(shareContactButton);
	}
	
	/**
	 * Im Folgenden Code werden Clickhandler und Asynchrone Methodenaufrufe für die Operationen Editieren, Löschen oder Teilen 
	 * eines <code>Contact</code> Objekts implementiert.
	 * @author Brase
	 *
	 */
	

	private class CreateOwnerCallback implements AsyncCallback<User> {

	private class DeleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			if(contactToDisplay == null) {
				Window.alert("Kein Kontakt ausgewählt");
			}else {
			editorService.deleteContact(contactToDisplay, new deleteContactCallback(contactToDisplay));
			}
		}
	}
	
	private class deleteContactCallback implements AsyncCallback<Void> {

		@Override
		
		private Contact contact = null;
		
		deleteContactCallback(Contact c) {
			contact = c;
		}
		
		public void onFailure(Throwable caugth) {
		}
		
		@Override
		public void onSuccess(Void result) {
			if(c != null) {
				Contacttree.removeContact(contact);
			}
		}
	}


}
	

