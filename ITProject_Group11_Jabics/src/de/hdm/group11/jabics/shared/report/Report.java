package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.User;

/**
 * Report is the basic class of all reports. Reports have methods and attributes which are inherited from all other reports. 
 * 
 * @author Kurrle
 * @author Anders
 *
 */

public abstract class Report implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Paragraph headline; 
	private Paragraph footline; 
	private Date creationDate; 
	private User creator;
	
	
	/*
	 * TODO 
	 */
	// public ArrayList getContacts() { }
	
		

	
	
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
