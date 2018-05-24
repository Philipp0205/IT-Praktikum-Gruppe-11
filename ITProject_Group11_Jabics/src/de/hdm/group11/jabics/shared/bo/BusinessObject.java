package de.hdm.group11.jabics.shared.bo;

import java.time.LocalDateTime;



/**
 * This is the Base Class for all Business Objects in Jabics.
 * @author Anders
 * 
 * TODO add date Updated to every set method! 
 */

public abstract class BusinessObject {
	
	public BusinessObject() { 
		this.dateCreated = LocalDateTime.now();
		this.dateUpdated = this.dateCreated;
	}

	int id;
	User owner;
	//Date dateCreated;
	//Date dateUpdated;
	
	LocalDateTime dateCreated;
	LocalDateTime dateUpdated;
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
	public User getOwner() {
		return this.owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}
	
	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public long getSerialVersionUID() {
		return serialVersionUID;
	}
	public void setSerialVersionUID(long serialVersionUID) {
		this.serialVersionUID = serialVersionUID;
	}
	
}