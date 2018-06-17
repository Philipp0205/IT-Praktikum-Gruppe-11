/**
 * 
 */
package de.hdm.group11.jabics.shared.bo;

import java.time.LocalDateTime;
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

public class Contact extends BusinessObject implements Comparable<Contact>{
	
	/**
	 * Instanzenvariablen
	 */
	ArrayList<PValue> values = new ArrayList<PValue>();
	private String name;
	private BoStatus shareStatus;
		
	public Contact(ArrayList<PValue> a, JabicsUser u) { 
		this(a);
		this.owner = u;
	}
		
	public Contact(ArrayList<PValue> a, String name) { 
		this(a);
		this.name = name;
	}
	
	public Contact(ArrayList<PValue> a) { 
		this();
		this.values = a;
		StringBuffer sBuffer = new StringBuffer("Vorname");
		for (PValue p : a) {
			if (p.getProperty().getLabel() == "Name") {
				sBuffer.append(p.getStringValue());					
				} else {
					System.out.println("Constructor in Contact: No name in Array.");
				}
		}
		for (PValue p2: a) {
			if (p2.getProperty().getLabel() == "Nachname") {
				sBuffer.append(p2.getStringValue());				
			} else {
				System.out.println("No lastname in Array");
			}
		}
		this.name = sBuffer.toString();
	
	}

	//Leerer Konstruktor
		public Contact() { 
			super();
		}

	
	@Override
	public String toString() {		
		return this.name;
	}
	
	/**
	 *  Adds value to the values Array 
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
		return values;
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
	/*
	 * Relevante Methoden für die spätere Anzeige mittels selectionModels und ListDataProvider
	 */
	public int compareTo(Contact c) {
		if (c.getId() == this.id) {
			return 0;
		} else return -1;
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
