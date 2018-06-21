package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;

/*
 * In der folgenden Klasse "Editor" wird die Darstellung einzelnen Klassen bestimmt:
 * ContactForm, ContactListForm
 */
public class Editor implements EntryPoint {
	
	private static final String SERVER_ERROR = "Der Server ist nicht erreichbar.";
	/**
	 * Im folgenden Interface werden die Items, öffnen und schließen, hinzugefügt.
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

	LoginServiceAsync loginService;
	EditorServiceAsync editorAdmin;
	LoginInfo loginfo;
	
	JabicsUser currentUser;

	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel topPanel = new HorizontalPanel();
	HorizontalPanel widgetPanel = new HorizontalPanel();
	HorizontalPanel placeholder = new HorizontalPanel();

	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Melden sie sich mit ihren Google-Account an um Jabics nutzen zu k�nnen.");
	private Anchor signInLink = new Anchor("Anmelden.");
	/**
	 * Instanzenvariablen, die Kontakte oder Kontaktlisten zu Anzeige bringen
	 */
	ContactForm cForm;
	ContactListForm clForm = new ContactListForm();
	ContactCollaborationForm ccForm;
	ContactListCollaborationForm clcForm;
	
	// SearchForm sForm = new SearchForm();
	TreeViewMenu treeViewMenu;

