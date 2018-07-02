package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.*;
import de.hdm.group11.jabics.shared.bo.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
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

public class ContactCollaborationForm extends HorizontalPanel {

	EditorAdmin e;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Contact sharedContact;

	/**
	 * TODO: all user wird nicht wirklich benötigt
	 */
	private ArrayList<JabicsUser> allUser;
	ArrayList<JabicsUser> finalUser = new ArrayList<JabicsUser>();
	JabicsUser selectedUser;
	JabicsUser suggestedUser;

	Button exit, addButton, removeButton, shareContact, shareContactWUser;

	MultiWordSuggestOracle oracle;
	SuggestBox suggestBox;

	TextColumn<JabicsUser> username;

	CellTable<JabicsUser> userTable;
	ListDataProvider<JabicsUser> userDataProvider;
	SingleSelectionModel<JabicsUser> userSelectionModel;
	MultiSelectionModel<PValue> multiSelectionModel;

	HashSet<PValue> selectedPV = new HashSet<PValue>();
	CellTable<PValue> valueTable;
	ListDataProvider<PValue> valueProvider;

	Column<PValue, Boolean> checkbox;
	Column<PValue, String> property;
	Column<PValue, String> propertyvalue;
	ContactForm cf;

	Grid grid;
	
	public void onLoad() {

		allUser = new ArrayList<JabicsUser>();
		retrieveUser();

	}

