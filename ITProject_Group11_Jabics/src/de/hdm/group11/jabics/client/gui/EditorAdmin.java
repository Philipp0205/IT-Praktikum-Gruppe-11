package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Editor;
import de.hdm.group11.jabics.resource.JabicsResources;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * In der Klasse <code>EditorAdmin</code> werden die einzelnen Teile der Gui
 * zusammengeführt und die Darstellung der einzelnen Klassen initiiert.
 * EditorAdmin kann wie eine Verwalterklasse gesehen werden, die zu jedem
 * Zeitpunkt über das Geschehen im Editor Bescheid weiß. Folgende Klassen werden
 * verwaltet: ShowContactForm, EditContactForm, ContactCollaborationForm,
 * ExisitingContactColaborationForm, ContactListForm,
 * ContactListCollaborationForm, SearchForm, TreeViewMenu mit seinen "Subtabs".
 */
public class EditorAdmin {

	private EditorServiceAsync editorService;

	private LoginInfo loginfo;
	private JabicsUser currentUser;

	/*
	 * Buttons
	 */
	private Button logoutButton;
	private Button deleteUserButton;
	private Button createC;
	private Button createCL;

	/*
	 * Panelstrutur
	 */
	private HorizontalPanel topPanel = new HorizontalPanel();
	private VerticalPanel menuPanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();

	private HorizontalPanel legendPanel = new HorizontalPanel();

	private HorizontalPanel logoutPanel = new HorizontalPanel();
	private HorizontalPanel widgetPanel = new HorizontalPanel();
	private HorizontalPanel formPanel = new HorizontalPanel();

	/*
	 * Instanzenvariablen, die Kontakte oder Kontaktlisten zu Anzeige bringen
	 */
	private EditContactForm ecForm;
	private ShowContactForm scForm;
	private ContactListForm clForm;
	private ContactCollaborationForm ccForm;
	private ExistingContactCollaborationForm eccForm;
	private ContactListCollaborationForm clcForm;
	private SearchForm sForm;

	private Label labelShared;
	private Label labelNotShared;
	private Image imageShared;
	private Image imageNotShared;

	private TreeViewMenu treeViewMenu;

	/**
	 * Eine neue Instanz des EditorAdmins erstellen. In einem eingeloggten Zustand
	 * existiert immer nur eine einzige Instanz dieser Klasse, die über alle anderen
	 * Klassen "Bescheid" weiß. Diesen Konstruktor darf also nur Editor aufrufen!
	 * 
	 * @param u, der eingeloggte <code>JabicsUser</code>
	 */
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

		imageShared = new Image(JabicsResources.INSTANCE.greendot());
		imageNotShared = new Image(JabicsResources.INSTANCE.reddot());

		labelShared = new Label("geteilt");
		labelNotShared = new Label("nicht geteilt");

		legendPanel.add(imageShared);
		legendPanel.add(labelShared);
		legendPanel.add(imageNotShared);
		legendPanel.add(labelNotShared);

		menuPanel.add(treeViewMenu.getStackPanel1());
		menuPanel.add(treeViewMenu.getStackPanel2());
		menuPanel.add(legendPanel);

