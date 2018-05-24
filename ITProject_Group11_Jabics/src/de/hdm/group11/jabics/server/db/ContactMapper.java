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
import de.hdm.group11.jabics.shared.bo.ContactList;

/**
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>Contact</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Kontakte in der Datenbank zur Verf�gung. 
 * 
 * @author Brase
 * @author Stahl
 *
 */
public class ContactMapper extends PValueMapper{

	/**
	 * Die Klasse ContactMapper wird nur einmal instantiiert. Man spricht
     * hierbei von einem sogenannten <b>Singleton</b>.
     * <p>
     * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
     * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
     * einzige Instanz dieser Klasse.
     * 
     * @see contactMapper()
	 */  	
	
	private static ContactMapper contactMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	
	protected ContactMapper() {
		
	}
	
	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>ContactMapper.contactMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>ContactMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> ContactMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>ContactMapper</code>-Objekt.
	 * @see contactMapper
	 */  
	
	public static ContactMapper contactMapper() {
		if (contactMapper == null) {
			contactMapper = new ContactMapper();
		}
		
		return contactMapper;
	}
	
	/** 
	 * Diese Methode tr�gt einen Kontakt in die Datenbank ein.
	 * 
	 * @param c das <code>Contact</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter �bergebene- <code>Contact</code> Objekt.
	 */
	public Contact insertContact(Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
		  Statement stmt = con.createStatement();
			
		 // Herausfinden der bisher h�chsten Kontakt-ID.
		ResultSet rs = stmt.executeQuery("SELECT MAX(contactID) AS maxid " + "FROM contact ");

			if (rs.next()) {
				// Setzen der Kontakt-ID
				c.setId(rs.getInt("maxid") + 1); 
		  
		  // Erzeugen eines ungef�llten SQL-Statements
				Statement stmt2 = con.createStatement();
	   
	   // F�llen des Statements
	   stmt2.executeUpdate("INSERT INTO contact (contactID, dateCreated, dateUpdated,) VALUES " 
	   
			   + "(" + c.getId() + c.getDateCreated() + "," + c.getDateUpdated() + ","  + ")"  );
	  
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
	 * @param u der <code>User</code>, der die �nderung durchf�hrt
	 * @return Das als Parameter �bergebene- <code>Contact</code> Objekt.
	 */
	
	public Contact updateContact(Contact c, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
		  // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		  
	   // Aktualisieren des Updatedatums des <code>Contact</code> Objekts.
	   stmt.executeUpdate("UPDATE contact SET dateUpdated = " + c.getDateUpdated() + "WHERE contactID=" + c.getId() + ")"  );
	 
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      return null;
	    }
	  return c;
	}
		/**
		 * Diese Methode l�scht ein <code>Contact</code> Objekt aus der Datenbank.
		 * 
		 * @param c das <code>Contact</code> Objekt, dass gel�scht werden soll.
		 * 
		 */

	public void deleteContact(Contact c, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
		// Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Löschen des Kontakts.
		   stmt.executeUpdate("DELETE FROM contactCollaboration WHERE contactID=" + c.getId()); 
		   
		// Erzeugen eines zweiten ungef�llten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   // Löschen des Kontakts.
		   stmt2.executeUpdate("DELETE FROM Collaboration WHERE contactID=" + c.getId()); 
		   
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    }
	  }
	/**
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten eines <code>User</code>
	 * Objekts aus der Datenbank zur�ck.
	 * 
	 * @param u das <code>User</code> Objekt, dessen Kontakte wiedergegeben werden sollen.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten des <code>User</code> Objekts.
	 */

