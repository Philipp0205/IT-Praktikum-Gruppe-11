package de.hdm.group11.jabics.shared.bo;

/** 
 * Ein Nutzer ist in Jabocs ein Bediener der Software, der sich über Google eingeloggt hat.
 * Viele Attribute werden direkt aus der Google Accounts API übernommen.
 *  @author Kurrle and Anders
 */

public class JabicsUser {
	
	private int id; 
	private String username;
	private String email;  
	
	public JabicsUser() {
		super();	
	}
	public JabicsUser(String name) {
		this();
		this.username= name;
	}
	public JabicsUser(String name, String email) {
		this(name);
		this.email = email;
	}

	public String toString() {
		return this.username;
	}
	
	/*
	 *  Getter und Setter
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	} 
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
