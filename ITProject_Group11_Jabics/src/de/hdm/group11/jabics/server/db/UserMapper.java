package de.hdm.group11.jabics.server.db;

import java.sql.*;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.*;

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
	 * Struktur von
	 * @author Thies
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Stahl
	 * 
	 * Die Klasse UserMapper wird nur einmal instantiiert. Man spricht
     * hierbei von einem sogenannten <b>Singleton</b>.
     * <p>
     * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
     * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
     * einzige Instanz dieser Klasse.
     * 
     * @see userMapper()
	 */  	
	private static UserMapper userMapper = null;
	
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
	protected UserMapper() {	
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
	 * <code>UserMapper.userMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>UserMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> UserMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>UserMapper</code>-Objekt.
	 * @see userMapper
	 */  
	public static UserMapper userMapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}
		return userMapper;
	}
	
	public JabicsUser findUserByContact(Contact c) {
		
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		JabicsUser u = new JabicsUser();
		
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUserID und ContactCollaboration zum Herausfinden der Userinformationen. 
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email"
					+ " FROM systemUser"
					+ " LEFT JOIN contactCollaboration ON systemUser.systemUserID = contactCollaboration.systemUserID"
					+ " WHERE contactCollaboration.contactID = " + c.getId() + " AND isOwner = 1" );
			
			if(rs.next()) {
			u.setId(rs.getInt("systemUserID"));
			u.setEmail(rs.getString("email"));
			u.setUsername(rs.getString("name"));
			}
		}
		catch (SQLException e) {
		    System.err.print(e);  
		}
		return u;
	}
	
	public JabicsUser findUserByContactList(ContactList cl) {
		
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		
		JabicsUser u = new JabicsUser();
		
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUserID und ContactListCollaboration zum Herausfinden der Userinformationen. 
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name "
					+ " FROM systemUser"
					+ " LEFT JOIN contactlistCollaboration ON systemUser.systemUserID = contactlistCollaboration.systemUserID"
					+ " WHERE contactlistCollaboration.contactListID = " + cl.getId() + " AND isOwner = 1");
			
			if(rs.next()) {
			u.setId(rs.getInt("systemUserID"));
			u.setEmail(rs.getString("email"));
			u.setUsername(rs.getString("name"));
			}
		}
		catch (SQLException e) {
		    System.err.print(e);  
		}
		return u;
	}
	
	public JabicsUser findUserByPValue(PValue pv) {
		
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		JabicsUser u = new JabicsUser();
		
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();

			// Join zwischen SystemUserID und PValueCollaboration zum Herausfinden der Userinformationen. 
			ResultSet rs = stmt.executeQuery("SELECT systemUser.systemUserID, systemUser.email, systemUser.name "
					+ " FROM systemUser"
					+ " LEFT JOIN pValueCollaboration ON systemUser.systemUserID = pValueCollaboration.systemUserID"
					+ " WHERE pValueCollaboration.pValueID = " + pv.getId() + " AND isOwner = 1");
			
			if(rs.next()) {
			u.setId(rs.getInt("systemUserID"));
			u.setEmail(rs.getString("email"));
			u.setUsername(rs.getString("name"));
			}
		}
		catch (SQLException e) {
		    System.err.print(e);  
		}
		return u;
	}
	
	
	 /** 
	 * Diese Methode tr�gt ein <code>User</code> Objekt in die Datenbank ein.
	 * @param u das <code>User</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter übergebene- <code>User</code> Objekt.
	 */
	public JabicsUser insertUser(JabicsUser u){
		
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		    
		try {
			// Einfügen des Users in die Datenbank.
			String query = ("INSERT INTO systemUser (email, name) VALUES " + "('"  + u.getEmail() + "','" + u.getUsername() + "')"  );
			
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
			
			stmt.executeUpdate( query, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				u.setId(rs.getInt(1));
			}
		
			return u;
		}
		catch (SQLException e) {
		    System.err.print(e);  
		}
		return null;
	}
		
	/**
	 * Diese Methode l��scht ein <code>User</code> Objekt aus der Datenbank.
	 * 
	 * @param u das <code>User</code> Objekt, dass gelöscht werden soll.
	 */
	public void deleteUser(JabicsUser u){
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
		    
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
			   
			// Löschen des Users.
			stmt.executeUpdate("DELETE FROM systemUser WHERE systemUserID = " + u.getId()); 
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
	public ArrayList<JabicsUser> findAllUser(){
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();
	    
		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
	   
			//Erzeugen einer ArrayList
			ArrayList<JabicsUser> al = new ArrayList<JabicsUser>();
	    
			// Auswählen der <code>User</code> Objekte geordnet nach ihrer E-Mail Adresse.
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser ORDER BY email");

			while (rs.next()) {
	      
				//Erstellen eines User-Objekts
				JabicsUser u = new JabicsUser();
				
				//Befüllen des Kontakt-Objekts und Einfügen in die Arraylist.
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
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
	 * Diese Methode erlaubt die Suche eines  <code>User</code> Objekts in der Datenbank.
	 * 
	 * @param id  die id nach der gesucht werden soll.
	 * @return das gesuchte  <code>User</code> Objekt.
	 */
	public JabicsUser findUserById(int id)  {
		// Erzeugen der Datenbankverbindung
		Connection con = DBConnection.connection();

		try {
			// Erzeugen eines ungefüllten SQL-Statements
			Statement stmt = con.createStatement();
		   
			// Auswählen aller User aus der Datenbank, die eine bestimmte ID haben.
			ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser " + " WHERE systemUserID = " + id);
		   
			if (rs.next()) {
				JabicsUser u = new JabicsUser();
				
				//Befüllen des Kontakt-Objekts
				u.setId(rs.getInt("systemUserID"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("name"));
		        
		        return u;
			}else
			return null;
		}
		catch (SQLException e) {
		    System.err.print(e);
		    return null;
		}
	}	


/**
 * Diese Methode erlaubt die Suche eines  <code>User</code> Objekts in der Datenbank nach seiner E-Mail-Adresse.
 * 
 * @param email  die email nach der gesucht werden soll.
 * @return das gesuchte  <code>User</code> Objekt.
 */
public JabicsUser findUserByEmail(String email)  {
	// Erzeugen der Datenbankverbindung
	Connection con = DBConnection.connection();

	try {
		// Erzeugen eines ungefüllten SQL-Statements
		Statement stmt = con.createStatement();
	   
		// Auswählen aller User aus der Datenbank, die eine bestimmte ID haben.
		ResultSet rs = stmt.executeQuery("SELECT * FROM systemUser " + " WHERE email = '" + email+"'");
	   
		if (rs.next()) {
			JabicsUser u = new JabicsUser();
			
			//Befüllen des Kontakt-Objekts
			u.setId(rs.getInt("systemUserID"));
			u.setEmail(rs.getString("email"));
			u.setUsername(rs.getString("name"));
	        
	        return u;
		}else
		return null;
	}
	catch (SQLException e) {
	    System.err.print(e);
	    return null;
	}
}	
}