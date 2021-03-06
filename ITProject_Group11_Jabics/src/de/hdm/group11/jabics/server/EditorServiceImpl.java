package de.hdm.group11.jabics.server;

import java.util.Date;
import java.util.ArrayList;

import de.hdm.group11.jabics.server.db.*;
import de.hdm.group11.jabics.shared.bo.*;
import de.hdm.group11.jabics.shared.EditorService;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Die Klasse <code>EditorServiceImpl</code> implementiert die Applikationslogik
 * für die Klasse <code>EditorService</code>. Sie stellt die Logik zur
 * Verfügung, die bei einem RPC aufgerufen wird und gibt die angefragten Objekte
 * zurück. Die Methoden in dieser Klasse sind alphabetisch geordnet.
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
	private ContactMapper cMapper = ContactMapper.contactMapper();

	/**
	 * Instanz der Klasse <code>ContactListMapper</code>
	 */
	private ContactListMapper clMapper = ContactListMapper.contactListMapper();

	/**
	 * Instanz der Klasse <code>PValueMapper</code>
	 */
	private PValueMapper pvMapper = PValueMapper.pValueMapper();

	/**
	 * Instanz der Klasse <code>PropertyMapper</code>
	 */
	private PropertyMapper pMapper = PropertyMapper.propertyMapper();

	/**
	 * Instanz der Klasse <code>UserMapper</code>
	 */
	private UserMapper uMapper = UserMapper.userMapper();

	/**
	 * Default Konstruktor
	 */
	public EditorServiceImpl() {
	}

	/**
	 * <code>Contact</code> Objekt einem <code>JabicsUser</code> freigeben.
	 * 
	 * @param c
	 *            der <code>Contact</code> für welchen die Freigabe erfolgen soll.
	 * @param u
	 *            der <code>JabicsUser</code> für welchen die Freigabe erfolgen
	 *            soll.
	 */
	public void addCollaboration(Contact c, JabicsUser u) {
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
			cMapper.insertCollaboration(u, c, false);
		} else
			return;
	}

	/**
	 * <code>ContactList</code> Objekt einem <code>JabicsUser</code> freigeben.
	 * 
	 * @param cl
	 *            die <code>ContactList</code> für welche die Freigabe erfolgen
	 *            soll.
	 * @param u
	 *            der <code>JabicsUser</code> für welchen die Freigabe erfolgen
	 *            soll.
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
				for (PValue pv : pvMapper.findPValueForContact(c)) {
					addCollaboration(pv, u);
				}
			}
		}
		return u;
	}

	/**
	 * <code>PValue</code> Objekt einem <code>JabicsUser</code> freigeben.
	 * 
	 * @param pv
	 *            die <code>PValue</code> für welches die Freigabe erfolgen soll.
	 * @param u
	 *            der <code>JabicsUser</code> für welchen die Freigabe erfolgen
	 *            soll.
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
	 * Ein <code>Contact</code> Objekt einer <code>ContactList</code> hinzufügen.
	 * 
	 * @param c
	 *            das <code>Contact</code> Objekt welches einer
	 *            <code>ContactList</code> hinzugefügt werden soll.
	 * @param cl
	 *            die <code>ContactList</code> zu welcher der <code>Contact</code>
	 *            hinzugefügt werden soll.
	 * @return Der <code>Contact</code> welcher der <code>ContactList</code>
	 *         hinzugefügt wurde.
	 */
	public Contact addContactToList(Contact c, ContactList cl) {

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
			ArrayList<PValue> pVals = pvMapper.findPValueForContact(c);
			for (JabicsUser u : clMapper.findCollaborators(cl)) {
				addCollaboration(c, u);
				for (PValue pv : pVals) {
					addCollaboration(pv, u);
				}
			}
			clMapper.insertContactIntoContactList(cl, c);
			return c;
		} else
			return null;

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
		// Kontakt erstellen und Nickname setzen lassen
		Contact newContact = new Contact(pArray, u);

		// Sicherstellen, dass in demm neu erstellten Kontakt keine PValues ohne ID
		// liegen
		newContact = cMapper.insertContact(newContact);
		newContact = cMapper.insertCollaboration(u, newContact, true);

		ArrayList<PValue> testArray = new ArrayList<PValue>();

		/*
		 * neu erstellte pv von alten trennen und inserten Es wird überprüft, ob ein
		 * pValue die standardid = 0 hat, da es dann ein durch das öffnen oder einen
		 * Klick auf "Hinzufügen" erstelltes ist. Zusätzlich muss ein Wert in dem Feld
		 * eingetragen worden sein, deswegen der containsValue()
		 */
		for (PValue pv : pArray) {
			PValue newPVal = new PValue();
			if (pv.getId() == 0 && pv.containsValue()) {
				switch (pv.getProperty().getType()) {
				case STRING:
					newPVal = createPValue(pv.getProperty(), pv.getStringValue(), newContact, u);
					testArray.add(newPVal);
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
					// newContact.addPValue(newPVal);
					testArray.add(newPVal);
				} else
					System.out.println("Beim Erstellen neuer PValues ist etwas schiefgegangen");
			} else
				System.out.println("Beim Erstellen eines Kontakts war eine Id oder ein Value nicht gesetzt!");
		}
		newContact.getValues().clear();

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
	 * <code>String</code>
	 * 
	 * @param p
	 *            die zugeordnete <code>Property</code>.
	 * @param s
	 *            die Ausprägung des <code>String</code>.
	 * @param c
	 *            das zugeordnete <code>Contact</code> Objekt.
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Das erzeugte <code>PValue</code> Objekt.
	 */
	public PValue createPValue(Property p, String s, Contact c, JabicsUser u) {
		PValue newPValue = new PValue(p, s, u);
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
	 * <p>
	 * Teilhaberschaft eines <code>JabicsUser</code> an einem <code>Contact</code>
	 * entfernen.
	 * </p>
	 * <b>Folge</b> Wenn der <code>Contact</code> in einer dem
	 * <code>JabicsUser</code> geteilten <code>ContactList</code> liegt, wird die
	 * Teilhaberschaft an dieser ebenfalls entfernt.
	 * 
	 * @param c
	 *            das <code>Contact</code> Objekt, zu welchem die Teilhaberschaft
	 *            entzogen werden soll.
	 * @param u
	 *            der <code>JabicsUser</code>, welchem die Teilhaberschaft entzogen
	 *            werden soll.
	 * @return Der <code>JabicsUser</code> welchem der <code>Contact</code> enzogen
	 *         wurde.
	 */
	public JabicsUser deleteCollaboration(Contact c, JabicsUser u) {
		if (c != null && u != null) {
			ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
			ArrayList<PValue> pVal = pvMapper.findPValueForContact(c);
			JabicsUser owner = uMapper.findUserByContact(c);
			if (owner.getId() != u.getId()) {
				for (PValue pv : pVal) {
					for (JabicsUser uu : users) {
						if (u.getId() == uu.getId()) {
							pvMapper.deleteCollaboration(pv, u);
						}
					}
				}

				cMapper.deleteCollaboration(c, u);
			}
		}
		return u;
	}

	/**
	 * Teilhaberschaft eines <code>JabicsUser</code> an einer
	 * <code>ContactList</code> entfernen. Alle Kontakte, die dem Nutzer nicht
	 * ausserhalb der Liste noch einmal geteilt sind,w erden ebenfalls entteilt.
	 * 
	 * @param cl
	 *            das <code>ContactList</code> Objekt, zu welchem die
	 *            Teilhaberschaft entzogen werden soll.
	 * @param u
	 *            der <code>JabicsUser</code>, welchem die Teilhaberschaft entzogen
	 *            werden soll.
	 * @return Die <code>ContactList</code>
	 */
	public ContactList deleteCollaboration(ContactList cl, JabicsUser u) {
		ArrayList<Contact> contactsInList = cMapper.findContactsOfContactList(cl);

		for (Contact c : contactsInList) {
			try {
				int i = 0;
				for (ContactList clAll : clMapper.findContactListOfUser(u)) {
					for (Contact cAll : getContactsOfList(clAll, u)) {
						if (cAll.getId() == c.getId()) {
							i++;
						}
					}
				}
				if ((i < 2) && (u.getId() != uMapper.findUserByContactList(cl).getId())) {
					// Damit werden auch alle Collabs an PValues gelöscht
					deleteCollaboration(c, u);
				}
			} catch (Exception e) {
				System.err.println("Failed to delete Collaboration for ContactList: " + e.toString());
			}
		}
		clMapper.deleteCollaboration(cl, u);

		ArrayList<BoStatus> statusContacts = cMapper.findShareStatus(contactsInList);
		if (statusContacts != null && contactsInList != null) {
			if (statusContacts.size() == contactsInList.size()) {
				int i = 0;
				for (Contact c : contactsInList) {
					// ArrayList<BoStatus> pvStatus = pvMapper.findShareStatus(cons);
					c.setShareStatus(statusContacts.get(i));
					c.setOwner(uMapper.findUserByContact(c));
					cl.addContact(c);
					i++;
				}
			}
			
			// Den Share Status der Liste neu setzen
			ArrayList<ContactList> cls = new ArrayList<ContactList>();
			cls.add(cl);
			ArrayList<BoStatus> status = clMapper.findShareStatus(cls);
			if (!statusContacts.isEmpty()) {
				cl.setShareStatus(status.get(0));
			}
			
		}
		return cl;
	}

	/**
	 * Teilhaberschaft eines <code>JabicsUser</code> an einem <code>PValue</code>
	 * entfernen.
	 * 
	 * @param pv
	 *            das <code>PValue</code> Objekt, zu welchem die Teilhaberschaft
	 *            entzogen werden soll.
	 * @param u
	 *            der <code>JabicsUser</code>, welchem die Teilhaberschaft entzogen
	 *            werden soll.
	 */
	public void deleteCollaboration(PValue pv, JabicsUser u) {
		pvMapper.deleteCollaboration(pv, u);
	}

	/**
	 * Ein <code>Contact</code> Objekt löschen.
	 * 
	 * @param c
	 *            das zu löschende <code>Contact</code> Objekt.
	 * @param ju
	 *            der aktuelle <code>JabicsUser</code>.
	 */
	public void deleteContact(Contact c, JabicsUser ju) {


		if (c != null && ju != null) {
			if (uMapper.findUserByContact(c).getId() == ju.getId()) {
				ArrayList<JabicsUser> users = cMapper.findCollaborators(c);
				ArrayList<PValue> pvalues = pvMapper.findPValueForContact(c);
				ArrayList<ContactList> allCL = clMapper.findContactListByContact(c);

				for (JabicsUser u : users) {
					ArrayList<PValue> userValues = getPValueOf(c, u);
					for (PValue pvUser : userValues) {
						pvMapper.deleteCollaboration(pvUser, u);
					}
					cMapper.deleteCollaboration(c, u);
				}

				for (PValue pv : pvalues) {
					pvMapper.deletePValue(pv);
				}
				for (ContactList cl : allCL) {
					clMapper.deleteContactfromContactList(cl, c);
				}
				cMapper.deleteContact(c);
			} else {
				ArrayList<PValue> pvalues = getPValueOf(c, ju);
				for (PValue pv : pvalues) {
					pvMapper.deleteCollaboration(pv, ju);
				}
				deleteCollaboration(c, ju);
			}
		}
		return;
	}

	/**
	 * Ein <code>ContactList</code> Objekt löschen.
	 * 
	 * @param cl
	 *            das zu löschende <code>ContactList</code> Objekt.
	 * @param ju
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return das gelöschte <code>ContactList</code> Objekt.
	 */
	public ContactList deleteContactList(ContactList cl, JabicsUser ju) {

		if (uMapper.findUserByContactList(cl).getId() == ju.getId()) {

			ArrayList<JabicsUser> users = clMapper.findCollaborators(cl);
			JabicsUser owner = uMapper.findUserByContactList(cl);

			ArrayList<Contact> contacts = cl.getContacts();


			for (JabicsUser u : users) {

				deleteCollaboration(cl, u);

			}

			for (Contact c : contacts) {

				removeContactFromList(c, cl);
			}

			clMapper.deleteContactList(cl);

		} else {
			clMapper.deleteCollaboration(cl, ju);
		}
		return cl;

	}

	/**
	 * Ein <code>Property</code> Objekt löschen.
	 * 
	 * @param p
	 *            das zu löschende <code>Property</code> Objekt.
	 */
	public void deleteProperty(Property p) {
		if (!p.isStandard()) {
			pMapper.deleteProperty(p);
		}
	}

	/**
	 * Ein <code>PValue</code> Objekt löschen.
	 * 
	 * @param pv
	 *            das zu löschende <code>Property</code> Objekt.
	 * @param c
	 *            das <code>Contact</code> Objekt zu welchem das <code>PValue</code>
	 *            Objekt gehört.
	 */
	public void deletePValue(PValue pv, Contact c) {
		ArrayList<JabicsUser> cols = pvMapper.findCollaborators(pv);
		/**
		 * glöckchen (bzw. überhaupt sinnvoll): if(pv.getOwner().getId() == u.getId()) {
		 */
		for (JabicsUser u : cols) {
			pvMapper.deleteCollaboration(pv, u);
			pvMapper.deletePValue(pv);
		}
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
	 * <p>
	 * Ein <code>JabicsUser</code> Objekt löschen.
	 * </p>
	 * Alle <code>PValue</code>, <code>Contact</code> und <code>ContactList</code>
	 * Objekte des <code>JabicsUser</code> werden dabei gelöscht.
	 * 
	 * @param u
	 *            das zu löschende <code>JabicsUser</code> Objekt.
	 */
	public void deleteUser(JabicsUser u) {
		ArrayList<ContactList> allListsOfUser = getListsOf(u);
		ArrayList<Contact> allContactsOfUser = getContactsOf(u);

		if (allContactsOfUser != null) {
			// Die Kontrolle, ob übergebener Nutzer der Eigentümer ist, regeln die
			// jeweiligen Methoden
			// Erst alle Kontakte löschen.
			if (!allContactsOfUser.isEmpty()) {
				for (Contact c : allContactsOfUser) {
					deleteContact(c, u);
				}
			}
		}
		if (allListsOfUser != null) {
			if (!allListsOfUser.isEmpty()) {
				for (ContactList cl : allListsOfUser) {
					deleteContactList(cl, u);
				}
			}
		}
		uMapper.deleteUser(u);
		return;

	}

	/**
	 * Auslesen aller nicht kollaborierenden <code>JabicsUser</code> Objekte an
	 * einem <code>Contact</code> Objekt
	 * 
	 * @param c
	 *            <code>Contact</code> für welchen die nicht kollaborierenden
	 *            <code>JabicsUser</code> benötigt werden.
	 * @return Liste der <code>JabicsUser</code> ohne Collaboration.
	 */
	public ArrayList<JabicsUser> getAllNotCollaboratingUser(Contact c) {
		ArrayList<JabicsUser> result = new ArrayList<JabicsUser>();
		ArrayList<JabicsUser> allUser = uMapper.findAllUser();
		for (JabicsUser u : allUser) {
			boolean bol = true;
			for (JabicsUser uu : cMapper.findCollaborators(c)) {
				if (u.getId() == uu.getId())
					bol = false;
			}
			if (bol)
				result.add(u);
		}
		return result;
	}

	/**
	 * Auslesen aller nicht kollaborierenden <code>JabicsUser</code> Objekte an
	 * einem <code>ContactList</code> Objekt
	 * 
	 * @param cl
	 *            <code>ContactList</code> für welche die nicht kollaborierenden
	 *            <code>JabicsUser</code> benötigt werden.
	 * @return Liste der <code>JabicsUser</code> ohne Collaboration.
	 */
	public ArrayList<JabicsUser> getAllNotCollaboratingUser(ContactList cl) {
		ArrayList<JabicsUser> result = new ArrayList<JabicsUser>();
		ArrayList<JabicsUser> allUser = uMapper.findAllUser();
		for (JabicsUser u : allUser) {
			boolean bol = true;
			for (JabicsUser uu : clMapper.findCollaborators(cl)) {
				if (u.getId() == uu.getId())
					bol = false;
			}
			if (bol)
				result.add(u);
		}
		return result;
	}

	/**
	 * Auslesen aller <code>Contact</code> Objekte, welche einem
	 * <code>JabicsUser</code> geteilt sind oder die ihm gehören.
	 * 
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Die Liste der sichtbaren <code>Contact</code> Objekte.
	 */
	public ArrayList<Contact> getAllSharedContactsOf(JabicsUser u) {
		ArrayList<Contact> result = new ArrayList<Contact>();
		ArrayList<Contact> allC = getContactsOf(u);
		if (allC != null) {
			for (Contact c : getContactsOf(u)) {

				// nicht notwendig, da in getContacsOf() schon gesetzt:
				// c.setOwner(uMapper.findUserByContact(c));
				if (c.getOwner().getId() != u.getId()) {
					result.add(c);
				}
			}
			return result;
		} else
			return null;
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
	 * Auslesen aller <code>JabicsUser</code> Objekte, welche eine Collaboration zu
	 * einem <code>Contact</code> Objekt besitzen.
	 *
	 * @param c
	 *            der <code>Contact</code> für welchen die Collaborator gesucht
	 *            werden.
	 * @return Liste aller <code>JabicsUser</code>, mit Collaboration.
	 */
	public ArrayList<JabicsUser> getCollaborators(Contact c) {
		ArrayList<JabicsUser> allCollabs = cMapper.findCollaborators(c);
		ArrayList<JabicsUser> result = new ArrayList<JabicsUser>();
		JabicsUser owner = uMapper.findUserByContact(c);
		for (JabicsUser u : allCollabs) {
			if (u.getId() != owner.getId())
				result.add(u);
		}
		return result;
	}

	/**
	 * Auslesen aller <code>JabicsUser</code> Objekte, welche eine Collaboration zu
	 * einem <code>ContactList</code> Objekt besitzen. Der Besitzer wird nicht
	 * zurückgegeben.
	 * 
	 * @param cl
	 *            die <code>ContactList</code> für welche die Collaborator gesucht
	 *            werden.
	 * @return Liste aller <code>JabicsUser</code>, mit Collaboration.
	 */
	public ArrayList<JabicsUser> getCollaborators(ContactList cl) {
		ArrayList<JabicsUser> allCollabs = clMapper.findCollaborators(cl);
		ArrayList<JabicsUser> result = new ArrayList<JabicsUser>();
		JabicsUser owner = uMapper.findUserByContactList(cl);
		for (JabicsUser u : allCollabs) {
			if (u.getId() != owner.getId())
				result.add(u);
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
		// Besitzer und ShareStatus setzen
		ArrayList<BoStatus> status = cMapper.findShareStatus(cons);
		if (status != null && cons != null) {
			if (status.size() == cons.size()) {
				int i = 0;
				for (Contact c : cons) {
					c.setOwner(uMapper.findUserByContact(c));
					c.setShareStatus(status.get(i));

					i++;
				}
			}
			return cons;
		} else
			return null;
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

		ArrayList<BoStatus> status = cMapper.findShareStatus(allC);
		int i = 0;

		for (Contact c : allC) {
			// Eine Liste ist immer komplett geteilt oder nicht geteilt
			// ArrayList<JabicsUser> collaborators = cMapper.findCollaborators(c);

			if (status.size() == allC.size()) {
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
	 * Liste aller <code>ContactList</code> Objekte eines <code>JabicsUser</code>.
	 * 
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return die Liste von <code>ContactList</code> Objekten.
	 */
	public ArrayList<ContactList> getListsOf(JabicsUser u) {

		ArrayList<ContactList> allLists = clMapper.findContactListOfUser(u);
		ArrayList<ContactList> result = new ArrayList<ContactList>();
		ArrayList<BoStatus> status = clMapper.findShareStatus(allLists);

		if (allLists != null) {
			int i = 0;
			for (ContactList cl : allLists) {

				cl.setOwner(uMapper.findUserByContactList(cl));
				result.add(cl);
				if (status != null) {
					if (status.size() == allLists.size()) {
						cl.setShareStatus(status.get(i));
					}
				}
				i++;
			}
			return result;
		} else
			return null;
	}

	/**
	 * Auslesen des <code>JabicsUser</code> Objekts, welches der Besitzer eines
	 * <code>Contact</code> Objekts ist.
	 * 
	 * @param c
	 *            <code>Contact</code> Objekt für welches der Besitzer ermittelt
	 *            werden soll.
	 * @return Der besitzende <code>JabicsUser</code>
	 */
	public JabicsUser getOwnerOfContact(Contact c) {
		return uMapper.findUserByContact(c);
	}

	/**
	 * Auslesen des <code>JabicsUser</code> Objekts, welches der Besitzer eines
	 * <code>ContactList</code> Objekts ist.
	 * 
	 * @param cl <code>ContactList</code> Objekt für welches der Besitzer ermittelt
	 *           werden soll.
	 * @return Der besitzende <code>JabicsUser</code>
	 */
	public JabicsUser getOwnerOfContactList(ContactList cl) {
		return uMapper.findUserByContactList(cl);
	}

	/**
	 * Auslesen aller <code>Property</code> Objekte eines <code>JabicsUser</code>.
	 * 
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Liste aus <code>Property</code> Objekten.
	 */
	public ArrayList<Property> getPropertysOfJabicsUser(JabicsUser u) {
		ArrayList<Property> results = new ArrayList<Property>();

		for (Contact c : cMapper.findAllContacts(u)) {
			for (PValue pv : pvMapper.findPValueForContact(c)) {
				results.add(pv.getProperty());
			}
		}
		return results;
	}

	/**
	 * Auslesen aller <code>PValue</code> Objekte eines <code>Contact</code>, welche
	 * einem <code>JabicsUser</code> geteilt sind oder die ihm gehören.
	 * 
	 * @param c
	 *            die <code>Contact</code> Objekt für welches die
	 *            <code>PValue</code> Objekte benötigt werden.
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Liste der sichtbaren <code>PValue</code> Objekte.
	 */
	public ArrayList<PValue> getPValueOf(Contact c, JabicsUser u) {
		if (c != null && u != null) {

			ArrayList<PValue> allPV = pvMapper.findPValueForContact(c);
			ArrayList<BoStatus> status = pvMapper.findShareStatus(allPV);
			ArrayList<PValue> result = new ArrayList<PValue>();
			int i = 0;

			// Im Fall, dass der Owner des Kontakt anfragt, schnellere Abfertigung
			JabicsUser owner = uMapper.findUserByContact(c);
			if (owner != null) {

				if (u.getId() == owner.getId()) {

					for (PValue pv : allPV) {
						pv.setShareStatus(status.get(i));
						result.add(pv);
						i++;
					}
					return result;
				} else {
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
			} else {
				return result;
			}

		} else
			return null;
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
	 * Auslesen des aktualisierten <code>Contact</code> Objektes.
	 * 
	 * @param c
	 *            der <code>Contact</code>, der aktualisiert wurde. * @return Den
	 *            aktuellen <code>Contact</code>.
	 */
	public Contact getUpdatedContact(Contact c) {
		Contact updated = cMapper.findContactById(c.getId());
		JabicsUser owner = uMapper.findUserByContact(c);
		ArrayList<PValue> pvalues = getPValueOf(c, owner);
		ArrayList<Contact> con = new ArrayList<Contact>();
		con.add(updated);
		ArrayList<BoStatus> status = cMapper.findShareStatus(con);
		updated.setValues(pvalues);
		try {
			if (!status.isEmpty()) {
				updated.setShareStatus(status.get(0));
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return updated;
	}

	public void initialise() {
	}

	/**
	 * Entfernen eines <code>Contact</code> aus einer <code>ContactList</code>.
	 * 
	 * @param c
	 *            der zu entfernende <code>Contact</code>.
	 * @param cl
	 *            die <code>ContactList</code> aus welcher der <code>Contact</code>
	 *            entfernt werden soll.
	 * @return Das entfernte <code>Contact</code> Objekt.
	 */
	public Contact removeContactFromList(Contact c, ContactList cl) {

		// cl.removeContact(c);
		ArrayList<PValue> pVals = pvMapper.findPValueForContact(c);
		for (JabicsUser u : cMapper.findCollaborators(c)) {
			try {
				boolean bol = true;
				for (ContactList clAll : clMapper.findContactListOfUser(u)) {
					for (Contact cAll : getContactsOfList(clAll, u)) {
						if (cAll.getId() == c.getId())
							bol = false;
					}
				}

				if (bol && (u.getId() != uMapper.findUserByContactList(cl).getId())) {
					for (PValue pv : pVals) {
						deleteCollaboration(pv, u);
					}
					deleteCollaboration(c, u);
				}
			} catch (Exception e) {
				deleteCollaboration(c, u);
			}
		}

		clMapper.deleteContactfromContactList(cl, c);
		return c;
	}

	/**
	 * Diese Methode wird verwendet, wenn der Datentyp des Suchkriteriums nicht
	 * bekannt ist! Suche nach allen <code>Contacts</code> in einer
	 * <code>ContactList</code>, die den mitgegebenen String als Property oder
	 * PropertyValue enthalten.
	 * 
	 * Suche nach einem <code>PValue</code> Objekt oder <code>Property</code> Objekt
	 * eines <code>Contact</code> in einem <code>ContactList</code> Objekt, welche
	 * einen bestimmten Wert als <code>String</code> besitzen.
	 * 
	 * @param s
	 *            der Wert des gesuchten <code>String</code>
	 * @param cl
	 *            die <code>ContactList</code> in der gesucht wird.
	 * @return Liste aller <code>Contact</code> Objekte, welche <code>PValue</code>
	 *         oder <code>Property</code> Objekte enthalten, die das Suchkriterium
	 *         erfüllen.
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
	 * Suche nach allen <code>Contact</code> Objekten eines <code>JabicsUser</code>,
	 * deren <code>Property</code> und <code>PValue</code> einen bestimmten
	 * <code>String</code> enthalten.
	 * 
	 * @param s
	 *            der Wert des <code>String</code>.
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Liste der <code>Contact</code> Objekte, welche den gesuchten
	 *         <code>String</code> enthalten.
	 */
	public ArrayList<Contact> searchForContactByExpression(String s, JabicsUser u) {
		ContactList cl = new ContactList(getContactsOf(u));
		return this.searchExpressionInList(s, cl);
	}

	/**
	 * <p>
	 * Suche nach einem <code>PValue</code> Objekt eines <code>Contact</code> in
	 * einem <code>ContactList</code> Objekt.
	 * </p>
	 * Diese Methode wird bei deutlich konkreteren Suchvorhaben oder Kriterien
	 * verwendet. Für eine allgemeine Suche siehe {@link searchExpressionInList}.
	 * 
	 * @param cl
	 *            das zu durchsuchende <code>ContactList</code> Objekt.
	 * @param pv
	 *            das <code>PValue</code> Objekt, nach welchem gesucht wird.
	 * @return Liste aller <code>Contact</code> Objekte, welche das
	 *         <code>PValue</code> Objekt enthalten.
	 */
	public ArrayList<Contact> searchInList(ContactList cl, PValue pv, JabicsUser u) {

		if (pv != null) {

			ArrayList<Contact> contacts = cMapper.findContactsOfContactList(cl);
			for (Contact c : contacts) {
				c.setValues(pvMapper.findPValueForContact(c));
			}

			if (pv.getProperty() != null) {
				// Kontakte nach Property filtern, falls gesetzt
				if (pv.getProperty().getLabel() != null) {
					contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
				}
			}

			// Kontakte nach PropertyValue filtern, falls gesetzt
			switch (pv.getPointer()) {
			case 1: {
				if (pv.getIntValue() != -2147483648) {
					contacts = Filter.filterContactsByInt(contacts, pv.getIntValue());
				}
				break;

			}
			case 2: {
				if (pv.getStringValue() != null) {
					contacts = Filter.filterContactsByString(contacts, pv.getStringValue());
				}
				break;
			}
			case 3: {
				if (pv.getDateValue() != null) {

					contacts = Filter.filterContactsByDate(contacts, pv.getDateValue());
				}
				break;

			}
			case 4: {
				if (pv.getFloatValue() != -99999997952f) {
					contacts = Filter.filterContactsByFloat(contacts, pv.getFloatValue());
				}
				break;

			}
			}

			ArrayList<BoStatus> status = cMapper.findShareStatus(contacts);
			if (status != null && contacts != null) {
				if (status.size() == contacts.size()) {
					int i = 0;
					for (Contact c : contacts) {
						c.setOwner(uMapper.findUserByContact(c));
						c.setValues(getPValueOf(c, u));
						c.setShareStatus(status.get(i));
						i++;
					}
				}
			}
			return contacts;
		} else
			return null;
	}

	/**
	 * Auslesen ob <code>JabicsUser</code>, Besitzer oder Teilhaber einer
	 * <code>ContactList</code> ist.
	 * 
	 * @param u
	 *            das <code>JabicsUser</code> Objekt.
	 * @param cl
	 *            das <code>ContactList</code> Objekt.
	 * @return Liste der <code>Contact</code> Objekte
	 */
	public ArrayList<Contact> searchInList(JabicsUser u, ContactList cl) {
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : cl.getContacts()) {
			// Wenn der Contact Owner der übergebene Nutzer ist oder ein Collaborator an
			// einem der Kontakte der übergebene Nutzer ist dann wird der Kontakt
			// zurückgegeben
			if (c.getOwner().getId() == u.getId() || cMapper.findCollaborators(c).contains(u)) {
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * Aktualisieren eines <code>Contact</code>.
	 * 
	 * @param c
	 *            der <code>Contact</code> der aktualisiert werden soll.
	 * @param u
	 *            der aktuelle <code>JabicsUser</code>.
	 * @return Der aktualisierte <code>Contact</code>.
	 */
	public Contact updateContact(Contact c, JabicsUser u) {

		// Nickname neu setzen
		c.updateNickname();

		Contact ctemp = cMapper.findContactById(c.getId());
		ctemp.setValues(pvMapper.findPValueForContact(ctemp));

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
	 * Aktualisieren einer <code>ContactList</code>.
	 * 
	 * @param cl
	 *            die <code>ContactList</code> die aktualisiert werden soll.
	 * @return Die aktualisierte <code>ContactList</code>.
	 */
	public ContactList updateContactList(ContactList cl) {
		ContactList cltemp = clMapper.findContactListById(cl.getId());
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
					addContactToList(c, cl);
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
					removeContactFromList(ctemp, cl);
				}
			}
			return clMapper.updateContactList(cl);
		} else
			return clMapper.findContactListById(cl.getId());
	}

	/**
	 * Aktualisieren eines <code>PValue</code>.
	 * 
	 * @param pv
	 *            der <code>PValue</code> die aktualisiert werden soll.
	 * @return Die aktualisierte <code>PValue</code>.
	 */
	public PValue updatePValue(PValue pv) {
		PValue pvtemp = pvMapper.findPValueById(pv.getId());
		if (pv != pvtemp) {
			PValue p = pvMapper.updatePValue(pv);
			return p;
		} else
		return pvMapper.findPValueById(pv.getId());
	}
}
