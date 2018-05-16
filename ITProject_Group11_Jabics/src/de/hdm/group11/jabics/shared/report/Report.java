package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.User;

/**
 * Report is the basic class of all reports. Reports have methods and attributes which are inherited from all other reports.
 * @author Kurrle
 * @author Anders
 *
 */

public abstract class Report implements Serializable {
	
	static final long serialVersionUID = 1L;
	
	
	Date creationDate;
	User creator;
	
	
	
	/*
	 * Getters and Setters
	 */
	
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
