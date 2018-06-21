/**
 * Die Klasse EditorServiceImpl implementiert die Applikationslogik für den Editor von Jabics.
 * Sie stellt die Logik zur Verfügung, die bei einem RPC aufgerufen wird und gibt die angefragten Objekte zurück.
 * 
 * @author Anders
 * @author Kurrle
 */

package de.hdm.group11.jabics.server;

import java.util.Date;
import java.util.ArrayList;

import de.hdm.group11.jabics.server.db.*;
import de.hdm.group11.jabics.shared.bo.*;

import de.hdm.group11.jabics.shared.EditorService;
import de.hdm.group11.jabics.shared.LoginInfo;

import com.google.appengine.api.utils.SystemProperty;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EditorServiceImpl extends RemoteServiceServlet implements EditorService{
	/**
	 * Testobjekte
	 */
	JabicsUser u;
	Property p1, p2, p3, p4, p5, p6, p7;
	PValue pv1, pv2, pv3, pv4, pv5, pv6, pv7;
	Contact c1, c2, c3;
	ContactList cl;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Die benötigten Mapper-Instanzen zur DB
	 */
	ContactMapper cMapper = ContactMapper.contactMapper();
	ContactListMapper clMapper = ContactListMapper.contactListMapper();
	PValueMapper pvMapper = PValueMapper.pValueMapper();
	PropertyMapper pMapper = PropertyMapper.propertyMapper();
	UserMapper uMapper = UserMapper.userMapper();
	
	// Momentaner User
	JabicsUser jabicsUser = new JabicsUser();
	
	
	public EditorServiceImpl() {
		
	}
	
	public String testMethod() {
	    /*Contact c = cMapper.findContactById(1);
	    //PValue pv = pvMapper.findPValueById(1);
	    System.out.println(c.getId());
	    System.out.println(c.getDateCreated().toString());
	    c.setOwner(uMapper.findUserByContact(c));
	    return c.getOwner().getEmail();*/
		//return "halowelt";
		ContactList cl = clMapper.findContactListById(1);
		ArrayList<Contact> c = cMapper.findContactsOfContactList(cl);
		for (Contact cnew : c) {
			for (PValue pv : pvMapper.findPValueForContact(cnew)) {
				cnew.addPValue(pv);
				System.out.println(pv.getStringValue());
			};
		}
		return c.get(1).getValues().get(1).toString();
	    //ArrayList<PValue> lol = pvMapper.findPValueForContact(c);
	    //for(int i= 0; i< lol.size(); i++) {
	    //  System.out.println(lol.get(i).getStringValue());
	    //}
	    //return lol.get(1).getStringValue();
	  }
	/**
	 * Diese Methode erstelle einen Nutzer, indem ihr ein String mit dem Namen und der email des Nutzers übergeben wird.
	 */
	public JabicsUser createUser(String name, String email) { 
		JabicsUser newUser = new JabicsUser(name, email);
		return uMapper.insertUser(newUser);
	}
	
	/**
	 * Diese Methode erstellt einen neuen Kontakt aus einem Array aus PValues und dem Nutzer,
	 * der den Kontakt erstellt.
	 */
	public Contact createContact(ArrayList<PValue> cArray, JabicsUser u) { 
		Contact newContact = new Contact(cArray, u);
		cMapper.insertContact(newContact);
		cMapper.insertCollaboration(u, newContact, true);
		return newContact;
	}
	
	/**
	 * Diese Methode erstellt eine neue KontaktListe aus einem Array aus Kontakten und dem Nutzer,
	 * der die KontaktListe erstellt. Zudem wird der Name der Liste als String benötigt
	 * @return die erstellte ContactList
	 */
	public ContactList createContactList(String name, ArrayList<Contact> cArray, JabicsUser u) { 
		ContactList newContactList = clMapper.insertContactList(new ContactList(cArray, name, u));
		clMapper.insertCollaboration(u, newContactList, true);
		return newContactList;
	}
	
	/*
	 * Erstellen eines neuen <code>PValue</code> Objekts, das einen String speichert.
	 */
	public PValue createPValue(Property p, String s, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, s, u);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		
		
		Contact cnew = cMapper.findContactById(c.getId());	
		//cnew.setDateUpdated(LocalDateTime.now());
		
		
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue; 
		
	}
	
	/**
	 * Erstellt ein PValue mit einem int Wert und fügt diesen mitsamt collaboration in die DB ein.
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, int i, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, i, u);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());
		
		//cnew.setDateUpdated(LocalDateTime.now());
		
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue; 
	}
	
	/**
	 * Erstellt ein PValue mit einem Datums Wert und fügt diesen mitsamt collaboration in die DB ein.
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, Date dt, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, dt, u);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());
		
		//cnew.setDateUpdated(LocalDateTime.now());
		
		
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue; 
	}
	
	/**
	 * Erstellt ein PValue mit einem float Wert und fügt diesen mitsamt Collaboration in die DB ein.
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, float f, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, f, u);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());
		
		//cnew.setDateUpdated(LocalDateTime.now());
		
		
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit isOwner = true 
		 * und zuletzt den Contact updaten, damit dieser einen neuen Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue; 
	}
	
	/**
	 * Diese Methode erstellt eine Property, basierend auf einem Namen für die Property und den Datentyp
	 * als Instanz des Enums Type, und gibt diese zurück
	 */
	public Property createProperty(String label, Type type) {
		return pMapper.insertProperty(new Property(label, type));
	}
	
	/**
	 * Gibt alle Kontaktlisten eines Nutzers zurück.
	 */
	public ArrayList<ContactList> getListsOf(JabicsUser u) {
		/*
		ArrayList<ContactList> res = new ArrayList<ContactList>();
		for (ContactList cl: clMapper.findAllContactList(u){
			cl.setOwner(uMapper.findOwnerForContactList(cl));
			res.add(cl);
		}
		return res; */
		
		// temporär: kann gelöscht werden
		ArrayList<ContactList> con = new ArrayList<ContactList>();
		con.add(this.cl);
		return con;
	}
	
	public ArrayList<Contact> getContactsOfList(ContactList cl, JabicsUser u) {
		/* dies ist der richtige Code, nicht löschen!!!
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : clMapper.findContactsFromContactList(cl)) {
			if(cMapper.findCollaborators(c).contains(u)) result.add(c);
		}
		for (Contact cres : result) {
			cres.setOwner(uMapper.findOwnerForContact(cres));
		}
		return result;
		*/
		
		// temporär: kann gelöscht werden sobald funktional
		ArrayList<Contact> cltemp = new ArrayList<Contact>();
		cltemp.add(this.c1);
		cltemp.add(this.c3);
		return cltemp; 
	}
	
	// Gibt alle Contact - Objekte, die ein Nutzer sehen darf, zurück.
	public ArrayList<Contact> getContactsOf(JabicsUser u) { 
		ArrayList<Contact> cons = cMapper.findAllContacts(u);
		for (Contact c : cons) {
			ArrayList<PValue> pvtemp = pvMapper.findPValueForContact(c);
			StringBuffer sBuffer = new StringBuffer();
			for (PValue p : pvtemp) {
				if (p.getProperty().getLabel() == "name") {
					sBuffer.append(p.getStringValue());					
					} else {
						System.out.println("getContactsOf: No name in Array.");
					}
			}
			for (PValue p2: pvtemp) {
				if (p2.getProperty().getLabel() == "lastname") {
					sBuffer.append(" " + p2.getStringValue());				
				} else {
					System.out.println("getContactsOf: No lastname in Array");
				}
			}
			c.setName(sBuffer.toString());
			c.setOwner(uMapper.findUserByContact(c));
		}
		//return cons;
		
		//temporary: kann gelöscht werden sobal fertig
		return cl.getContacts();
	}
	
	public ArrayList<Contact> getAllSharedContactsOf(JabicsUser u){
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : getContactsOf(u)) {
			if(!c.getOwner().equals(u)) result.add(c);
		}
		return result;
	}
	
	// is this method really needed?
	public JabicsUser getUserById(int id) {
		//return uMapper.findUserById(id);
		return this.u;
	}
	
	/**
	 * This Method inserts a specified <code>Contact</code> into a list
	 * @param Contact c 
	 * @param ContactList cl 
	 * @return updated contact list
	 */
	public ContactList addContactToList(Contact c, ContactList cl) {
		cl.addContact(c);
		clMapper.insertContactIntoContactList(cl, c);
		return clMapper.updateContactList(cl);
		
	}
	
	/*
	 * TODO: Diese Methode wird höchstwahrscheinlich nie gebraucht, da stattdessen immer create PValue verwendet wird
	 * erstmal noch drinlassen. Jan
	 */
	public Contact addValueToContact(PValue pv, Contact c, JabicsUser u) {
		if(cMapper.findCollaborators(c).contains(u)) {
			c.addPValue(pv);
			//pvMapper.insertPValue():
		}
		return cMapper.updateContact(c);
	}
	
	
	/**
	 * Suche nach allen <code>Contacts</code> eines <code>Users</code>, die den mitgegebenen String als Property oder PropertyValue enthalten.
	 * @return Eine ArrayList mit allen Contacts, die dem Suchkriterium entsprechen
	 */
	public ArrayList<Contact> searchForContactByExpression(String s, JabicsUser u){
		//neue Kontaktliste, um bereits implementierte Methode verwenden zu können
		ContactList cl = new ContactList(getContactsOf(u));
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
	 * Löscht einen <code>Contact</code> aus der Datenbank. Löscht den Contact für alle Nutzer permanent. Kann nicht rückgängig gemacht werden.
	 * @param Contact, der gelöscht werden soll
	 */
	public void deleteContact(Contact c, JabicsUser ju){
		if(cMapper.findContactById(c.getId()).getOwner().getId() == ju.getId()) {
			ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
			for (JabicsUser u : users) {
				cMapper.deleteCollaboration(c, u);
			}
			for (PValue pv : c.getValues()) {
				pvMapper.deletePValue(pv);
			}
			cMapper.deleteContact(c);
		}
		
	}
	
	/**
	 * Eine <code>ContactList</code> aus der DB löschen. Löscht die Liste für alle Nutzer permanent. Kann nicht rückgängig gemacht werden.
	 * @param cl ContactList, die gelöscht werden soll
	 */
	public void deleteContactList(ContactList cl, JabicsUser ju){
		if(clMapper.findContactListById(cl.getId()).getOwner().getId() == ju.getId()) {
			ArrayList<JabicsUser> users = clMapper.findCollaborators(cl);
			for (JabicsUser u : users) {
				clMapper.deleteCollaboration(cl, u);
			}
			for (Contact c: cl.getContacts()) {
				clMapper.deleteContactfromContactList(cl, c);
			}
			clMapper.deleteContactList(cl);
		}
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
	public PValue updatePValue(PValue pv) {
		
		PValue pvtemp = pvMapper.findPValueById(pv.getId());
		if(pv != pvtemp) {
			
			 //pv.setDateUpdated(LocalDateTime.now());
			 
			 return pvMapper.updatePValue(pv);
		}else return pvMapper.findPValueById(pv.getId());
	}
	
	public ContactList updateContactList(ContactList cl){
		ContactList cltemp = clMapper.findContactListById(cl.getId());
		if(cl != cltemp) {
			
			//cl.setDateUpdated(LocalDateTime.now());
			
			//Alle Kontakte in der neuen Liste durchlaufen, ob einer hinzugekommen ist, wenn ja, einfügen
			for (Contact c : cl.getContacts()) {
				boolean bol = false;
				for(Contact ctemp : cltemp.getContacts()) {
					if(c.getId() == ctemp.getId()) bol = true;
				}
				if (bol == false) clMapper.insertContactIntoContactList(cl, c);
			}
			//Alle Kontakte in der neuen Liste durchlaufen, ob einer weggefallen ist, wenn ja, löschen
			for (Contact ctemp : cltemp.getContacts()) {
				boolean bol = false;
				for(Contact c : cl.getContacts()) {
					if(c.getId() == ctemp.getId()) bol = true;
				}
				if (bol == false) clMapper.deleteContactfromContactList(cl, ctemp);
			}
			return clMapper.updateContactList(cl);
		}else return clMapper.findContactListById(cl.getId());
			/**
			 * TODO Nachdenken, ob wir nur Änderungen überprüfen und nur diese an die DB weitergeben oder das ganze ding in die DB geben
			 */
	}
	
	/*
	 * Diese Methode überprüft, ob der Contact in dieser Form in der DB vorhanden ist,
	 * wenn nicht wird alles auf Konsitenz geprüft und fehlende Inhalte werden upgedated
	 */
	public Contact updateContact(Contact c){
		Contact ctemp = cMapper.findContactById(c.getId());
		ctemp.setValues(pvMapper.findPValueForContact(ctemp));
		/*
		 * TODO: hier die !equals oder != operatoren? was ist besser um zu überprüfen, dass pvalues gleich sind
		 * .equals in Contact noch schreiben?
		 */
		if(c.equals(ctemp) == false) {
			
			//c.setDateUpdated(LocalDateTime.now());
			
			// überprüfen, ob pvalue übereinstimmt, wenn nicht update in db
			for (PValue pv : c.getValues()) {
				if(pvMapper.findPValueById(pv.getId()) != pv) {
					pvMapper.updatePValue(pv);
					pvMapper.deleteCollaboration(pv, pv.getOwner());
					pvMapper.insertCollaboration(u, pv, true);
				}
			}
			return cMapper.updateContact(c);
		}else return cMapper.findContactById(c.getId());
	}
	
	/**
	 * Eine Freigabe zwischen einem Nutzer und einer Kontaktliste einfügen.
	 * Diese Methode nicht! beim Erstellen eines Objekts aufrufen, da isOwner false gesetzt wird.
	 * @param ContactList, um die es sich handelt
	 * @param Nutzer, dem die Liste freigegeben werden soll
	 */
	public void addCollaboration(ContactList cl, JabicsUser u) {
		ArrayList<JabicsUser> users = clMapper.findCollaborators(cl);
		if (!users.contains(u)) {
			clMapper.insertCollaboration(u, cl, false);
		} else return;
	}
	
	/**
	 * Eine Freigabe zwischen einem Nutzer und einer ContactList einfügen.
	 * Diese Methode nicht! beim Erstellen eines Objekts aufrufen, da isOwner false gesetzt wird.
	 * @param ContactList, für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer, dem der Contact freigegeben werden soll
	 */
	public void addCollaboration(Contact c, JabicsUser u) {
		ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
		if (!users.contains(u)) {
			cMapper.insertCollaboration(u, c, false);
		} else return;
	}
	
	/**
	 * Eine Freigabe zwischen einem Nutzer und einem PValue einfügen.
	 * Diese Methode nicht! beim Erstellen eines Objekts aufrufen, da isOwner false gesetzt wird.
	 * @param PValue, für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer, dem das PValue freigegeben werden soll
	 */
	public void addCollaboration(PValue pv, JabicsUser u) {
		ArrayList<JabicsUser> users = pvMapper.findCollaborators(pv);
		if (!users.contains(u)) {
			pvMapper.insertCollaboration(u, pv, false);
		} else return;
	}
	
	public void deleteCollaboration(Contact c, JabicsUser u) {
		cMapper.deleteCollaboration(c, u);
	}
	
	public void deleteCollaboration(ContactList cl, JabicsUser u) {
		clMapper.deleteCollaboration(cl, u);
	}
	
	public void deleteCollaboration(PValue pv, JabicsUser u) {
		pvMapper.deleteCollaboration(pv, u);
	}
	
	/**
	 * @return Die PValues eines Kontakts, die ein Nutzer sehen darf
	 */
	public ArrayList<PValue> getPValueOf(Contact c, JabicsUser u){
		/*ArrayList<PValue> result = new ArrayList<PValue>();
		for (PValue pv : pvMapper.findPValueForContact(c)) {
			for (JabicsUser uu : pvMapper.findCollaborators(pv)) {
				if (u.getId() == uu.getId()) result.add(pv);
			}
		}
		return result;*/
		return c1.getValues();
	}
	
	/**
	 * ######################################################
	 *Kann Potentiell gelöscht werden, da eh nie aufgerufen, oder?
	 *#########################################################
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
	 * Eine Kontaktliste nach String-Values durchsuchen
	 * Diese Methode wird bei deutlich konkreteren Suchvorhaben oder Kriterien verwendet.
	 * Für eine allgemeine Suche siehe searchExpressionInList
	 */
	public ArrayList<Contact> searchInList(String s, ContactList cl){
		return Filter.filterContactsByString(cl.getContacts(), s);
	}
	
	/**
	 * Eine Kontaktliste nach Int-Values durchsuchen
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> searchInList(int i, ContactList cl){
		return Filter.filterContactsByInt(cl.getContacts(), i);
	}
	
	/**
	 * Eine Kontaktliste nach float-Values durchsuchen
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> searchInList(float f, ContactList cl ){
		return Filter.filterContactsByFloat(cl.getContacts(), f);
	}
	
	/**
	 * Eine Liste nach Nutzern durchsuchen, zB Kollaboratoren oder Eigentümer
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> searchInList(JabicsUser u, ContactList cl){
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

	/**
	 * Erhalten aller kollaborierenden Nutzer für einen Kontakt
	 */
	public ArrayList<JabicsUser> getCollaborators(Contact c){
		return cMapper.findCollaborators(c);
	}
	/**
	 * Erhalten aller kollaborierenden Nutzer für eine KontaktListe
	 */
	public ArrayList<JabicsUser> getCollaborators(ContactList cl){
		return clMapper.findCollaborators(cl);
	}
	/**
	 * Erhalten aller kollaborierenden Nutzer für ein PValue
	 */
	public ArrayList<JabicsUser> getCollaborators(PValue pv){
		return pvMapper.findCollaborators(pv);
	}
	
	/**
	 * Erhalten aller Nutzer im System
	 */
	public ArrayList<JabicsUser> getAllUsers(){
		return uMapper.findAllUser();
	}
	
	public JabicsUser setJabicsUser(JabicsUser u) { 
		this.jabicsUser = u;
		
		return jabicsUser;
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
		
		u = new JabicsUser("MeinNutzer");
		p1 = new Property("Vorname", Type.STRING);
		p2 = new Property("Nachname", Type.STRING);
		p1.setStandard(true);
		p3 = new Property("Straße", Type.STRING);
		p4 = new Property("Hausnummer", Type.INT);
		p5 = new Property("Geb", Type.DATE);
		p6 = new Property("Irgendwas1", Type.INT);
		p7 = new Property("Irgendwas2", Type.FLOAT);
		pv1 = new PValue(p1, "Hans", u);
		ArrayList<PValue> val = new ArrayList<PValue>();
		val.add(new PValue( p1, "Max", u));
		val.add(new PValue( p2, "Mustermann",u));
		val.add(new PValue( p3, "eineStraße",u));
		val.add(new PValue( p4, 63,u));
		val.add(new PValue( p5, new Date(1,2,3),u));
		val.add(new PValue( p7, 188.5f,u));
		c1 = new Contact(val, "maxmuster(absichtlichfalschundmitÜberlänge)");
		ArrayList<PValue> val2 = new ArrayList<PValue>();
		val2.add(new PValue( p1, "Alex",u));
		val2.add(new PValue( p2, "Muster123",u));
		val2.add(new PValue( p3, "eineStraße1234",u));
		val2.add(new PValue( p4, 4,u));
		val2.add(new PValue( p5, new Date(1,2,3),u));
		val2.add(new PValue( p7, 167.2f,u));
		c2 = new Contact(val2);
		ArrayList<PValue> val3 = new ArrayList<PValue>();
		val3.add(new PValue( p1, "Udo",u));
		val3.add(new PValue( p2, "Mildenberger",u));
		val3.add(new PValue( p3, "Nobelstraße",u));
		val3.add(new PValue( p4, 8,u));
		val3.add(new PValue( p5, new Date(1,2,3),u));
		val3.add(new PValue( p7, 7.2f,u));
		c3 = new Contact(val3);
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		contacts.add(c1);
		contacts.add(c2);
		contacts.add(c3);
		cl = new ContactList(contacts, "MeineListe",u);
	}


	public void initialise() {
		// TODO Auto-generated method stub
		
	}
}