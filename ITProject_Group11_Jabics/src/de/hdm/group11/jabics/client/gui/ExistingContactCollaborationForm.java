package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
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
	HashSet<JabicsUser> selectedUser;
	JabicsUser singleSelectedUser;

	Button exit, addButton, removeButton, shareContact, shareContactWAll;
	HorizontalPanel sharePanel; 
	
	Label valueLabel;

	CellTable<PValue> selValues;
	ArrayList<PValue> selectedValues;
	ArrayList<PValue> allValues;
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

	public ExistingContactCollaborationForm() {
		
		valueLabel = new Label("Eigenschaften, die der ausgewählte Nutzer sehen darf");

		selValues = new CellTable<PValue>();
		valueProvider = new ListDataProvider<PValue>();

		ldp = new ListDataProvider<JabicsUser>();
		selUser = new CellTable<JabicsUser>();
		
		ldp.addDataDisplay(selUser);
		
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
		selValues.setColumnWidth(checkbox, "50px");
		selValues.addColumn(property, "Merkmal");
		selValues.setColumnWidth(property, "30px");
		selValues.addColumn(propertyvalue, "Wert");
		selValues.setColumnWidth(propertyvalue, "50px");

		userSelectionModel = new MultiSelectionModel<JabicsUser>();

		userSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedUser = (HashSet<JabicsUser>) userSelectionModel.getSelectedSet();
				for (JabicsUser u : selectedUser) {
					GWT.log("Auswahl:" + u.getUsername());
				}
				if (selectedUser.size() == 1) {
					for (JabicsUser u : selectedUser) {
						showCollabOfUser(u);
						singleSelectedUser = u;
					}
					
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
					sharedUser.remove(singleSelectedUser);
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

		shareContact = new Button("Für ausgewählten Nutzer ändern");
		shareContact.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				Window.alert(
						"Achtung! Damit überschreibst du alle Freigaben mit dem aktuell ausgewählten Nutzer");
				updateContactShareForUser(singleSelectedUser);
				//e.returnToContactForm(sharedContact);
			}
		});
		shareContactWAll = new Button("Für alle Nutzer ändern");
		shareContact.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				Window.alert(
						"Achtung! Damit überschreibst du alle Freigaben mit allen ausgewählten Nutzern mit den aktuell ausgewählten Eigenschaften");
				updateContactWithAll();
				//e.returnToContactForm(sharedContact);
			}
		});
		
		exit = new Button("Abbrechen/Zurück");
		exit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				e.returnToContactForm(sharedContact);
			}
		});
		
		sharePanel = new HorizontalPanel();
		sharePanel.add(shareContact);
		sharePanel.add(shareContactWAll);
	}

	public void continueOnLoad() {
		
		createSelectionBox();
		createPValueBox(sharedContact.getValues());

		GWT.log("collab4");
		grid = new Grid(4, 3);
		// grid.setSize("500px", "400px");
		
		grid.setWidget(0, 1, addButton);
		grid.setWidget(1, 0, selUser);
		grid.setWidget(2, 0, removeButton);
		grid.setWidget(0, 2, valueLabel);
		grid.setWidget(1, 2, selValues);
		grid.setWidget(2, 2, sharePanel);

		grid.setWidget(3, 0, exit);
		this.add(grid);
		GWT.log("collab5");
	}
	// selUser.getResources und getRowElement
	
	public void createSelectionBox() {
		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		GWT.log("SelectBox");
		ldp.setList(sharedUser);
		ldp.flush();
	}

	public void setEditor(Editor e) {
		GWT.log("Editor in Collab setzen");
		this.e = e;
	}

	// selUser.getResources und getRowElement
	
	public void setUser(JabicsUser u) {
		this.u = u;
	}

	public void createPValueBox(ArrayList<PValue> pv) {
		valueProvider.setList(pv);
		allValues = pv;
		valueProvider.flush();
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
				selValues.setVisible(true);
			}
		});
	}

	public void setSelectedValues(ArrayList<PValue> pval) {
		this.selectedValues = pval;
		for (PValue pv : selectedValues) {
			multiSelectionModel.setSelected(pv, true);
		}
		valueProvider.flush();
	}

	/**
	 * Teilt den Kontakt mit allen ausgewählten Nutzern mit den neuen ausgewählten
	 * Eigenschaften
	 */
	public void updateContactWithAll() {
		if (!selectedUser.isEmpty()) {
			for (JabicsUser u : selectedUser) {
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

		for (PValue pv : allValues) {
			if (multiSelectionModel.isSelected(pv)) {
				GWT.log("Hinzugefügt wird: " + pv.toString());
				editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
			} else {
				GWT.log("Gelöscht wird: " + pv.toString());
				editorService.deleteCollaboration(pv, u, new AddPVCollaborationCallback());
			}
		}
	}
	
	public void removeContactFromTable() {
		
	}

	/**
	 * Führt den RPC zum Entfernen einer Kontakt-Kollaboration mit den ausgewählten Parametern
	 * durch
	 */
	public void deleteCollabWithUser(HashSet<JabicsUser> u) {
		GWT.log("Löschen für");
		for (JabicsUser user : u) {
			GWT.log(user.getEmail());
		}
		for (JabicsUser user : u) {
			editorService.deleteCollaboration(sharedContact, user, new DeleteContactCollaborationCallback());
		}
		exit.setVisible(false);
		//e.returnToContactForm(sharedContact);
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

	private class DeleteContactCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			Window.alert("Contact: Fail");
		}

		public void onSuccess(Void v) {
			if (v != null) {
				Window.alert("Kontakt erolgreich entteilt!");
				removeContactFromTable();
			}
			exit.setVisible(true);
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

	/*
	 * private class DeletePVCollaborationCallback implements AsyncCallback<Void> {
	 * public void onFailure(Throwable arg0) { Window.alert("PV: Fail"); }
	 * 
	 * public void onSuccess(Void v) { if (v != null) {
	 * Window.alert("PV erfolgreich entteilt!"); } } }
	 */

}
