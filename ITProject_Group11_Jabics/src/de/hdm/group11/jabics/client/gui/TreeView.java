package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * @author Philipp
 * 
 * Struktur von @author Thies
 *
 */

public class TreeView implements TreeViewModel {
	
	private ContactView cView; 
	private ContactListView clView;
	
	private Contact selectedContact;
	private ContactList selectedContactList;
	
	private ArrayList<ContactList> cLists = new ArrayList<ContactList>();
	private EditorServiceImpl eService = new EditorServiceImpl();
	
	private ListDataProvider<Contact> contactDataProviders = null;
	private EditorServiceAsync eServiceAsync = null;
	
	
	public TreeView() {
		boKeyProvider = new BusinessObjectKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time." 
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		
		contactListDataProvider = new HashMap<Contact, ListDataProvider<ContactList>>();
		
	}
	
	/*
	 * In der Map werden die ListDataProviders für die expandierten Kontakte gepespeichert.
	 * 
	 * Das Java Map Interface "mappt" einzigartige Schlüssel (keys) und den zugehörigen Wert (value), vergleichbar mit einem Wörterbuch oder 
	 * Zuweisungstabellen in der DB. Die values können jeder Zeit anhand der Keys aufgerufen werden. 
	 * 
	 * Beispiel: 
	 * key: 1234 --> Value: Kontakt (Max, Mustermann, 1990, ...)  
	 * 
	 * Weiter im Text: 
	 * Der DataProvider ist dafür zuständig, die Anzeige zu aktuallisieren, immer wenn etwas geändert wird. 
	 * Also Controller (m-v-c-Modell), zwischen der Anzeige (CellTable) und dem Modell (Liste von Objekten).
	 */
	private Map<Contact, ListDataProvider<ContactList>> contactListDataProvider = null;
	
	/**
	 * In folgender Klasse werden BusinessObjects auf eindeutige Zahlenobjekte abgebildet, die als Schlüssel für Baumknoten dienen. 
	 * Dadurch werden im Selektionsmodell alle Objekte mit derselben id selektiert, wenn eines davon selektiert wird. Der
	 * Schlüssel für Kontaktobjekte ist eine positive, der für Kundenobjekte eine
	 * negative Zahl, die sich jeweils aus der id des Objektes ergibt. Dadurch
	 * können Kunden- von Kontenobjekten unterschieden werden, auch wenn sie dieselbe id haben.
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
	 * im Baum ausgewählt wird. Es wird zwischen ausgewählten Kontakten und Kontaktlisten unterschieden.
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
	

	public ContactView getContactView() {
		return cView;
	}

	public void setContactView(ContactView cView) {
		this.cView = cView;
	}

	public ContactListView getContactListView() {
		return clView;
	}

	public void setContactListView(ContactListView clView) {
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
		//cView.setSelected(c);
		//
		
	}
	
	 public Contact getSelectedContact() {
		return selectedContact;
	}
	 
	 /**
	  * Erstellen von einem neuen Kontakt.
	  */
	 public void addContact(Contact c) { 
		 contactDataProviders.getList().add(c);
		 selectionModel.setSelected(c, true);
		 
	 }
	 
	 public void updateContact(Contact c) {
		 List<Contact> contacts = contactDataProviders.getList();
		 int i = 0;
			for (Contact c2 : contacts) {
				if (c.getId() == c.getId()) {
					contacts.set(i, c);
					break;
				} else {
					i++;
				}
			}
			contactDataProviders.refresh();
	 }
	 
	 public void removeContact(Contact c) {
		 contactDataProviders.getList().remove(c);
		 contactListDataProvider.remove(c);
	 }
	 
	 public void addContactList(Contact c, ContactList cl) { 
		 if (!contactListDataProvider.containsKey(c)) {
			 return;
		 }
		 ListDataProvider<ContactList> contactsProvider = contactListDataProvider.get(c);
		 if (!contactsProvider.getList().contains(cl)) {
			 contactsProvider.getList().add(cl);
			 
		 }
		 selectionModel.setSelected(cl, true);
	 }


	/**
     * Get the {@link NodeInfo} that provides the children of the specified
     * value.
     */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

}
