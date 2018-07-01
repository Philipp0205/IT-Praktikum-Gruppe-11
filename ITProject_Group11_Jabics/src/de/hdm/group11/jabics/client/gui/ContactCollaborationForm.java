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

	Editor e;
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

	CellTable<JabicsUser> selUser;
	ListDataProvider<JabicsUser> userDataProvider;
	SingleSelectionModel<JabicsUser> selectionModel;
	MultiSelectionModel<PValue> multiSelectionModel;

	HashSet<PValue> finalPV = new HashSet<PValue>();
	CellTable<PValue> selValues;
	ListDataProvider<PValue> valueProvider;

	Column<PValue, Boolean> checkbox;
	Column<PValue, String> property;
	Column<PValue, String> propertyvalue;
	ContactForm cf;

	Grid grid;

	public void onLoad() {

		shareContactWUser = new Button("Für ausgewählten Nutzer freigeben");
		shareContactWUser.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				if(selectedUser != null) {
					if(!finalPV.isEmpty()) {
						shareContactWithUser(selectedUser);
					} else Window.alert("Keine Ausprägungen ausgewählt. Bitte wählen Sie mindestens eine Eigeschaftsausprägung aus");
				} else Window.alert("Kein Nutzer ausgewählt! Bitte klicken sie auf einen Nutzer in der Tabelle links.");
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
		GWT.log("collab3");
		allUser = new ArrayList<JabicsUser>();
		retrieveUser();

	}

	public void continueOnLoad() {

		createSuggestBox();
		createPValueBox(sharedContact.getValues());

		GWT.log("collab4");
		grid = new Grid(5, 4);
		// grid.setSize("500px", "400px");
		grid.setWidget(0, 0, suggestBox);

		grid.setWidget(0, 1, addButton);
		grid.setWidget(1, 0, selUser);
		grid.setWidget(2, 0, removeButton);
		grid.setWidget(1, 2, selValues);
		selValues.setColumnWidth(checkbox, 20, Unit.PX);
		selValues.setColumnWidth(property, 20, Unit.EM);
		selValues.setColumnWidth(propertyvalue, 10, Unit.EM);
		grid.setWidget(3, 3, shareContact);
		grid.setWidget(3, 2, shareContactWUser);

		grid.setWidget(3, 0, exit);
		GWT.log("halloattach4");
		this.add(grid);
		GWT.log("collab5");
	}
	// selUser.getResources und getRowElement

	public void createSuggestBox() {
		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		GWT.log("SuggestBox");

		userDataProvider = new ListDataProvider<JabicsUser>();
		selUser = new CellTable<JabicsUser>();

		// ldp.setList(allUser);
		userDataProvider.addDataDisplay(selUser);

		selectionModel = new SingleSelectionModel<JabicsUser>();

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedUser = selectionModel.getSelectedObject();
			}
		});
		selUser.setSelectionModel(selectionModel);

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
		GWT.log("SuggestBox4");
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

	public void setEditor(Editor e) {
		GWT.log("Editor in Collab setzen");
		this.e = e;
	}

	public void createPValueBox(ArrayList<PValue> pv) {
		selValues = new CellTable<PValue>();
		valueProvider = new ListDataProvider<PValue>();
		valueProvider.setList(pv);
		valueProvider.addDataDisplay(selValues);
		// Es kann sein, dass hier noch kexprovider benötigt werden
		multiSelectionModel = new MultiSelectionModel<PValue>();

		// Bei Auswahl ausgewählte PValues inf finalPV speichern
		multiSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				finalPV = (HashSet<PValue>) multiSelectionModel.getSelectedSet();
				for (PValue pv : finalPV) {
					GWT.log("Auswahl:" + pv.getStringValue());
				}

				Window.alert("Auswahl geändert");
			}
		});
		selValues.setSelectionModel(multiSelectionModel);

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

		selValues.addColumn(checkbox, "Auswahl");
		selValues.setColumnWidth(checkbox, "10px");
		selValues.addColumn(property, "Merkmal");
		selValues.setColumnWidth(property, "50px");
		selValues.addColumn(propertyvalue, "Wert");
		selValues.setColumnWidth(propertyvalue, "50px");
	}

	/**
	 * Führt den RPC zur freigabe einens Kontakts mit den ausgewählten Parametern
	 * durch.
	 */
	public void shareContactWithUser(JabicsUser u) {
		GWT.log(selectedUser.getEmail());

		for (PValue pv : finalPV) {
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
				for (PValue pv : finalPV) {
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
