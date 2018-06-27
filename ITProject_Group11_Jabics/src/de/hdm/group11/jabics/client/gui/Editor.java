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
	VerticalPanel menuPanel = new VerticalPanel();
	HorizontalPanel formPanel = new HorizontalPanel();
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

		editorAdmin = ClientsideSettings.getEditorService();

		/**
		 * Zunächst wird eine User-Instanz hinzugefügt. Später entfernen und dies den
		 * Login übernehmen lassen
		 */
		currentUser = new JabicsUser();
		currentUser.setEmail("stahl.alexander@live.de");
		currentUser.setId(1);
		currentUser.setUsername("Alexander Stahl");

		/**
		 * Login
		 */
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
		widgetPanel.add(menuPanel);
		widgetPanel.add(formPanel);

		Button createC = new Button("Neuer Kontakt");
		createC.addClickHandler(new CreateCClickHandler());
		Button createCL = new Button("Neue Liste");
		createCL.addClickHandler(new CreateCLClickHandler());
		Button search = new Button("Suche");
		search.addClickHandler(new SearchClickHandler());
		// Button settings = new Button("irgendwas anderes");
		// settings.addClickHandler(new SearchClickHandler());

		topPanel.add(search);
		// topPanel.add(settings);
		topPanel.add(createC);
		topPanel.add(createCL);

		topPanel.addStyleName("topPanel");

		// Menu hinzufügen
		treeViewMenu = new TreeViewMenu();
		treeViewMenu.onLoad();
		treeViewMenu.setEditor(this);
		GWT.log("Editor: TreeViewMenu erstellt");

		menuPanel.add(treeViewMenu.getStackPanel());
		menuPanel.setStyleName("menuPanel");
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

	
	public void showMenuOnly() {
		if(treeViewMenu != null) {
			formPanel.clear();
		}
		Label noThing = new Label("Nichts anzuzeigen.");
		formPanel.add(noThing);
	}
	/**
	 * Kontakte, Listen und CollabForms anzeigen
	 */
	public void showContact(Contact c) {
		
		GWT.log("showCont");
		if (this.scForm == null) {
			scForm = new ShowContactForm();
			scForm.setEditor(this);
			scForm.setUser(this.currentUser);
		}
		formPanel.clear();
		scForm.setContact(c);

		// formPanel.clear();
		formPanel.insert(scForm, 0);
		GWT.log("ShowCont fertig");
	}

	public void editContact(Contact c) {
		GWT.log("editcont");
		// if (this.cForm == null) {
		cForm = new EditContactForm();
		cForm.setEditor(this);
		cForm.setUser(this.currentUser);

		formPanel.clear();
		GWT.log("AltesWidgetEntfernt");
		cForm.setNewContact(false);
		cForm.setContact(c);

		formPanel.insert(cForm, 0);
		GWT.log("editcontFertig");

		formPanel.setStyleName("formPanel");
	}

	public void newContact(Contact c) {
		GWT.log("editcont");
		// if (this.cForm == null) {
		cForm = new EditContactForm();
		cForm.setEditor(this);
		cForm.setUser(this.currentUser);

		formPanel.clear();
		GWT.log("AltesWidgetEntfernt");
		cForm.setNewContact(true);
		cForm.setContact(c);

		formPanel.insert(cForm, 0);
		GWT.log("editcontFertig");

		formPanel.setStyleName("formPanel");
	}

	public void showContactList(ContactList cl) {
		if (this.clForm == null) {
			clForm = new ContactListForm();
			clForm.setEditor(this);
			clForm.setUser(this.currentUser);
		}
		formPanel.clear();
		// widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// clForm.clear();
		clForm.setCurrentList(cl);

		formPanel.add(clForm);

	}

	public void showContactCollab(Contact c) {

		GWT.log("contactCollab");
		// if (this.ccForm == null) {
		ccForm = new ContactCollaborationForm();
		ccForm.setEditor(this);

		GWT.log("huhu");
		// formPanel.clear();
		// widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// ccForm.clear();
		// ccForm = new ContactCollaborationForm();
		ccForm.setEditor(this);
		ccForm.setContact(c);
		// ccForm.setUser(loginfo.getCurrentUser());
		formPanel.insert(ccForm, 0);
	}

	public void showExistingContactCollab(Contact c) {
		GWT.log("existingContactCollab");
		// if (this.eccForm == null) {
		eccForm = new ExistingContactCollaborationForm();
		eccForm.setEditor(this);
		eccForm.setContact(c);
		eccForm.setUser(this.currentUser);

		formPanel.clear();
		formPanel.add(eccForm);
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
		// if (this.sForm == null) {

		sForm = new SearchForm();
		sForm.setEditor(this);
		sForm.setContactList(cl);

		widgetPanel.remove(1);
		widgetPanel.add(sForm);
		GWT.log("#######SearchForm");
	}

	public void returnToContactForm(Contact c) {
		if (this.scForm == null) {
			scForm = new ShowContactForm();
		}
		// addContactToTree(c);
		scForm.setContact(c);
		formPanel.add(cForm);
		scForm.setStyleName("scForm");
	}

	public void returnToContactListForm(ContactList cl) {
		if (this.clForm == null) {
			clForm = new ContactListForm();
		}
		// addContactListToTree(cl);
		clForm.setCurrentList(cl);
		widgetPanel.add(clForm);
	}

	/**
	 * TreeView manipulieren
	 */
	public void addContactToTree(Contact c) {
		treeViewMenu.addContact(c);
	}
	
	public void addContactListToTree(ContactList cl) {
		treeViewMenu.addContactList(cl);
	}

	public void addContactToListInTree(ContactList cl, Contact c) {
		treeViewMenu.addContactToList(cl, c);
	}
	public void updateContactInTree(Contact c) {
		treeViewMenu.contactListTab.updateContact(c);
	}
	public void updateContactListInTree(ContactList cl) {
		treeViewMenu.addContactList(cl);
	}
	/*
	 * public void removeContactFromTree(Contact c) { treeViewMenu.removeContact(c);
	 * }
	 * 
	 * public void removeContactListFromTree(ContactList cl) {
	 * treeViewMenu.removeContactList(cl); }
	 * 
	 * public void removeContactFromListInTree(ContactList cl, Contact c) {
	 * treeViewMenu.removeContactFromList(cl, c); }
	 */

	// +++++++++++++++++++++++++++++++ClickHandler++++++++++++++++++++++++++
	/**
	 * ClickHandler um die SearchForm anzuzeigen
	 */
	private class SearchClickHandler implements ClickHandler {
		ContactList cl;

		SearchClickHandler() {
		}

		@Override
		public void onClick(ClickEvent event) {

			// treeViewMenu.setContactForm(cForm);
			// cForm.setTreeViewMenu(treeViewMenu);

			// hPanel.add(contactDetailPanel);
			//
			// contactDetailPanel.clear();
			// contactDetailPanel.add(cForm);

			// RootPanel.get("details").add(contactDetailPanel);
			// TODO
			GWT.log("clickEventForm");
			ContactList cl = new ContactList();
			cl.setId(1);
			JabicsUser u = new JabicsUser(1);
			Contact c1 = new Contact();
			Property p = new Property();
			p.setId(2);
			GWT.log("clickEventForm");
			PValue pv = new PValue(p, "Fiffi", u);
			c1.setName("Uschi");
			Contact c2 = new Contact();
			c2.setName("Strolch");
			Contact c3 = new Contact();
			c3.setId(3);
			c3.setName("Fiffi");
			GWT.log("clickEventForm");
			c3.addPValue(pv);
			GWT.log("clickEventForm");
			cl.setListName("Idiyets");
			cl.addContact(c1);
			cl.addContact(c2);
			cl.addContact(c3);
			showSearchForm(cl);
		}
	}

	/**
	 * ClickHandler um einen neuen Kontakt zu erstellen und zu bearbeiten
	 */
	private class CreateCClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Window.alert(
					"Wenn du fortfährst, gehen alle nicht gespeicherten Daten verloren. Diese Auswahl bitte noch einfügen! (Editor, klasse CreateClickHandler)");

			Contact newContact = new Contact();
			newContact(newContact);
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
