package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>PValue</code> Objekten
 * auf die relationale Datenbank. Sie stellt alle notwendigen Methoden zur
 * Verwaltung der Eigenschaftsausprägungen in der Datenbank zur Verfügung.
 *
 * @author Thies
 * @author Brase
 * @author Stahl
 */
public class PValueMapper {

	/**
	 * Die Klasse PValueMapper wird nur einmal instantiiert. Man spricht hierbei von
	 * einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 * 
	 * @see pValueMapper()
	 */
	private static PValueMapper pValueMapper = null;

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>PValueMapper.pValueMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>PValueMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> PValueMapper sollte nicht mittels <code>new</code> instantiiert
	 * werden, sondern stets durch Aufruf dieser statischen Methode.
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
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected PValueMapper() {
	}

	/**
	 * Diese Methode löscht eine Teilhaberschaft zwischen einem
	 * <code>JabicsUser</code> Objekt und einem <code>PValue</code> Objekt.
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

		} catch (SQLException e) {
			System.err.print(e);
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

		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen
	 * <code>JabicsUser</code> Objekten die eine Teilhaberschaft an einem bestimmten
	 * <code>PValue</code> Objekt besitzen.
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

			// Auswählen der <code>JabicsUser</code> Objekte mit einer bestimmten ID aus der
			// Teilhaberschaftstabelle.
			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name "
					+ " FROM systemUser"
					+ " LEFT JOIN pValueCollaboration ON systemUser.systemUserID = pValueCollaboration.systemUserID"
					+ " WHERE pValueCollaboration.pValueID = " + pv.getId());

			// Für jedes Tupel wird ein User Objekt erstellt und befüllt und an die
			// ArrayList angehängt
			while (rs.next()) {
				JabicsUser u = new JabicsUser(rs.getString("email"));
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("name"));
				al.add(u);
			}

			// Rückgabe der mit JabicsUsern befüllten ArrayList
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

			// Erzeugen eines PValue-Objekts
			PValue pv = new PValue();

			// Erzeugen eines Property-Objekts
			Property p = new Property();

			// Füllen des Statements

			ResultSet rs = stmt.executeQuery("SELECT pValue.pValueID, " + "pValue.stringValue, " + "pValue.intValue, "
					+ "pValue.floatValue, " + "pValue.dateValue, " + "pValue.dateCreated, " + "pValue.dateUpdated, "
					+ "pValue.contactID, " + "property.propertyID, " + "property.isStandard, " + "property.name, "
					+ "property.type " + "FROM pValue "
					+ "LEFT JOIN property ON pValue.propertyID = property.propertyID " + " WHERE pValueID = " + id);

			// Wenn ein Tupel in der Datenbank existiert, wird das PValue und das Property
			// Objekt befüllt
			if (rs.next()) {
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
				pv.setProperty(p);
			}

			// Rückgabe des PValue-Objekts
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

			// Füllen und ausführen des Statements
			ResultSet rs = stmt.executeQuery("SELECT pValue.pValueID, " + "pValue.stringValue, " + "pValue.intValue, "
					+ "pValue.floatValue, " + "pValue.dateValue, " + "pValue.dateCreated, " + "pValue.dateUpdated, "
					+ "pValue.contactID, " + "property.propertyID, " + "property.isStandard, " + "property.name, "
					+ "property.type " + "FROM pValue "
					+ "LEFT JOIN property ON pValue.propertyID = property.propertyID " + " WHERE contactID = "
					+ c.getId());

			// Erzeugen einer Eigenschaft und einer Ausprägung, befüllen dieser und anhängen
			// an eine ArrayList
			while (rs.next()) {
				PValue pv = new PValue();
				Property p = new Property();
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
			}

			// Rückgabe der mit PValues gefüllten ArrayList
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode ermittelt den Share-Status von in einer Liste übergebenen
	 * <code>PValue</code> Objekten
	 * 
	 * @param alPValue
	 *            ArrayList mit <code>PValue</code> Objekten
	 * @return ArrayList, welche den BoStatus der übergebenen Kontakte enthält
	 */
	public ArrayList<BoStatus> findShareStatus(ArrayList<PValue> alPValue) {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Erzeugen einer neuen ArrayList
			ArrayList<BoStatus> al = new ArrayList<BoStatus>();

			// Erzeugen eines neuen StringBuffers
			StringBuffer pValueIDs = new StringBuffer();

			// pValueIDs an den StringBuffer anhängen
			if (!alPValue.isEmpty()) {
				for (PValue pv : alPValue) {
					pValueIDs.append(pv.getId());
					pValueIDs.append(",");
				}
				// Letztes Komma im StringBuffer löschen
				pValueIDs.deleteCharAt(pValueIDs.lastIndexOf(","));
			} else {
				return null;
			}

			// Befüllen und ausführen des SQL-Statements
			ResultSet rs = stmt.executeQuery("SELECT pValueID " + " FROM pValueCollaboration "
					+ " WHERE isOwner = 0 AND pValueID IN (" + pValueIDs + ")");

			// Erzeugen einer neuen ArrayList
			ArrayList<Integer> ids = new ArrayList<Integer>();

			// Für jedes Tupel wird die pValueID an die ArrayList angehängt
			while (rs.next()) {
				ids.add(new Integer(rs.getInt("pValueID")));
			}

			// Setzen des Shared Status für jedes PValue in ArrayList<PValue>.
			// Wenn die übergebene pValueID in der Datenbank steht, ist der Kontakt geteilt.
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
			
			// Rückgabe der ArrayList<BoStatus>
			return al;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}