	public ArrayList<Contact> findAllContacts(User u){
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<Contact> al = new ArrayList();
	    
	   // F�llen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT C-ID FROM C-Teilhaberschaft " + "WHERE U-ID=" + u.getId() + " ORDER BY -");

	  while (rs.next()) {
	      
		  //Bef�llen des Kontakt-Objekts
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
	 * zur�ck
	 * @param fn der Vorname nach dem gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit diesem Vornamen.
	 */
	
	public ArrayList<Contact> findContactByFirstName(String fn){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	   //Erzeugen einer ArrayList
	    ArrayList<Contact> al = new ArrayList();
	    
	   // F�llen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT * FROM contacts " + "WHERE Firstname=" + fn + " ORDER BY -");

	  while (rs.next()) {
	      
		//Bef�llen des Kontakt-Objekts
	        Contact c = new Contact();
	        c.setId(rs.getInt("id"));
	        // setzen weiterer attribute wie datecreated und dateUpdated hier einf�gen
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
	 * zur�ck
	 * @param ln der Nachname nach dem gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit diesem Nachname.
	 */
	
	public ArrayList<Contact> findContactByLastName(String ln){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<Contact> al = new ArrayList();
	    
	   // F�llen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT Lastname FROM contacts " + "WHERE Lastname=" + ln + " ORDER BY -");

	  while (rs.next()) {
	      
		//Bef�llen des Kontakt-Objekts
	        Contact c = new Contact();
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
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einer bestimmten Straße
	 * zur�ck
	 * @param street Die Straße nach der gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit dieser Straße.
	 */
	
	public ArrayList<Contact> findContactByStreet(String street){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<Contact> al = new ArrayList();
	    
	    	// F�llen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT C-ID FROM contacts " + "WHERE Street =" + street + " ORDER BY -");

	    	while (rs.next()) {
	      
	    		//Bef�llen des Kontakt-Objekts
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
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einer bestimmten Telefonnummer
	 * zur�ck
	 * @param fon Die Telefonnummer nach der gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit dieser Telefonnummer.
	 */
	
	public ArrayList<Contact> findContactByFonNumber(String fon){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<Contact> al = new ArrayList();
	    
	    	// F�llen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT C-ID FROM contacts " + "WHERE FonNumber =" + fon + " ORDER BY -");

	    	while (rs.next()) {
	      
	    		//Bef�llen des Kontakt-Objekts
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
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einer bestimmten eMail-Adresse
	 * zur�ck
	 * @param mail Die eMail-Adresse nach der gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit dieser eMail-Adresse.
	 */
	
	public ArrayList<Contact> findContactByEMail(String mail){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<Contact> al = new ArrayList();
	    
	    	// F�llen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT C-ID FROM contacts " + "WHERE EMail =" + mail + " ORDER BY -");

	    	while (rs.next()) {
	      
	    		//Bef�llen des Kontakt-Objekts
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
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einer bestimmten Postleitzahl
	 * zur�ck
	 * @param zip Die Postleitzahl nach der gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten dieser Postleitzahl.
	 */
	
	public ArrayList<Contact> findContactByZip(String zip){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<Contact> al = new ArrayList();
	    
	    	// F�llen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT C-ID FROM contacts " + "WHERE Zip =" + zip + " ORDER BY -");

	    	while (rs.next()) {
	      
	    		//Bef�llen des Kontakt-Objekts
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
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einem bestimmten Ort
	 * zur�ck
	 * @param place Der Ort nach dem gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit diesem Ort.
	 */
	
	public ArrayList<Contact> findContactByPlace(String place){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<Contact> al = new ArrayList();
	    
	    	// F�llen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT C-ID FROM contacts " + "WHERE EMail =" + place + " ORDER BY -");

	    	while (rs.next()) {
	      
	    		//Bef�llen des Kontakt-Objekts
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
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>Contact</code> Objekten mit einem bestimmten Geburtsdatum
	 * zur�ck
	 * @param birthday Das Geburtsdatum nach dem gesucht werden soll.
	 * @return Die <code>ArrayList</code> mit den <code>Contact</code> Objekten mit diesem Geburtsdatum.
	 */
	
	public ArrayList<Contact> findContactByBirthday(String birthday){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<Contact> al = new ArrayList();
	    
	    	// F�llen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT C-ID FROM contacts " + "WHERE EMail =" + birthday + " ORDER BY -");

	    	while (rs.next()) {
	      
	    		//Bef�llen des Kontakt-Objekts
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
	 * Diese Methode gibt ein <code>ContactList</code> Objekt zur�ck, dass eine bestimmte ID hat.
	 * @param id die Id nach welcher gesucht werden soll.
	 * @return Das <code>ContactList</code> Objekt mit der gesuchten id.
	 */
	
	public ContactList findContactListById(int id)  {
	    // Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen eines Kontakt-Objektes
	    	ContactList cl = new ContactList();

	    	// F�llen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT id FROM contactlists " + "WHERE id = " + id + " ORDER BY -");
	   
	    	if (rs.next()) {
	       
	    		//Bef�llen des Kontakt-Objekts
	    		cl.setId(rs.getInt("id"));
	    		//  c.setOwnerID(rs.getInt("owner"));
	        
	    	}
	    return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	 
	  }
	
	/**
	 * Diese Methode gibt ein <code>Contact</code> Objekt zur�ck, dass eine bestimmte ID hat.
	 * @param id die Id nach welcher gesucht werden soll.
	 * @return Das <code>Contact</code> Objekt mit der gesuchten id.
	 */
	
	public Contact findContactById(int id)  {
	    // Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen eines Kontakt-Objektes
	   Contact c = new Contact();

	   // F�llen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT id FROM contacts " + "WHERE id=" + id + " ORDER BY -");
	   
	  if (rs.next()) {
	       
		//Bef�llen des Kontakt-Objekts
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
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	 //Erzeugen einer ArrayList
	    ArrayList<User> al = new ArrayList();
	    
	   // F�llen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT U-ID FROM C-Teilhaberschaft " + "WHERE C-Id=" + c.getId() + " ORDER BY -");

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
	 * Diese Methode tr�gt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>Contact</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einem Kontakt Teilhaberschaftsrechte erlangen soll.
	 * @param c der Kontakt an dem ein User Teilhaberschaft haben soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das �bergebene <code>Contact</code> Objekt
	 */
	
	public Contact insertCollaboration(User u, Contact c, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungef�llten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	    
	   // F�llen des Statements
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
	 * Diese Methode l�scht eine Teilhaberschaft zwischen einem <code>User</code> Objekt und einem <code>Contact</code> Objekt.
	 * 
	 * @param c der ausgew�hlte Kontakt.
	 * @param u der Nutzer der die Teilhaberschaft zu dem <code>Contact</code> Objekt verlieren soll.
	 */
	
	public void deleteCollaboration(Contact c, User u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   
		  // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // F�llen des Statements
		   stmt.executeUpdate("DELETE FROM C-Teilhaberschaft WHERE U-id=" + u.getId() + "AND C-Id=" + c.getId() ); 

	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	      
	    }

	  }
	
}