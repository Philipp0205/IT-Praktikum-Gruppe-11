package de.hdm.group11.jabics.server;

import java.io.Serializable;

import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 *  Objekte dieser Klasse werden die Login-Info vom User service enthalten. 
 *  LoginInfo implementiert serializable, da ein eine R�ckgabe einer RPC-Methode ist. 
 * @author P
 *
 */

public class LoginInfo implements Serializable  {
	
	/*
	 * Singelton
	 */
	private static LoginInfo loginInfo = null;
	
	public static LoginInfo getloginInfo() {
		if (loginInfo == null) {
			loginInfo = new LoginInfo();
		}
		return loginInfo;
	}
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean loggedIn = false;
	  private String loginUrl;
	  private String logoutUrl;
	  private String emailAddress;
	  private String nickname;
	  
	  private JabicsUser currentUser;
	  
	  
	  public JabicsUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(JabicsUser currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isLoggedIn() {
	    return loggedIn;
	  }

	  public void setLoggedIn(boolean loggedIn) {
	    this.loggedIn = loggedIn;
	  }

	  public String getLoginUrl() {
	    return loginUrl;
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
	    return emailAddress;
	  }

	  public void setEmailAddress(String emailAddress) {
	    this.emailAddress = emailAddress;
	  }

	  public String getNickname() {
	    return nickname;
	  }

	  public void setNickname(String nickname) {
	    this.nickname = nickname;
	  }

}
