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
 * @author Kurrle
 * @author Anders
 * @author Brase
 * @author Stahl
 */
public class ReportGeneratorServiceImpl extends RemoteServiceServlet implements ReportGeneratorService {

	/**
	 * <code>ContactMapper</code> in einer Instanz dieser Klasse.
	 */
	ContactMapper cMapper;

	/**
	 * <code>UserMapper</code> in einer Instanz dieser Klasse.
	 */
	UserMapper uMapper;

	/**
	 * <code>PValueMapper</code> in einer Instanz dieser Klasse.
	 */
	PValueMapper pvMapper;

	/**
	 * <code>PropertyMapper</code> in einer Instanz dieser Klasse.
	 */
	PropertyMapper pMapper;

	private static final long serialVersionUID = -4462530285584570547L;

	/**
	 * Default Konstruktor
	 * 
	 * @throws IllegalArgumentException
	 */
	public ReportGeneratorServiceImpl() throws IllegalArgumentException {
	}

	/**
	 * <p>
	 * Diese Methode wird aufgerufen, wenn der
	 * <code>ReportGeneratorServiceImpl</code> instanziiert werden soll.
	 * </p>
	 * Initialisierung der Mapper Klassen.
	 */
	public void init() throws IllegalArgumentException {
		cMapper = ContactMapper.contactMapper();
		uMapper = UserMapper.userMapper();
		pvMapper = PValueMapper.pValueMapper();
		pMapper = PropertyMapper.propertyMapper();
	}

