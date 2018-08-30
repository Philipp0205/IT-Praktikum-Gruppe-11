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
import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.CellTableResources;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Eine <code>ContactListForm</code> zeigt Optionen für eine Kontaktliste an, um Kontakte zu
 * dieser hinzuzufügen oder welche aus dieser zu entfernen.
 * 
 * @author Anders, Kurrle, Brase
 */
public class ContactListForm extends VerticalPanel {

	private EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	private EditorAdmin e;
	private JabicsUser u;
	private ContactList currentList;
	private Boolean isNewList;

	private MultiSelectionModel<Contact> selectionModelAdd;
	private MultiSelectionModel<Contact> selectionModelRemove;

	private CellTable<Contact> selValuesAdd;
	private CellTable<Contact> selValuesRemove;
	private ListDataProvider<Contact> valueProviderAdd;
	private ListDataProvider<Contact> valueProviderRemove;

	private HashSet<Contact> finalContactAdd;
	private HashSet<Contact> finalContactRemove;

	private ArrayList<Contact> cArray;

	// Die zwei übergreifenden Panels:

	// hinzufügen entfernen suche
	private HorizontalPanel mainPanel1 = new HorizontalPanel();

	private TextBox listBox;
	private Label headline;

	private HorizontalPanel addButtonPanel = new HorizontalPanel();
	private HorizontalPanel removeButtonPanel = new HorizontalPanel();
	private HorizontalPanel searchButtonPanel = new HorizontalPanel();

	// teilen, löschen, speichern
	private HorizontalPanel mainPanel2 = new HorizontalPanel();

	private HorizontalPanel deletePanel = new HorizontalPanel();
	private HorizontalPanel savePanel = new HorizontalPanel();
	private HorizontalPanel sharePanel = new HorizontalPanel();

	// Panels, die erscheinen, wenn kontakte hinzugefügt/entfernt werden
	private VerticalPanel addContactsPanel = new VerticalPanel();
	private VerticalPanel removeContactsPanel = new VerticalPanel();
	
	private HorizontalPanel addPanelButtons = new HorizontalPanel();
	private HorizontalPanel removePanelButtons = new HorizontalPanel();

	private Button searchInListButton = new Button("🔍");
	private Button deleteButton = new Button("🗑");
	private Button saveButton = new Button("✔");
	private Button shareButton = new Button("⋲");
	private Button removeButton = new Button("-");
	private Button addButton = new Button("+");

	private Button saveLabel = new Button("Änderungen speichern");
	private Button shareLabel = new Button("Liste teilen");
	private Button deleteLabel = new Button("Liste löschen");
	private Button addLabel = new Button("Kontakt");
	private Button remLabel = new Button("Kontakt");
	private Button searchLabel = new Button("Suche");

	private Button doneRemove = new Button("✖");
	private Button doneAdd = new Button("✖");
	private Button remove = new Button("Ausgewählte Kontakte aus Liste entfernen");
	private Button add = new Button("Ausgewählte Kontakte hinzufügen");

	private CellTableResources ctRes = GWT.create(CellTableResources.class);

