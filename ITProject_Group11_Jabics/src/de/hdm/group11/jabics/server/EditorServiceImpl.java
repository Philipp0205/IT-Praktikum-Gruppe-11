package de.hdm.group11.jabics.server;

import java.util.Date;
import java.util.ArrayList;

import de.hdm.group11.jabics.server.db.*;
import de.hdm.group11.jabics.shared.bo.*;
import de.hdm.group11.jabics.shared.EditorService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Die Klasse <code>EditorServiceImpl</code> implementiert die Applikationslogik
 * für die Klasse <code>EditorService</code>. Sie stellt die Logik zur
 * Verfügung, die bei einem RPC aufgerufen wird und gibt die angefragten Objekte
 * zurück.
 * 
 * @author Anders
 * @author Kurrle
 * @author Brase
 * @author Stahl
 */
public class EditorServiceImpl extends RemoteServiceServlet implements EditorService {

	private static final long serialVersionUID = 1L;

	/**
	 * Instanz der Klasse <code>ContactMapper</code>
	 */
	ContactMapper cMapper = ContactMapper.contactMapper();

	/**
	 * Instanz der Klasse <code>ContactListMapper</code>
	 */
	ContactListMapper clMapper = ContactListMapper.contactListMapper();

	/**
	 * Instanz der Klasse <code>PValueMapper</code>
	 */
	PValueMapper pvMapper = PValueMapper.pValueMapper();

	/**
	 * Instanz der Klasse <code>PropertyMapper</code>
	 */
	PropertyMapper pMapper = PropertyMapper.propertyMapper();

	/**
	 * Instanz der Klasse <code>UserMapper</code>
	 */
	UserMapper uMapper = UserMapper.userMapper();

	/**
	 * Objekt des aktuellen <code>JabicsUser</code>
	 */
	JabicsUser jabicsUser = new JabicsUser();

	/**
	 * Default Konstruktor
	 */
	public EditorServiceImpl() {
	}

