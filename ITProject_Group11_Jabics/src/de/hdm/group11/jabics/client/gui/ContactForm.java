package de.hdm.group11.jabics.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Jabics;
import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.shared.EditorService;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
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
	

