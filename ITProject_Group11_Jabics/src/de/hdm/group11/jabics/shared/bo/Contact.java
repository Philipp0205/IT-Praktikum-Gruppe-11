/**
 * 
 */
package de.hdm.group11.jabics.shared.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Realizes the Object Oriented Version of Contact Lists in Jabics. A Contact Object can store multiple PValue Objects.
 * PValues can be added and removed. Contacts have a status wich are realized with an enum. 
 * 
 * @author Anders
 * @author Kurrle 
 */

public class Contact extends BusinessObject {
	
	public Contact(ArrayList<PValue> a, String name) { 
		this.dateCreated = LocalDateTime.now();
	}
	
	public Contact(ArrayList<PValue> a) { 
		
		StringBuffer sBuffer = new StringBuffer("bName");
			for (PValue p : a) {
				if (p.getProperty().getLabel() == "name") {
					sBuffer.append(p.getStringValue());					
				} else {
					System.out.println("No name in Array.");
				}
			for (PValue p2: a) {
				if (p2.getProperty().getLabel() == "lastname") {
					sBuffer.append(p.getStringValue());				
			} else {
				System.out.println("No lastname in Array");
			}
			
		}
	}
	}

	//Code will be added soon
	
	ArrayList<PValue> values = new ArrayList<PValue>();
	private String name; 
	private BoStatus shareStatus;
	
	@Override
	public String toString() {		
		return this.name;
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
		this.dateUpdated = LocalDateTime.now();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.dateUpdated = LocalDateTime.now();
	}
	public BoStatus getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(BoStatus shareStatus) {
		this.shareStatus = shareStatus;
	}
	
}
