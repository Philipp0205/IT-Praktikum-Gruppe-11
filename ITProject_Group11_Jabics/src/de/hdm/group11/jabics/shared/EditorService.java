package de.hdm.group11.jabics.shared;

import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;
import de.hdm.group11.jabics.shared.bo.User;

public interface EditorService {
	
	public User createUser(String name) throws IllegalArgumentException;
	
	public Contact createContact(ArrayList<PValue> cArray );
	
	public ContactList createContactList(ArrayList<Contact> cArray);
	
	public PValue createPValue(Property p, String s, Contact c);
	
	public PValue createPValue(Property p, int i, Contact c);
	
	public Property createProperty(String label);
	
	public Property createProperty(String label, Type type);
	
	public ArrayList<ContactList> getListsOf(User u);
	
	public ArrayList<Contact> getContactsOf(User u);
	
	public User getUserById(int id);
	
	public ContactList addContactToList(Contact c, ContactList cl);
	
	public Contact addValueToContact(PValue pv, Contact c);
	
	public ArrayList<Contact> searchInLists(String s);
	
	public ArrayList<Contact> searchForContactByExpression(String s, User u);
	
	public ContactList removeContactFromList(Contact c, ContactList cl);
	
	public Contact deleteAllPValueFromContact(Contact c);
	
	public void deleteContact(Contact c);
	
	public void deleteContactList(ContactList cl);
	
	public void deleteProperty(Property p);
	
	public void deletePValue(PValue pv);
	
	public void updatePValue(PValue pv);
	
	public void updateContact(Contact c);
	
	public void updateContactList(ContactList cl);
		
	public void addCollaboration(ContactList cl, User u);
	
	public void addCollaboration(Contact c, User u);
	
	public void addCollaboration(PValue pv, User u);
	
	public void deleteCollaboration(PValue pv, User u);
	
	public void deleteCollaboration(ContactList cl, User u);
	
	public ArrayList<PValue> getPValueOf(Contact c , User u);
	
	public ArrayList<Contact> searchInList(String s, ContactList cl);
	
	public ArrayList<Contact> searchInList(int i, ContactList cl);
	
	public ArrayList<Contact> searchInList(float f, ContactList cl );
	
	public ArrayList<Contact> searchInList(User u, ContactList cl);

	public ArrayList<User> getCollaborators(Contact c);
	
	public ArrayList<User> getCollaborators(ContactList cl);
	
	public ArrayList<User> getCollaborators(PValue pv);
	
	public ArrayList<User> getAllUsers(User u);
	
	public void initialise();

}


