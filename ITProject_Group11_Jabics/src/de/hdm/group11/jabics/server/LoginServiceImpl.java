package de.hdm.group11.jabics.server;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.group11.jabics.server.db.UserMapper;
import de.hdm.group11.jabics.shared.LoginService;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService  {

	

	private static final long serialVersionUID = 1L;
	
	/*
	 * Hier finder die komplette Logik des Loginprozesses statt.
	 */
	@Override
	public JabicsUser login(String requestUri) {
		
		//Google User.
	 	UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    JabicsUser jabicsUser = new JabicsUser();
	    
	    // Logik aus http://www.gwtproject.org/doc/latest/tutorial/appengine.html
	    
	    if (user != null) {
	    	JabicsUser existingJabicsUser = UserMapper.userMapper().findUserByGoogleEmail(user.getEmail());
	    	
	    	if (existingJabicsUser != null) {
	    		existingJabicsUser.setLoggedIn(true);
	    		existingJabicsUser.setEmailAddress(user.getEmail());
	    		existingJabicsUser.setEmailAddress(user.getNickname());
	    		existingJabicsUser.setLogoutUrl(userService.createLogoutURL(requestUri));
	    		
	    	} else {
	    		existingJabicsUser.setLoggedIn(false);
	    		existingJabicsUser.setLoginUrl(userService.createLoginURL(requestUri));
	    	}
	    	return existingJabicsUser;
	    }
	}
}
