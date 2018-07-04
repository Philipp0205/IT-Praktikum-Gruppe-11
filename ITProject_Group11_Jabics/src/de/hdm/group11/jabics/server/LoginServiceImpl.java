package de.hdm.group11.jabics.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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
	@Override
	public LoginInfo login(String requestUri) {

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
					System.out.println("6########" + existingJabicsUser.getId() + existingJabicsUser.getEmail());
					loginInfo.setLoggedIn(true);
					loginInfo.setIsNewUser(false);
					System.out.println("7##################################");
					loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
				} else {
					System.out.println("neuer Nutzer");
					JabicsUser newJabicsUser = new JabicsUser();
					newJabicsUser.setEmail(user.getEmail());
					// Temporär den Nutzernamen aus Google setzen, damit der Nutzer wenigstens
					// irgendetwas hat
					try {
						newJabicsUser.setUsername(user.getNickname());
					} catch (Exception e) {
						System.err.println("Username not found" + e.toString());
						newJabicsUser.setUsername("not found");
					}
					loginInfo.setLoggedIn(true);
					loginInfo.setIsNewUser(true);
					// Für den Fall, dass etwas nicht tut LoginURL neu setzen
					String s = userService.createLoginURL(requestUri);
					loginInfo.setLoginUrl(s);
				}
				return loginInfo;

			} catch (Exception e) {
				System.out.println("Nutzer nicht gefunden: Exception");
				System.out.println(e.toString());
				String s = userService.createLoginURL(requestUri);
				loginInfo.setLoginUrl(s);
				loginInfo.setLoggedIn(false);
				loginInfo.setIsNewUser(true);
				return loginInfo;
			}
		} else {
			System.err.println("Nutzer konnte nicht ermittelt werden");
			loginInfo.setLoggedIn(false);
			loginInfo.setIsNewUser(true);
			String s = userService.createLoginURL(requestUri);
			loginInfo.setLoginUrl(s);
			return loginInfo;
		}
	}

	public LoginInfo createUser(LoginInfo logon, String requestUri) {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		try {
			System.out.println("Nutzer erstellen");
			// Sicherheitshalber neues LoginInfo Objekt erstellen
			LoginInfo newLogon = new LoginInfo();
			//Neuen Nutzer setzen mit dem gesetzten Nutzernamen
			JabicsUser newUser = logon.getCurrentUser();
			
			//Ist es auch wirklich der gleiche Nutzer
			if (newUser.getEmail() == user.getEmail()) {
				newUser.setEmail(logon.getCurrentUser().getEmail());
				// Nutzer in DB einfügen und mit Id zurückbekommen
				newUser = uMapper.insertUser(newUser);
				newLogon.setLoggedIn(true);
				newLogon.setIsNewUser(false);
				newLogon.setLogoutUrl(userService.createLogoutURL(requestUri));
				newLogon.setCurrentUser(newUser);
			} else {
				System.out.println("Nicht die gleiche Mail");
				String s = userService.createLoginURL(requestUri);
				newLogon.setLoginUrl(s);
				newLogon.setLoggedIn(false);
				newLogon.setIsNewUser(true);
			}

			return newLogon;
		} catch (Exception e) {
			System.out.println("Beim Erstellen des neuen Nutzers ist etwas schief gelaufen");
			System.err.println(e.toString());
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setLoggedIn(false);
			loginInfo.setIsNewUser(true);
			String s = userService.createLoginURL(requestUri);
			loginInfo.setLoginUrl(s);
			return loginInfo;
		}

	}

}
