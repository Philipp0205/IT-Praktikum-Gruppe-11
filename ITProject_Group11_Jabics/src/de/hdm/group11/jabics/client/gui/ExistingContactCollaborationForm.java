package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;

public class ExistingContactCollaborationForm extends HorizontalPanel {
	Editor e;
	JabicsUser u;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Contact sharedContact;
	ArrayList<JabicsUser> sharedUser = new ArrayList<JabicsUser>();
	JabicsUser[] selectedUser;

	Button exit, addButton, removeButton, shareContact, shareContactWUser;

	Label valueLabel;

	CellTable<PValue> selValues;
	ArrayList<PValue> selectedValues;
	ArrayList<PValue> allVisibleValues;
	MultiSelectionModel<PValue> multiSelectionModel;

	TextColumn<JabicsUser> username;

	CellTable<JabicsUser> selUser;
	ListDataProvider<JabicsUser> ldp;
	MultiSelectionModel<JabicsUser> userSelectionModel;

	HashSet<PValue> finalPV = new HashSet<PValue>();

	ListDataProvider<PValue> valueProvider;

	Column<PValue, Boolean> checkbox;
	Column<PValue, String> property;
	Column<PValue, String> propertyvalue;

	ContactForm cf;

	Grid grid;

	public void onLoad() {
		retrieveSharedUser();
	}

