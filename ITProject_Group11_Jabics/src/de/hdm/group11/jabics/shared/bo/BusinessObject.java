package de.hdm.group11.jabics.shared.bo;

import java.time.LocalDateTime;



/**
 * Dies ist die Basisklasse für alle Kontakte, Listen und Property(values) in Jabics
 * Hier sind Erstelldaten und Änderungsdaten gespeichert, sowie der Ersteller eines Objektes.
 * @author Anders
 * 
 */

public abstract class BusinessObject {
	
	public BusinessObject() { 
//		this.dateCreated = 
//		this.dateUpdated = this.dateCreated;
	}

	int id;
	JabicsUser owner;
	//LocalDateTime dateCreated;
	//LocalDateTime dateUpdated;
	
	String dateUpdated;
	String dateCreated;
	
	
	private long serialVersionUID = 1L;
	
	/**
	 * hashCode returns the id that is also used in the Database 
	 */
	public int hashCode() {
		return this.id;
	}
	
	/**
	 * Check if BusinessObject is the same as transfer parameter
	 */
	public boolean equals(Object obj) {
		if (obj instanceof BusinessObject) {
			BusinessObject bo = (BusinessObject) obj;
			if (bo.getId() == this.id) 
				return true;
		}
		return false;
	}

	/**
	 * Getters and Setters. Setting DateUpdated to current time whenever substantial information in the Object is changed.
	 */
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		/*
		 * TODO dateCreated ist im Konstruktor der Klasse sinnvoller?
		 */
		//this.dateCreated = LocalDateTime.now();
	}
	public JabicsUser getOwner() {
		return this.owner;
	}
	public void setOwner(JabicsUser owner) {
		this.owner = owner;
	}
//	public LocalDateTime getDateCreated() {
//		return dateCreated;
//	}
//	public void setDateCreated(int year, int month, int dayOfMonth, int hour, int minute, int second) {
//		this.dateCreated = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
//	}
//	public void setDateCreated(LocalDateTime dateCreated) {
//		this.dateCreated = dateCreated;
//	}
//	public LocalDateTime getDateUpdated() {
//		return dateUpdated;
//	}
//	public void setDateUpdated(LocalDateTime dateUpdated) {
//		this.dateUpdated = dateUpdated;
//	}
//	public void setDateUpdated(int year, int month, int dayOfMonth, int hour, int minute, int second) {
//		this.dateCreated = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
//	}
//	public long getSerialVersionUID() {
//		return serialVersionUID;
//	}
//	public void setSerialVersionUID(long serialVersionUID) {
//		this.serialVersionUID = serialVersionUID;
//	}
	
}