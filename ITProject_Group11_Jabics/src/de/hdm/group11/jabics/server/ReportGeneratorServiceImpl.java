package de.hdm.group11.jabics.server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;

import com.google.gwt.user.server.rpc.*;
import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.server.db.UserMapper;
import de.hdm.group11.jabics.shared.ReportGeneratorService;
import de.hdm.group11.jabics.shared.bo.*;
import de.hdm.group11.jabics.shared.report.*;
import de.hdm.thies.bankProjekt.shared.ReportGenerator;

/**
 * Implementierung des <code>ReportGeneratorService</code>-Interface. 
 * 
 * @see ReportGeneratorService
 * @author Kurrle und Anders
 */

public class ReportGeneratorServiceImpl extends RemoteServiceServlet 
	implements ReportGeneratorService {

	/**
	 * Instanzenvariablen
	 */
	ContactMapper cMapper = ContactMapper.contactMapper();
	UserMapper uMapper = UserMapper.userMapper();
	private static final long serialVersionUID = -4462530285584570547L;
	
	
	/**
	 * TODO: beschreibung
	 */
	@Override
	public AllContactsInSystemReport createAllContactsInSystemReport() {
		AllContactsInSystemReport result = new AllContactsInSystemReport();
		/**
		 * ArrayList<User> allUsers = uMapper.getAllUser();
		 * Dem result AllContactsInUser Reports für alle Nutzer hinzufügen
		 */
		for (User u: uMapper.getAllUser()) {
			result.addReport(createAllContactsOfUserReport(u));
		}
		return result;
	}
	
	
	/**
	 * TODO: beschreibung
	 */
	@Override
	public AllContactsOfUserReport createAllContactsOfUserReport(User u) {
		AllContactsOfUserReport result = new AllContactsOfUserReport();
		ArrayList<Contact> allContacts = cMapper.findAllContacts(u);
		return result;
	}


	/**
	 * Diese Methode filtert Contacte nach Filterkriterien und gibt ein Array aus gefilterten ContactReport zurück.
	 * @param ArrayList mit Contact-Objekten "contacts"
	 * @param Ein PValue-Objekt pv
	 * 
	 * @return
	 */
	@Override
	public FilteredContactsOfUserReport createFilteredContactsOfUserReport(ArrayList<Contact> contacts, PValue pv
			) throws IllegalArgumentException {

		// Zuerst wird ein leerer Report angelegt. 
		FilteredContactsOfUserReport results = new FilteredContactsOfUserReport(contacts, pv);
		
		// Jeder Report hat eine Überschrift sowe eine abschließende Nachricht, welche hier headline und footline genannt werden.
		results.setFootline("Ende des Reports.");
		
		// Erstellungsdatum des Reports auf "jetzt" stellen. 
		results.setCreationDate(LocalDateTime.now());
		
		
		
		String[] filtercriteria = new String[4];
		Paragraph p = new Paragraph();
			
		// Entscheidung nach was gefiltert wird. Die FilterByMethoden geben alle passenden Report Objekte mit, welche dann den results mitgegeben werden.
		switch (pv.getProperty().getType())  {
		case STRING: 
			for (ContactReport i : this.filterContactsByString(contacts, pv)) {
				filtercriteria[0] = "string";
				results.addReport(i);
		}
		break;
		case INT: 
			for (ContactReport i : this.filterContactsByInt(contacts, pv)) {
				filtercriteria[1] = "int";
				results.addReport(i);
		}
		break; 
		case FLOAT: 
			for (ContactReport i : this.filterContactsByDate(contacts, pv)) {
				filtercriteria[2] = "date";
				results.addReport(i);
		}
		break;
		case DATE: 
			for (ContactReport i : this.filterContactsByFloat(contacts, pv)) {
				filtercriteria[2] = "float";
				results.addReport(i);
		}
		break;
		default: System.out.println("Switch statement failed.");
		break;
			
		}
		results.setFiltercriteria(p, filtercriteria);
		/**
		 *  Oben im Report wird angegeben, wie der nachfolgende Report gefiltert wurde.
		 */
		for (String i : filtercriteria) {
			if (i != null) {
				results.setHeadline("Dieser Report wurde nach dem Datentyp " + i + " gefiltert.");
			}
		}
		
		return results;
	}
	
	/**
	 * @param contacts
	 * @param pv
	 * @return ArrayList mit Contact-Report-Objekten results
	 */
	public ArrayList<ContactReport> filterContactsByString(ArrayList<Contact> contacts, PValue pv) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
	
		for (Contact c : contacts ) {
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getStringValue() == pv.getStringValue()) {
					pviews.add(new PropertyView(pv));					
				} 
			} 	
		} results.add(new ContactReport(pviews));
		
		return results; //? 
	}
		
	public ArrayList<ContactReport> filterContactsByInt(ArrayList<Contact> contacts, PValue pv) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
	
		for (Contact c : contacts ) {
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getIntValue() == pv.getIntValue()) {
					pviews.add(new PropertyView(pv));					
				} 
			} 	
		} results.add(new ContactReport(pviews));
		
		return results; 		
		
	}
	
	public ArrayList<ContactReport> filterContactsByDate(ArrayList<Contact> contacts, PValue pv) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
	
		for (Contact c : contacts ) {
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getDateValue() == pv.getDateValue()) {
					pviews.add(new PropertyView(pv));					
				} 
			} 	
		} results.add(new ContactReport(pviews));
		
		return results; 		
		
	}
	
	public ArrayList<ContactReport> filterContactsByFloat(ArrayList<Contact> contacts, PValue pv) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
	
		for (Contact c : contacts ) {
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getFloatValue() == pv.getFloatValue()) {
					pviews.add(new PropertyView(pv));					
				} 
			} 	
		} results.add(new ContactReport(pviews));
		
		return results;  				
	}
	
//	public ArrayList<ContactReport> filterContractsByStringAndFirstLetter(ArrayList<Contact> contacts, PValue pv, String search) {
//		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
//		ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
//		ArrayList<String> strings = new ArrayList<String>();
//	
//		for (Contact c : contacts) {
//			List<PValue> pvalues = c.getValues();
//			for (PValue i : pvalues) {
//				strings.add(i.getStringValue());
//			}
//			List<String> filteredList = strings.stream()
//					.filter(s -> s.startsWith(search))
//					.collect(Collectors.toList());		
//		} 
//		
//		
//	} 
	
		
		
		
		
		
		
	}