	/**
	 * Diese Methode trägt eine Teilhaberschaft eines <code>JabicsUser</code>
	 * Objekts zu einem <code>PValue</code> Objekt in die Datenbank ein.
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

			// Füllen und ausführen des Statements
			stmt.executeUpdate("INSERT INTO pValueCollaboration (IsOwner, pValueID, systemUserID) VALUES " + "("
					+ IsOwner + ", " + pv.getId() + ", " + u.getId() + ")");
			
			// Rückgabe des pValue-Objekts
			return pv;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
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

			// Dieser switch-case sucht den richtigen Datentyp des <code>PValue</code>
			// Objekts und trägt den Wert in die Datenbank ein
			switch (pv.getProperty().getType()) {
			case STRING: {
				// Füllen und ausführen des SQL-Statements
				stmt.executeUpdate(
						"INSERT INTO pValue (stringValue, intValue, floatValue, "
								+ "dateValue, propertyID, contactID) VALUES " + "( '" + pv.getStringValue() + "' , "
								+ " null, " + " null, " + " null, " + pv.getProperty().getId() + ", " + c.getId() + ")",
						Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmt.getGeneratedKeys();

				Statement stmt2 = con.createStatement();

				// Wenn ein Tupel existiert, befüllen des PValue mit ID, Erstellungsdatum und
				// letztem Update
				if (rs.next()) {
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));
					if (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
				break;
			}
			case INT: {
				// Befüllen und ausführen des SQL-Statements
				stmt.executeUpdate(
						"INSERT INTO pValue (stringValue, intValue, floatValue, "
								+ "dateValue, propertyID, contactID) VALUES " + "(" + "null, " + pv.getIntValue() + ", "
								+ "null, null, " + pv.getProperty().getId() + ", " + c.getId() + ")",
						Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmt.getGeneratedKeys();
				Statement stmt2 = con.createStatement();

				// Wenn ein Tupel existiert, befüllen des PValue mit ID, Erstellungsdatum und
				// letztem Update
				if (rs.next()) {
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));
					if (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
				break;
			}
			case DATE: {
				//
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				// String mit SQL-Statement befüllen
				String query = ("INSERT INTO pValue (stringValue, intValue, floatValue, "
						+ " dateValue, propertyID, contactID) VALUES " + "( " + "NULL, " + "NULL, " + "NULL,'"
						+ dateFormat.format(pv.getDateValue()) + "', " + pv.getProperty().getId() + " , " + c.getId()
						+ " ) ");

				// SQL-Statement ausführen
				stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmt.getGeneratedKeys();
				Statement stmt2 = con.createStatement();
				ResultSet rs2;

				// Wenn ein Tupel existiert, befüllen des PValue mit ID, Erstellungsdatum und
				// letztem Update
				if (rs.next()) {
					rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));
					if (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
				break;
			}
			case FLOAT: {
				// Füllen und ausführen des SQL-Statements
				stmt.executeUpdate("INSERT INTO pValue (stringValue, intValue, floatValue, "
						+ " dateValue, propertyID, contactID) VALUES " + "( " + "null, " + "null, " + pv.getFloatValue()
						+ ", " + "null" + ", " + pv.getProperty().getId() + ", " + c.getId() + ")",
						Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmt.getGeneratedKeys();
				Statement stmt2 = con.createStatement();

				// Wenn ein Tupel existiert, befüllen des PValue mit ID, Erstellungsdatum und
				// letztem Update
				if (rs.next()) {
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM pValue WHERE pValueID = " + rs.getInt(1));
					pv.setId(rs.getInt(1));
					if (rs2.next()) {
						pv.setDateCreated(rs2.getTimestamp("dateCreated"));
						pv.setDateUpdated(rs2.getTimestamp("dateUpdated"));
					}
				}
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

			// Rückgabe des PValue-Objekts
			return pv;
		} catch (SQLException e) {
			System.err.print(e);
			return null;
		}
	}
}