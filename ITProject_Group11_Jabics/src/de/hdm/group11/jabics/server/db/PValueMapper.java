package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * @author Brase
 * @author Stahl
 * 
 *         Diese Mapper-Klasse realisiert die Abbildung von <code>PValue</code>
 *         Objekten auf die relationale Datenbank. Sie stellt alle notwendigen
 *         Methoden zur Verwaltung der Eigenschaftsausprägungen in der Datenbank
 *         zur Verfügung.
 *
 */
public class PValueMapper {

	/**
	 * Struktur von
	 * 
	 * @author Thies
	 * 
	 *         Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 *         Die Klasse PValueMapper wird nur einmal instantiiert. Man spricht
	 *         hierbei von einem sogenannten <b>Singleton</b>.
	 *         <p>
	 *         Diese Variable ist durch den Bezeichner <code>static</code> nur
	 *         einmal für sämtliche eventuellen Instanzen dieser Klasse vorhanden.
	 *         Sie speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see pValueMapper()
	 */
	private static PValueMapper pValueMapper = null;

	/**
	 * Struktur von
	 * 
	 * @author Thies
	 * 
	 *         Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 *         Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 *         <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected PValueMapper() {
	}

	/**
	 * Struktur von
	 * 
	 * @author Thies
	 * 
	 *         Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 *         Diese statische Methode kann aufgrufen werden durch
	 *         <code>PValueMapper.pValueMapper()</code>. Sie stellt die
	 *         Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine
	 *         einzige Instanz von <code>PValueMapper</code> existiert.
	 *         <p>
	 * 
	 *         <b>Fazit:</b> PValueMapper sollte nicht mittels <code>new</code>
	 *         instantiiert werden, sondern stets durch Aufruf dieser statischen
	 *         Methode.
	 * 
	 * @return Das <code>PValueMapper</code>-Objekt.
	 * @see pValueMapper
	 */
	public static PValueMapper pValueMapper() {
		if (pValueMapper == null) {
			pValueMapper = new PValueMapper();
		}
		return pValueMapper;
	}