	/**
	 * Diese Methode erstellt einen Report, der alle <code>Contact</code> Objekte im
	 * System wiedergibt. Hierfür werden alle <code>Contact</code> Objekte eines
	 * <code>JabicsUser</code> Objekts für alle <code>JabicsUser</code> Objekte
	 * ausgegeben. Der <code>Report</code> besteht aus einem <code>Paragraph</code>
	 * am Anfang und einem <code>Paragraph</code> am Ende und vielen
	 * <code>AllContactsOfUserReport</code> in einer Liste.
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
	 * Diese Methode erstellt einen <code>Report</code>, der alle
	 * <code>Contact</code> Objekte für das übergebene <code>JabicsUser</code>
	 * Objekt wiedergibt. Es werden nur <code>Contact</code> Objekte wiedergegeben,
	 * die der <code>JabicsUser</code> erstellt hat, von welchen er also der
	 * Eigentümer ist. Der <code>Report</code> besteht aus einem
	 * <code>Paragraph</code> am Anfang und einem <code>Paragraph</code> am Ende und
	 * vielen <code>ContactReport</code> in einer Liste.
	 * 
	 * @param u
	 *            das <code>JabicsUser</code> Objekt für welches der
	 *            <code>AllContactsOfUserReport</code> erstellt werden soll.
	 * 
	 * @return <code>AllContactsOfUserReport</code> mit allen
	 *         <code>ContactReport</code> des übergebenen <code>JabicsUser</code>.
	 */
	public AllContactsOfUserReport createAllContactsOfUserReport(JabicsUser u) {

		// Es wird ein leerer Report angelegt.
		AllContactsOfUserReport result = new AllContactsOfUserReport();
		// Headline und Footline werden gesetzt.
		result.setHeadline(new Paragraph("Report aller Kontakte von " + u.getUsername()));
		result.setFootline(new Paragraph("Ende des Reports"));
		result.setCreationDate(new Date());
		result.setCreator(new Paragraph(u.getUsername()));

		/**
		 * Einen neuen ContactReport für jeden Kontakt eines Nutzers und jedes PValue
		 * von diesem erstellen
		 */
		ArrayList<Contact> allContactsOfUser = cMapper.findAllContacts(u);
		if (allContactsOfUser.isEmpty())
			for (Contact c : allContactsOfUser) {
				c.setOwner(uMapper.findUserByContact(c));
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

	/**
	 * Einen <code>ContactReport</code> für einen <code>Contact</code> erstellen.
	 * 
	 * @param pv
	 *            die Liste von <code>PropertyView</code> Objekten.
	 * @param contact
	 *            das <code>Contact</code> Objekt für welches der
	 *            <code>ContactReport</code> erstellt werden soll.
	 * @param collaborators
	 *            die Liste aller <code>JabicsUser</code>, welche eine
	 *            Teilhaberschaft zu dem <code>Contact</code> Objekt besitzen.
	 * @return das erstellte <code>ContactReport</code> Objekt.
	 */
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

	/**
	 * Einen <code>FilteredContactsOfUserReport</code> für einen
	 * <code>JabicsUser</code> erstellen, der nach Kollaborationen an
	 * <code>Contact</code> Objekten gefiltert ist.
	 * 
	 * @param u
	 *            das <code>JabicsUser</code> Objekt, für welches der
	 *            <code>FilteredContactsOfUserReport</code> erstellt werden soll.
	 * @param finalUser
	 *            die Liste der <code>JabicsUser</code> Objekte, nach denen
	 *            gefiltert werden soll.
	 * @return das erstellte <code>FilteredContactsOfUserReport</code> Objekt.
	 */
	public FilteredContactsOfUserReport createAllSharedContactsReport(JabicsUser u, ArrayList<JabicsUser> finalUser) {

		FilteredContactsOfUserReport report = new FilteredContactsOfUserReport();
		String[] filtercriteria = new String[finalUser.size()];

		report.setHeadline(new Paragraph("Alle Kontakte, die Sie geteilt haben."));
		report.setCreator(new Paragraph(u.getUsername()));
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
	 * Diese Methode filtert <code>Contact</code> Objekte eines
	 * <code>JabicsUser</code> nach einem <code>PValue</code> Objekt.
	 * 
	 * @param pv
	 *            das <code>PValue</code> Objekt nach dem gefiltert werden soll.
	 * @param u
	 *            das <code>JabicsUser</code> Objekt dessen <code>Contact</code>
	 *            Objekte gefiltert werden soll.
	 * @return <code>FilteredContactsOfUserReport</code>
	 */
	public FilteredContactsOfUserReport createFilteredContactsOfUserReport(PValue pv, JabicsUser u)
			throws IllegalArgumentException {

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
		result.setCreator(new Paragraph(u.getUsername()));
		result.setFootline("Ende des Reports.");

		// Erstellungsdatum des Reports auf "jetzt" stellen.
		result.setCreationDate(new Date());

		String[] filtercriteria = new String[4];

		// Entscheidung nach was gefiltert wird. Die FilterByMethoden geben alle
		// passenden Report Objekte mit, welche dann den results mitgegeben werden.
		switch (pv.getProperty().getType()) {
		case STRING:
			result.setSubReports(this.filterContactsByString(contacts, pv));
			if (pv.getStringValue() != null) {
				filtercriteria[0] = pv.getStringValue();
			} else {
				filtercriteria[0] = pv.getProperty().getLabel();
			}
			break;

		case INT:
			result.setSubReports(this.filterContactsByInt(contacts, pv));
			if (pv.getIntValue() != -2147483648) {
				Integer integ = (Integer) pv.getIntValue();
				filtercriteria[1] = integ.toString();
			} else {
				filtercriteria[1] = pv.getProperty().getLabel();
			}
			break;

		case FLOAT:
			result.setSubReports(this.filterContactsByFloat(contacts, pv));
			if (pv.getFloatValue() != -99999997952f) {
				Float fl = (Float) pv.getFloatValue();
				filtercriteria[2] = fl.toString();
			} else {
				filtercriteria[2] = pv.getProperty().getLabel();
			}
			break;
		case DATE:
			result.setSubReports(this.filterContactsByDate(contacts, pv));
			if (pv.getDateValue() != null) {
				Date dt = pv.getDateValue();
				filtercriteria[3] = dt.toString();
			} else {
				filtercriteria[3] = pv.getProperty().getLabel();
			}
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
	 * Diese Methode filtert eine Liste aus <code>Contact</code> Objekte nach einem
	 * <code>String</code>, das in einem <code>PValue</code> mitgegeben wird.
	 * 
	 * @param contacts
	 *            Liste zu filternder <code>Contact</code> Objekte.
	 * @param pv
	 *            <code>PValue</code> Objekt mit dem <code>String</code> Wert.
	 * @return Liste aller <code>ContactReport</code>, welche dem Filterkriterium
	 *         entspricht.
	 */
	public ArrayList<ContactReport> filterContactsByString(ArrayList<Contact> contacts, PValue pv) {
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();

		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}

		// Kontakte nach Property filtern, falls gesetzt
		if (pv.getProperty().getLabel() != null) {
			contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
		}
		for (Contact c : contacts) {
			System.err.println("Contact : " + c.getName());
		}
		// Kontakte nach PropertyValue filtern, falls gesetzt
		if (pv.getStringValue() != null) {
			contacts = Filter.filterContactsByString(contacts, pv.getStringValue());
		}
		// Reports für die gefilterten Kontakte erstellen
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
	 * Diese Methode filtert eine Liste aus <code>Contact</code> Objekte nach einem
	 * <code>int</code>, dass in einem <code>PValue</code> mitgegeben wird.
	 * 
	 * @param contacts
	 *            Liste zu filternder <code>Contact</code> Objekte.
	 * @param pv
	 *            <code>PValue</code> Objekt mit dem <code>int</code> Wert.
	 * @return Liste aller <code>ContactReport</code>, welche dem Filterkriterium
	 *         entspricht.
	 */
	public ArrayList<ContactReport> filterContactsByInt(ArrayList<Contact> contacts, PValue pv) {

		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		for (Contact c : contacts) {
			c.setValues(pvMapper.findPValueForContact(c));
		}
		// Kontakte nach Property filtern, falls gesetzt
		if (pv.getProperty().getLabel() != null) {
			contacts = Filter.filterContactsByProperty(contacts, pv.getProperty());
		}
		// Kontakte nach PropertyValue filtern, falls gesetzt
		if (pv.getIntValue() != Integer.MIN_VALUE) {
			contacts = Filter.filterContactsByInt(contacts, pv.getIntValue());
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
	 * Diese Methode filtert eine Liste aus <code>Contact</code> Objekte nach einem
	 * <code>Date</code>, dass in einem <code>PValue</code> mitgegeben wird.
	 * 
	 * @param contacts
	 *            Liste zu filternder <code>Contact</code> Objekte.
	 * @param pv
	 *            <code>PValue</code> Objekt mit dem <code>Date</code> Wert.
	 * @return Liste aller <code>ContactReport</code>, welche dem Filterkriterium
	 *         entspricht.
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
			contacts = Filter.filterContactsByDate(contacts, pv.getDateValue());
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
	 * Diese Methode filtert eine Liste von <code>Contact</code> Objekten nach einem
	 * oder mehreren <code>JabicsUser</code>.
	 * 
	 * @param allUserContacts
	 *            Liste der zu filternden <code>Contact</code> Objekte.
	 * @param finalUser
	 *            Liste der <code>JabicsUser</code> Objekte nach denen gefiltert
	 *            werden soll.
	 * @return Die gefilterte Liste aus den <code>ContactReport</code> Objekten.
	 */
	public ArrayList<ContactReport> filterContactsByCollaborators(ArrayList<Contact> allUserContacts,
			ArrayList<JabicsUser> finalUser) {

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
	 * Diese Methode filtert eine Liste aus <code>Contact</code> Objekte nach einem
	 * <code>float</code>, dass in einem <code>PValue</code> mitgegeben wird.
	 * 
	 * @param contacts
	 *            Liste zu filternder <code>Contact</code> Objekte.
	 * @param pv
	 *            <code>PValue</code> Objekt mit dem <code>float</code> Wert.
	 * @return Liste aller <code>ContactReport</code>, welche dem Filterkriterium
	 *         entspricht.
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
		if (pv.getFloatValue() != -99999997952f) {
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
}