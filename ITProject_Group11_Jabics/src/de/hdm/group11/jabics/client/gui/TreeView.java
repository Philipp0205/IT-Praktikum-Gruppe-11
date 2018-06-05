package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
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
 */

public class TreeView implements TreeViewModel {
	
	private ContactView cView; 
	private ContactListView clView;
	
	private Contact selectedContact;
	private ContactList selectedContactList;
	
	private ArrayList<ContactList> cLists = new ArrayList<ContactList>();
	private EditorServiceImpl eService = new EditorServiceImpl();
	
	private ListDataProvider<Contact> contactDataProvider = null;
	private EditorServiceAsync eServiceAsync = null;
	
	/*
	 * In der Map werden die ListDataProviders für die expandierten Kontakte gepesichert.
	 */
	private Map<ContactList, ListDataProvider<Contact>> ContactDataProviders = null;
	
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
	 * @author Philipp
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
		
		

		private void setSelectedContactList(ContactList selection) {
			// TODO Auto-generated method stub
			
		}

		private void setSelectedContact(Contact selection) {
			// TODO Auto-generated method stub
			
		}
		
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