		menuPanel.setStyleName("menuPanel");
		legendPanel.addStyleName("legendPanel");
	}

	/**
	 * Den Editor anzeigen, dem Rootpanel hinzufügen.
	 */
	public void loadEditor() {
		GWT.log("hallo gwt");
		if (editorService == null) {
			editorService = ClientsideSettings.getEditorService();
		}

		// Menu hinzufügen
		loadLogout();
		RootPanel.get("details").clear();
		RootPanel.get("nav").add(topPanel);
		RootPanel.get("menu").add(menuPanel);
		RootPanel.get("details").add(mainPanel);
		RootPanel.get("deleteAccount").add(deleteUserButton);
	}

	/**
	 * Die aktuelle LoginInfo setzen
	 * 
	 * @param logon LoginInfo mit aktuellem Nutzer
	 */
	public void setLoginInfo(LoginInfo logon) {
		this.loginfo = logon;
	}

	/**
	 * Den aktuellen Nutzer setzen
	 * 
	 * @param u, Aktueller Nutzer
	 */
	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
		// treeViewMenu.setUser(u);
	}

	/**
	 * Den LogoutButton anzeigen
	 */
	public void loadLogout() {
		if (loginfo != null) {
			logoutButton = new Button("Abmelden");
			logoutButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.Location.assign(loginfo.getLogoutUrl());
				}
			});
			logoutButton.setStyleName("logoutbutton");
			logoutPanel.add(logoutButton);
		} else {
			Window.alert("loginfo null");
		}
	}

	/**
	 * Ausschließlich das linke Auswahlmenu anzeigen
	 */
	public void showMenuOnly() {
		if (treeViewMenu != null) {
			formPanel.clear();
		}
		Label noThing = new Label("Nichts anzuzeigen.");
		formPanel.add(noThing);
	}

	/**
	 * EInen Kontakt anzeigen
	 * 
	 * @param c, Kontakt, der angezeigt werden soll
	 */
	public void showContact(Contact c) {
		if (this.scForm == null) {
			scForm = new ShowContactForm();
			scForm.setEditor(this);
			scForm.setUser(this.currentUser);
			scForm.setStyleName("scForm");
		}
		formPanel.clear();
		scForm.setContact(c);
		formPanel.add(scForm);
	}

	/**
	 * Einen Kontakt editieren.
	 * 
	 * @param c, Kontakt, der editiert werden soll
	 */
	public void editContact(Contact c) {
		if (c != null) {
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
		} else {
			Window.alert("Kontakt anzeigen ist null");
		}
	}

	/**
	 * Eine EditContactForm für einen neuen Kontakt anzeigen
	 * 
	 * @param c, neuer Kontakt, der angelegt werden soll
	 */
	public void newContact(Contact c) {
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

	/**
	 * Eine ContactListForm für eine neue Kontaktliste anzeigen.
	 * 
	 * @param cl, neue Kontaktliste, die angelegt werden soll
	 */
	public void newContactList(ContactList cl) {
		// if (this.clForm == null) {
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

	/**
	 * Eine Kontaktliste in einer ContactListForm anzeigen
	 * 
	 * @param cl, Kontaktliste, die angezeigt werden soll
	 */
	public void showContactList(ContactList cl) {
		if (cl != null) {
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
		} else {
			Window.alert("Kontaktliste anzeigen ist null");
		}
	}

	/**
	 * ContactCollaborationFOrm anzeigen, um einen Kontakt neu zu teilen.
	 * 
	 * @param c, Kontakt, der geteilt werden soll
	 */
	public void showContactCollab(Contact c) {
		GWT.log("contactCollab");
		if (this.ccForm == null) {
			ccForm = new ContactCollaborationForm();
			ccForm.setEditor(this);
		}

		formPanel.clear();
		ccForm.setContact(c);
		// ccForm.setUser(loginfo.getCurrentUser());
		formPanel.add(ccForm);
	}

	/**
	 * Die Kollaborationen zwischen Nutzern und einem Kontakt bearbeiten
	 * 
	 * @param c, Kontakt, dessen Kollaborationen bearbeitet werden sollen.
	 */
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

	/**
	 * Die ContactListCollaborationForm anzeigen, um Teilhaberschaften einer
	 * Kontaktliste zu bearbeiten
	 * 
	 * @param cl, Kontaktliste, für die Teilhaberschaften bearbeitet werden sollen
	 */
	public void showContactListCollab(ContactList cl) {
		GWT.log("contactListCollab");
		if (this.clcForm == null) {
			clcForm = new ContactListCollaborationForm();
			clcForm.setEditor(this);
			clcForm.setUser(this.currentUser);
		}
		formPanel.clear();
		clcForm.clear();
		clcForm.setContactList(cl);
		formPanel.add(clcForm);
		// formPanel.insert(clcForm, 0);
	}

	/**
	 * Die Searchform anzeigen, die das Durchsuchen einer Kotnaktliste ermöglicht.
	 * 
	 * @param cl, Kontaktliste, die durchsucht werden soll
	 */
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

	/**
	 * Zuruück zur Anzeige einer Kontaktliste
	 * 
	 * @param cl, Kontaktliste, die angezeigt werden soll
	 */
	public void returnToContactListForm(ContactList cl) {
		if (this.clForm == null) {
			clForm = new ContactListForm();
			scForm.setEditor(this);
			scForm.setUser(this.currentUser);
		}
		// addContactListToTree(cl);
		formPanel.clear();
		clForm.setCurrentList(cl);
		formPanel.add(clForm);
	}

	/**
	 * Einen Nutzer löschen. Löscht den Nutzer permanent aus dem System, Vorsicht
	 * beim Aufruf diser Methode!
	 */
	public void deleteUser() {
		editorService.deleteUser(currentUser, new DeleteUserCallback());
	}

	/**
	 * Contact zum Menü hinzufügen
	 * 
	 * @param
	 */
	public void addContactToTree(Contact c) {
		treeViewMenu.addContact(c);
	}

	/**
	 * Kontaktliste zum TreeMenu hinzufügen
	 * 
	 * @param cl, Kontaktliste, die hinzugefügt werden soll
	 */
	public void addContactListToTree(ContactList cl) {
		treeViewMenu.addContactList(cl);
	}

	/**
	 * Einen Kontakt einer Liste im Tree hinzufügen
	 * 
	 * @param cl Kontaktliste, der der Kontakt hinzugefügt werden soll
	 * @param c  Kontakt, der der Liste hinzugefügt werden soll
	 */
	public void addContactToListInTree(ContactList cl, Contact c) {
		treeViewMenu.addContactToList(cl, c);
	}

	/**
	 * Einen Kontakt in allen Tabs und im Tree aktualisieren * @param c Kontakt, der
	 * aktualisiert werden soll
	 */
	public void updateContactInTree(Contact c) {
		treeViewMenu.updateContact(c);
	}

	/**
	 * Kontaktliste im Tree aktualisieren
	 * 
	 * @param cl Kontaktliste, die aktualisiert werden soll
	 */
	public void updateContactListInTree(ContactList cl) {
		treeViewMenu.updateContactListInTree(cl);
	}

	/**
	 * Einen Kontakt aus einer Liste im Tree entfernen
	 * 
	 * @param cl, Kontaktliste, aus der der Kontakt entfernt werden soll
	 * @param c, Kontakt, der aus der Liste entfernt werden soll
	 */
	public void removeContactFromContactListInTree(ContactList cl, Contact c) {
		treeViewMenu.removeContactOfContactList(cl, c);
	}

	/**
	 * Kontakt aus dem Menu vollständig entfernen.
	 * 
	 * @param c Kontakt, der entfernt werden soll
	 */
	public void removeContact(Contact c) {
		treeViewMenu.removeContact(c);
	}

	/**
	 * Kontaktliste aus dem TreeMenu entfernen
	 * 
	 * @param cl, Kontaktliste, die entfernt werden soll
	 */
	public void removeContactListFromTree(ContactList cl) {
		treeViewMenu.removeContactListFromTree(cl);
	}

	/**
	 * ClickHandler, der die DeleteUserDialogBox zur Anzeige bringe
	 * 
	 * @author Anders
	 *
	 */
	private class DeleteUserClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			DeleteUserDialogBox deleteBox = new DeleteUserDialogBox();
			deleteBox.show();
		}
	}

	/**
	 * DialogBox, die den Nutzer fragt, ob er sein Konto wirklich löschen möchte
	 * 
	 * @author Anders
	 */
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
			confirmButton.addClickHandler(new DeleteClickHandler());
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

			ContactList newContactList = new ContactList();
			newContactList(newContactList);
		}
	}

	/**
	 * RPC++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	private class DeleteUserCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Da ist etwas schiefgegangen, bitte versuchen Sie es erneut");
		}

		@Override
		public void onSuccess(Void v) {
			try {
				Window.alert("Account erfolgreich gelöscht");
				Window.Location.assign(loginfo.getLogoutUrl());

			} catch (Exception e) {
				Window.alert("Account erfolgreich gelöscht");
				Editor ed = new Editor();
				ed.onModuleLoad();
			}
		}
	}
}
