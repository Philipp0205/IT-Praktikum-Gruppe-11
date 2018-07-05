package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

/**
 * <p>
 * Die Klasse <code>PValue</code> implementiert Eigenschaftsausprägungen in
 * Jabics. Diese können entweder im Datentyp int, String, float oder Date
 * angelegt sein. In jedem <code>PValue</code> Objekt ist ein
 * <code>Property</code> Objekt gespeichert, welches Informationen über den
 * Charakter der Eigenschaft, zu welcher die Ausprägung gehört, enthält.
 * </p>
 * 
 * @author Kurrle
 * @author Anders
 * @author Stahl
 */

public class PValue extends BusinessObject implements Comparable<PValue>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Ausprägung der Ganzzahl einer Instanz dieser Klasse.
	 */
	private int intValue;

	/**
	 * Ausprägung der Zeichenkette einer Instanz dieser Klasse.
	 */
	private String stringValue;

	/**
	 * Ausprägung des Datums einer Instanz dieser Klasse.
	 */
	private Date dateValue;

	/**
	 * Ausprägung der Kommazahl einer Instanz dieser Klasse.
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
	 * Information über den Datentyp einer Instanz dieser Klasse. 1 = int, 2 =
	 * String, 3 = Date und 4 = float
	 */
	private int pointer = 0;

	/**
	 * Information ob in der Instanz dieser Klasse eine Ausprägung hinterlegt ist.
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
	 * erzeugen. Wenn kein Datentyp angegeben, pointer aufgrund der Property setzen
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
	 * Konstruktor um eine Instanz dieser Klasse mit Eigenschaft, String-Ausprägung
	 * und Besitzer zu erzeugen.
	 * 
	 * @param p
	 * @param s
	 * @param u
	 */
	public PValue(Property p, String stringValue, JabicsUser u) {
		this(p, u);
		this.stringValue = stringValue;
		this.contains = true;
		this.pointer = 2;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Eigenschaft, Date-Ausprägung
	 * und Besitzer zu erzeugen.
	 * 
	 * @param p
	 * @param date
	 * @param u
	 */
	public PValue(Property p, Date dateValue, JabicsUser u) {
		this(p, u);
		this.dateValue = dateValue;
		this.contains = true;
		this.pointer = 3;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Eigenschaft, Float-Ausprägung
	 * und Besitzer zu erzeugen.
	 * 
	 * @param p
	 * @param f
	 * @param u
	 */
	public PValue(Property p, float floatValue, JabicsUser u) {
		this(p, u);
		this.floatValue = floatValue;
		this.contains = true;
		this.pointer = 4;
	}

	/**
	 * Textuelle Repräsentation des <code>PValue</code> Obejekts durch den Wert der
	 * Ausprägung.
	 * 
	 * @return intValue, stringValue, dateValue oder floatValue
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
	 * Prüfen ob das <code>PValue</code> Objekt das Gleiche wie der Parameter ist.
	 * 
	 * @param obj
	 * 
	 * @return true oder false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PValue) {
			PValue pv = (PValue) obj;
			if (pv.getId() == this.id) {
				boolean bol = true;
				if (pv.toString() != this.toString())
					bol = false;
				if (pv.containsValue() != this.containsValue())
					bol = false;
				if (!pv.getProperty().equals(this.getProperty()))
					bol = false;
				return bol;
			}
			return false;
		}
		return false;
	}

	/**
	 * Auslesen der Information, ob ein Wert hinterlegt ist.
	 * 
	 * @return contatins
	 */
	public boolean containsValue() {
		return this.contains;
	}

	/**
	 * Auslesen der Ausprägung in der Form einer Ganzzahl.
	 * 
	 * @return intValue
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * Setzen der Ausprägung in der Form einer Ganzzahl.
	 * 
	 * @param intValue
	 */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
		this.contains = true;
		this.pointer = 1;
	}

	/**
	 * Auslesen der Ausprägung in der Form einer Zeichenkette.
	 * 
	 * @return
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Setzen der Ausprägung in der Form einer Zeichenkette.
	 * 
	 * @param string
	 */
	public void setStringValue(String string) {
		this.stringValue = string;
		this.contains = true;
		this.pointer = 2;
	}

	/**
	 * Auslesen der Ausprägung in der Form eines Datums.
	 * 
	 * @return
	 */
	public Date getDateValue() {
		return dateValue;
	}

	/**
	 * Setzen der Ausprägung in der Form eines Datums.
	 * 
	 * @param dateValue
	 */
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		this.contains = true;
		this.pointer = 3;
	}

	/**
	 * Auslesen der Ausprägung in der Form einer Kommazahl.
	 * 
	 * @return
	 */
	public float getFloatValue() {
		return floatValue;
	}

	/**
	 * Setzen der Ausprägung in der Form einer Kommazahl.
	 * 
	 * @param floatValue
	 */
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		this.contains = true;
		this.pointer = 4;
	}

	/**
	 * Auslesen des zugehörigen <code>Property</code> Objekts.
	 * 
	 * @return
	 */
	public Property getProperty() {
		return this.property;
	}

	/**
	 * Setzen des zugehörigen <code>Property</code> Objekts.
	 * 
	 * @param property
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	/**
	 * Auslesen der ID des zugehörigen <code>Property</code> Objekts.
	 * 
	 * @return
	 */
	public int getPropertyId() {
		return propertyId;
	}

	/**
	 * Setzen der ID des zugehörigen <code>Property</code> Objekts.
	 * 
	 * @param propertyId
	 */
	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Auslesen des Pointers.
	 * 
	 * @return pointer
	 */
	public int getPointer() {
		return pointer;
	}

	/**
	 * Setzen des Pointers.
	 * 
	 * @param pointer
	 */
	public void setPointer(int pointer) {
		this.pointer = pointer;
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
	 * <code>PValue</code> Objekt vergleichen.
	 * 
	 * @param pv
	 */
	@Override
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