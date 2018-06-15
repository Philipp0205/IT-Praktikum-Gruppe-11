package de.hdm.group11.jabics.server.db;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * @author Brase
 * @author Stahl
 * 
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>PValue</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Eigenschaftsausprägungen in der Datenbank zur Verfügung. 
 *
 */
public class PValueMapper {

	/**
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 * Die Klasse PValueMapper wird nur einmal instantiiert. Man spricht
     * hierbei von einem sogenannten <b>Singleton</b>.
     * <p>
     * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
     * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
     * einzige Instanz dieser Klasse.
     * 
     * @see pValueMapper()
	 */  	
	private static PValueMapper pValueMapper = null;
	
	/**
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	protected PValueMapper() {
	}
	
	/**
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>PValueMapper.pValueMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>PValueMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> PValueMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
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
	 * @param pv das <code>PValue</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @param c der Kontakt zu dem das <code>PValue</code> Objekt gehört.
	 * @return Das als Parameter übergebene- <code>PValue</code> Objekt.
	 */
	public PValue insertPValue(PValue pv, Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
			
			/**
			 * Dieser switch-case sucht den richtigen Datentyp des <code>PValue</code> Objekts
			 * und trägt den Wert in die Datenbank ein
			 */
			switch (pv.getProperty().getType()) {
				case STRING: {
					String value = pv.getStringValue();
		
					// Füllen des Statements
					stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated, stringValue, intValue, floatValue, "
					+ "dateValue, propertyID, contactID) VALUES " 
					+ "('" + ServiceClass.convertdate(c.getDateCreated()) + "' , '" + ServiceClass.convertdate(c.getDateUpdated()) + "' , '"  + value + "' , "  + " null, "  
					+ " null, " + " null, " + pv.getProperty().getId() + ", " + c.getId() + ")"  ); 
					break;
				}
				case INT: {
					int value = pv.getIntValue();
					System.out.println(pv.getIntValue());
    	
					stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated, stringValue, intValue, floatValue, "
					+ "pValueID, dateValue, propertyID, contactID) VALUES " 
					+ "('" + ServiceClass.convertdate(c.getDateCreated()) + "' , '" + ServiceClass.convertdate(c.getDateUpdated()) + "', "  + "null, " + value  
					+ ", " + "null, " + pv.getId() + "," + "null, " + pv.getProperty().getId() + ", " + c.getId() + ")"  );  
					break;
				}
				case DATE: {
					
					/**
					 *  Befüllen des Statements.
					 * (Die Tabelle hat folgende Spalten:
					 * 
					 *     dateCreated|dateUpdated|stringValue|intValue|floatValue|pValueID|dateValue|propertyID|contactID)
					 */
					stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated, stringValue, intValue, floatValue, "
					+ " dateValue, propertyID, contactID) VALUES " 
					+ "('" + ServiceClass.convertdate(c.getDateCreated()) + "' ,'" + ServiceClass.convertdate(c.getDateUpdated()) + "', "  + "null, " + "null, " 
					+ "null, " + "'" + ServiceClass.convertdatevalue(pv.getDateValue()) + "', " + pv.getProperty().getId() + " , " + c.getId() + " )"  );
					break;
				}
				case FLOAT: {
					Float value = pv.getFloatValue();
		
					// Füllen des Statements
					stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated, stringValue, intValue, floatValue, "
					+ " dateValue, propertyID, contactID) VALUES " 
					+ "('" + ServiceClass.convertdate(c.getDateCreated()) + "' , '" + ServiceClass.convertdate(c.getDateUpdated()) + "', " +  "null, " +  "null, " + value 
					+ ", " + "null" + ", " + pv.getProperty().getId() + ", "  + c.getId() + ")"  ); 
					break;
				}
   			}

			/**
			 * Mit der @insertCollaboration Methode (dieser Klasse) wird der <code>Owner</code> des <code>PValue</code> festgelegt.
			 * 
			 */
			//insertCollaboration(pv.getOwner(), pv, true);
		
		//Rückgabe des PValue
		return pv;
		}
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	  }
	}

	/** 
	 * Diese Methode sucht die <code>PValue</code> Objekte eines Kontaktes und gibt sie in Form einer ArrayList zurück.
	 * 
	 * @param c der Kontakt zu welchem die <code>PValue</code> Objekte ermittelt werden sollen.
	 * @return Die ArrayList mit <code>PValue</code> Objekten.
	 */
	
	public ArrayList<PValue> findPValueForContact(Contact c) {
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();

	    	//Erzeugen einer ArrayList
		    ArrayList<PValue> al = new ArrayList<PValue>();

	 	   	// Füllen des Statements
	 	   	ResultSet rs = stmt.executeQuery("SELECT * FROM pValue WHERE contactID = " + c.getId());
	 	   	System.out.println(c.getId());

	 	   	while (rs.next()) {
	 	   	//Befüllen des PValue-Objekts und Hinzufügen zur ArrayList.
	 	   		PValue pv = new PValue();
	 	   		
	    		pv.setId(rs.getInt("pValueID"));
	    		pv.setStringValue(rs.getString("stringValue"));
	    		pv.setIntValue(rs.getInt("intValue"));
	    		pv.setFloatValue(rs.getFloat("floatValue"));
	    		pv.setPropertyId(rs.getInt("propertyID"));
	    		pv.setDateCreated(rs.getTimestamp("dateCreated").toLocalDateTime());
	    		pv.setDateUpdated(rs.getTimestamp("dateUpdated").toLocalDateTime());
	    		Date date =rs.getDate("dateValue");
	    		//Muss noch in der Apl realisiert werden
	    		pv.setDateValue(rs.getDate("dateValue").toLocalDate());
	    		
	    		al.add(pv);
	 	    }
	 	   	return al;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
	
	/**
	 * Diese Methode gibt ein <code>PValue</code> Objekt zurück, dass eine bestimmte ID hat.
	 * @param id die Id nach welcher gesucht werden soll.
	 * @return Das <code>PValue</code> Objekt mit der gesuchten id.
	 */
	public PValue findPValueById(int id)  {
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	    	
	    	//Erzeugen eines PValue-Objektes
	    	PValue pv = new PValue();

	    	// Füllen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM pValue " + "WHERE pValueID = " + id );
	   
	    	if (rs.next()) {
	    		//Befüllen des PValue-Objekts und Hinzufügen zur ArrayList.
	    		pv.setId(rs.getInt("pValueID"));
	    		pv.setStringValue(rs.getString("stringValue"));
	    		pv.setIntValue(rs.getInt("intValue"));
	    		pv.setFloatValue(rs.getFloat("floatValue"));
	    		pv.setPropertyId(rs.getInt("propertyID"));
	    		pv.setDateCreated(rs.getTimestamp("dateCreated").toLocalDateTime());
	    		pv.setDateUpdated(rs.getTimestamp("dateUpdated").toLocalDateTime());
	    		//Muss noch implementiert werden.
	    		pv.setDateValue(rs.getDate("dateValue").toLocalDate()); 
	    	}
	    	return pv;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
	
	/**
	 * Diese Methode aktualisiert ein <code>PValue</code> Objekt in der Datenbank.
	 * 
	 * @param pv das <code>PValue</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter übergebene- <code>PValue</code> Objekt.
	 */
	public PValue updatePValue(PValue pv){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	    	
	    	/**
	    	 * Dieser switch-case sucht den richtigen Datentyp des <code>PValue</code> Objekts
	    	 * und trägt den Wert in die Datenbank ein
	    	 */
	    	
	    	switch (pv.getProperty().getType()) {
	    		case STRING: {
	    			
	    			stmt.executeUpdate("UPDATE pValue SET stringValue = '" + pv.getStringValue() + " ', dateUpdated = '" 
	    			+ ServiceClass.convertdate(pv.getDateUpdated()) + "'"
	    			+  " WHERE pValueID = '" + pv.getId() + "';");
	    			break;
	    		}
	    		case INT: {
	    			String columnname = "intValue";
	    			stmt.executeUpdate("UPDATE pValue SET dateUpdated ='" + ServiceClass.convertdate(pv.getDateUpdated())
	    			+ "'," + columnname + "= '"
	    			+ pv.getIntValue() + "' WHERE pValueID = '" + pv.getId() + "';");  
	    			break;
	    		}
	    		case DATE: {
	    			String columnname = "dateValue";
	    			stmt.executeUpdate("UPDATE pValue SET pValue.dateUpdated ='" + ServiceClass.convertdate(pv.getDateUpdated())
	    			+ "'," + columnname + "= '"
	    			+ pv.getDateValue() + "' WHERE pValueID = '" + pv.getId() + "';"); 
	    			break;
	    		}
	    		case FLOAT: {

	    			String columnname = "floatValue";
	    			stmt.executeUpdate("UPDATE pValue SET dateUpdated ='" + ServiceClass.convertdate(pv.getDateUpdated())
	    			+ "'," + columnname + "= '"
	    			+ pv.getFloatValue() + "' WHERE pValueID = '" + pv.getId() + "';"); 
	    			break;
	    		}
	    	}	
	    	
		    return pv;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}	
	
	/**
	 * Diese Methode löscht ein <code>PValue</code> Objekt aus der Datenbank.
	 * 
	 * @param pv das <code>PValue</code> Objekt, dass gelöscht werden soll.
	 * 
	 */
	public void deletePValue(PValue pv){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	// Füllen des Statements
	    	stmt.executeUpdate("DELETE FROM pValue WHERE pValueID=" + pv.getId()); 
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    }
	}


	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>User</code> Objekten die eine Teilhaberschaft 
	 * an einem bestimmten <code>PValue</code> Objekt besitzen.
	 * @param pv das <code>PValue</code> Objekt, dessen Teilhaber gesucht werden.
	 * @return Die <code>ArrayList</code> mit den Teilhabern.
	 */
	public ArrayList<JabicsUser> findCollaborators(PValue pv){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();
	    
	    	// Auswählen der <code>User</code> Objekte mit einer bestimmten ID aus der Teilhaberschaftstabelle.
	    	ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email"
					+ " FROM systemUser"
					+ " LEFT JOIN pValueCollaboration ON systemUser.systemUserID = pValueCollaboration.systemUserID"
					+ " WHERE pValueCollaboration.pValueID = " + pv.getId()  );
	    	while (rs.next()) {
	    		//Befüllen des User-Objekts und Hinzufügen zur ArrayList.
	    		JabicsUser u = new JabicsUser(rs.getString("email"));
	    		u.setId(rs.getInt("systemUserID"));
	    		al.add(u);
	    	}
	    	return al;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}

	/**
	 * Diese Methode trägt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>PValue</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einer Eigenschaftsausprägung Teilhaberschaftsrechte erlangen soll.
	 * @param pv die Eigenschaftsausprägung an der ein User Teilhaberschaft haben soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das übergebene <code>PValue</code> Objekt.
	 */
	public PValue insertCollaboration(JabicsUser u, PValue pv, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	// Füllen des Statements
	    	stmt.executeUpdate("INSERT INTO pValueCollaboration (IsOwner, pValueID, systemUserID) VALUES " 
	    	+ "(" + IsOwner + ", " + pv.getId() + ", " + u.getId() +   ")"  );
	    	return pv;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
	
	/**
	 * Diese Methode löscht eine Teilhaberschaft zwischen einem <code>User</code> Objekt und einem <code>PValue</code> Objekt.
	 * 
	 * @param pv das ausgewählte <code>PValue</code> Objekt.
	 * @param u der Nutzer der die Teilhaberschaft zu dem <code>PValue</code> Objekt verlieren soll.
	 */
	public void deleteCollaboration(PValue pv, JabicsUser u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	    	
	    	// Füllen des Statements
	    	stmt.executeUpdate("DELETE FROM pValueCollaboration WHERE systemUserID= " + u.getId() + " AND pValueID= " + pv.getId()); 
	    }
	    catch (SQLException e) {
	    	System.err.print(e); 
	    }
	}
}