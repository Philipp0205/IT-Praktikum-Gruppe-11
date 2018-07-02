package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>
 * Die Klasse <code>ContactList</code> implementiert Kontaktlisten in Jabics. In einer
 * Kontaktliste sind mehrere <code>Contact</code> Objekte gespeichert.
 * </p>
 * 
 * @author Anders
 *
 */
public class ContactList extends BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Name einer Instanz dieser Klasse.
	 */
	private String listName;

	/**
	 * Share-Status einer Instanz dieser Klasse.
	 */
	private BoStatus shareStatus = BoStatus.NOT_SHARED;

	/**
	 * <code>Contact</code> Objekte welche in einer Instanz dieser Klasse liegen.
	 */
	ArrayList<Contact> contacts = new ArrayList<Contact>();

	/**
	 * Konstruktor, welcher den Konstruktor seiner Superklasse aufruft.
	 */
	public ContactList() {
		super();
	}

	/**
	 * 
	 * 
	 * @param al
	 */
	public ContactList(ArrayList<Contact> al) {
		this();
		this.contacts = al;
	}

	/**
	 * 
	 * 
	 * @param al
	 * @param u
	 */
	public ContactList(ArrayList<Contact> al, JabicsUser u) {
		this(al);
		this.owner = u;
	}

	/**
	 * 
	 * 
	 * @param al
	 * @param ln
	 * @param u
	 */
	public ContactList(ArrayList<Contact> al, String ln, JabicsUser u) {
		this(al, u);
		this.listName = ln;
	}

	/**
	 * Textuelle Repräsentation des <code>Contact</code> Objekts durch den Name und
	 * wenn dieser nicht gesetzt ist durch die ID.
	 */
	@Override
	public String toString() {
		if (listName != null) {
			return listName;
		} else
			return Integer.toString(this.id);
	}

	/**
	 * Ein <code>Contact</code> Objekt einer Instanz dieser Klasse
	 * 
	 * @param <code>Contact</code>
	 */
	public void addContact(Contact c) {
		contacts.add(c);
	}

	/**
	 * Entfernen eines Kontakts aus der Liste
	 */
	public void removeContact(Contact c) {
		contacts.remove(c);
	}

	/**
	 * Fügt alle Kontakte in einer ArrayList<Contact> zur Liste hinzu
	 */
	public void addContacts(ArrayList<Contact> conts) {
		this.contacts.addAll(conts);
	}

	/**
	 * Auslesen einer ArrayList mit alle <code>Contact</code> Objekten, welche in
	 * einer <code>ContactList</code> liegen.
	 * 
	 * @return contacts
	 */
	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	/**
	 * Setzen aller <code>Contact</code> Objekte, die einer <code>ContactList</code>
	 * angehören.
	 * 
	 * @param contacts
	 */
	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}

	/**
	 * Auslesen des Listennamens.
	 * 
	 * @return
	 */
	public String getListName() {
		return listName;
	}

	/**
	 * Setzen des Listennamens.
	 * 
	 * @param listName
	 */
	public void setListName(String listName) {
		this.listName = listName;
	}

	/**
	 * Auslesen des Share-Status.
	 * 
	 * @return
	 */
	public BoStatus getShareStatus() {
		return shareStatus;
	}

	/**
	 * Setzen des Share-Status
	 * 
	 * @param shareStatus
	 */
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}
}