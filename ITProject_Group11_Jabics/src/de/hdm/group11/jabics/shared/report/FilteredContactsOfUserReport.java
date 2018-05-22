package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
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
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	Paragraph filtercriteria; // String, float, int oder Contact
	Property property;
	PValue pv;

	public FilteredContactsOfUserReport(ArrayList<Contact> contacts, PValue pv) {
		this.contacts = contacts;	
		this.pv = pv;
		
	}

	public Paragraph getFiltercriteria() {
		return filtercriteria;
	}

	public void setFiltercriteria(Paragraph filtercriteria) {
		this.filtercriteria = filtercriteria;
	}
	
	public void addReport(ContactReport cr) {
		subReports.add(cr);
	}
	
	public void removeReport(ContactReport cr) {
		subReports.remove(cr);
	}
	
	

}
