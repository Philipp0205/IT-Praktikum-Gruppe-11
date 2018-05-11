package de.hdm.group11.jabics.shared.bo;

import java.util.ArrayList;
import java.util.Date;

/**
 * This Class realises the Object Oriented Version of Contact Lists in Jabics. It can store an undefined amount of Contact Objects.
 * Contacts can be added using the addContacts or addContact method or removed using removeContact. ContactLists have a list name.
 * @author Anders
 *
 */
public class ContactList extends BusinessObject {

			private String listName; 
			private BoStatus shareStatus; 
			
			ArrayList<Contact> contacts = new ArrayList<Contact>();  
			
			/**
			 * toString returns the list name
			 */
			@Override 
			public String toString() {
				if(listName != null) {
					return listName; 
				} else return Integer.toString(this.id);
				
				
			}

			/** 
			 * This method adds one Contact to the ContactList
			 */
			public void addContact(Contact c) {				
				contacts.add(c);
			}
			
			/** 
			 * This method adds all Contacts in @param conts to the Contact Array in an Object
			 */
			
			public void addContacts(ArrayList<Contact> conts) {
				this.contacts.addAll(conts);
			}
			
			/**
			 * Removes a specified contact from the List
			 */
			public void removeContact(Contact c) {
				this.contacts.remove(c);
			}
			
			/**
			 * Getters and Setters. Setting DateUpdated to current time whenever substantial information in the Object is changed.
			 */
			public ArrayList<Contact> getContacts() {
				return contacts;
			}
			public void setContacts(ArrayList<Contact> contacts) {
				this.contacts = contacts;
				setDateUpdated(new Date());
			}
			public String getListName() {
				return listName;
			}
			public void setListName(String listName) {
				this.listName = listName;
				setDateUpdated(new Date());
			}
			public BoStatus getShareStatus() {
				return shareStatus;
			}
			public void setShareStatus(BoStatus shareStatus) {
				this.shareStatus = shareStatus;
				setDateUpdated(new Date());
			}
			
			
}
