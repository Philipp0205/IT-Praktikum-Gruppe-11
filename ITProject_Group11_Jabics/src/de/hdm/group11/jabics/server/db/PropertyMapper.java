/**
 * 
 */
package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

/**
 * @author Brase
 * @author Stahl
 *
 * Diese Mapper-Klasse realisiert die Abbildung von <code>Property</code> Objekten auf die relationale Datenbank.
 * Sie stellt alle notwendigen Methoden zur Verwaltung der Eigenschaften in der Datenbank zur Verfügung. 
 *
 */
public class PropertyMapper {
	
	/**
	 * @author Thies
     * Aus dem Bankprojekt
     * 
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
	 * @author Thies
     * Aus dem Bankprojekt
     * 
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
	 * neue Instanzen dieser Klasse zu erzeugen. 
	 */
	
	protected PropertyMapper() {
		
	}
	
	/**
	 * @author Thies
     * Aus dem Bankprojekt
     * 
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
	 * Diese Methode trägt eine Eigenschaft in die Datenbank ein.
	 * 
	 * @param p das <code>Property</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter übergebene- <code>Property</code> Objekt.
	 */
	
	public Property insertProperty(Property p){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	    // Erzeugen eines ungefüllten SQL-Statements
	    Statement stmt = con.createStatement();
	   
	    
	    // Füllen des Statements
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
	 * Diese Methode löscht ein <code>Property</code> Objekt aus der Datenbank.
	 * 
	 * @param p das <code>Property</code> Objekt, dass gelöscht werden soll.
	 * 
	 */
	
	public void deleteProperty(Property p){
		// Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();
	    
	    try {
	   
	    	// Erzeugen eines ungefüllten SQL-Statements
	    	Statement stmt = con.createStatement();
		   
	    	// Füllen des Statements
	    	stmt.executeUpdate("DELETE FROM Property WHERE P-id=" + p.getId()); 
	  	  
	    }
	    catch (SQLException e) {
	    	System.err.print(e); 
	    }

	  }
	
	/**
	 * Diese Methode gibt ein <code>Property</code> Objekt zurück, dass eine bestimmte ID hat.
	 * @param id die Id nach welcher gesucht werden soll.
	 * @return Das <code>PValue</code> Objekt mit der gesuchten id.
	 */
	
	public Property findPropertyById(int id)  {
	    // Erzeugen der Datenbankverbindung
	    Connection con = DBConnection.connection();

	    try {
	    	// Erzeugen eines ungef�llten SQL-Statements
	    	Statement stmt = con.createStatement();
	   
	    	//Erzeugen eines Property-Objektes
	    	
	    	Property p = null;
	    	// Füllen des Statements
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM Property " + "WHERE PropertyID = " + id + " ORDER BY -");
	   
	    	if (rs.next()) {
	    		
	    		//Befüllen des Property-Objekts
	    		p.setId(rs.getInt("propertyID"));
	    		
	    		
	    	}
	    	return p;
	    }
	    catch (SQLException e) {
	    	System.err.print(e);
	    	return null;
	    }
	 
	  }

}