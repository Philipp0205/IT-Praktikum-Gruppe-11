package de.hdm.group11.jabics.shared.bo;


/**
 * 
 * @author Anders
 */

public class Property extends BusinessObject {
	
	private String label;
	private Type type;
	private boolean isStandard = false;
	
	public Property(String label, Type type) {
		this.label = label;
		this.type = type;
	}
	
	/**
	 * Getters and Setters. Setting DateUpdated to current time whenever substantial information in the Object is changed.
	 */
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public boolean isStandard() {
		return isStandard;
	}
	public void setStandard(boolean isStandard) {
		this.isStandard = isStandard;
	}
}