	/**
	 * Diese Methode trägt eine Eigenschaftsausprägung in die Datenbank ein.
	 * 
	 * @param pv
	 *            das <code>PValue</code> Objekt, dass in die Datenbank eingetragen
	 *            werden soll.
	 * @param c
	 *            der Kontakt zu dem das <code>PValue</code> Objekt gehört.
	 * @return Das als Parameter übergebene- <code>PValue</code> Objekt.
	 */
	public PValue insertPValue(PValue pv, Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
			/**
			 * Dieser switch-case sucht den richtigen Datentyp des <code>PValue</code>
			 * Objekts und trägt den Wert in die Datenbank ein
			 */
			switch (pv.getProperty().getType()) {
			case STRING: {
				// Füllen des Statements
				stmt.executeUpdate(
						"INSERT INTO pValue (stringValue, intValue, floatValue, "
								+ "dateValue, propertyID, contactID) VALUES " + "( '" + pv.getStringValue() + "' , "
								+ " null, " + " null, " + " null, " + pv.getProperty().getId() + ", " + c.getId() + ")",
						Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmt.getGeneratedKeys();

				Statement stmt2 = con.createStatement();

				while (rs.next()) {
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));
					System.err.println("PValID " + pv.getId());
					while (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
				// Schließen des SQL-Statements
				stmt.close();
				stmt2.close();

				// Schließen der Datenbankverbindung
				con.close();

				break;
			}
			case INT: {
				stmt.executeUpdate(
						"INSERT INTO pValue (stringValue, intValue, floatValue, "
								+ "dateValue, propertyID, contactID) VALUES " + "(" + "null, " + pv.getIntValue() + ", "
								+ "null, null, " + pv.getProperty().getId() + ", " + c.getId() + ")",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.getGeneratedKeys();
				Statement stmt2 = con.createStatement();
				while (rs.next()) {
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));
					while (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
				// Schließen des SQL-Statements
				stmt.close();
				stmt2.close();

				// Schließen der Datenbankverbindung
				con.close();

				break;
			}
			case DATE: {
				//
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				String query = ("INSERT INTO pValue (stringValue, intValue, floatValue, "
						+ " dateValue, propertyID, contactID) VALUES " + "( " + "NULL, " + "NULL, " + "NULL,'"
						+ dateFormat.format(pv.getDateValue()) + "', " + pv.getProperty().getId() + " , " + c.getId()
						+ " ) ");
				stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.getGeneratedKeys();
				Statement stmt2 = con.createStatement();
				ResultSet rs2;
				while (rs.next()) {
					rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));
					while (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
				// Schließen des SQL-Statements
				stmt.close();
				stmt2.close();

				// Schließen der Datenbankverbindung
				con.close();

				break;
			}
			case FLOAT: {
				// Füllen des Statements
				stmt.executeUpdate("INSERT INTO pValue (stringValue, intValue, floatValue, "
						+ " dateValue, propertyID, contactID) VALUES " + "( " + "null, " + "null, " + pv.getFloatValue()
						+ ", " + "null" + ", " + pv.getProperty().getId() + ", " + c.getId() + ")",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.getGeneratedKeys();
				Statement stmt2 = con.createStatement();
				while (rs.next()) {
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));

					while (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
				// Schließen des SQL-Statements
				stmt.close();
				stmt2.close();

				// Schließen der Datenbankverbindung
				con.close();

				break;
			}
			}
			// Rückgabe des PValue
			return pv;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode sucht die <code>PValue</code> Objekte eines Kontaktes und gibt
	 * sie in Form einer ArrayList zurück.
	 * 
	 * @param c
	 *            der Kontakt zu welchem die <code>PValue</code> Objekte ermittelt
	 *            werden sollen.
	 * @return Die ArrayList mit <code>PValue</code> Objekten.
	 */
	public ArrayList<PValue> findPValueForContact(Contact c) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<PValue> al = new ArrayList<PValue>();

			// Füllen des Statements
			ResultSet rs = stmt.executeQuery("SELECT pValue.pValueID, " + "pValue.stringValue, " + "pValue.intValue, "
					+ "pValue.floatValue, " + "pValue.dateValue, " + "pValue.dateCreated, " + "pValue.dateUpdated, "
					+ "pValue.contactID, " + "property.propertyID, " + "property.isStandard, " + "property.name, "
					+ "property.type, " + "property.dateCreated, " + "property.dateUpdated " + "FROM pValue "
					+ "LEFT JOIN property ON pValue.propertyID = property.propertyID " + " WHERE contactID = "
					+ c.getId());
			while (rs.next()) {

				PValue pv = new PValue();
				Property p = new Property();
				// Befüllen des PValue-Objekts und Hinzufügen zur ArrayList.
				pv.setId(rs.getInt("pValueID"));
				pv.setStringValue(rs.getString("stringValue"));
				pv.setIntValue(rs.getInt("intValue"));
				pv.setFloatValue(rs.getFloat("floatValue"));
				pv.setDateCreated(rs.getTimestamp("dateCreated"));
				pv.setDateUpdated(rs.getTimestamp("dateUpdated"));
				pv.setDateValue(rs.getDate("dateValue"));
				p.setId(rs.getInt("propertyID"));
				p.setStandard(rs.getBoolean("isStandard"));
				p.setLabel(rs.getString("name"));
				p.setType(rs.getString("type"));
				System.err.println(p.getTypeInString());
				p.setDateCreated(rs.getTimestamp("dateCreated"));
				p.setDateUpdated(rs.getTimestamp("dateUpdated"));
				pv.setProperty(p);
				if (p.getType().equals(Type.STRING)) {
					pv.setPointer(2);
				} else if (p.getType().equals(Type.INT)) {
					pv.setPointer(1);
				} else if (p.getType().equals(Type.DATE)) {
					pv.setPointer(3);
				} else if (p.getType().equals(Type.FLOAT)) {
					pv.setPointer(4);
				} else {
					pv.setPointer(0);
				}
				al.add(pv);
//				System.out.println(pv.getStringValue());
//				System.out.println(pv.getPointer());
			}

			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			// Rückgabe der mit PValues gefüllten ArrayList
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode gibt ein <code>PValue</code> Objekt zurück, dass eine bestimmte
	 * ID hat.
	 * 
	 * @param id
	 *            die Id nach welcher gesucht werden soll.
	 * @return Das <code>PValue</code> Objekt mit der gesuchten id.
	 */
	public PValue findPValueById(int id) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen eines PValue-Objektes
			PValue pv = new PValue();

			Property p = new Property();

			// Füllen des Statements

			ResultSet rs = stmt.executeQuery("SELECT pValue.pValueID, " + "pValue.stringValue, " + "pValue.intValue, "
					+ "pValue.floatValue, " + "pValue.dateValue, " + "pValue.dateCreated, " + "pValue.dateUpdated, "
					+ "pValue.contactID, " + "property.propertyID, " + "property.isStandard, " + "property.name, "
					+ "property.type, " + "property.dateCreated, " + "property.dateUpdated " + "FROM pValue "
					+ "LEFT JOIN property ON pValue.propertyID = property.propertyID " + " WHERE pValueID = " + id);
			if (rs.next()) {
				// Befüllen des PValue-Objekts und Hinzufügen zur ArrayList.
				pv.setId(rs.getInt("pValueID"));
				pv.setStringValue(rs.getString("stringValue"));
				pv.setIntValue(rs.getInt("intValue"));
				pv.setFloatValue(rs.getFloat("floatValue"));
				pv.setDateCreated(rs.getTimestamp("dateCreated"));
				pv.setDateUpdated(rs.getTimestamp("dateUpdated"));
				pv.setDateValue(rs.getDate("dateValue"));
				p.setId(rs.getInt("propertyID"));
				p.setStandard(rs.getBoolean("isStandard"));
				p.setLabel(rs.getString("name"));
				p.setType(rs.getString("type"));
				p.setDateCreated(rs.getTimestamp("dateCreated"));
				p.setDateUpdated(rs.getTimestamp("dateUpdated"));
				pv.setProperty(p);
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			// Rückgabe des PValue-Objekts
			return pv;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode aktualisiert ein <code>PValue</code> Objekt in der Datenbank.
	 * 
	 * @param pv
	 *            das <code>PValue</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter übergebene- <code>PValue</code> Objekt.
	 */
	public PValue updatePValue(PValue pv) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			/**
			 * Dieser switch-case sucht den richtigen Datentyp des <code>PValue</code>
			 * Objekts und trägt den Wert in die Datenbank ein
			 */
			switch (pv.getProperty().getType()) {

			case STRING: {
				stmt.executeUpdate("UPDATE pValue SET stringValue = '" + pv.getStringValue() + "' WHERE pValueID = "
						+ pv.getId() + ";");
				break;
			}
			case INT: {
				stmt.executeUpdate(
						"UPDATE pValue SET intValue = '" + pv.getIntValue() + "' WHERE pValueID = " + pv.getId() + ";");
				break;
			}
			case DATE: {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				stmt.executeUpdate("UPDATE pValue SET dateValue = '" + dateFormat.format(pv.getDateValue())
						+ "' WHERE pValueID = " + pv.getId() + ";");
				break;
			}
			case FLOAT: {
				stmt.executeUpdate("UPDATE pValue SET floatValue = '" + pv.getFloatValue() + "' WHERE pValueID = "
						+ pv.getId() + ";");
				break;
			}
			}

			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			// Rückgabe des PValue-Objekts
			return pv;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode löscht ein <code>PValue</code> Objekt aus der Datenbank.
	 * 
	 * @param pv
	 *            das <code>PValue</code> Objekt, dass gelöscht werden soll.
	 * 
	 */
	public void deletePValue(PValue pv) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Füllen des Statements
			stmt.executeUpdate("DELETE FROM pValue WHERE pValueID = " + pv.getId());

			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>User</code>
	 * Objekten die eine Teilhaberschaft an einem bestimmten <code>PValue</code>
	 * Objekt besitzen.
	 * 
	 * @param pv
	 *            das <code>PValue</code> Objekt, dessen Teilhaber gesucht werden.
	 * @return Die <code>ArrayList</code> mit den Teilhabern.
	 */
	public ArrayList<JabicsUser> findCollaborators(PValue pv) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer ArrayList
			ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();

			// Auswählen der <code>User</code> Objekte mit einer bestimmten ID aus der
			// Teilhaberschaftstabelle.
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email" + " FROM systemUser"
					+ " LEFT JOIN pValueCollaboration ON systemUser.systemUserID = pValueCollaboration.systemUserID"
					+ " WHERE pValueCollaboration.pValueID = " + pv.getId());

			//
			while (rs.next()) {
				// Befüllen des User-Objekts und Hinzufügen zur ArrayList.
				JabicsUser u = new JabicsUser(rs.getString("email"));
				u.setId(rs.getInt("systemUserID"));
				al.add(u);
			}
			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			// Rückgabe der mit JabicsUsern befüllten ArrayList
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode trägt eine Teilhaberschaft eines <code>User</code> Objekts zu
	 * einem <code>PValue</code> Objekt in die Datenbank ein.
	 * 
	 * @param u
	 *            der User der an einer Eigenschaftsausprägung
	 *            Teilhaberschaftsrechte erlangen soll.
	 * @param pv
	 *            die Eigenschaftsausprägung an der ein User Teilhaberschaft haben
	 *            soll.
	 * @param IsOwner
	 *            ein <code>boolean</code> Wert der wiederspiegelt ob der
	 *            zuzuweisende Teilhaber auch der Owner ist.
	 * @return das übergebene <code>PValue</code> Objekt.
	 */
	public PValue insertCollaboration(JabicsUser u, PValue pv, boolean IsOwner) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			System.err.println("pvid" + pv.getId());
			System.err.println("uid" + u.getId());
			System.err.println("tbool: " + IsOwner);
			// Füllen des Statements
			stmt.executeUpdate("INSERT INTO pValueCollaboration (IsOwner, pValueID, systemUserID) VALUES " + "("
					+ IsOwner + ", " + pv.getId() + ", " + u.getId() + ")");

			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			// Rückgabe des pValue-Objekts
			return pv;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode löscht eine Teilhaberschaft zwischen einem <code>User</code>
	 * Objekt und einem <code>PValue</code> Objekt.
	 * 
	 * @param pv
	 *            das ausgewählte <code>PValue</code> Objekt.
	 * @param u
	 *            der Nutzer der die Teilhaberschaft zu dem <code>PValue</code>
	 *            Objekt verlieren soll.
	 */
	public void deleteCollaboration(PValue pv, JabicsUser u) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Füllen des Statements
			stmt.executeUpdate("DELETE FROM pValueCollaboration WHERE systemUserID = " + u.getId() + " AND pValueID = "
					+ pv.getId());

			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public ArrayList<BoStatus> findShareStatus(ArrayList<PValue> alPValue) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Deklaration und Initialisierung einer ArrayList<BoStatus>
			ArrayList<BoStatus> al = new ArrayList<BoStatus>();

			// Deklaration und Initialisierung eines StringBuffers
			StringBuffer pValueIDs = new StringBuffer();

			// pValueIDs an den StringBuffer anhängen
			for (PValue pv : alPValue) {
				pValueIDs.append(pv.getId());
				pValueIDs.append(",");
			}

			// Letztes Komma im StringBuffer löschen
			pValueIDs.deleteCharAt(pValueIDs.lastIndexOf(","));

			ResultSet rs = stmt.executeQuery("SELECT pValueID " + " FROM pValueCollaboration "
					+ " WHERE isOwner = 0 AND pValueID IN (" + pValueIDs + ")");

			// Das Resultset in ein Array aus BoStatus überführen
			ArrayList<Integer> ids = new ArrayList<Integer>();

			while (rs.next()) {
				ids.add(new Integer(rs.getInt("pValueID")));
			}

			for (PValue p : alPValue) {
				Boolean bol = false;
				for (Integer i : ids) {
					if (i.equals(p.getId())) {
						bol = true;
					}
				}
				if (bol) {
					al.add(BoStatus.IS_SHARED);
				} else {
					al.add(BoStatus.NOT_SHARED);
				}
			}
			/*
			 * for (PValue pv : alPValue) { while (rs.next()) { if (rs.getInt("pValueID") ==
			 * pv.getId()) { al.add(BoStatus.IS_SHARED); } else {
			 * al.add(BoStatus.NOT_SHARED); } }
			 * 
			 * }
			 */

			// Schließen des SQL-Statements
			stmt.close();

			// Schließen der Datenbankverbindung
			con.close();

			// Rückgabe der ArrayList<BoStatus>
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}
}