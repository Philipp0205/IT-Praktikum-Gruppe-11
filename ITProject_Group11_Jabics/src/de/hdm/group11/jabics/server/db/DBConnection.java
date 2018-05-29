<<<<<<< HEAD
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
	

=======
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import com.google.appengine.api.utils.SystemProperty;

/**
 * 
 * Die DBConnection-Klasse stellt Ã¼ber die connection() Methode eine Verbindung mit der Datenbank zur VerfÃ¼gung.
 * 
 * Struktur von
 * @author Thies
 * 
 * Angepasst von
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
		/**
		 * Der DriverManager baut mit den angegebenen Verbindungsinformationen die Verbindung zur Datenbank auf. 
		 * Diese Verbinfung wird in der Variable  "con" gespeichert.  
		 */
		con = DriverManager.getConnection("http://mysql.webhosting31.1blu.de/phpMyAdmin/db_structure.php?server=1&db=db242770x2739576&token=fea7589569dc0203a67963a8687a0d73","s242770_2739576","itPROJEKT2018");
					
			System.out.println("Connected to DB");	
		} 
		catch (Exception e) {
			con = null;
			e.printStackTrace();
			System.err.print(e);
		} 
		//RÃ¼ckgabe der Verbindung
		return con;
	}
>>>>>>> Testing
}