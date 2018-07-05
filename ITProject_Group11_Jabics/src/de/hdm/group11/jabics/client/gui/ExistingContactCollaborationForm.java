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
import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.CellTableResources;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;

public class ExistingContactCollaborationForm extends HorizontalPanel {
	EditorAdmin e;
	JabicsUser u;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Contact sharedContact;
	ArrayList<JabicsUser> sharedUser = new ArrayList<JabicsUser>();
	HashSet<JabicsUser> selectedUser = new HashSet<JabicsUser>();
	JabicsUser singleSelectedUser = null;

	Button exit, addButton, removeButton, shareContact, shareContactWAll;
	HorizontalPanel sharePanel;

	Label valueLabel;

	CellTable<PValue> selValues;
	ArrayList<PValue> selectedValues;
	ArrayList<PValue> allValues;
	MultiSelectionModel<PValue> multiSelectionModel;

	TextColumn<JabicsUser> username;

	CellTable<JabicsUser> selUser;
	ListDataProvider<JabicsUser> userProvider;
	MultiSelectionModel<JabicsUser> userSelectionModel;

	HashSet<PValue> finalPV = new HashSet<PValue>();

	ListDataProvider<PValue> valueProvider;

	Column<PValue, Boolean> checkbox;
	Column<PValue, String> property;
	Column<PValue, String> propertyvalue;

	ContactForm cf;

	Grid grid;
	
	//Ressourcen für CellTables
	private CellTableResources ctRes = GWT.create(CellTableResources.class);

	public void onLoad() {
		retrieveSharedUser();
	}

	/**
	 * Eine neue ExistingContactCollaborationForm erstellen.
	 */
	public ExistingContactCollaborationForm() {

		grid = new Grid(4, 3);

		valueLabel = new Label("Eigenschaften, die der ausgewählte Nutzer sehen darf");

		selValues = new CellTable<PValue>(100,ctRes);
		valueProvider = new ListDataProvider<PValue>();

		userProvider = new ListDataProvider<JabicsUser>();
		selUser = new CellTable<JabicsUser>();

		userProvider.addDataDisplay(selUser);

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

		property.setCellStyleNames("prop");
		propertyvalue.setCellStyleNames("pval");

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
						shareContact.setEnabled(true);
					}
				} else {
					selValues.setVisible(false);
					shareContact.setEnabled(false);
				}
			}
		});
		selUser.setSelectionModel(userSelectionModel);

		removeButton = new Button("Freigabe entfernen");
		removeButton.setStyleName("removeexisting");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (selectedUser != null) {
					deleteCollabWithUser(selectedUser);

					userProvider.flush();
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
				if (singleSelectedUser != null) {
					updateContactShareForUser(singleSelectedUser);
				}else {
					Window.alert("Kein einzelner Nutzer ausgewählt");
				}
			}

		});
		shareContactWAll = new Button("Für alle Nutzer ändern");
		shareContactWAll.setStyleName("edit4all");
		shareContactWAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				updateContactWithAll();
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

	/**
	 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
	 */
	public void createSelectionBox() {

		GWT.log("SelectBox");
		userProvider.setList(sharedUser);
		userProvider.flush();
	}

	public void setEditor(EditorAdmin e) {
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
		for (PValue pval : allValues) {
			multiSelectionModel.setSelected(pval, false);
		}
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
		for (PValue pv : allValues) {
			boolean bol = false;
			for (PValue val : pval) {
				if (val.getId() == pv.getId())
					bol = true;
			}
			if (bol) {
				multiSelectionModel.setSelected(pv, true);
			} else
				multiSelectionModel.setSelected(pv, false);
		}
		valueProvider.flush();
	}

	public void updateShareStatus() {
		editorService.getUpdatedContact(sharedContact, new AsyncCallback<Contact>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to update Contact" + caught.toString());
			}

			public void onSuccess(Contact result) {
				e.updateContactInTree(result);
			}
		});
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
		if (u != null) {
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
	}

	/**
	 * Einen Nutzer aus der Tabelle, die alle Nutzer, mit denen der Kontakt geteilt
	 * ist, anzeigt, entfernen
	 * 
	 * @param JabicsUser der Nutzer der entfernt werden soll
	 */
	public void removeUserFromTable(JabicsUser u) {
		for (JabicsUser uu : sharedUser) {
			if (uu.getId() == u.getId())
				sharedUser.remove(uu);
			userProvider.flush();
		}

	}

	/**
	 * Führt den RPC zum Entfernen einer Kontakt-Kollaboration mit den ausgewählten
	 * Parametern durch
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
		// e.returnToContactForm(sharedContact);
	}

	/**
	 * Den aktuell angezeigten Kontakt setzen
	 * 
	 * @param c der Kontakt der angezeigt werden soll
	 */
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
		}
	}

	private class DeleteContactCollaborationCallback implements AsyncCallback<JabicsUser> {
		public void onFailure(Throwable arg0) {
			Window.alert("Teilen fehlgeschlagen!");
		}

		public void onSuccess(JabicsUser u) {
			Window.alert("Kontakt erfolgreich entteilt!");
			removeUserFromTable(u);
			updateShareStatus();
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

}
