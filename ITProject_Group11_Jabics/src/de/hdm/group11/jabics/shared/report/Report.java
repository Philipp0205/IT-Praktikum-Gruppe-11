package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.time.LocalDateTime;

import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Report is the basic class of all reports. Reports have methods and attributes which are inherited from all other reports.
 * @author Kurrle
 * @author Anders
 *
 */

public abstract class Report implements Serializable {
	
	
	static final long serialVersionUID = 1L;
	
	LocalDateTime creationDate;
	JabicsUser creator;
	
	public Report() {
		this.creationDate = LocalDateTime.now();
	}
	
	
	/*
	 * Getters and Setters
	 */
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
//	public void setCreationDateNow() {
//		this.creationDate = LocalDateTime.now();
//	}
	
	public JabicsUser getCreator() {
		return creator;
	}
	public void setCreator(JabicsUser creator) {
		this.creator = creator;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
