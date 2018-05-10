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
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>ContactList</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Kontaktlisten in der Datenbank zur Verf�gung. 
 *
 */
public class ContactListMapper {
	
	
		/** 
		 * Diese Methode tr�gt einen <code>ContactList</code> Objekt in die Datenbank ein.
		 *
		 * @param cl das <code>ContactList</code> Objekt, dass in die Datenbank eingetragen werden soll.
		 * @return Das als Parameter �bergebene- <code>ContactList</code> Objekt.
		 */
	
	public ContactList insertContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    ArrayList<Contact> al = cl.getContacts();
	    int size = al.size();
	    
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	  
	   // F�llen des Statements
	   stmt.executeUpdate("INSERT INTO contactlists (id, ?) VALUES " );

	  
	   
	   
	   return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	  
	  }
	
	/**
	 * Diese Methode aktualisiert ein <code>ContactList</code> Objekt in der Datenbank.
	 * Dazu m�ssen in der Datenbank zwei Tabellen editiert werden. Die Kontaktlisten-Tabelle
	 * und die Kontakt-Kontaktliste-Tabelle.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter �bergebene- <code>ContactList</code> Objekt.
	 */
	
	public ContactList updateContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		// Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // F�llen des Statements
		   stmt.executeUpdate("DELETE FROM contactlists WHERE L-id=" + cl.getId()); 

		   // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   // F�llen des Statements
		   stmt2.executeUpdate("DELETE FROM contacts-contactlists WHERE L-id=" + cl.getId()); 
		  
//--------------------------------------------------------------------------------------------	
	  
		  // Erzeugen eines dritten ungef�llten SQL-Statements
	   Statement stmt3 = con.createStatement();
	   
	   // F�llen des Statements
	   stmt3.executeUpdate("INSERT INTO contactlists (?) VALUES " );

	   // Erzeugen eines vierten ungef�llten SQL-Statements
	   Statement stmt4 = con.createStatement();
	   
	   // F�llen des Statements
	   stmt4.executeUpdate("INSERT INTO contacts-contactlists (?) VALUES " );
	   
	  	  return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
	
	/**
	 * Diese Methode l�scht ein <code>ContactList</code> Objekt aus der Datenbank.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, dass gel�scht werden soll.
	 */
	
	public void deleteContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // F�llen des Statements
		   stmt.executeUpdate("DELETE FROM contactlists WHERE L-id=" + cl.getId()); 

		   // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   // F�llen des Statements
		   stmt2.executeUpdate("DELETE FROM contacts-contactlists WHERE L-id=" + cl.getId()); 
		   
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
	
	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>User</code> Objekten die eine Teilhaberschaft 
	 * an einer bestimmten Kontaktliste besitzen.
	 * @param cl das <code>ContactList</code> Objekt, dessen Teilhaber gesucht werden.
	 * @return Die <code>ArrayList</code> mit den Teilhabern.
	 */

	public ArrayList<User> findCollaborators(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<User> al = new ArrayList();
	    
	    
	   // F�llen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT U-ID FROM KL-Teilhaberschaft " + "WHERE CL-Id=" + cl.getId() + " ORDER BY -");

	  while (rs.next()) {
	      
		//Bef�llen des User-Objekts
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
	 * Diese Methode tr�gt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>ContactList</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einer Kontaktliste Teilhaberschaftsrechte erlangen soll.
	 * @param cl die Kontaktliste an welcher ein User eine Teilhaberschaft bekommen soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das �bergebene <code>ContactList</code> Objekt
	 */
	
	public ContactList insertCollaboration(User u, ContactList cl, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	    
	   // F�llen des Statements
	   stmt.executeUpdate("INSERT INTO KL-Teilhaberschaft (KL-id, U-id, IsOwner) VALUES " 
	   
			   + "(" + cl.getId() + "," + u.getId() + "," + IsOwner + ")"  );

	  	  return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }
	  return null;
	  }
	
	/**
	 * Diese Methode l�scht eine Teilhaberschaft zwischen einem <code>User</code> Objekt und 
	 * einem <code>ContactList</code> Objekt.
	 * 
	 * @param cl die ausgew�hlte Kontaktliste.
	 * @param u der Nutzer der die Teilhaberschaft zu der Kontaktlite verlieren soll.
	 */
	
	public void deleteCollaboration(ContactList cl, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // F�llen des Statements
		   stmt.executeUpdate("DELETE FROM KL-Teilhaberschaft WHERE U-id=" + u.getId() + "AND CL-Id=" + cl.getId() ); 

	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
}
	
