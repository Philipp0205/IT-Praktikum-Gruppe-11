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

	private static String googleUrl = "jdbc:google:mysql://it-projekt-jabics:europe-west3:jabics/jabics?user=root&password=ThieskesOberesDrittel!";
	private static String localUrl = "jdbc:mysql://35.198.159.112:3306/jabics?user=root&password=ThieskesOberesDrittel!";
	
	public static Connection connection() {
		 
		if (con == null) {
			String url = null;
			try {
				if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
					// Load the class that provides the new "jdbc:google:mysql://" prefix.
					Class.forName("com.mysql.jdbc.GoogleDriver");
					url = googleUrl;
					System.out.println("googleDB!");
				} else {
					// Local MySQL instance to use during development.
					Class.forName("com.mysql.jdbc.Driver");
					url = localUrl;
					System.out.println("localDB!");
				}
				con = DriverManager.getConnection(url);
				System.out.println("Connected to DB");	
			} 
			catch (Exception e) {
			con = null;
			e.printStackTrace();
			System.err.print(e);
			} 
		}
		//Rückgabe der Verbindung
		return con;
	}
}
