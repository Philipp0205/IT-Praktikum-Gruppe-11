package de.hdm.group11.jabics.shared;

import java.io.Serializable;

import com.google.gwt.user.client.Window;

import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Objekte dieser Klasse werden die Login-Info des UserService enthalten. Hier
 * ist gespeichert, ob der Nutzer, der zurückgegeben wird, eingelogg ist.
 * LoginInfo implementiert Serializable, da ein eine Rückgabe einer RPC-Methode
 * ist.
 * 
 * Struktur und Name der Klasse übernommen aus
 * http://www.gwtproject.org/doc/latest/tutorial/appengine.html#user. Zugriff am
 * 20.06.18.
 * 
 * @author Kurrle
 */
public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Die URL für den Login einer Instanz dieser Klasse.
	 */
	private String loginUrl;

	/**
	 * Die URL für den Logout einer Instanz dieser Klasse.
	 */
	private String logoutUrl;

	/**
	 * Login-Status einer Instanz dieser Klasse.
	 */
	private boolean isLoggedIn;

	/**
	 * User-Status einer Instanz dieser Klasse.
	 */
	private boolean isNewUser;

	/**
	 * <code>JabicsUser</code> einer Instanz dieser Klasse.
	 */
	private JabicsUser currentUser;

	/**
	 * Default Konstruktor.
	 */
	public LoginInfo() {
	}

	/**
	 * Auslesen des aktuellen Users.
	 * 
	 * @return
	 */
	public JabicsUser getCurrentUser() {
		return currentUser;
	}

	/**
	 * Auslesen der E-Mail Adresse.
	 * 
	 * @return String
	 */
	public String getEmailAddress() {
		return this.currentUser.getEmail();
	}

	/**
	 * Auslesen des Login-Status.
	 * 
	 * @return isLoggedIn
	 */
	public boolean getIsLoggedIn() {
		return this.isLoggedIn;
	}

	/**
	 * Auslesen der Login URL.
	 * 
	 * @return String
	 */
	public String getLoginUrl() {
		Window.alert("4");
		return this.loginUrl;
	}

	/**
	 * Auslesen der Logout URL.
	 * 
	 * @return String
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	/**
	 * Auslesen des Benutzernamens.
	 * 
	 * @return String
	 */
	public String getNickname() {
		return this.currentUser.getUsername();
	}

	/**
	 * Auslesen des Userstatus.
	 * 
	 * @return boolean
	 */
	public boolean isNewUser() {
		return isNewUser;
	}

	/**
	 * Setzen des aktuellen Users.
	 * 
	 * @param currentUser
	 */
	public void setCurrentUser(JabicsUser currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Setzen der E-Mail Adresse.
	 * 
	 * @param emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.currentUser.setEmail(emailAddress);
	}

	/**
	 * Setzen des Userstatus.
	 * 
	 * @param isNewUser
	 *            der boolesche Wert beschreibt ob ein User neu ist oder dem System
	 *            bekannt.
	 */
	public void setIsNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}

	/**
	 * Setzen des Login-Status.
	 * 
	 * @param isLoggedIn
	 */
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	/**
	 * Setzen der URL für den Login.
	 * 
	 * @param loginUrl
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * Setzen der URL für den Logout.
	 * 
	 * @param logoutUrl
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	/**
	 * Setzen des Benutzernamens.
	 * 
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		this.currentUser.setUsername(nickname);
	}

}