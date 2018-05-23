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
	}

	@Override
	public FilteredContactsOfUserReport createFilteredContactsOfUserReport(ArrayList<Contact> contacts, PValue pv
			) throws IllegalArgumentException {
		
		
		switch (pv.getProperty().getType())  {
		case STRING: this.filterContactsByString(contacts, pv);
		break;
		case INT: this.filterContactsByInt(contacts, pv);
		break;
		case FLOAT: this.filterContactsByFloat(contacts, pv);
		break;
		case DATE: this.filterContactsByDate(contacts, pv);
		break;
		default: System.out.println("Switch statement failed.");
		break;
			
		}
	
		return null;
	}
	
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
		
		return results;
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
	
	public ArrayList<ContactReport> filterContractsByFirstLetter(ArrayList<Contact> contacts, String search) {
		
		for (Contact c : contacts) {
			List<PValue> lPValues = c.getValues();
			for (PValue pv : lPValues) {
				List<PValue> filteredList = lPValues.stream().filter(new java.util.function.Predicate<String>() {
					@Override
		            public boolean test(String s) {
		                return s.startsWith(search);
		            }
				}).collect(Collectors.toList());
			}
		}
		return null;
		
	}
	
		
		
		
		
		
		
	}

