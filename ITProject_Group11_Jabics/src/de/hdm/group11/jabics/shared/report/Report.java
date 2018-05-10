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
	
	Paragraph headline; 
	Paragraph footline; 
	Date creationDate;
	User creator;
	
	public String toString() {
		return this.headline + ": " + this. footline + ". Created on " + this.creationDate.toString() + " by " + this.creator.toString();
	}
	
	/*
	 * Getters and Setters
	 */
	
	public Paragraph getHeadline() {
		return headline;
	}
	public void setHeadline(Paragraph headline) {
		this.headline = headline;
	}
	public Paragraph getFootline() {
		return footline;
	}
	public void setFootline(Paragraph footline) {
		this.footline = footline;
	}
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
