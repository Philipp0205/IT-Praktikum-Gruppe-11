package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * Struktur von
 * 
 * @author Thies
 * 
 *         Angepasst von
 * @author Brase
 * @author Stahl
 * 
 *         Diese Mapper-Klasse realisiert die Abbildung von <code>User</code>
 *         Objekten auf die relationale Datenbank. Sie stellt alle notwendigen
 *         Methoden zur Verwaltung der User in der Datenbank zur Verfügung.
 */
public class UserMapper {

	/**
	 * Die Klasse UserMapper wird nur einmal instantiiert. Man spricht hierbei von
	 * einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 * 
	 * @see userMapper()
	 */
	private static UserMapper userMapper = null;

	/**
	 * 
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected UserMapper() {
	}

	/**
	 * 
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>UserMapper.userMapper()</code>. Sie stellt die Singleton-Eigenschaft
	 * sicher, indem Sie dafür sorgt, dass nur eine einzige Instanz von
	 * <code>UserMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> UserMapper sollte nicht mittels <code>new</code> instantiiert
	 * werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>UserMapper</code>-Objekt.
	 * @see userMapper
	 */
	public static UserMapper userMapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}
		return userMapper;
	}

	/**
	 * Diese Methode trägt ein <code>User</code> Objekt in die Datenbank ein.
	 * 
	 * @param u
	 *            das <code>User</code> Objekt, dass in die Datenbank eingetragen
	 *            werden soll.
	 * @return Das als Parameter übergebene- <code>User</code> Objekt.
	 */
	public JabicsUser insertUser(JabicsUser u) {

		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Einfügen des Users in die Datenbank.
			String query = ("INSERT INTO systemUser (email, name) VALUES " + "('" + u.getEmail() + "','"
					+ u.getUsername() + "')");

			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				u.setId(rs.getInt(1));
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			return u;
		} catch (SQLException e) {
			System.err.print(e);
		}
		return null;
	}

	/**
	 * Diese Methode löscht ein <code>User</code> Objekt aus der Datenbank.
	 * 
	 * @param u
	 *            das <code>User</code> Objekt, dass gelöscht werden soll.
	 */
	public void deleteUser(JabicsUser u) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Löschen des Users.
			stmt.executeUpdate("DELETE FROM systemUser WHERE systemUserID = " + u.getId());
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	/**
	 * Diese Methode erlaubt die Suche eines <code>User</code> Objekts in der
	 * Datenbank.
	 * 
	 * @param id
	 *            Die id nach der gesucht werden soll.
	 * @return Das gesuchte <code>User</code> Objekt.
	 */
	public JabicsUser findUserById(int id) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		JabicsUser u = new JabicsUser();
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Auswählen aller User aus der Datenbank, die eine bestimmte ID haben.
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser " + " WHERE systemUserID = " + id);

			if (rs.next()) {

				// Befüllen des Kontakt-Objekts
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			return u;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}

	}

	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code>
	 * Objekten eines <code>User</code> Objekts aus der Datenbank zurück.
	 * 
	 * @param u
	 *            das <code>User</code> Objekt, dessen Kontakte wiedergegeben werden
	 *            sollen.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten des
	 *         <code>User</code> Objekts.
	 */
	public ArrayList<JabicsUser> findAllUser() {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();

			// Auswählen der <code>User</code> Objekte geordnet nach ihrer E-Mail Adresse.
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser ORDER BY email");

			while (rs.next()) {

				// Erstellen eines User-Objekts
				JabicsUser u = new JabicsUser();

				// Befüllen des Kontakt-Objekts und Einfügen in die Arraylist.
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
				al.add(u);
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Gibt den Besitzer/Ersteller eines Kontakts zurück.
	 * 
	 * @param Contact
	 *            c Der Kontakt für den der Besitzer gefunden werden soll
	 * @return JabicsUser u
	 */
	public JabicsUser findUserByContact(Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		// Erzeugen eines neuen JabicUser-Objekts
		JabicsUser u = new JabicsUser();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUser und ContactCollaboration um den Besitzer eines
			// Kontaktes zu finden.
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name"
					+ " FROM systemUser"
					+ " LEFT JOIN contactCollaboration ON systemUser.systemUserID = contactCollaboration.systemUserID"
					+ " WHERE contactCollaboration.contactID = " + c.getId() + " AND isOwner = 1");
			while (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

		} catch (SQLException e) {
			System.err.print(e);
		}
		return u;
	}

	public JabicsUser findUserByContactList(ContactList cl) {

		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		// Erzeugen eines neuen JabicUser-Objekts
		JabicsUser u = new JabicsUser();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUser und ContactlistCollaboration um den Besitzer einer
			// Kontaktliste zu finden.
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name "
					+ " FROM systemUser"
					+ " LEFT JOIN contactlistCollaboration ON systemUser.systemUserID = contactlistCollaboration.systemUserID"
					+ " WHERE contactlistCollaboration.contactListID = " + cl.getId() + " AND isOwner = 1");

			while (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

		} catch (SQLException e) {
			System.err.print(e);
		}
		return u;
	}

	public JabicsUser findUserByPValue(PValue pv) {

		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		// Erzeugen eines neuen JabicUser-Objekts
		JabicsUser u = new JabicsUser();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUser und pValueCollaboration um den Besitzer einer
			// Ausprägung zu finden.
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name "
					+ " FROM systemUser"
					+ " LEFT JOIN pValueCollaboration ON systemUser.systemUserID = pValueCollaboration.systemUserID"
					+ " WHERE pValueCollaboration.pValueID = " + pv.getId() + " AND isOwner = 1");

			if (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

		} catch (SQLException e) {
			System.err.print(e);
		}
		return u;
	}

	/**
	 * Diese Methode erlaubt die Suche eines <code>User</code> Objekts in der
	 * Datenbank nach seiner E-Mail-Adresse.
	 * 
	 * @param email
	 *            die email nach der gesucht werden soll.
	 * @return das gesuchte <code>User</code> Objekt.
	 */
	public JabicsUser findUserByEmail(String email) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Auswählen aller User aus der Datenbank, die eine bestimmte ID haben.
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser " + " WHERE email = '" + email + "'");

			if (rs.next()) {
				JabicsUser u = new JabicsUser();

				// Befüllen des Kontakt-Objekts
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));

				// Schließen des SQL-Statements
				stmt.close();

				// Schließen der Datenbankverbindung
				con.close();

				return u;
			} else
				return null;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}
}