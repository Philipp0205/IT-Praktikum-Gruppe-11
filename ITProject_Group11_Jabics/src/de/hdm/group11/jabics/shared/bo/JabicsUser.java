package de.hdm.group11.jabics.shared.bo;

/** 
 * Ein Nutzer ist in Jabocs ein Bediener der Software, der sich über Google eingeloggt hat.
 * Viele Attribute werden direkt aus der Google Accounts API übernommen.
 * 
 *  @author Kurrle and Anders
 */

public class JabicsUser {
	
	private int id; 
	private String email;
	private String username;
	
	public JabicsUser() {
		super();	
	}
	public JabicsUser(String email) {
		this();
		this.email = email;
	}
	public JabicsUser(String email, String user) {
		this(email);
		this.username = user;
	}
	public String toString() {
		return this.email;
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
		return this.username;
	}
	public void setUsername(String user) {
		this.username = user;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
