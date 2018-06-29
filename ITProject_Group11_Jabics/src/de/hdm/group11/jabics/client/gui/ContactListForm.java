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
	Boolean isNewList = true;

	MultiSelectionModel<Contact> selectionModel;

	CellTable<Contact> selValues;
	ListDataProvider<Contact> valueProvider;

	HorizontalPanel sharePanel = new HorizontalPanel();
	HorizontalPanel editPanel = new HorizontalPanel();
	HorizontalPanel changePanel = new HorizontalPanel();

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
	
	ArrayList<Contact> cArray;

	ContactCellListTab cTab = new ContactCellListTab();
	ListDataProvider<Contact> contactDataProvider;

	public ContactListForm() {

		contactDataProvider = cTab.getContactDataProvider();
		selValues = new CellTable<Contact>();
		valueProvider = new ListDataProvider<Contact>();
		valueProvider.addDataDisplay(selValues);
		// finalC;
		// Es kann sein, dass hier noch kexprovider benötigt werden

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
		
		//GWT.log("isNewList: " + isNewList);
		
		if (isNewList) {
			deleteButton.setVisible(false);
		}


		/**
		 * 3 Reihen Die erste bietet die Optionen auf Listenebene an (Liste teilen1,
		 * Liste teilen 2). Die zweite bietet die Option an, die Liste zu löschen. Die
		 * dritte bietet die Optionen innerhalb der Liste an (Kontakt hinzufügen,
		 * Kontakt entfernen)
		 */

		shareButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// e.showContactListCollab(currentList);
				removeAddPanel();
				removeRemovePanel();
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
		// Kontakte hinzufügen
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editorService.getContactsOf(u, new GetAllContactsOfUserCallback());
				GWT.log("7.2 AddButton");
				GWT.log("7.2 User: " + u.getId());
				
				cArray = new ArrayList<Contact>();
//				if (isNewList) {
//					editorService.createContactList(listBox.getText(), cArray, u, new CreateContactListCallback());
//				}
				
				editorService.createContactList(listBox.getText(), cArray, u, new CreateContactListCallback());
				
				editorService.getContactsOf(u, new GetAllContactsOfUserCallback());
				// contacts = contactDataProvider.getList();
			}
		});
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("7.2 removeButton");
				editorService.getContactsOfList(currentList, u, new GetContactsOfListCallback());
			}
		});

		sharePanel.add(shareButton);
		sharePanel.add(shareExistingButton);
		editPanel.add(addButton);
		editPanel.add(removeButton);
		changePanel.add(deleteButton);
		changePanel.add(saveButton);

		sharePanel.setStyleName("sharePanel");

		this.add(listBox);
		this.add(sharePanel);
		this.add(editPanel);
		this.add(changePanel);
		this.add(addPanel);
		this.add(removePanel);

	}

	public void setCurrentList(ContactList cl) {
		this.currentList = cl;

		if (cl != null) {
			GWT.log("7.3.1 CurrentList: " + cl.getListName());
			this.currentList = cl;
			GWT.log(currentList.getContacts().toString());
			// valueProvider.setList(cl.getContacts());
			if (cl.getListName() != null) {
				listBox.setText(cl.getListName());
			}
			// this.u = u;
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
		
		//Überprüfen ob Name gesetzt
		boolean nameExistent = false;
		//for ()

		/*
		 * TODO: implement
		 */
		//Window.alert("Not yet implemented");
		
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

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				/**
				 * TODO: überlegen ob nächste Zeile benötigt oder durch clickhandler in button
				 * add abgedeckt!
				 */
				HashSet<Contact> finalC = (HashSet<Contact>) selectionModel.getSelectedSet();
				//Window.alert("Auswahl geändert");
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
				//GWT.log("7.3 selected getPValue 2");
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
					
					GWT.log("7.4 Add Contact " + c.getName() + "to List " + currentList.getId()+ " " + currentList.getListName() + " " +
				currentList.getContacts().toString());
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
				return selectionModel.isSelected(object);
			}
		};
		Column<Contact, String> contact = new Column<Contact, String>(new TextCell()) {
			public String getValue(Contact object) {
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
				for (Contact c : selectionModel.getSelectedSet()) {
					editorService.removeContactFromList(c, currentList, new AddContactToListCallback());
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

	private class DeleteContactListCallback implements AsyncCallback<Void> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Liste konnte nicht gelöscht werden.");
		}

		public void onSuccess(Void v) {

			if (v != null) {
				e.onModuleLoad();
			}

		}
	}

	private class UpdateContactListCallback implements AsyncCallback<ContactList> {
		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Liste konnte nicht gelöscht werden.");
		}

		public void onSuccess(ContactList cl) {

			if (cl != null) {
				GWT.log("7.4 UpdateContactListCallback on Success");

				/**
				 * TODO: die geupdatete ContactList in den TreeView wieder einfügen bzw
				 * anzeigen?
				 */
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
				currentList.addContacts(al);
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

	private class AddContactToListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler 3! Kontakt konnte nicht hinzugefügt werden");
		}

		public void onSuccess(ContactList list) {

			if (list != null) {
				
				GWT.log(list.getContacts().toString());
				setCurrentList(list);

				Window.alert("Kontakte hinzugefügt");
				/**
				 * TODO: diese liste auch in dem TreeViewModel updaten!
				 */
				GWT.log("7.5  " + "add " + currentList.getListName() + " "+ currentList.getContacts().toString() + " to Tree" + 
				 currentList.getContacts().toString());
				e.addContactListToTree(currentList);

				//onLoad();

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
				isNewList = false;
				deleteButton.setVisible(true);
			}
			
		}
		
	}

	public void setIsNewList(boolean b) {
		this.isNewList = b;

	}

	public void setContactList(ContactList cl) {
		this.currentList = cl;

	}
}
