/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>Kontakt</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Kontakte in der Datenbank zur Verfügung. 
 * 
 * @author Brase
 * @author Stahl
 *
 */
public class ContactMapper {

	
	public ArrayList<Contact> findContactByLastName(String ln){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();

	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT Lastname FROM contacts " + "WHERE Lastname=" + ln + " ORDER BY -");

	  if (rs.next()) {
	       
	        Contact c = new Contact();
	        c.setLastName(rs.getString("LastName"));
	      //  c.setOwnerID(rs.getInt("owner"));
	        return c;
	      }
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
		
	}
	
	
	public Contact findByKey(int id) {
	    // Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();

	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT id FROM contacts " + "WHERE id=" + id + " ORDER BY -");

	  if (rs.next()) {
	       
	        Contact c = new Contact();
	        c.setId(rs.getInt("id"));
	      //  c.setOwnerID(rs.getInt("owner"));
	        return c;
	      }
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
}