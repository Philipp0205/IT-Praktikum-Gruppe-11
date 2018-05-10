package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

/** 
 * A simple report which extends the super class Report. 
 * @author P
 *
 */

public abstract class SimpleReport extends Report  {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<PropertyView> content = new ArrayList<PropertyView>();
	private String userInfo;
	
	// TODO 
	public void addLine(PropertyView l) { 
		this.content.add(l);
	}
	
	/**
	 * Getters and setters
	 */
	
	public void setContent(ArrayList<PropertyView> content) {
		this.content = content;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

}
