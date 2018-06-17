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
	
	private boolean isLoggedIn;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;
	
	// Singelton
	private static JabicsUser jabicsUser = null;
	
	public static JabicsUser getJabicsUser() {
		if (jabicsUser == null) {
			jabicsUser = new JabicsUser();
		}
		return jabicsUser;
	}
	
	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
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
