package de.hdm.group11.jabics.shared.bo;

import java.time.LocalDateTime;
import java.time.Month;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Diese Klasse realisiert die Eigenschaftsauspr�gungen eines Kontakts.
 * Diese k�nnen entweder im Datentyp int, String, float oder LocalDateTime angelegt sein.
 * Der int pointer repr�sentiert die Information, welcher Datentyp in PValue gespeichert ist.
 * 1 bedeutet int, 2 String, 3 Date und 4 float.
 * Zus�tzlich ist in jedem PValue Objekt ein Property Objekt gespeichert,
 * welches Informationen �ber den Charakter der Eigenschaft, zu welcher die Auspr�gung geh�rt, enth�lt.
 * 
 * @author Kurrle und Anders
 */
public class PValue extends BusinessObject implements Comparable<PValue>{


	
	private int intValue; 
	private String stringValue; 
	private LocalDateTime dateValue;
	private float floatValue; 
	private Property property;
	private int propertyId;
	private int pointer = 0; 
	
	/**
	 * Konstruktoren
	 */
	public PValue() {
		super();
	}
	public PValue(Property p) {
		this();
		this.property = p;
	}
	public PValue(Property p, int i) {
		this(p);
		this.intValue = i;
		this.pointer = 1;
	}
	public PValue(Property p, String s) {
		this(p);
		this.stringValue = s;
		this.pointer = 2;
	}
	public PValue(Property p, LocalDateTime date) {
		this(p);
		this.dateValue = date;
		this.pointer = 3;
	}
	public PValue(Property p, float f) {
		this(p);
		this.floatValue = f;
		this.pointer = 4;
	}
	
	public String toString() {
		switch (pointer) { 
		case 1 : 
			return Integer.toString(intValue);
		case 2: 
			return stringValue; 
		case 3: 
			return dateValue.toString();
		case 4:
			return Float.toString(floatValue);
		default: 
			return null;
		}
	}
	
	/** 
	 * Getter und Setter. DateUpdated wird bei allen substantiellen Informations�nderungen mitge�ndert.
	 */
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 1; 

	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 2; 

	}
	public LocalDateTime getDateValue() {
		return dateValue;
	}
	public void setDateValue(int year, int month, int dayOfMonth) {
		this.dateValue = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 3; 
	}
	// overload method if date is given in the datatype "month". 
	public void setDateValue(int year, Month month, int dayOfMonth) {
		this.dateValue = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 3; 
	}
	public void setDateValue(LocalDateTime t) {
		this.dateValue = t;
		this.pointer = 3; 
	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 4; 
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
		this.dateUpdated = LocalDateTime.now();
	}
	public int getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}
	public int getPointer() {
		return pointer;
	}
	public void setPointer(int pointer) {
		this.pointer = pointer;
	}
	@Override
	// das mit dem implements comparable und compare to nochmal überlgen
	public int compareTo(PValue pv) {
		if (pv.getId() == this.id) {
			return 0;
		} else return -1;
	}
	/**
     * Der Key Provider für ein PValue
     */
    public static final ProvidesKey<PValue> KEY_PROVIDER = new ProvidesKey<PValue>() {
      public Object getKey(PValue pv) {
        return (Integer)pv.getId();
      }
    };

}
