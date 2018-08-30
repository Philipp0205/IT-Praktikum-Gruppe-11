package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * Die Klasse <code>BusinessObject</code> ist die Basisklasse für
 * <code>ContactList</code>, <code>Contact</code>, <code>Property</code> und
 * <code>PValue</code>.
 * </p>
 * 
 * @author Anders
 * @author Stahl
 */
public abstract class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID einer Instanz dieser Klasse.
	 */
	int id;

	/**
	 * Besitzer einer Instanz dieser Klasse.
	 */
	private JabicsUser owner;

	/**
	 * Letztes Update einer Instanz dieser Klasse.
	 */
	private Timestamp dateUpdated;

	/**
	 * Erstellungsdatum einer Instanz dieser Klasse.
	 */
	private Timestamp dateCreated;

	/**
	 * Leerer Konstruktor.
	 */
	public BusinessObject() {
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
	 * Auslesen des Erstellungsdatums.
	 * 
	 * @return dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
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
	 * Auslesen der ID.
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
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
	 * Erzeugen einer ID.
	 * 
	 * @return id
	 */
	@Override
	public int hashCode() {
		return this.id;
	}

	/**
	 * Setzen des Erstellungsdatums.
	 * 
	 * @param dateCreated
	 */
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
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
	 * Setzen der ID.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Setzen der Owner.
	 * 
	 * @param owner
	 */
	public void setOwner(JabicsUser owner) {
		this.owner = owner;
	}
}