package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Diese Klasse implementiert Kontaktlisten in Jabics. Kontaktlisten haben einen
 * Namen und ein Feld aus Kontakten. Diese können eine nicht definierte Menge an
 * <code>Contact</code> Objekten speichern. Kontakte können einer Liste mittels
 * addContact() und removeContact() hinzugefügt oder entfernt werden.
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

	ArrayList<Contact> contacts = new ArrayList<Contact>();

	// Konstruktoren
	public ContactList() {
		super();
	}

	public ContactList(ArrayList<Contact> al) {
		this();
		this.contacts = al;
	}

	public ContactList(ArrayList<Contact> al, JabicsUser u) {
		this(al);
		this.owner = u;
	}

	public ContactList(ArrayList<Contact> al, String ln, JabicsUser u) {
		this(al, u);
		this.listName = ln;
	}

	/**
	 * toString gibt den Listennamen zurück
	 */
	@Override
	public String toString() {
		if (listName != null) {
			return listName;
		} else
			return Integer.toString(this.id);
	}

	/**
	 * Fügt einen Kontakt zur Liste hinzu und aktualisiert das Änderungsdatum.
	 * 
	 * @param <code>Contact</code>
	 */
	public void addContact(Contact c) {
		contacts.add(c);
		// this.setDateUpdated(LocalDateTime.now());
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

	// Getter und Setter
	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public BoStatus getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}
}