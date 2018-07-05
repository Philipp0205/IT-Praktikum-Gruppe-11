package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

public class AllContactsOfUserReport extends CompositeReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<ContactReport> subReports = new ArrayList<ContactReport>();

	public AllContactsOfUserReport() {
	}

	/**
	 * Einen neuen <code>AllContactsOfUserReport</code> erstellen.
	 * 
	 * @param reports,
	 *            ArrayList<ContactReport> aller hinzuzufügenden Subreports
	 */
	public AllContactsOfUserReport(ArrayList<ContactReport> reports) {
		this();
		this.subReports = reports;
	}

	/**
	 * Einen ContactReport zum Report hinzufügen
	 * 
	 * @param cr,
	 *            COntactReport, der hinzugefügt werden soll
	 */
	public void addReport(ContactReport cr) {
		this.subReports.add(cr);
	}

	/**
	 * Einen ContactReport aus dem Report entfernen
	 * 
	 * @param cr,
	 *            ContactReport, der entfernt werden soll. Muss die gleiche Referenz
	 *            aufweisen wie der bereits enthaltete Report
	 */
	public void removeReport(ContactReport cr) {
		subReports.remove(cr);
	}

	/**
	 * Alle ContactReports aus dem AllContactOfUserReport erhalten
	 * 
	 * @returns ArrayList<ContactReport>, alle ContactReports des Reports
	 */
	public ArrayList<ContactReport> getSubReports() {
		return this.subReports;
	}

}