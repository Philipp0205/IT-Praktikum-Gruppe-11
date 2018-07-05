package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Report is the basic class of all reports. Reports have methods and attributes which are inherited from all other reports.
 * @author Kurrle
 * @author Anders
 *
 */

public abstract class Report implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Date creationDate;
	Paragraph creator;
	
	
//	public Report() {
//	//	this.creationDate = new Date();
//	}
	
	public Report() {}

	/*
	 * Getters and Setters
	 */
	public Date getCreationDateasString() {
		return creationDate;
	}
	public String getCreationDateAsString() {
		return creationDate.toString();
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Paragraph getCreator() {
		return creator;
	}
	public void setCreator(Paragraph creator) {
		this.creator = creator;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
