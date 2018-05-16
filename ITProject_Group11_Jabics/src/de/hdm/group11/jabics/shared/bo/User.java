package de.hdm.group11.jabics.shared.bo;

/** Representation of a User in the Software. Many Attributes are directly taken from google Account API.
 * 
 *  @author Kurrle and Anders
 */

public class User {
	
	private int id; 
	private String username;
	private String email;  
	
	
	

	public String toString() {
		return this.username;
	}
	
	/*
	 *  Getters and setters
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
