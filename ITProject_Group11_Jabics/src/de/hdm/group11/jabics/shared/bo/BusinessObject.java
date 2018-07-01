package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Dies ist die Basisklasse für alle Listen, Kontakte, Eigenschaften und deren
 * Ausprägungen. Hier sind Erstelldaten und Änderungsdaten gespeichert, sowie
 * der Ersteller eines Objektes.
 * 
 * @author Anders
 */
public abstract class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	public BusinessObject() {
	}
	
	/**
	 * ID einer Instanz dieser Klasse.
	 */
	int id;
	
	/**
	 * Besitzer einer Instanz dieser Klasse.
	 */
	JabicsUser owner;
	
	/**
	 * Letztes Update einer Instanz dieser Klasse.
	 */
	Timestamp dateUpdated;
	
	/**
	 * Erstellungsdatum einer Instanz dieser Klasse.
	 */
	Timestamp dateCreated;

	/**
	 * Erzeugen einer ID.
	 * 
	 * @return id
	 */
	@Override
	public int hashCode() {
		return this.id;
	}

	/**
	 * Prüfen ob BusinessObject das gleiche ist wie der Übergabeparameter.
	 * 
	 * @param obj
	 *            Das Objekt, welches mit dem BusinessObject verglichen werden soll
	 * 
	 * @return true or false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BusinessObject) {
			BusinessObject bo = (BusinessObject) obj;
			if (bo.getId() == this.id)
				return true;
		}
		return false;
	}

	/**
	 * Auslesen der ID.
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setzen der ID.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Auslesen des Owners.
	 * 
	 * @return owner
	 */
	public JabicsUser getOwner() {
		return this.owner;
	}

	/**
	 * Setzen der Owner.
	 * 
	 * @param owner
	 */
	public void setOwner(JabicsUser owner) {
		this.owner = owner;
	}

	/**
	 * Auslesen des letzten Updates.
	 * 
	 * @return dateUpdated
	 */
	public Timestamp getDateUpdated() {
		return dateUpdated;
	}

	/**
	 * Setzen des letzten Updates.
	 * 
	 * @param dateUpdated
	 */
	public void setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	/**
	 * Auslesen des Erstellungsdatums.
	 * 
	 * @return dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * Setzen des Erstellungsdatums.
	 * 
	 * @param dateCreated
	 */
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

}