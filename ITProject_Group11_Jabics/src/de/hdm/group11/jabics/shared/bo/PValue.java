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
 * @author Kurrle
 * @author Anders
 * @author Stahl
 */

public class PValue extends BusinessObject implements Comparable<PValue>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Integer Wert einer Instanz dieser Klasse.
	 */
	private int intValue;

	/**
	 * String Wert einer Instanz dieser Klasse.
	 */
	private String stringValue;

	/**
	 * Date Wert einer Instanz dieser Klasse.
	 */
	private Date dateValue;

	/**
	 * Float Wert einer Instanz dieser Klasse.
	 */
	private float floatValue;

	/**
	 * <code>Property</code> Objekt welche eine Instanz dieser Klasse angehört.
	 */
	private Property property;

	/**
	 * ID des <code>Property</code> Objekts welche eine Instanz dieser Klasse
	 * angehört.
	 */
	private int propertyId;

	/**
	 * 
	 */
	private int pointer = 0;

	/**
	 * 
	 */
	private boolean contains;

	/**
	 * Share Status einer Instanz dieser Klasse
	 */
	private BoStatus shareStatus = BoStatus.NOT_SHARED;

	/**
	 * Konstruktor, welcher den Konstruktor seiner Superklasse aufruft.
	 */
	public PValue() {
		super();
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Eigenschaft und Besitzer zu
	 * erzeugen. Wenn kein Datentyp angegeben, pointer aufgrund der PRoperty setzen
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
				this.pointer = 2;
				break;
			case INT:
				this.pointer = 1;
				break;
			case DATE:
				this.pointer = 3;
				break;
			case FLOAT:
				this.pointer = 4;
				break;
			default:
				this.pointer = 0;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Eigenschaft, ID und Besitzer zu
	 * erzeugen.
	 * 
	 * @param p
	 * @param i
	 * @param u
	 */
	public PValue(Property p, int i, JabicsUser u) {
		this(p, u);
		this.intValue = i;
		this.contains = true;
		this.pointer = 1;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Bezeichner und Type zu
	 * erzeugen.
	 * 
	 * @param p
	 * @param s
	 * @param u
	 */
	public PValue(Property p, String s, JabicsUser u) {
		this(p, u);
		this.stringValue = s;
		this.contains = true;
		this.pointer = 2;
	}

	/**
	 * 
	 * @param p
	 * @param date
	 * @param u
	 */
	public PValue(Property p, Date date, JabicsUser u) {
		this(p, u);
		this.dateValue = date;
		this.contains = true;
		this.pointer = 3;
	}

	/**
	 * 
	 * @param p
	 * @param f
	 * @param u
	 */
	public PValue(Property p, float f, JabicsUser u) {
		this(p, u);
		this.floatValue = f;
		this.contains = true;
		this.pointer = 4;
	}

	/**
	 * 
	 */
	@Override
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

	/**
	 * 
	 * @return
	 */
	public boolean containsValue() {
		return this.contains;
	}

	/**
	 * Check if BusinessObject is the same as transfer parameter
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PValue) {
			PValue pv = (PValue) obj;
			if (pv.getId() == this.id) {
				boolean bol = true;
				// Wenn keine PValues vorhanden, wird in diese Zeilen gar nicht gesprungen
				if (pv.toString() != this.toString())
					bol = false;
				if (pv.containsValue() != this.containsValue())
					bol = false;
				if (!pv.getProperty().equals(this.getProperty()))
					bol = false;
				System.out.println("Ists gleich: " + bol);
				return bol;
			}
			return false;
		}
		return false;
	}

	/**
	 * Setzen
	 * 
	 * @return
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * 
	 * 
	 * @param intValue
	 */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
		this.contains = true;
		this.pointer = 1;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * 
	 * @param string
	 */
	public void setStringValue(String string) {
		this.stringValue = string;
		this.contains = true;
		this.pointer = 2;
	}

	/**
	 * 
	 * @return
	 */
	public Date getDateValue() {
		return dateValue;
	}

	/**
	 * 
	 * @param dateValue
	 */
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		this.contains = true;
		this.pointer = 3;
	}

	/**
	 * 
	 * @return
	 */
	public float getFloatValue() {
		return floatValue;
	}

	/**
	 * 
	 * @param floatValue
	 */
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		this.contains = true;
		this.pointer = 4;
	}

	/**
	 * 
	 * @return
	 */
	public Property getProperty() {
		return this.property;
	}

	/**
	 * 
	 * @param property
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	/**
	 * 
	 * @return
	 */
	public int getPropertyId() {
		return propertyId;
	}

	/**
	 * 
	 * @param propertyId
	 */
	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * 
	 * @return
	 */
	public int getPointer() {
		return pointer;
	}

	/**
	 * 
	 * @param pointer
	 */
	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

	/**
	 * 
	 * @return
	 */
	public BoStatus getShareStatus() {
		return shareStatus;
	}

	/**
	 * 
	 * @param shareStatus
	 */
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}

	/**
	 * 
	 */
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