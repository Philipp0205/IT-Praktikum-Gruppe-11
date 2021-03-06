package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.CellTableResources;
import de.hdm.group11.jabics.shared.*;
import de.hdm.group11.jabics.shared.bo.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ListDataProvider;

public class ContactListCollaborationForm extends VerticalPanel {

	private EditorAdmin e;
	private EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	private HorizontalPanel suggestionPanel = new HorizontalPanel();
	private HorizontalPanel listPanel = new HorizontalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();

	private ContactList sharedContactList;
	private JabicsUser u;

	private Button exit, addButton, removeButton, shareList, deShareList;

	private Boolean otherCallbackArrived = false;

	private MultiWordSuggestOracle oracle;
	private SuggestBox suggestBox;
	private JabicsUser suggestedUser;
	private JabicsUser singleSelectedUser;

	// Für jede der beiden Auswahlmöglichkeiten CellTables, DataProvider,
	// SelectionModels, ArrayLists aus Usern
	private CellTable<JabicsUser> existingCollabTable;
	private CellTable<JabicsUser> newCollabTable;
	private ListDataProvider<JabicsUser> existingUserDataProvider;
	private ListDataProvider<JabicsUser> newCollabDataProvider;

	private MultiSelectionModel<JabicsUser> existingUserSelectionModel;
	private SingleSelectionModel<JabicsUser> newUserSelectionModel;
	private ArrayList<JabicsUser> existingCollaborators;
	private ArrayList<JabicsUser> newCollaborators;

	private ArrayList<JabicsUser> finalCollaborators;

	private ArrayList<JabicsUser> allUser;

	private TextColumn<JabicsUser> existingCollabName;
	private TextColumn<JabicsUser> newCollabName;

	private CellTableResources ctRes = GWT.create(CellTableResources.class);

