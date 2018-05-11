/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.User;

/**
 * @author Brase
 * @author Stahl
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>User</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der User in der Datenbank zur Verfügung. 
 * 
 *
 */
public class UserMapper {
	

	 /** 
	 * Diese Methode trägt ein <code>User</code> Objekt in die Datenbank ein.

	 * @param u das <code>User</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter übergebene- <code>User</code> Objekt.
	 */


		public User insertUser(User u){
			// Erzeugen der Datenbankverbindung
		    Connection con = DBConnection.connection();
		    
		  try {
		   // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		    
		   // Füllen des Statements
		   stmt.executeUpdate("INSERT INTO User (id) VALUES " 
		   
				   + "(" + u.getId() +  ")"  );

		   return u;
		    }
		    catch (SQLException e) {
		    	System.err.print(e);
		      
		    }
		   return null;
		  }
		
		/**
		 * Diese Methode löscht ein <code>User</code> Objekt aus der Datenbank.
		 * 
		 * @param u das <code>User</code> Objekt, dass gelöscht werden soll.
		 */
		
		public void deleteUser(User u){
			// Erzeugen der Datenbankverbindung
		    Connection con = DBConnection.connection();
		    
		  try {
		   
			  // Erzeugen eines ungefüllten SQL-Statements
			   Statement stmt = con.createStatement();
			   
			   // Löschen des Users
			   stmt.executeUpdate("DELETE FROM users WHERE id=" + u.getId()); 
			   
			// Erzeugen eines zweiten ungefüllten SQL-Statements
			   Statement stmt2 = con.createStatement();
			   
			   // Löschen aller Teilhaberschaften an Kontakten.
			   stmt2.executeUpdate("DELETE FROM C-Teilhaberschaft WHERE u-id=" + u.getId()); 
			   
			// Löschen aller Teilhaberschaften an KontaktListen.
			   stmt2.executeUpdate("DELETE FROM CL-Teilhaberschaft WHERE u-id=" + u.getId()); 
			   
			// Löschen aller Teilhaberschaften an <code>PValue</code> Objekten.
			   stmt2.executeUpdate("DELETE FROM PValue-Teilhaberschaft WHERE u-id=" + u.getId()); 

		    }
		    catch (SQLException e) {
		    	System.err.print(e);
		    }
		  
		  }
		
		/**
		 * Diese Methode erlaubt die Suche eines  <code>User</code> Objekts in der Datenbank.
		 * 
		 * @param id  die id nach der gesucht werden soll.
		 * @return das gesuchte  <code>User</code> Objekt.
		 */
		
		public User findUserById(int id)  {
		    // Erzeugen der Datenbankverbindung
		    Connection con = DBConnection.connection();

		  try {
		   // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		 //Erzeugen eines User-Objektes
		   User u = new User();

		   // Füllen des Statements
		   ResultSet rs = stmt.executeQuery("SELECT id FROM users " + "WHERE id=" + id + " ORDER BY -");
		   
		  if (rs.next()) {
		       
			//Befüllen des Kontakt-Objekts
		        u.setId(rs.getInt("id"));
		      //  c.setOwnerID(rs.getInt("owner"));
		        
		      }
		  return u;
		    }
		    catch (SQLException e) {
		    	System.err.print(e);
		      return null;
		    }
		 
		  }
		
		
}