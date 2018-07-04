package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.BoStatus;

/**
 * Diese Mapper-Klasse realisiert die Abbildung von <code>ContactList</code>
 * Objekten auf die relationale Datenbank. Sie stellt alle notwendigen Methoden
 * zur Verwaltung der Kontaktlisten in der Datenbank zur Verfügung.
 *
 * @author Thies
 * @author Brase
 * @author Stahl
 * 
 */
public class ContactListMapper {

	/**
	 * Die Klasse ContactListMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 * 
	 * @see contactListMapper()
	 */
	private static ContactListMapper contactListMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected ContactListMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>ContactListMapper.contactListMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
	 * Instanz von <code>ContactListMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> ContactListMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>ContactListMapper</code>-Objekt.
	 */
	public static ContactListMapper contactListMapper() {
		if (contactListMapper == null) {
			contactListMapper = new ContactListMapper();
		}
		return contactListMapper;
	}

	/**
	 * Diese Methode trägt ein <code>ContactList</code> Objekt in die Datenbank ein.
	 *
	 * @param cl
	 *            das <code>ContactList</code> Objekt, dass in die Datenbank
	 *            eingetragen werden soll.
	 * @return Das als Parameter übergebene <code>ContactList</code> Objekt.
	 */
	public ContactList insertContactList(ContactList cl) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Strings mit einem SQL-Statement befüllen
			String query = ("INSERT INTO contactList (listname) VALUES ('" + cl.getListName() + "')");

			// Zwei ungefüllt SQL-Statements erstellen
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			// Befüllen, ausführen des SQL-Statements und gesetzte ID verfügbar machen
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

			// Erzeugen von zwei ResultSets
			ResultSet rs = stmt.getGeneratedKeys();
			ResultSet rs2;

