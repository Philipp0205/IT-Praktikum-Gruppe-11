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
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected PropertyMapper() {
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

			// Ausführen des SQL-Statements
			stmt.executeUpdate("DELETE FROM property WHERE propertyID = " + p.getId());

		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	/**
	 * Auslesen aller <code>Property</code> Objekte, welche zu den Standard
	 * Eigenschaften gehören.
	 * 
	 * @return Liste der <code>Property</code> Objekte, welche Standard sind.
	 */
	public ArrayList<Property> findAllStandardPropertys() {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<Property> al = new ArrayList<Property>();

			// SQL-Statement ausführen
			ResultSet rs = stmt.executeQuery("SELECT * FROM property " + "WHERE isStandard = 1 ");

			// Für jedes Tupel in der Datenbank wird ein Property Objekt erstellt und
			// befüllt.
			while (rs.next()) {
				Property p = new Property();
				p.setId(rs.getInt("propertyID"));
				p.setStandard(rs.getBoolean("isStandard"));
				p.setLabel(rs.getString("name"));
				p.setType(rs.getString("type"));
				al.add(p);
			}

			// Rückgabe der ArrayList
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Auslesen eines <code>Property</code> Objekts mit einer bestimmten ID.
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

			// Prüfen ob Tupel vorhanden, wenn ja befüllen des Objekts
			if (rs.next()) {
				p.setId(rs.getInt("propertyID"));
				p.setStandard(rs.getBoolean("isStandard"));
				p.setLabel(rs.getString("name"));
				p.setType(rs.getString("type"));
			}

			// Rückgabe des Property Objekts
			return p;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode trägt ein <code>Property</code> Obejekt in die Datenbank ein.
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
			// Zwei ungefüllte SQL-Statements erzeugen
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			// String mit SQL-Statement erzeugen
			String query = ("INSERT INTO property (isStandard, type, name) VALUES " + "(" + p.isStandard() + ", '"
					+ p.getTypeInString() + "' , '" + p.getLabel() + "' ) ");

			// Ausführen des SQL-Statements und ID verfügbar machen
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

			// Resultsets erzeugen
			ResultSet rs = stmt.getGeneratedKeys();

			// Property Objekt mit ID befüllen
			if (rs.next()) {
				p.setId(rs.getInt(1));
			}

			// Rückgabe des Property Objekts
			return p;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}
}