package de.hdm.group11.jabics.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import de.hdm.group11.jabics.shared.report.*;
import de.hdm.group11.jabics.shared.bo.*;

/**
 *  Diese Klasse ist eine synchrone Schnittstelle zur Erstellung von allen Reports.
 *  In Jabics gibt es drei verschiedene Reportarten, welche ausgegeben werden können. 
 *  Siehe {@link AllContactsInSystemReport}, {@link AllContactsOfUserReport} und {@link FilteredContactsOfUserReport}.
 *  
 *  Dabei bietet diese Klasse untere anderem verschiedene <code>create</code>-Methoden, mit denen die zuvor genannten
 *  Reports erstellt werden können.
 *  
 *  @author Kurrle
 *  @author Anders
 */ 



public interface ReportGeneratorService extends RemoteService {
	
	/**
	 * TODO adddescription
	 * @return
	 */
	public AllContactsInSystemReport createAllContactsInSystemReport() throws IllegalArgumentException;
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	AllContactsOfUserReport createAllContactsOfUserReport(User u) throws IllegalArgumentException;
	
	/** 
	 *  Diese Methode erstelle einen FilteredContactsOfUserReport.
	 *  Dieser Report gibt durch Mitgabe einer oder mehrere Filterkriteren eine gefilterete Liste von Kontakten aus.
	 * 
	 * @param reports Enthält ContactReport, die gefiltert werden sollen.
	 * @param p enthält Filterkriteren wie int, float oder Contact.
	 * @return Das gefilterte Reportobjekt.
	 * @throws IllegalArgumentException
	 */
	FilteredContactsOfUserReport createFilteredContactsOfUserReport(ArrayList<Contact> contacts, PValue pv) throws IllegalArgumentException;
	
	
	

}
