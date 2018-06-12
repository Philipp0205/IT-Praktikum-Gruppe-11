package de.hdm.group11.jabics.shared.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Diese Klasse implementiert Kontaktlisten in Jabics. Kontaktlisten haben einen Namen und ein Feld aus Kontakten.
 * Diese können eine nicht definierte Menge an <code>Contact</code> Objekten speichern.
 * Kontakte können einer Liste mittels addContact() und removeContact() hinzugefügt oder entfernt werden.
 * @author Anders
 *
 */
public class ContactList extends BusinessObject {
	
		private String listName; 
	    private BoStatus shareStatus; 
	    
	    ArrayList<Contact> contacts = new ArrayList<Contact>();  
	    
	    /**
	     * Konstruktoren
	     */
	    public ContactList() {
	      super();
	    }
	    public ContactList(ArrayList<Contact> al) {
	        this();
	    	this.contacts = al;
	    }
			
		public ContactList(ArrayList<Contact> al, String ln) {
			this(al);
			this.listName = ln;
		}
			
			
			/**
			 * toString gibt den Listennamen zurück
			 */
			@Override 
			public String toString() {
				if(listName != null) {
					return listName; 
				} else return Integer.toString(this.id);
				
				
			}

			/** 
			 * Fügt einen Kontakt zur Liste hinzu
			 */
			public void addContact(Contact c) {				
				contacts.add(c);
			}
			
			/** 
			 * Fügt alle Kontakte in einer ArrayList<Contact> zur Liste hinzu
			 */
			
			public void addContacts(ArrayList<Contact> conts) {
				this.contacts.addAll(conts);
			}
			
			/**
			 * Entfernen eines Kontakts aus der Liste
			 */
			public void removeContact(Contact c) {
				this.contacts.remove(c);
			}
			
			/**
			 * Getters und Setter. DateUpdated wird wann immer sinvoll auf "jetzt" gesetzt.
			 */
			public ArrayList<Contact> getContacts() {
				return contacts;
			}
			public void setContacts(ArrayList<Contact> contacts) {
				this.contacts = contacts;
				this.setDateUpdated(LocalDateTime.now());
			}
			public String getListName() {
				return listName;
			}
			public void setListName(String listName) {
				this.listName = listName;
				this.dateUpdated = LocalDateTime.now();
			}
			public BoStatus getShareStatus() {
				return shareStatus;
			}
			public void setShareStatus(BoStatus shareStatus) {
				this.shareStatus = shareStatus;
				this.dateUpdated = LocalDateTime.now();
			}
			
			
}
