package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.view.client.ProvidesKey;

/**
 * <p>
 * Die Klasse <code>Contact</code> implementiert Kontakte in Jabics. In einem
 * Kontakt sind mehrere <code>PValue</code> Objekte gespeichert.
 * </p>
 * 
 * @author Anders
 * @author Kurrle
 */
public class Contact extends BusinessObject implements Comparable<Contact>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ArrayList aus Eigenschaftsauspräfungen, welcher einer Instanz dieser Klasse
	 * zugeordnet werden.
	 */
	private ArrayList<PValue> values = new ArrayList<PValue>();

	/**
	 * Name einer Instanz dieser Klasse.
	 */
	private String name;

	/**
	 * Share-Status einer Instanz dieser Klasse.
	 */
	private BoStatus shareStatus = BoStatus.NOT_SHARED;

	/**
	 * Leerer Konstruktor
	 */
	public Contact() {
		super();
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Bezeichner und Type zu
	 * erzeugen.
	 * 
	 * @param values
	 */
	public Contact(ArrayList<PValue> values) {
		this();
		this.values = values;
	}

	/**
	 * 
	 * @param values
	 * @param name
	 */
	public Contact(ArrayList<PValue> values, String name) {
		this(values);
		this.name = name;
	}

	/**
	 * 
	 * @param a
	 * @param u
	 */
	public Contact(ArrayList<PValue> values, JabicsUser u) {
		this(values);
		this.updateNickname();
		this.owner = u;
	}

	/**
	 * Textuelle Repräsentation des <code>Contact</code> Objekts durch den Name
	 * 
	 * @return name
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Fügt einen <code>PValue</code> der <code>ArrayList<code> <code>values</code>
	 * hinzu.
	 */
	public void addPValue(PValue pValue) {
		this.values.add(pValue);
	}

	/**
	 * Entfernt eine Ausprägung aus der Liste von Ausprägungen
	 */
	public void removePValue(PValue pValue) {
		this.values.remove(pValue);
	}

	/**
	 * Auslesen der Eigenschaftsausprägungen
	 * 
	 * @return values
	 */
	public ArrayList<PValue> getValues() {
		return this.values;
	}

	/**
	 * 
	 * @param values
	 */
	public void setValues(ArrayList<PValue> values) {
		this.values = values;
	}

	/**
	 * Auslesen des Namens
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzen des Namens.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		// this.dateUpdated = LocalDateTime.now();
	}

	/**
	 * Auslesen des Share-Status.
	 * 
	 * @return shareStatus
	 */
	public BoStatus getShareStatus() {
		return shareStatus;
	}

	/**
	 * Setzen des Share-Status.
	 * 
	 * @param shareStatus
	 */
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}

	/**
	 * Überprüfen, ob der Name dieses <code>Contact</code> Objekts noch aktuell ist,
	 * wenn nicht wird der Name neu gesetzt.
	 */
	public void updateNickname() {
		StringBuffer sBuffer = new StringBuffer("VornameNachname");
		for (PValue p : values) {
			if (p.getProperty().getId() == 1) {
				sBuffer.replace(0, sBuffer.length(), p.getStringValue());
			} else {
				System.out.println("Constructor in Contact: No name in Array.");
			}
		}
		for (PValue p2 : values) {
			if (p2.getProperty().getId() == 2) {
				sBuffer.append(" " + p2.getStringValue());
			} else {
				System.out.println("No lastname in Array");
			}
		}
		System.out.println("Neuer Nickname: " + sBuffer.toString());
		this.name = sBuffer.toString();
	}

	/**
	 * <code>Contect</code> vergleichen.
	 * 
	 * @param c
	 * 
	 * @return int
	 */
	public int compareTo(Contact c) {
		if (c.getId() == this.id) {
			return 0;
		} else
			return -1;
	}

	/**
	 * Prüfen ob das <code>Contact</code> Objekt, das Gleiche wie der Parameter ist.
	 * 
	 * @param obj
	 * 
	 * @return true oder false
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Contact) {
			Contact c = (Contact) obj;
			if (c.getId() == this.id) {
				boolean bol = true;
				for (PValue pv : c.getValues()) {
					if (!this.values.contains(pv)) {
						bol = false;
					}
				}
				for (PValue pv : this.values) {
					if (!c.getValues().contains(pv)) {
						bol = false;
					}
				}
				return bol;
			}
			return false;
		}
		return false;
	}

	/**
	 * Der Key Provider für einen Contact
	 */
	public static final ProvidesKey<Contact> KEY_PROVIDER = new ProvidesKey<Contact>() {
		public Object getKey(Contact c) {
			return (Integer) c.getId();
		}
	};
}