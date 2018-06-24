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

		System.err.println("LoginStart++++++++++++++++++++++++++++++++++");
		LoginInfo res = new LoginInfo();
		JabicsUser u = new JabicsUser(1);
		res.setCurrentUser(u);
		res.setLoggedIn(true);
		System.err.println("LogiReturns++++++++++++++++++++++++++++++++++");
		return res;
		
		/*
		// Google User.
		UserService userService = UserServiceFactory.getUserService();
		System.out.println("2##################################");
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		//return new LoginInfo();
		*/
		/**
		 * Logik aus http://www.gwtproject.org/doc/latest/tutorial/appengine.html
		 */
		/*
		try {
			// JabicsUser existingJabicsUser = UserMapper.userMapper().findUserByEmail(user.getEmail());

			// Temporary! Delete when DB is deployed
			JabicsUser existingJabicsUser = new JabicsUser(1);
			
			if (existingJabicsUser != null) {
				System.out.println("3##################################");
				loginInfo.setCurrentUser(existingJabicsUser);
				System.out.println("4##################################");
				loginInfo.setLoggedIn(true);
				System.out.println("5##################################");
				//loginInfo.setEmailAddress(user.getEmail());
				//loginInfo.setNickname(user.getNickname());
				//loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			} else {
				System.out.println("6##################################");
				loginInfo.setLoggedIn(false);
				//loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
			}
			System.out.println("7##################################");
			return loginInfo;
			
		} catch (Exception e) {
			loginInfo.setLoggedIn(false);
			return loginInfo;
		}
		*/
	}
}
