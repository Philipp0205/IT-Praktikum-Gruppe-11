package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;

/**
 * Ein komplexer Report, welcher anhand einer oder mehrere Kriteren die Kontakte
 * gefiltet zurück gibt.
 * 
 * @author Kurrle
 */
public class FilteredContactsOfUserReport extends CompositeReport implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Filterkriterien einer Instanz dieser Klasse.
	 */
	Paragraph filtercriteria; // String, float, int oder Contact

	/**
	 * Default Konstruktor.
	 */
	public FilteredContactsOfUserReport() {
	}

	/**
	 * Auslesen der <code>filtercriteria</code>.
	 * 
	 * @return filtercriteria
	 */
	public Paragraph getFiltercriteria() {
		return filtercriteria;
	}

	/**
	 * Setzen der <code>filtercriteria</code>.
	 * 
	 * @param filtercriteria
	 */
	public void setFiltercriteria(Paragraph filtercriteria) {
		this.filtercriteria = filtercriteria;
	}

}