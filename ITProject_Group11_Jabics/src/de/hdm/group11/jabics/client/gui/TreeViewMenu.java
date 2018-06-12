package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.User;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * Diese Klasse repr�sentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * @author Philipp
 * 
 * Struktur von @author Thies
 *
 */

public class TreeViewMenu implements TreeViewModel {
	
	private ContactForm cView; 
	private ContactListForm clView;
	
	private Contact selectedContact;
	private ContactList selectedContactList;
	
	private ArrayList<ContactList> cLists = new ArrayList<ContactList>();
	// User?
	private EditorServiceAsync eService = null;
	private User user = new User();
	
	/*
	 * Der DataProvider ist daf�r zust�ndig, die Anzeige zu aktualisieren, immer wenn etwas ge�ndert wird. 
	 * Also Controller (m-v-c-Modell), zwischen der Anzeige (CellTable) und dem Modell (Liste von Objekten).
	 * 
	 * In diesem Fall werden werden Kontaktlisten bereitgestellt. 
	 */
	private ListDataProvider<ContactList> contactListDataProviders = null;
	private EditorServiceAsync eServiceAsync = null;
	
	
	public TreeViewMenu() {
		boKeyProvider = new BusinessObjectKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time." 
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		/*
		 * Assoziativspeicher, bei dem Kontakte Kontaktlisten zugeordnet werden.
		 * Freunde --> Max Mustermann 
		 * 
		 * (wird weiter unten deklariert)
		 */
		contactDataProviders = new HashMap<ContactList, ListDataProvider<Contact>>();
		
	}
	
	/*
	 * In der Map werden die ListDataProviders f�r die expandierten Kontakte gepespeichert.
	 * 
	 * Das Java Map Interface "mappt" einzigartige Schl�ssel (keys) und den zugeh�rigen Wert (value), vergleichbar mit einem W�rterbuch oder 
	 * Zuweisungstabellen in der DB. Die values k�nnen jeder Zeit anhand der Keys aufgerufen werden. Also ein Assoziativspeicher. 
	 * 
	 * Beispiel: 
	 * key: 1234 --> Value: Kontakt (Max, Mustermann, 1990, ...)  
	 * 
	 */
	private Map<ContactList, ListDataProvider<Contact>> contactDataProviders = null;
	
	/**
	 * In folgender Klasse werden BusinessObjects auf eindeutige Zahlenobjekte abgebildet, die als Schl�ssel f�r Baumknoten dienen. 
	 * Dadurch werden im Selektionsmodell alle Objekte mit derselben id selektiert, wenn eines davon selektiert wird. Der
	 * Schl�ssel f�r Kontaktobjekte ist eine positive, der f�r Kundenobjekte eine
	 * negative Zahl, die sich jeweils aus der id des Objektes ergibt. Dadurch
	 * k�nnen Kunden- von Kontenobjekten unterschieden werden, auch wenn sie dieselbe id haben.
	 * 
	 * @author Thies
	 */
	private class BusinessObjectKeyProvider implements ProvidesKey<BusinessObject> {

		@Override
		public Integer getKey(BusinessObject bo) {
			if (bo ==null) {
				return null;
			}
			if (bo instanceof Contact) {
				return new Integer(bo.getId());
			} else {
				return new Integer(-bo.getId());
			}
		}
		
	};
	
	private BusinessObjectKeyProvider boKeyProvider = null;
	
	private SingleSelectionModel<BusinessObject> selectionModel = null;
	
