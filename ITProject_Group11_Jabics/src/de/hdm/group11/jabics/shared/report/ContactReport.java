package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Ein simpler Report, welcher die Superklasse Report erweitert.
 * 
 * @author Anders and Kurrle
 *
 */

public class ContactReport extends Report implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<PropertyView> content = new ArrayList<PropertyView>();
	private Paragraph contactInfo;
	private Paragraph creationInfo;
	private Paragraph updateInfo;
	private Paragraph userInfo;
	private Paragraph collaborationInfo;

	public ContactReport() {
	}

	public ContactReport(ArrayList<PropertyView> pv, Paragraph contactInfo, Paragraph userInfo, Paragraph creationInfo,
			Paragraph updateInfo, Paragraph collaborationInfo) {
		this(pv);
		this.userInfo = userInfo;
		this.contactInfo = contactInfo;
		this.creationInfo = creationInfo;
		this.updateInfo = updateInfo;
		this.collaborationInfo = collaborationInfo;
	}

	/**
	 * constructor
	 */
	public ContactReport(ArrayList<PropertyView> pv, Paragraph contactInfo, Paragraph userInfo) {
		this(pv);
		this.userInfo = userInfo;
		this.contactInfo = contactInfo;
	}

	/**
	 * Konstruktor der nur eine Kontaktinfo und einen Nutzer erhält
	 */
	public ContactReport(Paragraph contactInfo, Paragraph userInfo) {
		this.userInfo = userInfo;
		this.contactInfo = contactInfo;
	}

	/**
	 * constructor that needs at least one Property view in an ArrayList and sets
	 * contactInfo based on Information in that ArrayList. Only the last name is set
	 * as contactInfo, surnames are not searched for. Only use this Constructor if
	 * contactName cannot be determined.
	 */
	public ContactReport(ArrayList<PropertyView> pv) {
		if (!pv.isEmpty()) {
			this.content = pv;
		} else
			System.out.println("Beim Erstellen des ContactReports waren keine PValues vorhanden");

		/*
		 * try { for(PropertyView p : pv) { if(p.getPname() == "firstname")
		 * this.contactInfo = p.getPvalue(); } } catch (Exception e){ if
		 * (this.contactInfo == null) { this.contactInfo = "noContactNameSpecified";
		 * System.out.println("A Contact name in a Report could not be determined." +
		 * "This should not have happend, as all Contacts must have at least a name.");
		 * } }
		 */
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
	public ArrayList<PropertyView> getContent() {
		return this.content;
	}

	public void setContent(ArrayList<PropertyView> content) {
		this.content = content;
	}

	public Paragraph getContactInfo() {
		return this.contactInfo;
	}

	public void setContactInfo(Paragraph value) {
		this.contactInfo = value;
	}

	public Paragraph getCollaboratorInfo() {
		return this.collaborationInfo;
	}

	public void setCollaboratorInfo(Paragraph value) {
		this.collaborationInfo = value;
	}

	public Paragraph getCreationInfo() {
		return creationInfo;
	}

	public void setCreationInfo(Paragraph creationInfo) {
		this.creationInfo = creationInfo;
	}

	public Paragraph getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(Paragraph updateInfo) {
		this.updateInfo = updateInfo;
	}

	public Paragraph getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(Paragraph value) {
		this.userInfo = value;
	}

	public ArrayList<PropertyView> getPropertyViews() {
		return content;
	}

}