package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

/**
 * Ein komplexer Report, welcher anhand einer oder mehrere Kriteren die Kontakte gefiltet zurück gibt.
 * 
 * @author Kurrle
 */

// TODO Simple Report --> ContactReport
public class FilteredContactsOfUserReport extends CompositeReport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Paragraph filtercriteria; // String, float, int oder Contact


	public FilteredContactsOfUserReport(ArrayList<Contact> contacts, PValue pv) {	
		
	}

	public Paragraph getFiltercriteria() {
		return filtercriteria;
	}

	public void setFiltercriteria(Paragraph filtercriteria, String[] string) {
		filtercriteria.setFiltercriteria(string);
	}
	
	

}
