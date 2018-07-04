package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * Diese Mapper-Klasse realisiert die Abbildung von <code>Contact</code>
 * Objekten auf die relationale Datenbank. Sie stellt alle notwendigen Methoden
 * zur Verwaltung der Kontakte in der Datenbank zur Verfügung.
 * 
 * @author Thies
 * @author Brase
 * @author Stahl
 *
 */
public class ContactMapper {

	/**
	 * Die Klasse ContactMapper wird nur einmal instantiiert. Man spricht hierbei
	 * von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * 
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 * 
	 * @see contactMapper()
	 */
	private static ContactMapper contactMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected ContactMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>ContactMapper.contactMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>ContactMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> ContactMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>ContactMapper</code>-Objekt.
	 * @see contactMapper
	 */
	public static ContactMapper contactMapper() {
		if (contactMapper == null) {
			contactMapper = new ContactMapper();
		}
		return contactMapper;
	}

	/**
	 * Diese Methode trägt einen Kontakt in die Datenbank ein.
	 * 
	 * @param c das <code>Contact</code> Objekt, dass in die Datenbank eingetragen
	 *          werden soll.
	 * @return Das als Parameter übergebene- <code>Contact</code> Objekt.
	 */
	public Contact insertContact(Contact c) {

		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Zwei ungefüllte SQL-Statements erzeugen
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			// String mit SQL-Statement erzeugen
			String query = ("INSERT INTO contact (nickname) VALUES ('" + c.getName() + "') ");

			// Ausführen des SQL-Statements und ID verfügbar machen
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

			// Resultsets erzeugen
			ResultSet rs = stmt.getGeneratedKeys();
			ResultSet rs2;

			// Contact Objekt mit ID, Erstellungsdatum und letztem Update befüllen
			if (rs.next()) {
				rs2 = stmt2.executeQuery("SELECT * FROM contact WHERE contactID = " + rs.getInt(1));
				c.setId(rs.getInt(1));
				if (rs2.next()) {
					c.setDateCreated(rs2.getTimestamp("dateCreated"));
					c.setDateUpdated(rs2.getTimestamp("dateUpdated"));
				}
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!stmt2.isClosed()) {
				stmt2.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des Objekts.
			return c;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode trägt eine Teilhaberschaft eines <code>JabicsUser</code>
	 * Objekts zu einem <code>Contact</code> Objekt in die Datenbank ein.
	 * 
	 * @param u       der User der an einem Kontakt Teilhaberschaftsrechte erlangen
	 *                soll.
	 * @param c       der Kontakt an dem ein User Teilhaberschaft haben soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der
	 *                zuzuweisende Teilhaber auch der Owner ist.
	 * @return Das übergebene <code>Contact</code> Objekt
	 */
	public Contact insertCollaboration(JabicsUser u, Contact c, boolean IsOwner) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Befüllen und ausführen des SQL-Statements
			stmt.executeUpdate("INSERT INTO contactCollaboration (isOwner, contactID, systemUserID) VALUES " + "("
					+ IsOwner + ", " + c.getId() + ", " + u.getId() + ")");

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des Contact-Objekts
			return c;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode aktualisiert ein <code>Contact</code> Objekt in der Datenbank.
	 * 
	 * @param c das <code>Contact</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter übergebene- <code>Contact</code> Objekt.
	 */
	public Contact updateContact(Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Zwei ungefüllte SQL-Statements erzeugen
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			// Befüllen und ausführen des ersten SQL-Statements
			stmt.executeUpdate("UPDATE contact SET nickname = '" + c.getName() + "' WHERE contactID = " + c.getId());

			// Befüllen und ausführen des zweiten SQL-Statements
			ResultSet rs = stmt2.executeQuery("SELECT dateUpdated FROM contact WHERE contactID = " + c.getId());

			// Auslesen und setzen des letzten Updates
			if (rs.next()) {
				c.setDateUpdated(rs.getTimestamp("dateUpdated"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!stmt2.isClosed()) {
				stmt2.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
		// Rückgabe des Contact Objekts.
		return c;
	}

	/**
	 * Diese Methode löscht ein <code>Contact</code> Objekt aus der Datenbank.
	 * 
	 * @param c Das <code>Contact</code> Objekt, dass gelöscht werden soll.
	 * 
	 */
	public void deleteContact(Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Zwei ungefüllt SQL-Statements erstellen
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			// Befüllen und ausführen des ersten SQL-Statements
			stmt.executeUpdate("DELETE FROM contactContactLists WHERE contactID = " + c.getId());

			// Befüllen und ausführen des zweiten SQL-Statements
			stmt2.executeUpdate("DELETE FROM contact WHERE contactID = " + c.getId());

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	/**
	 * Diese Methode löscht eine Teilhaberschaft zwischen einem
	 * <code>JabicsUser</code> Objekt und einem <code>Contact</code> Objekt.
	 * 
	 * @param c das <code>Contact</code> Objekt, zu welchem die Teilhaberschaft
	 *          gelöscht werden soll.
	 * @param u das <code>JabicsUser</code> Objekt, welches die Teilhaberschaft zu
	 *          dem <code>Contact</code> Objekt verlieren soll.
	 */
	public void deleteCollaboration(Contact c, JabicsUser u) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Befüllen und ausführen des SQL-Statements
			stmt.executeUpdate("DELETE FROM contactCollaboration WHERE systemUserID = " + u.getId()
					+ " AND contactID = " + c.getId());

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code>
	 * Objekten eines <code>JabicsUser</code> Objekts aus der Datenbank zurück.
	 * 
	 * @param u Das <code>JabicsUser</code> Objekt, dessen Kontakte wiedergegeben
	 *          werden sollen.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten des
	 *         <code>JabicsUser</code> Objekts.
	 */
	public ArrayList<Contact> findAllContacts(JabicsUser u) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		System.out.println("Alle Kontakte Finden");

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<Contact> al = new ArrayList<Contact>();

			// Join zwischen Contact und ContactCollaboration und Auswählen der Stellen mit
			// einer bestimmten UserID.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt
					.executeQuery("SELECT contact.contactID, contact.dateCreated, contact.dateUpdated, contact.nickname"
							+ " FROM contact"
							+ " LEFT JOIN contactCollaboration ON contact.contactID = contactCollaboration.contactID"
							+ " WHERE contactCollaboration.systemUserID = " + u.getId());

			// Für jedes Tupel wird ein Nutzer erstellt, mit Werten befüllt und an die
			// ArrayList angehängt.
			while (rs.next()) {
				Contact c = new Contact();
				c.setId(rs.getInt("contactID"));
				c.setDateCreated(rs.getTimestamp("dateCreated"));
				c.setDateUpdated(rs.getTimestamp("dateUpdated"));
				c.setName(rs.getString("nickname"));
				al.add(c);
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe der mit <code>Contact</code>-Objekt befüllten ArrayList.
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode gibt ein <code>Contact</code> Objekt zurück, dass eine
	 * bestimmte ID hat.
	 * 
	 * @param id Die Id nach welcher gesucht werden soll.
	 * @return Das <code>Contact</code> Objekt mit der gesuchten id.
	 */
	public Contact findContactById(int id) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Befüllen und ausführen eines SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT * FROM contact WHERE contactID = " + id);

			// Erzeugen eines Contact Objektes
			Contact c = new Contact();

			// Befüllen des Contact Objekts
			if (rs.next()) {
				c.setId(rs.getInt("contactID"));
				c.setDateCreated(rs.getTimestamp("dateCreated"));
				c.setDateUpdated(rs.getTimestamp("dateUpdated"));
				c.setName(rs.getString("nickname"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des Contact Objekts
			return c;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Mit dieser Methode werden alle <code>Contact</code> Objekte einer bestimmten
	 * Liste aus der Datenbank abgerufen.
	 *
	 * @param cl das <code>ContactList</code> Objekt aus welchem alle Kontakte
	 *           ermittelt werden sollen.
	 * @return Die <code>Contact</code> Objekte in Form einer ArrayList.
	 */

	public ArrayList<Contact> findContactsOfContactList(ContactList cl) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<Contact> al = new ArrayList<Contact>();

			// Join zwischen Contact und ContactContactlist um <code>Contact</code> Objekte
			// einer Liste auszuwählen.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt
					.executeQuery("SELECT contact.contactID, contact.dateCreated, contact.dateUpdated, contact.nickname"
							+ " FROM contact"
							+ " LEFT JOIN contactContactLists ON contact.contactID = contactContactLists.contactID"
							+ " WHERE contactContactLists.contactListID = " + cl.getId());

			// Für jedes Tupel wird ein Contact Objekt erstellt, mit Werten befüllt und an
			// die ArrayList angehängt.
			while (rs.next()) {
				Contact c = new Contact();
				c.setId(rs.getInt("contactID"));
				c.setDateCreated(rs.getTimestamp("dateCreated"));
				c.setDateUpdated(rs.getTimestamp("dateUpdated"));
				c.setName(rs.getString("nickname"));
				al.add(c);
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe der mit Contact Objekten befüllten ArrayList
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen
	 * <code>JabicsUser</code> Objekten die eine Teilhaberschaft an einem bestimmten
	 * Kontakt besitzen.
	 * 
	 * @param c das <code>Contact</code> Objekt, dessen Teilhaber gesucht werden.
	 * @return Die <code>ArrayList</code> mit den Teilhabern.
	 */
	public ArrayList<JabicsUser> findCollaborators(Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();

			// Auswählen von Usern mit einer Bestimmten ID in der contactCollaboration
			// Tabelle.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name"
					+ " FROM systemUser"
					+ " LEFT JOIN contactCollaboration ON systemUser.systemUserID = contactCollaboration.systemUserID"
					+ " WHERE contactCollaboration.contactID = " + c.getId());

			// Für jedes Tupel wird ein Nutzer erstellt, mit Werten befüllt und an die
			// ArrayList angehängt.
			while (rs.next()) {
				JabicsUser u = new JabicsUser(rs.getString("email"));
				u.setId(rs.getInt("systemUserID"));
				u.setUsername(rs.getString("name"));
				al.add(u);
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe der mit JabicsUsern befüllten ArrayList
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode ermittelt den Share-Status von in einer Liste übergebenen
	 * <code>Contact</code> Objekten
	 * 
	 * @param alContact ArrayList mit <code>Contact</code> Objekten
	 * @return ArrayList, welche den BoStatus der übergebenen Kontakte enthält
	 */
	public ArrayList<BoStatus> findShareStatus(ArrayList<Contact> alContact) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<BoStatus> al = new ArrayList<BoStatus>();

			// Erzeugen eines StringBuffers
			StringBuffer contactIDs = new StringBuffer();

			// contactIDs an den StringBuffer anhängen
			for (Contact c : alContact) {
				contactIDs.append(c.getId());
				contactIDs.append(",");
			}

			// Letztes Komma im StringBuffer löschen
			contactIDs.deleteCharAt(contactIDs.lastIndexOf(","));

			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT contactID " + " FROM contactCollaboration "
					+ " WHERE isOwner = 0 AND contactID IN (" + contactIDs + ")");

			// Erzeugen einer ArrayList
			ArrayList<Integer> ids = new ArrayList<Integer>();

			// Für jedes Tupel wird die contactID an die ArrayList angehängt
			while (rs.next()) {
				ids.add(new Integer(rs.getInt("contactID")));
			}

			// Setzen des Shared Status für jeden Contact in ArrayList<Contact>.
			// Wenn die übergebene contactID in der Datenbank steht, ist der Kontakt
			// geteilt.
			for (Contact c : alContact) {
				Boolean bol = false;
				for (Integer i : ids) {
					if (i.equals(c.getId())) {
						bol = true;
					}
				}
				if (bol) {
					al.add(BoStatus.IS_SHARED);
				} else {
					al.add(BoStatus.NOT_SHARED);
				}
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe der ArrayList<BoStatus>
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}
}