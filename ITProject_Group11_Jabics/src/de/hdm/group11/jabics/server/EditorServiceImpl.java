package de.hdm.group11.jabics.server;

import java.util.Date;
import java.util.ArrayList;

import de.hdm.group11.jabics.server.db.*;
import de.hdm.group11.jabics.shared.bo.*;
import de.hdm.group11.jabics.shared.EditorService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Die Klasse EditorServiceImpl implementiert die Applikationslogik für den
 * Editor von Jabics. Sie stellt die Logik zur Verfügung, die bei einem RPC
 * aufgerufen wird und gibt die angefragten Objekte zurück.
 * 
 * @author Anders
 * @author Kurrle
 */
public class EditorServiceImpl extends RemoteServiceServlet implements EditorService {
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
		/*
		 * Contact c = cMapper.findContactById(1); //PValue pv =
		 * pvMapper.findPValueById(1); System.out.println(c.getId());
		 * System.out.println(c.getDateCreated().toString());
		 * c.setOwner(uMapper.findUserByContact(c)); return c.getOwner().getEmail();
		 */
		// return "halowelt";
		ContactList cl = clMapper.findContactListById(1);
		ArrayList<Contact> c = cMapper.findContactsOfContactList(cl);
		for (Contact cnew : c) {
			for (PValue pv : pvMapper.findPValueForContact(cnew)) {
				cnew.addPValue(pv);
				System.out.println(pv.getStringValue());
			}
			;
		}
		return c.get(1).getValues().get(1).toString();
		// ArrayList<PValue> lol = pvMapper.findPValueForContact(c);
		// for(int i= 0; i< lol.size(); i++) {
		// System.out.println(lol.get(i).getStringValue());
		// }
		// return lol.get(1).getStringValue();
	}

	/**
	 * Diese Methode erstelle einen Nutzer, indem ihr ein String mit dem Namen und
	 * der email des Nutzers übergeben wird.
	 */
	public JabicsUser createUser(String name, String email) {
		JabicsUser newUser = new JabicsUser(name, email);
		return uMapper.insertUser(newUser);
	}

	/**
	 * Diese Methode erstellt einen neuen Kontakt aus einem Array aus PValues und
	 * dem Nutzer, der den Kontakt erstellt.
	 */
	public Contact createContact(ArrayList<PValue> cArray, JabicsUser u) {
		System.out.println(cArray);
		// Kontakt erstellen und Nickname setzen lassen
		System.err.println("create COntact");
		Contact newContact = new Contact(cArray, u);
		System.err.println("create COntact2");
		System.out.println(cArray.toString());

		// Sicherstellen, dass in demm neu erstellten Kontakt keine PValues ohne ID
		// liegen;

		System.err.println("create COntact3");
		newContact = cMapper.insertContact(newContact);
		System.err.println("create COntact4");
		System.out.println("Kontakt id: " + newContact.getId());
		newContact = cMapper.insertCollaboration(u, newContact, true);
		System.out.println("Kontakt id: " + newContact.getId());

		ArrayList<PValue> testArray = cArray;

		/*
		 * neu erstellte pv von alten trennen und inserten Es wird überprüft, ob ein
		 * pValue die standardid = 0 hat, da es dann ein durch das öffnen oder einen
		 * Klick auf "Hinzufügen" erstelltes ist. Zusätzlich muss ein Wert in dem Feld
		 * eingetragen worden sein, deswegen der containsValue()
		 */
		System.err.println("create COntact4");
		for (PValue pv : cArray) {
			System.out.println("PValueID 1 " + pv.getId());
			PValue newPVal = new PValue();
			if (pv.getId() == 0 && pv.containsValue()) {
				System.out.println("PValueID 2" + pv.getId());
				switch (pv.getProperty().getType()) {
				case STRING:
					newPVal = createPValue(pv.getProperty(), pv.getStringValue(), newContact, u);
					System.out.println("Switch String " + pv.getId());
					break;
				case DATE:
					createPValue(pv.getProperty(), pv.getDateValue(), newContact, u);
					break;
				case FLOAT:
					createPValue(pv.getProperty(), pv.getFloatValue(), newContact, u);
					break;
				case INT:
					createPValue(pv.getProperty(), pv.getIntValue(), newContact, u);
					break;
				}
				if (newPVal.containsValue() && pv.getId() != 0) {
					System.out.println("Add PValue to Contact " + pv.getId());
					// newContact.addPValue(newPVal);
					testArray.add(newPVal);
				} else
					System.out.println("Beim Erstellen neuer PValues ist etwas schiefgegangen");
			} else
				System.out.println("Beim Erstellen eines Kontakts war eine Id oder ein Value nicht gesetzt!");
		}
		newContact.getValues().clear();
		System.out.println("newContact " + newContact.toString());

		newContact.setValues(testArray);
		return newContact;
	}

	/**
	 * Diese Methode erstellt eine neue KontaktListe aus einem Array aus Kontakten
	 * und dem Nutzer, der die KontaktListe erstellt. Zudem wird der Name der Liste
	 * als String benötigt
	 * 
	 * @return die erstellte ContactList
	 */
	public ContactList createContactList(String name, ArrayList<Contact> cArray, JabicsUser u) {
		ContactList newContactList = clMapper.insertContactList(new ContactList(cArray, name, u));
		clMapper.insertCollaboration(u, newContactList, true);
		return newContactList;
	}

	/*
	 * Erstellen eines neuen <code>PValue</code> Objekts, das einen String
	 * speichert.
	 */
	public PValue createPValue(Property p, String s, Contact c, JabicsUser u) {
		System.out.println(p.getLabel());
		System.out.println(p.getTypeInString());
		PValue newPValue = new PValue(p, s, u);

		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und
		 * DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());

		// cnew.setDateUpdated(LocalDateTime.now());
		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		System.out.println("newPValue " + newPValue.getId());

		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue;
	}

	/**
	 * Erstellt ein PValue mit einem int Wert und fügt diesen mitsamt collaboration
	 * in die DB ein.
	 * 
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, int i, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, i, u);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und
		 * DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());

		// cnew.setDateUpdated(LocalDateTime.now());

		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		System.out.println("createPValue: Neue ID: " + newPValue.getId());
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue;
	}

	/**
	 * Erstellt ein PValue mit einem Datums Wert und fügt diesen mitsamt
	 * collaboration in die DB ein.
	 * 
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, Date dt, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, dt, u);
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und
		 * DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());

		// cnew.setDateUpdated(LocalDateTime.now());

		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue;
	}

	/**
	 * Erstellt ein PValue mit einem float Wert und fügt diesen mitsamt
	 * Collaboration in die DB ein.
	 * 
	 * @return das neu erstellte PValue Objekt
	 */
	public PValue createPValue(Property p, float f, Contact c, JabicsUser u) {
		System.out.println("start");
		PValue newPValue = new PValue(p, f, u);
		System.out.println("ende");
		/*
		 * Contact aus der Datenbank abrufen, um Datenkonsistenz sicherzustellen und
		 * DateUpdated auf jetzt stellen.
		 */
		Contact cnew = cMapper.findContactById(c.getId());

		// cnew.setDateUpdated(LocalDateTime.now());

		/*
		 * erst erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, cnew);
		pvMapper.insertCollaboration(u, newPValue, true);
		cMapper.updateContact(cnew);
		return newPValue;
	}

	/**
	 * Diese Methode erstellt eine Property, basierend auf einem Namen für die
	 * Property und den Datentyp als Instanz des Enums Type, und gibt diese zurück
	 */
	public Property createProperty(String label, Type type) {
		return pMapper.insertProperty(new Property(label, type));
	}

	/**
	 * Gibt alle Kontaktlisten eines Nutzers zurück.
	 */
	public ArrayList<ContactList> getListsOf(JabicsUser u) {

		ArrayList<ContactList> result = new ArrayList<ContactList>();

		for (ContactList cl : clMapper.findContactListOfUser(u)) {
			cl.setOwner(uMapper.findUserByContactList(cl));
			System.out.println("2.2 getListsOf " + cl.getListName());
			result.add(cl);
		}

		return result;
		// temporär: kann gelöscht werden

		// ArrayList<ContactList> cl = new ArrayList<ContactList>();
		// cl.add(this.cl);
		// return cl;

	}

	public ArrayList<Contact> getContactsOfList(ContactList cl, JabicsUser u) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		// result = cMapper.findContactsOfContactList(cl);
		System.out.println("Got all Contacts of List " + cl.toString());

		for (Contact c : cMapper.findContactsOfContactList(cl)) {
			System.out.println("2.2 find Contact" + c.toString());
			// if(cMapper.findCollaborators(c).contains(u)) result.add(c);

			// c.setOwner(uMapper.findUserByContact(c));
			result.add(c);
		}
		// for (Contact cres : result) {
		// cres.setOwner(uMapper.findUserByContact(cres));
		// }

		return result;
	}

	// Gibt alle Contact - Objekte, die ein Nutzer sehen darf, zurück.
	public ArrayList<Contact> getContactsOf(JabicsUser u) {
		ArrayList<Contact> cons = cMapper.findAllContacts(u);
		// für jedes Kontaktobjekt werden die PValues in einer temporären ArrayList
		// gespeichert.
		System.out.println("Got all Contacts of User " + u.getId());
		for (Contact c : cons) {

			c.setOwner(uMapper.findUserByContact(c));

			/*
			 * !!!!!!!!!!!!!!!!!!!!! hier ist die Logik für ShareStatus TODO:
			 * einkommentieren
			 */
		}
		return cons;

	}

	public ArrayList<Contact> getAllSharedContactsOf(JabicsUser u) {
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : getContactsOf(u)) {
			if (c.getOwner().getId() != u.getId())
				result.add(c);
		}
		return result;
	}

	// is this method really needed?
	public JabicsUser getUserById(int id) {
		return uMapper.findUserById(id);
	}

	/**
	 * This Method inserts a specified <code>Contact</code> into a list
	 * 
	 * @param Contact     c
	 * @param ContactList cl
	 * @return updated contact list
	 */

	public Contact addContactToList(Contact c, ContactList cl) {
		System.err.println("Liste ändern: " + cl.getListName());

		cl.addContact(c);
		// Für alle Nutzer, mit denen der Kontakt geteilt ist, eine Collab einfügen. Die
		// überprüfung, ob der Kontakt bereits geteilt ist, passiert in
		// addCollaboration()
		System.err.println("Kollaboratoren finden:");
		for (JabicsUser u : clMapper.findCollaborators(cl)) {
			System.err.println("Kollaborator:" + u.getUsername());
			addCollaboration(c, u);
		}
		System.err.println("Liste den Kontakt hinzufügen: " + c.getName());
		clMapper.insertContactIntoContactList(cl, c);
		return c;
	}

	/*
	 * TODO: Diese Methode wird höchstwahrscheinlich nie gebraucht, da stattdessen
	 * immer create PValue verwendet wird erstmal noch drinlassen. Jan
	 */
	public Contact addValueToContact(PValue pv, Contact c, JabicsUser u) {
		if (cMapper.findCollaborators(c).contains(u)) {
			c.addPValue(pv);
			// pvMapper.insertPValue():
		}
		return cMapper.updateContact(c);
	}

	/**
	 * Suche nach allen <code>Contacts</code> eines <code>Users</code>, die den
	 * mitgegebenen String als Property oder PropertyValue enthalten.
	 * 
	 * @return Eine ArrayList mit allen Contacts, die dem Suchkriterium entsprechen
	 */

	public ArrayList<Contact> searchForContactByExpression(String s, JabicsUser u) {
		// neue Kontaktliste, um bereits implementierte Methode verwenden zu können
		ContactList cl = new ContactList(getContactsOf(u));
		return this.searchExpressionInList(s, cl);
	}

	/**
	 * Entfernt einen <code>Contact</code> aus einer <code>ContactList</code>
	 * 
	 * @return Die ContactList ohne den zu entfernenden Contact
	 */
	public Contact removeContactFromList(Contact c, ContactList cl) {
		cl.removeContact(c);
		for (JabicsUser u : clMapper.findCollaborators(cl)) {
			deleteCollaboration(c, u);
		}
		clMapper.deleteContactfromContactList(cl, c);
		return c;
	}

	/**
	 * Löscht einen <code>Contact</code> aus der Datenbank. Löscht den Contact für
	 * alle Nutzer permanent. Kann nicht rückgängig gemacht werden.
	 * 
	 * @param Contact, der gelöscht werden soll
	 */
	public void deleteContact(Contact c, JabicsUser ju) {
		if (uMapper.findUserByContact(c).getId() == ju.getId()) {
			ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
			ArrayList<PValue> pvalues = pvMapper.findPValueForContact(c);
			for (JabicsUser u : users) {
				cMapper.deleteCollaboration(c, u);
				for (int i = 0; i < pvalues.size(); i++) {
					pvMapper.deleteCollaboration(pvalues.get(i), u);
				}
				System.out.println("Contact " + c.getId() + "Collabs deleted");
			}
			for (PValue pv : pvalues) {
				pvMapper.deletePValue(pv);
				System.out.println("PValue " + pv.getId() + "deleted");
			}
			cMapper.deleteContact(c);
			System.out.println("Contact " + c.getId() + "deleted");
		} else {
			System.out.println("fail");
		}
	}

	/**
	 * Eine <code>ContactList</code> aus der DB löschen. Löscht die Liste für alle
	 * Nutzer permanent. Kann nicht rückgängig gemacht werden.
	 * 
	 * @param cl ContactList, die gelöscht werden soll
	 */
	public void deleteContactList(ContactList cl, JabicsUser ju) {
		if (clMapper.findContactListById(cl.getId()).getOwner().getId() == ju.getId()) {
			ArrayList<JabicsUser> users = clMapper.findCollaborators(cl);
			for (JabicsUser u : users) {
				clMapper.deleteCollaboration(cl, u);
			}
			for (Contact c : cl.getContacts()) {
				clMapper.deleteContactfromContactList(cl, c);
			}
			clMapper.deleteContactList(cl);
		}
	}

	/**
	 * Eine Property aus der Datenbank löschen. Es wird überprüft, ob die
	 * Eigenschaft gelöscht werden darf.
	 * 
	 * @param Property, die gelöscht werden soll
	 */
	public void deleteProperty(Property p) {
		if (!p.isStandard()) {
			pMapper.deleteProperty(p);
		} else
			System.out.println("Tried to delete standard property. This should not have been possible");
	}

	public void deletePValue(PValue pv, Contact c) {
		ArrayList<JabicsUser> cols = pvMapper.findCollaborators(pv);
		// überprüfen, ob dieses PV das letzte seiner Art ist und wenn ja die zugehörige
		// Property löschen
		if (!pv.getProperty().isStandard()) {
			ArrayList<PValue> otherPVal = pvMapper.findPValueForContact(c);
			boolean bol = true;
			for (PValue o : otherPVal) {
				if (o.getProperty().getId() == pv.getProperty().getId())
					bol = false;
			}
			if (bol)
				pMapper.deleteProperty(pv.getProperty());
		}

		/**
		 * glöckchen: if(pv.getOwner().getId() == u.getId()) {
		 */
		for (int i = 0; i < cols.size(); i++) {
			pvMapper.deleteCollaboration(pv, cols.get(i));
			pvMapper.deletePValue(pv);
		}
	}

	/**
	 * Ein <code>PValue</code> aktualisieren, sodass es in der Datenbank konsitent
	 * gespeichert wird.
	 * 
	 * @param Ein PropertyValue, das aktualisiert werden soll
	 */
	public PValue updatePValue(PValue pv) {
		System.out.println(pv.getId());
		PValue pvtemp = pvMapper.findPValueById(pv.getId());
		// TODO: eine gescheite .equals für PValue programmieren
		if (pv != pvtemp) {
			PValue p = pvMapper.updatePValue(pv);
			System.out.println(p.toString());
			return p;
		} else
			System.out.println("updatePValue unnötig oder fehlgeschlagen");
		return pvMapper.findPValueById(pv.getId());
	}

	public ContactList updateContactList(ContactList cl) {
		ContactList cltemp = clMapper.findContactListById(cl.getId());
		// TODO: gescheite .equals für Kontaktlisten
		if (cl != cltemp) {

			// Alle Kontakte in der neuen Liste durchlaufen, ob einer hinzugekommen ist,
			// wenn ja, einfügen. Allen freigegebenen Nutzern den Kontakt freigeben
			for (Contact c : cl.getContacts()) {
				boolean bol = false;
				for (Contact ctemp : cltemp.getContacts()) {
					if (c.getId() == ctemp.getId())
						bol = true;
				}
				if (bol == false) {
					clMapper.insertContactIntoContactList(cl, c);
					for (JabicsUser u : clMapper.findCollaborators(cl)) {
						addCollaboration(c, u);
					}
				}
			}
			// Alle Kontakte in der neuen Liste durchlaufen, ob einer weggefallen ist, wenn
			// ja, löschen, Allen freigegebenen Nutzern den Kontakt entziehen
			for (Contact ctemp : cltemp.getContacts()) {
				boolean bol = false;
				for (Contact c : cl.getContacts()) {
					if (c.getId() == ctemp.getId())
						bol = true;
				}
				if (bol == false) {
					clMapper.deleteContactfromContactList(cl, ctemp);
					for (JabicsUser u : clMapper.findCollaborators(cl)) {
						deleteCollaboration(ctemp, u);
					}
				}
			}
			return clMapper.updateContactList(cl);
		} else
			return clMapper.findContactListById(cl.getId());
	}

	/**
	 * Diese Methode überprüft, ob der Contact in dieser Form in der DB vorhanden
	 * ist, wenn nicht wird alles auf Konsitenz geprüft und fehlende Inhalte werden
	 * upgedated
	 */
	public Contact updateContact(Contact c) {

		// Nickname neu setzen
		c.updateNickname();
		System.out.println("5.1 updateContact");
		// GWT.log("5.1 Contact:" + c.getName());
		System.out.println("5.1 Contact:" + c.getName());

		Contact ctemp = cMapper.findContactById(c.getId());
		ctemp.setValues(pvMapper.findPValueForContact(ctemp));
		/*
		 * TODO: hier die !equals oder != operatoren? was ist besser um zu überprüfen,
		 * dass pvalues gleich sind .equals in Contact noch schreiben?
		 */
		for (PValue pv : c.getValues()) {
			System.out.println("Eigenschaft angekommen: " + pv.toString());
		}
		System.out.println("5.1 ctemp" + "ist kontakt gleich?" + c.equals(ctemp) + " kontaktename: " + ctemp.getName());
		if (c.equals(ctemp) == false) {

			// überprüfen, ob pvalue übereinstimmt, wenn nicht update in db
			for (PValue pv : c.getValues()) {
				if (pvMapper.findPValueById(pv.getId()) != pv) {
					pvMapper.updatePValue(pv);
					// pvMapper.deleteCollaboration(pv, pv.getOwner());
					// pvMapper.insertCollaboration(u, pv, true);
				}
			}

			try {
				c.setShareStatus(c.getShareStatus());
			} catch (Exception e) {
				System.err.println("Share Status des Kontakts " + c.getId() + "wurde nicht gefunden");
			}
			c.setValues(pvMapper.findPValueForContact(c));
			return cMapper.updateContact(c);
		} else
			return cMapper.findContactById(c.getId());
	}

	/**
	 * Eine Freigabe zwischen einem Nutzer und einer Kontaktliste einfügen. Diese
	 * Methode nicht! beim Erstellen eines Objekts aufrufen, da isOwner false
	 * gesetzt wird.
	 * 
	 * @param ContactList, um die es sich handelt
	 * @param Nutzer, dem die Liste freigegeben werden soll
	 */
	public void addCollaboration(ContactList cl, JabicsUser u) {
		ArrayList<JabicsUser> users = clMapper.findCollaborators(cl);
		Boolean bol = true;
		// Überprüfen, ob die Liste bereits freigegeben ist
		for (JabicsUser user : users) {
			if (user.getId() == u.getId()) {
				bol = false;
			}
		}
		// Wenn die Liste dem Nutzer noch nicht freigegeben ist
		if (bol) {
			cl.setShareStatus(BoStatus.IS_SHARED);
			clMapper.insertCollaboration(u, cl, false);
			ArrayList<Contact> contactsInList = cMapper.findContactsOfContactList(cl);
			for (Contact c : contactsInList) {
				addCollaboration(c, u);
			}
		}
		return;
	}

	/**
	 * Eine Freigabe zwischen einem Nutzer und einer ContactList einfügen. Diese
	 * Methode nicht! beim Erstellen eines Objekts aufrufen, da isOwner false
	 * gesetzt wird.
	 * 
	 * @param ContactList, für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer, dem der Contact freigegeben werden soll
	 */
	public void addCollaboration(Contact c, JabicsUser u) {
		System.err.println("Kollab: " + c.getName() + u.getUsername());
		ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
		Boolean bol = true;
		// Überprüfen, ob der Kontakt bereits freigegeben ist
		for (JabicsUser user : users) {
			if (user.getId() == u.getId()) {
				bol = false;
			}
		}
		// Wenn der Kontakt dem Nutzer noch nicht freigegeben ist
		if (bol) {
			c.setShareStatus(BoStatus.IS_SHARED);
			System.err.println("Kollab einfügen: " + c.getName() + u.getUsername());
			cMapper.insertCollaboration(u, c, false);
		} else
			return;
	}

	/**
	 * Eine Freigabe zwischen einem Nutzer und einem PValue einfügen. Diese Methode
	 * nicht! beim Erstellen eines Objekts aufrufen, da isOwner false gesetzt wird.
	 * 
	 * @param PValue, für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer, dem das PValue freigegeben werden soll
	 */
	public void addCollaboration(PValue pv, JabicsUser u) {
		ArrayList<JabicsUser> users = pvMapper.findCollaborators(pv);
		Boolean bol = true;
		// Überprüfen, ob der Kontakt bereits freigegeben ist
		for (JabicsUser user : users) {
			if (user.getId() == u.getId()) {
				bol = false;
			}
		}
		// Wenn das PValue dem Nutzer noch nicht freigegeben ist
		if (bol) {
			pv.setShareStatus(BoStatus.IS_SHARED);
			pvMapper.insertCollaboration(u, pv, false);
		} else
			return;
	}

	public void deleteCollaboration(Contact c, JabicsUser u) {
		ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
		if (users.isEmpty() || (users.size() == 1 && (users.get(0).getId() == u.getId()))) {
			c.setShareStatus(BoStatus.NOT_SHARED);
		}
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
	public ArrayList<PValue> getPValueOf(Contact c, JabicsUser u) {

		ArrayList<PValue> result = new ArrayList<PValue>();

		for (PValue pv : pvMapper.findPValueForContact(c)) {
			for (JabicsUser uu : pvMapper.findCollaborators(pv)) {
				if (u.getId() == uu.getId()) {
					result.add(pv);
				}
			}
		}

		return result;
	}

	/**
	 * ###################################################### Kann Potentiell
	 * gelöscht werden, da eh nie aufgerufen, oder?
	 * ######################################################### Diese Methode wird
	 * verwendet, wenn der Datentyp des Suchkriteriums nicht bekannt ist! Suche nach
	 * allen <code>Contacts</code> in einer <code>ContactList</code>, die den
	 * mitgegebenen String als Property oder PropertyValue enthalten.
	 * 
	 * @return Eine ArrayList mit allen Contacts, die dem Suchkriterium entsprechen
	 */
	public ArrayList<Contact> searchExpressionInList(String s, ContactList cl) {
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : cl.getContacts()) {
			if (c.getName().contains(s))
				result.add(c);
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
	 * Eine Kontaktliste nach String-Values durchsuchen Diese Methode wird bei
	 * deutlich konkreteren Suchvorhaben oder Kriterien verwendet. Für eine
	 * allgemeine Suche siehe searchExpressionInList
	 */
	public ArrayList<Contact> searchInList(String s, ContactList cl) {	
		ArrayList<Contact> contacts = cMapper.findContactsOfContactList(cl);
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}
		ArrayList<Contact> alc = Filter.filterContactsByString(contacts, s);
		return alc;
	}

	/**
	 * Eine Kontaktliste nach Int-Values durchsuchen
	 * 
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> searchInList(int i, ContactList cl) {
		return Filter.filterContactsByInt(cl.getContacts(), i);
	}

	/**
	 * Eine Kontaktliste nach float-Values durchsuchen
	 * 
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> searchInList(float f, ContactList cl) {
		return Filter.filterContactsByFloat(cl.getContacts(), f);
	}

	/**
	 * Eine Liste nach Nutzern durchsuchen, zB Kollaboratoren oder Eigentümer
	 * 
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> searchInList(JabicsUser u, ContactList cl) {
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : cl.getContacts()) {
			/*
			 * Wenn der Contact Owner der übergebene Nutzer ist oder ein Collaborator an
			 * einem der Kontakte der übergebene Nutzer ist dann wird der Kontakt
			 * zurückgegeben
			 */
			if (c.getOwner().getId() == u.getId() || cMapper.findCollaborators(c).contains(u)) {
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * Erhalten aller noch nicht kollaborierenden Nutzer für einen Kontakt
	 */
	public ArrayList<JabicsUser> getAllNotCollaboratingUser(Contact c) {
		ArrayList<JabicsUser> res = uMapper.findAllUser();
		for (JabicsUser u : cMapper.findCollaborators(c)) {
			res.remove(u);
		}
		return res;
	}

	/**
	 * Erhalten aller noch nicht kollaborierenden Nutzer für eine Kontaktliste
	 */
	public ArrayList<JabicsUser> getAllNotCollaboratingUser(ContactList cl) {
		ArrayList<JabicsUser> res = uMapper.findAllUser();
		for (JabicsUser u : clMapper.findCollaborators(cl)) {
			res.remove(u);
		}
		return res;
	}

	/**
	 * Den Besitzer eines Kontakt-Objekts ermitteln und zurückgeben
	 */
	public JabicsUser getOwnerOfContact(Contact c) {
		return uMapper.findUserByContact(c);
	}

	/**
	 * Erhalten aller kollaborierenden Nutzer für einen Kontakt
	 */
	public ArrayList<JabicsUser> getCollaborators(Contact c) {
		return cMapper.findCollaborators(c);
	}

	/**
	 * Erhalten aller kollaborierenden Nutzer für eine KontaktListe
	 */
	public ArrayList<JabicsUser> getCollaborators(ContactList cl) {
		return clMapper.findCollaborators(cl);
	}

	/**
	 * Erhalten aller kollaborierenden Nutzer für ein PValue
	 */
	public ArrayList<JabicsUser> getCollaborators(PValue pv) {
		return pvMapper.findCollaborators(pv);
	}

	/**
	 * Erhalten aller Nutzer im System
	 */
	public ArrayList<JabicsUser> getAllUsers() {
		return uMapper.findAllUser();
	}

	public JabicsUser setJabicsUser(JabicsUser u) {
		this.jabicsUser = u;

		return jabicsUser;
	}

	public ArrayList<Property> getStandardProperties(){
		return pMapper.findAllStandardPropertys();
	}
	
	
	public void initialise() {
		// TODO Auto-generated method stub

	}

}