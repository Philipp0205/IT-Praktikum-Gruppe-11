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
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ListDataProvider;

public class ContactCollaborationForm extends HorizontalPanel {

	EditorAdmin e;
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Contact sharedContact;

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

	private CellTableResources ctRes = GWT.create(CellTableResources.class);

	/**
	 * Die Contact Form das erste Mal erstellen, Tabellen hinzufügen und alles
	 * verknüpfen,
	 */
	/**
	 * Eine <code>ContactCollaborationForm</code> Instantz wird erstellt. Es werden Provider Ceckboxen ein CellTable
	 * initalisiert, welche später gebraucht werden.
	 * Des weiteren werden enthält der Konstrukro alle benötigten Buttons der <code>ContactCollaborationForm</code> 
	 */
	public ContactCollaborationForm() {

		// Alles, was mit der PVal Tabelle zu tun hat
		valueTable = new CellTable<PValue>(100, ctRes);

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

		property.setCellStyleNames("prop");
		propertyvalue.setCellStyleNames("pval");

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

		// ####################### Alles, was mit der Auwahl der Nutzer zu tun hat

		userTable = new CellTable<JabicsUser>(100, ctRes);

		userDataProvider = new ListDataProvider<JabicsUser>();
		userDataProvider.addDataDisplay(userTable);

		oracle = new MultiWordSuggestOracle();
		suggestBox = new SuggestBox(oracle);
		suggestBox.setStyleName("pvBox");

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
						e.returnToContactForm(sharedContact);

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
	
	/**
	 *  Nach die <code>GetAllNotCollaboratingUserCallback</code> und dem <code>GetAllUsersCallback</code> abgeschlossen werden
	 *  hier weitere Elemtente der ContactCollaborationForm geladen. Dem GWT <code>Grid</code> werden oben initalisierte Objekte
	 *  hinzugefügt.
	 */
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
	
	/**
	 * Befüllt die PValueBox, welche alle Eigenschaftsausprägungen eines Kontakes anzeigt. 
	 * 
	 * @param pv, eine ArrayList von PValues welche angezeigt werden soll.
	 */
	public void fillPValueBox(ArrayList<PValue> pv) {
		valueProvider.setList(pv);
		valueProvider.flush();
	}
	
	/**
	 *  Befüllt die GWT <code>SuggestBox</code> mit <code>JabicsUser</code> Objekten. Anschließend werden beim Eingeben von Buchstaben die
	 *  passenden <code>JabicsUser</code> vorgeschlagen
	 */
	public void fillSuggestBox() {
		GWT.log("SuggestBox");
		// SuggestBox mit Optionen befüllen
		for (JabicsUser u : allUser) {
			try {
				oracle.add(u.getUsername() + " " + u.getEmail());
				GWT.log("Nutzer zu Sug hinzugefügt");
			} catch (NullPointerException e) {
				GWT.log("Setzen des nutzernamens oder mailadresse in sugstbox failed, Nutzer mit Id: " + u.getId());
				try {
					oracle.add(u.getUsername());
				} catch (NullPointerException ex) {
					GWT.log("Setzen des nutzernamens auch fehlgeschlagen, Nutzer mit Id: " + u.getId());
				}
			}
		}
		suggestBox.setLimit(5);
	}

	/**
	 * Die onLoadd Methode, die alle Nutzer für die SuggestBox lädt und erst im
	 * Callback dieses RPC weiterläuft
	 */
	public void onLoad() {
		GWT.log("ONLOAD");
		allUser = new ArrayList<JabicsUser>();
		retrieveUser();

	}
	
	/**
	 *  
	 */
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

	public void setContact(Contact c) {
		if (c != null) {
			this.sharedContact = c;
		} else {
			Window.alert("Freigabe nicht möglich, kein Kontakt ausgewählt");
		}
	}

	public void setEditor(EditorAdmin e) {
		GWT.log("Editor in Collab setzen");
		this.e = e;
	}

	/**
	 * Führt den RPC zur Freigabe einens Kontakts mit allen ausgewählten Nutzern mit
	 * den ausgewählten PValues durch.
	 */
	public void shareContactWithAll() {
		if (!finalUser.isEmpty()) {
			if (!selectedPV.isEmpty()) {
				for (JabicsUser u : finalUser) {
					for (PValue pv : selectedPV) {
						editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
					}
					editorService.addCollaboration(sharedContact, u, new AddContactCollaborationCallback());
				}
			} else {
				Window.alert("Bitte mindestens eine Eigenschaft auswählen");
			}
		} else {
			Window.alert("Keine Nutzer ausgewählt");
		}

	}

	/**
	 * Führt den RPC zur Freigabe eines Kontakts mit den ausgewählten Parametern
	 * durch.
	 */
	public void shareContactWithUser(JabicsUser u) {
		GWT.log(selectedUser.getEmail());

		if (!selectedPV.isEmpty()) {
			for (PValue pv : selectedPV) {
				editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
			}

			editorService.addCollaboration(sharedContact, u, new AddContactCollaborationCallback());

		} else {
			Window.alert("Bitte mindestens eine Eigenschaft auswählen");
		}
	}

	private class AddContactCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			Window.alert("Kontakt konnte nicht geteilt werden");
		}

		public void onSuccess(Void v) {
			Window.alert("Kontakt erfolgreich geteilt!");

			updateShareStatus();
		}
	}

	private class AddPVCollaborationCallback implements AsyncCallback<Void> {
		public void onFailure(Throwable arg0) {
			GWT.log("PV konnte nicht geteilt werden");
		}

		public void onSuccess(Void v) {
			GWT.log("PV erfolgreich geteilt!");
		}
	}

	public interface CellTableResources extends CellTable.Resources {

		@Override
		@Source("JabicsCellTable.css")
		CellTable.Style cellTableStyle();
	}

	public class GetAllNotCollaboratingUserCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				GWT.log("alleNutzergesetzt   ");
				setAllUser(user);
				continueOnLoad();
			} else {
				GWT.log("Keine anderen Nutzer gefunden");
			}

		}
	}

	public class GetAllUsersCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			if (user != null) {
				GWT.log("alleNutzergesetzt   ");
				setAllUser(user);
				continueOnLoad();
			}
		}
	}

	public void updateShareStatus() {
		editorService.getUpdatedContact(sharedContact, new AsyncCallback<Contact>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to update Contact" + caught.toString());
			}

			public void onSuccess(Contact result) {
				GWT.log("Update ShareStatus: On Sucess");
				e.updateContactInTree(result);
				e.showContact(result);
			}
		});
	}

}