			// Wenn ein Tupel existiert wird die ID gesetzt
			if (rs.next()) {
				rs2 = stmt2.executeQuery(
						"SELECT dateCreated, dateUpdated FROM contactList WHERE contactListID = " + rs.getInt(1));
				cl.setId(rs.getInt(1));
				// Wenn ein Tupel existiert wird das Erstellungdatum und letzte Update gesetzt
				if (rs2.next()) {
					cl.setDateCreated(rs2.getTimestamp("dateCreated"));
					cl.setDateUpdated(rs2.getTimestamp("dateUpdated"));
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

			// Rückgabe des ContactList Objekts
			return cl;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode fügt einem <code>ContactList</code> Objekt ein
	 * <code>Contact</code> Objekt in der Datenbank hinzu. Dazu mussen in der
	 * Datenbank ein neues Tupel in der Kontakt-Kontaktliste-Tabelle angelegt
	 * werden.
	 * 
	 * @param cl
	 *            das <code>ContactList</code> Objekt, welchem das
	 *            <code>Contact</code> Objekt zugeordnet wird.
	 * @param c
	 *            das <code>ContactList</code> Objekt, welches einer
	 *            <code>ContactList</code> hinzugefügt werden soll.
	 * @return Das als Parameter übergebene <code>ContactList</code> Objekt.
	 */
	public ContactList insertContactIntoContactList(ContactList cl, Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Verknüpfungen zwischen Kontaktliste und Kontakten erzeugen.
			// Befüllen und ausführen des SQL-Statements
			stmt.executeUpdate("INSERT INTO contactContactLists (contactID, contactListID) VALUES " + "(" + c.getId()
					+ ", " + cl.getId() + ")");

			// Erzeugen eines zweiten ungefüllten SQL-Statements
			Statement stmt2 = con.createStatement();

			// Update des letzten Updates der Kontaktliste.
			// Befüllen und ausführen des SQL-Statements
			stmt2.executeUpdate(
					"UPDATE contactList SET dateUpdated = CURRENT_TIMESTAMP WHERE contactListID = " + cl.getId());

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

			// Rückgabe des ContactList Objekts
			return cl;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode trägt eine Teilhaberschaft eines <code>JabicsUser</code>
	 * Objekts zu einem <code>ContactList</code> Objekt in die Datenbank ein.
	 * 
	 * @param u
	 *            der <code>JabicsUser</code> der an der <code>ContactList</code>
	 *            Teilhaberschaftsrechte erlangen soll.
	 * @param cl
	 *            die <code>ContactList</code> an welcher ein der
	 *            <code>JabicsUser</code> eine Teilhaberschaft bekommen soll.
	 * @param IsOwner
	 *            ein <code>boolean</code> Wert der wiederspiegelt ob der
	 *            zuzuweisende Teilhaber auch der Besitzer ist.
	 * @return Das übergebene <code>ContactList</code> Objekt
	 */
	public ContactList insertCollaboration(JabicsUser u, ContactList cl, boolean IsOwner) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Einfügen einer neuen Teilhaberschaft mit Eigentümerschaft in die
			// ContactlistCollaboration Tabelle.
			// Befüllen und ausführen des SQL-Statements
			stmt.executeUpdate("INSERT INTO contactlistCollaboration (isOwner, contactListID, systemUserID) VALUES "
					+ "(" + IsOwner + ", " + cl.getId() + ", " + u.getId() + ")");

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des ContactList Objekts
			return cl;
		} catch (SQLException e) {
			System.err.print(e);
		}
		return null;
	}

	/**
	 * Diese Methode aktualisiert den Namen des <code>ContactList</code> Objekts in
	 * der Datenbank.
	 * 
	 * @param cl
	 *            das <code>ContactList</code> Objekt, dass aktualisiert werden
	 *            soll.
	 * @return Das als Parameter übergebene <code>ContactList</code> Objekt.
	 */
	public ContactList updateContactList(ContactList cl) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Zwei ungefüllte SQL-Statements erzeugen
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			// Update des Namens der Kontaktliste
			// Befüllen und ausführen des ersten SQL-Statements
			stmt.executeUpdate(
					"UPDATE contactList SET listname = '" + cl.getListName() + "' WHERE contactListID = " + cl.getId());

			// Auslesen des letzten Updates
			// Befüllen und ausführen des zweiten SQL-Statements
			ResultSet rs = stmt2
					.executeQuery("SELECT dateUpdated FROM contactList WHERE contactListID = " + cl.getId());

			// Wenn ein Tupel in der Datenbank existiert wird das letzte Update ausgelesen
			if (rs.next()) {
				cl.setDateUpdated(rs.getTimestamp("dateUpdated"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des ContactList Objekts
			return cl;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode löscht ein <code>ContactList</code> Objekt aus der Datenbank.
	 * 
	 * @param cl
	 *            das <code>ContactList</code> Objekt, dass gelöscht werden soll.
	 */
	public void deleteContactList(ContactList cl) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Löschen des <code>ContactList</code> Objekts aus der Datenbank.
			// Befüllen und ausführen des SQL-Statements
			stmt.executeUpdate("DELETE FROM contactList WHERE contactListID = " + cl.getId());

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
	 * Diese Methode löscht ein <code>Contact</code> Objekt aus einer
	 * <code>ContactList</code>.
	 * 
	 * @param cl
	 *            das <code>ContactList</code> Objekt, aus welchem der Kontakt
	 *            gelöscht werden soll.
	 * @param c
	 *            das <code>Contact</code> Objekt, dass aus der Liste gelöscht
	 *            werden soll.
	 */
	public void deleteContactfromContactList(ContactList cl, Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Zwei ungefüllte SQL-Statements erzeugen
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			// Löschen des Kontakts aus der Liste.
			// Befüllen und ausführen des ersten SQL-Statements
			stmt.executeUpdate("DELETE FROM contactContactLists WHERE contactID = " + c.getId()
					+ " AND contactListID = " + cl.getId());

			// Update des letzten Updates der Kontaktliste.
			// Befüllen und ausführen des zweiten SQL-Statements
			stmt2.executeUpdate(
					"UPDATE contactList SET dateUpdated = CURRENT_TIMESTAMP WHERE contactListID = " + cl.getId());

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
		}
	}

	/**
	 * Diese Methode löscht eine Teilhaberschaft zwischen einem
	 * <code>JabicsUser</code> Objekt und einem <code>ContactList</code> Objekt.
	 * 
	 * @param cl
	 *            die ausgewählte Kontaktliste.
	 * @param u
	 *            der Nutzer der die Teilhaberschaft zu der Kontaktliste verlieren
	 *            soll.
	 */
	public void deleteCollaboration(ContactList cl, JabicsUser u) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Löschen einer Teilhaberschaft aus der ContactlistCollaboration Tabelle.
			// Befüllen und ausführen des SQL-Statements
			stmt.executeUpdate("DELETE FROM contactlistCollaboration WHERE contactListID =" + cl.getId()
					+ " AND systemUserID = " + u.getId());

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
	 * Diese Methode gibt ein <code>ContactList</code> Objekt zurück, dass eine
	 * bestimmte ID hat.
	 * 
	 * @param id
	 *            die Id nach welcher gesucht werden soll.
	 * @return Das <code>ContactList</code> Objekt mit der gesuchten id.
	 */
	public ContactList findContactListById(int id) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen eines Kontaktlisten-Objektes
			ContactList cl = new ContactList();

			// Auswählen eines Contaktlistenobjekts mit einer bestimmten ID.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT * FROM contactList " + "WHERE contactListID = " + id);

			// Befüllen des Kontaktlisten Objekts
			if (rs.next()) {
				cl.setId(rs.getInt("contactListID"));
				cl.setListName(rs.getString("listname"));
				cl.setDateCreated(rs.getTimestamp("dateCreated"));
				cl.setDateUpdated(rs.getTimestamp("dateUpdated"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe der Kontaktliste
			return cl;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode gibt alle <code>ContactList</code> Objekte zurück, die einen
	 * bestimmten Teilhaber haben haben.
	 * 
	 * @param u
	 *            der Teilhaber, dessen <code>ContactList</code> Objekte
	 *            zurückgegeben werden sollen.
	 * @return Die ArrayList, die mit den <code>ContactList</code> Objekten befüllt
	 *         ist.
	 */
	public ArrayList<ContactList> findContactListOfUser(JabicsUser u) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<ContactList> al = new ArrayList<ContactList>();

			// Join zwischen ContactList und ContactListCollaboration und Auswählen der
			// Stellen mit einer bestimmten User-ID.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery(
					"SELECT contactList.contactListID, contactList.listname, contactList.dateCreated, contactList.dateUpdated"
							+ " FROM contactList"
							+ " LEFT JOIN contactlistCollaboration ON contactList.contactListID = contactlistCollaboration.contactListID"
							+ " WHERE contactlistCollaboration.systemUserID =" + u.getId());

			// Für jedes Tupel in der Datenbank wird ein Kontaktlisten Objekt erstellt, mit
			// Werten
			// befüllt und an die Liste angehängt
			while (rs.next()) {
				ContactList cl = new ContactList();
				cl.setId(rs.getInt("contactListID"));
				cl.setListName(rs.getString("listname"));
				cl.setDateCreated(rs.getTimestamp("dateCreated"));
				cl.setDateUpdated(rs.getTimestamp("dateUpdated"));
				al.add(cl);
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe der Liste
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Auslesen einer Liste von <code>JabicsUser</code> Objekten, welche eine
	 * Teilhaberschaft an einem <code>ContactList</code> Objekt besitzen.
	 * 
	 * @param cl
	 *            Das <code>ContactList</code> Objekt, dessen Teilhaber gesucht
	 *            werden.
	 * @return Die <code>ArrayList</code> mit den Teilhabern.
	 */
	public ArrayList<JabicsUser> findCollaborators(ContactList cl) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();

			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID , systemUser.email, systemUser.name "
					+ " FROM systemUser "
					+ " LEFT JOIN contactlistCollaboration ON systemUser.systemUserID = contactlistCollaboration.systemUserID "
					+ " WHERE contactListID = " + cl.getId());

			// Für jedes Tupel in der Datenbank wird ein Nutzer Objekt erstellt, mit Werten
			// befüllt und an die Liste angehängt
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

			// Rückgabe der Liste
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Auslesen aller <code>BoStatus</code> aus einer Liste von
	 * <code>ContactList</code> Objekten.
	 * 
	 * @param alContactList
	 *            Die <code>ArrayList</code> aus <code>ContactList</code> Objekten,
	 *            für welche der Share Status benötigt wird.
	 * @return <code>ArrayList</code> mit <code>BoStatus</code>
	 */
	public ArrayList<BoStatus> findShareStatus(ArrayList<ContactList> alContactList) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer neuen ArrayList
			ArrayList<BoStatus> al = new ArrayList<BoStatus>();

			// Erzeugen eines neuen StringBuffers
			StringBuffer contactListIDs = new StringBuffer();

			// contactListIDs an den StringBuffer anhängen
			if (!alContactList.isEmpty()) {
				for (ContactList cl : alContactList) {
					contactListIDs.append(cl.getId());
					contactListIDs.append(",");
				}
				// Letztes Komma im StringBuffer löschen
				contactListIDs.deleteCharAt(contactListIDs.lastIndexOf(","));
			} else {
				return null;
			}

			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT contactListID " + " FROM contactlistCollaboration "
					+ " WHERE isOwner = 0 AND contactListID IN (" + contactListIDs + ")");

			// Erzeugen einer neuen ArrayList
			ArrayList<Integer> ids = new ArrayList<Integer>();

			// Für jedes Tupel wird die contactListID an die ArrayList angehängt
			while (rs.next()) {
				ids.add(new Integer(rs.getInt("contactListID")));
			}

			// Setzen des Shared Status für jede ContactList in ArrayList<ContactList>
			// Wenn die übergebene contactListID in der Datenbank steht, ist die
			// Kontaktliste geteilt.
			for (ContactList cl : alContactList) {
				Boolean bol = false;
				for (Integer i : ids) {
					if (i.equals(cl.getId())) {
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