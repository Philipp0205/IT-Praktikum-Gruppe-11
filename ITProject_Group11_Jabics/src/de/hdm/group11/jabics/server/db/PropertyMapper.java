/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.Property;

/**
 * @author Brase
 * @author Stahl
 *
 * Diese Mapper-Klasse realisiert die Abbildung von <code>Property</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Eigenschaften in der Datenbank zur Verf�gung. 
 *
 */
public class PropertyMapper {
	
	/**
	 * Die Klasse PropertyMapper wird nur einmal instantiiert. Man spricht
     * hierbei von einem sogenannten <b>Singleton</b>.
     * <p>
     * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
     * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
     * einzige Instanz dieser Klasse.
     * 
     * @see propertyMapper()
	 */  	
	
	private static PropertyMapper propertyMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	
	protected PropertyMapper() {
		
	}
	
	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>PropertyMapper.propertyMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>PropertyMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> PropertyMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return Das <code>PropertyMapper</code>-Objekt.
	 * @see propertyMapper
	 */  
	
	public static PropertyMapper propertyMapper() {
		if (propertyMapper == null) {
			propertyMapper = new PropertyMapper();
		}
		
		return propertyMapper;
	}
	
	/** 
	 * Diese Methode tr�gt eine Eigenschaft in die Datenbank ein.
	 * 
	 * @param p das <code>Property</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter �bergebene- <code>Property</code> Objekt.
	 */
	
	public Property insertProperty(Property p){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    // Erzeugen eines ungef�llten SQL-Statements
	    Statement stmt = con.createStatement();
	   
	    
	    // F�llen des Statements
	    stmt.executeUpdate("INSERT INTO Property (P-id, P-label, P-type, P-isStandard) VALUES " 
	   
			+ "(" + p.getId() + "," + p.getLabel() + "," + p.getType() + "," + p.isStandard() + ")"  ); 

	    return p;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	  
	}
	
	/**
	 * Diese Methode aktualisiert ein <code>Property</code> Objekt in der Datenbank.
	 * 
	 * @param p das <code>Property</code> Objekt, dass aktualisiert werden soll.
	 * @return Das als Parameter �bergebene- <code>Property</code> Objekt.
	 */
	
	public Property updatePValue(Property p){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	   
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	// F�llen des Statements
	    	stmt.executeUpdate("DELETE FROM Property WHERE id=" + p.getId()); 
		   
	    	// Erzeugen eines zweiten ungef�llten SQL-Statements
	    	Statement stmt2 = con.createStatement();
	   
	    	// F�llen des Statements
	    	stmt2.executeUpdate("INSERT INTO Property (P-id, P-label, P-type, P-isStandard) VALUES " 
			   
				+ "(" + p.getId() + "," + p.getLabel() + "," + p.getType() + "," + p.isStandard() + ")"  ); 

	  	  	return p;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }

	}
	
	
	/**
	 * Diese Methode l�scht ein <code>Property</code> Objekt aus der Datenbank.
	 * 
	 * @param p das <code>Property</code> Objekt, dass gel�scht werden soll.
	 * 
	 */
	
	public void deleteProperty(Property p){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	   
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	// F�llen des Statements
	    	stmt.executeUpdate("DELETE FROM Property WHERE P-id=" + p.getId()); 
	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e); 
	    }

	  }

}