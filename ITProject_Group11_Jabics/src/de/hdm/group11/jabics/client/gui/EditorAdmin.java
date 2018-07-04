package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Editor;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class EditorAdmin {

	private EditorServiceAsync editorService;

	private LoginInfo loginfo;
	private JabicsUser currentUser;

	private Button logoutButton;
	private Button deleteUserButton;
	private Button createC;
	private Button createCL;

	private HorizontalPanel topPanel = new HorizontalPanel();
	private VerticalPanel menuPanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();

	private HorizontalPanel logoutPanel = new HorizontalPanel();
	private HorizontalPanel widgetPanel = new HorizontalPanel();
	private HorizontalPanel formPanel = new HorizontalPanel();

	/**
	 * Instanzenvariablen, die Kontakte oder Kontaktlisten zu Anzeige bringen
	 */
	private EditContactForm ecForm;
	private ShowContactForm scForm;
	private ContactListForm clForm;
	private ContactCollaborationForm ccForm;
	private ExistingContactCollaborationForm eccForm;
	private ContactListCollaborationForm clcForm;
	private SearchForm sForm;

	private TreeViewMenu treeViewMenu;
	
	
	RootLayoutPanel rp;

	public EditorAdmin(JabicsUser u) {
		this.currentUser = u;
		editorService = ClientsideSettings.getEditorService();

		createC = new Button("Neuer Kontakt");
		createC.addClickHandler(new CreateCClickHandler());
		createC.setStyleName("btn1");
		createCL = new Button("Neue Liste");
		createCL.addClickHandler(new CreateCLClickHandler());
		createCL.setStyleName("btn2");
		deleteUserButton = new Button("Konto löschen");
		deleteUserButton.setStyleName("deleteAccount");
		deleteUserButton.addClickHandler(new DeleteUserClickHandler());

		topPanel.add(createC);
		topPanel.add(createCL);
		topPanel.add(logoutPanel);
		logoutPanel.setStyleName("logout");
		topPanel.setStyleName("topPanel");

		mainPanel.add(widgetPanel);
		// widgetPanel.add(menuPanel);
		widgetPanel.add(formPanel);

		treeViewMenu = new TreeViewMenu(currentUser);
		treeViewMenu.setEditor(this);

		treeViewMenu.setStyleName("treeView");
		
		menuPanel.add(treeViewMenu.getStackPanel1());
		menuPanel.add(treeViewMenu.getStackPanel2());

		//menuPanel.setWidth("400px");

		menuPanel.setStyleName("menuPanel");
	}

	public void loadEditor() {
		GWT.log("hallo gwt");
		if (editorService == null) {
			editorService = ClientsideSettings.getEditorService();
		}

		// Menu hinzufügen
		loadLogout();

		RootPanel.get("nav").add(topPanel);
		RootPanel.get("menu").add(menuPanel);
		RootPanel.get("details").add(mainPanel);
		RootPanel.get("deleteAccount").add(deleteUserButton);
	}

	/**
	 * Getter und Setter
	 */
	public void setLoginInfo(LoginInfo logon) {
		this.loginfo = logon;
	}

	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
		// treeViewMenu.setUser(u);
	}

	public void loadLogout() {
		logoutButton = new Button("Abmelden");
		logoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.assign(loginfo.getLogoutUrl());
			}
		});
		logoutButton.setStyleName("logbutton");
		logoutPanel.add(logoutButton);
	}

	public void showMenuOnly() {
		if (treeViewMenu != null) {
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
		GWT.log("form einfügen");
		formPanel.insert(scForm, 0);
		// formPanel.add(scForm);
		GWT.log("ShowCont fertig");
	}

	public void editContact(Contact c) {
		GWT.log("editcont");
		// if (this.cForm == null) {
		ecForm = new EditContactForm();
		ecForm.setEditor(this);
		ecForm.setUser(this.currentUser);

		formPanel.clear();
		GWT.log("AltesWidgetEntfernt");
		ecForm.setNewContact(false);
		ecForm.setContact(c);

		formPanel.insert(ecForm, 0);
		GWT.log("editcontFertig");

		formPanel.setStyleName("formPanel");
	}

	public void newContact(Contact c) {
		GWT.log("editcont");
		// if (this.cForm == null) {
		ecForm = new EditContactForm();
		ecForm.setEditor(this);
		ecForm.setUser(this.currentUser);

		formPanel.clear();
		GWT.log("AltesWidgetEntfernt");
		ecForm.setNewContact(true);
		ecForm.setContact(c);

		formPanel.insert(ecForm, 0);
		GWT.log("editcontFertig");

		formPanel.setStyleName("formPanel");
	}

	public void newContactList(ContactList cl) {
		// if (this.cForm == null) {
		clForm = new ContactListForm();
		clForm.setEditor(this);
		clForm.setUser(this.currentUser);

		formPanel.clear();
		GWT.log("Editor: isNewList true");
		clForm.setIsNewList(true);
		clForm.setContactList(cl);

		formPanel.insert(clForm, 0);

		formPanel.setStyleName("formPanel");
	}

	public void showContactList(ContactList cl) {
		GWT.log("7.x showContactList");

		if (this.clForm == null) {
			clForm = new ContactListForm();
			clForm.setUser(this.currentUser);
			clForm.setEditor(this);
		}
		formPanel.clear();

		// widgetPanel.add(treeViewMenu.getStackLayoutPanel());
		// clForm.clear();
		clForm.setIsNewList(false);
		clForm.setCurrentList(cl);
		formPanel.add(clForm);
	}

	public void showContactCollab(Contact c) {
		GWT.log("contactCollab");
		if (this.ccForm == null) {
			ccForm = new ContactCollaborationForm();
			ccForm.setEditor(this);
		}

		formPanel.clear();
		// ccForm.clear();
		ccForm.setContact(c);
		// ccForm.setUser(loginfo.getCurrentUser());
		formPanel.add(ccForm);
	}

	public void showExistingContactCollab(Contact c) {
		GWT.log("existingContactCollab");
		if (this.eccForm == null) {
			eccForm = new ExistingContactCollaborationForm();
			eccForm.setEditor(this);
			eccForm.setUser(this.currentUser);
		}
		eccForm.setContact(c);
		formPanel.clear();
		formPanel.insert(eccForm, 0);
	}

	public void showContactListCollab(ContactList cl) {

		GWT.log("contactListCollab");
		if (this.clcForm == null) {
			clcForm = new ContactListCollaborationForm();
			clcForm.setEditor(this);
		}

		formPanel.clear();
		clcForm.clear();
		clcForm.setContactList(cl);
		formPanel.add(clcForm);
		// formPanel.insert(clcForm, 0);
	}

	public void showSearchForm(ContactList cl) {
		// if (this.sForm == null) {

		sForm = new SearchForm();
		sForm.setEditor(this);
		sForm.setContactList(cl);
		sForm.setJabicsUser(currentUser);
		formPanel.clear();
		formPanel.add(sForm);
		GWT.log("#######SearchForm");
	}

	public void returnToContactForm(Contact c) {
		if (this.scForm == null) {
			scForm = new ShowContactForm();
			scForm.setUser(this.currentUser);
			scForm.setEditor(this);
		}
		// addContactToTree(c);
		formPanel.clear();
		scForm.setContact(c);
		formPanel.add(scForm);
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

	public void deleteUser() {
		Window.alert("Achtung nicht auf ok drücken");
		editorService.deleteUser(currentUser, new DeleteUserCallback());
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
		treeViewMenu.updateContact(c);
	}

	public void updateContactListInTree(ContactList cl) {
		treeViewMenu.addContactList(cl);
	}

	public void removeContactFromContactListInTree(ContactList cl, Contact c) {
		treeViewMenu.removeContactOfContactList(cl, c);
	}

	public void removeContact(Contact c) {
		treeViewMenu.removeContact(c);
	}

	public void removeContactListFromTree(ContactList cl) {
		treeViewMenu.removeContactListFromTree(cl);
	}
/*TODO: wird das benötigt
	public void flushContactLists() {
		treeViewMenu.flushContactListsProvider();
	}*/

	private class DeleteUserClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			DeleteUserDialogBox deleteBox = new DeleteUserDialogBox();
			deleteBox.show();
		}
	}

	private class DeleteUserDialogBox extends DialogBox {
		private Label confirmation = new Label("Damit löschen Sie Ihr Konto bei Jabics, alle zugehörigen Kontakte, "
				+ "Listen und Freigaben. Sind Sie sicher?");
		private VerticalPanel mainPanel = new VerticalPanel();
		private HorizontalPanel buttons = new HorizontalPanel();
		private Button exitButton = new Button("Abbruch");
		private Button confirmButton = new Button("Ja, ich bin sicher");

		public DeleteUserDialogBox() {
			this.setSize("300px", "150px");
			exitButton.addClickHandler(new ExitDialogBoxClickHandler());
			buttons.add(exitButton);
			buttons.add(confirmButton);
			mainPanel.add(confirmation);
			mainPanel.add(buttons);
			this.add(mainPanel);
			this.center();
		}

		/**
		 * ClickHandler, der die DialogBox verschwinden lässt
		 */
		private class ExitDialogBoxClickHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				DeleteUserDialogBox.this.hide();
			}
		}

		/**
		 * ClickHandler, der den <code>User</code> löscht
		 */
		private class DeleteClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				if (currentUser != null) {
					deleteUser();
				}
			}
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
//			Window.alert(
//					"Wenn du fortfährst, gehen alle nicht gespeicherten Daten verloren. Diese Auswahl bitte noch einfügen! (Editor, klasse CreateClickHandler)");

			ContactList newContactList = new ContactList();
			newContactList(newContactList);
			// showContactList(newContactList);
		}
	}

	/**
	 * RPC++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	private class DeleteUserCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(Void v) {
			try {
				Window.alert("Account erfolgreich gelöscht");
				Editor e = new Editor();
				e.onModuleLoad();

			} catch (Exception e) {
				Window.alert("Account erfolgreich gelöscht");
			}
		}
	}
}
