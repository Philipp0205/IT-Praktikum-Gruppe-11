package de.hdm.group11.jabics.server;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
 * Implementierung des <code>ReportGeneratorService</code>-Interface. Diese
 * Klasse stellt die Logik zur Verfügung, die bei einem RPC aufgerufen wird und
 * gibt den angefragten Report zurück.
 * 
 * @see ReportGeneratorService
 * @author Kurrle
 * @author Anders
 * @author Brase
 */
public class ReportGeneratorServiceImpl extends RemoteServiceServlet implements ReportGeneratorService {

	/**
	 * Instanzenvariablen
	 */
	ContactMapper cMapper;
	UserMapper uMapper;
	PValueMapper pvMapper;
	PropertyMapper pMapper;
	private static final long serialVersionUID = -4462530285584570547L;

	// Alternative Lösung die wir vorerst nicht beachten müssen
	// private EditorServiceImpl eService = null;

	public ReportGeneratorServiceImpl() throws IllegalArgumentException {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der ReportGeneratorImpl instantiiert
	 * werden soll
	 */
	public void init() throws IllegalArgumentException {
		cMapper = ContactMapper.contactMapper();
		uMapper = UserMapper.userMapper();
		pvMapper = PValueMapper.pValueMapper();
		pMapper = PropertyMapper.propertyMapper();
	}

	/**
	 * Diese Methode erstellt einen Report, der alle Kontakte im System wiedergibt.
	 * Hierfür werden alle Kontakte eines Nutzers für alle Nutzer ausgegeben. Der
	 * Report besteht aus einem Paragraphen am Anfang und einem Paragraphen am Ende
	 * und vielen <code>AllContactsOfUserReport</code> in einer ArrayList.
	 * 
	 * @return: AllContactsInSystemReport
	 */
	@Override
	public AllContactsInSystemReport createAllContactsInSystemReport() {

		System.out.println("Reporterstellt nicht befüllt");
		AllContactsInSystemReport result = new AllContactsInSystemReport();
		result.setHeadline(new Paragraph("Report aller Kontakte im System"));
		result.setFootline(new Paragraph("Ende des Reports"));
		result.setCreationDate(new Date());
		System.out.println("Reporterstellt nicht befüllt");
		for (JabicsUser u : uMapper.findAllUser()) {
			if (u.getId() != 0) {
				AllContactsOfUserReport newACU = createAllContactsOfUserReport(u);
				result.addReport(newACU);
			}
		}
		return result;
	}

	/**
	 * Diese Methode erstellt einen Report, der alle Kontakte für den übergebenen
	 * Nutzer wiedergibt. Es werden nur Kontakte wiedergegeben, die der Nutzer
	 * erstellt hat, von welchen er also der Eigentümer ist. Der Report besteht aus
	 * einem Paragraphen am Anfang und einem Paragraphen am Ende und vielen
	 * <code>ContactReport</code> in einer ArrayList.
	 * 
	 * @return AllContactsOfUserReport mit allen Kontakten des übergebenen Nutzers
	 */
	public AllContactsOfUserReport createAllContactsOfUserReport(JabicsUser u) {

		// Es wird ein leerer Report angelegt.
		AllContactsOfUserReport result = new AllContactsOfUserReport();
		// Headline und Footline werden gesetzt.
		result.setHeadline(new Paragraph("Report aller Kontakte für " + u.getUsername()));
		result.setFootline(new Paragraph("Ende des Reports"));
		result.setCreationDate(new Date());
		result.setCreator(u);

		/**
		 * Einen neuen ContactReport für jeden Kontakt eines Nutzers und jedes PValue
		 * von diesem erstellen
		 */
		ArrayList<Contact> allContactsOfUser = cMapper.findAllContacts(u);
		if (allContactsOfUser.isEmpty())
			System.err.println("Keine Kontakte");
		for (Contact c : allContactsOfUser) {
			c.setOwner(uMapper.findUserByContact(c));
			System.err.println("Nutzer geadded: " + c.getOwner().getId());
		}
		for (Contact c : allContactsOfUser) {
			ArrayList<PropertyView> pval = new ArrayList<PropertyView>();
			ArrayList<JabicsUser> allCollaborators = cMapper.findCollaborators(c);
			ArrayList<PValue> allPV = pvMapper.findPValueForContact(c);
			if (allPV.isEmpty())
				System.err.println("keine PValues");
			for (PValue pv : allPV) {
				PropertyView newPV = new PropertyView(pv);
				pval.add(newPV);
			}
			if (!pval.isEmpty()) {
				// Hier wird der Report letztendlich erstellt
				System.err.println("CR add: ");
				result.addReport(createContactReport(pval, c, allCollaborators));
			} else {
				ContactReport newRP = new ContactReport(new Paragraph(c.getName()),
						new Paragraph(c.getOwner().getUsername()));
				result.addReport(newRP);
			}
		}
		return result;
	}

	public ContactReport createContactReport(ArrayList<PropertyView> pv, Contact contact,
			ArrayList<JabicsUser> collaborators) {
		Paragraph contactInfo = new Paragraph("Kein Kontaktname");
		Paragraph userInfo = new Paragraph("Kein Nutzername");
		Paragraph creationInfo = new Paragraph("Kein Erstelldatum");
		Paragraph updateInfo = new Paragraph("Kein Updatedatum");
		Paragraph collaborationInfo = new Paragraph("Keine Teilhaberschaften");

		if (contact.getName() != null) {
			contactInfo.setContent(contact.getName());
		}
		if (contact.getOwner() != null) {
			userInfo.setContent(contact.getOwner().getUsername());
		} else {
			userInfo.setContent(uMapper.findUserByContact(contact).getUsername());
		}
		// Datum formattieren
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		if (contact.getDateCreated() != null) {
			creationInfo.setContent(contact.getDateCreated().toLocalDateTime().format(formatter));
		}
		if (contact.getDateUpdated() != null) {
			updateInfo.setContent(contact.getDateUpdated().toLocalDateTime().format(formatter));
		}
		// Teilhaber setzen
		if (!collaborators.isEmpty()) {
			String info = new String();
			for (JabicsUser collaborator : collaborators) {
				info = info + " " + collaborator.getUsername() + ",";
			}
			info = info.substring(0, info.length() - 1);
			collaborationInfo.setContent(info);
		}
		ContactReport newRP = new ContactReport(pv, contactInfo, userInfo, creationInfo, updateInfo, collaborationInfo);
		return newRP;
	}

	public FilteredContactsOfUserReport createAllSharedContactsReport(JabicsUser u, ArrayList<JabicsUser> finalUser) {

		FilteredContactsOfUserReport report = new FilteredContactsOfUserReport();
		String[] filtercriteria = new String[finalUser.size()];

		report.setHeadline(new Paragraph("Alle gemeinsamen Kontakte von " + u.getUsername()));
		report.setFootline(new Paragraph("Ende des Reports"));
		ArrayList<Contact> allUserContacts = cMapper.findAllContacts(u);
		report.setSubReports(filterContactsByCollaborators(allUserContacts, finalUser));
		for (int i = 0; i < finalUser.size(); i++) {
			filtercriteria[i] = finalUser.get(i).getUsername();
		}
		report.setFiltercriteria(new Paragraph(filtercriteria));
		return report;
	}

	/**
	 * Diese Methode filtert Contacte nach Filterkriterien und gibt ein Array aus
	 * gefilterten ContactReport zurück.
	 * 
	 * @param ArrayList mit Contact-Objekten "contacts"
	 * @param Ein       PValue-Objekt pv
	 * 
	 * @return FilteredContactsOfUserReport
	 */
	public FilteredContactsOfUserReport createFilteredContactsOfUserReport(PValue pv, JabicsUser u)
			throws IllegalArgumentException {

		System.out.println("Filtern nach " + pv.toString() + pv.getProperty().getLabel());
		/**
		 * Es wird eine ArrayList mit allen Kontakten des jeweiligen Nutzers erstellt.
		 * Aus dieser werden dann anschließend die entsprechenden Kontakte gefiltert.
		 */
		ArrayList<Contact> contacts = cMapper.findAllContacts(u);
		// Zuerst wird ein leerer Report angelegt.
		FilteredContactsOfUserReport result = new FilteredContactsOfUserReport();

		// Jeder Report hat eine Überschrift sowe eine abschließende Nachricht, welche
		// hier headline und footline genannt werden.
		result.setHeadline("Gefilterter Report für Nutzer " + u.getUsername());
		result.setFootline("Ende des Reports.");

		// Erstellungsdatum des Reports auf "jetzt" stellen.
		result.setCreationDate(new Date());

		String[] filtercriteria = new String[4];

		// Entscheidung nach was gefiltert wird. Die FilterByMethoden geben alle
		// passenden Report Objekte mit, welche dann den results mitgegeben werden.
		/**
		 * TODO: zu einem späteren Zeitpunkt, wenn nach mehreren Punkten gefiltert
		 * werden kann, die breaks entfernen und immer das vorergebnis einspeisen!
		 */
		switch (pv.getProperty().getType()) {
		case STRING:
			result.setSubReports(this.filterContactsByString(contacts, pv));
			filtercriteria[0] = pv.getStringValue();
			break;

		case INT:
			result.setSubReports(this.filterContactsByInt(contacts, pv));
			Integer integ = (Integer) pv.getIntValue();
			filtercriteria[1] = integ.toString();
			break;

		case FLOAT:
			result.setSubReports(this.filterContactsByDate(contacts, pv));
			Date dt = pv.getDateValue();
			filtercriteria[2] = dt.toString();
			break;

		case DATE:
			result.setSubReports(this.filterContactsByFloat(contacts, pv));
			Float fl = (Float) pv.getFloatValue();
			filtercriteria[3] = fl.toString();
			break;
		default:
			System.out.println("Switch statement in FiltertContactReport failed.");
			break;

		}
		result.setFiltercriteria(new Paragraph(filtercriteria));
		System.out.println("FilteredContacts-return ");
		return result;
	}

	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem StringValue,
	 * das in einem PValue mitgegeben wird, und gibt eine fertige ArrayList,
	 * bestehend aus ContactReports, zurück.
	 * 
	 * @param        ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByString(ArrayList<Contact> contacts, PValue pv) {
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		System.err.println("Filterkriterium PVAL: " + pv.getStringValue());

		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}

		// Kontakte nach Property filtern, falls gesetzt
		if (pv.getProperty().getLabel() != null) {
			System.err.println("Nach Property filtern" + pv.getProperty().getLabel());
			contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
		}
		System.err.println("Gefundene kontakte: ");
		for (Contact c : contacts) {
			System.err.println("Contact : " + c.getName());
		}
		// Kontakte nach PropertyValue filtern, falls gesetzt
		if (pv.getStringValue() != null) {
			System.err.println("Nach PVal filtern");
			contacts = Filter.filterContactsByString(contacts, pv.getStringValue(), pv.getProperty());
		}
		System.err.println("Zurückgeben");
		// Reports für die gefilterten Kontakte erstellen
		for (Contact c : contacts) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}
			//JabicsUser u = uMapper.findUserByContact(c);
			results.add(createContactReport(pviews, c, cMapper.findCollaborators(c)));
		}
		return results;
	}

	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem Int-Value, das
	 * in einem PValue mitgegeben wird, und gibt eine fertige ArrayList, bestehend
	 * aus ContactReports, zurück.
	 * 
	 * @param        ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByInt(ArrayList<Contact> contacts, PValue pv) {

		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}
		// Kontakte nach Property filtern, falls gesetzt
		if (pv.getProperty().getLabel() != null) {
			System.out.println("Filtern nach Property");
			contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
		}
		// Kontakte nach PropertyValue filtern, falls gesetzt
		if (pv.getIntValue() != 0) {
			contacts = Filter.filterContactsByInt(contacts, pv.getIntValue(), pv.getProperty());
		}

		for (Contact c : contacts) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}
			results.add(createContactReport(pviews, c, cMapper.findCollaborators(c)));
		}
		return results;

	}

	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem
	 * LocalDateTime-Value, das in einem PValue mitgegeben wird, und gibt eine
	 * fertige ArrayList, bestehend aus ContactReports, zurück.
	 * 
	 * @param        ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByDate(ArrayList<Contact> contacts, PValue pv) {

		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}
		// Kontakte nach Property filtern, falls gesetzt
		if (pv.getProperty().getLabel() != null) {
			contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
		}
		// Kontakte nach PropertyValue filtern, falls gesetzt
		if (pv.getDateValue() != null) {
			contacts = Filter.filterContactsByDate(contacts, pv.getDateValue(), pv.getProperty());
		}
		for (Contact c : contacts) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}
			results.add(createContactReport(pviews, c, cMapper.findCollaborators(c)));
		}
		return results; // ?

	}
	
	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem oder mehreren Nutzern,
	 * die in einem Array mitgegeben werden und gibt eine fertige ArrayList,
	 * bestehend aus ContactReports, zurück.
	 * 
	 * @param        ArrayList<Contact> contacts
	 * @param finalUser, ArrayList<JabicsUser>
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByCollaborators(ArrayList<Contact> allUserContacts, ArrayList<JabicsUser> finalUser) {

		ArrayList<ContactReport> results = new ArrayList<ContactReport>();

		for (Contact c : allUserContacts) {
			ArrayList<JabicsUser> collaborators = cMapper.findCollaborators(c);
			ArrayList<Contact> matches = Filter.filterContactsByCollaborators(finalUser, collaborators, c);
			for (Contact c2 : matches) {
				ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
				c2.setValues(pvMapper.findPValueForContact(c2));
				for (PValue p : c2.getValues()) {
					pviews.add(new PropertyView(p));
				}

				results.add(createContactReport(pviews, c, collaborators));
			}
		}
		return results;
	}

	/**
	 * Diese Methode filtert eine ArrayList aus Kontakten nach einem Float-Value,
	 * das in einem PValue mitgegeben wird, und gibt eine fertige ArrayList,
	 * bestehend aus ContactReports, zurück.
	 * 
	 * @param        ArrayList<Contact> contacts
	 * @param PValue pv
	 * @return ArrayList mit Contact-Report-Objekten
	 */
	public ArrayList<ContactReport> filterContactsByFloat(ArrayList<Contact> contacts, PValue pv) {

		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}
		// Kontakte nach Property filtern, falls gesetzt
		if (pv.getProperty().getLabel() != null) {
			contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
		}
		// Kontakte nach PropertyValue filtern, falls gesetzt
		if (pv.getFloatValue() != 0.0f) {
			contacts = Filter.filterContactsByFloat(contacts, pv.getFloatValue());
		}
		for (Contact c : contacts) {
			ArrayList<PropertyView> pviews = new ArrayList<PropertyView>();
			for (PValue p : c.getValues()) {
				pviews.add(new PropertyView(p));
			}

			results.add(createContactReport(pviews, c, cMapper.findCollaborators(c)));
		}
		return results; // ?
	}

	public ArrayList<Property> getPropertysOfJabicsUser(JabicsUser u) {

		ArrayList<Property> results = new ArrayList<Property>();

		for (Contact c : cMapper.findAllContacts(u)) {
			for (PValue pv : pvMapper.findPValueForContact(c)) {
				results.add(pv.getProperty());
			}
		}
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
//	} 	
}
