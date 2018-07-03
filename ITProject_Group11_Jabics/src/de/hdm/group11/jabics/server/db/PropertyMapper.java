package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * Die Klasse <code>PropertyMapper</code> realisiert die Abbildung von
 * <code>Property</code> Objekten auf die relationale Datenbank. Sie stellt alle
 * notwendigen Methoden zur Verwaltung der Eigenschaften in der Datenbank zur
 * Verfügung.
 *
 * @author Thies
 * @author Brase
 * @author Stahl
 */
public class PropertyMapper {

	/**
	 * Die Klasse PropertyMapper wird nur einmal instantiiert. Man spricht hierbei
	 * von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 * 
	 * @see propertyMapper()
	 */
	private static PropertyMapper propertyMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected PropertyMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>PropertyMapper.propertyMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>PropertyMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> PropertyMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>PropertyMapper</code>-Objekt.
	 * @see propertyMapper
	 */
	public static PropertyMapper propertyMapper() {
		if (propertyMapper == null) {
			propertyMapper = new PropertyMapper();
		}
		return propertyMapper;
	}

	/**
	 * Diese Methode trägt eine Eigenschaft in die Datenbank ein.
	 * 
	 * @param p
	 *            das <code>Property</code> Objekt, dass in die Datenbank
	 *            eingetragen werden soll.
	 * @return Das als Parameter übergebene- <code>Property</code> Objekt.
	 */
	public Property insertProperty(Property p) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		try {
			// Einfügen der neuen Eigenschaft in die Datenbank.
			String query = ("INSERT INTO property (isStandard, type, name) VALUES " + "(" + p.isStandard() + ", '"
					+ p.getTypeInString() + "' , '" + p.getLabel() + "' ) ");
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			Statement stmt2 = con.createStatement();
			ResultSet rs2;
			while (rs.next()) {
				rs2 = stmt2.executeQuery("SELECT * FROM property WHERE propertyID = " + rs.getInt(1));
				p.setId(rs.getInt(1));

				while (rs2.next()) {
					p.setDateCreated(rs2.getTimestamp("dateCreated"));
					p.setDateUpdated(rs2.getTimestamp("dateUpdated"));
				}
			}
			// Schließen des SQL-Statements
			stmt.close();
			stmt2.close();

			// Schließen der Datenbankverbindung
			con.close();

			return p;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode löscht ein <code>Property</code> Objekt aus der Datenbank.
	 * 
	 * @param p
	 *            das <code>Property</code> Objekt, dass gelöscht werden soll.
	 */
	public void deleteProperty(Property p) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Löschen der Eigenschaft aus der Datenbank.
			stmt.executeUpdate("DELETE FROM property WHERE propertyID = " + p.getId());
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	/**
	 * Diese Methode gibt ein <code>Property</code> Objekt zurück, dass eine
	 * bestimmte ID hat.
	 * 
	 * @param id
	 *            die Id nach welcher gesucht werden soll.
	 * @return Das <code>PValue</code> Objekt mit der gesuchten id.
	 */
	public Property findPropertyById(int id) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Auswählen der Eigenschaften mit einer bestimmten id.
			ResultSet rs = stmt.executeQuery("SELECT * FROM property " + "WHERE PropertyID = " + id);

			// Erzeugen eines Property-Objektes
			Property p = new Property();

			if (rs.next()) {
				// Befüllen des Property-Objekts
				p.setId(rs.getInt("propertyID"));
				p.setStandard(rs.getBoolean("isStandard"));
				p.setLabel(rs.getString("name"));
				p.setType(rs.getString("type"));
				p.setDateCreated(rs.getTimestamp("dateCreated"));
				p.setDateUpdated(rs.getTimestamp("dateUpdated"));

			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			return p;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode gibt ein <code>Property</code> Objekt zurück, dass eine
	 * bestimmte ID hat.
	 * 
	 * @param id
	 *            die Id nach welcher gesucht werden soll.
	 * @return Das <code>PValue</code> Objekt mit der gesuchten id.
	 */
	public ArrayList<Property> findAllStandardPropertys() {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<Property> al = new ArrayList<Property>();

			// Auswählen der Eigenschaften mit einer bestimmten id.
			ResultSet rs = stmt.executeQuery("SELECT * FROM property " + "WHERE isStandard = 1 ");

			while (rs.next()) {
				// Erzeugen eines Property-Objektes
				Property p = new Property();

				// Befüllen des Property-Objekts
				p.setId(rs.getInt("propertyID"));
				p.setStandard(rs.getBoolean("isStandard"));
				p.setLabel(rs.getString("name"));
				p.setType(rs.getString("type"));
				p.setDateCreated(rs.getTimestamp("dateCreated"));
				p.setDateUpdated(rs.getTimestamp("dateUpdated"));
				al.add(p);
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
}