/**
 * 
 */
package de.hdm.group11.jabics.shared.bo;

import java.util.ArrayList;

/**
 * @author Anders
 *
 */

public class Contact extends BusinessObject {

	//Code will be added soon
	
	ArrayList<PValue> values = new ArrayList<PValue>();
	private String name; 
	private BoStatus shareStatus;
	
	public String toString() {
		return name; 
		/**
		 *  TODO
		 */
	}
	
	public void addPValue(PValue p ) { 
		/**
		 * TODO
		 */
	}
	public void removePValue(PValue p) { 
		/**
		 * TODO
		 */
	}
	
	/**
	 *  Getter and Setter
	 */
	
	public ArrayList<PValue> getValues() {
		return values;
	}
	public void setValues(ArrayList<PValue> values) {
		this.values = values;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BoStatus getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}
	
}
