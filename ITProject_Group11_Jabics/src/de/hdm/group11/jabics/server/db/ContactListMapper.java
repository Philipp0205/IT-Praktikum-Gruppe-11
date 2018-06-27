package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

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
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
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
	protected ContactListMapper() {
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
	 * <code>ContactListMapper.contactListMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
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
	 * @return Das als Parameter übergebene <code>ContactList</code> Objekt.
	 */
	public ContactList insertContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	   
	    try {
	    	String query = ("INSERT INTO contactList (listname) VALUES ('" + cl.getListName() + "')");
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();	
			stmt.executeUpdate( query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			Statement stmt2 =  con.createStatement();
			ResultSet rs2;
			if(rs.next()) {
				rs2 = stmt2.executeQuery("SELECT * FROM contactList WHERE contactListID = " + rs.getInt(1));
				cl.setId(rs.getInt(1));
			if(rs2.next()) {
				cl.setDateCreated(rs2.getTimestamp("dateCreated"));
				cl.setDateUpdated(rs2.getTimestamp("dateUpdated"));
			}
			}
			con.close();
	    	return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
	
	/**
	 * Diese Methode aktualisiert ein <code>ContactList</code> Objekt in der Datenbank.
	 * In der Datenbank muss in der Kontaktlisten-Tabelle ein Update des Namens erfolgen, sowie des Datums des letzten Updates.
	 * 
	 * @param cl das <code>ContactList</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter übergebene <code>ContactList</code> Objekt.
	 */
	public ContactList updateContactList(ContactList cl){
		
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	//Update des Namens der Kontaktliste und des letzten Updates
	    	stmt.executeUpdate("UPDATE contactList SET listname = '" + cl.getListName()	+ "' WHERE contactlistID = " + cl.getId()); 
	   		con.close();
	  	  	return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
	
	/**
	 * Diese Methode löscht ein <code>ContactList</code> Objekt aus der Datenbank.
	 * @param cl das <code>ContactList</code> Objekt, dass gelöscht werden soll.
	 */
	public void deleteContactList(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	// Löschen des <code>ContactList</code> Objekts aus der Datenbank.
	    	stmt.executeUpdate("DELETE FROM contactList WHERE  contactlistID = " + cl.getId());
		    con.close();
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    }
	}
	
	/**
	 * Diese Methode fügt einem <code>ContactList</code> Objekt ein <code>Contact</code> Objekt in der Datenbank hinzu.
	 * Dazu mussen in der Datenbank neue Tupel in der Kontakt-Kontaktliste-Tabelle angelegt werden.
	 * @param cl das <code>ContactList</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter übergebene <code>ContactList</code> Objekt.
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
		   
	    	//Update des letzten Updates der Kontaktliste.
	    	stmt2.executeUpdate("UPDATE contactList SET dateUpdated = CURRENT_TIMESTAMP WHERE contactlistID = " + cl.getId()); 
		   con.close();
	    	return cl;
	    }  
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;	
	    }
	}
	
	/**
	 * Diese Methode löscht ein <code>Contact</code> Objekt aus einer Kontaktliste.
	 * @param cl das <code>ContactList</code> Objekt, aus welchem der Kontakt gelöscht werden soll.
	 * @param c das <code>Contact</code> Objekt, dass aus der Liste gelöscht werden soll.
	 */
	public void deleteContactfromContactList(ContactList cl, Contact c){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	  try {
		  // Erzeugen eines ungefüllten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		   // Löschen des Kontakts aus der Liste.
		   stmt.executeUpdate("DELETE FROM contactContactLists WHERE contactID= " + cl.getId() + " AND contactListID = " + cl.getId());
		   
		   // Erzeugen eines zweiten ungefüllten SQL-Statements
		   Statement stmt2 = con.createStatement();
		   
		   //Update des letzten Updates der Kontaktliste.
		   stmt2.executeUpdate("UPDATE contactList SET dateUpdated = CURRENT_TIMESTAMP WHERE contactlistID = " + cl.getId()); 
	  con.close();
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

	    	// Auswählen eines Contaktlistenobjekts mit einer bestimmten ID.
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM contactList " + "WHERE contactlistID = " + id );
	   
	    	if (rs.next()) {
	    		//Befüllen des Kontaktlisten-Objekts
	    		cl.setId(rs.getInt("contactListID"));
	    		cl.setListName(rs.getString("listname"));
	    		cl.setDateCreated(rs.getTimestamp("dateCreated"));
	    		cl.setDateUpdated(rs.getTimestamp("dateUpdated"));
	    	}
		    con.close();
	    	return cl;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
	
	/**
	 * Diese Methode gibt alle <code>ContactList</code> Objekte zurück, die einen bestimmten Teilhaber haben haben. 
	 * @param u der Teilhaber, dessen <code>ContactList</code> Objekte zurückgegeben werden sollen.
	 * @return Die ArrayList, die mit den <code>ContactList</code> Objekten befüllt ist.
	 */
	public ArrayList<ContactList> findContactListOfUser(JabicsUser u)  {  
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	    	//Erzeugen einer ArrayList
			ArrayList<ContactList> al = new ArrayList<ContactList>();
	    	
	    	//Erzeugen eines Kontaktlisten-Objektes
	    	//ContactList cl = new ContactList();

	    	// Join zwischen ContactList und ContactListCollaboration und Auswählen der Stellen mit einer bestimmten User-ID.
	    	ResultSet rs = stmt.executeQuery("SELECT contactList.contactlistID, contactList.listname, contactList.dateCreated, contactList.dateUpdated"
	    			+ " FROM contactList"
	    			+ " LEFT JOIN contactlistCollaboration ON contactList.contactlistID = contactlistCollaboration.contactlistID"
	    			+ " WHERE contactlistCollaboration.systemUserID =" + u.getId());
	   
	    	while (rs.next()) {
	    		//Erzeugen eines Kontaktlisten-Objektes
	    	  	ContactList cl = new ContactList();
	    		//Befüllen des Kontaktlisten-Objekts
	    		cl.setId(rs.getInt("contactListID"));
	    		cl.setListName(rs.getString("listname"));
	    		cl.setDateCreated(rs.getTimestamp("dateCreated"));
	    		cl.setDateUpdated(rs.getTimestamp("dateUpdated"));
	    		al.add(cl);
	    	}
	    	//System.out.println(cl.getListName());
		    con.close();
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
	public ContactList insertCollaboration(JabicsUser u, ContactList cl, boolean IsOwner){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	// Einfügen einer neuen Teilhaberschaft mit Eigentümerschaft in die ContactlistCollaboration Tabelle.
	    	stmt.executeUpdate("INSERT INTO contactlistCollaboration (isOwner, contactListID, systemUserID) VALUES " 
	    	+ "(" + IsOwner + ", " + cl.getId() + ", " + u.getId() + ")"  );
		con.close();
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
	 * @param u der Nutzer der die Teilhaberschaft zu der Kontaktliste verlieren soll.
	 */
	public void deleteCollaboration(ContactList cl, JabicsUser u){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	// Löschen einer Teilhaberschaft aus der ContactlistCollaboration Tabelle.
	    	stmt.executeUpdate("DELETE FROM contactlistCollaboration WHERE contactListID =" + cl.getId() + " AND systemUserID = " + u.getId());   
	    con.close();
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
	public ArrayList<JabicsUser> findCollaborators(ContactList cl){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen einer ArrayList
	    	ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();
	    
	    	// Auswählen von Tupeln mit einer bestimmten User-Id. 
	    	ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID , systemUser.mail "
	    			+ " FROM systemUser " 
	    			+ " LEFT JOIN contactlistCollaboration ON systemUser.systemUserID = contactlistCollaboration.systemUserID "
	    			+ " WHERE contactListID = " + cl.getId());

	    	while (rs.next()) {
	    		//Befüllen des User-Objekts und hinzufügen zur Arraylist.
	    		JabicsUser u = new JabicsUser(rs.getString("email"));
	    		u.setId(rs.getInt("systemUserID"));
	    		al.add(u);
	        }
		    con.close();
	    	return al;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	}
}