	/**
	 * Implementation der GWT Klasse SelectionsChangeEvent. Diese Methode regelt, was passiert, wenn ein Objekt
	 * im Baum ausgew�hlt wird. Es wird zwischen ausgew�hlten Kontakten und Kontaktlisten unterschieden.
	 *
	 */
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			BusinessObject selection = selectionModel.getSelectedObject();
			if (selection instanceof Contact) {
				setSelectedContact((Contact) selection);
			} else if (selection instanceof ContactList) {
				setSelectedContactList((ContactList) selection);
			}
					
		}
					
	}
	

	public ContactForm getContactForm() {
		return cView;
	}

	public void setContactForm(ContactForm cView) {
		this.cView = cView;
	}

	public ContactListForm getContactListForm() {
		return clView;
	}

	public void setContactListForm(ContactListForm clView) {
		this.clView = clView;
	}

	private void setSelectedContactList(ContactList cl) {
		selectedContactList = cl;	
		//clView.setSelected(cl);
		
	}
	
	public ContactList getSelectedContactList() {
		return selectedContactList;
	}

	private void setSelectedContact(Contact c) {
		selectedContact	= c;
		// momentan aktiver User muss angegeben werden
		ContactForm.setSelected(c, u);
		
		if (c != null) {
			eService.getUserById(c.getOwner().getId(), new AsyncCallback<User>() {

				@Override
				public void onFailure(Throwable caught) {
					// nix.
					
				}

				@Override
				public void onSuccess(User result) {
					//Muss das result nicht ein Kontakt sein?
					selectedContact = c;
					//contactForm.setSelected(c);				
				}
				
			});
		}
		
	}
	
	 public Contact getSelectedContact() {
		return selectedContact;
	}
	 
	 /**
	  * Erstellen einer neuen Kontaktliste.
	  */
	 public void addContactList(ContactList cl) { 
		 //Neue Kontaktliste wird dem DataProvider hinzugef�gt.
		 contactListDataProviders.getList().add(cl);
		 //Die neue Liste wird ausgew�hlt.
		 selectionModel.setSelected(cl, true);
		 
	 }
	 
	 public void updateContactList(ContactList cl) {
		 List<ContactList> contactlists = contactListDataProviders.getList();
		 int i = 0;
			for (ContactList cl2 : contactlists) {
				if (cl2.getId() == cl2.getId()) {
					contactlists.set(i, cl);
					break;
				} else {
					i++;
				}
			}
			contactListDataProviders.refresh();
	 }
	 
	 public void removeContactList(Contact cl) {
		 contactListDataProviders.getList().remove(cl);
		 contactDataProviders.remove(cl);
	 }
	 
	 /*
	  * Weiter zu den Kontakten
	  */	 
	 public void addContactOfList(ContactList cl, Contact c) {
		 // wenn es noch keinen Kontaktlisten Provider für den Kontakt git, dann wurde der Baum noch nicht geöffnet und es passiert nichts.
		 if (!contactDataProviders.containsKey(cl)) {
			 return;
		 }
		 ListDataProvider<Contact> contactsProvider = contactDataProviders.get(cl);
		 if (!contactsProvider.getList().contains(c)) {
			 contactsProvider.getList().add(c);
			 
		 }
		 selectionModel.setSelected(c, true);
	 }
	 
	 public void removeContactOfContactList(ContactList cl, Contact c) {
		 if (!contactDataProviders.containsKey(cl)) {
			 return;
		 }
		 contactDataProviders.get(cl).getList().remove(c);
		 selectionModel.setSelected(cl, true);
	 }
	 
	 /* TODO
	  * Ein altes Kontakt-Objekt wird durch einen neues ersetzt, die ID bleibt gleich! 
	  */
	 public void updateContact(Contact c) {
		 //eService.getContactById(c.getOwnerID(), new UpdateContactCallback(a));
	 }
	 
	 private class UpdateContactCallback implements AsyncCallback<ContactList> {
		 
		 Contact contact = null;
		 
		 UpdateContactCallback(Contact c) {
			 contact = c;
		 }

		@Override
		public void onFailure(Throwable caught) {
			//nix. 
		}

		@Override
		public void onSuccess(ContactList cl) {
			List<Contact> contacts = contactDataProviders.get(cl).getList();
			
			for (int i = 0; i<contacts.size(); i++) {
				if(contact.getId() == contacts.get(i).getId()) {
					contacts.set(i, contact);
					break;
				}
			}

			
		}
		 
	 }


	/**
     * Get the {@link NodeInfo} that provides the children of the specified
     * value.
     */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if (value.equals("Root")) {
			contactListDataProviders = new ListDataProvider<ContactList>();
			
			eService.getListsOf(user, new AsyncCallback<ArrayList<ContactList>>() {

				@Override
				public void onFailure(Throwable caught) {
					// Nix.
					
				}

				@Override
				public void onSuccess(ArrayList<ContactList> contactlists) {
					for (ContactList cl : contactlists) {
						contactListDataProviders.getList().add(cl);
					}
					
				}
				
			});
			
		// Return a node info that pairs the data with a cell.	
			return new DefaultNodeInfo<ContactList>(contactListDataProviders, new ContactListCell(), selectionModel, null);
			
		}
		
		if (value instanceof ContactList) {
			final ListDataProvider<Contact> contactProvider = new ListDataProvider<Contact>();
			contactDataProviders.put((ContactList) value, contactProvider);
			
			eService.getContactsOf((User) value, new AsyncCallback<ArrayList<Contact>>() {
				

				@Override
				public void onFailure(Throwable caught) {
					// Nix.
					
				}

				@Override
				public void onSuccess(ArrayList<Contact> contacts) {
					for (Contact c : contacts) {
						contactProvider.getList().add(c);
					}			
				}		
			});
			
			// Return a node info that pairs the data with a cell.
			return new DefaultNodeInfo<Contact>(contactProvider, new ContactCell(), selectionModel, null);
		}
		return null;
		
		
	}
	
	// Check if the specified value represents a leaf node. Leaf nodes
	// cannot be opened.
	@Override
	public boolean isLeaf(Object value) {
		// value is of type Account
		return (value instanceof Contact);
	}

}
