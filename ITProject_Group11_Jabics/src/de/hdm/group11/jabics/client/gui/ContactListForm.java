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
	HorizontalPanel horp1 = new HorizontalPanel();
	HorizontalPanel horp2 = new HorizontalPanel();
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

	Button doneRemove = new Button("Fertig");
	Button doneAdd = new Button("Fertig");
	Button remove = new Button("Ausgewählte Kontakte aus Liste entfernen");
	Button add = new Button("Ausgewählte Kontakte hinzufügen");

	Button searchInListButton = new Button("🔍");

	ArrayList<Contact> cArray;

	ListDataProvider<Contact> contactDataProvider;

	/**
	 * Eine neue ContactListForm erstellen
	 * 
	 * (Dies ist der vermutlich längste Konstruktor der Welt)
	 */
	public ContactListForm() {

		// LABELS
		Label listname = new Label("Name:");
		listname.setStyleName("Listenname");
		listBox = new TextBox();

		Label addlabel = new Label("Kontakt");
		addlabel.addClickHandler(new AddClickHandler());
		Label remlabel = new Label("Kontakt");
		remlabel.addClickHandler(new RemoveClickHandler());
		Label searchlabel = new Label("Suche");
		searchlabel.addClickHandler(new SearchClickHandler());
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
		deleteLabel.addClickHandler(new DeleteClickHandler());
		Label shareLabel = new Label("Liste teilen");
		shareLabel.addClickHandler(new ShareClickHandler());
		Label saveLabel = new Label("Änderungen speichern");
		saveLabel.addClickHandler(new SaveClickHandler());
		HorizontalPanel deletePanel = new HorizontalPanel();
		HorizontalPanel savePanel = new HorizontalPanel();
		HorizontalPanel sharePanel = new HorizontalPanel();

		/*
		 * ---------- Clickhandler für alle Buttons -----------------
		 */

		remove.addClickHandler(new DeleteContactsFromListClickHandler());
		doneRemove.addClickHandler(new DoneRemovingClickHandler());
		add.addClickHandler(new AddContactsToContactListClickHandler());
		doneAdd.addClickHandler(new DoneAddingClickHandler());

		saveButton.addClickHandler(new SaveClickHandler());
		deleteButton.addClickHandler(new DeleteClickHandler());

		addButton.addClickHandler(new AddClickHandler());
		removeButton.addClickHandler(new RemoveClickHandler());

		shareButton.addClickHandler(new ShareClickHandler());

		deletePanel.add(deleteLabel);
		deletePanel.add(deleteButton);
		savePanel.add(saveLabel);
		savePanel.add(saveButton);
		sharePanel.add(shareLabel);
		sharePanel.add(shareButton);
		horp2.add(deletePanel);
		horp2.add(savePanel);
		horp2.add(sharePanel);
		changePanel.add(horp2);

		deleteLabel.setStyleName("cldeleteLabel");
		deleteButton.setStyleName("cldeleteButton");
		saveLabel.setStyleName("clsaveLabel");
		saveButton.setStyleName("clsaveButton");
		shareLabel.setStyleName("clshareLabel");
		shareButton.setStyleName("clshareButton");

		headline = new Label("Liste: ");

		// Komplexere Datenstrukturen und Objekte

		cArray = new ArrayList<Contact>();

		selValues = new CellTable<Contact>();
		valueProvider = new ListDataProvider<Contact>();
		valueProvider.addDataDisplay(selValues);
		selectionModel = new MultiSelectionModel<Contact>();
		selValues.setSelectionModel(selectionModel);

		/*
		 * RemovePanel
		 */
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

		/**
		 * 3 Reihen Die erste bietet die Optionen auf Listenebene an (Liste teilen1,
		 * Liste teilen 2). Die zweite bietet die Option an, die Liste zu löschen. Die
		 * dritte bietet die Optionen innerhalb der Liste an (Kontakt hinzufügen,
		 * Kontakt entfernen)
		 */

		removePanel.add(selValues);
		removePanel.add(remove);
		removePanel.add(doneRemove);

		addPanel.add(selValues);
		addPanel.add(add);
		addPanel.add(doneAdd);

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

		searchInListButton.addClickHandler(new SearchClickHandler());

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
		addButton.setEnabled(true);
		removeButton.setEnabled(true);
		addPanel.clear();
		this.remove(addPanel);
	}

	/**
	 * Entfernt das Panel, das die Möglichkeit gibt, Kontakte zu entfernen;
	 */
	void removeRemovePanel() {
		GWT.log("7.4 removeRemovePanel");
		addButton.setEnabled(true);
		removeButton.setEnabled(true);
		removePanel.clear();
		this.remove(removePanel);
	}

	void save() {
		currentList.setListName(listBox.getValue());
		editorService.updateContactList(currentList, new UpdateContactListCallback());

		valueProvider.flush();

	}

	/**
	 * Diese Methode fügt ein Auswahlfenster für alle Kontakte, die ein Nutzer sehen
	 * kann, unter der ContactForm (bzw darin, aber unter der Anzeige der
	 * allgemeinen Informationen) ein. Es können Kontakte ausgewählt werden und
	 * durch Klick auf einen Button der Liste hinzugefügt werden.
	 * 
	 * @param ArrayList<Contact> alle Kontakte eines Nutzers
	 */
	public void addContactPanel(ArrayList<Contact> allC) {

		valueProvider.setList(allC);
		valueProvider.flush();

	}

	/**
	 * Diese Methode ist praktisch identisch zu addContacts(). Sie fügt ein
	 * Auswahlfenster für alle Kontakte, die ein Nutzer sehen kann, unter der
	 * ContactForm (bzw darin, aber unter der Anzeige der allgemeinen Informationen)
	 * ein. Es können Kontakte ausgewählt werden und durch Klick auf einen Button
	 * aus der Liste entfernt werden.
	 * 
	 * @param ArrayList<Contact> alle Kontakte eines Nutzers
	 */
	public void removeContactPanel(ArrayList<Contact> allC) {

		GWT.log("7.7 removeContactPanel");
		// PValue selectedPV;
		// selValues = new CellTable<Contact>();
		valueProvider.setList(allC);
		valueProvider.flush();
		// valueProvider.addDataDisplay(selValues);
		// finalC;
		// Es kann sein, dass hier noch kexprovider benötigt werden

	}

	public void setIsNewList(boolean b) {
		this.isNewList = b;
	}

	public void setContactList(ContactList cl) {
		this.currentList = cl;
	}

	public void setEditor(EditorAdmin e) {
		this.e = e;
	}

	public void setUser(JabicsUser u) {
		this.u = u;
	}

	/* ------------------CLICK HANDLER ------------------- */

	private class DeleteContactsFromListClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			removeRemovePanel();
			GWT.log("7.4 currentList: " + currentList.getListName());

			for (Contact c : selectionModel.getSelectedSet()) {
				GWT.log("7.4 Remove Contact " + c.getName() + " from List " + currentList.getId() + " "
						+ currentList.getListName());
				editorService.removeContactFromList(c, currentList, new RemoveContactFromListCallback());
			}
		}
	}

	private class AddContactsToContactListClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			removeAddPanel();

			for (Contact c : selectionModel.getSelectedSet()) {
				GWT.log("7.4 Add Contact " + c.getName() + "to List " + currentList.getId() + " "
						+ currentList.getListName() + " " + currentList.getContacts().toString());
				editorService.addContactToList(c, currentList, new AddContactToListCallback());
				editPanel.setVisible(false);
			}
		}
	}

	private class DoneAddingClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			// addPanel.clear();
			// removePanel.clear();
			// addPanel.remove(0);
			removeAddPanel();
			valueProvider.setList(null);
			valueProvider.flush();
		}
	}

	private class DoneRemovingClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			// addPanel.clear();
			// removePanel.clear();
			// addPanel.remove(0);
			removeRemovePanel();
			valueProvider.setList(null);
			valueProvider.flush();
		}
	}

	class DeleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			editorService.deleteContactList(currentList, u, new DeleteContactListCallback());
		}
	}

	class SaveClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			save();
		}
	}

	class ShareClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			GWT.log("Teile Liste " + currentList.getListName());

			e.showContactListCollab(currentList);
			// Die Liste editieren Panels ausblenden, da sie nicht mehr benötigt werden
			removeAddPanel();
			removeRemovePanel();
		}
	}

	/**
	 * ClickHandler, um Kontakte zur Liste hinzufügen zu können. Kontakte zur Liste
	 * hinzufügen. Im Fall einer neuen Liste wird die Liste zusätzlich serverseitig
	 * in die DB eingetragen und kommt mit einer ID zurück, damit auf ihr
	 * Operationen durchgeführt werden können.
	 *
	 */
	class AddClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			editorService.getContactsOf(u, new GetAllContactsOfUserCallback());

			GWT.log("7.2 AddButton");
			GWT.log("7.2 User: " + u.getId());
			addButton.setEnabled(false);
			removeButton.setEnabled(false);
			// Im Falle einer neuen Liste diese erstellen, damit Kontakte hinzugefügt werden
			// können
			if (isNewList) {
				editorService.createContactList(listBox.getText(), cArray, u, new CreateContactListCallback());
			}
		}
	}

	/**
	 * ClickHandler, um Kontakte aus der Liste zu entfernen.
	 */
	class RemoveClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			editorService.getContactsOfList(currentList, u, new GetContactsOfListCallback());
			addButton.setEnabled(false);
			removeButton.setEnabled(false);
		}
	}

	/**
	 * Weiterleittung zur SearchForm, in der man Kontaktlisten durchsuchen kann
	 */
	class SearchClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			e.showSearchForm(currentList);
		}
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

			if (al != null) {
				// Die Liste updaten (ist immer gut)
				currentList.setContacts(al);
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
				GWT.log("7.5  " + "add " + contact.getName() + " to " + currentList.getListName() + " to Tree"
						+ currentList.getContacts().toString());
				currentList.addContact(contact);
				e.addContactToListInTree(currentList, contact);
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
}
