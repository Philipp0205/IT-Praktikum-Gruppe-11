/**
 * Die Klasse EditorServiceImpl impelemtiert die Applikationslogik für den Editor von Jabics.
 * Sie stellt die Logik zur Verfügung, die bei einem RPC aufgerufen wird und gibt die angefragten Objekte zurück.
 * 
 * @author Anders
 * @author Kurrle
 */

package de.hdm.group11.jabics.server;

import java.time.LocalDateTime;
import java.util.ArrayList;

import de.hdm.group11.jabics.server.db.*;
import de.hdm.group11.jabics.shared.bo.*;

import de.hdm.group11.jabics.shared.EditorService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EditorServiceImpl extends RemoteServiceServlet implements EditorService{
	/**
	 * Testobjekte
	 */
	User u;
	Property p1, p2, p3, p4, p5, p6, p7;
	PValue pv1, pv2, pv3, pv4, pv5, pv6, pv7;
	Contact c1, c2, c3;
	ContactList cl;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

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
	
	/*
	 * Erstellen eines neuen <code>PValue</code> Objekts, das einen String speichert.
	 */
	public PValue createPValue(Property p, String s, Contact c, User u) {
		PValue newPValue = new PValue(p, s);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());
		cnew.setDateUpdated(LocalDateTime.now());
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return pvMapper.findPValueById(newPValue.getId()); 
		
	}
	
	/**
	 * Erstellt ein PValue mit einem int Wert und fügt diesen mitsamt collaboration in die DB ein.
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, int i, Contact c, User u) {
		PValue newPValue = new PValue(p, i);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());
		cnew.setDateUpdated(LocalDateTime.now());
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return pvMapper.findPValueById(newPValue.getId());
	}
	
	/**
	 * Erstellt ein PValue mit einem Datums Wert und fügt diesen mitsamt collaboration in die DB ein.
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, LocalDateTime dt, Contact c, User u) {
		PValue newPValue = new PValue(p, dt);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());
		cnew.setDateUpdated(LocalDateTime.now());
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return pvMapper.findPValueById(newPValue.getId());
	}
	
	/**
	 * Erstellt ein PValue mit einem float Wert und fügt diesen mitsamt Collaboration in die DB ein.
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, float f, Contact c, User u) {
		PValue newPValue = new PValue(p, f);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());
		cnew.setDateUpdated(LocalDateTime.now());
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return pvMapper.findPValueById(newPValue.getId());
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.group11.jabics.shared.EditorService#createProperty(java.lang.String, de.hdm.group11.jabics.shared.bo.Type)
	 */
	public Property createProperty(String label, Type type) {
		Property newProperty = new Property(label, type);
		return pMapper.insertProperty(newProperty);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.group11.jabics.shared.EditorService#getListsOf(de.hdm.group11.jabics.shared.bo.User)
	 */
	public ArrayList<ContactList> getListsOf(User u) {
		//return clMapper.findAllContactList(u);
		
		
		// temporär: kann gelöscht werden
		ArrayList<ContactList> cl = new ArrayList<ContactList>();
		cl.add(this.cl);
		return cl; 
	}
	
	// Gibt alle Contact - Objekte, die ein Nutzer sehen darf, zurück.
	public ArrayList<Contact> getContactsOf(User u) { 
		//ArrayList<Contact> cons = cMapper.findAllContact(u);
		//sind die Kontakte die der mapper zurückgibt auf den Nutzer "zugeschnitten?" also enthalten nur pvalues die der nutzer sehen darf
		/*for (Contact c : cons) {
			pvMapper.
		}*/
		//return cons;
		
		//temporary: kann gelöscht werden sobal fertig
		return cl.getContacts();
	}
	
	// is this method really needed?
	public User getUserById(int id) {
		//return uMapper.findUserById(id);
		return this.u;
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
	
	/*
	 * TODO: Diese Methode wird höchstwahrscheinlich nie gebraucht, da stattdessen immer create PValue verwendet wird
	 * erstmal noch drinlassen. Jan
	 */
	public Contact addValueToContact(PValue pv, Contact c, User u) {
		c.addPValue(pv);
		return cMapper.updateContact(c);
	}
	
	
	/**
	 * Suche nach allen <code>Contacts</code> eines <code>Users</code>, die den mitgegebenen String als Property oder PropertyValue enthalten.
	 * @return Eine ArrayList mit allen Contacts, die dem Suchkriterium entsprechen
	 */
	public ArrayList<Contact> searchForContactByExpression(String s, User u){
		//neue Kontaktliste, um bereits implementierte Methode verwenden zu können
		ContactList cl = new ContactList(cMapper.findAllContacts(u));
		return this.searchExpressionInList(s, cl);
	}
	/**
	 * Entfernt einen <code>Contact</code> aus einer <code>ContactList</code>
	 * @return Die ContactList ohne den zu entfernenden Contact
	 */
	public ContactList removeContactFromList(Contact c, ContactList cl) {
		cl.removeContact(c);
		clMapper.deleteContactfromContactList(cl, c);
		return cl;
	}
	
	/**
	 * Löscht einen <code>Contact</code> aus der Datenbank. Löscht den Contact für alle Nutzer permanent. Kann nicht rückgüngig gemacht werden.
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
			clMapper.deleteContactfromContactList(cl, c);
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
		
		PValue pvtemp = pvMapper.findPValueById(pv.getId());
		if(pv != pvtemp) {
			 pv.setDateUpdated(LocalDateTime.now());
			 pvMapper.updatePValue(pv);
		}
	}
	
	public void updateContactList(ContactList cl){
		ContactList cltemp = clMapper.findContactListById(cl.getId());
		if(cl != cltemp) {
			cl.setDateUpdated(LocalDateTime.now());
			clMapper.updateContactList(cl);
		}
			/**
			 * TODO Nachdenken, ob wir nur Änderungen überprüfen und nur diese an die DB weitergeben oder das ganze ding in die DB geben
			 */
	}
	
	/*
	 * Diese Methode checkt, ob der Contact in dieser Form in der DB vorhanden ist,
	 * wenn nicht wird alles auf Konsitenz geprüft und fehlende Inhalte werden upgedated
	 */
	public void updateContact(Contact c){
		Contact ctemp = cMapper.findContactById(c.getId());
		ctemp.setValues(pvMapper.findPValueForContact(ctemp));
		/*
		 * TODO: hier die !equals oder != operatoren? was ist besser um zu überprüfen, dass pvalues gleich sind
		 * .equals in Contact noch schreiben?
		 */
		if(c.equals(ctemp) == false) {
			c.setDateUpdated(LocalDateTime.now());
			// überprüfen, ob pvalue übereinstimmt, wenn nicht update in db
			for (PValue pv : c.getValues()) {
				if(pvMapper.findPValueById(pv.getId()) != pv) {
					pvMapper.updatePValue(pv);
					pvMapper.deleteCollaboration(pv, pv.getOwner());
					pvMapper.insertCollaboration(u, pv, true);
				}
			}
			cMapper.updateContact(c);
		}
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
	
	/**
	 * @return Die PValues eines Kontakts, die ein Nutzer sehen darf
	 */
	public ArrayList<PValue> getPValueOf(Contact c, User u){
		ArrayList<PValue> result = new ArrayList<PValue>();
		for (PValue pv : pvMapper.findPValueForContact(c)) {
			for (User uu : pvMapper.findCollaborators(pv)) {
				if (u.getId() == uu.getId()) result.add(pv);
			}
		}
		return result;
	}
	
	/**
	 * Diese Methode wird verwendet, wenn der Datentyp des Suchkriteriums nicht bekannt ist!
	 * Suche nach allen <code>Contacts</code> in einer <code>ContactList</code>, die den mitgegebenen String als Property oder PropertyValue enthalten.
	 * @return Eine ArrayList mit allen Contacts, die dem Suchkriterium entsprechen
	 */
	public ArrayList<Contact> searchExpressionInList(String s, ContactList cl){
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
	
	/*
	 * Diese Methode wird bei deutlich konkreteren Suchvorhaben oder kriterien verwendet.
	 * Für eine allgemeine Suche siehe searchExpressionInList
	 */
	public ArrayList<Contact> searchInList(String s, ContactList cl){
		return Filter.filterContactsByString(cl.getContacts(), s);
	}
	
	public ArrayList<Contact> searchInList(int i, ContactList cl){
		return Filter.filterContactsByInt(cl.getContacts(), i);
	}
	
	public ArrayList<Contact> searchInList(float f, ContactList cl ){
		return Filter.filterContactsByFloat(cl.getContacts(), f);
	}
	
	public ArrayList<Contact> searchInList(User u, ContactList cl){
		ArrayList<Contact> result = new ArrayList<Contact>();
		for(Contact c : cl.getContacts()) {
			/*
			 * Wenn der Contact Owner der übergebene Nutzer ist oder
			 * ein Collaborator an einem der Kontakte der übergebene Nutzer ist
			 * dann wird der Kontakt zurückgegeben
			 */
			if(c.getOwner().getId() == u.getId() || cMapper.findCollaborators(c).contains(u)) {
					result.add(c);
			}
		}
		return result;
	}

	public ArrayList<User> getCollaborators(Contact c){
		return cMapper.findCollaborators(c);
	}
	
	public ArrayList<User> getCollaborators(ContactList cl){
		return clMapper.findCollaborators(cl);
	}
	
	public ArrayList<User> getCollaborators(PValue pv){
		return pvMapper.findCollaborators(pv);
	}
	
	public ArrayList<User> getAllUsers(){
		return uMapper.findAllUser();
	}
	
	
	public void init() {
		/*
		 * Instanzen von allen Mappern
		 */
		
		this.cMapper = ContactMapper.contactMapper();
		this.clMapper = ContactListMapper.contactListMapper();
		this.pvMapper = PValueMapper.pValueMapper();
		this.pMapper = PropertyMapper.propertyMapper();
		this.uMapper = UserMapper.userMapper();
		/**
		 * TODO: Implemetieren Init methode
		 */
		u = new User("MeinNutzer");
		p1 = new Property("Name", Type.STRING);
		p2 = new Property("VorName", Type.STRING);
		p1.setStandard(true);
		p3 = new Property("Straße", Type.STRING);
		p4 = new Property("Hausnummer", Type.INT);
		p5 = new Property("Geb", Type.DATE);
		p6 = new Property("Irgendwas1", Type.INT);
		p7 = new Property("Irgendwas2", Type.FLOAT);
		ArrayList<PValue> val = new ArrayList<PValue>();
		val.add(new PValue( p1, "Max"));
		val.add(new PValue( p2, "Mustermann"));
		val.add(new PValue( p3, "eineStraße"));
		val.add(new PValue( p4, 63));
		val.add(new PValue( p5, LocalDateTime.of(2000, 5, 1, 20, 10)));
		val.add(new PValue( p7, 188.5f));
		c1 = new Contact(val, "maxmuster(absichtlichfalschundmitÜberlänge)");
		ArrayList<PValue> val2 = new ArrayList<PValue>();
		val2.add(new PValue( p1, "Alex"));
		val2.add(new PValue( p2, "Muster123"));
		val2.add(new PValue( p3, "eineStraße1234"));
		val2.add(new PValue( p4, 4));
		val2.add(new PValue( p5, LocalDateTime.of(1993, 2, 1, 10, 34)));
		val2.add(new PValue( p7, 167.2f));
		c2 = new Contact(val2);
		ArrayList<PValue> val3 = new ArrayList<PValue>();
		val3.add(new PValue( p1, "Udo"));
		val3.add(new PValue( p2, "Mildenberger"));
		val3.add(new PValue( p3, "Nobelstraße"));
		val3.add(new PValue( p4, 8));
		val3.add(new PValue( p5, LocalDateTime.of(2015, 2, 1, 3, 15)));
		val3.add(new PValue( p7, 7.2f));
		c3 = new Contact(val3);
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		contacts.add(c1);
		contacts.add(c2);
		contacts.add(c3);
		cl = new ContactList(contacts, "MeineListe");
	}


	public void initialise() {
		// TODO Auto-generated method stub
		
	}
	
}