	public void continueOnLoad() {

		shareContact = new Button("Für alle angegebenen Nutzer ändern");
		shareContact.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				Window.alert(
						"Achtung! Damit überschreibst du alle Freigaben mit allen ausgewählten Nutzern mit den aktuell ausgewählten Eigenschaften");
				updateContactWithAll();
				e.returnToContactForm(sharedContact);
			}
		});
		exit = new Button("Abbrechen/Zurück");
		exit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				e.returnToContactForm(sharedContact);
			}
		});

		valueLabel = new Label("Eigenschaften, die der ausgewählte Nutzer sehen darf");
		createSelectionBox();
		createPValueBox(sharedContact.getValues());

		GWT.log("collab4");
		grid = new Grid(5, 4);
		// grid.setSize("500px", "400px");

		grid.setWidget(0, 1, addButton);
		grid.setWidget(1, 0, selUser);
		grid.setWidget(2, 0, removeButton);
		grid.setWidget(0, 2, valueLabel);
		grid.setWidget(1, 2, selValues);
		selValues.setColumnWidth(checkbox, "10px");
		selValues.setColumnWidth(property, "30px");
		selValues.setColumnWidth(propertyvalue, "40px");
		grid.setWidget(3, 2, shareContactWUser);
		grid.setWidget(2, 2, shareContact);

		grid.setWidget(3, 0, exit);
		GWT.log("halloattach4");
		this.add(grid);
		GWT.log("collab5");
	}
	// selUser.getResources und getRowElement

	public void createSelectionBox() {
		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		GWT.log("SelectBox");

		ldp = new ListDataProvider<JabicsUser>();
		selUser = new CellTable<JabicsUser>();

		ldp.setList(sharedUser);
		ldp.addDataDisplay(selUser);

		userSelectionModel = new MultiSelectionModel<JabicsUser>();

		userSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedUser = (JabicsUser[]) userSelectionModel.getSelectedSet().toArray();
				if (selectedUser.length == 1) {
					showCollabOfUser(selectedUser[1]);
				} else {
					selValues.setVisible(false);
				}
			}
		});
		selUser.setSelectionModel(userSelectionModel);

		removeButton = new Button("Freigabe entfernen");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (selectedUser != null) {
					deleteCollabWithUser(selectedUser);
					sharedUser.remove(selectedUser);
					ldp.flush();
				}
			}
		});
		username = new TextColumn<JabicsUser>() {
			public String getValue(JabicsUser u) {
				return u.getEmail();
			}
		};
		selUser.addColumn(username, "Nutzer");

	}

	public void setEditor(Editor e) {
		GWT.log("Editor in Collab setzen");
		this.e = e;
	}

	public void setUser(JabicsUser u) {
		this.u = u;
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
		selValues.setColumnWidth(checkbox, 50, Unit.PX);
		selValues.addColumn(property, "Merkmal");
		selValues.setColumnWidth(property, 30, Unit.EM);
		selValues.addColumn(propertyvalue, "Wert");
		selValues.setColumnWidth(propertyvalue, 50, Unit.EM);
	}

	public void showCollabOfUser(JabicsUser u) {
		editorService.getPValueOf(sharedContact, u, new AsyncCallback<ArrayList<PValue>>() {
			public void onFailure(Throwable arg0) {
				Window.alert("PValue konnten nicht geladen werden");
			}

			public void onSuccess(ArrayList<PValue> pv) {
				if (pv != null) {
					setSelectedValues(pv);
				}
			}
		});
	}

	public void setSelectedValues(ArrayList<PValue> pval) {
		this.selectedValues = pval;
		for (PValue pv : selectedValues) {
			multiSelectionModel.setSelected(pv, true);
		}
	}

	/**
	 * Teilt den Kontakt mit allen ausgewählten Nutzern mit den neuen ausgewählten
	 * Eigenschaften
	 */
	public void updateContactWithAll() {
		if (!sharedUser.isEmpty()) {
			for (JabicsUser u : sharedUser) {
				updateContactShareForUser(u);
			}
		} else {
			Window.alert("Keine Nutzer ausgewählt");
		}
	}

	/**
	 * Hier findet die "Logik" des Updatens der Collaboration statt. Ausgewählte
	 * PValues werden geteilt. Die Entscheidung, ob ein solches bereits geteilt
	 * wurde und dementsprechend das Speichern überflüssig ist, übernimmt die
	 * Applikationslogik severseitig.
	 */
	public void updateContactShareForUser(JabicsUser u) {
		GWT.log(u.getEmail());

		for (PValue pv : allVisibleValues) {
			if (multiSelectionModel.isSelected(pv)) {
				GWT.log("Hinzugefügt wird: " + pv.toString());
				editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
			} else {
				GWT.log("Gelöscht wird: " + pv.toString());
				editorService.deleteCollaboration(pv, u, new AddPVCollaborationCallback());
			}
		}
	}

	/**
	 * Führt den RPC zur freigabe einens Kontakts mit den ausgewählten Parametern
	 * durch
	 */
	public void deleteCollabWithUser(JabicsUser[] u) {
		GWT.log(selectedUser[0].getEmail());

		// Wird von deleteCollaboration(Contact, user) erledigt
//		for (PValue pv : finalPV) {
//			editorService.deleteCollaboration(pv, u, new DeletePVCollaborationCallback());
//		}

		for (JabicsUser user : u) {
			editorService.deleteCollaboration(sharedContact, user, new DeleteContactCollaborationCallback());
		}
		e.returnToContactForm(sharedContact);
	}

	public void setContact(Contact c) {
		if (c != null) {
			this.sharedContact = c;
		} else {
			this.sharedContact = null;
		}
		GWT.log("Kontakt gesetzt");
	}

	private void retrieveSharedUser() {
		GWT.log("allUser");
		editorService.getCollaborators(sharedContact, new GetCollaboratorsCallback());
		GWT.log("allUserfetisch");
	}

	private void setSharedUser(ArrayList<JabicsUser> user) {
		this.sharedUser = user;
		for (JabicsUser u : this.sharedUser) {
			GWT.log(u.getEmail());
		}
	}

	public JabicsUser getUser() {
		return this.u;
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

	private class DeletePVCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			Window.alert("PV: Fail");
		}

		public void onSuccess(Void v) {
			if (v != null) {
				Window.alert("PV erfolgreich entteilt!");
			}
		}
	}

	private class DeleteContactCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			Window.alert("Contact: Fail");
		}

		public void onSuccess(Void v) {
			if (v != null) {
				Window.alert("Kontakt erolgreich entteilt!");
				// e.returnToContact();
			}

		}
	}

	class GetCollaboratorsCallback implements AsyncCallback<ArrayList<JabicsUser>> {
		public void onFailure(Throwable arg0) {
			Window.alert("Geteilte Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> u) {
			if (u != null) {
				for (JabicsUser user : u) {
					if (user.getId() == getUser().getId())
						u.remove(user);
				}
				setSharedUser(u);
				continueOnLoad();
			}

		}
	}

}
