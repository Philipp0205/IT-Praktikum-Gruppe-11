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

	EditorAdmin e;
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
	HorizontalPanel horp1;
	HorizontalPanel horp2;
	HorizontalPanel confirmPanel;

	VerticalPanel addPanel = new VerticalPanel();
	VerticalPanel removePanel = new VerticalPanel();

	TextBox listBox;
	Label headline;

	Button deleteButton = new Button("🗑");
	Button saveButton = new Button("✔");
	Button shareButton = new Button("⋲");
	Button removeButton = new Button("-");
	Button addButton = new Button("+");

	Button searchInListButton = new Button("🔍");

	ArrayList<Contact> cArray;

	ListDataProvider<Contact> contactDataProvider;

	public ContactListForm() {

		// LABELS
		horp1 = new HorizontalPanel();
		horp2 = new HorizontalPanel();
		Label listname = new Label("Name:");
		listname.setStyleName("Listenname");
		listBox = new TextBox();

		Label addlabel = new Label("Kontakt");
		addlabel.addClickHandler(new addClickHandler());
		Label remlabel = new Label("Kontakt");
		remlabel.addClickHandler(new removeClickHandler());
		Label searchlabel = new Label("Suche");
		searchlabel.addClickHandler(new searchClickHandler());
		HorizontalPanel hinzufügen = new HorizontalPanel();
		HorizontalPanel entfernen = new HorizontalPanel();
		HorizontalPanel suche = new HorizontalPanel();

		horp1.add(listname);
		horp1.add(listBox);
		hinzufügen.add(addlabel);
		hinzufügen.add(addButton);
		entfernen.add(remlabel);
		entfernen.add(removeButton);
		suche.add(searchlabel);
		suche.add(searchInListButton);
		horp1.add(hinzufügen);
		horp1.add(entfernen);
		horp1.add(suche);

		addlabel.setStyleName("claddlabel");
		remlabel.setStyleName("clremlabel");
		searchlabel.setStyleName("clsearchlabel");
		removeButton.setStyleName("cdeleteBtn");
		addButton.setStyleName("caddButton");
		listBox.setStyleName("TextBox");
		searchInListButton.setStyleName("searchButton");

		Label deleteLabel = new Label("Liste löschen");
		deleteLabel.addClickHandler(new deleteClickHandler());
		Label shareLabel = new Label("Liste teilen");
		shareLabel.addClickHandler(new shareClickHandler());
		Label saveLabel = new Label("Änderungen speichern");
		saveLabel.addClickHandler(new saveClickHandler());
		HorizontalPanel löschen = new HorizontalPanel();
		HorizontalPanel speichern = new HorizontalPanel();
		HorizontalPanel teilen = new HorizontalPanel();

		löschen.add(deleteLabel);
		löschen.add(deleteButton);
		speichern.add(saveLabel);
		speichern.add(saveButton);
		teilen.add(shareLabel);
		teilen.add(shareButton);
		horp2.add(löschen);
		horp2.add(speichern);
		horp2.add(teilen);
		changePanel.add(horp2);

		deleteLabel.setStyleName("cldeleteLabel");
		deleteButton.setStyleName("cldeleteButton");
		saveLabel.setStyleName("clsaveLabel");
		saveButton.setStyleName("clsaveButton");
		shareLabel.setStyleName("clshareLabel");
		shareButton.setStyleName("clshareButton");

		headline = new Label("Liste: ");

		/*
		 * ---------- Clickhandler für alle Buttons -----------------
		 */
		deleteButton.addClickHandler(new deleteClickHandler());

		/*
		 * Kontakte hinzufügen
		 */
		addButton.addClickHandler(new addClickHandler());

		/**
		 * 3 Reihen Die erste bietet die Optionen auf Listenebene an (Liste teilen1,
		 * Liste teilen 2). Die zweite bietet die Option an, die Liste zu löschen. Die
		 * dritte bietet die Optionen innerhalb der Liste an (Kontakt hinzufügen,
		 * Kontakt entfernen)
		 */

		shareButton.addClickHandler(new shareClickHandler());

		saveButton.addClickHandler(new saveClickHandler());

		/*
		 * Kontakte entfernen
		 */
		removeButton.addClickHandler(new removeClickHandler());

		GWT.log("isNewList " + isNewList);

		selValues = new CellTable<Contact>();
		valueProvider = new ListDataProvider<Contact>();
		valueProvider.addDataDisplay(selValues);

		selectionModel = new MultiSelectionModel<Contact>();

	}

	class deleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			GWT.log("7.2 deleteButton");

			editorService.deleteContactList(currentList, u, new DeleteContactListCallback());

		}
	}

	class saveClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			GWT.log("7.2 saveButton");
			save();
		}
	}

	class shareClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			GWT.log("Teile Liste " + currentList.getListName());

			e.showContactListCollab(currentList);

			// removeAddPanel();
			// removeRemovePanel();
		}
	}

	class addClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			editorService.getContactsOf(u, new GetAllContactsOfUserCallback());

			GWT.log("7.2 AddButton");
			GWT.log("7.2 User: " + u.getId());
			addButton.setEnabled(false);
			removeButton.setEnabled(false);
			cArray = new ArrayList<Contact>();
			if (isNewList) {
				editorService.createContactList(listBox.getText(), cArray, u, new CreateContactListCallback());

			} else {
				
				GWT.log("7.2 updateList " + currentList.getListName());
				// editorService.updateContactList(currentList, new
				// UpdateContactListCallback());
			}

			// editorService.getContactsOf(u, new GetAllContactsOfUserCallback());
		}
	}

	class removeClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			editorService.getContactsOfList(currentList, u, new GetContactsOfListCallback());
			addButton.setEnabled(false);
			removeButton.setEnabled(false);
			GWT.log("7.2 RemoveContactButton");
			GWT.log("7.2 User: " + u.getId());

			// editorService.updateContactList(currentList, new
			// UpdateContactListCallback());

			// editorService.getContactsOf(u, new GetAllContactsOfUserCallback());
		}
	}

	class searchClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			e.showSearchForm(currentList);
		}
	}

	public void onLoad() {
		super.onLoad();
		// For Debugging
		GWT.log("7.1 onLoad");

		headline.setText("Liste: " + currentList.getListName());

		headline.setStyleName("contactListHeadline");

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

		searchInListButton.addClickHandler(new searchClickHandler());

		sharePanel.setStyleName("sharePanel");

		this.add(horp1);
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
		currentList.setListName(listBox.getValue());
		editorService.updateContactList(currentList, new UpdateContactListCallback());

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

		// selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		// public void onSelectionChange(SelectionChangeEvent event) {
		// /**
		// * TODO: überlegen ob nächste Zeile benötigt oder durch clickhandler in button
		// * add abgedeckt!
		// */
		// HashSet<Contact> finalC = (HashSet<Contact>) selectionModel.getSelectedSet();
		// // Window.alert("Auswahl geändert");
		// }
		// });

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
		Button done2 = new Button("Fertig");
		done2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				// addPanel.clear();
				// removePanel.clear();
				// addPanel.remove(0);
				removeAddPanel();
				valueProvider.setList(null);
				valueProvider.flush();
			}
		});

		addPanel.add(selValues);
		addPanel.add(add);
		addPanel.add(done2);
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
				// addPanel.clear();
				// removePanel.clear();
				// addPanel.remove(0);
				removeAddPanel();
				valueProvider.setList(null);
				valueProvider.flush();
			}
		});

		removePanel.add(selValues);
		removePanel.add(remove);
		removePanel.add(done);
		valueProvider.flush();
	}

	public void setEditor(EditorAdmin e) {
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
			GWT.log("7.3 DeleteContactListCallback Success");
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
