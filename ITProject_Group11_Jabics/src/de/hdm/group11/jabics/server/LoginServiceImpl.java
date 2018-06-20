package de.hdm.group11.jabics.server;

import java.io.PrintStream;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.group11.jabics.server.db.UserMapper;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginService;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = 1L;

	/*
	 * Hier finder die komplette Logik des Loginprozesses statt.
	 */
	@Override
	public LoginInfo login(String requestUri) {

		System.err.println("++++++++++++++++++++++++++++++++++");
		System.out.println("##################################");
		// Google User.
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		
		/**
		 * Logik aus http://www.gwtproject.org/doc/latest/tutorial/appengine.html
		 */
		try {
			// JabicsUser existingJabicsUser =
			// UserMapper.userMapper().findUserByEmail(user.getEmail());

			// Temporary! Delete when DB is deployed
			JabicsUser existingJabicsUser = new JabicsUser("Testuser");

			if (existingJabicsUser != null) {
				loginInfo.setLoggedIn(true);
				loginInfo.setEmailAddress(user.getEmail());
				loginInfo.setNickname(user.getNickname());
				loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
				loginInfo.setCurrentUser(existingJabicsUser);
			} else {
				loginInfo.setLoggedIn(false);
				loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
			}

			return loginInfo;
			
		} catch (Exception e) {
			loginInfo.setLoggedIn(false);
			return loginInfo;
		}
	}
}

/*
 * if (user != null) { JabicsUser existingJabicsUser =
 * UserMapper.userMapper().findUserByEmail(user.getEmail());
 * 
 * if (existingJabicsUser != null) { existingJabicsUser.setLoggedIn(true);
 * existingJabicsUser.setEmailAddress(user.getEmail());
 * existingJabicsUser.setEmailAddress(user.getNickname());
 * existingJabicsUser.setLogoutUrl(userService.createLogoutURL(requestUri));
 * 
 * } else { existingJabicsUser.setLoggedIn(false);
 * existingJabicsUser.setLoginUrl(userService.createLoginURL(requestUri)); }
 */
