package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Die Klasse <code>AllContactsOfUserReport</code>, ist für die Erstellung von
 * <code>CompositeReport</code> für alle <code>Contact</code>, welche einem
 * <code>JabicsUser</code> bekannt sind zuständig.
 * 
 * @author Anders
 * @author Kurrle
 * @author Stahl
 */
public class AllContactsOfUserReport extends CompositeReport implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Liste aus zugehörigen <code>ContactReport</code> einer Instanz dieser Klasse.
	 */
	private ArrayList<ContactReport> subReports = new ArrayList<ContactReport>();

	/**
	 * Default Konstruktor
	 */
	public AllContactsOfUserReport() {
	}

	/**
	 * Eine Instanz der Klasse <code>AllContactsOfUserReport</code> mit allen <code>ContactReport</code> erstellen.
	 * 
	 * @param contactReport
	 *           Liste aller zugehörigen <code>ContactReport</code>.
	 */
	public AllContactsOfUserReport(ArrayList<ContactReport> contactReport) {
		this();
		this.subReports = contactReport;
	}

	/**
	 * Einen <code>ContactReport</code> zu <code>subReports</code>
	 * hinzufügen.
	 * 
	 * @param contactReport
	 */
	public void addReport(ContactReport contactReport) {
		this.subReports.add(contactReport);
	}

	/**
	 * Einen <code>ContactReport</code> von <code>subReports</code>
	 * löschen.
	 * 
	 * @param contactReport
	 */
	public void removeReport(ContactReport contactReport) {
		subReports.remove(contactReport);
	}

	/**
	 * Auslesen aller <code>ContactReports</code> in <code>subReports</code>.
	 * 
	 * @returns Liste aller <code>ContactReports</code> in <code>subReports</code>.
	 */
	public ArrayList<ContactReport> getSubReports() {
		return this.subReports;
	}

}