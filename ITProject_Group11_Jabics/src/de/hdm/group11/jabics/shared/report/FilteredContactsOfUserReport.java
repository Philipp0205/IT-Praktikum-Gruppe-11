package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

/**
 * Ein komplexer Report, welcher anhand einer oder mehrere Kriteren die Kontakte gefiltet zur�ck gibt.
 * 
 * @author Kurrle
 */

// TODO Simple Report --> ContactReport
public class FilteredContactsOfUserReport extends CompositeReport implements Serializable{
	
	private static final long serialVersionUID = 1L;

	Paragraph filtercriteria; // String, float, int oder Contact


	public FilteredContactsOfUserReport() {	
		
	}

	public Paragraph getFiltercriteria() {
		return filtercriteria;
	}

	public void setFiltercriteria(Paragraph filtercriteria) {
		this.filtercriteria = filtercriteria;
	}
	
	

}
