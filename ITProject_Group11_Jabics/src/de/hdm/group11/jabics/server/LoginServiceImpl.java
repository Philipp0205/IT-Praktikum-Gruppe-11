package de.hdm.group11.jabics.server;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.group11.jabics.shared.LoginService;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public LoginInfo login(String requestUri) {
		
	 	UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LoginInfo loginInfo = new LoginInfo();
	    
	    loginInfo.setCurrentUser(new JabicsUser(user.getNickname(), user.getEmail()));

	    if (user != null) {
	    	
	      loginInfo.setLoggedIn(true);
	      loginInfo.setEmailAddress(user.getEmail());
	      loginInfo.setNickname(user.getNickname());
	      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
	    } else {
	      loginInfo.setLoggedIn(false);
	      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
	    }
	    return loginInfo;
	}
}
