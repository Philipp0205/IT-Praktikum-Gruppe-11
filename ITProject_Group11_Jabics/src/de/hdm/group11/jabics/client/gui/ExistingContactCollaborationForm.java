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
	private EditorAdmin e;
	private JabicsUser u;
	private EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	private Contact sharedContact;
	private ArrayList<JabicsUser> sharedUser = new ArrayList<JabicsUser>();
	private HashSet<JabicsUser> selectedUser = new HashSet<JabicsUser>();
	private JabicsUser singleSelectedUser = null;

	private Button exit, addButton, removeButton, shareContact;
	private HorizontalPanel sharePanel;

	private Label valueLabel;

	private CellTable<PValue> selValues;
	private ArrayList<PValue> selectedValues;
	private ArrayList<PValue> allValues;
	private MultiSelectionModel<PValue> multiSelectionModel;

	private TextColumn<JabicsUser> username;

	private CellTable<JabicsUser> selUser;
	private ListDataProvider<JabicsUser> userProvider;
	private MultiSelectionModel<JabicsUser> userSelectionModel;

	private HashSet<PValue> finalPV = new HashSet<PValue>();

	private ListDataProvider<PValue> valueProvider;

	private Column<PValue, Boolean> checkbox;
	private Column<PValue, String> property;
	private Column<PValue, String> propertyvalue;

	private Grid grid;

	// Ressourcen für CellTables
	private CellTableResources ctRes = GWT.create(CellTableResources.class);

	public void onLoad() {
		exit.setText("Abbrechen");
		exit.setEnabled(true);
		retrieveSharedUser();
	}

	/**
	 * Eine neue ExistingContactCollaborationForm erstellen.
	 */
	public ExistingContactCollaborationForm() {

		grid = new Grid(4, 3);
		valueLabel = new Label("Eigenschaften, die der ausgewählte Nutzer sehen darf");

		selValues = new CellTable<PValue>(100, ctRes);
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
				} else {
					Window.alert("Kein einzelner Nutzer ausgewählt");
				}
			}

		});

		exit = new Button("Abbrechen");
		exit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				sharedUser = null;
				e.showContact(sharedContact);
			}
		});

		sharePanel = new HorizontalPanel();
		sharePanel.add(shareContact);
	}

	public void continueOnLoad() {

		createSelectionBox();
		createPValueBox(sharedContact.getValues());

		// grid.setSize("500px", "400px");

		grid.setWidget(0, 1, addButton);
		grid.setWidget(1, 0, selUser);
		grid.setWidget(2, 0, removeButton);
		grid.setWidget(0, 2, valueLabel);
		grid.setWidget(1, 2, selValues);
		grid.setWidget(2, 2, sharePanel);

		grid.setWidget(3, 0, exit);
		this.add(grid);
	}

	/**
	 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
	 */
	public void createSelectionBox() {
		userProvider.setList(sharedUser);
		userProvider.flush();
	}

	public void setEditor(EditorAdmin e) {
		this.e = e;
	}

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
				sharedContact = result;
				updateContact(result);
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
		if (u != null) {
			for (PValue pv : allValues) {
				if (multiSelectionModel.isSelected(pv)) {
					editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
				} else {
					editorService.deleteCollaboration(pv, u, new AddPVCollaborationCallback());
				}
			}
		}
	}

	/**
	 * Einen Kontakt, wenn er zurückgegeben wird, im Menu updaten
	 * 
	 * @param result
	 */
	public void updateContact(Contact result) {
		exit.setText("Zurück");
		exit.setEnabled(true);
		e.updateContactInTree(result);
	}

	/**
	 * Einen Nutzer aus der Tabelle, die alle Nutzer, mit denen der Kontakt geteilt
	 * ist, anzeigt, entfernen
	 * 
	 * @param u
	 *            <code>JabicsUser</code> der Nutzer der entfernt werden soll
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
		for (JabicsUser user : u) {
			editorService.deleteCollaboration(sharedContact, user, new DeleteContactCollaborationCallback());
		}
		exit.setEnabled(false);
		// e.returnToContactForm(sharedContact);
	}

	/**
	 * Den aktuell angezeigten Kontakt setzen
	 * 
	 * @param c
	 *            der Kontakt der angezeigt werden soll
	 */
	public void setContact(Contact c) {
		if (c != null) {
			this.sharedContact = c;
		} else {
			this.sharedContact = null;
		}
	}

	private void retrieveSharedUser() {
		editorService.getCollaborators(sharedContact, new GetCollaboratorsCallback());
	}

	private void setSharedUser(ArrayList<JabicsUser> user) {
		this.sharedUser = user;
	}

	public JabicsUser getUser() {
		return this.u;
	}

	private class AddPVCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			Window.alert("PV konnte nicht geteilt werden");
		}

		public void onSuccess(Void v) {
			updateShareStatus();
		}
	}

	private class DeleteContactCollaborationCallback implements AsyncCallback<JabicsUser> {
		public void onFailure(Throwable arg0) {
			Window.alert("Teilen fehlgeschlagen!");
		}

		public void onSuccess(JabicsUser u) {
			removeUserFromTable(u);
			updateShareStatus();
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
