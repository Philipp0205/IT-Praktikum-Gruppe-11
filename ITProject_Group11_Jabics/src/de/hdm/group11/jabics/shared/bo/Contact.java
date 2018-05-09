/**
 * 
 */
package de.hdm.group11.jabics.shared.bo;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Anders
 *
 */

public class Contact extends BusinessObject {

	//Code will be added soon
	
	ArrayList<PValue> values = new ArrayList<PValue>();
	private String name; 
	private BoStatus shareStatus;
	
	@Override
	public String toString() {		
		return name; 
	}
	
	/**
	 *  Adds value to the values Array 
	 */
	public void addPValue(PValue p) { 
		
		this.values.add(p);	
	}
	/**
	 *  Removes value from the value Array
	 */
	public void removePValue(PValue p) { 
		
		this.values.remove(p);
	}
	
	/**
	 *  Getter and Setter
	 */
	
	public ArrayList<PValue> getValues() {
		return values;
	}
	public void setValues(ArrayList<PValue> values) {
		this.values = values;
		setDateUpdated(new Date());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		setDateUpdated(new Date());
	}
	public BoStatus getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}
	
}
