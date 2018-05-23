package de.hdm.group11.jabics.server;

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
		
		FilteredContactsOfUserReport results = new FilteredContactsOfUserReport(contacts, pv);
		Paragraph headline = new Paragraph();
		Paragraph footline = new Paragraph();
		//String[] filtercriteria;
		headline.setContent(pv.toString());
		footline.setContent(pv.toString());

		results.setFootline(footline);
		//results.setFiltercriteria(filtercriteria.setFiltercriteria(pv.toString()));
			
		// Entscheidung nach was gefiltert wird. Die FilterByMethoden geben alle passenden Report Objekte mit, welche dann den results mitgegeben werden.
		switch (pv.getProperty().getType())  {
		case STRING: 
			for (ContactReport i : this.filterContactsByString(contacts, pv)) {
				//filtercriteria.setFiltercriteria(filtercriteria);
				results.addReport(i);
		}
		break;
		case INT: 
			for (ContactReport i : this.filterContactsByInt(contacts, pv)) {
				results.addReport(i);
		}
		break; 
		case FLOAT: 
			for (ContactReport i : this.filterContactsByInt(contacts, pv)) {
				results.addReport(i);
		}
		break;
		case DATE: 
			for (ContactReport i : this.filterContactsByInt(contacts, pv)) {
				results.addReport(i);
		}
		break;
		default: System.out.println("Switch statement failed.");
		break;
			
		}

		return results;
	}
	
	/**
	 * 
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

