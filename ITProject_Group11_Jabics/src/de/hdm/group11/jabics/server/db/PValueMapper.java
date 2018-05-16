/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Eigenschaftsausprägungen in der Datenbank zur Verfügung. 
 *
 */
public class PValueMapper {

	
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
		  
		  Statement stmt0 = con.createStatement();
			
			 // Herausfinden der bisher höchsten PValue-ID.
			
		ResultSet rs = stmt0.executeQuery("SELECT MAX(pv-id) AS maxid " + "FROM PValues ");

			if (rs.next()) {
				
				// Setzen der <code>PValue</code>-ID
				 
				pv.setId(rs.getInt("maxid") + 1); 	  
		  
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	
	   //Integer- Werte können auf !null überprüft werden. Dies wird gleich benötigt.
	   Integer itg = pv.getIntValue();
	   
	 /**
	  * Diese If-Kaskade sucht den richtigen Datentyp des <code>PValue</code> Objekts
	  * und trägt den Wert in die Datenbank ein
	  */
	   
	    if(pv.getDateValue()!=null) {
	    	
	     Date value = (Date) pv.getDateValue();
	     
	     /**
	      *  Befüllenüllen des Statements.
	      * (Die Tabelle hat folgende Spalten:
	      *    c-id | pv-id | string | date | int | float)
	      */
		 stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id,string, date, int, float) VALUES " 
		   
				   + "(" + c.getId() + "," + pv.getId() + "," + "null," + value + "," + "null," +  "null" +  ")"  ); 
	     
		//Hier wird auf den Integer-Wert zurückgegriffen.	
	    	}if(itg !=null) {
	    	
	    	Integer value = itg;
	    	
	    	// Füllen des Statements
	 	   stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id, string, date, int, float) VALUES " 
		   
				   + "(" + c.getId() + "," + pv.getId() +"," + "null," + "null," +  value +  "null" +  ")"  ); 
	    	
	    		}if(pv.getStringValue()!=null) {
	    	
	    		String value = pv.getStringValue();
	    		
	    		// Füllen des Statements
	    		   stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id, string, date, int, float) VALUES " 
		   
				   + "(" + c.getId() + "," + pv.getId() +"," +  value + "null," +  "null" + "null" +  ")"  );   
	    		
	    			}if(pv.getProperty()!=null) {
 
	    	
	    				}else {
	    	
	    				Float value = pv.getFloatValue();
	    				
	    				// Füllen des Statements
	    				   stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id, string, date, int, float) VALUES " 
	    				   
	    						   + "(" + c.getId() + "," + pv.getId() + "null," +  "null" + "null" + "," + value +  ")"  ); 		
	    			}
			}
	   
	   /**
	    * Mit der @insertCollaboration Methode (dieser Klasse) wird der <code>Owner</code> des <code>PValue</code> festgelegt.
	    * 
	    */
			insertCollaboration(pv.getOwner(), pv, true);
			
	   //Rückgabe des PValue
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
	 * @param c der Kontakt zu dem das <code>PValue</code> Objekt gehört.
	 * @param u der User, welcher die Eigenschaftsausprägung aktualisiert.
	 * @return Das als Parameter übergebene- <code>PValue</code> Objekt.
	 */
	
	public PValue updatePValue(PValue pv, Contact c, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();

		   //Integer- Werte können auf !null überprüft werden. Dies wird gleich benötigt.
		   Integer itg = pv.getIntValue();
		   
		 /**
		  * Diese If-Kaskade sucht den richtigen Datentyp des <code>PValue</code> Objekts
		  * und trägt den Wert in die Datenbank ein
		  */
		   
		    if(pv.getDateValue()!=null) {
		    	
		     Date value = (Date) pv.getDateValue();
		     
		     /**
		      *  Befüllenüllen des Statements.
		      * (Die Tabelle hat folgende Spalten:
		      *    c-id | pv-id | string | date | int | float)
		      */
			 stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id, string, date, int, float) VALUES " 
			   
					   + "(" + c.getId() + "," + pv.getId() + "," + "null," + value + "," + "null," +  "null" +  ")"  ); 
		     
			//Hier wird auf den Integer-Wert zurückgegriffen.	
		    	}if(itg !=null) {
		    	
		    	Integer value = itg;
		    	
		    	// Füllen des Statements
		 	   stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id, string, date, int, float) VALUES " 
			   
					   + "(" + c.getId() + "," + pv.getId() + "," + "null," + "null," +  value +  "null" +  ")"  ); 
		    	
		    		}if(pv.getStringValue()!=null) {
		    	
		    		String value = pv.getStringValue();
		    		
		    		// Füllen des Statements
		    		   stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id, string, date, int, float) VALUES " 
			   
					   + "(" + c.getId() + "," + pv.getId() +"," +  value + "null," +  "null" + "null" +  ")"  );   
		    		
		    			}if(pv.getProperty()!=null) {
	 
		    	
		    				}else {
		    	
		    				Float value = pv.getFloatValue();
		    				
		    				// Füllen des Statements
		    				   stmt.executeUpdate("INSERT INTO PValues (c-id, pv-id, string, date, int, float) VALUES " 
		    				   
		    						   + "(" + c.getId() + "," + pv.getId()  + "null," +  "null" + "null" + "," + value +  ")"  ); 		
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
		   stmt.executeUpdate("DELETE FROM PValues WHERE PV-id=" + pv.getId()); 
		   
		   /** 
		    * <code>Collaborations</code> werden mit der @deleteCollaboration Methode gelöst.
		    */
		   deleteCollaboration(pv, pv.getOwner());
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
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<User> al = new ArrayList();
	    
	    
	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT U-ID,Username FROM PV-Teilhaberschaft " + "WHERE PV-Id=" 
	   
			   	 + pv.getId() + " ORDER BY -");

	  while (rs.next()) {
	      
		//Befüllen des User-Objekts
	        User u = new User();
	        u.setId(rs.getInt("U-ID"));
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
	 * Diese Methode trägt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>PValue</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einer Eigenschaftsausprägung Teilhaberschaftsrechte erlangen soll.
	 * @param pv die Eigenschaftsausprägung an der ein User Teilhaberschaft haben soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das Übergebene <code>PValue</code> Objekt.
	 */
	
	public PValue insertCollaboration(User u, PValue pv, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	    
	   // Füllen des Statements
	   stmt.executeUpdate("INSERT INTO PV-Teilhaberschaft (PV-id, U-id, IsOwner) VALUES " 
	   
			   + "(" + pv.getId() + "," + u.getId() + "," + IsOwner + ")"  );

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
	
	public void deleteCollaboration(PValue pv, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Füllen des Statements
		   stmt.executeUpdate("DELETE FROM PV-Teilhaberschaft WHERE U-id=" + u.getId() + "AND PV-Id=" + pv.getId() ); 

	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
}
	
