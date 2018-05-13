/**
 * @author Ander
 * @author Kurrle
 */

package de.hdm.group11.jabics.server;

import java.util.ArrayList;

import de.hdm.group11.jabics.server.db.*;
import de.hdm.group11.jabics.shared.bo.*;
/**
 * TODO: write interface EditorService
 */
import de.hdm.group11.jabics.shared.EditorService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EditorServiceImpl extends RemoteServiceServlet implements EditorService{
	
	/**
	 * TODO: implement all methods
	 */
	
	ContactMapper cMapper = ContactMapper.contactMapper();
	ContactListMapper clMapper = ContactListMapper.contactListMapper();
	PValueMapper pvMapper = PValueMapper.pValueMapper();
	PropertyMapper pMapper = PropertyMapper.propertyMapper();
	UserMapper uMapper = UserMapper.userMapper();
	
	
	public EditorServiceImpl() {
		
	}
	
	public User createUser(String name) { 
		User newUser = new User(name);
		return uMapper.createUser(newUser);
	}
	
	public Contact createContact(ArrayList<PValue> cArray) { 
		Contact newContact = new Contact(cArray);
		return cMapper.insertContact(newContact);
	}
	
	public ContactList createContactList(ArrayList<Contact> cArray) { 
		ContactList newContactList = new ContactList(cArray);
		return clMapper.insertContactList(newContactList);
	}
	
	//Are the create PValue Methods really needed? can't this be done on clientside and we just insert these values into the database?
	public PValue createPValue(Property p, String s, Contact c) {
		PValue newPValue = new PValue(p, s);
		c.addPValue(newPValue);
		//to be determined: does insert pv also connect pv with contact? if yes, next method is unnecessary
		pvMapper.insertPValue(pv, c);
		// potentially add user object into method parameters
		cMapper.updateContact(c, u);
		return newPValue; 
		
	}
	
	/**
	 * TODO: determine if comments in method above also apply here
	 * @param p
	 * @param i
	 * @param c
	 * @return
	 */
	public PValue createPValue(Property p, int i, Contact c) {
		PValue newPValue = new PValue(p, i);
		c.addPValue(newPValue);
		
		return newPValue; 
		
	}
	
	// TODO DOKU: Statt "name" label und die überladene Methode
	public Property createProperty(String label) {
		Property newProperty = new Property(label);
		newProperty.setLabel(label);
		
		return newProperty;
	}
	
	public Property createProperty(String label, Type type) {
		Property newProperty = new Property();
		newProperty.setLabel(label);
		
		return newProperty;
	}
	
	// TODO
	public ArrayList<ContactList> getListsOf(User u) {
		return null;
		
	}
	
	// TODO
	public ArrayList<Contact> getContactsOf(User u) { 
		return null; 
	}
	
	// is this method really needed?
	public User getUSerById(int id) {
		return uMapper.findUserById(id);
	}
	
	/**
	 * This Method inserts a specified <code>Contact</code> into a list
	 * @param c Contact
	 * @param cl ContactList
	 * @return updated contact list
	 */
	public ContactList addContactToList(Contact c, ContactList cl) {
		cl.addContact(c);
		return clMapper.updateContactList(cl);
		
	}
	
	public Contact addValueToContact(PValue pv, Contact c, User u) {
		c.addPValue(pv);
		return cMapper.updateContact(c, u);
	}
	
	/**
	 * Searches a <code>ContactList</code> for all <code>Contact</code>s that contain the passed on String
	 * @param cl ContactList that shall be searched
	 * @param s Expression that shall be searched for
	 * @return ArrayList with all Contacts that fit the criteria
	 */
	public ArrayList<Contact> searchInList(ContactList cl, String s){
		ArrayList<Contact> result = new ArrayList<Contact>();
		for(Contact c : cl.getContacts()) {
			if(c.getName().contains(s)) result.add(c);
			for (PValue pv : c.getValues()) {
				/**
				 * TODO: add more fields that are searched through
				 */
				if (pv.getProperty().toString().contains(s) || pv.getStringValue().contains(s)) {
					result.add(c);
				}
			}
		}
		return result;
	}
	
	/**
	 * Searches for all <code>Contacts</code> visible to a User which contain specified String
	 * @param s
	 * @param u
	 * @return ArrayList with all found Contacts
	 */
	public ArrayList<Contact> searchForContactByExpression(String s, User u){
		//creating new ContactList for all contacts of given user to be able to call already implemented method
		ContactList cl = new ContactList(cMapper.findAllContact(u));
		return this.searchInList(cl, s);
		
	}
	/**
	 * Removes a <code>Contact</code> from a <code>ContactList</code>
	 * @param c
	 * @param cl
	 * @return updated ContactList without specified Contact
	 */
	public ContactList removeContactFromList(Contact c, ContactList cl) {
		cl.removeContact(c);
		return clMapper.updateContactList(cl);
	}
	
	/**
	 * Deletes a contact from the Database. The Contact is deleted for all Users. This deletes it permanently and cannot be undone.
	 * @param c Contact that shall be deleted
	 */
	public void deleteContact(Contact c){
		cMapper.deleteContact(c);
	}
	/**
	 * Deletes a <code>ContactList</code> from the Database. Is deleted for all Users. This deletes it permanently and cannot be undone.
	 * @param cl ContactList that shall be deleted
	 */
	public void deleteContactList(ContactList cl){
		clMapper.deleteContactList(cl);
	}
	/**
	 * Deletes a property from the database. The property is deleted for all users. This deletes it permanently and cannot be undone.
	 * @param c Contact that shall be deleted
	 */
	public void deleteProperty(Property p){
		if(!p.isStandard()) {
			pMapper.deleteProperty(p);
		}
		else System.out.println("Tried to delete standard property. This should not have been possible");
	}
	
	public void deletePValue(PValue pv){
		if(pv.getProperty().getLabel() != "name") {
			pvMapper.deletePValue(pv);
		}
	}
	
	
	
	
	
	
}
