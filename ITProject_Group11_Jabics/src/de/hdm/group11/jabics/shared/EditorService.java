package de.hdm.group11.jabics.shared;

import java.util.Date;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

public interface EditorService {
	
	public JabicsUser createUser(String name, String email) throws IllegalArgumentException;
	
	public Contact createContact(ArrayList<PValue> cArray, JabicsUser u);
	
	public ContactList createContactList(String name, ArrayList<Contact> cArray, JabicsUser u);
	
	public PValue createPValue(Property p, String s, Contact c, JabicsUser u);
	
	public PValue createPValue(Property p, int i, Contact c, JabicsUser u);
	
	public PValue createPValue(Property p, Date dt, Contact c, JabicsUser u);
	
	public PValue createPValue(Property p, float i, Contact c, JabicsUser u);
	
	public Property createProperty(String label, Type type);
	
	public ArrayList<ContactList> getListsOf(JabicsUser u);
	
	public ArrayList<Contact> getContactsOf(JabicsUser u);
	
	public ArrayList<Contact> getAllSharedContactsOf(JabicsUser u);
	
	public JabicsUser getUserById(int id);
	
	public ContactList addContactToList(Contact c, ContactList cl);
	
	public Contact addValueToContact(PValue pv, Contact c, JabicsUser u);
	
	public ArrayList<Contact> searchForContactByExpression(String s, JabicsUser u);
	
	public ContactList removeContactFromList(Contact c, ContactList cl);
	
	public void deleteContact(Contact c, JabicsUser u);
	
	public void deleteContactList(ContactList cl, JabicsUser u);
	
	public void deleteProperty(Property p);
	
	public void deletePValue(PValue pv);
	
	public PValue updatePValue(PValue pv);
	
	public Contact updateContact(Contact c);
	
	public ContactList updateContactList(ContactList cl);
		
	public void addCollaboration(ContactList cl, JabicsUser u);
	
	public void addCollaboration(Contact c, JabicsUser u);
	
	public void addCollaboration(PValue pv, JabicsUser u);
	
	public void deleteCollaboration(PValue pv, JabicsUser u);
	
	public void deleteCollaboration(ContactList cl, JabicsUser u);
	
	public ArrayList<PValue> getPValueOf(Contact c , JabicsUser u);
	
	public ArrayList<Contact> getContactsOfList(ContactList cl, JabicsUser u);
	
	public ArrayList<Contact> searchExpressionInList(String s, ContactList cl);
	
	public ArrayList<Contact> searchInList(String s, ContactList cl);
	
	public ArrayList<Contact> searchInList(int i, ContactList cl);
	
	public ArrayList<Contact> searchInList(float f, ContactList cl );
	
	public ArrayList<Contact> searchInList(JabicsUser u, ContactList cl);

	public ArrayList<JabicsUser> getCollaborators(Contact c);
	
	public ArrayList<JabicsUser> getCollaborators(ContactList cl);
	
	public ArrayList<JabicsUser> getCollaborators(PValue pv);
	
	public ArrayList<JabicsUser> getAllUsers();
	
	public JabicsUser setJabicsUser(JabicsUser u) throws IllegalArgumentException;
	
	public void initialise();

}


