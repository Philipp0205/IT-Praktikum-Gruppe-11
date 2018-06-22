package de.hdm.group11.jabics.client.gui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.MultiSelectionModel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class ShowContactForm {
	
EditorServiceAsync editorService = ClientsideSettings.getEditorService();
	
	Editor e;
	JabicsUser u;
	Contact currentContact;
	
	VerticalPanel listEdit, conEdit;
	HorizontalPanel listShareBox, listDeleteBox, listAddBox, listRmvBox;
	
	MultiSelectionModel<Contact> selectionModel;
	
	Button deleteListButton = new Button("Liste löschen");
	Button shareListButton = new Button("Liste teilen");
	
	MultiSelectionModel<Contact> selectionModel1  = new MultiSelectionModel<Contact>();
	
	MultiSelectionModel<Contact> selectionModel2  = new MultiSelectionModel<Contact>();	
	
	
	

}
