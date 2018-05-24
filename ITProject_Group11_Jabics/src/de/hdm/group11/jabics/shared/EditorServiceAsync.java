package de.hdm.group11.jabics.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.shared.bo.*;


public interface EditorServiceAsync {

	void createUser(String name, AsyncCallback<User> callback) throws IllegalArgumentException;
	
	void createContact(ArrayList<PValue> cArray, AsyncCallback<Contact> callback);
	
	void createContactList(ArrayList<Contact> cArray, AsyncCallback<ContactList> callback);
	
	void createPValue(Property p, String s, Contact c, AsyncCallback<PValue> callback);
	
	void createPValue(Property p, int i, Contact c, AsyncCallback<PValue> callback);
	
	void createProperty(String label, AsyncCallback<Property> callback);
	
	void createProperty(String label, Type type, AsyncCallback<Property> callback);
	
	void getListsOf(User u, AsyncCallback<ArrayList<ContactList>> callback);
	
	void getContactsOf(User u, AsyncCallback<ArrayList<Contact>> callback);
	
	void getUserById(int id, AsyncCallback<User> callback);
	
	void addContactToList(Contact c, ContactList cl, AsyncCallback<ContactList> callback);
	
	void addValueToContact(PValue pv, Contact c, AsyncCallback<Contact> callback);
	
	void searchInLists(String s, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchForContactByExpression(String s, User u, AsyncCallback<ArrayList<Contact>> callback);
	
	void removeContactFromList(Contact c, ContactList cl, AsyncCallback<ContactList> callback);
	
	void deleteAllPValueFromContact(Contact c, AsyncCallback<Contact> callback);
	
	void deleteContact(Contact c, AsyncCallback<Void> callback);
	
	void deleteContactList(ContactList cl, AsyncCallback<Void> callback);
	
	void deleteProperty(Property p, AsyncCallback<Void> callback);
	
	void deletePValue(PValue pv, AsyncCallback<Void> callback);
	
	void updatePValue(PValue pv, AsyncCallback<Void> callback);
	
	void updateContact(Contact c, AsyncCallback<Void> callback);
	
	void updateContactList(ContactList cl, AsyncCallback<Void> callback);
		
	void addCollaboration(ContactList cl, User u, AsyncCallback<Void> callback);
	
	void addCollaboration(Contact c, User u, AsyncCallback<Void> callback);
	
	void addCollaboration(PValue pv, User u, AsyncCallback<Void> callback);
	
	void deleteCollaboration(PValue pv, User u, AsyncCallback<Void> callback);
	
	void deleteCollaboration(ContactList cl, User u, AsyncCallback<Void> callback);
	
	void getPValueOf(Contact c , User u, AsyncCallback<ArrayList<PValue>> callback);
	
	void searchInList(String s, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchInList(int i, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchInList(float f, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);
	
	void searchInList(User u, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);

	void getCollaborators(Contact c, AsyncCallback<ArrayList<User>> callback);
	
	void getCollaborators(ContactList cl, AsyncCallback<ArrayList<User>> callback);
	
	void getCollaborators(PValue pv, AsyncCallback<ArrayList<User>> callback);
	
	void getAllUsers(User u, AsyncCallback<ArrayList<User>> callback);
	
	void initialise(AsyncCallback<Void> callback);
}