	@Override
	public void onModuleLoad() {

		//testMethod();

		/*
		 * Zunächst wird eine Editor-Instanz hinzugefügt.
		 */
		currentUser = new JabicsUser(1);
		editorAdmin = ClientsideSettings.getEditorService();
		loginService = ClientsideSettings.getLoginService();
		GWT.log(GWT.getHostPageBaseURL());
		//loadEditor();
		loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());
		//loadEditor();
	}
	
	public void loadEditor() {

		if (editorAdmin == null) {
			editorAdmin = ClientsideSettings.getEditorService();
		}
		
		mainPanel.add(topPanel);
		mainPanel.add(widgetPanel);

		
		treeViewMenu = new TreeViewMenu();
		
		treeViewMenu.getStackLayoutPanel();

		Button createC = new Button("Neuer Kontakt");
		createC.addClickHandler(new CreateCClickHandler());
		Button createCL = new Button("Neue Liste");
		createCL.addClickHandler(new CreateCLClickHandler());
		Button search = new Button("Suche");
		search.addClickHandler(new SearchClickHandler());
		Button settings = new Button("irgendwas anderes");
		settings.addClickHandler(new SearchClickHandler());

		clForm = new ContactListForm();
		cForm = new ContactForm();
		ccForm = new ContactCollaborationForm();

		topPanel.add(search);
		topPanel.add(settings);
		topPanel.add(createC);
		topPanel.add(createCL);
		GWT.log("2");
		/**
		 * TODO: wie funktioniert das hinzufügen des TreeView?
		 */ 

		// widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		JabicsUser u = new JabicsUser();
		u.setEmail("stahl.alexander@live.de");
		u.setId(1);
		u.setUsername("Alexander Stahl");
		cForm.setUser(u);

		Property p1 = new Property("Name", Type.STRING);
		Property p2 = new Property("VorName", Type.STRING);
		p1.setStandard(true);
		Property p3 = new Property("Straße", Type.STRING);
		ArrayList<PValue> val = new ArrayList<PValue>();
		val.add(new PValue(p1, "Max", u));
		val.add(new PValue(p2, "Mustermann", u));
		val.add(new PValue(p3, "eineStraße", u));
		
		Contact c1 = new Contact(val, "maxmuster");
		ContactList cl1 = new ContactList();
		cl1.addContact(c1);
		cl1.setId(5);
		cl1.setListName("Lischde");
		
		c1.setValues(val);

		c1.setId(1);

		showContact(c1);
		//showContactCollab(c1);
		
		//treeViewMenu.addContact(c1);
		//widgetPanel.add(treeViewMenu.getStackLayoutPanel());

		GWT.log("5");
		//showContact(c1);
		GWT.log("6");

		mainPanel.add(widgetPanel);

		RootPanel.get("details").add(mainPanel);
	}

	private void loadLogin() {
	    // Assemble login panel.
		Window.alert("3.1");
	    signInLink.setHref(loginfo.getLoginUrl());
	    Window.alert("3.2");
	    loginPanel.add(loginLabel);
	    Window.alert("3.3");
	    loginPanel.add(signInLink);
	    Window.alert("3.4");
	    RootPanel.get("content").add(loginPanel);
	}
	
	public void setLoginInfo(LoginInfo logon) {
		this.loginfo = logon;
	}

	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
	}
	
	public void showContact(Contact c) {
		if (this.cForm == null) {
			cForm.setEditor(this);
			cForm = new ContactForm();
		}

		cForm.setEditor(this);

		this.widgetPanel.clear();
		GWT.log("9");

		//widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// cForm.clear();
		cForm.setCurrentContact(c);
		// cForm.setUser(loginfo.getCurrentUser());
		
		widgetPanel.add(cForm);
		}



	public void showContactList(ContactList cl) {
		if (this.clForm == null) {
			//clForm = new ContactListForm(); ist in der loadEditor
			clForm.setEditor(this);
		}
		widgetPanel.clear();
		//widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// clForm.clear();
		clForm.setCurrentList(cl);
		clForm.setUser(loginfo.getCurrentUser());
		
		widgetPanel.add(clForm);
		
	}

	public void showContactCollab(Contact c) {

		GWT.log("contactCollab");

		if (this.ccForm == null) {
			ccForm = new ContactCollaborationForm();
			ccForm.setEditor(this);
			
		}
		GWT.log("huhu");
		widgetPanel.clear();

		//widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// ccForm.clear();
		//ccForm = new ContactCollaborationForm();
		ccForm.setEditor(this);
		ccForm.setContact(c);
		// ccForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(ccForm);
	}

	public void showContactListCollab(ContactList cl) {
		if (this.clcForm == null) {
			clcForm = new ContactListCollaborationForm();
			clcForm.setEditor(this);
		}
		widgetPanel.clear();
		widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// clcForm.clear();
		clcForm.setList(cl);
		// clcForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(clcForm);
	}

	public void returnToContactForm(Contact c) {
		if (this.cForm == null) {
			cForm = new ContactForm();
		}
		addContactToTree(c);
		cForm.setCurrentContact(c);
		widgetPanel.add(cForm);
	}

	public void returnToContactListForm(ContactList cl) {
		if (this.clForm == null) {
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
			

			// //treeViewMenu.setContactForm(cForm);
			// //cForm.setTreeViewMenu(treeViewMenu);
			//
			// hPanel.add(contactDetailPanel);
			//
			// contactDetailPanel.clear();
			// contactDetailPanel.add(cForm);
			//
			// RootPanel.get("details").add(contactDetailPanel);

		}
	}

	private class SetJabicsUserCallback implements AsyncCallback<JabicsUser> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(JabicsUser u) {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
		} 
	}
	private class loginServiceCallback implements AsyncCallback<LoginInfo> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}
 
		@Override
		public void onSuccess(LoginInfo logon) {
			GWT.log("Login sucess 2");
			currentUser = logon.getCurrentUser();
			setLoginInfo(logon);

			if (currentUser.getIsLoggedIn()) {
				setJabicsUser(logon.getCurrentUser());
				loadEditor();
			} else {
				Window.alert("User not logged in");
			}
		}
	}

	/**
	 * ClickHandler um einen neuen Kontakt zu erstellen
	 */
	private class CreateCClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Window.alert(
					"Wenn du fortfährst, gehen alle nicht gespeicherten Daten verloren. Diese Auswahl bitte noch einfügen! (Editor, klasse CreateClickHandler)");

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
			Window.alert(
					"Wenn du fortfährst, gehen alle nicht gespeicherten Daten verloren. Diese Auswahl bitte noch einfügen! (Editor, klasse CreateClickHandler)");

			ContactList newContactList = new ContactList();
			showContactList(newContactList);
		}
	}

}
