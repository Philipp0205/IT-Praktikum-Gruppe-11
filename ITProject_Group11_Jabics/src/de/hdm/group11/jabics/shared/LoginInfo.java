package de.hdm.group11.jabics.shared;

import java.io.Serializable;

import com.google.gwt.user.client.Window;

import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 *  Objekte dieser Klasse werden die Login-Info vom User service enthalten. 
 *  LoginInfo implementiert serializable, da ein eine Rückgabe einer RPC-Methode ist. 
 * @author P
 *
 */

public class LoginInfo implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private String loginUrl;
	private String logoutUrl;
	  
	private JabicsUser currentUser;
	
	public LoginInfo() {}
	  
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
