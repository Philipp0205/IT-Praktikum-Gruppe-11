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
 * Sie stellt alle notwendigen Methoden zur Verwaltung der User in der Datenbank zur Verf�gung. 
 * 
 *
 */
public class UserMapper {
	
	/**
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
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	
	protected UserMapper() {
		
	}
	
	/**
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
	
	 /** 
	 * Diese Methode tr�gt ein <code>User</code> Objekt in die Datenbank ein.

	 * @param u das <code>User</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter �bergebene- <code>User</code> Objekt.
	 */


		public User insertUser(User u){
			// Erzeugen der Datenbankverbindung
		    Connection con = DBConnection.connection();
		    
		  try {
		   // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		    
		   // F�llen des Statements
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
		 * Diese Methode l�scht ein <code>User</code> Objekt aus der Datenbank.
		 * 
		 * @param u das <code>User</code> Objekt, dass gel�scht werden soll.
		 */
		
		public void deleteUser(User u){
			// Erzeugen der Datenbankverbindung
		    Connection con = DBConnection.connection();
		    
		  try {
		   
			  // Erzeugen eines ungef�llten SQL-Statements
			   Statement stmt = con.createStatement();
			   
			   // L�schen des Users
			   stmt.executeUpdate("DELETE FROM users WHERE id=" + u.getId()); 
			   
			// Erzeugen eines zweiten ungef�llten SQL-Statements
			   Statement stmt2 = con.createStatement();
			   
			   // L�schen aller Teilhaberschaften an Kontakten.
			   stmt2.executeUpdate("DELETE FROM C-Teilhaberschaft WHERE u-id=" + u.getId()); 
			   
			// L�schen aller Teilhaberschaften an KontaktListen.
			   stmt2.executeUpdate("DELETE FROM CL-Teilhaberschaft WHERE u-id=" + u.getId()); 
			   
			// L�schen aller Teilhaberschaften an <code>PValue</code> Objekten.
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
		   // Erzeugen eines ungef�llten SQL-Statements
		   Statement stmt = con.createStatement();
		   
		 //Erzeugen eines User-Objektes
		   User u = new User();

		   // F�llen des Statements
		   ResultSet rs = stmt.executeQuery("SELECT id FROM users " + "WHERE id=" + id + " ORDER BY -");
		   
		  if (rs.next()) {
		       
			//Bef�llen des Kontakt-Objekts
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
		
		/**
		 * Diese Methode erlaubt die Suche eines  <code>User</code> Objekts in der Datenbank.
		 * 
		 * @param mail  die mail nach der gesucht werden soll.
		 * @return das gesuchte  <code>User</code> Objekt.
		 */
		
		public User findUserByMail(String mail)  {
		    // Erzeugen der Datenbankverbindung
		    Connection con = DBConnection.connection();

		    try {
		    	// Erzeugen eines ungef�llten SQL-Statements
		    	Statement stmt = con.createStatement();
		   
		    	//Erzeugen eines User-Objektes
		    	User u = new User();

		    	// F�llen des Statements
		    	ResultSet rs = stmt.executeQuery("SELECT id FROM users " + "WHERE Mail = " + mail + " ORDER BY -");
		   
		    	if (rs.next()) {
		    		//Bef�llen des Kontakt-Objekts
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