package de.hdm.group11.jabics.client.gui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.User;

/*
 * Formular für die Darstellung von Kontakten eines Users
 * */

public class ContactForm extends VerticalPanel {

	EditorServiceAsync editorAdmin = ClientsideSettings.getEditorService();
	
	User userToDisplay = null;
	Contact contactToDisplay = null;
	//CustomDetailsViewModal cdvm = null;
	
	Label userInformationLabel = new Label("User: ");
	Label contactFirstNameLabel = new Label("Vorname: ");
	Label contactLastNameLabel = new Label("Nachname: ");
	Label contactTelefonLabel = new Label("Telefon: ");
	
	Label userSetInformationLabel = new Label();
	Label contactSetFirstNameLabel = new Label();
	Label contactSetLastNameLabel = new Label();
	Label contactSetTelefonLabel = new Label();
	
	Button createContactButton = new Button("Kontakt erstellen");
	Button deleteContactButton = new Button("Kontakt löschen");
	Button editContactButton = new Button("Kontakt editieren");
	
	public void onLoad() {
		
		super.onLoad();
		
		Grid userInformationGrid = new Grid(1, 2);
		
		userInformationGrid.setWidget(1, 0, userInformationLabel);
		userInformationGrid.setWidget(1, 1, userSetInformationLabel);
		
		Grid contactInformationGrid = new Grid(3, 3);
		
		contactInformationGrid.setWidget(1, 0, contactFirstNameLabel);
		contactInformationGrid.setWidget(1, 1, contactSetFirstNameLabel);
		
		contactInformationGrid.setWidget(2, 0, contactLastNameLabel);
		contactInformationGrid.setWidget(2, 1, contactSetLastNameLabel);
		
		contactInformationGrid.setWidget(3, 0, contactTelefonLabel);
		contactInformationGrid.setWidget(3, 1, contactSetTelefonLabel);
		
		
		editorAdmin.getUserById(userToDisplay, new CreateOwnerCallback());
		
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
