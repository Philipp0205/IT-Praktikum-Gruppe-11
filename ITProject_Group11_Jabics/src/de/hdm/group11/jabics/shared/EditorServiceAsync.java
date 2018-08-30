package de.hdm.group11.jabics.shared;

import java.util.Date;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * Das asynchrone Gegenstück des Interface {@link EditorService}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link EditorService}.
 * 
 * @author Thies
 * @author Anders
 * @author Kurrle
 */
public interface EditorServiceAsync {

	void addCollaboration(Contact c, JabicsUser u, AsyncCallback<Void> callback);

	void addCollaboration(ContactList cl, JabicsUser u, AsyncCallback<JabicsUser> callback);

	void addCollaboration(PValue pv, JabicsUser u, AsyncCallback<Void> callback);

	void addContactToList(Contact c, ContactList cl, AsyncCallback<Contact> callback);

	void createContact(ArrayList<PValue> cArray, JabicsUser u, AsyncCallback<Contact> callback);

	void createContactList(String name, ArrayList<Contact> cArray, JabicsUser u, AsyncCallback<ContactList> callback);

	void createProperty(String label, Type type, AsyncCallback<Property> callback);

	void createPValue(Property p, Date dt, Contact c, JabicsUser u, AsyncCallback<PValue> callback);

	void createPValue(Property p, float f, Contact c, JabicsUser u, AsyncCallback<PValue> callback);

	void createPValue(Property p, int i, Contact c, JabicsUser u, AsyncCallback<PValue> callback);
	
	void createPValue(Property p, String s, Contact c, JabicsUser u, AsyncCallback<PValue> callback);

	void deleteCollaboration(Contact c, JabicsUser u, AsyncCallback<JabicsUser> callback);

	void deleteCollaboration(ContactList cl, JabicsUser u, AsyncCallback<ContactList> callback);

	void deleteCollaboration(PValue pv, JabicsUser u, AsyncCallback<Void> callback);

	void deleteContact(Contact c, JabicsUser u, AsyncCallback<Void> callback);

	void deleteContactList(ContactList cl, JabicsUser u, AsyncCallback<ContactList> callback);

	void deleteProperty(Property p, AsyncCallback<Void> callback);
	
	void deletePValue(PValue pv, Contact c, AsyncCallback<Void> callback);

	void deleteUser(JabicsUser u, AsyncCallback<Void> callback);

	void getAllNotCollaboratingUser(Contact c, AsyncCallback<ArrayList<JabicsUser>> callback);

	void getAllNotCollaboratingUser(ContactList cl, AsyncCallback<ArrayList<JabicsUser>> callback);

	void getAllSharedContactsOf(JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);

	void getAllUsers(AsyncCallback<ArrayList<JabicsUser>> callback);

	void getCollaborators(Contact c, AsyncCallback<ArrayList<JabicsUser>> callback);

	void getCollaborators(ContactList cl, AsyncCallback<ArrayList<JabicsUser>> callback);

	void getCollaborators(PValue pv, AsyncCallback<ArrayList<JabicsUser>> callback);

	void getContactsOf(JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);

	void getContactsOfList(ContactList cl, JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);

	void getListsOf(JabicsUser u, AsyncCallback<ArrayList<ContactList>> callback);

	void getOwnerOfContact(Contact c, AsyncCallback<JabicsUser> callback);
	
	void getOwnerOfContactList(ContactList cl, AsyncCallback<JabicsUser> callback);

	void getPropertysOfJabicsUser(JabicsUser u, AsyncCallback<ArrayList<Property>> callback);

	void getPValueOf(Contact c, JabicsUser u, AsyncCallback<ArrayList<PValue>> callback);

	void getStandardProperties(AsyncCallback<ArrayList<Property>> callback) throws IllegalArgumentException;

	void getUpdatedContact(Contact c, AsyncCallback<Contact> callback);

	void initialise(AsyncCallback<Void> callback);

	void removeContactFromList(Contact c, ContactList cl, AsyncCallback<Contact> callback);

	void searchExpressionInList(String s, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);

	void searchForContactByExpression(String s, JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);

	void searchInList(ContactList cl, PValue pv, JabicsUser u, AsyncCallback<ArrayList<Contact>> callback);

	void searchInList(JabicsUser u, ContactList cl, AsyncCallback<ArrayList<Contact>> callback);

	void updateContact(Contact c, JabicsUser u, AsyncCallback<Contact> callback);

	void updateContactList(ContactList cl, AsyncCallback<ContactList> callback);

	void updatePValue(PValue pv, AsyncCallback<PValue> callback);

}
