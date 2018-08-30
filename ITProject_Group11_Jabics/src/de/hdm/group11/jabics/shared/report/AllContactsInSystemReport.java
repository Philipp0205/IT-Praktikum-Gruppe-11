package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Die Klasse <code>AllContactsInSystemReport</code>, ist für die Erstellung von
 * <code>CompositeReport</code> für alle <code>Contact</code> Objekte, welche
 * dem System bekannt sind zuständig.
 * 
 * @author Anders
 * @author Kurrle
 * @author Stahl
 */
public class AllContactsInSystemReport extends CompositeReport implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Eine Instanz von
	 * <code>AllContactsInSystemReport</code> besteht aus mehreren <code>AllContactsOfUserReport</code>.
	 */
	private ArrayList<AllContactsOfUserReport> subReports;

	/**
	 * Konstruktor um eine Instanz dieser Klasse zu erzeugen.
	 */
	public AllContactsInSystemReport() {
		subReports = new ArrayList<AllContactsOfUserReport>();
	}

	/**
	 * Einen <code>AllContactsOfUserReport</code> zu <code>subReports</code>
	 * hinzufügen.
	 * 
	 * @param allContactsOfUserReport
	 */
	public void addReport(AllContactsOfUserReport allContactsOfUserReport) {
		this.subReports.add(allContactsOfUserReport);
	}

	/**
	 * Auslesen aller <code>AllContactsOfUserReport</code> in
	 * <code>subReports</code>.
	 * 
	 * @return Liste aller <code>AllContactsOfUserReport</code> in
	 *          <code>subReports</code>.
	 */
	public ArrayList<AllContactsOfUserReport> getSubReports() {
		return this.subReports;
	}

	/**
	 * Einen <code>AllContactsOfUserReport</code> von <code>subReports</code>
	 * löschen.
	 * 
	 * @param allContactsOfUserReport
	 */
	public void removeReport(AllContactsOfUserReport allContactsOfUserReport) {
		subReports.remove(allContactsOfUserReport);
	}

}