	/**
	 * Konstruktor welcher eine Instanz von <code>ContactListForm</code> erzeugt.
	 * Eine neue <code>ContactListForm</code> erstellt 2 Reihen: Die erste bietet die Optionen
	 * innerhalb der Liste an (Kontakt hinzufügen, Kontakt entfernen, suchen). Die
	 * zweite Reihe bietet Funktionen wie Liste teilen, löschen und abbrechen (Dies
	 * ist der vermutlich längste Konstruktor der Welt)
	 */
	public ContactListForm() {

		// LABELS
		Label listname = new Label("Name:");
		listname.setStyleName("Listenname");
		listBox = new TextBox();
		headline = new Label("Liste: ");

		addButtonPanel.add(addLabel);
		addButtonPanel.add(addButton);
		removeButtonPanel.add(remLabel);
		removeButtonPanel.add(removeButton);
		searchButtonPanel.add(searchLabel);
		searchButtonPanel.add(searchInListButton);

		mainPanel1.add(listname);
		mainPanel1.add(listBox);
		mainPanel1.add(addButtonPanel);
		mainPanel1.add(removeButtonPanel);
		mainPanel1.add(searchButtonPanel);
		
		addPanelButtons.add(add);
		addPanelButtons.add(doneAdd);
		
		removePanelButtons.add(remove);
		removePanelButtons.add(doneRemove);

		removeButton.setStyleName("cdeleteBtn");
		addButton.setStyleName("caddButton");
		listBox.setStyleName("TextBox");
		searchInListButton.setStyleName("searchButton");

		addLabel.setStyleName("claddlabel");
		remLabel.setStyleName("clremlabel");
		searchLabel.setStyleName("clsearchlabel");

		deleteLabel.setStyleName("cldeleteLabel");
		deleteButton.setStyleName("cldeleteButton");
		saveLabel.setStyleName("clsaveLabel");
		saveButton.setStyleName("clsaveButton");
		shareLabel.setStyleName("clshareLabel");
		shareButton.setStyleName("clshareButton");

		/*
		 * ---------- Clickhandler für alle Buttons und Labels -----------------
		 */

		remove.addClickHandler(new DeleteContactsFromListClickHandler());
		doneRemove.addClickHandler(new DoneRemovingClickHandler());
		add.addClickHandler(new AddContactsToContactListClickHandler());
		doneAdd.addClickHandler(new DoneAddingClickHandler());

		saveButton.addClickHandler(new SaveClickHandler());
		saveLabel.addClickHandler(new SaveClickHandler());

		deleteButton.addClickHandler(new DeleteClickHandler());
		deleteLabel.addClickHandler(new DeleteClickHandler());

		addButton.addClickHandler(new AddClickHandler());
		addLabel.addClickHandler(new AddClickHandler());

		remLabel.addClickHandler(new RemoveClickHandler());
		removeButton.addClickHandler(new RemoveClickHandler());

		shareButton.addClickHandler(new ShareClickHandler());
		shareLabel.addClickHandler(new ShareClickHandler());

		searchLabel.addClickHandler(new SearchClickHandler());

		deletePanel.add(deleteLabel);
		deletePanel.add(deleteButton);
		savePanel.add(saveLabel);
		savePanel.add(saveButton);
		sharePanel.add(shareLabel);
		sharePanel.add(shareButton);
		mainPanel2.add(deletePanel);
		mainPanel2.add(savePanel);
		mainPanel2.add(sharePanel);

		// Komplexere Datenstrukturen und Objekte

		cArray = new ArrayList<Contact>();

		selValuesAdd = new CellTable<Contact>(100, ctRes);
		selValuesRemove = new CellTable<Contact>(100, ctRes);

		valueProviderAdd = new ListDataProvider<Contact>();
		valueProviderAdd.addDataDisplay(selValuesAdd);
		valueProviderRemove = new ListDataProvider<Contact>();
		valueProviderRemove.addDataDisplay(selValuesRemove);

		selectionModelAdd = new MultiSelectionModel<Contact>();
		selectionModelRemove = new MultiSelectionModel<Contact>();
		selValuesAdd.setSelectionModel(selectionModelAdd);
		selValuesRemove.setSelectionModel(selectionModelRemove);

		selectionModelAdd.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				finalContactAdd = (HashSet<Contact>) selectionModelAdd.getSelectedSet();
			}
		});

		selectionModelRemove.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				finalContactRemove = (HashSet<Contact>) selectionModelRemove.getSelectedSet();
			}
		});

		Column<Contact, Boolean> checkboxAdd = new Column<Contact, Boolean>(new CheckboxCell(true, false)) {
			public Boolean getValue(Contact object) {
				return selectionModelAdd.isSelected(object);
			}
		};
		Column<Contact, Boolean> checkboxRemove = new Column<Contact, Boolean>(new CheckboxCell(true, false)) {
			public Boolean getValue(Contact object) {
				return selectionModelRemove.isSelected(object);
			}
		};
		Column<Contact, String> contactAdd = new Column<Contact, String>(new TextCell()) {
			public String getValue(Contact object) {
				return object.toString();
			}
		};
		Column<Contact, String> contactRemove = new Column<Contact, String>(new TextCell()) {
			public String getValue(Contact object) {
				return object.toString();
			}
		};

		selValuesAdd.addColumn(checkboxAdd, "Auswahl");
		selValuesAdd.setColumnWidth(checkboxAdd, 30, Unit.PX);
		selValuesAdd.addColumn(contactAdd, "Kontakt");
		selValuesAdd.setColumnWidth(contactAdd, 300, Unit.PX);

		selValuesRemove.addColumn(checkboxRemove, "Auswahl");
		selValuesRemove.setColumnWidth(checkboxRemove, 30, Unit.PX);
		selValuesRemove.addColumn(contactRemove, "Kontakt");
		selValuesRemove.setColumnWidth(contactRemove, 300, Unit.PX);

		removeContactsPanel.add(selValuesRemove);
		removeContactsPanel.add(removePanelButtons);

		addContactsPanel.add(selValuesAdd);
		addContactsPanel.add(addPanelButtons);
	}

	/**
	 * Wird beim ersten laden der ContactListForm ausgeführt.
	 */
	public void onLoad() {

		sharePanel.setVisible(false);
		userIsOwner();

		headline.setText("Liste: " + currentList.getListName());

		headline.setStyleName("contactListHeadline");

		this.add(headline);


		if (isNewList == true) {
			saveLabel.setText("Liste erstellen");
			deletePanel.setVisible(false);
			removeButtonPanel.setVisible(false);

		} else {
			addButtonPanel.setVisible(true);
			deletePanel.setVisible(true);
			removeButtonPanel.setVisible(true);
			addButtonPanel.setVisible(true);
		}

		searchInListButton.addClickHandler(new SearchClickHandler());

		
		sharePanel.setStyleName("sharePanel");

		this.add(mainPanel1);
		this.add(mainPanel2);
		this.add(addContactsPanel);
		this.add(removeContactsPanel);
		addContactsPanel.setVisible(false);
		removeContactsPanel.setVisible(false);
	}

	/**
	 * Setzt die aktuelle Liste.
	 * 
	 * @param cl Liste, die gesetzt werden soll.
	 */
	public void setCurrentList(ContactList cl) {
		if (cl != null) {
			this.currentList = cl;
			if (cl.getId() != 0) {
				this.currentList = cl;

				if (cl.getListName() != null) {
					listBox.setText(cl.getListName());
				}

				deletePanel.setVisible(true);
				sharePanel.setVisible(true);
			} else {
				this.currentList = null;
				deletePanel.setVisible(false);
				sharePanel.setVisible(false);
			}
		}
	}
	
	/**
	 * Den Besitzer der Liste setzen
	 */
	public void setOwner(JabicsUser u) {
		this.currentList.setOwner(u);
	}
	
	/**
	 * Feststellen, ob der Besitzer der Kontaktliste der aktuelle Nutzer ist
	 */
	public void userIsOwner() {
		if(currentList.getOwner() == null) {
			editorService.getOwnerOfContactList(currentList, new AsyncCallback<JabicsUser>() {
				public void onFailure(Throwable caught) {
				}

				public void onSuccess(JabicsUser result) {
					setOwner(result);
					userIsOwner();
				}
			});
		}
		if(u != null && currentList.getOwner() != null) {
			if(u.getId() == currentList.getOwner().getId()) {
				this.sharePanel.setVisible(true);
			}
		}else {
			this.sharePanel.setVisible(false);
		}
	}

	/**
	 * Entfernt das Panel, das die Möglichkeit gibt, Kontakte hizuzufügen.
	 */
	void removeAddPanel() {
		addButtonPanel.setVisible(true);
		removeButtonPanel.setVisible(true);
		addContactsPanel.setVisible(false);
	}

	/**
	 * Entfernt das Panel, das die Möglichkeit gibt, Kontakte zu entfernen.
	 */
	void removeRemovePanel() {
		addButtonPanel.setVisible(true);
		removeButtonPanel.setVisible(true);
		removeContactsPanel.setVisible(false);
	}
	
	/**
	 * Speichert die Änderungen, indem die <code>ContctList</code> aktuallisiert wird.
	 */
	void save() {
		currentList.setListName(listBox.getValue());
		editorService.updateContactList(currentList, new UpdateContactListCallback());
		valueProviderAdd.flush();
	}

	/**
	 * Diese Methode fügt ein Auswahlfenster für alle Kontakte, die ein Nutzer sehen
	 * kann, unter der ContactForm (bzw darin, aber unter der Anzeige der
	 * allgemeinen Informationen) ein. Es können Kontakte ausgewählt werden und
	 * durch Klick auf einen Button der Liste hinzugefügt werden.
	 * 
	 * @param allC
	 * 				Liste alle <code>Contact</code> eines Nutzers
	 */
	public void showAddContactPanel(ArrayList<Contact> allC) {
		addContactsPanel.setVisible(true);
		removeButtonPanel.setVisible(false);
		addButtonPanel.setVisible(false);
		valueProviderAdd.setList(allC);
		valueProviderAdd.flush();

	}

	/**
	 * Diese Methode ist praktisch identisch zu addContacts(). Sie fügt ein
	 * Auswahlfenster für alle Kontakte, die ein Nutzer sehen kann, unter der
	 * ContactForm (bzw darin, aber unter der Anzeige der allgemeinen Informationen)
	 * ein. Es können Kontakte ausgewählt werden und durch Klick auf einen Button
	 * aus der Liste entfernt werden.
	 * 
	 * @param allC
	 * 			Liste alle <code>Contact</code> Objekte eines Nutzers
	 */
	public void showRemoveContactPanel(ArrayList<Contact> allC) {
		removeButtonPanel.setVisible(false);
		addButtonPanel.setVisible(false);
		valueProviderRemove.setList(allC);
		valueProviderAdd.flush();
		removeContactsPanel.setVisible(true);

	}

	/**
	 * Setzt den Indikator, der angiebt, ob es sich um eine neue Liste handelt.
	 * 
	 * @param b Wert der gesetzt werden soll.
	 */
	public void setIsNewList(boolean b) {
		this.isNewList = b;
	}

	/**
	 * Setzt die aktuelle Kontaktliste
	 * 
	 * @param cl Kontaktliste, die gesetzt werden soll.
	 */
	public void setContactList(ContactList cl) {
		this.currentList = cl;
	}

	/**
	 * Setzt den Editor.
	 * 
	 * @param e 
	 * 		<code>Editor</code> der gesetzt werden soll.
	 */
	public void setEditor(EditorAdmin e) {
		this.e = e;
	}

	/**
	 * Setzt den User der ContactListForm
	 * 
	 * @param u 
	 * 			<code>User</code> der gesetzt werden soll.
	 */
	public void setUser(JabicsUser u) {
		this.u = u;
	}

	/* ------------------CLICK HANDLER ------------------- */

	/**
	 * Clickhanlder welcher für das Löschen von Konrakten aus Listen verantwortlich
	 * ist.
	 *
	 */
	private class DeleteContactsFromListClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			removeRemovePanel();
			for (Contact c : finalContactRemove) {
				editorService.removeContactFromList(c, currentList, new RemoveContactFromListCallback());
			}
		}
	}

	/**
	 * ClickHandler, welcher für das Hinzufügen von Kontakten zu Listen
	 * verantwortlich ist.
	 *
	 */
	private class AddContactsToContactListClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			removeAddPanel();
			for (Contact c : finalContactAdd) {
				editorService.addContactToList(c, currentList, new AddContactToListCallback());
			}
		}
	}

	/**
	 * ClickHanlder welcher Panel beim Klicken auf "Fertig" entfernt. Der Button
	 * wird beim Hinzufügen von Kontakten zu einer Liste angezeigt.
	 *
	 */
	private class DoneAddingClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			removeAddPanel();
			addButtonPanel.setVisible(true);
			removeButtonPanel.setVisible(true);
			valueProviderAdd.setList(null);
			valueProviderAdd.flush();
		}
	}

	/**
	 * ClickHanlder welcher Panel beim Klicken auf "Fertig" entfernt. DEr Button
	 * wird beim Entfernen von Kotakten aus einer Liste angezeigt.
	 *
	 */
	private class DoneRemovingClickHandler implements ClickHandler {
		public void onClick(ClickEvent e) {
			removeRemovePanel();
			addButtonPanel.setVisible(true);
			removeButtonPanel.setVisible(true);
			valueProviderAdd.setList(null);
			valueProviderAdd.flush();
		}
	}

	/**
	 * CLickHanlder, welcher für das Löschen eines Kontaktes aus einer Liste
	 * verantwortlich ist.
	 *
	 */
	class DeleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			editorService.deleteContactList(currentList, u, new DeleteContactListCallback());
		}
	}

	/**
	 * KlickHandler, welcher für das Speichern verantwortlich ist.
	 *
	 */
	class SaveClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if (isNewList) {
				editorService.createContactList(listBox.getText(), cArray, u, new CreateContactListCallback());
			} else {
				save();
			}
		}
	}

	/**
	 * Clickhanlder welcher für das Teilen von Kontakten verantwortlich ist.
	 *
	 */
	class ShareClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

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
			addButtonPanel.setVisible(false);
			removeButtonPanel.setVisible(false);
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
	 * Clickhandler und asynchrone Methodenaufrufe für das Löschen eines
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

	/**
	 * Eine upgedatete Kontaktliste um Tree anzeigen und auch die ContctList form
	 * damit aktualisieren
	 * 
	 */
	private class UpdateContactListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Liste konnte nicht geupdated werden.");
		}

		public void onSuccess(ContactList cl) {
			if (cl != null) {
				e.updateContactListInTree(cl);
				setCurrentList(cl);
				onLoad();
			}
		}
	}

	/**
	 * Alle Kontakte einer Liste zurückerhalten und in den valueProvider einspeisen,
	 * damit Kontakte entfernt werden können.
	 *
	 */
	private class GetContactsOfListCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Kontakte konnten nicht geladen werden.");
		}

		public void onSuccess(ArrayList<Contact> al) {

			if (al != null) {
				// Die Liste updaten (ist immer gut)
				currentList.setContacts(al);
				showRemoveContactPanel(al);
			}
		}
	}

	/**
	 * Alle Kontakte eines Nutzers erhalten, damit Kontakte zu einer Liste
	 * hinzugefügt werden können.
	 *
	 */
	private class GetAllContactsOfUserCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Kontakte konnten nicht geladen werden.");
		}

		public void onSuccess(ArrayList<Contact> al) {
			if (al != null) {
				showAddContactPanel(al);
			}
		}
	}

	/**
	 * Einen Kontakt im Tree hinzufügen, wenn er erfolgreich zur Liste hinzugefügt
	 * wurde.
	 *
	 */
	private class AddContactToListCallback implements AsyncCallback<Contact> {

		public void onFailure(Throwable arg0) {
			Window.alert("Kontakt konnte nicht hinzugefügt werden");
		}

		public void onSuccess(Contact contact) {
			if (contact != null) {
				removeAddPanel();
				currentList.addContact(contact);
				e.addContactToListInTree(currentList, contact);
			}
		}
	}

	/**
	 * Einen Kontakt aus einer Liste im Tree entfernen, wenn er erfolgreich aus der
	 * Liste entfernt wurde
	 *
	 */
	private class RemoveContactFromListCallback implements AsyncCallback<Contact> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Kontakt konnte nicht hinzugefügt werden");
		}

		@Override
		public void onSuccess(Contact contact) {
			if (contact != null) {
				removeRemovePanel();
				currentList.removeContact(contact);
				e.removeContactFromContactListInTree(currentList, contact);
			}
		}
	}

	/**
	 * Eine neu erstellte Liste zurodnen und im Tree anzeigen, wenn Sie erstellt
	 * wurde.
	 *
	 */
	private class CreateContactListCallback implements AsyncCallback<ContactList> {
		@Override
		public void onFailure(Throwable caught) {
			
		}
		@Override
		public void onSuccess(ContactList cl) {
			if (cl != null) {
				isNewList = false;
				setCurrentList(cl);
				saveLabel.setText("Änderungen speichern");
				deletePanel.setVisible(true);
				e.addContactListToTree(cl);
			}
		}
	}

	/**
	 * Eine Liste löschen, wenn erfolgreich nichts anzeigen.
	 *
	 */
	private class DeleteContactListCallback implements AsyncCallback<ContactList> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Kontaktliste konnte nicht gelöscht werden.");

		}

		@Override
		public void onSuccess(ContactList cl) {
			e.removeContactListFromTree(cl);
			e.showMenuOnly();
		}
	}

}
