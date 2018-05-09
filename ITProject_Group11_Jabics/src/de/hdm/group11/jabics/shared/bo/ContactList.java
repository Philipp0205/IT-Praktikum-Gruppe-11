/**
 * 
 */
package de.hdm.group11.jabics.shared.bo;
import java.util.ArrayList;

/**
 * @author Anders
 *
 */
public class ContactList extends BusinessObject {
	
	
			private String listName; 
			//private BoStatus shareStatus; 
			ArrayList<Contact> contacts = new ArrayList<Contact>();  
			BoStatus shareStatus;
			
			public String toString() {
				
				/** 
				 * TODO 
				 */
				return listName; 
				
			}

			public void addContact(Contact c) { 
				/**
				 *  TODO implementieren 
				 */
				
				contacts.add(c);
			}
			
			public void removeContact(Contact c) {
				/** 
				 *  TODO 
				 */
			}
			// Getter und Setter 
			
			public String getListName() {
				return listName;
			}
			public void setListName(String listName) {
				this.listName = listName;
			}
			public BoStatus getShareStatus() {
				return shareStatus;
			}
			public void setShareStatus(BoStatus shareStatus) {
				this.shareStatus = shareStatus;
			}
			
			
}