	/**
	 * Erzeugen eines <code>Contact</code> Objekts.
	 * 
	 * @param pArray
	 *            Die Liste aus <code>PValue</code> Objekten.
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Der erzeugte <code>Contact</code>.
	 */
	public Contact createContact(ArrayList<PValue> pArray, JabicsUser u) {
		System.out.println(pArray);
		// Kontakt erstellen und Nickname setzen lassen
		System.err.println("create COntact");
		Contact newContact = new Contact(pArray, u);
		System.err.println("create COntact2");
		System.out.println(pArray.toString());

		// Sicherstellen, dass in demm neu erstellten Kontakt keine PValues ohne ID
		// liegen
		System.err.println("create COntact3");
		newContact = cMapper.insertContact(newContact);
		System.err.println("create COntact4");
		System.out.println("Kontakt id: " + newContact.getId());
		newContact = cMapper.insertCollaboration(u, newContact, true);
		System.out.println("Kontakt id: " + newContact.getId());

		ArrayList<PValue> testArray = new ArrayList<PValue>();

		/*
		 * neu erstellte pv von alten trennen und inserten Es wird überprüft, ob ein
		 * pValue die standardid = 0 hat, da es dann ein durch das öffnen oder einen
		 * Klick auf "Hinzufügen" erstelltes ist. Zusätzlich muss ein Wert in dem Feld
		 * eingetragen worden sein, deswegen der containsValue()
		 */
		System.err.println("create COntact4");
		for (PValue pv : pArray) {
			System.out.println("PValueID 1 " + pv.getId());
			PValue newPVal = new PValue();
			if (pv.getId() == 0 && pv.containsValue()) {
				System.out.println("PValueID 2" + pv.getId());
				switch (pv.getProperty().getType()) {
				case STRING:
					newPVal = createPValue(pv.getProperty(), pv.getStringValue(), newContact, u);
					testArray.add(newPVal);
					System.out.println("Switch String " + pv.getId());
					break;
				case DATE:
					newPVal = createPValue(pv.getProperty(), pv.getDateValue(), newContact, u);
					testArray.add(newPVal);
					break;
				case FLOAT:
					newPVal = createPValue(pv.getProperty(), pv.getFloatValue(), newContact, u);
					testArray.add(newPVal);
					break;
				case INT:
					newPVal = createPValue(pv.getProperty(), pv.getIntValue(), newContact, u);
					testArray.add(newPVal);
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
	 * Erzeugen eines <code>ContactList</code> Objekts.
	 * 
	 * @param name
	 *            der <code>String</code>, welcher den name definiert.
	 * @param cArray
	 *            die Liste von <code>Contact</code> Objekten.
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Die erstellte <code>ContactList</code>.
	 */
	public ContactList createContactList(String name, ArrayList<Contact> cArray, JabicsUser u) {
		ContactList newContactList = clMapper.insertContactList(new ContactList(cArray, name, u));
		clMapper.insertCollaboration(u, newContactList, true);
		return newContactList;
	}

	/**
	 * Erzeugen eines neuen <code>PValue</code> Objekt vom <code>Type</code>
	 * <code>String</code>
	 * 
	 * @param p
	 *            die zugeordnete <code>Property</code>.
	 * @param s
	 *            die Ausprägung des <code>String</code>.
	 * @param c
	 *            das zugeordnete <code>Contact</code> Objekt.
	 * @param u
	 *            der aktueller <code>JabicsUser</code>.
	 * @return Das erzeugte <code>PValue</code> Objekt.
	 */
	public PValue createPValue(Property p, String s, Contact c, JabicsUser u) {
		System.out.println(p.getLabel());
		System.out.println(p.getTypeInString());
		PValue newPValue = new PValue(p, s, u);
		/*
		 * erst Erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true, dann dem Besitzer das PValue freigeben, wenn dieser es noch
		 * nicht hat und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, c);
		System.out.println("newPValue " + newPValue.getId());
		pvMapper.insertCollaboration(u, newPValue, true);

		JabicsUser usertemp = uMapper.findUserByContact(c);
		if (u.getId() != usertemp.getId()) {
			pvMapper.insertCollaboration(usertemp, newPValue, false);
		}
		cMapper.updateContact(c);
		return newPValue;
	}

	/**
	 * Erzeugen eines neuen <code>PValue</code> Objekt vom <code>Type</code>
	 * <code>int</code>.
	 * 
	 * @param p
	 *            die zugeordnete <code>Property</code>.
	 * @param i
	 *            die Ausprägung des <code>int</code>.
	 * @param c
	 *            das zugeordnete <code>Contact</code> Objekt.
	 * @param u
	 *            der aktueller <code>JabicsUser</code>.
	 * @return Das erzeugte <code>PValue</code> Objekt.
	 */
	public PValue createPValue(Property p, int i, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, i, u);
		/*
		 * erst Erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true, dann dem Besitzer das PValue freigeben, wenn dieser es noch
		 * nicht hat und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, c);
		System.out.println("createPValue: Neue ID: " + newPValue.getId());
		pvMapper.insertCollaboration(u, newPValue, true);
		JabicsUser usertemp = uMapper.findUserByContact(c);
		if (u.getId() != usertemp.getId()) {
			pvMapper.insertCollaboration(usertemp, newPValue, false);
		}
		cMapper.updateContact(c);
		return newPValue;
	}

	/**
	 * Erzeugen eines neuen <code>PValue</code> Objekt vom <code>Type</code>
	 * <code>Date</code>.
	 * 
	 * @param p
	 *            die zugeordnete <code>Property</code>.
	 * @param dt
	 *            die Ausprägung des <code>Date</code>.
	 * @param c
	 *            das zugeordnete <code>Contact</code> Objekt.
	 * @param u
	 *            der aktueller <code>JabicsUser</code>.
	 * @return Das erzeugte <code>PValue</code> Objekt.
	 */
	public PValue createPValue(Property p, Date dt, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, dt, u);
		/*
		 * erst Erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true, dann dem Besitzer das PValue freigeben, wenn dieser es noch
		 * nicht hat und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, c);
		pvMapper.insertCollaboration(u, newPValue, true);
		JabicsUser usertemp = uMapper.findUserByContact(c);
		if (u.getId() != usertemp.getId()) {
			pvMapper.insertCollaboration(usertemp, newPValue, false);
		}
		cMapper.updateContact(c);
		return newPValue;
	}

	/**
	 * Erzeugen eines neuen <code>PValue</code> Objekt vom <code>Type</code>
	 * <code>float</code>.
	 * 
	 * @param p
	 *            die zugeordnete <code>Property</code>.
	 * @param f
	 *            die Ausprägung des <code>float</code>.
	 * @param c
	 *            das zugeordnete <code>Contact</code> Objekt.
	 * @param u
	 *            der aktueller <code>JabicsUser</code>.
	 * @return Das erzeugte <code>PValue</code> Objekt.
	 */
	public PValue createPValue(Property p, float f, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, f, u);
		/*
		 * erst Erstellen des PValue Objektes in der db, dann die Collaboration mit
		 * isOwner = true, dann dem Besitzer das PValue freigeben, wenn dieser es noch
		 * nicht hat und zuletzt den Contact updaten, damit dieser einen neuen
		 * Zeitstempel bekommt.
		 */
		newPValue = pvMapper.insertPValue(newPValue, c);
		pvMapper.insertCollaboration(u, newPValue, true);
		JabicsUser usertemp = uMapper.findUserByContact(c);
		if (u.getId() != usertemp.getId()) {
			pvMapper.insertCollaboration(usertemp, newPValue, false);
		}
		cMapper.updateContact(c);
		return newPValue;
	}

	/**
	 * Erzeugen eines <code>Property</code> Objekts.
	 * 
	 * @param label
	 *            der Bezeichner in Form eines <code>String</code>.
	 * @param type
	 *            der <code>Type</code> <code>STRING</code>, <code>INT</code>,
	 *            <code>DATE</code> oder <code>FLOAT</code>.
	 * @return Das erzeugte <code>Property</code> Objekt.
	 */
	public Property createProperty(String label, Type type) {
		return pMapper.insertProperty(new Property(label, type));
	}

	/**
	 * Liste aller <code>ContactList</code> Objekte eines <code>JabicsUser</code>.
	 * 
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return die Liste von <code>ContactList</code> Objekten.
	 */
	public ArrayList<ContactList> getListsOf(JabicsUser u) {

		ArrayList<ContactList> allLists = clMapper.findContactListOfUser(u);
		ArrayList<ContactList> result = new ArrayList<ContactList>();
		ArrayList<BoStatus> status = clMapper.findShareStatus(result);

		for (ContactList cl : allLists) {

			cl.setOwner(uMapper.findUserByContactList(cl));
			System.out.println("2.2 getListsOf " + cl.getListName());
			result.add(cl);

			// int i = 0;
			// if (status.size() == allLists.size()) {
			// System.out.println("BOStatus für Kontaktliste: " + cl.getId() +
			// status.get(i).toString());
			// cl.setShareStatus(status.get(i));
			// i++;
			// }

		}
		return result;
	}

	/**
	 * Auslesen aller <code>Property</code> Objekte eines <code>JabicsUser</code>.
	 * 
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Liste aus <code>Property</code> Objekten.
	 */
	public ArrayList<Property> getPropertysOfJabicsUser(JabicsUser u) {
		System.out.println("log");

		ArrayList<Property> results = new ArrayList<Property>();

		for (Contact c : cMapper.findAllContacts(u)) {
			for (PValue pv : pvMapper.findPValueForContact(c)) {
				results.add(pv.getProperty());
			}
		}
		return results;
	}

	/**
	 * Auslesen aller <code>Contact</code> Objekte eines <code>ContactList</code>
	 * Objekts.
	 * 
	 * @param cl
	 *            das <code>ContactList</code> Objekt, für welche die
	 *            <code>Contact</code> Objekte gesucht werden.
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Die Liste der <code>Contact</code> Objekte, welche in einer
	 *         <code>ContactList</code> liegen.
	 */
	public ArrayList<Contact> getContactsOfList(ContactList cl, JabicsUser u) {

		ArrayList<Contact> allC = cMapper.findContactsOfContactList(cl);
		ArrayList<Contact> result = new ArrayList<Contact>();
		System.out.println("Got all Contacts of List " + cl.toString());

		ArrayList<BoStatus> status = cMapper.findShareStatus(allC);
		int i = 0;

		for (Contact c : allC) {
			System.out.println("2.2 find Contact" + c.toString());
			// Eine Liste ist immer komplett geteilt oder nicht geteilt
			// ArrayList<JabicsUser> collaborators = cMapper.findCollaborators(c);

			if (status.size() == allC.size()) {
				System.out.println("BOStatus für Kontakt: " + c.getId() + status.get(i).toString());

				// ArrayList<BoStatus> pvStatus = pvMapper.findShareStatus(cons);
				c.setShareStatus(status.get(i));
				i++;
			}
			c.setOwner(uMapper.findUserByContact(c));
			result.add(c);
		}
		return result;
	}

	/**
	 * Auslesen aller <code>Contact</code> Objekte, welche einem
	 * <code>JabicsUser</code> geteilt sind oder die ihm gehören.
	 * 
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Die Liste der <code>Contact</code> Objekte.
	 */
	public ArrayList<Contact> getContactsOf(JabicsUser u) {
		ArrayList<Contact> cons = cMapper.findAllContacts(u);
		// ArrayList<Contact> result = new ArrayList<Contact>();
		// für jedes Kontaktobjekt werden die PValues in einer temporären ArrayList
		// gespeichert.
		System.out.println("Got all Contacts of User " + u.getId());
		// Besitzer und ShareStatus setzen
		ArrayList<BoStatus> status = cMapper.findShareStatus(cons);
		if (status.size() == cons.size()) {
			int i = 0;
			for (Contact c : cons) {
				// System.out.println("BOStatus für Kontakt: " + c.getId() +
				// status.get(i).toString());
				c.setOwner(uMapper.findUserByContact(c));
				// ArrayList<BoStatus> pvStatus = pvMapper.findShareStatus(cons);

				c.setShareStatus(status.get(i));

				i++;
			}
			// result.add(c);
		}
		return cons;
	}

	/**
	 * Auslesen des aktualisierten <code>Contact</code> Objektes.
	 * 
	 * @param c
	 * der 
	 * @return Contact c, einen aktuellen Kontakt
	 */
	public Contact getUpdatedContact(Contact c) {
		Contact updated = cMapper.findContactById(c.getId());
		ArrayList<Contact> con = new ArrayList<Contact>();
		con.add(updated);
		ArrayList<BoStatus> status = cMapper.findShareStatus(con);
		if (status.isEmpty()) {
			updated.setShareStatus(status.get(1));
		}
		return updated;
	}

	/**
	 * @return Die PValues eines Kontakts, die ein Nutzer sehen darf
	 */
	public ArrayList<PValue> getPValueOf(Contact c, JabicsUser u) {

		ArrayList<PValue> allPV = pvMapper.findPValueForContact(c);
		ArrayList<PValue> result = new ArrayList<PValue>();
		ArrayList<BoStatus> status = pvMapper.findShareStatus(allPV);
		int i = 0;
		// PValues filtern, wenn nicht geteilt und den Share Status setzen
		for (PValue pv : allPV) {
			pv.setShareStatus(status.get(i));
			i++;
			for (JabicsUser uu : pvMapper.findCollaborators(pv)) {
				if (u.getId() == uu.getId()) {
					result.add(pv);
				}
			}
		}
		return result;
	}

	public ArrayList<Contact> getAllSharedContactsOf(JabicsUser u) {
		System.err.println("Alle Geteilten Kontakte");
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : getContactsOf(u)) {

			// nicht notwendig, da in getContacsOf() schon gesetzt:
			// c.setOwner(uMapper.findUserByContact(c));
			if (c.getOwner().getId() != u.getId()) {
				result.add(c);
				System.err.println("Geteilter Kontakt 1:" + c.getId());
			}
		}
		return result;
	}

	// is this method really needed?
	public JabicsUser getUserById(int id) {
		return uMapper.findUserById(id);
	}

	/**
	 * Einen <code>Contact</code> in eine Liste einfügen. Es wird überprüft, ob der
	 * Kontakt bereits ind er Liste liegt.
	 * 
	 * @param Contact
	 *            c
	 * @param ContactList
	 *            cl
	 * @return updated ContactList
	 */

	public Contact addContactToList(Contact c, ContactList cl) {
		System.err.println("Liste ändern: " + cl.getListName());

		ArrayList<Contact> clOld = cMapper.findContactsOfContactList(cl);
		Boolean bol = true;
		for (Contact cOld : clOld) {
			if (c.getId() == cOld.getId())
				bol = false;
		}
		// Wenn der Kontakt wirklich neu in der Liste ist
		if (bol) {
			cl.addContact(c);
			// Für alle Nutzer, mit denen der Kontakt geteilt ist, eine Collab für den
			// Kontakt und dessen PValues einfügen. Die
			// Überprüfung, ob der Kontakt bereits geteilt ist, passiert in
			// addCollaboration()
			System.err.println("Kollaboratoren finden:");
			ArrayList<PValue> pVals = pvMapper.findPValueForContact(c);
			for (JabicsUser u : clMapper.findCollaborators(cl)) {
				System.err.println("Kollaborator:" + u.getUsername());
				addCollaboration(c, u);
				for (PValue pv : pVals) {
					addCollaboration(pv, u);
				}
			}
			System.err.println("Liste den Kontakt hinzufügen: " + c.getName());
			clMapper.insertContactIntoContactList(cl, c);
			return c;
		} else
			return null;

	}

	/**
	 * Entfernt einen <code>Contact</code> aus einer <code>ContactList</code>
	 * 
	 * @return Der Kontakt ohne den zu entfernenden Contact
	 */
	public Contact removeContactFromList(Contact c, ContactList cl) {
		System.err.println("Liste ändern: " + cl.getListName());

		// cl.removeContact(c);
		ArrayList<PValue> pVals = pvMapper.findPValueForContact(c);
		System.err.println("Kollaboratoren finden:");
		for (JabicsUser u : cMapper.findCollaborators(c)) {
			try {
				boolean bol = true;
				for (ContactList clAll : clMapper.findContactListOfUser(u)) {
					for (Contact cAll : getContactsOfList(cl, u)) {
						if (cAll.getId() == c.getId())
							bol = false;
					}
				}

				if (bol) {
					for (PValue pv : pVals) {
						deleteCollaboration(pv, u);
					}
					deleteCollaboration(c, u);
				}
			} catch (Exception e) {
				System.err.println(e.toString());
				deleteCollaboration(c, u);
			}
		}

		System.err.println("editorSerivce -> removeContactFromList: Kontakt in Liste löschen: " + c.getName());

		clMapper.deleteContactfromContactList(cl, c);
		return c;
	}

	/*
	 * TODO: Diese Methode wird höchstwahrscheinlich nie gebraucht, da stattdessen
	 * immer create PValue verwendet wird erstmal noch drinlassen. Jan
	 */
	public Contact addValueToContact(PValue pv, Contact c, JabicsUser u) {
		ArrayList<JabicsUser> collaborators = cMapper.findCollaborators(c);
		JabicsUser owner = uMapper.findUserByContact(c);
		/*
		 * if(u.getId() == owner.getId()) {
		 * 
		 * } for(JabicsUser u : collaborators) {
		 * 
		 * } if (.contains(u)) { c.addPValue(pv); // pvMapper.insertPValue(): }
		 */
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
	 * Löscht einen <code>Contact</code> aus der Datenbank oder entteilt ihn, wenn
	 * die Löschanfrage nicht vom Besitzer des Kontakts kommt. Löscht den Contact
	 * für alle Nutzer permanent. Kann nicht rückgängig gemacht werden.
	 * 
	 * @param Contact,
	 *            der gelöscht werden soll
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
			System.out.println("Löschversuch ohne Besitzerschaft");
			deleteCollaboration(c, ju);
		}
	}

	/**
	 * Eine <code>ContactList</code> aus der DB löschen. Löscht die Liste für alle
	 * Nutzer permanent. Kann nicht rückgängig gemacht werden. Zuerst werden die
	 * Collaborations der Liste gelöscht, dann die Kontakte aus der Kontaktliste,
	 * zuletzt wird die Liste an sich gelöscht.
	 * 
	 * @param cl
	 *            ContactList, die gelöscht werden soll
	 * @return
	 */
	public ContactList deleteContactList(ContactList cl, JabicsUser ju) {

		System.out.println("START deleting List");

		if (uMapper.findUserByContactList(cl).getId() == ju.getId()) {

			ArrayList<JabicsUser> users = clMapper.findCollaborators(cl);
			System.out.println(users.toString());

			ArrayList<Contact> contacts = cl.getContacts();

			System.out.println("EditorService -> deleteContactList(): Contacts " + contacts.toString());

			for (JabicsUser u : users) {
				System.out.println("delete Collaboration from user " + u.getId() + " and ContactList " + cl.getId());

				deleteCollaboration(cl, u);

				System.out.println("delete Collaboration from user" + u.getId());

			}

			for (Contact c : contacts) {

				System.out.println("Delete Contact " + c.getId() + " from List " + cl.getId());
				removeContactFromList(c, cl);

				System.out.println("Schleife fertig");

			}

			clMapper.deleteContactList(cl);
			System.out.println("Delete ContactList " + cl.getListName());

		} else {
			System.out.println("Löschversuch Liste ohne Besitz");
			clMapper.deleteCollaboration(cl, ju);
		}
		return cl;

	}

	/**
	 * Einen JabicsUser aus dem System löschen. Löscht alle Listen und Kontakte, die
	 * dieser Nutzer hat. PValue-Objekte werden bei Kontakten, die dem Nutzer
	 * geteilt waren, nicht gelöscht.
	 * 
	 * @param JabicsUser
	 *            u
	 */
	public void deleteUser(JabicsUser u) {
		ArrayList<ContactList> allListsOfUser = getListsOf(u);
		ArrayList<Contact> allContactsOfUser = getContactsOf(u);

		// Die Kontrolle, ob übergebener Nutzer der Eigentümer ist, regeln die
		// jeweiligen Methoden
		for (ContactList cl : allListsOfUser) {
			deleteContactList(cl, u);
		}
		for (Contact c : allContactsOfUser) {
			deleteContact(c, u);
		}

		uMapper.deleteUser(u);

		return;
	}

	/**
	 * Eine Property aus der Datenbank löschen. Es wird überprüft, ob die
	 * Eigenschaft gelöscht werden darf.
	 * 
	 * @param Property,
	 *            die gelöscht werden soll
	 */
	public void deleteProperty(Property p) {
		if (!p.isStandard()) {
			pMapper.deleteProperty(p);
		} else
			System.out.println("Tried to delete standard property. This should not have been possible");
	}

	public void deletePValue(PValue pv, Contact c) {
		System.err.println("PValue löschen" + pv.toString());
		ArrayList<JabicsUser> cols = pvMapper.findCollaborators(pv);

		System.err.println("PValue löschen2" + pv.toString());
		/**
		 * glöckchen (bzw. überhaupt sinnvoll): if(pv.getOwner().getId() == u.getId()) {
		 */
		for (JabicsUser u : cols) {
			pvMapper.deleteCollaboration(pv, u);
			pvMapper.deletePValue(pv);
		}
		System.err.println("PValue löschen3" + pv.toString());
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
		return;
	}

	/**
	 * Ein <code>PValue</code> aktualisieren, sodass es in der Datenbank konsitent
	 * gespeichert wird.
	 * 
	 * @param Ein
	 *            PropertyValue, das aktualisiert werden soll
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
	public Contact updateContact(Contact c, JabicsUser u) {

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
				boolean bol = false;
				for (PValue pvtemp : ctemp.getValues()) {
					// Wenn das gleiche PValue gemeint ist, es sich aber geändert hat, updaten
					if (pvtemp.getId() == pv.getId() && (!pvtemp.equals(pv))) {
						pvMapper.updatePValue(pv);
						bol = true;
						// Wenn die PValue schon existiert, aber nicht geändert wurde, merken
					} else if (pvtemp.getId() == pv.getId() && (pvtemp.equals(pv))) {
						bol = true;
					}
					// pvMapper.deleteCollaboration(pv, pv.getOwner());
					// pvMapper.insertCollaboration(u, pv, true);
				}
				// Wenn die PValue neu ist, dann neu erstellen
				if (!bol) {
					switch (pv.getProperty().getType()) {
					case STRING:
						pv = createPValue(pv.getProperty(), pv.getStringValue(), c, u);
						System.out.println("Switch String " + pv.getId());
						break;
					case DATE:
						pv = createPValue(pv.getProperty(), pv.getDateValue(), c, u);
						break;
					case FLOAT:
						pv = createPValue(pv.getProperty(), pv.getFloatValue(), c, u);
						break;
					case INT:
						pv = createPValue(pv.getProperty(), pv.getIntValue(), c, u);
						break;
					}
					System.err.println("PValue einfügen: " + pv.toString());
				}
			}
			c.setValues(pvMapper.findPValueForContact(c));
			c = cMapper.updateContact(c);
			c.setValues(getPValueOf(c, u));
			return c;
		} else
			c = cMapper.findContactById(c.getId());
		c.setValues(getPValueOf(c, u));
		return c;
	}

	/**
	 * Eine Freigabe zwischen einem Nutzer und einer Kontaktliste einfügen. Diese
	 * Methode nicht! beim Erstellen eines Objekts aufrufen, da isOwner false
	 * gesetzt wird.
	 * 
	 * @param ContactList,
	 *            um die es sich handelt
	 * @param Nutzer,
	 *            dem die Liste freigegeben werden soll
	 */
	public JabicsUser addCollaboration(ContactList cl, JabicsUser u) {
		ArrayList<JabicsUser> users = clMapper.findCollaborators(cl);
		boolean bol = true;
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
		return u;
	}

	/**
	 * Eine Freigabe zwischen einem Nutzer und einer ContactList einfügen. Diese
	 * Methode nicht! beim Erstellen eines Objekts aufrufen, da isOwner false
	 * gesetzt wird.
	 * 
	 * @param ContactList,
	 *            für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer,
	 *            dem der Contact freigegeben werden soll
	 */
	public void addCollaboration(Contact c, JabicsUser u) {
		System.err.println("Kollab: " + c.getName() + u.getUsername());
		ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
		boolean bol = true;
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
	 * @param PValue,
	 *            für den eine Collaboration hinzugefügt werden soll
	 * @param Nutzer,
	 *            dem das PValue freigegeben werden soll
	 */
	public void addCollaboration(PValue pv, JabicsUser u) {
		ArrayList<JabicsUser> users = pvMapper.findCollaborators(pv);
		boolean bol = true;
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

	/**
	 * Die Freigabe zwischen einem Nutzer und einem Kontakt entfernen. Wenn die
	 * Freigabe für einen Kontakt entfernt wird, der in einer geteilten Liste
	 * enthalten ist, wird die Freigabe der Liste ebenfalls entfernt
	 */
	public JabicsUser deleteCollaboration(Contact c, JabicsUser u) {
		System.out.println("Lösche Kollaboration für User " + c.getId());
		ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
		ArrayList<PValue> pVal = pvMapper.findPValueForContact(c);
		for (PValue pv : pVal) {
			for (JabicsUser uu : users) {
				if (u.getId() == uu.getId()) {
					pvMapper.deleteCollaboration(pv, u);
				}
			}
		}
		// if (users.isEmpty() || (users.size() == 1 && (users.get(0).getId() ==
		// u.getId()))) {
		// c.setShareStatus(BoStatus.NOT_SHARED);
		// }
		cMapper.deleteCollaboration(c, u);
		return u;
	}

	/**
	 * 
	 */
	public ContactList deleteCollaboration(ContactList cl, JabicsUser u) {
		clMapper.deleteCollaboration(cl, u);
		ArrayList<ContactList> cls = new ArrayList<ContactList>();
		cls.add(cl);
		ArrayList<BoStatus> status = clMapper.findShareStatus(cls);
		if (!status.isEmpty()) {
			cl.setShareStatus(status.get(0));
		}
		return cl;

	}

	/**
	 * 
	 */
	public void deleteCollaboration(PValue pv, JabicsUser u) {
		pvMapper.deleteCollaboration(pv, u);
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

	/**
	 * <p>
	 * Suche nach einem <code>PValue</code> Objekt in einem <code>ContactList</code>
	 * Objekt.
	 * </p>
	 * Diese Methode wird bei deutlich konkreteren Suchvorhaben oder Kriterien
	 * verwendet. Für eine allgemeine Suche siehe
	 * <code>searchExpressionInList</code>.
	 * 
	 * @param cl
	 *            das zu durchsuchende <code>ContactList</code> Objekt.
	 * @param pv
	 *            das <code>PValue</code> Objekt, nach welchem gesucht wird.
	 * @return Liste aller <code>Contact</code> Objekte, welche das
	 *         <code>PValue</code> Objekt enthalten.
	 */
	public ArrayList<Contact> searchInList(ContactList cl, PValue pv) {

		// Wenn die PValue leer ist, wird lediglich nach dem String-Wert in Labels und
		// Werten der Kontakte gesucht.
		// if (pv.getStringValue() == null && pv.getProperty() == null) {
		// ArrayList<Contact> contacts = cMapper.findContactsOfContactList(cl);
		// for (Contact c : contacts) {
		// c.setValues(pvMapper.findPValueForContact(c));
		// }
		// ArrayList<Contact> alc = Filter.filterContactsByString(contacts, s);
		// for (Contact c : alc) {
		// System.out.println(c.getName());
		// }
		// return alc;
		// } else {

		ArrayList<Contact> contacts = cMapper.findContactsOfContactList(cl);
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}

		// Kontakte nach Property filtern, falls gesetzt
		if (pv.getProperty().getLabel() != null) {
			System.err.println("Nach Property filtern" + pv.getProperty().getLabel());
			contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
		}
		System.err.println("Gefundene kontakte: ");
		for (Contact c : contacts) {
			System.err.println("Contact : " + c.getName());
		}
		// Kontakte nach PropertyValue filtern, falls gesetzt
		switch (pv.getPointer()) {
		case 1: {
			if (pv.getIntValue() != -2147483648) {
				System.err.println("Nach PVal filtern");
				contacts = Filter.filterContactsByInt(contacts, pv.getIntValue());
			}
			break;

		}
		case 2: {
			if (pv.getStringValue() != null) {
				System.err.println("Nach PVal filtern");
				contacts = Filter.filterContactsByString(contacts, pv.getStringValue());
			}
			break;
		}
		case 3: {
			if (pv.getDateValue() != null) {
				System.err.println("Nach PVal filtern");

				contacts = Filter.filterContactsByDate(contacts, pv.getDateValue());
			}
			break;

		}
		case 4: {
			if (pv.getFloatValue() != -99999997952f) {
				System.err.println("Nach PVal filtern");
				contacts = Filter.filterContactsByFloat(contacts, pv.getFloatValue());
			}
			break;

		}
		}

		return contacts;

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
			// for (JabicsUser uu : res) {
			// if (uu.getId() == u.getId())
			res.remove(u);
			// }
		}
		return res;
	}

	/**
	 * Auslesen aller nicht kollaborierenden <code>JabicsUser</code> Objekte an
	 * einem <code>ContactList</code> Objekt
	 * 
	 * @param cl
	 * 
	 * @return Liste der <code>JabicsUser</code> ohne Collaboration.
	 */
	public ArrayList<JabicsUser> getAllNotCollaboratingUser(ContactList cl) {
		ArrayList<JabicsUser> res = uMapper.findAllUser();
		for (JabicsUser u : clMapper.findCollaborators(cl)) {
			res.remove(u);
		}
		return res;
	}

	/**
	 * Auslesen des <code>JabicsUser</code> Objekts, welches der Besitzer eines
	 * <code>Contact</code> Objekts ist.
	 * 
	 * @param c
	 * 
	 * @return Der besitzende <code>JabicsUser</code>
	 */
	public JabicsUser getOwnerOfContact(Contact c) {
		return uMapper.findUserByContact(c);
	}

	/**
	 * Auslesen aller <code>JabicsUser</code> Objekte, welche eine Collaboration zu
	 * einem <code>Contact</code> Objekt besitzen.
	 *
	 * @param c
	 *            der <code>Contact</code> für welchen die Collaborator gesucht
	 *            werden.
	 * @return Liste aller <code>JabicsUser</code>, mit Collaboration.
	 */
	public ArrayList<JabicsUser> getCollaborators(Contact c) {
		System.out.println("Kollaboratoren holen");
		return cMapper.findCollaborators(c);
	}

	/**
	 * Auslesen aller <code>JabicsUser</code> Objekte, welche eine Collaboration zu
	 * einem <code>ContactList</code> Objekt besitzen.
	 * 
	 * @param cl
	 *            die <code>ContactList</code> für welche die Collaborator gesucht
	 *            werden.
	 * @return Liste aller <code>JabicsUser</code>, mit Collaboration.
	 */
	public ArrayList<JabicsUser> getCollaborators(ContactList cl) {
		ArrayList<JabicsUser> result = clMapper.findCollaborators(cl);
		JabicsUser owner = uMapper.findUserByContactList(cl);
		for (JabicsUser u : result) {
			System.out.println(u.getId() + u.getEmail());
			// if(u.getId() == owner.getId()) result.remove(u);
		}
		return result;
	}

	/**
	 * Auslesen aller <code>JabicsUser</code> Objekte, welche eine Collaboration zu
	 * einem <code>PValue</code> Objekt besitzen.
	 * 
	 * @param pv
	 *            die <code>PValue</code> für welche die Collaborator gesucht werde.
	 * @return Liste aller <code>JabicsUser</code>, mit Collaboration.
	 */
	public ArrayList<JabicsUser> getCollaborators(PValue pv) {
		return pvMapper.findCollaborators(pv);
	}

	/**
	 * Auslesen aller <code>JabicsUser</code> Objekte.
	 * 
	 * @return Liste aller <code>JabicsUser</code>.
	 */
	public ArrayList<JabicsUser> getAllUsers() {
		return uMapper.findAllUser();
	}

	/**
	 * Auslesen aller Standard <code>Property</code> Objekte.
	 * 
	 * @return Liste von Standard <code>Property</code>.
	 */
	public ArrayList<Property> getStandardProperties() {
		return pMapper.findAllStandardPropertys();
	}

	/**
	 * 
	 */
	public void initialise() {
		// TODO Auto-generated method stub
	}
}
