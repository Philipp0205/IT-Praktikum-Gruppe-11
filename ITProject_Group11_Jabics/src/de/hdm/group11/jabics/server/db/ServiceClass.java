package de.hdm.group11.jabics.server.db;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Brase
 * @author Stahl
 * 
 * 
 * Diese Service-Klasse realisiert Methoden für das Konvertieren von Datumsformaten wie sql Date oder LocalDateTime 
 * in von der Datenbank lesbare String Objekte.  
 *
 */

public class ServiceClass {
	
	/** 
	 * Diese Methode convertiert ein Datumsobjekt in ein von der Datenbank lesbares Stringobjekt. 
	 * 
	 * @param ldt das <code>LocalDateTime</code> Objekt, dass konvertiert werden soll.
	 * @return das String Objekt in der korrekten SQL-Syntax.
	 */
	public static String convertdate(LocalDateTime ldt){
		String convDate = new String();
		convDate = (""+ldt.getYear() +"-"+ ldt.getMonthValue()
				+"-"+ ldt.getDayOfMonth()+" "+ ldt.getHour()
				+":"+ ldt.getMinute()+":"+ldt.getSecond());
		return convDate;
	}

	/** 
	 * Diese Methode convertiert ein Datumsobjekt in ein von der Datenbank lesbares Stringobjekt. 
	 * 
	 * @param ldt das <code>LocalDate</code> Objekt, dass konvertiert werden soll.
	 * @return das String Objekt in der korrekten SQL-Syntax.
	 */

public static String convertdatevalue(LocalDate ldt){
	
	String convDate = new String();
	
	convDate = (""+ldt.getYear() +"-"+ ldt.getMonthValue()
			+"-"+ ldt.getDayOfMonth() );
	  return convDate;
	}
}
