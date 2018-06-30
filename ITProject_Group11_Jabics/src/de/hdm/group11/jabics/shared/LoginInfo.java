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

	public boolean isLoggedIn() {
		return currentUser.getIsLoggedIn();
	}

	public void setLoggedIn(boolean loggedIn) {
		this.currentUser.setLoggedIn(loggedIn);
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
