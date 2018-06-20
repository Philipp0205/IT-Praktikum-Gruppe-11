package de.hdm.group11.jabics.server.db;

import java.sql.*;
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
			Class.forName("com.mysql.jdbc.GoogleDriver");
			
			/**
			 * Der DriverManager baut mit den angegebenen Verbindungsinformationen die Verbindung zur Datenbank auf. 
			 * Diese Verbinfung wird in der Variable  "con" gespeichert.  
			 */
			//con = DriverManager.getConnection("jdbc:mysql://35.198.159.112:3306/jabics?verifyServerCertificate=false&useSSL=true","root","ThieskesOberesDrittel!");
			/* Bin mir nicht sicher ob die klappt: */
			con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-jabics:europe-west3:jabics/jabics?user=root&password=ThieskesOberesDrittel!");
			 /**/
			
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