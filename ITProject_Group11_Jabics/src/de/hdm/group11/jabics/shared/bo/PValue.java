package de.hdm.group11.jabics.shared.bo;

import java.util.Date;

public class PValue {
	
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
	 * Getter uns Setter 
	 */
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}

}
