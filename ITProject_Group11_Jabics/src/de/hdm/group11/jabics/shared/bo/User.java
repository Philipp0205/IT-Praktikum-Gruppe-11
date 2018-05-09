package de.hdm.group11.jabics.shared.bo;

/** Representation of a User in the Software. 
 * 
 *  @author Kurrle 
 */

public class User {
	
	private int id; 
	private String username;
	private String email;  
	
	
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

}
