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
 * http://www.gwtproject.org/doc/latest/tutorial/appengine.html#user.
 * Zugriff am 20.06.18.
 * 
 * @author Kurrle
 */
public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String loginUrl;
	private String logoutUrl;
	
	/**
	 * Login-Status einer Instanz dieser Klasse.
	 */
	private boolean isLoggedIn;
	
	private boolean isNewUser;

	private JabicsUser currentUser;

	public LoginInfo() {
	}

	/*
	 *  Getter und Setter
	 */
	
	public JabicsUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(JabicsUser currentUser) {
		this.currentUser = currentUser;
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
	 * Setzen des Login-Status.
	 * 
	 * @param isLoggedIn
	 */
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public boolean isNewUser() {
		return isNewUser;
	}

	public void setIsNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}


	public String getLoginUrl() {
		Window.alert("4");
		return this.loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
		return this.currentUser.getEmail();
	}

	public void setEmailAddress(String emailAddress) {
		this.currentUser.setEmail(emailAddress);
	}

	public String getNickname() {
		return this.currentUser.getUsername();
	}

	public void setNickname(String nickname) {
		this.currentUser.setUsername(nickname);
	}

}
