package de.hdm.group11.jabics.shared.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;


public class PValue extends BusinessObject {

/** 
 * Realises a Poperty Value of a Contact. For example: Name, mobilenumber, adress etc.
 * @author Philipp 
 */
	private int intValue; 
	private String stringValue; 
	//private Date dateValue; 
	private LocalDate dateValue;
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
			return dateValue.toString();
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
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 1; 

	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 2; 

	}
	public LocalDate getDateValue() {
		return dateValue;
	}
	public void setDateValue(int year, int month, int dayOfMonth) {
		this.dateValue = LocalDate.of(year, month, dayOfMonth);
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 3; 

	}
	// overload method if date is given in the datatype "month". 
	public void setDateValue(int year, Month month, int dayOfMonth) {
		this.dateValue = LocalDate.of(year, month, dayOfMonth);
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 3; 

	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		this.dateUpdated = LocalDateTime.now();
		this.pointer = 4; 
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
		this.dateUpdated = LocalDateTime.now();
	}
	public int getPointer() {
		return pointer;
	}
	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

}
