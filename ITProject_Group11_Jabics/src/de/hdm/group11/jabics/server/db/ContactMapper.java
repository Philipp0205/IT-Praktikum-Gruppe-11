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
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Kontakte in der Datenbank zur Verfügung. 
 * 
 * @author Brase
 * @author Stahl
 *
 */
public class ContactMapper extends PValueMapper{

	/**
	 * @author Thies
     * Aus dem Bankprojekt
     * 
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
	 * @author Thies
     * Aus dem Bankprojekt
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	protected ContactMapper() {
	}
	
	/**
	 * @author Thies
     * Aus dem Bankprojekt
     * 
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
	 * Diese Methode trägt einen Kontakt in die Datenbank ein.
	 * 
	 * @param c das <code>Contact</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter übergebene- <code>Contact</code> Objekt.
	 */
	public Contact insertContact(Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
	   
			// Füllen des Statements
			stmt.executeUpdate("INSERT INTO contact (contactID, dateCreated, dateUpdated,) VALUES " 
			+ "(" + c.getId() + c.getDateCreated() + "," + c.getDateUpdated() + ","  + ")"  );
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
		   
		   // Löschen des Kontakts.
		   stmt.executeUpdate("DELETE FROM contactCollaboration WHERE contactID=" + c.getId()); 
		   
		   // Erzeugen eines zweiten ungefüllten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   // Löschen des Kontakts.
		   stmt2.executeUpdate("DELETE FROM contact WHERE contactID=" + c.getId()); 
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
	public ArrayList<Contact> findAllContacts(User u){
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
	    		//   c.setDateUpdated();//wird noch besprochen! 
	    	}
	    	return c;
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
	//Übergabeparameter Anpassen, in der DB gibt es die Spalten String, int, float, date
	/**
	public ArrayList<Contact> findContactByPValue(String pvalue){

		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	  try {
	   // Erzeugen eines ungefüllten SQL-Statements
	   Statement stmt = con.createStatement();
	   
	   //Erzeugen einer ArrayList
	    ArrayList<Contact> al = new ArrayList();
	    
	   // Füllen des Statements
	   ResultSet rs = stmt.executeQuery("SELECT * FROM contacts " + "WHERE Firstname=" + fn + " ORDER BY -");

	  while (rs.next()) {
	      
		//Befüllen des Kontakt-Objekts
	        Contact c = new Contact();
	        c.setId(rs.getInt("id"));

	      // setzen weiterer attribute wie datecreated und dateUpdated hier einfügen
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

	*/
	
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
	   
	    	//Erzeugen eines Kontakt-Objektes
	    	ContactList cl = new ContactList();

	    	// Füllen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT id FROM contactlists " + "WHERE id = " + id + " ORDER BY -");
	   
	    	if (rs.next()) {
	    		//Befüllen des Kontakt-Objekts
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
	    	ResultSet rs = stmt.executeQuery("SELECT systemUserID FROM contactCollaboration " + "WHERE contactID=" + c.getId() );

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
	
	/**
	 * Diese Methode trägt eine Teilhaberschaft eines <code>User</code> Objekts zu einem <code>Contact</code> Objekt
	 * in die Datenbank ein.
	 * 
	 * @param u der User der an einem Kontakt Teilhaberschaftsrechte erlangen soll.
	 * @param c der Kontakt an dem ein User Teilhaberschaft haben soll.
	 * @param IsOwner ein <code>boolean</code> Wert der wiederspiegelt ob der zuzuweisende Teilhaber auch der Owner ist.
	 * @return das übergebene <code>Contact</code> Objekt
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