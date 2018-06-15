package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import com.google.appengine.api.utils.SystemProperty;

/**
 * 
 * Die DBConnection-Klasse stellt über die connection() Methode eine Verbindung mit der Datenbank zur Verfügung.
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
	
	public static Connection connection() {
		
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
			con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting31.1blu.de/db242770x2739576?verifyServerCertificate=false&useSSL=true","s242770_2739576","itPROJEKT2018");
		
			
			System.out.println("Connected to DB");	
		} 
		catch (Exception e) {
			con = null;
			e.printStackTrace();
			System.err.print(e);
		} 
		//Rückgabe der Verbindung
		return con;
	}
}