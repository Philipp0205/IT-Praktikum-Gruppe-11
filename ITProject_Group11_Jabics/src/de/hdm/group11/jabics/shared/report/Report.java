package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Diese Klasse <code>Report</code> ist die Basisklasse für alle Reports.
 * 
 * @author Kurrle
 * @author Anders
 * @author Stahl
 *
 */

public abstract class Report implements Serializable {

	/**
	 * Seriennummer einer Instanz dieser Klasse.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Datum einer Instanz dieser Klasse.
	 */
	Date creationDate;

	/**
	 * <code>Paragraph</code> einer Instanz dieser Klasse.
	 */
	Paragraph creator;

	/**
	 * Default Konstruktor.
	 */
	public Report() {
	}

	/**
	 * Auslesen des Erstellungsdatum.
	 * 
	 * @return Date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Auslesen des Erstellungsdatum als <code>String</code>.
	 * 
	 * @return String
	 */
	public String getCreationDateAsString() {
		return creationDate.toString();
	}

	/**
	 * Setzen des Erstellungsdatum.
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Auslesen des Erstellers.
	 * 
	 * @return Paragraph
	 */
	public Paragraph getCreator() {
		return creator;
	}

	/**
	 * Setzen des Erstellers.
	 * 
	 * @param creator
	 */
	public void setCreator(Paragraph creator) {
		this.creator = creator;
	}

	/**
	 * Auslesen der Seriennummer.
	 * 
	 * @return long
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}