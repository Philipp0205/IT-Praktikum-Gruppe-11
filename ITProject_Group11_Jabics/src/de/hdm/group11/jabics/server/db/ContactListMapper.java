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
	   
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	// Befüllen der Kontaktlistentabelle.
	    	stmt.executeUpdate("INSERT INTO contactList (contactlistID, listname, dateCreated, dateUpdated) VALUES " + cl.getId() 
	   
	    	+ cl.getListName()  + cl.getDateCreated() + cl.getDateUpdated());
	   
	    	// Verknüpfungen zwischen Kontaktliste und Kontakten erzeugen.
	   
	    	for(int i = 0; i<al.size();i++) {
		   
	    		// Erzeugen eines zweiten ungef�llten SQL-Statements
	    		Statement stmt3 = con.createStatement();
		   
	    		stmt3.executeUpdate("INSERT INTO contactContactLists ( contactID, contactlistID) VALUES "  + al.get(i).getId() +  cl.getId() );
	   		
	    	}
	   
	    	/**
	    	 * Mit der @insertCollaboration Methode (dieser Klasse) wird der <code>Owner</code> des <code>ContactList</code> 
	    	 * Objekts festgelegt.
	    	 * 
	    	 */
	   
	    	insertCollaboration(cl.getOwner(), cl, true);
		
	    }
	   
	    catch (SQLException e) {
	    	
	    	System.err.print(e);
	    	return null;

	    }
	    
	    return cl;
	   
	}
	
	/**
	 * Diese Methode aktualisiert ein <code>ContactList</code> Objekt in der Datenbank.
	 * In der Datenbank muss in der Kontaktlisten-Tabelle ein Update des Namens erfolgen.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter übergebene- <code>ContactList</code> Objekt.
	 */
	
	public ContactList updateContactList(ContactList cl){
		
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	   
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	//Update des Namens der Kontaktliste und des letzten Updates
	    	stmt.executeUpdate("UPDATE contactList SET listname = " + cl.getListName() + ", dateUpdate = " + cl.getDateUpdated() + "  WHERE contactlistID = " + cl.getId()); 
	   
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
	    	stmt.executeUpdate("DELETE FROM contactList WHERE  contactlistID =" + cl.getId()); 

	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt2 = con.createStatement();
		   
	    	// Füllen des Statements
	    	stmt2.executeUpdate("DELETE FROM contactsContactlists WHERE contactlistID =" + cl.getId()); 
		   
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
	 * Diese Methode fügt einem <code>ContactList</code> Objekt einen oder mehrere <code>Contact</code> Objekt in der Datenbank hinzu.
	 * Dazu mussen in der Datenbank neue Tupel in der Kontakt-Kontaktliste-Tabelle angelegt werden.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter übergebene- <code>ContactList</code> Objekt.
	 */
	
	public ContactList insertContactIntoContactList(ContactList cl, Contact c){
		
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
		   
	    	// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
			   
			// Verknüpfungen zwischen Kontaktliste und Kontakten erzeugen.
			stmt.executeUpdate("INSERT INTO contactContactLists (contactID,contactlistID) VALUES " + c.getId() + cl.getId());
	    	
	    	// Erzeugen eines zweiten ungefüllten SQL-Statements
	    	Statement stmt2 = con.createStatement();
		   
	    	//Update des letzten Updates der Kontaktliste
	    	stmt2.executeUpdate("UPDATE contactList SET dateUpdate = " + cl.getDateUpdated() + "  WHERE contactlistID = " + cl.getId()); 
		   
	    	return cl;
	    	
	    }
	    
	    catch (SQLException e) {
	    	
	    	System.err.print(e);
	    	return null;
	    	
	    }

	}
	
	/**
	 * Diese Methode löscht ein <code>Contact</code> Objekt aus einer Kontaktliste.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, aus welchem der Kontakt gelöscht werden soll.
	 * @param c das <code>Contact</code> Objekt, dass aus der Liste gelöscht werden soll.
	 */
	
	public void deleteContactfromContactList(ContactList cl, Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	  try {
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Löschen des Kontakts aus der Liste
		   stmt.executeUpdate("DELETE FROM contactContactLists WHERE contactID=" + cl.getId() + "AND contactListID = " + cl.getId()); 

	  }
	    catch (SQLException e) {
	    	
	    	System.err.print(e);
	    	
	    }
	  }
	
	/**
	 * Diese Methode gibt ein <code>ContactList</code> Objekt zurück, dass eine bestimmte ID hat.
	 * @param id die Id nach welcher gesucht werden soll.
	 * @return Das <code>ContactList</code> Objekt mit der gesuchten id.
	 */
	
	public ContactList findContactListById(int id)  {
	   
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen eines Kontaktlisten-Objektes
	    	ContactList cl = new ContactList();

	    	// Füllen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM contactList " + "WHERE contactlistID = " + id );
	   
	    	if (rs.next()) {
	       
	    		//Befüllen des Kontaktlisten-Objekts
	    		cl.setId(rs.getInt("id"));
	    		cl.setListName(rs.getString("listname"));
	    		cl.setDateUpdated(rs.get);//wird noch besprochen!
	    		
	    	}
	    	
	    	return cl;
	    
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	  }
	
	/**
	 * Diese Methode gibt ein <code>ContactList</code> Objekt zurück, dass einen bestimmten Namen hat.
	 * @param name der Name nach welcher gesucht werden soll.
	 * @return Das <code>ContactList</code> Objekt mit der gesuchten id.
	 */
	
	public ArrayList<ContactList> findContactListByName(String name)  {
	   
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
		    ArrayList<ContactList> al = new ArrayList();

	    	// Füllen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM contactList " + "WHERE listname = " + name );
	   
	    	while (rs.next()) {
	       
	    		//Befüllen des Kontaktlisten-Objekts
	    		ContactList cl = new ContactList();
	    		cl.setId(rs.getInt("id"));
	    		cl.setListName(rs.getString("listname"));
	    		cl.setDateCreated(rs.get);//wird noch besprochen!
	    		cl.setDateUpdated(rs.get);//wird noch besprochen!
	    		
	    		al.add(cl);
	    		
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
	   stmt.executeUpdate("INSERT INTO contactlistCollaboration (isOwner, contactListID, systemUserID) VALUES " 
	   
			   + "(" + IsOwner + "," + cl.getId() + "," + u.getId() + ")"  );

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
		   stmt.executeUpdate("DELETE FROM contactlistCollaboration WHERE contactListID =" + cl.getId() + " AND systemUserID = " + u.getId()); 

	  	  
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
	   ResultSet rs = stmt.executeQuery("SELECT systemUserID FROM contactlistCollaboration " + "WHERE contactListID = " + cl.getId() + " ORDER BY -");

	  while (rs.next()) {
	  
		//Befüllen des User-Objekts
	        User u = new User(rs.getString("email"));
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
	
}
	
