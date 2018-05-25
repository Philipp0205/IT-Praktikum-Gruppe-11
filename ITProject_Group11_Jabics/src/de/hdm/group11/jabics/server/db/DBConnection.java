/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.google.appengine.api.utils.SystemProperty;
/**
 * 
 * Die DBConnection-Klasse stellt über die connection() Methode eine Verbindung mit der Datenbank zur Verfügung.
 * 
 * @author Thies
 * Aus dem Bankprojekt
 * 
 * @author Brase
 * @author Stahl
 *
 */
public class DBConnection {

	private static Connection con = null;

	//private static String googleUrl = "";
	
	public static Connection connection() { //wozu der  throws ClassNotFoundException {
		
		try {
		/** 
		 * Laden des JDBC Treibers
		 */
		Class.forName("com.mysql.jdbc.Driver");
			
		/**
		 * Der DriverManager baut mit den angegebenen Verbindungsinformationen die Verbindung zur Datenbank auf. 
		 * Diese Verbinfung wird in der Variable  "con" gespeichert.  
		 */
		con = DriverManager.getConnection("jdbc:mysql://localhost");
					
		System.out.println("Connected to DB");
			
		} catch (Exception e) {
			con = null;
			e.printStackTrace();
			System.err.print(e);
		
		} 
		//Rückgabe der Verbindung
		return con;
	
}
	

}