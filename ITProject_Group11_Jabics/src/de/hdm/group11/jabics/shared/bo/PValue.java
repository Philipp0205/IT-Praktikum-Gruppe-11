package de.hdm.group11.jabics.shared.bo;

import java.util.Date;

public class PValue extends BusinessObject {
	
	private int intValue; 
	private String stringValue; 
	private Date dateValue; 
	private float floatValue; 
	private Property property;
	
	public String toString() {
		return stringValue; 
		/**
		 * TODO
		 */
	}
	
	
	/** 
	 * Getter and Setters. Setting DateUpdated to current time whenever substantial information in the Object is changed. 
	 */
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
		setDateUpdated(new Date());
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		setDateUpdated(new Date());
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		setDateUpdated(new Date());
	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		setDateUpdated(new Date());
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
		setDateUpdated(new Date());
	}

}
