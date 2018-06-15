package de.hdm.group11.jabics.server.db;

import java.time.LocalDateTime;

public class ServiceClass {
	
	/** 
	 * Diese Methode convertiert ein Datumsobjekt in ein von der Datenbank lesbares Stringobjekt. 
	 * 
	 * @param c das <code>Contact</code> Objekt, dass in die Datenbank eingetragen werden soll.
	 * @return Das als Parameter übergebene- <code>Contact</code> Objekt.
	 */
	public static String convertdate(LocalDateTime ldt){
		String convDate = new String();
		convDate = (""+ldt.getYear() +"-"+ ldt.getMonthValue()
				+"-"+ ldt.getDayOfMonth()+" "+ ldt.getHour()
				+":"+ ldt.getMinute()+":"+ldt.getSecond());
		return convDate;
	}
	public static String convertdatevalue(LocalDateTime ldt){
		String convDate = new String();
		convDate = (""+ldt.getYear() +"-"+ ldt.getMonthValue()
			+"-"+ ldt.getDayOfMonth() );
		return convDate;
	}
}
