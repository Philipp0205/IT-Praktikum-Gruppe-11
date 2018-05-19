package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Property;

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


	ArrayList<ContactReport> subReports = new ArrayList<ContactReport>();
	Paragraph filtercriteria; // String, float, int oder Contact
	Property property;

	public FilteredContactsOfUserReport(ArrayList<ContactReport> reports, Paragraph pa, Property pp, String search) {
		this.subReports = reports;	
		this.filtercriteria = pa;
		this.property = pp;
	}

	public Paragraph getFiltercriteria() {
		return filtercriteria;
	}

	public void setFiltercriteria(Paragraph filtercriteria) {
		this.filtercriteria = filtercriteria;
	}
	
	public ArrayList<ContactReport> filter(Paragraph p) {
		
		return subReports;		
	}
	
	public void addReport(ContactReport cr) {
		subReports.add(cr);
	}
	
	public void removeReport(ContactReport cr) {
		subReports.remove(cr);
	}
	
	

}
