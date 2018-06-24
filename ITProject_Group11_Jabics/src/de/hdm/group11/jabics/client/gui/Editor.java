package de.hdm.group11.jabics.client.gui;

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
import com.google.gwt.user.client.ui.StackPanel;
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

/**
 * In der folgenden Klasse "Editor" wird die Darstellung einzelnen Klassen
 * bestimmt: ContactForm, ContactListForm
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
	//VerticalPanel menuPanel = new VerticalPanel();
	//VerticalPanel formPanel = new VerticalPanel();
	HorizontalPanel placeholder = new HorizontalPanel();

	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Melden sie sich mit ihren Google-Account an um Jabics nutzen zu k�nnen.");
	private Anchor signInLink = new Anchor("Anmelden.");

	/**
	 * Instanzenvariablen, die Kontakte oder Kontaktlisten zu Anzeige bringen
	 */
	EditContactForm cForm;
	ShowContactForm scForm;
	ContactListForm clForm = new ContactListForm();
	ContactCollaborationForm ccForm;
	ContactListCollaborationForm clcForm;
	ExistingContactCollaborationForm eccForm;
	SearchForm sForm;
	// ExistingContactListCollaborationForm clcForm;

	// SearchForm sForm = new SearchForm();
	TreeViewMenu treeViewMenu;

	@Override
	public void onModuleLoad() {

		/*
		 * Zunächst wird eine Editor-Instanz hinzugefügt.
		 */
		currentUser = new JabicsUser();
		currentUser.setEmail("stahl.alexander@live.de");
		currentUser.setId(1);
		currentUser.setUsername("Alexander Stahl");

		editorAdmin = ClientsideSettings.getEditorService();
		// loginService = ClientsideSettings.getLoginService();
		// GWT.log(GWT.getHostPageBaseURL());
		// loadEditor();
		// loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());
		loadEditor();

	}

	public void loadEditor() {

		if (editorAdmin == null) {
			editorAdmin = ClientsideSettings.getEditorService();
		}

		mainPanel.add(topPanel);
		mainPanel.add(widgetPanel);
	//	widgetPanel.add(menuPanel);
	//	widgetPanel.add(formPanel);

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

		
		// Menü hinzufügen

		treeViewMenu = new TreeViewMenu();
		treeViewMenu.onLoad();
		treeViewMenu.setEditor(this);
		GWT.log("Editor: TreeViewMenu erstellt");

		widgetPanel.add(treeViewMenu.getStackPanel());
		/**
		 * Das kann weg sobald treeview passt
		 */
		Contact c6 = new Contact();
		c6.setId(1);
		editContact(c6);
		
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

	/**
	 * Getter und Setter
	 */
	public void setLoginInfo(LoginInfo logon) {
		this.loginfo = logon;
	}

	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
	}

	/**
	 * Kontakte, Listen und CollabForms anzeigen
	 */
	public void showContact(Contact c) {
		//if (this.scForm == null) {

		scForm = new ShowContactForm();
		scForm.setEditor(this);
		scForm.setUser(this.currentUser);

		GWT.log("showCont2");
		scForm.setUser(currentUser);
		scForm.showContact(c);
		GWT.log("showCOnt4");
		widgetPanel.remove(1);
		widgetPanel.insert(scForm, 1);
	}
	
	public void editContact(Contact c) {
		GWT.log("editcont");
		//if (this.cForm == null) {
			cForm = new EditContactForm();
			cForm.setEditor(this);
			cForm.setUser(this.currentUser);
		
		cForm.setContact(c);
		GWT.log("editContact");
		//widgetPanel.remove(1);
		GWT.log("AltesWidgetEntfernt");
		widgetPanel.insert(cForm, 1);
		GWT.log("editcontFertig");
	}

	public void showContactList(ContactList cl) {
		//if (this.clForm == null) {
			clForm = new ContactListForm();
			clForm.setEditor(this);
		
		widgetPanel.clear();
		// widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// clForm.clear();
		clForm.setCurrentList(cl);
		clForm.setUser(loginfo.getCurrentUser());

		widgetPanel.add(clForm);

	}

	public void showContactCollab(Contact c) {

		GWT.log("contactCollab");
		//if (this.ccForm == null) {
			ccForm = new ContactCollaborationForm();
			ccForm.setEditor(this);
		
		GWT.log("huhu");
		widgetPanel.clear();
		// widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// ccForm.clear();
		// ccForm = new ContactCollaborationForm();
		ccForm.setEditor(this);
		ccForm.setContact(c);
		// ccForm.setUser(loginfo.getCurrentUser());
		widgetPanel.insert(ccForm, 1);
	}

	
	public void showExistingContactCollab(Contact c) {

		GWT.log("existingContactCollab");
		eccForm = new ExistingContactCollaborationForm();
		//if (this.eccForm == null) {
			eccForm = new ExistingContactCollaborationForm();
			eccForm.setEditor(this);

		
		widgetPanel.clear();

		// widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// ccForm.clear();
		// ccForm = new ContactCollaborationForm();
		eccForm.setContact(c);
		// ccForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(eccForm);
	}
	
	public void showContactListCollab(ContactList cl) {
		if (this.clcForm == null) {
			clcForm = new ContactListCollaborationForm();
			clcForm.setEditor(this);
		}
		widgetPanel.clear();
		// clcForm.clear();
		clcForm.setList(cl);
		// clcForm.setUser(loginfo.getCurrentUser());
		widgetPanel.add(clcForm);
	}

	
	public void showExistingContactListCollab(ContactList cl) {
		/**
		 * TODO implement
		 */
	}
	
	 public void showSearchForm(ContactList cl) {
//		if (this.sForm == null) {
		sForm = new SearchForm();
		
		sForm.setEditor(this);
		widgetPanel.clear();
		sForm.setContactList(cl);
		widgetPanel.add(sForm);
	} 

	
	public void returnToContactForm(Contact c) {
		if (this.cForm == null) {
			scForm = new ShowContactForm();
		}
		//addContactToTree(c);
		scForm.setContact(c);
		widgetPanel.add(cForm);
	}

	
	public void returnToContactListForm(ContactList cl) {
		if (this.clForm == null) {
			clForm = new ContactListForm();
		}
		//addContactListToTree(cl);
		clForm.setCurrentList(cl);
		widgetPanel.add(clForm);
	}

	/**
	 * TreeView manipulieren
	 */
	/*
	public void addContactToTree(Contact c) {
		treeViewMenu.addContact(c);
	}

	public void addContactListToTree(ContactList cl) {
		treeViewMenu.addContactList(cl);
	}

	public void addContactToListInTree(ContactList cl, Contact c) {
		treeViewMenu.addContactToList(cl, c);
	}
	*/

	
	//+++++++++++++++++++++++++++++++ClickHandler++++++++++++++++++++++++++
	/**
	 * ClickHandler um die SearchForm anzuzeigen
	 */
	private class SearchClickHandler implements ClickHandler {
		ContactList cl;
		void setCL(ContactList cl){
			this.cl=cl;
		}
		@Override
		public void onClick(ClickEvent event) {
			
			 //treeViewMenu.setContactForm(cForm);
			 //cForm.setTreeViewMenu(treeViewMenu);
			
//			 hPanel.add(contactDetailPanel);
//			
//			 contactDetailPanel.clear();
//			 contactDetailPanel.add(cForm);
			
	//		 RootPanel.get("details").add(contactDetailPanel);
			//TODO 
			ContactList cl = new ContactList();
			cl.setId(1);
			JabicsUser u = new JabicsUser();
			Contact c1 = new Contact();
			Property p = new Property();
			p.setId(2);
			PValue pv = new PValue(p,"Fiffi",u);
			c1.setName("Uschi");
			Contact c2 = new Contact();
			c2.setName("Strolch");
			Contact c3 = new Contact();
			c3.setName("Fiffi");
			c3.addPValue(pv);
			cl.setListName("Idiyets");
			cl.addContact(c1);
			cl.addContact(c2);
			cl.addContact(c3);
			showSearchForm(cl);

		}
	}/**
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

	/**
	 * 
	 * RPC++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *
	 */
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

	

}