	/**
	 * Konstruktor welcher einer Instanz der Klasse
	 * <code>ContactListCollaborationForm</code> erzeugt, welcher alle Objekte
	 * inistialisiert welcher die Form braucht. Darunter Fallen Objekte der Klassen
	 * <code>Button</code> <code>HorizontalPanel</code>. Des weiteren werden
	 * verschiedene <code>ClickHandler</code> der Buttons gesetzt.
	 */
	public ContactListCollaborationForm() {

		allUser = new ArrayList<JabicsUser>();
		newCollaborators = new ArrayList<JabicsUser>();
		existingCollaborators = new ArrayList<JabicsUser>();
		finalCollaborators = new ArrayList<JabicsUser>();

		oracle = new MultiWordSuggestOracle();
		suggestBox = new SuggestBox(oracle);

		newCollabTable = new CellTable<JabicsUser>(200, ctRes);
		existingCollabTable = new CellTable<JabicsUser>(200, ctRes);

		newCollabDataProvider = new ListDataProvider<JabicsUser>();
		existingUserDataProvider = new ListDataProvider<JabicsUser>();

		newUserSelectionModel = new SingleSelectionModel<JabicsUser>();
		existingUserSelectionModel = new MultiSelectionModel<JabicsUser>();

		listPanel.setStyleName("listpanel");

		shareList = new Button("Ausgewählten Nutzern freigeben");
		shareList.setStyleName("clcbtn");
		shareList.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				shareContactList();
			}
		});
		deShareList = new Button("Ausgewählten Nutzern entteilen");
		deShareList.setStyleName("clcbtn");
		deShareList.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				deshareContactList();
			}
		});
		exit = new Button("Fertig");
		exit.setStyleName("clcbtn");
		exit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				e.returnToContactListForm(sharedContactList);
			}
		});
		// Buttons, die es ermöglichen Nutzer einer Table hinzuzufügen
		addButton = new Button("Nutzer hinzufügen");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (suggestedUser != null) {
					newCollaborators.add(suggestedUser);
				}
				// newCollabDataProvider.setList(newCollaborators);
				suggestBox.setText("");
				newCollabDataProvider.refresh();
				newCollabDataProvider.flush();
			}
		});
		removeButton = new Button("Nutzer entfernen");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (newCollaborators != null) {
					newCollaborators.remove(singleSelectedUser);
					newCollabDataProvider.setList(newCollaborators);
					newCollabDataProvider.flush();
				}
			}
		});

		buttonPanel.add(shareList);
		buttonPanel.add(deShareList);

	}

	/**
	 * Wird beim kaden der Form aufgeruden. Es werden alle Objekte deklariert
	 * welchen für die Funktionalität der Klasse gebraucht werden.
	 */
	public void onLoad() {
		retrieveUser();

		this.add(suggestionPanel);
		this.add(listPanel);
		this.add(buttonPanel);
		this.add(exit);

	}

	/**
	 * Die onLoad weiterführen, nachdem alle Nutzer und alle Kollaboratoren
	 * aufgerufen wurde.
	 */
	public void continueOnLoad() {

		createTables();
		createSuggestBox();

		listPanel.add(newCollabTable);
		listPanel.add(existingCollabTable);

		suggestionPanel.add(suggestBox);
		suggestionPanel.add(addButton);
		suggestionPanel.add(removeButton);

		suggestBox.setStyleName("TextBox");
		addButton.setStyleName("clcbtn");
		removeButton.setStyleName("clcbtn");
	}

	public void getContacts() {
		editorService.getContactsOfList(sharedContactList, u, new AsyncCallback<ArrayList<Contact>>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(ArrayList<Contact> contacts) {
				setContacts(contacts);
				updateShareStatus();
			}
		});
	}

	/**
	 * Führt den RPC zur freigabe einens Kontakts mit allen ausgewählten Nutzern mit
	 * den ausgewählten Parametern durch.
	 */
	public void shareContactList() {
		if (!newCollaborators.isEmpty()) {

			for (JabicsUser u : newCollaborators) {
				editorService.addCollaboration(sharedContactList, u, new AddContactListCollaborationCallback());
			}
		} else {
			Window.alert("Keine Nutzer hinzugefügt");
		}
	}

	/**
	 * Ennteilt einen Kontakt. Dies bedeuetet, dass die Kollaboration des Kontakts
	 * gelöscht wird und somit andere Nutzer keinen Zugriff mehr auf den Kontakt
	 * haben.
	 */
	public void deshareContactList() {
		if (!finalCollaborators.isEmpty()) {

			for (JabicsUser u : finalCollaborators) {
				editorService.deleteCollaboration(sharedContactList, u, new DeleteContactListCollaborationCallback());
			}
		} else {
			Window.alert("Keine Nutzer ausgewählt");
		}
	}

	/**
	 * Setzt die Kontaktliste, mit der dann später weitere Aktionen durchgeführt
	 * werden können wie z.B. teilen.
	 * 
	 * @param cl
	 *            Kontaktliste, die ausgewählt werden soll.
	 */
	public void setContactList(ContactList cl) {
		if (cl != null) {
			this.sharedContactList = cl;
		} else {
			Window.alert("Freigabe nicht möglich, da keine Kontaktliste ausgewählt.");
		}
	}

	/**
	 * Setzt die Kontakte der Liste, mit der dann später weitere Aktionen
	 * durchgeführt werden können wie z.B. teilen.
	 * 
	 * @param c
	 *            Liste von <code>Contact</code>, die in der Liste liegen.
	 */
	public void setContacts(ArrayList<Contact> c) {
		if (c != null) {
			this.sharedContactList.setContacts(c);
			for (Contact cl : sharedContactList.getContacts()) {
			}
		} else {
			Window.alert("Kontakte hinzufügen null");
		}
	}

	/**
	 * Setzt den User der ContactListCollaborationForm
	 * 
	 * @param u
	 *            User, der gesetzt werden soll.
	 */
	public void setUser(JabicsUser u) {
		this.u = u;
	}

	/**
	 * Setzt den Editor der ContactListCollaborationForm
	 * 
	 * @param e
	 *            Editor, der gesetzt werden soll.
	 */
	public void setEditor(EditorAdmin e) {
		this.e = e;
	}

	/**
	 * Diese Methode holt sowohl alle Nutzer als auch alle bestehenden
	 * Kollaboratoren vom Server. Im zuletzt ankommenden Callback wird
	 * continueOnLoad() aufgerufen.
	 */
	private void retrieveUser() {
		editorService.getAllNotCollaboratingUser(sharedContactList, new GetAllNotCollaboratingUserCallback());
		editorService.getCollaborators(sharedContactList, new GetAllCollaboratorsCallback());
	}

	/**
	 * Setzt eine Variable, welche alle Nutzer enthält.
	 * 
	 * @param user
	 *            User, die übergeben werde sollen. In diesem Fall sind das alle
	 *            User.
	 */
	private void setAllUser(ArrayList<JabicsUser> user) {
		this.allUser = user;
	}

	/**
	 * Sezt die alle Kollaboratoren.
	 * 
	 * @param user
	 *            User, die gesetzt werden sollen.
	 */
	public void setAllCollaborators(ArrayList<JabicsUser> user) {
		this.existingCollaborators = user;
		existingUserDataProvider.setList(existingCollaborators);
		// existingUserDataProvider.addDataDisplay(existingCollabTable);

		this.existingUserDataProvider.flush();
	}

	public void updateShareStatus() {
		for (Contact c : sharedContactList.getContacts()) {
			c.setShareStatus(BoStatus.IS_SHARED);
			e.updateContactInTree(c);
		}

		sharedContactList.setShareStatus(BoStatus.IS_SHARED);
		e.updateContactListInTree(sharedContactList);
	}

	/**
	 * Erstellt die tabellenfämige Ansicht, welche anzeigt mit welchen Usern die
	 * Kontaktliste bereits geteilt wurde und die neu hinzugeüfgten Usern mit denen
	 * der Teilvorgang gerade stattfindet.
	 */
	public void createTables() {

		/**
		 * Provider erstellen, der ausgewählte Nutzer einer Tabelle zur Verfügung stellt
		 */
		newUserSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				singleSelectedUser = newUserSelectionModel.getSelectedObject();
			}
		});

		existingUserSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				if (!existingUserSelectionModel.getSelectedSet().isEmpty()) {
					HashSet<JabicsUser> users = (HashSet<JabicsUser>) existingUserSelectionModel.getSelectedSet();
					for (JabicsUser u : users) {
						finalCollaborators.add(u);
					}
				}
			}
		});

		newCollabTable.setSelectionModel(newUserSelectionModel);
		newCollabDataProvider.setList(newCollaborators);
		newCollabDataProvider.addDataDisplay(newCollabTable);

		existingCollabTable.setSelectionModel(existingUserSelectionModel);
		existingUserDataProvider.setList(existingCollaborators);
		existingUserDataProvider.addDataDisplay(existingCollabTable);

		existingCollabName = new TextColumn<JabicsUser>() {
			public String getValue(JabicsUser u) {
				return u.getUsername();
			}
		};
		newCollabName = new TextColumn<JabicsUser>() {
			public String getValue(JabicsUser u) {
				return u.getUsername();
			}
		};
		existingCollabTable.addColumn(existingCollabName, "Bereits geteilt mit");
		newCollabTable.addColumn(newCollabName, "Neu teilen mit");
	}

	/**
	 * SuggestBox um Nutzer hinzufügen.
	 */
	public void createSuggestBox() {

		for (JabicsUser u : allUser) {
			try {
				oracle.add(u.getUsername() + " " + u.getEmail());
			} catch (NullPointerException e) {
				Window.alert(
						"setzen des nutzernamens oder mailadresse in sugstbox failed, Nutzer mit Id: " + u.getId());
				try {
					oracle.add(u.getUsername());
				} catch (NullPointerException ex) {
					Window.alert("setzen des nutzernamens auch fehlgeschlagen, Nutzer mit Id: " + u.getId());
				}
			}
		}

		/**
		 * SelectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch
		 * die Suggestbox ausgewählt wurde. Dieser wird durch Klick auf den button
		 * "Nutzer hinzufügen" zur liste der zu teilenden Nutzer hinzugefügt
		 */
		suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> sel) {
				for (JabicsUser u : allUser) {
					if (suggestBox.getValue().contains(u.getUsername())
							&& suggestBox.getValue().contains(u.getEmail())) {
						suggestedUser = u;
					}
				}
			}
		});

		suggestBox.setLimit(5);

	}

	/**
	 * Callback welches bei dem Erstellen einer neuenen Kollarboration einer
	 * Kontaktliste ausgeführt wird. Bei einem erfolgreichem Callback wird der
	 * ShareStatus aktuallsiert und die Kontaktliste wird im Menü aktuallisiert.
	 *
	 */
	private class AddContactListCollaborationCallback implements AsyncCallback<JabicsUser> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Kontaktliste konnte nicht geteilt werden");
		}

		@Override
		public void onSuccess(JabicsUser result) {
			if (result != null) {
				existingCollaborators.add(result);
				existingUserDataProvider.setList(existingCollaborators);
				existingUserDataProvider.refresh();
				existingUserDataProvider.flush();
				for (JabicsUser uu : newCollaborators) {
					if (uu.getId() == result.getId()) {
						newCollaborators.remove(uu);
						newCollabDataProvider.flush();
					}
				}
				// Kontakte holen, um sie im Tree view upzudaten
				getContacts();

			}
		}
	}

	/**
	 * Callback welcher bei Löschen einer Kollaboration einer Kontaktliste
	 * ausgeführt wird. Bein einem erfolgreichen Callback wird die Kontaktliste aus
	 * den Menüs entfernt.
	 *
	 */
	private class DeleteContactListCollaborationCallback implements AsyncCallback<ContactList> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Kontaktliste konnte nicht entteilt werden");
		}

		@Override
		public void onSuccess(ContactList result) {
			if (result != null) {
				for (JabicsUser u : finalCollaborators) {
					existingCollaborators.remove(u);
				}
				existingUserDataProvider.flush();
				for (Contact c : result.getContacts()) {
					e.updateContactInTree(c);
				}

				e.updateContactListInTree(result);
			}
		}
	}

	/**
	 * Ein Callbacker wecher beim Beziehen aller Kontakte die nicht Kollaborieren
	 * ausgeführt wird.
	 * 
	 *
	 */
	private class GetAllNotCollaboratingUserCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				setAllUser(user);
			}
			if (!otherCallbackArrived) {
				otherCallbackArrived = true;
			} else {
				continueOnLoad();
			}
		}
	}

	/**
	 * Callback, welcher beim Beziehen aller Kollaboratoren einer Kontaktliste
	 * ausgeführt wird.
	 *
	 */
	private class GetAllCollaboratorsCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Kollaboratoren konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				setAllCollaborators(user);
			}
			if (!otherCallbackArrived) {
				otherCallbackArrived = true;
			} else {
				continueOnLoad();
			}
		}
	}
}
