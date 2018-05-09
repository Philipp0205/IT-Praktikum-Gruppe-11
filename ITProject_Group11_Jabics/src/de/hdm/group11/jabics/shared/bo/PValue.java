package de.hdm.group11.jabics.shared.bo;

import java.util.Date;

/** 
 * Realises a Poperty Value of a Contact. For example: Name, mobilenumber, adress etc.
 * @author Philipp 
 *
 */

public class PValue {
	
	private int intValue; 
	private String stringValue; 
	private Date dateValue; 
	private float floatValue; 
	private Property property;
	
	private int pointer = 0; 
	
	/** 
	 *  Return a String representation of the Property Value. 
	 */
	public String toString() {
		switch (pointer) { 
		case 1 : 
			return Integer.toString(intValue);
		case 2: 
			return stringValue; 
		case 3: 
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
	 * Getter uns Setter of the Attributes. 
	 */
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
		this.pointer = 1; 
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		this.pointer = 2; 
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		this.pointer = 3; 
	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
		this.pointer = 4; 
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}

}
