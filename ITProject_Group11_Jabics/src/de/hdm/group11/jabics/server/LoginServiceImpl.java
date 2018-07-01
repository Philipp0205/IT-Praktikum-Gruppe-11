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

	UserMapper uMapper = UserMapper.userMapper();

	/*
	 * Hier finder die komplette Logik des Loginprozesses statt.
	 */
	public LoginInfo login(String requestUri) {

		// JabicsUser u = new JabicsUser(1);
		//
		// u.setEmail("stahl.alexander@live.de");
		// u.setId(1);
		// u.setUsername("Alexander Stahl");
		//
		// System.err.println("LoginStart++++++++++++++++++++++++++++++++++");
		// LoginInfo res = new LoginInfo();
		//
		// res.setCurrentUser(u);
		// res.setLoggedIn(true);
		// System.err.println("LogiReturns++++++++++++++++++++++++++++++++++");
		// return res;

		System.out.println("1##################################");
		// Google User.
		UserService userService = UserServiceFactory.getUserService();
		System.out.println("2##################################");
		User user = userService.getCurrentUser();
		System.out.println("3################################## "); // + user.getEmail());
		LoginInfo loginInfo = new LoginInfo();
		System.out.println("4##################################");
		// return new LoginInfo();

		/**
		 * Logik aus http://www.gwtproject.org/doc/latest/tutorial/appengine.html
		 */
		if (user != null) {

			try {

				JabicsUser existingJabicsUser = UserMapper.userMapper().findUserByEmail(user.getEmail());

				if (existingJabicsUser != null) {
					System.out.println("Nutzer gefunden");
					System.out.println("5##################################");
					loginInfo.setCurrentUser(existingJabicsUser);
					System.out.println("6##################################");
					loginInfo.setLoggedIn(true);
					System.out.println("7##################################");
					// loginInfo.setEmailAddress(user.getEmail());
					// loginInfo.setNickname(user.getNickname());
					// loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
				} else {
					System.out.println("Nutzer nicht gefunden");
					JabicsUser newUser = new JabicsUser();
					newUser.setEmail(user.getEmail());
					newUser.setUsername("Neuer Testnutzer");
					System.out.println("new user1##################################");
					loginInfo.setLoggedIn(true);
					loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));

					newUser = uMapper.insertUser(newUser);
					loginInfo.setCurrentUser(newUser);
					System.out.println("newuser2############# mit id: " + newUser.getId());
				}
				System.out.println("9##################################");
				return loginInfo;

			} catch (Exception e) {
				System.err.println("Login failed. Catch!");
				System.out.println(e.toString());
				loginInfo.setLoggedIn(false);
				return loginInfo;
			}
		} else {
			System.err.println("Nutzer konnte nicht ermittelt werden");
			loginInfo.setLoggedIn(false);
			String s = userService.createLoginURL(requestUri);
			System.err.println(s);
			loginInfo.setLoginUrl(s);
			return loginInfo;

		}
	}
}