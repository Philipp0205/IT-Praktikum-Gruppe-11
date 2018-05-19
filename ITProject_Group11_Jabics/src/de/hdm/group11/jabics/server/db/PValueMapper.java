/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gwt.thirdparty.javascript.jscomp.regex.CaseCanonicalize;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.User;

/**
 * @author Brase
 * @author Stahl
 * 
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>PValue</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Eigenschaftsauspr�gungen in der Datenbank zur Verf�gung. 
 *
 */
public class PValueMapper {
	/**
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
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	protected PValueMapper() {
		
	}
	/**
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
	 * Diese Methode tr�gt eine Eigenschaftsauspr�gung in die Datenbank ein.
	 * 
	 * @param pv das <code>PValue</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @param c der Kontakt zu dem das <code>PValue</code> Objekt geh�rt.
	 * @return Das als Parameter �bergebene- <code>PValue</code> Objekt.
	 */
	public PValue insertPValue(PValue pv, Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
		  Statement stmt0 = con.createStatement();
			
			// Herausfinden der bisher h�chsten PValue-ID.
		ResultSet rs = stmt0.executeQuery("SELECT MAX(pValueID) AS maxid " + "FROM pValue ");

			if (rs.next()) {
				
				// Setzen der <code>PValue</code>-ID
				 pv.setId(rs.getInt("maxid") + 1); 	  
		  
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	 /**
	  * Dieser switch-case sucht den richtigen Datentyp des <code>PValue</code> Objekts
	  * und tr�gt den Wert in die Datenbank ein
	  */
	   switch (pv.getProperty().getType()) {
	   case STRING: {
		   String value = pv.getStringValue();
   		
   		// F�llen des Statements
   		   stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated, stringValue,"
			   		
					   + "pValueID,propertyID, contactID) VALUES " 
			   
					   + "(" + c.getDateCreated() + "," + c.getDateUpdated() + ","  + value + ","
					   
					   + pv.getId() + "," +  pv.getProperty().getId() + "," + c.getId() + ")"  ); 
	   }
	   case INT: {
		   int value = pv.getIntValue();
	    	
	 	   stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated,intValue,"
			   		
					   + "pValueID, propertyID, contactID) VALUES " 
			   
					   + "(" + c.getDateCreated() + "," + c.getDateUpdated() + "," + value  
					   
					   + "," + pv.getId() + "," + pv.getProperty().getId() + "," + c.getId() + ")"  );  
	   }
	   case DATE: {
		   LocalDate value = pv.getDateValue();
	   /**
	      *  Bef�llen�llen des Statements.
	      * (Die Tabelle hat folgende Spalten:
	      * 
	      *     dateCreated|dateUpdated|stringValue|intValue|floatValue|pValueID|dateValue|propertyID|contactID)
	      */
		 stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated, "
			   		
					   + "pValueID, dateValue, propertyID, contactID) VALUES " 
			   
					   + "(" + c.getDateCreated() + "," + c.getDateUpdated() 
					   
					   + "," + pv.getId() + "," + value + "," + pv.getProperty().getId() + "," + c.getId() + ")"  );
	   }
	   case FLOAT: {
		   Float value = pv.getFloatValue();
			
			// F�llen des Statements
			   stmt.executeUpdate("INSERT INTO pValue (dateCreated, dateUpdated, floatValue, "
			   		
					   + "pValueID, propertyID, contactID) VALUES " 
			   
					   + "(" + c.getDateCreated() + "," + c.getDateUpdated() + ","  + value +
					   
					   "," + pv.getId() + "," + pv.getProperty().getId() + ","  + c.getId() + ")"  ); 
	   				}
	   									}
	   }
	   /**
	    * Mit der @insertCollaboration Methode (dieser Klasse) wird der <code>Owner</code> des <code>PValue</code> festgelegt.
	    * 
	    */
	   insertCollaboration(pv.getOwner(), pv, true);
	 //R�ckgabe des PValue
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
	 * @param c der Kontakt zu dem das <code>PValue</code> Objekt geh�rt.
	 * @param u der User, welcher die Eigenschaftsauspr�gung aktualisiert.
	 * @return Das als Parameter �bergebene- <code>PValue</code> Objekt.
	 */
	public PValue updatePValue(PValue pv, Contact c, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
		  // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();

		 /**
		  * Dieser switch-case sucht den richtigen Datentyp des <code>PValue</code> Objekts
		  * und tr�gt den Wert in die Datenbank ein
		  */
		   String columnname;
		   switch (pv.getProperty().getType()) {
		   case STRING: {
			   
			   columnname = "stringValue";
	   		   stmt.executeUpdate("UPDATE contact SET dateUpdated =" + c.getDateUpdated() + "," + columnname + "=" +
						   pv.getStringValue() + " WHERE pValueID = " + pv.getId() +")");
		   }
		   case INT: {
			   
			   columnname = "intValue";
	   		   stmt.executeUpdate("UPDATE contact SET dateUpdated =" + c.getDateUpdated() + "," + columnname + "=" +
						   pv.getIntValue() + " WHERE pValueID = " + pv.getId() +")");  
		   }
		   case DATE: {
			   		   
			   LocalDate locald = pv.getDateValue();
			   Date date = Date.valueOf(locald); 
			   columnname = "dateValue";
	   		   stmt.executeUpdate("UPDATE contact SET dateUpdated =" + c.getDateUpdated() + "," + columnname + "=" +
				date + " WHERE pValueID = " + pv.getId() +")"); 
		   }
		   case FLOAT: {
			   columnname = "floatValue";
	   		   stmt.executeUpdate("UPDATE contact SET dateUpdated =" + c.getDateUpdated() + "," + columnname + "=" +
				pv.getFloatValue() + " WHERE pValueID = " + pv.getId() +")"); 
		   				}
		   					}
		   /**
		    * Mit der @insertCollaboration Methode (dieser Klasse) wird der <code>Owner</code> des <code>PValue</code> festgelegt.
		    * 
		    */
				insertCollaboration(u, pv, true);
	  	  return pv;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	  }	
	/**
	 * Diese Methode l�scht ein <code>PValue</code> Objekt aus der Datenbank.
	 * 
	 * @param pv das <code>PValue</code> Objekt, dass gel�scht werden soll.
	 * 
	 */
	public void deletePValue(PValue pv){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	  try {
		  /** 
		    * Collaborations werden mit folgendem Statement gelöscht:
		    */
		// Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   // F�llen des Statements
		   stmt2.executeUpdate("DELETE FROM pvCollaboration WHERE pValueID=" + pv.getId()); 
		  
		// Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // F�llen des Statements
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
	
	public ArrayList<User> findCollaborators(PValue pv){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<User> al = new ArrayList<User>();
	    
	   // F�llen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT systemUserID, Username FROM pValueCollaboration " + "WHERE pValueID" 
	   
			   	 + pv.getId() );

	  while (rs.next()) {
	      
		//Bef�llen des User-Objekts
	        User u = new User();
	        u.setId(rs.getInt("systemUserID"));
	        u.setUsername(rs.getString("Username"));
	      //  c.setOwnerID(rs.getInt("owner"));
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
	 * Diese Methode tr�gt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>PValue</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einer Eigenschaftsauspr�gung Teilhaberschaftsrechte erlangen soll.
	 * @param pv die Eigenschaftsauspr�gung an der ein User Teilhaberschaft haben soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das �bergebene <code>PValue</code> Objekt.
	 */
	public PValue insertCollaboration(User u, PValue pv, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	   // F�llen des Statements
	   stmt.executeUpdate("INSERT INTO pValueCollaboration (pvCollaborationID, IsOwner, pValueID, systemUserID) VALUES " 
	   
			   + "(" + pv.getId() + "," + IsOwner + "," + pv.getProperty().getId() + "," + u.getId() +   ")"  );

	  	  return pv;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	  }
	/**
	 * Diese Methode l�scht eine Teilhaberschaft zwischen einem <code>User</code> Objekt und einem <code>PValue</code> Objekt.
	 * 
	 * @param pv das ausgew�hlte <code>PValue</code> Objekt.
	 * @param u der Nutzer der die Teilhaberschaft zu dem <code>PValue</code> Objekt verlieren soll.
	 */
	
	public void deleteCollaboration(PValue pv, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	  try {
		  // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // F�llen des Statements
		   stmt.executeUpdate("DELETE FROM pvCollaborationID WHERE systemUserID=" + u.getId() + 
				   "AND pvCollaborationID=" + pv.getId() ); 
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    }
	  }
}