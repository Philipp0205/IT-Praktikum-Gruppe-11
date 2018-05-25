
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
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Kontaktlisten in der Datenbank zur Verfügung. 
 *
 */
public class ContactListMapper {
		
	/**
	 * @author Thies
     * Aus dem Bankprojekt
     * 
	 * Die Klasse ContactListMapper wird nur einmal instantiiert. Man spricht
     * hierbei von einem sogenannten <b>Singleton</b>.
     * <p>
     * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
     * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
     * einzige Instanz dieser Klasse.
     * 
     * @see contactListMapper()
	 */  	
	
	private static ContactListMapper contactListMapper = null;
	
	/**
	 * @author Thies
     * Aus dem Bankprojekt
     * 
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen.
	 */
	
	protected ContactListMapper() {
		
	}
	
	/**
	 * @author Thies
     * Aus dem Bankprojekt
     * 
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>ContactListMapper.contactListMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>ContactListMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> ContactListMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>ContactListMapper</code>-Objekt.
	 * @see contactListMapper
	 */  
	
	public static ContactListMapper contactListMapper() {
		if (contactListMapper == null) {
			contactListMapper = new ContactListMapper();
		}
		
		return contactListMapper;
	}
		
		/** 
		 * Diese Methode trägt ein <code>ContactList</code> Objekt in die Datenbank ein.
		 *
		 * @param cl das <code>ContactList</code> Objekt, dass in die Datenbank eingetragen werden soll.
		 * @return Das als Parameter übergebene- <code>ContactList</code> Objekt.
		 */
	
	public ContactList insertContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    //Extrahieren aller Kontakte aus der Kontaktliste in eine Arraylist.
	    ArrayList<Contact> al = cl.getContacts();
	   
	  try {
	   	   
		  Statement stmt = con.createStatement();
		
			 // Herausfinden der bisher höchsten Kontaktlisten-ID.
			
		ResultSet rs = stmt.executeQuery("SELECT MAX(cl-id) AS maxid " + "FROM contactlists ");

			if (rs.next()) {
				
				// Setzen der Kontaktlisten-ID
				 
				cl.setId(rs.getInt("maxid") + 1); 
	   
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt2 = con.createStatement();
	   
	   // Befüllen der Kontaktlistentabelle.
	   stmt2.executeUpdate("INSERT INTO contactlists (cl-id, CL-name, Cr-Date) VALUES " + cl.getId() 
	   
	   	+ cl.getListName()  + cl.getDateCreated() );
	   
	// Verknüpfungen zwischen Kontaktliste und Kontakten erzeugen.
	   
	   for(int i = 0; i<al.size();i++) {
		   
		// Erzeugen eines zweiten ungef�llten SQL-Statements
		   Statement stmt3 = con.createStatement();
		   
		
		   stmt3.executeUpdate("INSERT INTO contacts-contactlists (cl-id, c-id) VALUES " + al.get(i).getId() +  cl.getId() );
	   		}
	   /**
	    * Mit der @insertCollaboration Methode (dieser Klasse) wird der <code>Owner</code> des <code>ContactList</code> 
	    * Objekts festgelegt.
	    * 
	    */
		} insertCollaboration(cl.getOwner(), cl, true);
	  }
	   
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	    return cl;
	  }
	
	/**
	 * Diese Methode aktualisiert ein <code>ContactList</code> Objekt in der Datenbank.
	 * Dazu müssen in der Datenbank zwei Tabellen editiert werden. Die Kontaktlisten-Tabelle
	 * und die Kontakt-Kontaktliste-Tabelle.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter �bergebene- <code>ContactList</code> Objekt.
	 */
	
	public ContactList updateContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  //Extrahieren aller Kontakte aus der Kontaktliste in eine Arraylist.
	    ArrayList<Contact> al = cl.getContacts();
	    
	  try {
	   
		// Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Löschen der veralteten Version der Kontaktliste
		   stmt.executeUpdate("DELETE FROM contactlists WHERE CL-id=" + cl.getId()); 

		   // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   // Löschen der veralteten Verkn�pfungen zu Kontakten
		   stmt2.executeUpdate("DELETE FROM contacts-contactlists WHERE CL-id=" + cl.getId()); 
		  
//--------------------------------------------------------------------------------------------	
	  
		   // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt3 = con.createStatement();
		   
		   // Bef�llen der Kontaktlistentabelle.
		   stmt3.executeUpdate("INSERT INTO contactlists (cl-id, CL-name, Cr-Date) VALUES " + cl.getId() 
		   
		   	+ cl.getListName()  + cl.getDateCreated() );
		   
		   for(int i = 0; i<al.size();i++) {
			   
			// Erzeugen eines zweiten ungefüllten SQL-Statements
			   Statement stmt4 = con.createStatement();
			   
			// Verknüpfungen zwischen Kontaktliste und Kontakten erzeugen.
			   stmt4.executeUpdate("INSERT INTO contacts-contactlists (cl-id, c-id) VALUES " + al.get(i).getId() +  cl.getId() );
			   
			   /**
			    * Mit der @insertCollaboration Methode (dieser Klasse) wird der <code>Owner</code> des <code>ContactList</code> 
			    * Objekts festgelegt.
			    * 
			    */
				} insertCollaboration(cl.getOwner(), cl, true);
	   
	  	  return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }

	  }
	
	/**
	 * Diese Methode löscht ein <code>ContactList</code> Objekt aus der Datenbank.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, dass gelöscht werden soll.
	 */
	
	public void deleteContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Füllen des Statements
		   stmt.executeUpdate("DELETE FROM contactlists WHERE CL-id=" + cl.getId()); 

		   // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   // Füllen des Statements
		   stmt2.executeUpdate("DELETE FROM contacts-contactlists WHERE CL-id=" + cl.getId()); 
		   
		   /** 
		    * <code>Collaborations</code> werden mit der @deleteCollaboration Methode gelöst.
		    */
		   deleteCollaboration(cl, cl.getOwner());
	  }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
	
	/**
	 * Löschen eines Contact aus einer Liste
	 * @author Anders
	 */
	public void removeContactFromList(ContactList cl, Contact c) {
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
		   // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   // L�schen der veralteten Verkn�pfungen zu Kontakten
		   stmt.executeUpdate("DELETE FROM contacts-contactlists WHERE CL-id =" + cl.getId() + " AND C-id=" + c.getId()); 
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
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<User> al = new ArrayList();
	    
	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT U-id FROM KL-Teilhaberschaft " + "WHERE CL-id=" + cl.getId() + " ORDER BY -");

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
	 * Diese Methode trägt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>ContactList</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einer Kontaktliste Teilhaberschaftsrechte erlangen soll.
	 * @param cl die Kontaktliste an welcher ein User eine Teilhaberschaft bekommen soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das übergebene <code>ContactList</code> Objekt
	 */
	
	public ContactList insertCollaboration(User u, ContactList cl, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	    
	   // Füllen des Statements
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
	 * Diese Methode löscht eine Teilhaberschaft zwischen einem <code>User</code> Objekt und 
	 * einem <code>ContactList</code> Objekt.
	 * 
	 * @param cl die ausgewählte Kontaktliste.
	 * @param u der Nutzer der die Teilhaberschaft zu der Kontaktlite verlieren soll.
	 */
	
	public void deleteCollaboration(ContactList cl, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Füllen des Statements
		   stmt.executeUpdate("DELETE FROM KL-Teilhaberschaft WHERE U-id=" + u.getId() + "AND CL-Id=" + cl.getId() ); 

	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
}
	
