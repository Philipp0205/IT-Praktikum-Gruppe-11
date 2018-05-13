/**
 * 
 */
package de.hdm.group11.jabics.server;

import java.util.ArrayList;

import de.hdm.group11.jabics.server.db.ContactListMapper;
import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.server.db.PValueMapper;
import de.hdm.group11.jabics.server.db.PropertyMapper;
import de.hdm.group11.jabics.shared.EditorService;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.User;
import de.hdm.group11.jabics.shared.bo.Type;
import de.hdm.group11.jabics.server.db.*;

/**
 * @author Jan
 *
 */
public class EditorServiceImpl implements EditorService {
	
	/**
	 * TODO: implement all methods
	 */
	
	ContactMapper cMapper = new ContactMapper();
	ContactListMapper clMapper = new ContactListMapper();
	PValueMapper pvMapper = new PValueMapper();
	PropertyMapper pMapper = new PropertyMapper();
	// UserMapperImpl uMapper = new UserMapperImpl();
	
	
	public EditorServiceImpl() {
		
	}
	
	public User createUser(String name) { 
		User newUser = new User();
		newUser.setUsername(name);
		
		return newUser;
	}
	
	public Contact createContact(ArrayList<PValue> cArray ) { 
		Contact newContact = new Contact();
		newContact.setValues(cArray);
		
		return newContact;
	}
	
	public ContactList createContactList(ArrayList<Contact> cArray) { 
		ContactList newContactList = new ContactList();
		newContactList.setContacts(cArray);
		
		return newContactList;
	}
	
	public PValue createPValue(Property p, String s, Contact c) {
		PValue newPValue = new PValue(p, s);
		c.addPValue(newPValue);
		
		return newPValue; 
		
	}
	
	public PValue createPValue(Property p, int i, Contact c) {
		PValue newPValue = new PValue(p, i);
		c.addPValue(newPValue);
		
		return newPValue; 
		
	}
	
	// TODO DOKU: Statt "name" label und die überladene Methode
	public Property createProperty(String label) {
		Property newProperty = new Property();
		newProperty.setLabel(label);
		
		return newProperty;
	}
	
	public Property createProperty(String label, Type type) {
		Property newProperty = new Property();
		newProperty.setLabel(label);
		
		return newProperty;
	}
	
	// TODO DOKU: Rename zu getListsOfUser() sinnvoll.
	public ArrayList<ContactList> getListsOf(User u) {
		ContactListMapper clMapper = new ContactListMapper();
		//clMapper.findAllContactList(u);
		
		return null;
		
	}
	
	public ArrayList<Contact> getContactsOf(User u) { 
		
		ContactMapper cMapper = new ContactMapper();
		
		return cMapper.findAllContact(u);
		
	}
	
	public User getUserById(int id) {
		
		UserMapper uMapper = new UserMapper();
		
		return uMapper.findUserById(id);
	}
	
	public ContactList addContactToList(Contact c, ContactList cl) {
		cl.addContact(c);
		
		return cl;
	}
	
	// TODO DOKU: PValue statt PropertyValue
	public Contact addValueToContact(PValue pv, Contact c) {
		c.addPValue(pv);
		
		return c;
	}
	
	// TODO
	public ArrayList<Contact> searchInLists(String s) {
		
		// ContactListMapper cMapper = new ContactListMapper();
		return null;
		
	}
	
	//TODO
	public ArrayList<Contact> searchForContactByExpression(String s) {
		
		
		return null;
		
	}

	public ContactList removeContactFromList(Contact c, ContactList cl) {
		cl.removeContact(c);
		
		return cl;
	}
	
	public void deleteContact(Contact c) {
		ContactMapper cMapper = new ContactMapper();
		cMapper.deleteContact(c);
		
	}
	
	public void deleteContactList(ContactList cl) {
		ContactListMapper clMapper = new ContactListMapper();
		clMapper.deleteContactList(cl);
	}
	
	public void deleteProperty(Property p) {
		//PropertyMapper pMapper = PropertyMapper();
		//pMapper.delete(p);
	}
	
	
	
	
	
	
}
