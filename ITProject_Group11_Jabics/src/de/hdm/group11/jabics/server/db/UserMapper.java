package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * Diese Mapper-Klasse realisiert die Abbildung von <code>JabicsUser</code>
 * Objekten auf die relationale Datenbank. Sie stellt alle notwendigen Methoden
 * zur Verwaltung der User in der Datenbank zur Verfügung.
 * 
 * @author Thies
 * @author Brase
 * @author Stahl
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
	 * Diese Methode trägt ein <code>JabicsUser</code> Objekt in die Datenbank ein.
	 * 
	 * @param u
	 *            das <code>JabicsUser</code> Objekt, dass in die Datenbank
	 *            eingetragen werden soll.
	 * @return Das als Parameter übergebene <code>JabicsUser</code> Objekt.
	 */
	public JabicsUser insertUser(JabicsUser u) {

		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Strings mit einem SQL-Statement befüllen
			String query = ("INSERT INTO systemUser (email, name) VALUES " + "('" + u.getEmail() + "','"
					+ u.getUsername() + "')");

			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Ausführen des SQL-Statements und gesetzte ID verfügbar machen
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

			// Auslesen der gesetzten ID
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				u.setId(rs.getInt(1));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des JabicsUsers mit ID
			return u;
		} catch (SQLException e) {
			System.err.print(e);
		}
		return null;
	}

	/**
	 * Diese Methode löscht ein <code>JabicsUser</code> Objekt aus der Datenbank.
	 * 
	 * @param u
	 *            das <code>JabicsUser</code> Objekt, dass gelöscht werden soll.
	 */
	public void deleteUser(JabicsUser u) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Befüllen und ausführen des SQL-Statements
			stmt.executeUpdate("DELETE FROM systemUser WHERE systemUserID = " + u.getId());

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
	 * Diese Methode erlaubt die Suche eines <code>JabicsUser</code> Objekts mit der
	 * ID
	 * 
	 * @param id
	 *            Die id nach der gesucht werden soll.
	 * @return Das gesuchte <code>JabicsUser</code> Objekt.
	 */
	public JabicsUser findUserById(int id) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser " + " WHERE systemUserID = " + id);

			// Erzeugen eines Nutzer Objekts
			JabicsUser u = new JabicsUser();

			// Wenn ein Tupel existiert wird das Nutzer Objekt mit Werten befüllt
			if (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des Nutzer Objekts
			return u;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}

	}

	/**
	 * Auslesen aller <code>JabicsUser</code> Objekte aus der Datenbank
	 * 
	 * @return Liste mit allen <code>JabicsUser</code> Objekten.
	 */
	public ArrayList<JabicsUser> findAllUser() {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();

			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser ORDER BY email");

			// Für jedes Tupel in der Datenbank wird ein Nutzer Objekt erstellt, mit Werten
			// befüllt und an die Liste angehängt
			while (rs.next()) {
				JabicsUser u = new JabicsUser();
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
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

			// Rückgabe der Liste mit Nutzer Objekten
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Auslesen eines <code>JabicsUser</code> Objektes, welches der Besitzer eines
	 * <code>Contact</code> Objektes ist.
	 * 
	 * @param c
	 *            <code>Contact</code> Objekt für welches der Beitzer gesucht wird.
	 * @return Besitzer in Form eines <code>JabicsUser</code> Objektes
	 */
	public JabicsUser findUserByContact(Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUser und ContactCollaboration um den Besitzer eines
			// Kontaktes zu finden.
			// Befüllen und ausführen des Nutzer Objekts
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name"
					+ " FROM systemUser"
					+ " LEFT JOIN contactCollaboration ON systemUser.systemUserID = contactCollaboration.systemUserID"
					+ " WHERE contactCollaboration.contactID = " + c.getId() + " AND isOwner = 1");

			// Erzeugen eines neuen JabicUser-Objekts
			JabicsUser u = new JabicsUser();

			// Wenn ein Tupel existiert wird das Nutzer Objekt befüllt
			if (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des Nutzer Objekts
			return u;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Auslesen eines <code>JabicsUser</code> Objektes, welches der Besitzer eines
	 * <code>ContactList</code> Objektes ist.
	 * 
	 * @param cl
	 *            <code>ContactList</code> Objekt für welches der Beitzer gesucht
	 *            wird.
	 * @return Besitzer in Form eines <code>JabicsUser</code> Objektes
	 */
	public JabicsUser findUserByContactList(ContactList cl) {

		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUser und ContactlistCollaboration um den Besitzer einer
			// Kontaktliste zu finden.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name "
					+ " FROM systemUser"
					+ " LEFT JOIN contactlistCollaboration ON systemUser.systemUserID = contactlistCollaboration.systemUserID"
					+ " WHERE contactlistCollaboration.contactListID = " + cl.getId() + " AND isOwner = 1");

			// Erzeugen eines neuen JabicUser-Objekts
			JabicsUser u = new JabicsUser();

			// Wenn ein Tupel existiert wird das Nutzer Objekt befüllt
			if (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}
			// Rückgabe des Nutzer Objekts
			return u;

		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Auslesen eines <code>JabicsUser</code> Objektes, welches der Besitzer eines
	 * <code>PValue</code> Objektes ist.
	 * 
	 * @param pv
	 *            <code>PValue</code> Objekt für welches der Beitzer gesucht wird.
	 * @return Besitzer in Form eines <code>JabicsUser</code> Objektes
	 */
	public JabicsUser findUserByPValue(PValue pv) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUser und pValueCollaboration um den Besitzer einer
			// Ausprägung zu finden.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name "
					+ " FROM systemUser"
					+ " LEFT JOIN pValueCollaboration ON systemUser.systemUserID = pValueCollaboration.systemUserID"
					+ " WHERE pValueCollaboration.pValueID = " + pv.getId() + " AND isOwner = 1");

			// Erzeugen eines neuen JabicUser-Objekts
			JabicsUser u = new JabicsUser();

			// Wenn ein Tupel existiert wird das Nutzer Objekt befüllt
			if (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}

			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}
			// Rückgabe des Nutzer Objekts
			return u;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode erlaubt die Suche eines <code>JabicsUser</code> Objekts in der
	 * Datenbank nach seiner E-Mail-Adresse.
	 * 
	 * @param email
	 *            die E-Mail-Adresse, für welche das <code>JabicsUser</code> Objekt
	 *            gesucht wird.
	 * @return das gesuchte <code>JabicsUser</code> Objekt.
	 */
	public JabicsUser findUserByEmail(String email) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Auswählen aller User aus der Datenbank, die eine bestimmte ID haben.
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser " + " WHERE email = '" + email + "'");

			// Erzeugen eines Nutzer Objekts
			JabicsUser u = new JabicsUser();

			// Wenn ein Tupel existiert wird das Nutzer Objekt befüllt
			if (rs.next()) {
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
			}
			// Prüfen ob offene Statements oder eine Datenbankverbindung bestehen, falls ja,
			// werden diese geschlossen.
			if (!stmt.isClosed()) {
				stmt.close();
			}
			if (!con.isClosed()) {
				con.close();
			}

			// Rückgabe des Nutzer Objekts
			return u;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}
}