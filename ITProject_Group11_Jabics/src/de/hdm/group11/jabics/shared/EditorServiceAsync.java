package de.hdm.group11.jabics.shared;

import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.shared.bo.*;


public interface EditorServiceAsync {

	void createUser(String name, String email, AsyncCallback<JabicsUser> callback) throws IllegalArgumentException;
	
	void createContact(ArrayList<PValue> cArray, JabicsUser u, AsyncCallback<Contact> callback);
	
	void createContactList(String name, ArrayList<Contact> cArray, JabicsUser u, AsyncCallback<ContactList> callback);
	
	void createPValue(Property p, String s, Contact c, JabicsUser u, AsyncCallback<PValue> callback);
	
	void createPValue(Property p, int i, Contact c, JabicsUser u, AsyncCallback<PValue> callback);

	void createPValue(Property p, LocalDate dt, Contact c, JabicsUser u, AsyncCallback<PValue> callback);
	
	void createPValue(Property p, float f, Contact c, JabicsUser u, AsyncCallback<PValue> callback);
	
	void createProperty(String label, Type type, AsyncCallback<Property> callback);
	
	void getListsOf(JabicsUser u, AsyncCallback<ArrayList<ContactList>> callback);
	
	void getContactsOfList(ContactList cl, JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);
	
	void getContactsOf(JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);
	
	void getAllSharedContactsOf(JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);
	
	void getUserById(int id, AsyncCallback<JabicsUser> callback);
	
	void addContactToList(Contact c, ContactList cl, AsyncCallback<ContactList> callback);
	
	void addValueToContact(PValue pv, Contact c, AsyncCallback<Contact> callback);
	
	void searchForContactByExpression(String s, JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);
	
	void removeContactFromList(Contact c, ContactList cl, AsyncCallback<ContactList> callback);
	
	void deleteContact(Contact c, JabicsUser u, AsyncCallback<Void> callback);
	
	void deleteContactList(ContactList cl, JabicsUser u, AsyncCallback<Void> callback);
	
	void deleteProperty(Property p, AsyncCallback<Void> callback);
	
	void deletePValue(PValue pv, AsyncCallback<Void> callback);
	
	void updatePValue(PValue pv, AsyncCallback<PValue> callback);
	
	void updateContact(Contact c, AsyncCallback<Contact> callback);
	
	void updateContactList(ContactList cl, AsyncCallback<ContactList> callback);
		
	void addCollaboration(ContactList cl, JabicsUser u, AsyncCallback<Void> callback);
	
	void addCollaboration(Contact c, JabicsUser u, AsyncCallback<Void> callback);
	
	void addCollaboration(PValue pv, JabicsUser u, AsyncCallback<Void> callback);
	
	void deleteCollaboration(PValue pv, JabicsUser u, AsyncCallback<Void> callback);
	
	void deleteCollaboration(ContactList cl, JabicsUser u, AsyncCallback<Void> callback);
	
	void getPValueOf(Contact c , JabicsUser u, AsyncCallback<ArrayList<PValue>> callback);
	
	void searchExpressionInList(String s, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchInList(String s, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchInList(int i, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchInList(float f, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchInList(JabicsUser u, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);

	void getCollaborators(Contact c, AsyncCallback<ArrayList<JabicsUser>> callback);
	
	void getCollaborators(ContactList cl, AsyncCallback<ArrayList<JabicsUser>> callback);
	
	void getCollaborators(PValue pv, AsyncCallback<ArrayList<JabicsUser>> callback);
	
	void getAllUsers(AsyncCallback<ArrayList<JabicsUser>> callback);
	
	void initialise(AsyncCallback<Void> callback);
}
