package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

/*
 * In der folgenden Klasse "Editor" wird die Darstellung einzelnen Klassen bestimmt:
 * ContactForm, ContactListForm
 */


public class Editor implements EntryPoint{
	
	/**
	 * Im folgenden Interface werden die Items, öffnen und schlie0en, hinzugefügt.
	 */
	static interface JabicsTreeResources extends CellTree.Resources {
		@Override
		@Source("cellTreeClosedItem.gif")
		ImageResource cellTreeClosedItem();
		@Override
		@Source("cellTreeOpenItem.gif")
		ImageResource cellTreeOpenItem();
		@Override
		@Source("JabicsCellTree.css")
		CellTree.Style cellTreeStyle();
	}
	
	EditorServiceAsync editorAdmin = null;
	LoginInfo loginfo;
	
	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel topPanel = new HorizontalPanel();
	HorizontalPanel widgetPanel = new HorizontalPanel();
	HorizontalPanel placeholder = new HorizontalPanel();
	
	/**
	 * Instanzenvariablen, die Kontakte oder Kontaktlisten zu Anzeige bringen
	 */
	ContactForm cForm;
	ContactListForm clForm;
	ContactCollaborationForm ccForm;
	ContactListCollaborationForm clcForm;
	//SearchForm sForm = new SearchForm();
	TreeViewMenu treeViewMenu;
	
	@Override
	public void onModuleLoad() {
		
		
		if(editorAdmin == null) {
			editorAdmin = ClientsideSettings.getEditorService();
		}
		
		mainPanel.add(topPanel);
		mainPanel.add(widgetPanel);
		
		treeViewMenu = new TreeViewMenu();
		
		Button createC = new Button("Neuer Kontakt");
		createC.addClickHandler(new CreateCClickHandler());
		Button createCL = new Button("Neue Liste");
		createCL.addClickHandler(new CreateCLClickHandler());
		Button search = new Button("Suche");
		search.addClickHandler(new SearchClickHandler());
		Button settings = new Button("irgendwas anderes");
		settings.addClickHandler(new SearchClickHandler());
		
		topPanel.add(search);
		topPanel.add(settings);
		topPanel.add(createC);
		topPanel.add(createCL);
		
		/**
		 * TODO: wie funktioniert das hinzufügen des TreeView?
		 */
		widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		
		RootPanel.get("details").add(mainPanel);
		
	}
	
	public void setLoginInfo(LoginInfo logon) {
		this.loginfo = logon;
	}
	
	public void showContact(Contact c) {
		if(this.cForm == null) {
			cForm = new ContactForm();
			cForm.setEditor(this);
		}
		widgetPanel.clear();
		widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		//cForm.clear();
		cForm.setCurrentContact(c);
		cForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(cForm);
	}
	
	public void showContactList(ContactList cl) {
		if(this.clForm == null) {
			clForm = new ContactListForm();
			clForm.setEditor(this);
		}
		widgetPanel.clear();
		widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		//clForm.clear();
		clForm.setCurrentList(cl);
		clForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(clForm);
	}
	public void showContactCollab(Contact c) {
		if(this.ccForm == null) {
			ccForm = new ContactCollaborationForm();
			ccForm.setEditor(this);
		}
		widgetPanel.clear();
		widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		//ccForm.clear();
		ccForm.setContact(c);
		//ccForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(ccForm);
	}
	
	public void showContactListCollab(ContactList cl) {
		if(this.clcForm == null) {
			clcForm = new ContactListCollaborationForm();
			clcForm.setEditor(this);
		}
		widgetPanel.clear();
		widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		//clcForm.clear();
		clcForm.setList(cl);
		//clcForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(clcForm);
	}
	public void returnToContactForm(Contact c) {
		if(this.cForm == null) {
			cForm = new ContactForm();
		}
		addContactToTree(c);
		cForm.setCurrentContact(c);
		widgetPanel.add(cForm);
	}
	public void returnToContactListForm(ContactList cl) {
		if(this.clForm == null) {
			clForm = new ContactListForm();
		}
		addContactListToTree(cl);
		clForm.setCurrentList(cl);
		widgetPanel.add(clForm);
	}
	public void addContactToTree(Contact c) {
		treeViewMenu.addContact(c);
	}
	public void addContactListToTree(ContactList cl) {
		treeViewMenu.addContactList(cl);
	}
	public void addContactToListInTree(ContactList cl, Contact c) {
		treeViewMenu.addContactToList(cl, c);
	}
	
	
	/**
	 * ClickHandler um die SearchForm anzuzeigen
	 */
	private class SearchClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
//			//treeViewMenu.setContactForm(cForm);
//			//cForm.setTreeViewMenu(treeViewMenu);
//			
//			hPanel.add(contactDetailPanel);
//			
//			contactDetailPanel.clear();
//			contactDetailPanel.add(cForm);
//			
//			RootPanel.get("details").add(contactDetailPanel);
			
		}
	}
	/**
	 * ClickHandler um einen neuen Kontakt zu erstellen
	 */
	private class CreateCClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Window.alert("Wenn du fortfährst, gehen alle nicht gespeicherten Daten verloren. Diese Auswahl bitte noch einfügen! (Editor, klasse CreateClickHandler)");
			
			Contact newContact = new Contact();
			showContact(newContact);
		}
	}
	/**
	 * ClickHandler um einen neuen Kontakt zu erstellen
	 */
	private class CreateCLClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Window.alert("Wenn du fortfährst, gehen alle nicht gespeicherten Daten verloren. Diese Auswahl bitte noch einfügen! (Editor, klasse CreateClickHandler)");
			
			ContactList newContactList = new ContactList();
			showContactList(newContactList);
		}
	}
	

}
