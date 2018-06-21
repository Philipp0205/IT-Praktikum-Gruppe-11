package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;
import java.sql.*;


/**
 * Dies ist die Basisklasse für alle Kontakte, Listen und Property(values) in Jabics
 * Hier sind Erstelldaten und Änderungsdaten gespeichert, sowie der Ersteller eines Objektes.
 * @author Anders
 * 
 */
public abstract class BusinessObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public BusinessObject() {
	}

	int id;
	JabicsUser owner;	
	Timestamp dateUpdated;
	Timestamp dateCreated;
	
	
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
	public Timestamp getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public Timestamp getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}
	
}