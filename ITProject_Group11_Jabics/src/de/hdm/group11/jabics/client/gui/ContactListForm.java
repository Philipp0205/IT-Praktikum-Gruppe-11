package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class ContactListForm extends VerticalPanel {
	/**
	 * Struktur von
	 * 
	 * @author Christian Rathke
	 * 
	 *         Angepasst von
	 * @author Anders
	 */
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Editor e;
	JabicsUser u = null;
	ContactList currentList;
	ContactList currentList2;
	Boolean isNewList;

	MultiSelectionModel<Contact> selectionModel;

	CellTable<Contact> selValues;
	ListDataProvider<Contact> valueProvider;

	HorizontalPanel sharePanel = new HorizontalPanel();
	HorizontalPanel editPanel = new HorizontalPanel();
	HorizontalPanel changePanel = new HorizontalPanel();
	HorizontalPanel searchPanel = new HorizontalPanel();

	VerticalPanel addPanel = new VerticalPanel();
	VerticalPanel removePanel = new VerticalPanel();

	TextBox listBox = new TextBox();
	Label formName;
	Label headline;

	Button deleteButton = new Button("Liste löschen");
	Button saveButton = new Button("Änderungen speichern");
	Button shareButton = new Button("Liste teilen");
	Button shareExistingButton = new Button("Teilen bearbeiten");
	Button removeButton = new Button("Kontakte entfernen");
	Button addButton = new Button("Kontakte hinzufügen");

	Button searchInListButton = new Button("Liste durchsuchen");

	ArrayList<Contact> cArray;

	ContactCellListTab cTab = new ContactCellListTab();
	ListDataProvider<Contact> contactDataProvider;

	public ContactListForm() {

		GWT.log("isNewList " + isNewList);

		contactDataProvider = cTab.getContactDataProvider();
		selValues = new CellTable<Contact>();
		valueProvider = new ListDataProvider<Contact>();
		valueProvider.addDataDisplay(selValues);

		selectionModel = new MultiSelectionModel<Contact>();

	}

	public void onLoad() {
		super.onLoad();
		// For Debugging
		GWT.log("7.1 onLoad");

		formName = new Label("Listen-Editor. Kontakte in der Liste sind links im Menu zu sehen");
		headline = new Label("Liste: " + currentList.getListName());

		headline.setStyleName("contactListHeadline");

		this.add(formName);
		this.add(headline);

		// GWT.log("isNewList: " + isNewList);

		if (isNewList == true) {
			GWT.log("7.1 isNewList true");
			deleteButton.setVisible(false);
			removeButton.setVisible(false);

		} else {
			deleteButton.setVisible(true);
			removeButton.setVisible(true);
		}

		/**
		 * 3 Reihen Die erste bietet die Optionen auf Listenebene an (Liste teilen1,
		 * Liste teilen 2). Die zweite bietet die Option an, die Liste zu löschen. Die
		 * dritte bietet die Optionen innerhalb der Liste an (Kontakt hinzufügen,
		 * Kontakt entfernen)
		 */

		shareButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("Teile Liste " + currentList.getListName());
				
				e.showContactListCollab(currentList);
				
//				removeAddPanel();
//				removeRemovePanel();
			}
		});
		shareExistingButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.alert("aktuell noch falsche share form!");
				e.showContactListCollab(currentList);
			}
		});

		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("7.2 deleteButton");

				editorService.deleteContactList(currentList, u, new DeleteContactListCallback());

			}
		});
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("7.2 saveButton");
				save();
			}
		});
		
		/*
		 * Kontakte hinzufügen
		 */
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editorService.getContactsOf(u, new GetAllContactsOfUserCallback());

				GWT.log("7.2 AddButton");
				GWT.log("7.2 User: " + u.getId());

				cArray = new ArrayList<Contact>();
				if (isNewList) {
					editorService.createContactList(listBox.getText(), cArray, u, new CreateContactListCallback());
					
				} else {
					GWT.log("7.2 updateList " + currentList.getListName());
					//editorService.updateContactList(currentList, new UpdateContactListCallback());
				}

				// editorService.getContactsOf(u, new GetAllContactsOfUserCallback());
			}
		});

		/*
		 * Kontakte entfernen
		 */
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editorService.getContactsOfList(currentList, u, new GetContactsOfListCallback());

				GWT.log("7.2 RemoveContactButton");
				GWT.log("7.2 User: " + u.getId());

				// editorService.updateContactList(currentList, new
				// UpdateContactListCallback());

				// editorService.getContactsOf(u, new GetAllContactsOfUserCallback());
			}
		});

		sharePanel.add(shareButton);
		sharePanel.add(shareExistingButton);
		editPanel.add(addButton);
		editPanel.add(removeButton);
		changePanel.add(deleteButton);
		changePanel.add(saveButton);
		searchPanel.add(searchInListButton);
		searchInListButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				e.showSearchForm(currentList);
			}
		});

		sharePanel.setStyleName("sharePanel");

		this.add(listBox);
		this.add(sharePanel);
		this.add(editPanel);
		this.add(changePanel);
		this.add(addPanel);
		this.add(removePanel);
		this.add(searchPanel);
	}

	public void setCurrentList(ContactList cl) {
		this.currentList = cl;

		if (cl != null) {
			GWT.log("7.3.1 CurrentList: " + cl.getListName());
			this.currentList = cl;
			GWT.log(currentList.getContacts().toString());

			if (cl.getListName() != null) {
				listBox.setText(cl.getListName());
			}

			deleteButton.setEnabled(true);
			shareButton.setEnabled(true);
		} else {
			this.currentList = null;
			GWT.log("7.3.2 CurrentList: " + currentList.getListName() + " " + currentList.getClass().toString());
			deleteButton.setEnabled(false);
			shareButton.setEnabled(false);
		}
	}

	/**
	 * Entfernt das Panel, das die Möglichkeit gibt, Kontakte hizuzufügen
	 */
	void removeAddPanel() {
		GWT.log("7.4 removeAddPanel");
		this.remove(addPanel);
	}

	/**
	 * Entfernt das Panel, das die Möglichkeit gibt, Kontakte zu entfernen;
	 */
	void removeRemovePanel() {
		GWT.log("7.4 removeRemovePanel");
		this.remove(removePanel);
	}

	void save() {
		GWT.log("7.4 saveMethod");

		// Überprüfen ob Name gesetzt
		boolean nameExistent = false;
		// for ()

		/*
		 * TODO: implement
		 */
		// Window.alert("Not yet implemented");

	}

	/**
	 * Diese Methode fügt ein Auswahlfenster für alle Kontakte, die ein Nutzer sehen
	 * kann, unter der ContactForm (bzw darin, aber unter der Anzeige der
	 * allgemeinen Informationen) ein. Es können Kontakte ausgewählt werden und
	 * durch Klick auf einen Button der Liste hinzugefügt werden.
	 * 
	 * @param ArrayList<Contact>
	 *            alle Kontakte eines Nutzers
	 */
	public void addContactPanel(ArrayList<Contact> allC) {

		valueProvider.setList(allC);

		selectionModel = new MultiSelectionModel<Contact>();

//		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			public void onSelectionChange(SelectionChangeEvent event) {
//				/**
//				 * TODO: überlegen ob nächste Zeile benötigt oder durch clickhandler in button
//				 * add abgedeckt!
//				 */
//				HashSet<Contact> finalC = (HashSet<Contact>) selectionModel.getSelectedSet();
//				// Window.alert("Auswahl geändert");
//			}
//		});

		selValues.setSelectionModel(selectionModel);

		Column<Contact, Boolean> checkbox = new Column<Contact, Boolean>(new CheckboxCell(true, false)) {
			public Boolean getValue(Contact object) {
				GWT.log("7.3 selected getPValue");
				return selectionModel.isSelected(object);
			}
		};
		Column<Contact, String> contact = new Column<Contact, String>(new TextCell()) {
			public String getValue(Contact object) {
				// GWT.log("7.3 selected getPValue 2");
				return object.toString();
			}
		};

		selValues.addColumn(checkbox, "Auswahl");
		selValues.setColumnWidth(checkbox, 50, Unit.PX);
		selValues.addColumn(contact, "Kontakt");
		selValues.setColumnWidth(contact, 50, Unit.EM);

		Button add = new Button("Ausgewählte Kontakte hinzufügen");
		add.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {

				GWT.log("7.4 AddContactsButton ");
				GWT.log("7.4 currentList: " + currentList.getListName());

				for (Contact c : selectionModel.getSelectedSet()) {

					GWT.log("7.4 Add Contact " + c.getName() + "to List " + currentList.getId() + " "
							+ currentList.getListName() + " " + currentList.getContacts().toString());

					/*
					 * TODO hier gibt es zwei möglichkeiten der Implementierung: nummer 2 ist
					 * auskommatiert, noch entscheiden welhes besser ist!
					 */
					editorService.addContactToList(c, currentList, new AddContactToListCallback());

					/*
					 * currentList.addContact(c); editorService.updateContact(c,
					 * UpdateContactCallback);
					 */
				}
			}
		});
		Button done = new Button("Fertig");
		done.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				removeAddPanel();
			}
		});

		addPanel.add(selValues);
		addPanel.add(add);
		addPanel.add(done);
		valueProvider.flush();
	}

	/**
	 * Diese Methode ist praktisch identisch zu addContacts(). Sie fügt ein
	 * Auswahlfenster für alle Kontakte, die ein Nutzer sehen kann, unter der
	 * ContactForm (bzw darin, aber unter der Anzeige der allgemeinen Informationen)
	 * ein. Es können Kontakte ausgewählt werden und durch Klick auf einen Button
	 * aus der Liste entfernt werden.
	 * 
	 * @param ArrayList<Contact>
	 *            alle Kontakte eines Nutzers
	 */
	public void removeContactPanel(ArrayList<Contact> allC) {
		GWT.log("7.7 removeContactPanel");
		// PValue selectedPV;
		// selValues = new CellTable<Contact>();
		valueProvider.setList(allC);
		// valueProvider.addDataDisplay(selValues);
		// finalC;
		// Es kann sein, dass hier noch kexprovider benötigt werden

		selectionModel = new MultiSelectionModel<Contact>();

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				/**
				 * TODO: überlegen ob nächste Zeile benötigt oder durch clickhandler in button
				 * add abgedeckt!
				 */
				HashSet<Contact> finalC = (HashSet<Contact>) selectionModel.getSelectedSet();
				Window.alert("Auswahl geändert");
			}
		});

		selValues.setSelectionModel(selectionModel);

		Column<Contact, Boolean> checkbox = new Column<Contact, Boolean>(new CheckboxCell(true, false)) {
			public Boolean getValue(Contact object) {
				GWT.log("7.3 selected getPValue");
				return selectionModel.isSelected(object);
			}
		};
		Column<Contact, String> contact = new Column<Contact, String>(new TextCell()) {
			public String getValue(Contact object) {
				GWT.log("7.3 selected getPValue");

				return object.toString();
			}
		};

		selValues.addColumn(checkbox, "Auswahl");
		selValues.setColumnWidth(checkbox, 50, Unit.PX);
		selValues.addColumn(contact, "Kontakt");
		selValues.setColumnWidth(contact, 50, Unit.EM);

		Button remove = new Button("Ausgewählte Kontakte aus Liste entfernen");
		remove.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {

				/*
				 * TODO hier gibt es zwei möglichkeiten der Implementierung nummer 2 ist
				 * auskommatiert, noch entscheiden welhes besser ist!
				 */

				GWT.log("7.4 RemoveContactsButton ");
				GWT.log("7.4 currentList: " + currentList.getListName());

				for (Contact c : selectionModel.getSelectedSet()) {

					GWT.log("7.4 Remove Contact " + c.getName() + "from List " + currentList.getId() + " "
							+ currentList.getListName() + " " + currentList.getContacts().toString());

					editorService.removeContactFromList(c, currentList, new RemoveContactFromListCallback());
					// editorService.removeContactFromList(c, currentList, new
					// RemoveContactFromListCallback());
					/*
					 * currentList.removeContact(c); editorService.updateContact(c,
					 * UpdateContactCallback);
					 */
				}
			}
		});
		Button done = new Button("Fertig");
		done.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				removeRemovePanel();
			}
		});

		removePanel.add(selValues);
		removePanel.add(remove);
		removePanel.add(done);
		valueProvider.flush();
	}

	public void setEditor(Editor e) {
		this.e = e;
	}

	public void setUser(JabicsUser u) {
		this.u = u;
	}

	/**
	 * Clickhandler und Asynchrone Methodenaufrufe für das Löschen eines
	 * <code>ContactList</code> Objekts.
	 * 
	 * @author Brase
	 * @author Ilg
	 */
	private class DeleteListClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (currentList == null) {
				Window.alert("Keine Liste ausgewählt");
			} else {
				editorService.deleteContactList(currentList, u, new DeleteContactListCallback());
			}
		}
	}

	private class UpdateContactListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Liste konnte nicht geupdated werden.");
		}

		public void onSuccess(ContactList cl) {

			if (cl != null) {
				GWT.log("7.4 UpdateContactListCallback on Success");

				/**
				 * TODO: die geupdatete ContactList in den TreeView wieder einfügen bzw
				 * anzeigen?
				 */
				e.updateContactListInTree(cl);

				setCurrentList(cl);
				onLoad();
			}

		}
	}

	private class GetContactsOfListCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler1! Kontakte konnten nicht geladen werden.");
		}

		public void onSuccess(ArrayList<Contact> al) {

			// GWT.log("7.3 GetContactsOfListCallback onSuccess" );

			if (al != null) {
				// currentList.addContacts(al);
				removeContactPanel(al);

			}

		}
	}

	private class GetAllContactsOfUserCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler2! Kontakte konnten nicht geladen werden.");
		}

		public void onSuccess(ArrayList<Contact> al) {
			if (al != null) {
				addContactPanel(al);
			}

		}
	}

	private class AddContactToListCallback implements AsyncCallback<Contact> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler 3! Kontakt konnte nicht hinzugefügt werden");
		}

		public void onSuccess(Contact contact) {

			if (contact != null) {

				// GWT.log(list.getContacts().toString());
				// setCurrentList(list);

				// Window.alert("Kontakt" + contact.getName() + " hinzugefügt");
				/**
				 * TODO: diese liste auch in dem TreeViewModel updaten!
				 */
				GWT.log("7.5  " + "add " + contact.getName() + " to " + currentList.getListName() + " to Tree"
						+ currentList.getContacts().toString());

				currentList.addContact(contact);
				e.addContactToListInTree(currentList, contact);
				// e.updateContactListInTree(list);

				// onLoad();

			}

		}
	}

	private class RemoveContactFromListCallback implements AsyncCallback<Contact> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fehler 5! Kontakt konnte nicht hinzugefügt werden");

		}

		@Override
		public void onSuccess(Contact contact) {
			if (contact != null) {

				Window.alert("Kontakt" + contact.getName() + " aus Liste gelöscht.");
				GWT.log("7.5  " + "remove " + contact.getName() + " from " + currentList.getListName() + "in Tree"
						+ currentList.getContacts().toString());

				currentList.removeContact(contact);
				e.removeContactFromContactListInTree(currentList, contact);

			}

		}

	}

	private class CreateContactListCallback implements AsyncCallback<ContactList> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(ContactList cl) {
			if (cl != null) {
				GWT.log("7.3 createContactListCallback onSuccess ContactListID " + cl.getId());

				setCurrentList(cl);
				e.addContactListToTree(cl);
				isNewList = false;
				deleteButton.setVisible(true);
			}

		}

	}

	private class DeleteContactListCallback implements AsyncCallback<ContactList> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fehler 5 Kontakt konnte nicht gelöscht werden.");

		}
		@Override
		public void onSuccess(ContactList cl) {
			e.removeContactListFromTree(cl);
		}
	}

	public void setIsNewList(boolean b) {
		this.isNewList = b;

	}

	public void setContactList(ContactList cl) {
		this.currentList = cl;

	}
}
