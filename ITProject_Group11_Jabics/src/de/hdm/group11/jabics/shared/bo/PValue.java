package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Diese Klasse realisiert die Eigenschaftsausprägungen eines Kontakts. Diese
 * können entweder im Datentyp int, String, float oder LocalDate angelegt sein.
 * Der int pointer repräsentiert die Information, welcher Datentyp in PValue
 * gespeichert ist. 1 bedeutet int, 2 String, 3 Date und 4 float. Zusätzlich ist
 * in jedem PValue Objekt ein Property Objekt gespeichert, welches Informationen
 * über den Charakter der Eigenschaft, zu welcher die Ausprägung gehört,
 * enthält.
 * 
 * @author Kurrle und Anders
 */

public class PValue extends BusinessObject implements Comparable<PValue>, Serializable {

	private static final long serialVersionUID = 1L;

	private int intValue;
	private String stringValue;
	private Date dateValue;
	private float floatValue;
	private Property property;
	private int propertyId;
	private int pointer = 0;
	private boolean contains;
	private BoStatus shareStatus = BoStatus.NOT_SHARED;

	/**
	 * Konstruktoren
	 */
	public PValue() {
		super();
	}

	/**
	 * Wenn kein Datentyp angegeben, pointer aufgrund der PRoperty setzen
	 * 
	 * @param p
	 * @param u
	 */
	public PValue(Property p, JabicsUser u) {
		this();
		this.property = p;
		this.owner = u;
		try {
			switch (p.getType()) {
			case STRING:
				pointer = 2;
				break;
			case INT:
				pointer = 1;
				break;
			case DATE:
				pointer = 3;
				break;
			case FLOAT:
				pointer = 4;
				break;
			default:
				pointer = 0;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PValue(Property p, int i, JabicsUser u) {
		this(p, u);
		this.intValue = i;
		this.contains = true;
		this.pointer = 1;
	}

	public PValue(Property p, String s, JabicsUser u) {
		this(p, u);
		this.stringValue = s;
		this.contains = true;
		this.pointer = 2;
	}

	public PValue(Property p, Date date, JabicsUser u) {
		this(p, u);
		this.dateValue = date;
		this.contains = true;
		this.pointer = 3;
	}

	public PValue(Property p, float f, JabicsUser u) {
		this(p, u);
		this.floatValue = f;
		this.contains = true;
		this.pointer = 4;
	}

	public String toString() {
		try {
			switch (pointer) {
			case 1:
				return Integer.toString(intValue);
			case 2:
				return stringValue;
			case 3:
				return dateValue.toString();
			case 4:
				return Float.toString(floatValue);
			default:
				return "Test(wird noch entfernt)";
			}
		} catch (Exception e) {
			return "nicht gesetzt";
		}
	}

	public boolean containsValue() {
		return this.contains;
	}

	/**
	 * Getter und Setter. DateUpdated wird bei allen substantiellen
	 * Informations�nderungen mitge�ndert.
	 */
	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
		this.contains = true;
		this.pointer = 1;

	}

	public String getStringValue() {
		return stringValue;
	}
	
	public void setStringValue(String string) {
		this.stringValue = string;
		this.contains = true;
		this.pointer = 2;

	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		this.contains = true;
		this.pointer = 3;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		this.contains = true;
		this.pointer = 4;
	}

	public Property getProperty() {
		return this.property;
	}

	public void setProperty(Property property) {
		this.property = property;
		// this.dateUpdated = LocalDateTime.now();
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
	public BoStatus getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}

	@Override
	// das mit dem implements comparable und compare to nochmal überlgen
	public int compareTo(PValue pv) {
		if (pv.getId() == this.id) {
			return 0;
		} else
			return -1;
	}

	/**
	 * Der Key Provider für ein PValue
	 */
	public static final ProvidesKey<PValue> KEY_PROVIDER = new ProvidesKey<PValue>() {
		public Object getKey(PValue pv) {
			return (Integer) pv.getId();
		}
	};


}
