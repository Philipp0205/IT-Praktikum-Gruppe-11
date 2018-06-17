package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class ContactCellListTab  {
	
	private Contact selectedContact;
	
	private EditorServiceAsync eService = null;
	//LoginInfo loginfo = new LoginInfo();
	JabicsUser user = new JabicsUser();
	//private final ArrayList<Contact> allcontacts = cMapper.findAllContacts(loginfo.getCurrentUser());
	ListDataProvider<Contact> contactsProvider = new ListDataProvider<Contact>();
	Editor editor;
	
	public ContactCellListTab() {
		
		boKeyProvider = new BusinessObjectKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time." 
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());	
	}
	
	
	
	// ursprünglich onLoad();
	public Widget createTab() {
		
		/*
		 * Der ListDataProvider wird mit den Kontakten befüllt.
		 */
		eService.getContactsOf(JabicsUser.getJabicsUser(), new AsyncCallback<ArrayList<Contact>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Nix.
				
			}

			@Override
			public void onSuccess(ArrayList<Contact> contacts) {
				for (Contact c : contacts) {
					contactsProvider.getList().add(c);
				}
				
			}
					
		});
      
      // Create a CellList using the keyProvider.
      CellList<Contact> cellList = new CellList<Contact>(new ContactCell());
     
           
      /*
       * Redraw the CellList. Sarah/Sara will still be selected because we
       * identify her by ID. If we did not use a keyProvider, 
       * Sara would not be selected.
       */
      cellList.redraw();
      
      return cellList;
      
	}
	
	private class BusinessObjectKeyProvider implements ProvidesKey<BusinessObject> {
		
		/*
		 * Der Key provider für einen Kontakt sorgt dafür, dass die Auswahl in der CellList gleich bleibt
		 * auch wenn das Objekt sich ändert. 
		 */	
		@Override
		public Object getKey(BusinessObject bo) {
			// Zurückgeben das unique Key von dem Objekt.
            return (bo == null) ? null : bo.getId();
		}
		
	}
	
	private BusinessObjectKeyProvider boKeyProvider = null;
	
	private SingleSelectionModel<BusinessObject> selectionModel = null;
	
	
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler{

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			BusinessObject selection = selectionModel.getSelectedObject();
			this.setSelectedContact((Contact)selection);
				
		}

		private void setSelectedContact(Contact c) {
			selectedContact = c;
			editor.showContact(c);
			
		}
		
	}
	
	
	public void addContact(Contact c) {
		contactsProvider.getList().add(c);
		selectionModel.setSelected(c, true);
	}
	
	
	public void removeContact(Contact c) {
		contactsProvider.getList().remove(c);
	}
	
	public void updateContact(Contact c) {
		List<Contact> contacts = contactsProvider.getList();
		int i = 0;
			for (Contact c2 : contacts) { 
				if (c2.getId() == c.getId()) {
					contacts.set(i, c);
					break;
				} else {
					i++;
				}
			}
			contactsProvider.refresh();
	}
	
	/*
	 * Funktioniert so noch nicht.
	 */
	private class UpdateContactCallback implements AsyncCallback<Contact> {
		 
		 Contact contact = null;
		 
		 UpdateContactCallback(Contact c) {
			 contact = c;
		 }

		@Override
		public void onFailure(Throwable caught) {
			//nix. 
		}

		@Override
		public void onSuccess(Contact c) {
			List<Contact> contacts = contactsProvider.getList();
			
			for (int i = 0; i<contacts.size(); i++) {
				if(contact.getId() == contacts.get(i).getId()) {
					contacts.set(i, contact);
					break;
				}
			}

			
		}
		 
	 }
	
	
	
	
	
	

}
