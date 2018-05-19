/**
 * Die Klasse EditorServiceImpl impelemtiert die Applikationslogik für den Editor von Jabics.
 * Sie stellt die Lodik zur verfügung, die bei einem RPC aufgerufen wird und gibt die angefragten Objekte zurück.
 * @author Anders
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
		return uMapper.insertUser(newUser);
	}
	
	public Contact createContact(ArrayList<PValue> cArray, User u) { 
		Contact newContact = new Contact(cArray);
		cMapper.insertContact(newContact);
		cMapper.insertCollaboration(u, newContact, true);
		return newContact;
	}
	
	public ContactList createContactList(ArrayList<Contact> cArray, User u) { 
		ContactList newContactList = new ContactList(cArray);
		clMapper.insertContactList(newContactList);
		clMapper.insertCollaboration(u, newContactList, true);
		return newContactList;
		
		
	}
	
	//Are the create PValue Methods really needed? can't this be done on clientside and we just insert these values into the database?
	public PValue createPValue(Property p, String s, Contact c, User u) {
		PValue newPValue = new PValue(p, s);
		c.addPValue(newPValue);
		//to be determined: does insert pv also connect pv with contact? if yes, next method is unnecessary
		pvMapper.insertPValue(newPValue, c);
		pvMapper.insertCollaboration(u, newPValue, true);
		// potentially add user object into method parameters
		cMapper.updateContact(c, u);
		return newPValue; 
		
	}
	
	/**
	 * Erstellt ein PValue mit einem int Wert und fügt diesen mitsamt collaboration in die DB ein.
	 */
	public PValue createPValue(Property p, int i, Contact c, User u) {
		PValue newPValue = new PValue(p, i);
		c.addPValue(newPValue);
		pvMapper.insertPValue(newPValue, c);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(c, u);
		return newPValue; 
		
	}
	
	public Property createProperty(String label, Type type) {
		Property newProperty = new Property(label, type);
		return pMapper.insertProperty(newProperty);
	}
	
	// TODO
	public ArrayList<ContactList> getListsOf(User u) {
		return clMapper.findAllContactList(u);
	}
	
	// Gibt alle Contact - Objekte, die ein Nutzer sehen darf, zurück.
	public ArrayList<Contact> getContactsOf(User u) { 
		ArrayList<Contact> cons = cMapper.findAllContact(u);
		//sind die Kontakte die der mapper zurückgibt auf den Nutzer "zugeschnitten?" also enthalten nur pvalues die der nutzer sehen darf
		/*for (Contact c : cons) {
			pvMapper.
		}*/
		return cons;
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
	 * Suche nach allen <code>Contacts</code> in einer <code>ContactList</code>, die den mitgegebenen String als Property oder PropertyValue enthalten.
	 * @return Eine ArrayList mit allen Contacts, die dem Suchkriterium entsprechen
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
	 * Suche nach allen <code>Contacts</code> eines <code>Users</code>, die den mitgegebenen String als Property oder PropertyValue enthalten.
	 * @return Eine ArrayList mit allen Contacts, die dem Suchkriterium entsprechen
	 */
	public ArrayList<Contact> searchForContactByExpression(String s, User u){
		//creating new ContactList for all contacts of given user to be able to call already implemented method
		ContactList cl = new ContactList(cMapper.findAllContact(u));
		return this.searchInList(cl, s);
		
	}
	/**
	 * Entfernt einen <code>Contact</code> aus einer <code>ContactList</code>
	 * @return Die ContactList ohne den zu entfernenden Contact
	 */
	public ContactList removeContactFromList(Contact c, ContactList cl) {
		cl.removeContact(c);
		clMapper.removeContactFromList(cl, c);
		return cl;
	}
	
	/**
	 * Löscht einen <code>Contact</code> aus der Datenbank. Löscht den Contact für alle Nutzer permanent. Kann nicht rückgängig gemacht werden.
	 * @param Contact, der gelöscht werden soll
	 */
	public void deleteContact(Contact c){
		for (PValue pv : c.getValues()) {
			pvMapper.deletePValue(pv);
		}
		cMapper.deleteContact(c);
	}
	
	/**
	 * Eine <code>ContactList</code> aus der DB löschen. Löscht die Liste für alle Nutzer permanent. Kann nicht rückgängig gemacht werden.
	 * @param cl ContactList, die gelöscht werden soll
	 */
	public void deleteContactList(ContactList cl){
		ArrayList<User> users = clMapper.findCollaborators(cl);
		for (User u : users) {
			clMapper.deleteCollaboration(cl, u);
		}
		for (Contact c: cl.getContacts()) {
			clMapper.deleteContactFromList(cl, c);
		}
		clMapper.deleteContactList(cl);
	}
	/**
	 * Eine Property aus der Datenbank löschen. Es wird überprüft, ob die Eigenschaft gelöscht werden darf.
	 * @param Property, die gelöscht werden soll
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
	
	/**
	 * Ein <code>PValue</code> aktualisieren, sodass es in der Datenbank konsitent gespeichert wird.
	 * @param Ein PropertyValue, das aktualisiert werden soll
	 */
	public void updatePValue(PValue pv) {
		/**
		 * TODO: implement method findPValueById in PValue Mapper
		 */
		PValue pvtemp = pvMapper.findPValueById();
		if(pv != pvtemp) {
			 pvMapper.updatePValue(pv);
		}
	}
	
	public void updateContactList(ContactList cl){
		ContactList cltemp = clMapper.findContactListById();
		if(cl != cltemp) {
			clMapper.updateContactList(cl);
		}
			/**
			 * TODO Nachdenken, ob wir nur änderungen überprüfen und nur diese an die db weitergeben oder das ganze ding in die DB geben
			 */
	}
	
	/**
	 * Eine Freigabe zwischen einem Nutzer und einer Kontaktliste einfügen.Diese Methode nicht! beim erstellen eines Objekts aufrufen, da isOwner false gesetzt wird.
	 * @param ContactList, um die es sich handelt
	 * @param Nutzer, dem die Liste freigegeben werden soll
	 */
	public void addCollaboration(ContactList cl, User u) {
		ArrayList<User> users = clMapper.findCollaborators(cl);
		if (!users.contains(u)) {
			clMapper.insertCollaboration(u, cl, false);
		} else return;
	}
	
	/**
	 * Eine Freigabe zwischen einem Nutzer und einer ContactList einfügen. Diese Methode nicht! beim erstellen eines Objekts aufrufen, da isOwner false gesetzt wird.
	 * @param ContactList, für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer, dem der Contact freigegeben werden soll
	 */
	public void addCollaboration(Contact c, User u) {
		ArrayList<User> users = cMapper.findCollaborators(c);
		if (!users.contains(u)) {
			cMapper.insertCollaboration(u, c, false);
		} else return;
	}
	
	/**
	 * Eine Freigabe zwischen einem Nutzer und einem PValue einfügen. Diese Methode nicht! beim erstellen eines Objekts aufrufen, da isOwner false gesetzt wird.
	 * @param PValue, für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer, dem das PValue freigegeben werden soll
	 */
	public void addCollaboration(PValue pv, User u) {
		ArrayList<User> users = pvMapper.findCollaborators(pv);
		if (!users.contains(u)) {
			pvMapper.insertCollaboration(u, pv, false);
		} else return;
	}
	
	public void deleteCollaboration(Contact c, User u) {
		cMapper.deleteCollaboration(c, u);
	}
	
	public void deleteCollaboration(ContactList cl, User u) {
		clMapper.deleteCollaboration(cl, u);
	}
	
	public void deleteCollaboration(PValue pv, User u) {
		pvMapper.deleteCollaboration(pv, u);
	}
	
	
	public ArrayList<PValue> getPValueOf(Contact c , User u);
	
	public ArrayList<Contact> searchInList(String s, ContactList cl);
	
	public ArrayList<Contact> searchInList(int i, ContactList cl);
	
	public ArrayList<Contact> searchInList(float f, ContactList cl );
	
	public ArrayList<Contact> searchInList(User u, ContactList cl);

	public ArrayList<User> getCollaborators(Contact c);
	
	public ArrayList<User> getCollaborators(ContactList cl);
	
	public ArrayList<User> getCollaborators(PValue pv);
	
	public ArrayList<User> getAllUsers(User u);
	
	
	public void initialise() {
		/**
		 * TODO: Implemetieren Init methode
		 */
	}
	
	
	
	
	
	
}
