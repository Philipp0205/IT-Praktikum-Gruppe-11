package de.hdm.group11.jabics.server.db;

import java.sql.*;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import de.hdm.group11.jabics.shared.bo.*;

/**
 * 
 * Diese Mapper-Klasse realisiert die Abbildung von <code>Contact</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Kontakte in der Datenbank zur Verfügung. 
 * 
 * @author Brase
 * @author Stahl
 *
 */
public class ContactMapper{

	/**
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
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
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	protected ContactMapper() {
	}
	
	/**
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
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
		String query = ("INSERT INTO contact (nickname) VALUES ('" + c.getName() + "') ");
		// Erzeugen eines ungefüllten SQL-Statements
		Statement stmt = con.createStatement();
		stmt.executeUpdate( query, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		Statement stmt2 =  con.createStatement();
		ResultSet rs2 = stmt2.executeQuery("SELECT * FROM contact WHERE contactID = " + rs.getInt(1));
		
		if(rs.next()) {
			c.setId(rs.getInt(1));
		}
		if(rs2.next()) {
			c.setDateCreated(rs2.getTimestamp("dateCreated"));
			c.setDateUpdated(rs2.getTimestamp("dateUpdated"));
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
	 * @return Das als Parameter übergebene- <code>Contact</code> Objekt.
	 */
	public Contact updateContact(Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
		  
	    	// Aktualisieren des Updatedatums des <code>Contact</code> Objekts.
	    	stmt.executeUpdate("UPDATE contact SET dateUpdated = CURRENT_TIMESTAMP AND SET nickname = '" + c.getName() + "' WHERE contactID = " + c.getId());
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
		   stmt.executeUpdate("DELETE FROM contact WHERE contactID = " + c.getId()); 
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
	public ArrayList<Contact> findAllContacts(JabicsUser u){
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
	    
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
	   
			//Erzeugen einer ArrayList
			ArrayList<Contact> al = new ArrayList<Contact>();
	    
			// Join zwischen Contact und ContactCollaboration und Auswählen der Stellen mit einer bestimmten User-ID.
			ResultSet rs = stmt.executeQuery("SELECT contact.contactID, contact.dateCreated, contact.dateUpdated"
			+ " FROM contact"
			+ " LEFT JOIN contactCollaboration ON contact.contactID = contactCollaboration.contactID"
			+ " WHERE contactCollaboration.systemUserID = " + u.getId());
			
			while (rs.next()) {
				//Instanzierung eines Kontaktobjekts.
				Contact c = new Contact();
	      
				//Befüllen des Kontakt-Objekts und hinzufügen in die ArrayList.
				c.setId(rs.getInt("contactID"));
	    		c.setDateCreated(rs.getTimestamp("dateCreated"));
	    		c.setDateUpdated(rs.getTimestamp("dateUpdated"));
	    		c.setName(rs.getString("nickname"));
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
	    	// Auswählen eines Kontakts mit einer bestimmten ID.
			ResultSet rs = stmt.executeQuery("SELECT * FROM contact WHERE contactID = " + id);
	   
			//Erzeugen eines Kontakt-Objektes
	    	Contact c = new Contact();
	    	if (rs.next()) {
		    	//Befüllen des Kontakt-Objekts und hinzufügen in die ArrayList.
				c.setId(rs.getInt("contactID"));
				c.setDateCreated(rs.getTimestamp("dateCreated"));
	    		c.setDateUpdated(rs.getTimestamp("dateUpdated"));
	    		c.setName(rs.getString("nickname"));
	    	}
	    	return c;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
	
	/** 
	 * Mit dieser Methode werden alle <code>Contact</code> Objekte einer bestimmten Liste aus der Datenbank abgerufen.
	 *
	 * @param cl das <code>ContactList</code> Objekt aus welchem alle Kontakte ermittelt werden sollen.
	 * @return Die gewollten <code>Contact</code> Objekte in Form einer ArrayList.
	 */
	
	public ArrayList<Contact> findContactsOfContactList(ContactList cl)  {
		
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	    	
	    	//Erzeugen einer ArrayList
        	ArrayList<Contact> al = new ArrayList<Contact>();
	    	
	    	// Join zwischen Contact und ContactContactlist um <code>Contact</code> Objekte einer Liste auszuwählen.
	    	ResultSet rs = stmt.executeQuery("SELECT contact.contactID, contact.dateCreated, contact.dateUpdated"
	    			+ " FROM contact"
	    			+ " LEFT JOIN contactContactLists ON contact.contactID = contactContactLists.contactID"
	    			+ " WHERE contactContactLists.contactListID = " + cl.getId()) ;
	   
	    		//Befüllen des Kontaktlisten-Objekts
	    		while (rs.next()) {
					
					//Instanzierung eines Kontaktobjekts.
					Contact c = new Contact();
		      
					//Befüllen des Kontakt-Objekts und hinzufügen in die ArrayList.
					c.setId(rs.getInt("contactID"));
					c.setDateCreated(rs.getTimestamp("dateCreated"));
		    		c.setDateUpdated(rs.getTimestamp("dateUpdated"));
		    		c.setName(rs.getString("nickname"));
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
	 * Diese Methode gibt eine <code>ArrayList</code> mit allen <code>User</code> Objekten die eine Teilhaberschaft 
	 * an einem bestimmten Kontakt besitzen.
	 * @param c das <code>Contact</code> Objekt, dessen Teilhaber gesucht werden.
	 * @return Die <code>ArrayList</code> mit den Teilhabern.
	 */
	public ArrayList<JabicsUser> findCollaborators(Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	   
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();

	    	// Auswählen von Usern mit einer Bestimmten ID in der contactCollaboration Tabelle.
	    	ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email"
					+ " FROM systemUser"
					+ " LEFT JOIN contactCollaboration ON systemUser.systemUserID = contactCollaboration.systemUserID"
					+ " WHERE contactCollaboration.contactID = " + c.getId()  );

	    	while (rs.next()) {
	    		//Befüllen des User-Objekts und hinzufügen in die ArrayList.
	    		JabicsUser u = new JabicsUser(rs.getString("email"));
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
	public Contact insertCollaboration(JabicsUser u, Contact c, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();

	    	// Einfügen der Teilhaberschaft in die contactCollaboration-Tabelle.
	    	stmt.executeUpdate("INSERT INTO contactCollaboration (isOwner, contactID, systemUserID) VALUES " 
	    	+ "(" + IsOwner + ", "
	    	+ c.getId() + ", "
	    	+ u.getId() + ")");

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
	public void deleteCollaboration(Contact c, JabicsUser u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Löschen der Teilhaberschaft.
		   stmt.executeUpdate("DELETE FROM contactCollaboration WHERE systemUserID= " + u.getId() + " AND contactID= " + c.getId() );   	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    }
	}
}