	/**
	 * Die Contact Form das erste Mal erstellen, Tabellen hinzufügen und alles verknüpfen
	 */
	public ContactCollaborationForm() {

		// Alles, was mit der PVal Tabelle zu tun hat
		valueTable = new CellTable<PValue>();
		
		valueProvider = new ListDataProvider<PValue>();
		valueProvider.addDataDisplay(valueTable);

		checkbox = new Column<PValue, Boolean>(new CheckboxCell(true, false)) {
			public Boolean getValue(PValue object) {
				return multiSelectionModel.isSelected(object);
			}
		};
		property = new Column<PValue, String>(new TextCell()) {
			public String getValue(PValue object) {
				return object.getProperty().getLabel();
			}
		};
		propertyvalue = new Column<PValue, String>(new TextCell()) {
			public String getValue(PValue object) {
				return object.toString();
			}
		};
		
		
		valueTable.addColumn(checkbox, "Auswahl");
		valueTable.setColumnWidth(checkbox, "10px");
		valueTable.addColumn(property, "Merkmal");
		valueTable.setColumnWidth(property, "50px");
		valueTable.addColumn(propertyvalue, "Wert");
		valueTable.setColumnWidth(propertyvalue, "50px");

		multiSelectionModel = new MultiSelectionModel<PValue>();
		// Bei Auswahl ausgewählte PValues in finalPV speichern
		multiSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedPV = (HashSet<PValue>) multiSelectionModel.getSelectedSet();
				for (PValue pv : selectedPV) {
					GWT.log("Auswahl:" + pv.getStringValue());
				}
			}
		});
		valueTable.setSelectionModel(multiSelectionModel);

		
		
		//####################### Alles, was mit der Auwahl der Nutzer zu tun hat
		
		userTable = new CellTable<JabicsUser>();
		
		userDataProvider = new ListDataProvider<JabicsUser>();
		userDataProvider.addDataDisplay(userTable);

		oracle = new MultiWordSuggestOracle();
		suggestBox = new SuggestBox(oracle);

		/**
		 * selectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch
		 * die Suggestbox ausgewählt wurde. Dieser wird durch Klick auf den button
		 * "Nutzer hinzufügen" zur liste der zu teilenden Nutzer hinzugefügt
		 */
		suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> sel) {
				for (JabicsUser u : allUser) {
					GWT.log(suggestBox.getValue());
					if (suggestBox.getValue().contains(u.getUsername())
							&& suggestBox.getValue().contains(u.getEmail())) {
						GWT.log("Nutzer erkant");

						suggestedUser = u;
					}
				}
			}
		});
		
		username = new TextColumn<JabicsUser>() {
			public String getValue(JabicsUser u) {
				return u.getUsername();
			}
		};
		userTable.addColumn(username, "Nutzer");

		userSelectionModel = new SingleSelectionModel<JabicsUser>();
		userSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedUser = userSelectionModel.getSelectedObject();
			}
		});
		userTable.setSelectionModel(userSelectionModel);


		// +++++++++++++Alle Buttons
		
		removeButton = new Button("Nutzer entfernen");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (selectedUser != null) {
					finalUser.remove(selectedUser);
					userDataProvider.setList(finalUser);
					userDataProvider.flush();
				}
			}
		});
		addButton = new Button("Nutzer hinzufügen");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (suggestedUser != null) {
					finalUser.add(suggestedUser);
				}
				userDataProvider.setList(finalUser);
				suggestBox.setText("");
				userDataProvider.refresh();
				userDataProvider.flush();
			}
		});
		shareContactWUser = new Button("Für ausgewählten Nutzer freigeben");
		shareContactWUser.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				if (selectedUser != null) {
					if (!selectedPV.isEmpty()) {
						shareContactWithUser(selectedUser);
					} else
						Window.alert(
								"Keine Ausprägungen ausgewählt. Bitte wählen Sie mindestens eine Eigeschaftsausprägung aus");
				} else
					Window.alert("Kein Nutzer ausgewählt! Bitte klicken sie auf einen Nutzer in der Tabelle links.");
			}
		});
		shareContact = new Button("Für alle angegebenen Nutzer freigeben");
		shareContact.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				Window.alert(
						"Achtung! Damit überschreibst du alle Freigaben mit allen ausgewählten Nutzern mit den aktuell ausgewählten Eigenschaften");
				shareContactWithAll();
				e.returnToContactForm(sharedContact);
			}
		});

		exit = new Button("Abbrechen/Zurück");
		exit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				e.returnToContactForm(sharedContact);
			}
		});
	}



	public void continueOnLoad() {

		fillSuggestBox();
		fillPValueBox(sharedContact.getValues());

		GWT.log("collab4");
		grid = new Grid(5, 4);
		// grid.setSize("500px", "400px");
		grid.setWidget(0, 0, suggestBox);

		grid.setWidget(0, 1, addButton);
		grid.setWidget(1, 0, userTable);
		grid.setWidget(2, 0, removeButton);
		grid.setWidget(1, 2, valueTable);
		grid.setWidget(3, 3, shareContact);
		grid.setWidget(3, 2, shareContactWUser);
		grid.setWidget(3, 0, exit);

		this.add(grid);
	}
	// selUser.getResources und getRowElement

	public void fillSuggestBox() {
		GWT.log("SuggestBox");

		// SuggestBox mit Optionen befüllen
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
		suggestBox.setLimit(5);
	}

	public void setEditor(EditorAdmin e) {
		GWT.log("Editor in Collab setzen");
		this.e = e;
	}

	public void fillPValueBox(ArrayList<PValue> pv) {

		valueProvider.setList(pv);
		valueProvider.flush();

	}

	/**
	 * Führt den RPC zur freigabe einens Kontakts mit den ausgewählten Parametern
	 * durch.
	 */
	public void shareContactWithUser(JabicsUser u) {
		GWT.log(selectedUser.getEmail());

		for (PValue pv : selectedPV) {
			editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
		}

		editorService.addCollaboration(sharedContact, u, new AddContactCollaborationCallback());

		e.returnToContactForm(sharedContact);

	}

	/**
	 * Führt den RPC zur freigabe einens Kontakts mit allen ausgewählten Nutzern mit
	 * den ausgewählten Parametern durch.
	 */
	public void shareContactWithAll() {
		if (!finalUser.isEmpty()) {
			/*
			 * oder aber: for (User u: ldp.getList()) {
			 */
			for (JabicsUser u : finalUser) {
				for (PValue pv : selectedPV) {
					editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
				}
				editorService.addCollaboration(sharedContact, u, new AddContactCollaborationCallback());
			}
		} else {
			Window.alert("Keine Nutzer ausgewählt");
		}

	}

	public void setContact(Contact c) {
		if (c != null) {
			this.sharedContact = c;
		} else {
			Window.alert("Freigabe nicht möglich, kein Kontakt ausgewählt");
		}
	}

	private void retrieveUser() {
		GWT.log("allUser");
		editorService.getAllNotCollaboratingUser(sharedContact, new GetAllNotCollaboratingUserCallback());
		GWT.log("allUserfetisch");
	}

	private void setAllUser(ArrayList<JabicsUser> user) {
		GWT.log("alleNutzersetzen");
		this.allUser = user;
		for (JabicsUser u : this.allUser) {
			GWT.log(u.getEmail());
		}
	}

	private class AddPVCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			Window.alert("PV konnte nicht geteilt werden");
		}

		public void onSuccess(Void v) {
			if (v != null) {
				Window.alert("PV erfolgreich geteilt!");
			}
		}
	}

	private class AddContactCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			Window.alert("Kontakt konnte nicht geteilt werden");
		}

		public void onSuccess(Void v) {
			if (v != null) {
				Window.alert("Kontakt erolgreich geteilt!");
				// e.returnToContact();
			}
		}
	}

	public class GetAllUsersCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				GWT.log("alleNutzergesetzt   " + user.get(1).getEmail());
				setAllUser(user);
				continueOnLoad();
			}
		}
	}

	public class GetAllNotCollaboratingUserCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				GWT.log("alleNutzergesetzt   " + user.get(1).getEmail());
				setAllUser(user);
				continueOnLoad();
			}

		}
	}
}
