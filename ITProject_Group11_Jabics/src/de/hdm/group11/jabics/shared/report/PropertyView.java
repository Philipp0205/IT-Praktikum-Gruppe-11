package de.hdm.group11.jabics.shared.report;

import de.hdm.group11.jabics.shared.bo.PValue;

public class PropertyView {

	
	private String pname; 
	private String pvalue;
	
	public PropertyView(String p, String v) {
		this.pname = p;
		this.pvalue = v;
	}
	
	public PropertyView (PValue pv) throws IllegalArgumentException {
		this.pname = pv.getProperty().getLabel();
		switch(pv.getPointer()) {
		
			/**
			 * TODO: convert data types to String, so they can be displayed in a report 
			 * case 1: this.pvalue = pv.getIntValue();
			 * case 1: this.pvalue = pv.getStringValue();
			 * case 1: this.pvalue = pv.getDateValue();
			 * case 1: this.pvalue = pv.getFloatValue();
			 */
			default: System.out.println("PropertyValue could not be found by PropertyView");
		}
	}
	
	/**
	 * Getters and Setters 
	 */
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPvalue() {
		return pvalue;
	}
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	} 

}
