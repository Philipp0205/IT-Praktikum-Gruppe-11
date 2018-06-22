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
		 String url = null;
		 
		try {
			/** 
			 * Laden des JDBC Treibers
			 */
			
			/**
			 * Der DriverManager baut mit den angegebenen Verbindungsinformationen die Verbindung zur Datenbank auf. 
			 * Diese Verbinfung wird in der Variable  "con" gespeichert.  
			 */
			/* Bin mir nicht sicher ob die klappt: */
			
			if (SystemProperty.environment.value() ==
				      SystemProperty.Environment.Value.Production) {
				    // Load the class that provides the new "jdbc:google:mysql://" prefix.
				    Class.forName("com.mysql.jdbc.GoogleDriver");
				    con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-jabics:europe-west3:jabics/jabics?user=root&password=ThieskesOberesDrittel!");
				  } else {
				    // Local MySQL instance to use during development.
				    Class.forName("com.mysql.jdbc.Driver");
				  //url = ("jdbc:mysql://mysql.webhosting31.1blu.de/db242770x2739576?user=s242770_2739576&password=itPROJEKT2018");
				//  url = ("jdbc:mysql://mysql.webhosting31.1blu.de/db242770x2739576?verifyServerCertificate=false&useSSL=true","s242770_2739576","itPROJEKT2018");
				    con = DriverManager.getConnection("jdbc:mysql://35.198.159.112:3306/jabics?verifyServerCertificate=false&useSSL=true","root","ThieskesOberesDrittel!");
				  }
			
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
