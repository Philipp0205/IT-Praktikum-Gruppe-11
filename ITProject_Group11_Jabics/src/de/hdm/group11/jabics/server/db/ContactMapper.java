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
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.User;

/**
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>Contact</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Kontakte in der Datenbank zur Verfügung. 
 * 
 * @author Brase
 * @author Stahl
 *
 */
public class ContactMapper extends PValueMapper{

	
	/** 
	 * Diese Methode trägt einen Kontakt in die Datenbank ein.
	 * 
	 * @param c das <code>Contact</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter übergebene- <code>Contact</code> Objekt.
	 */
	public Contact insertContact(Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  Statement stmt = con.createStatement();
			
			 // Herausfinden der bisher höchsten Kontakt-ID.
			
		ResultSet rs = stmt.executeQuery("SELECT MAX(c-id) AS maxid " + "FROM contacts ");

			if (rs.next()) {
				
				// Setzen der Kontakt-ID
				 
				c.setId(rs.getInt("maxid") + 1); 
		  
		  // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt2 = con.createStatement();
	   
	    
	   // Füllen des Statements
	   stmt2.executeUpdate("INSERT INTO contacts (c-id) VALUES " 
	   
			   + "(" + c.getId() + ")"  );
	   
	   /**
	    * Die <code>Values</code> des <code>Contact</code> Objektes werden in eine Arraylist extrahiert und über die 
	    * @insertPValue Methode in der Datenbank gespeichert.
	    */
	   
	   ArrayList<PValue> cv = c.getValues();
	   
	   for (int i =0; i<cv.size(); i++) {
	  
		   /**
		    * Einfügen der Eigenschaftsausprägungen in die Datenbank über die @insertPValue - Methode
		    */
		   insertPValue(cv.get(i), c);
	   		}
			}
	  }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	  return c;
	  }
	
	/**
	 * Diese Methode aktualisiert ein <code>Contact</code> Objekt in der Datenbank.
	 * 
	 * @param c das <code>Contact</code> Objekt, dass aktualisiert werden soll.
	 * @param u der <code>User</code>, der die Änderung durchführt
	 * @return Das als Parameter übergebene- <code>Contact</code> Objekt.
	 */
	
	public Contact updateContact(Contact c, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Füllen des Statements
		   stmt.executeUpdate("DELETE FROM contacts WHERE id=" + c.getId()); 
	  
		  // Erzeugen eines zweiten ungefüllten SQL-Statements
	   Statement stmt2 = con.createStatement();
	   
	   // Füllen des Statements
	   stmt2.executeUpdate("INSERT INTO contacts (id) VALUES " 
	   
			   + "(" + c.getId() + ")"  );
	   /**
	    * Die <code>Values</code> des <code>Contact</code> Objektes werden in eine Arraylist extrahiert und über die 
	    * @insertPValue Methode in der Datenbank gespeichert.
	    */
	   ArrayList<PValue> cv = c.getValues();
	   
	   for (int i =0; i<cv.size(); i++) {
	  
		   //Der als Parameter mitgegebene User wird als Owner der einzelnen neuen Eigenschaftsausprägungen festgelegt.
		   updatePValue(cv.get(i), c, u);
	   }
	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	  return c;
	}
		/**
		 * Diese Methode löscht ein <code>Contact</code> Objekt aus der Datenbank.
		 * 
		 * @param c das <code>Contact</code> Objekt, dass gelöscht werden soll.
		 * 
		 */

	public void deleteContact(Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Füllen des Statements
		   stmt.executeUpdate("DELETE FROM contacts WHERE id=" + c.getId()); 

	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
	
	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten eines <code>User</code>
	 * Objekts aus der Datenbank zurück.
	 * 
	 * @param u das <code>User</code> Objekt, dessen Kontakte wiedergegeben werden sollen.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten des <code>User</code> Objekts.
	 */

	public ArrayList<Contact> findAllContact(User u){
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<Contact> al = new ArrayList();
	    
	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT C-ID FROM C-Teilhaberschaft " + "WHERE U-ID=" + u.getId() + " ORDER BY -");

	  while (rs.next()) {
	      
		  //Befüllen des Kontakt-Objekts
	        Contact c = new Contact();
	        c.setId(rs.getInt("C-ID"));
	      //  c.setOwnerID(rs.getInt("owner"));
	        al.add(c);
	        
	      }
	  return al;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
	
	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einem bestimmten Vornamen
	 * zurück
	 * @param fn der Vorname nach dem gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit diesem Vornamen.
	 */
	
	public ArrayList<Contact> findContactByFirstName(String fn){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<Contact> al = new ArrayList();
	    
	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT Firstname FROM contacts " + "WHERE Firstname=" + fn + " ORDER BY -");

	  while (rs.next()) {
	      
		//Befüllen des Kontakt-Objekts
	        Contact c = new Contact();
	        c.setFirstname(rs.getString("FirstName"));
	      //  c.setOwnerID(rs.getInt("owner"));
	        al.add(c);
	        
	      }
	  return al;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
	
	
	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einem bestimmten Nachamen
	 * zurück
	 * @param ln der Nachname nach dem gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit diesem Vornamen.
	 */
	
	public ArrayList<Contact> findContactByLastName(String ln){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<Contact> al = new ArrayList();
	    
	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT Lastname FROM contacts " + "WHERE Lastname=" + ln + " ORDER BY -");

	  while (rs.next()) {
	      
		//Befüllen des Kontakt-Objekts
	        Contact c = new Contact();
	        c.setLastName(rs.getString("LastName"));
	      //  c.setOwnerID(rs.getInt("owner"));
	        al.add(c);
	        
	      }
	  return al;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
		
	
	/**
	 * Diese Methode gibt ein <code>Contact</code> Objekt zurück, dass eine bestimmte ID hat.
	 * @param id die Id nach welcher gesucht werden soll.
	 * @return Das <code>Contact</code> Objekt mit der gesuchten id.
	 */
	
	public Contact findContactById(int id)  {
	    // Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen eines Kontakt-Objektes
	   Contact c = new Contact();

	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT id FROM contacts " + "WHERE id=" + id + " ORDER BY -");
	   
	  if (rs.next()) {
	       
		//Befüllen des Kontakt-Objekts
	        c.setId(rs.getInt("id"));
	      //  c.setOwnerID(rs.getInt("owner"));
	        
	      }
	  return c;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	 
	  }
	
	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>User</code> Objekten die eine Teilhaberschaft 
	 * an einem bestimmten Kontakt besitzen.
	 * @param c das <code>Contact</code> Objekt, dessen Teilhaber gesucht werden.
	 * @return Die <code>ArrayList</code> mit den Teilhabern.
	 */
	
	public ArrayList<User> findCollaborators(Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<User> al = new ArrayList();
	    
	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT U-ID FROM C-Teilhaberschaft " + "WHERE C-Id=" + c.getId() + " ORDER BY -");

	  while (rs.next()) {
	      
		//Befüllen des User-Objekts
	        User u = new User();
	        u.setId(rs.getInt("id"));
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
	 * Diese Methode trägt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>Contact</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einem Kontakt Teilhaberschaftsrechte erlangen soll.
	 * @param c der Kontakt an dem ein User Teilhaberschaft haben soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das Übergebene <code>Contact</code> Objekt
	 */
	
	public Contact insertCollaboration(User u, Contact c, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	    
	   // Füllen des Statements
	   stmt.executeUpdate("INSERT INTO K-Teilhaberschaft (K-id, U-id, IsOwner) VALUES " 
	   
			   + "(" + c.getId() + "," + u.getId() + "," + IsOwner + ")"  );

	  	  return c;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
	
	/**
	 * Diese Methode löscht eine Teilhaberschaft zwischen einem <code>User</code> Objekt und einem <code>Contact</code> Objekt.
	 * 
	 * @param c der ausgewählte Kontakt.
	 * @param u der Nutzer der die Teilhaberschaft zu dem <code>Contact</code> Objekt verlieren soll.
	 */
	
	public void deleteCollaboration(Contact c, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Füllen des Statements
		   stmt.executeUpdate("DELETE FROM C-Teilhaberschaft WHERE U-id=" + u.getId() + "AND C-Id=" + c.getId() ); 

	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
}