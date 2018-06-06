package de.hdm.group11.jabics.shared;

import java.time.LocalDateTime;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

public interface EditorService {
	
	public User createUser(String name) throws IllegalArgumentException;
	
	public Contact createContact(ArrayList<PValue> cArray, User u);
	
	public ContactList createContactList(ArrayList<Contact> cArray, User u);
	
	public PValue createPValue(Property p, String s, Contact c, User u);
	
	public PValue createPValue(Property p, int i, Contact c, User u);
	
	public PValue createPValue(Property p, LocalDateTime dt, Contact c, User u);
	
	public PValue createPValue(Property p, float i, Contact c, User u);
	
	public Property createProperty(String label, Type type);
	
	public ArrayList<ContactList> getListsOf(User u);
	
	public ArrayList<Contact> getContactsOf(User u);
	
	public User getUserById(int id);
	
	public ContactList addContactToList(Contact c, ContactList cl);
	
	public Contact addValueToContact(PValue pv, Contact c, User u);
	
	public ArrayList<Contact> searchForContactByExpression(String s, User u);
	
	public ContactList removeContactFromList(Contact c, ContactList cl);
	
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
	
	public ArrayList<Contact> searchExpressionInList(String s, ContactList cl);
	
	public ArrayList<Contact> searchInList(String s, ContactList cl);
	
	public ArrayList<Contact> searchInList(int i, ContactList cl);
	
	public ArrayList<Contact> searchInList(float f, ContactList cl );
	
	public ArrayList<Contact> searchInList(User u, ContactList cl);

	public ArrayList<User> getCollaborators(Contact c);
	
	public ArrayList<User> getCollaborators(ContactList cl);
	
	public ArrayList<User> getCollaborators(PValue pv);
	
	public ArrayList<User> getAllUsers();
	
	public void initialise();

}


