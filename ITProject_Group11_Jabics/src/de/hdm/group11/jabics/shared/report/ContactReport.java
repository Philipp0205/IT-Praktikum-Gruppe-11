package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

/**
 * Ein simpler Report, welcher die Superklasse Report erweitert.
 * @author Anders and Kurrle
 *
 */

public class ContactReport extends Report  {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<PropertyView> content = new ArrayList<PropertyView>();
	private String contactInfo;
	
	/**
	 * simple constructor that needs at least one Property view in an ArrayList
	 */
	public ContactReport(ArrayList<PropertyView> pv, String contactInfo) {
		this.content = pv;
		this.contactInfo = contactInfo;
	}
	
	/**
	 * constructor that needs at least one Property view in an ArrayList and sets contactInfo based on Information in that ArrayList.
	 * Only the last name is set as contactInfo, surnames are not searched for. Only use this Constructor if contactName cannot be determined.
	 */
	public ContactReport(ArrayList<PropertyView> pv) {
		this.content = pv;
		try {
			for(PropertyView p : pv) {
				if(p.getPname() == "name") this.contactInfo = p.getPvalue();
			}
		} catch (Exception e){
			if (this.contactInfo == null) {
				this.contactInfo = "noContactNameSpecified";
				System.out.println("A Contact name in a Report could not be determined."
						+ "This should not have happend, as all Contacts must have at least a name.");
			}
		}
	}
	
	public void addLine(PropertyView pv) { 
		this.content.add(pv);
	}
	
	@Override
	public String toString() {
		return "Report for Contact: " + this.contactInfo;
	}
	
	/**
	 * Getters and setters
	 */
	
	public void setContent(ArrayList<PropertyView> content) {
		this.content = content;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String value) {
		this.contactInfo = value;
	}

	public ArrayList<PropertyView> getPropertyViews() {
		return content;
	}
	
	

}