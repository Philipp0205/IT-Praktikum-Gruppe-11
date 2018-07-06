package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Ein simpler Report, welcher die Superklasse Report erweitert und einen
 * einzelnen Kontakt mit dessen Eigenschaften repräsentiert.
 * 
 * @author Anders
 * @author Kurrle
 * @author Stahl
 */
public class ContactReport extends Report implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Liste aus <code>PropertyView</code> einer Instanz dieser Klasse.
	 */
	private ArrayList<PropertyView> content = new ArrayList<PropertyView>();

	/**
	 * Kontaktinfo einer Instanz dieser Klasse.
	 */
	private Paragraph contactInfo;

	/**
	 * Erstellungsinfo einer Instanz einer Klasse.
	 */
	private Paragraph creationInfo;

	/**
	 * Update Info einer Instanz einer Klasse.
	 */
	private Paragraph updateInfo;

	/**
	 * Nutzer Info einer Instanz dieser Klasse.
	 */
	private Paragraph userInfo;

	/**
	 * Kollaboration Info einer Instanz dieser Klasse.
	 */
	private Paragraph collaborationInfo;

	/**
	 * Default Konstruktor
	 */
	public ContactReport() {
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit einer Liste aus
	 * <code>PropertyView</code>, der <code>contactInfo</code>, der
	 * <code>userInfo</code>, der <code>creationInfo</code>, der
	 * <code>updateInfo</code> und der <code>collaborationInfo</code> zu erstellen.
	 * 
	 * @param pv
	 *            Liste aus <code>PropertyView</code>
	 * @param contactInfo
	 * @param userInfo
	 * @param creationInfo
	 * @param updateInfo
	 * @param collaborationInfo
	 */
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
	 * Konstruktor um eine Instanz dieser Klasse mit einer Liste aus
	 * <code>PropertyView</code>, der <code>contactInfo</code> und der
	 * <code>userInfo</code> zu erstellen.
	 * 
	 * @param pv
	 *            Liste aus <code>PropertyView</code>
	 * @param contactInfo
	 * @param userInfo
	 */
	public ContactReport(ArrayList<PropertyView> pv, Paragraph contactInfo, Paragraph userInfo) {
		this(pv);
		this.userInfo = userInfo;
		this.contactInfo = contactInfo;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit der <code>contactInfo</code>
	 * und der <code>userInfo</code> zu erstellen.
	 * 
	 * @param contactInfo
	 * @param userInfo
	 */
	public ContactReport(Paragraph contactInfo, Paragraph userInfo) {
		this.userInfo = userInfo;
		this.contactInfo = contactInfo;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit einer Liste aus
	 * <code>PropertyView</code> zu erstellen.
	 * 
	 * @param pv
	 *            Liste aus <code>PropertyView</code>.
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

	/**
	 * Eine <code>PropertsView</code> dem <code>Report</code> hinzufügen.
	 * 
	 * @param pv
	 *            <code>PropertsView</code>, die hinzugefügt werden soll
	 */
	public void addProperty(PropertyView pv) {
		this.content.add(pv);
	}

	/**
	 * Textuelle Repräsentation des <code>ContactReport</code> Objekts durch die
	 * <code>contactInfo</code>.
	 * 
	 * @return String im Format: "Report for Contact: " + Anzeigename des Kontakts.
	 */
	public String toString() {
		return "Report for Contact: " + this.contactInfo;
	}

	/**
	 * Auslesen des <code>content</code>.
	 * 
	 * @return Liste aus <code>PropertyView</code>.
	 */
	public ArrayList<PropertyView> getContent() {
		return this.content;
	}

	/**
	 * Setzen des <code>content</code>.
	 * 
	 * @param content
	 *            Liste aus <code>PropertyView</code>.
	 */
	public void setContent(ArrayList<PropertyView> content) {
		this.content = content;
	}

	/**
	 * Auslesen der <code>contactInfo</code>.
	 * 
	 * @return Paragraph
	 */
	public Paragraph getContactInfo() {
		return this.contactInfo;
	}

	/**
	 * Setzen der <code>contactInfo</code>.
	 * 
	 * @param contactInfo
	 */
	public void setContactInfo(Paragraph contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * Auslesen der <code>collaborationInfo</code>.
	 * 
	 * @return Paragraph
	 */
	public Paragraph getCollaboratorInfo() {
		return this.collaborationInfo;
	}

	/**
	 * Setzen der <code>collaborationInfo</code>.
	 * 
	 * @return collaborationInfo
	 */
	public void setCollaboratorInfo(Paragraph collaborationInfo) {
		this.collaborationInfo = collaborationInfo;
	}

	/**
	 * Auslesen der <code>creationInfo</code>.
	 * 
	 * @return Paragraph
	 */
	public Paragraph getCreationInfo() {
		return creationInfo;
	}

	/**
	 * Setzen der <code>creationInfo</code>.
	 * 
	 * @param creationInfo
	 */
	public void setCreationInfo(Paragraph creationInfo) {
		this.creationInfo = creationInfo;
	}

	/**
	 * Auslesen der <code>updateInfo</code>.
	 * 
	 * @return Paragraph
	 */
	public Paragraph getUpdateInfo() {
		return updateInfo;
	}

	/**
	 * Setzen der <code>updateInfo</code>.
	 * 
	 * @param updateInfo
	 */
	public void setUpdateInfo(Paragraph updateInfo) {
		this.updateInfo = updateInfo;
	}

	/**
	 * Auslesen der <code>userInfo</code>.
	 * 
	 * @return Paragraph
	 */
	public Paragraph getUserInfo() {
		return this.userInfo;
	}

	/**
	 * Setzen der <code>userInfo</code>.
	 * 
	 * @param userInfo
	 */
	public void setUserInfo(Paragraph userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Auslesen der Liste der <code>PropertyView</code>.
	 * 
	 * @return content
	 */
	public ArrayList<PropertyView> getPropertyViews() {
		return content;
	}
}