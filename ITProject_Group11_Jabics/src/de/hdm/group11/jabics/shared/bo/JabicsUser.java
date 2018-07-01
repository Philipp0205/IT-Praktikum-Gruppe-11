package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;

/**
 * Ein Nutzer ist in Jabics ein Bediener der Software, der sich über Google
 * eingeloggt hat. Viele Attribute werden direkt aus der Google Accounts API
 * übernommen.
 * 
 * @author Kurrle
 * @author Anders
 * @author Stahl
 */

public class JabicsUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID einer Instanz dieser Klasse.
	 */
	private int id;

	/**
	 * Mail Adresse einer Instanz dieser Klasse.
	 */
	private String email;

	/**
	 * Benutzername einer Instanz dieser Klasse.
	 */
	private String username;



	/**
	 * Leerer Konstruktor
	 */
	public JabicsUser() {

	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit einer ID zu erzeugen.
	 * 
	 * @param id
	 */
	public JabicsUser(int id) {
		this.id = id;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Mail Adresse zu erzeugen.
	 * 
	 * @param email
	 */
	public JabicsUser(String email) {
		this();
		this.email = email;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Mail Adresse und Benutzername
	 * zu erzeugen.
	 * 
	 * @param email
	 * @param user
	 */
	public JabicsUser(String email, String username) {
		this(email);
		this.username = username;
	}

	/**
	 * Textuelle repräsentation des <code>JabicsUser</code> in der Form der Mail
	 * Adresse.
	 * 
	 * @return email
	 */
	@Override
	public String toString() {
		return this.email;
	}



	/**
	 * Auslesen der ID.
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Setzen der ID.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Auslesen des Usernames.
	 * 
	 * @return username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Setzen des Usernames.
	 * 
	 * @param user
	 */
	public void setUsername(String user) {
		this.username = user;
	}

	/**
	 * Setzen der Mail Adresse.
	 * 
	 * @return email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Auslesen der Mail Adresse.
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}