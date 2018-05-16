/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * Die DBConnection-Klasse stellt �ber die connection() Methode eine Verbindung mit der Datenbank zur Verf�gung. 
 * 
 * @author Brase
 * @author Stahl
 *
 */
public class DBConnection {

	private static Connection con = null;

	//private static String googleUrl = "";
	
public static Connection connection() throws ClassNotFoundException {
		
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
					
		System.out.println("Connected");
			
		} catch (SQLException e) {

			System.err.print(e);
		
		} 
		//R�ckgabe der Verbindung
		return con;
	
}
	

}