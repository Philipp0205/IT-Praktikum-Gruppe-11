package de.hdm.group11.jabics.shared.bo;

import java.util.Date;


public class PValue extends BusinessObject {

/** 
 * Realises a Poperty Value of a Contact. For example: Name, mobilenumber, adress etc.
 * @author Philipp 
 *
 */
	private int intValue; 
	private String stringValue; 
	private Date dateValue; 
	private float floatValue; 
	private Property property;
	private int pointer = 0; 
	
	
	public String toString() {
		switch (pointer) { 
		case 1 : 
			return Integer.toString(intValue);
		case 2: 
			return stringValue; 
		case 3: 
			/*
			 * TODO Neueren Shit finden Date ist alt.
			 */
			StringBuffer sBuffer = new StringBuffer("date");
			sBuffer.append(dateValue.getMonth());
			sBuffer.append(dateValue.getDay());
			sBuffer.append(dateValue.getHours());
			sBuffer.append(dateValue.getMinutes());
			return sBuffer.toString(); 
		case 4:
			return Float.toString(floatValue);
		default: 
			return null;
		}
	}
	
	/** 
	 * Getter and Setters. Setting DateUpdated to current time whenever substantial information in the Object is changed. 
	 *  Return a String representation of the Property Value. 
	 */
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
		setDateUpdated(new Date());
		this.pointer = 1; 

	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		setDateUpdated(new Date());
		this.pointer = 2; 

	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		setDateUpdated(new Date());
		this.pointer = 3; 

	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		setDateUpdated(new Date());
		this.pointer = 4; 
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
		setDateUpdated(new Date());
	}

}
