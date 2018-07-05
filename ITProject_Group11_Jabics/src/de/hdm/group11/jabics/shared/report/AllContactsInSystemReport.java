package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

public class AllContactsInSystemReport extends CompositeReport implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public AllContactsInSystemReport() {
		subReports = new ArrayList<AllContactsOfUserReport>();
	}
	
	private ArrayList<AllContactsOfUserReport> subReports;
	
	/**
	 * Einen <code>AllContactsOfUserReport</code> zum Report hinzufügen
	 * 
	 * @param cr,
	 *            Report, der hinzugefügt werden soll
	 */
	public void addReport(AllContactsOfUserReport cr) {
		this.subReports.add(cr);
	}
	
	/**
	 * Alle AllContactsOfUserReport aus dem Report erhalten
	 * 
	 * @returns ArrayList<ContactReport>, alle ContactReports des Reports
	 */
	public ArrayList<AllContactsOfUserReport> getSubReports(){
		return this.subReports;
	}
	
	/**
	 * Einen <code>AllContactsOfUserReport</code> aus dem Report entfernen
	 * 
	 * @param cr,
	 *            Report, der entfernt werden soll. Muss die gleiche Referenz
	 *            aufweisen wie der bereits enthaltete Report
	 */
	public void removeReport(AllContactsOfUserReport cr) {
		subReports.remove(cr);
	}
	
}