package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.GetAllNotCollaboratingUserCallback;
import de.hdm.group11.jabics.shared.*;
import de.hdm.group11.jabics.shared.bo.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.cell.client.*;
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
import com.google.gwt.dom.client.Style.Unit;

public class ContactListCollaborationForm extends HorizontalPanel {

	Editor e;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Button exit, addButton, removeButton, shareList, deShareList;

	MultiWordSuggestOracle oracle;
	SuggestBox suggestBox;

	CellTable<JabicsUser> selUser;
	ListDataProvider<JabicsUser> userDataProvider;
	MultiSelectionModel<JabicsUser> userSelectionModel;

	private ArrayList<JabicsUser> allUser;
	ArrayList<JabicsUser> selectedUser;
	ContactList sharedContactList;

	JabicsUser singleSelectedUser;
	JabicsUser suggestedUser;
	TextColumn<JabicsUser> username;

	public void onLoad() {

		deShareList = new Button("Für alle angegebenen Nutzer freigeben");
		deShareList.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				Window.alert(
						"Achtung! Damit überschreibst du alle Freigaben mit allen ausgewählten Nutzern mit den aktuell ausgewählten Eigenschaften");
				deshareContactWithAll();
				// e.returnToContactListForm(sharedContactList);
			}
		});
		GWT.log("collab2");

		shareList = new Button("Für alle angegebenen Nutzer freigeben");
		shareList.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				Window.alert(
						"Achtung! Damit überschreibst du alle Freigaben mit allen ausgewählten Nutzern mit den aktuell ausgewählten Eigenschaften");
				shareContactWithAll();
				// e.returnToContactListForm(sharedContactList);

			}
		});

		exit = new Button("Abbrechen/Zurück");
		exit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				e.returnToContactListForm(sharedContactList);
			}
		});
		GWT.log("collab3");

		allUser = new ArrayList<JabicsUser>();
		retrieveUser();
	}

	public void continueOnLoad() {

		createSuggestBox();

		this.add(addButton);
		this.add(exit);
		this.add(removeButton);
		this.add(shareList);
		// this.add(shareContactWUser);
		this.add(suggestBox);
		this.add(selUser);

	}
	
	public void addSelectedUser(JabicsUser c) {
		this.selectedUser.add(c);
	}

	/**
	 * Führt den RPC zur freigabe einens Kontakts mit allen ausgewählten Nutzern mit
	 * den ausgewählten Parametern durch.
	 */
	public void shareContactWithAll() {
		if (!selectedUser.isEmpty()) {

			for (JabicsUser u : selectedUser) {
				editorService.addCollaboration(sharedContactList, u, new AddContactListCollaborationCallback());
			}
		} else {
			Window.alert("Keine Nutzer ausgewählt");
		}

	}

	public void deshareContactWithAll() {
		if (!selectedUser.isEmpty()) {

			for (JabicsUser u : selectedUser) {
				GWT.log("deshareContactWithAll" + u.getUsername());
				// editorService.addCollaboration(sharedContactList, u, new
				// AddContactListCollaborationCallback());
				editorService.deleteCollaboration(sharedContactList, u, new DeleteContactListCollaborationCallback());
			}
		} else {
			Window.alert("Keine Nutzer ausgewählt");
		}
	}

	public void setContactList(ContactList cl) {
		if (cl != null) {
			this.sharedContactList = cl;
		} else {
			Window.alert("Freigabe nicht möglich, da keine Kontaktliste ausgewählt.");
		}
	}

	public void setEditor(Editor e) {
		GWT.log("Editor in ContactlistCollab setzen");
		this.e = e;
	}

	private void retrieveUser() {
		GWT.log("allUser");
		editorService.getAllNotCollaboratingUser(sharedContactList, new GetAllNotCollaboratingUserCallback());
		editorService.getCollaborators(sharedContactList, new GetAllCollaboratorsCallback());
		GWT.log("allUserfetisch");
	}

	private void setAllUser(ArrayList<JabicsUser> user) {
		GWT.log("alleNutzersetzen");
		this.allUser = user;
		for (JabicsUser u : this.allUser) {
			GWT.log(u.getEmail());
		}
	}

	public void setAllCollaborators(ArrayList<JabicsUser> user) {
		GWT.log("setAllCollaborators");

		selectedUser = user;

	}

	public void createSuggestBox() {
		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		GWT.log("SuggestBox");

		userDataProvider = new ListDataProvider<JabicsUser>();
		selUser = new CellTable<JabicsUser>();

		userDataProvider.setList(selectedUser);
		userDataProvider.addDataDisplay(selUser);

		userSelectionModel = new MultiSelectionModel<JabicsUser>();

		userSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				if (!userSelectionModel.getSelectedSet().isEmpty()) {
					JabicsUser[] users = (JabicsUser[]) userSelectionModel.getSelectedSet().toArray();
					if (users.length == 1) {
						singleSelectedUser = users[0];
					} else {
						for (JabicsUser u : users) {
							addSelectedUser(u);
						}

					}
				}
			}
		});
		selUser.setSelectionModel(userSelectionModel);

		addButton = new Button("Nutzer hinzufügen");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (suggestedUser != null) {
					selectedUser.add(suggestedUser);
				}
				userDataProvider.setList(selectedUser);
				suggestBox.setText("");
				userDataProvider.refresh();
				userDataProvider.flush();
			}
		});
		GWT.log("SuggestBox4");
		removeButton = new Button("Nutzer entfernen");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (selectedUser != null) {
					selectedUser.remove(selectedUser);
					userDataProvider.setList(selectedUser);
					userDataProvider.flush();
				}
			}
		});
		GWT.log("SuggestBox5");
		username = new TextColumn<JabicsUser>() {
			public String getValue(JabicsUser u) {
				return u.getUsername();
			}
		};
		selUser.addColumn(username, "Nutzer");

		/**
		 * SuggestBox hinzufügen und mit Optionen befüllen
		 */
		oracle = new MultiWordSuggestOracle();
		suggestBox = new SuggestBox(oracle);

		for (JabicsUser u : allUser) {
			GWT.log("SuggestBoxalluser");
			try {
				oracle.add(u.getUsername() + " " + u.getEmail());
				GWT.log("Nutzer zu Sug hinzugefügt");
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
		 * selectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch
		 * die suggestbox ausgewählt wurde. Dieser wird durch Klick auf den button
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

	private class AddContactListCollaborationCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Kontaktliste konnte nicht geteilt werden");

		}

		@Override
		public void onSuccess(Void result) {
			if (result != null) {
				Window.alert("Kontakt erolgreich geteilt!");

			}

		}
	}

	private class DeleteContactListCollaborationCallback implements AsyncCallback<Void> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Kontaktliste konnte nicht entteilt werden");

		}

		@Override
		public void onSuccess(Void result) {
			if (result != null) {
				Window.alert("Kontakt erolgreich entteilt!");

			}

		}
	}

	public class GetAllNotCollaboratingUserCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				GWT.log("GetAllNotCollaboratingUserCallback onSuccess");
				// GWT.log("alleNutzergesetzt " + user.get(1).getEmail());
				setAllUser(user);
				continueOnLoad();
			}

		}
	}

	public class GetAllCollaboratorsCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				GWT.log("GetAllNotCollaboratingUserCallback onSuccess");
				// GWT.log("alleNutzergesetzt " + user.get(1).getEmail());
				setAllCollaborators(user);
				continueOnLoad();
			}

		}
	}
}
