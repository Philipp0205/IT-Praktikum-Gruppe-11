package de.hdm.group11.jabics.server;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.server.rpc.*;
import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.server.db.PValueMapper;
import de.hdm.group11.jabics.server.db.PropertyMapper;
import de.hdm.group11.jabics.server.db.UserMapper;
import de.hdm.group11.jabics.shared.ReportGeneratorService;
import de.hdm.group11.jabics.shared.bo.*;
import de.hdm.group11.jabics.shared.report.*;

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
	ContactMapper cMapper;
	UserMapper uMapper;
	PValueMapper pvMapper;
	PropertyMapper pMapper;
	private static final long serialVersionUID = -4462530285584570547L;
	
	// Alternative Lösung die wir vorerst nicht beachten müssen
	//private EditorServiceImpl eService = null;
	// 
	
	
	public ReportGeneratorServiceImpl() throws IllegalArgumentException {
	  }
	
	/**
	 * Diese Methode wird aufgerufen, wenn der ReportGeneratorImpl instantiiert werden soll
	 */
	public void init() throws IllegalArgumentException {
	    
		cMapper = ContactMapper.contactMapper();
		uMapper = UserMapper.userMapper();
		pvMapper = PValueMapper.pValueMapper();
		pMapper = PropertyMapper.propertyMapper();

	  }
	
	
	/**
	 * Diese Methode erstellt einen Report, der alle Kontakte im System wiedergibt.
	 * Hierfür werden alle Kontakte eines Nutzers für alle Nutzer ausgegeben.
	 * Der Report besteht aus einem Paragraphen am Anfang und einem Paragraphen am Ende und vielen 
	 * <code>AllContactsOfUserReport</code> in einer ArrayList. 
	 * 
	 * @return: AllContactsInSystemReport 
	 */
	@Override
	public AllContactsInSystemReport createAllContactsInSystemReport() {
		AllContactsInSystemReport result = new AllContactsInSystemReport();
		result.setHeadline(new Paragraph("Report aller Kontakte im System"));
		result.setFootline(new Paragraph("Ende des Reports"));
		result.setCreationDate(new Date());
		for (JabicsUser u: uMapper.findAllUser()) {
			result.addReport(createAllContactsOfUserReport(u));
		}
		return result;
	}	
	
	/**
	 * Diese Methode erstellt einen Report, der alle Kontakte für den übergebenen Nutzer wiedergibt.
	 * Es werden nur Kontakte wiedergegeben, die der Nutzer erstellt hat, von welchen er also der Eigentümer ist.
	 * Der Report besteht aus einem Paragraphen am Anfang und einem Paragraphen am Ende und vielen 
	 * <code>ContactReport</code> in einer ArrayList.
	 * 
	 * @return AllContactsOfUserReport mit allen Kontakten des übergebenen Nutzers
	 */
	public AllContactsOfUserReport createAllContactsOfUserReport(JabicsUser u) {
		
		// Es wird ein leerer Report angelegt.
		AllContactsOfUserReport result = new AllContactsOfUserReport();
		// Headline und Footline werden gesetzt.
		result.setHeadline(new Paragraph("Report aller Kontakte f�r " + u.getUsername()));
		result.setFootline(new Paragraph("Ende des Reports"));
		result.setCreationDate(new Date());
		/**
		 * Einen neuen ContactReport für jeden Kontakt eines Nutzers und jedes PValue von diesem erstellen 
		 */
		for (Contact c : cMapper.findAllContacts(u)) {
			ArrayList<PropertyView> pval = new ArrayList<PropertyView>();
			for (PValue pv : pvMapper.findPValueForContact(c)) {
				pval.add(new PropertyView(pv));
			}
			result.addReport(new ContactReport(pval));
		}
		return result;
	}

	/**
	 * Diese Methode filtert Contacte nach Filterkriterien und gibt ein Array aus gefilterten ContactReport zurück.
	 * @param ArrayList mit Contact-Objekten "contacts"
	 * @param Ein PValue-Objekt pv
	 * 
	 * @return FilteredContactsOfUserReport
	 */
	public FilteredContactsOfUserReport createFilteredContactsOfUserReport(PValue pv, JabicsUser u) throws IllegalArgumentException {
		/**
		 *  Es wird eine ArrayList mit allen Kontakten des jeweiligen Nutzers erstellt. 
		 *  Aus dieser werden dann anschließend die entsprechenden Kontakte gefiltert.
		 */
		ArrayList<Contact> contacts = cMapper.findAllContacts(u);
		// Zuerst wird ein leerer Report angelegt. 
		FilteredContactsOfUserReport result = new FilteredContactsOfUserReport();
		
		// Jeder Report hat eine Überschrift sowe eine abschließende Nachricht, welche hier headline und footline genannt werden.
		result.setHeadline("Gefilterter Report für Nutzer " + u.getUsername());
		result.setFootline("Ende des Reports.");
		
		// Erstellungsdatum des Reports auf "jetzt" stellen. 
		result.setCreationDate(new Date());
		
		
		String[] filtercriteria = new String[4];
			
		// Entscheidung nach was gefiltert wird. Die FilterByMethoden geben alle passenden Report Objekte mit, welche dann den results mitgegeben werden.
		/**
		 * TODO: zu einem späteren Zeitpunkt, wenn nach mehreren Punkten gefiltert werden kann, die breaks entfernen und immer das vorergebnis einspeisen!
		 */
		switch (pv.getProperty().getType())  {
		case STRING:
			result.setSubReports(this.filterContactsByString(contacts, pv));
			filtercriteria[0] = pv.getStringValue();
		break;
		
		case INT: 
			result.setSubReports(this.filterContactsByInt(contacts, pv));
			Integer integ = (Integer)pv.getIntValue();
			filtercriteria[1] = integ.toString();
		break; 
		
		case FLOAT:
			result.setSubReports(this.filterContactsByDate(contacts, pv));
			Date dt = pv.getDateValue();
			filtercriteria[2] = dt.toString();	
		break;
		
		case DATE: 
			result.setSubReports(this.filterContactsByFloat(contacts, pv));
			Float fl = (Float)pv.getFloatValue();
			filtercriteria[3] = fl.toString();
		break;
		default: System.out.println("Switch statement in FiltertContactReport failed.");
		break;
			
		}
		result.setFiltercriteria(new Paragraph(filtercriteria));
		System.out.println("FilteredContacts-return ");
		return result;
	}
	
	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem StringValue, das in einem PValue mitgegeben wird, 
	 * und gibt eine fertige ArrayList, bestehend aus ContactReports, zurück.
	 * @param ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByString(ArrayList<Contact> contacts, PValue pv) {
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
			for (PValue p : c.getValues()) {
				p.setProperty(pMapper.findPropertyById(p.getPropertyId()));
			}}
		for (Contact c : Filter.filterContactsByString(contacts, pv.getStringValue())) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}
			results.add(new ContactReport(pviews));
		}
		return results; //? 
	}
	
	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem Int-Value, das in einem PValue mitgegeben wird, 
	 * und gibt eine fertige ArrayList, bestehend aus ContactReports, zurück.
	 * @param ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByInt(ArrayList<Contact> contacts, PValue pv) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
			for (PValue p : c.getValues()) {
				p.setProperty(pMapper.findPropertyById(p.getPropertyId()));
			}}
		for (Contact c : Filter.filterContactsByInt(contacts, pv.getIntValue())) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}
			results.add(new ContactReport(pviews));
		}
		return results; //? 		
		
	}
	
	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem LocalDateTime-Value, das in einem PValue mitgegeben wird, 
	 * und gibt eine fertige ArrayList, bestehend aus ContactReports, zurück.
	 * @param ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByDate(ArrayList<Contact> contacts, PValue pv) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
			for (PValue p : c.getValues()) {
				p.setProperty(pMapper.findPropertyById(p.getPropertyId()));
			}}
		for (Contact c : Filter.filterContactsByDate(contacts, pv.getDateValue())) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}
			results.add(new ContactReport(pviews));
		}
		return results; //? 	
		
	}
	
	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem Float-Value, das in einem PValue mitgegeben wird, 
	 * und gibt eine fertige ArrayList, bestehend aus ContactReports, zurück.
	 * @param ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByFloat(ArrayList<Contact> contacts, PValue pv) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
			for (PValue p : c.getValues()) {
				p.setProperty(pMapper.findPropertyById(p.getPropertyId()));
			}}
		for (Contact c : Filter.filterContactsByFloat(contacts, pv.getFloatValue())) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}
			results.add(new ContactReport(pviews));
		}
		return results; //? 				
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
//	} 	
}

