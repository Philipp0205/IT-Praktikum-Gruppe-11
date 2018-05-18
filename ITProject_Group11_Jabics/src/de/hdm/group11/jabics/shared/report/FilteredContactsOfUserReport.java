package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

/**
 * Ein komplexter Report, welcher anhand einer oder mehrere Kriteren die Kontakte gefiltet zurück gibt.
 * 
 * @author Kurrle
 */

// TODO Simple Report --> ContactReport
public class FilteredContactsOfUserReport extends CompositeReport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	ArrayList<SimpleReport> subReports = new ArrayList<SimpleReport>();
	Paragraph filtercriteria; // String, float, int oder Contact

	public FilteredContactsOfUserReport(ArrayList<SimpleReport> reports, Paragraph p) {
		this.subReports = reports;	
		this.filtercriteria = p;
	}

	public Paragraph getFiltercriteria() {
		return filtercriteria;
	}

	public void setFiltercriteria(Paragraph filtercriteria) {
		this.filtercriteria = filtercriteria;
	}
	
	public ArrayList<SimpleReport> filter(Paragraph p) {
		return subReports;
		
	}
	
	

}
