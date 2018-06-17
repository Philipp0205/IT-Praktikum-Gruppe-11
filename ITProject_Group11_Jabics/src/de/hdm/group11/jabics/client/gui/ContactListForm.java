package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.gui.ContactForm.GetPValuesCallback;
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
	 * @author Brase
	 * @author Ilg
	 */
	
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Editor e;
	JabicsUser u = null;
	ContactList currentList = null;
	
	Grid contactListGrid;
	
	VerticalPanel listEdit, conEdit;
	HorizontalPanel listShareBox, listDeleteBox, listAddBox, listRmvBox;
	
	MultiSelectionModel<Contact> selectionModel;
	
	Button deleteListButton, shareListButton;
	
	MultiSelectionModel<Contact> selectionModel1  = new MultiSelectionModel<Contact>();
	
	MultiSelectionModel<Contact> selectionModel2  = new MultiSelectionModel<Contact>();
		
		
		
	public void onLoad() { // Editor e, ContactList cl) {

		/*
		 * noch rausfinden ob das geht this.currentList = cl; this.e = e;
		 */

		super.onLoad();
		// For Debugging
		Window.alert("Neue CL Form");

		contactListGrid = new Grid(6, 1);

		Label formName = new Label("Listen-Editor");
		contactListGrid.setWidget(0, 0, formName);

		Label listName = new Label(currentList.getListName());
		contactListGrid.setWidget(1, 0, listName);

		/**
		 * 2 Vertical Panels:
		 * 
		 * Die erste bietet die Optionen auf Listenebene an (Liste teilen, Liste
		 * löschen) Die zweite bietet die Optionen innerhalb der Liste an (Kontakt
		 * hinzufügen, Kontakt entfernen)
		 */

		// Beginn Reihe 1
		listEdit = new VerticalPanel();

		listShareBox = new HorizontalPanel();
		Label shareQuestion = new Label("Wollen Sie diese Liste teilen?");
		listShareBox.add(shareQuestion);

		shareListButton = new Button("Liste teilen");
		shareListButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				e.showContactListCollab(currentList);
			}
		});
		listShareBox.add(shareListButton);

		listDeleteBox = new HorizontalPanel();
		Label deleteQuestion = new Label("Wollen Sie diese Liste löschen?");
		listDeleteBox.add(deleteQuestion);

		deleteListButton = new Button("Liste löschen");
		deleteListButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editorService.deleteContactList(currentList, u, new DeleteContactListCallback());
			}
		});
		listDeleteBox.add(deleteListButton);

		listEdit.add(listShareBox);
		listEdit.add(listDeleteBox);
		contactListGrid.setWidget(2, 0, listEdit);

		// Beginn Reihe 2
		conEdit  = new VerticalPanel();

		listAddBox = new HorizontalPanel();
		Label addQuestion = new Label("Wollen Sie Kontakte hinzufügen?");
		listAddBox.add(addQuestion);

		Button addButton = new Button("Kontakte hinzufügen");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editorService.getContactsOf(u, new GetAllContactsOfUserCallback());
			}
		});
		listAddBox.add(addButton);

		listRmvBox = new HorizontalPanel();
		Label rmvQuestion = new Label("Wollen Sie Kontakte entfernen?");
		listRmvBox.add(rmvQuestion);

		Button removeButton = new Button("Kontakte entfernen");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editorService.getContactsOfList(currentList, u, new GetContactsOfListCallback());
			}
		});
		listRmvBox.add(removeButton);

		conEdit.add(listAddBox);
		conEdit.add(listRmvBox);
		contactListGrid.setWidget(3, 0, conEdit);

	}
	
	public void setCurrentList(ContactList cl) {
		this.currentList = cl;
		if (cl != null) {
			this.currentList = cl;
			//this.u = u;
			deleteListButton.setEnabled(true);
			shareListButton.setEnabled(true);
		} else {
			this.currentList = null;
			deleteListButton.setEnabled(false);
			shareListButton.setEnabled(false);
		}
	}
	
	/**
	 * Diese Methode fügt ein Auswahlfenster für alle Kontakte, die ein Nutzer sehen kann, unter der ContactForm
	 * (bzw darin, aber unter der Anzeige der allgemeinen Informationen) ein.
	 * Es können Kontakte ausgewählt werden und durch Klick auf einen Button der Liste hinzugefügt werden.
	 * 
	 * @param ArrayList<Contact> alle Kontakte eines Nutzers
	 */
	public void addContactPanel(ArrayList<Contact> allC) {
		//PValue selectedPV;
		CellTable<Contact> selValues = new CellTable<Contact>();
		ListDataProvider<Contact> valueProvider = new ListDataProvider<Contact>(allC);
		valueProvider.addDataDisplay(selValues);
		 //finalC;
		// Es kann sein, dass hier noch kexprovider benötigt werden

		selectionModel  = new MultiSelectionModel<Contact>();

		
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	         public void onSelectionChange(SelectionChangeEvent event) {
	        	 /**
	        	  *  TODO: überlegen ob nächste Zeile benötigt oder durch clickhandler in button add abgedeckt!
	        	  */
	        	 HashSet<Contact> finalC = (HashSet<Contact>) selectionModel1.getSelectedSet();
	        	 Window.alert("Auswahl geändert");
	         }
	    });
		
		selValues.setSelectionModel(selectionModel1);
		
		Column<Contact, Boolean> checkbox = new Column<Contact, Boolean>(new CheckboxCell(true, false)){
			public Boolean getValue(Contact object) {
		        return selectionModel1.isSelected(object);
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
		
		Button add = new Button("Ausgewählte Kontakte hinzufügen");
		add.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				for(Contact c : selectionModel1.getSelectedSet()) {
					/*
					 * TODO hier gibt es zwei möglichkeiten der Implementierung: nummer 2 ist auskommatiert, noch entscheiden welhes besser ist!
					 */
					editorService.addContactToList(c, currentList, new AddContactToListCallback());
					/*
					currentList.addContact(c);
					editorService.updateContact(c, UpdateContactCallback);
					*/
				}
			}
		});
		
		contactListGrid.setWidget(3, 0, selValues);
		contactListGrid.setWidget(4, 0, add);
	}
	
	/**
	 * Diese Methode ist praktisch identisch zu addContacts(). Sie fügt ein Auswahlfenster für alle Kontakte,
	 * die ein Nutzer sehen kann, unter der ContactForm (bzw darin, aber unter der Anzeige der allgemeinen Informationen) ein.
	 * Es können Kontakte ausgewählt werden und durch Klick auf einen Button aus der Liste entfernt werden.
	 * 
	 * @param ArrayList<Contact> alle Kontakte eines Nutzers
	 */
	public void removeContactPanel(ArrayList<Contact> allC) {
		//PValue selectedPV;
		CellTable<Contact> selValues = new CellTable<Contact>();
		ListDataProvider<Contact> valueProvider = new ListDataProvider<Contact>(allC);
		valueProvider.addDataDisplay(selValues);
		 //finalC;
		// Es kann sein, dass hier noch kexprovider benötigt werden

		
		selectionModel2.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	         public void onSelectionChange(SelectionChangeEvent event) {
	        	 /**
	        	  *  TODO: überlegen ob nächste Zeile benötigt oder durch clickhandler in button add abgedeckt!
	        	  */
	        	 HashSet<Contact> finalC = (HashSet<Contact>) selectionModel2.getSelectedSet();
	        	 Window.alert("Auswahl geändert");
	         }
	    });
		
		selValues.setSelectionModel(selectionModel2);
		
		Column<Contact, Boolean> checkbox = new Column<Contact, Boolean>(new CheckboxCell(true, false)){
			public Boolean getValue(Contact object) {
		        return selectionModel2.isSelected(object);
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
				 * TODO hier gibt es zwei möglichkeiten der Implementierung nummer 2 ist auskommatiert, noch entscheiden welhes besser ist!
				 */
				for(Contact c : selectionModel2.getSelectedSet()) {
					editorService.removeContactFromList(c, currentList, new AddContactToListCallback());
					/*
					currentList.removeContact(c);
					editorService.updateContact(c, UpdateContactCallback);
					*/
				}
			}
		});
		
		contactListGrid.setWidget(3, 0, selValues);
		contactListGrid.setWidget(4, 0, remove);
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
			e.onModuleLoad();
		}
	}
	private class UpdateContactListCallback implements AsyncCallback<ContactList> {
		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Liste konnte nicht gelöscht werden.");
		}

		public void onSuccess(ContactList cl) {
			/**
			 * TODO: die geupdatete ContactList in den TreeView wieder einfügen bzw anzeigen?
			 */
			setCurrentList(cl);
			onLoad();
			
		}
	}
	private class GetContactsOfListCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Kontakte konnten nicht geladen werden.");
		}
		public void onSuccess(ArrayList<Contact> al) {
			currentList.addContacts(al);
			removeContactPanel(al);
		}
	}
	private class GetAllContactsOfUserCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Kontakte konnten nicht geladen werden.");
		}
		public void onSuccess(ArrayList<Contact> al) {
			addContactPanel(al);
		}
	}
	private class AddContactToListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable arg0) {
			Window.alert("Fehler! Kontakt konnte nicht hinzugefügt werden");
		}
		public void onSuccess(ContactList list) {
			Window.alert("Kontakte hinzugefügt");
			/**
			 * TODO: diese liste auch in dem TreeViewModel updaten!
			 */
			setCurrentList(list);
			onLoad();
		}
	}
}
