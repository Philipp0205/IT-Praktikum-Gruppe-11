package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Diese Klasse implementiert Kontakte in Jabics. In einem Kontakt sind mehrere PValue Objekte gespeichert.
 * PValues können hinzugefügt oder gelöscht werden, jedoch benötigt jeder Kontakt mindestens einen Namen, der
 * entweder über eine ArrayList<PValue>, in dem mindestens die PValues zu Name und Vorname vorhanden sind,
 * gesetzt werden kann, oder direkt über einen String.
 * 
 * @author Anders
 * @author Kurrle 
 */
public class Contact extends BusinessObject implements Comparable<Contact>, Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Instanzenvariablen
	 */
	private ArrayList<PValue> values = new ArrayList<PValue>();
	private String name;
	private BoStatus shareStatus = BoStatus.NOT_SHARED;
	

	public Contact(ArrayList<PValue> a, JabicsUser u) { 
		this(a);
		this.updateNickname();
		this.owner = u;
	}
		
	public Contact(ArrayList<PValue> a, String name) { 
		this(a);
		this.name = name;
	}
	
	public Contact(ArrayList<PValue> a) { 
		this();
		this.values = a;
	}

	/**
	 * Leerer Konstruktor
	 */
		public Contact() { 
			super();
		}

	
	@Override
	public String toString() {		
		return this.name;
	}
	
	/**
	 *  Fügt einen <code>PValue</code> einer <code>ArrayList<code> hinzu
	 */
	public void addPValue(PValue p) { 
		this.values.add(p);	
	}
	/**
	 *  Removes value from the value Array
	 */
	public void removePValue(PValue p) {
		this.values.remove(p);
	}
	
	/**
	 *  Getter and Setter
	 */
	
	public ArrayList<PValue> getValues() {
		return this.values;
	}
	public void setValues(ArrayList<PValue> values) {
		this.values = values;
		//this.dateUpdated = LocalDateTime.now();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		//this.dateUpdated = LocalDateTime.now();
	}
	public BoStatus getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}
	
	/**
	 * Überprüfen, ob der Nickname dieses Kontakts noch aktuell ist und neu setzen.
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
		for (PValue p2: values) {
			if (p2.getProperty().getId() == 2) {
				sBuffer.append(" " + p2.getStringValue());				
			} else {
				System.out.println("No lastname in Array");
			}
		}
		System.out.println("Neuer Nickname: " + sBuffer.toString());
		this.name = sBuffer.toString();
	}
	
	/*
	 * Relevante Methoden für die spätere Anzeige mittels selectionModels und ListDataProvider
	 */
	public int compareTo(Contact c) {
		if (c.getId() == this.id) {
			return 0;
		} else return -1;
	}
	
	/**
	 * Check if BusinessObject is the same as transfer parameter
	 */
	public boolean equals(Object obj) {
		System.out.println("equals1");
		if (obj instanceof Contact) {
			Contact c = (Contact) obj;
			if (c.getId() == this.id) {
				System.out.println("equals2");
				boolean bol = true;
				// Wenn keine PValues vorhanden, wird in diese Zeilen gar nicht gesprungen
				for(PValue pv : c.getValues()) {
					if (!this.values.contains(pv)) {
						bol = false;
					}
				}
				for(PValue pv : this.values) {
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
        return (Integer)c.getId();
      }
    };
	
}
