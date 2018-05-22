package de.hdm.group11.jabics.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

/**
 *  Diese Klasse ist eine synchrone Schnittstelle zur Erstellung von allen in Reports.
 *  In dieser Software gibt es drei verschiedene Report, welche ausgegeben werden können. 
 *  Siehe {@link AllContactsInSystemReport}, {@link AllContactsOfUserReport} und {@link FilteredContactsOfUserReport}.
 *  
 *  Dabei bietet diese Klasse untere anderem verschiedene <code>create</code>-Methoden, mit denen die zuvor genannten
 *  Reports erstellt werden können.
 *  
 *  @author Kurrle
 *  @author Anders
 */ 

import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.ContactReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.Paragraph;

public interface ReportGeneratorService extends RemoteService {
	
	
	public AllContactsInSystemReport createAllContactsInSystemReport();

	FilteredContactsOfUserReport createFilteredContactsOfUserReport(ArrayList<Contact> contacts, PValue pv) throws IllegalArgumentException;
	
	/** 
	 *  Diese Methode erstelle einen FilteredContactsOfUserReport.
	 *  Dieser Report gibt durch Mitgabe einer oder mehrere Filterkriteren eine gefilterete Liste von Kontakten aus.
	 * 
	 * @param reports Enthält ContactReport, die gefiltert werden sollen.
	 * @param p enthält Filterkriteren wie int, float oder Contact.
	 * @return Das gefilterte Reportobjekt.
	 * @throws IllegalArgumentException
	 */
	

}
