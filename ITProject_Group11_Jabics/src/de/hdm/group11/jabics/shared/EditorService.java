package de.hdm.group11.jabics.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * Interface der Klasse <code>EditorServiceImpl</code>.
 * 
 * @author Kurrle
 * @author Anders
 */
@RemoteServiceRelativePath("editor")
public interface EditorService extends RemoteService {

	public void addCollaboration(Contact c, JabicsUser u);

	public JabicsUser addCollaboration(ContactList cl, JabicsUser u);

	public void addCollaboration(PValue pv, JabicsUser u);

	public Contact addContactToList(Contact c, ContactList cl);

	public Contact createContact(ArrayList<PValue> cArray, JabicsUser u);

	public ContactList createContactList(String name, ArrayList<Contact> cArray, JabicsUser u);

	public Property createProperty(String label, Type type);

	public PValue createPValue(Property p, Date dt, Contact c, JabicsUser u);

	public PValue createPValue(Property p, float i, Contact c, JabicsUser u);
	
	public PValue createPValue(Property p, int i, Contact c, JabicsUser u);
	
	public PValue createPValue(Property p, String s, Contact c, JabicsUser u);

	public JabicsUser deleteCollaboration(Contact c, JabicsUser u);

	public ContactList deleteCollaboration(ContactList cl, JabicsUser u);

	public void  deleteCollaboration(PValue pv, JabicsUser u);

	public void deleteContact(Contact c, JabicsUser u);
	
	public ContactList deleteContactList(ContactList cl, JabicsUser u);
	
	public void deleteProperty(Property p);

	public void deletePValue(PValue pv, Contact c);

	public void deleteUser(JabicsUser u);

	public ArrayList<JabicsUser> getAllNotCollaboratingUser(Contact c);

	public ArrayList<JabicsUser> getAllNotCollaboratingUser(ContactList cl);

	public ArrayList<Contact> getAllSharedContactsOf(JabicsUser u);

	public ArrayList<JabicsUser> getAllUsers();

	public ArrayList<JabicsUser> getCollaborators(Contact c);

	public ArrayList<JabicsUser> getCollaborators(ContactList cl);

	public ArrayList<JabicsUser> getCollaborators(PValue pv);

	public ArrayList<Contact> getContactsOf(JabicsUser u);

	public ArrayList<Contact> getContactsOfList(ContactList cl, JabicsUser u);

	public ArrayList<ContactList> getListsOf(JabicsUser u);

	public JabicsUser getOwnerOfContact(Contact c);
	
	public JabicsUser getOwnerOfContactList(ContactList cl);

	public ArrayList<Property> getPropertysOfJabicsUser(JabicsUser u) throws IllegalArgumentException;

	public ArrayList<PValue> getPValueOf(Contact c, JabicsUser u);

	public ArrayList<Property> getStandardProperties() throws IllegalArgumentException;

	public Contact getUpdatedContact(Contact c);

	public void initialise();

	public Contact removeContactFromList(Contact c, ContactList cl);

	public ArrayList<Contact> searchExpressionInList(String s, ContactList cl);

	public ArrayList<Contact> searchForContactByExpression(String s, JabicsUser u);

	public ArrayList<Contact> searchInList(ContactList cl, PValue pv, JabicsUser u);

	public ArrayList<Contact> searchInList(JabicsUser u, ContactList cl);

	public Contact updateContact(Contact c, JabicsUser u);

	public ContactList updateContactList(ContactList cl);

	public PValue updatePValue(PValue pv);

}