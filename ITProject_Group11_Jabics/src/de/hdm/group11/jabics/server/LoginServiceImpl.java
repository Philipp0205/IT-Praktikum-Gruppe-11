package de.hdm.group11.jabics.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.group11.jabics.server.db.UserMapper;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginService;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Die Klasse <code>LoginServiceImpl</code> implementiert das Interface
 * <code>LoginService</code> und stellt die Applikationslogik für den Login
 * bereit.
 * 
 * @author Kurrle
 * @author Anders
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = 1L;

	/**
	 * Instanz der Klasse <code>UserMapper</code>.
	 */
	UserMapper uMapper = UserMapper.userMapper();

	/**
	 * Logik des Loginprozesses für dem System bekannten <code>JabicsUser</code>.
	 * 
	 * @param requestUri
	 *            der <code>String</code> mit der URL der Anfrage.
	 * 
	 * @return das <code>LoginInfo</code> Objekt in dem die Information über den
	 *         Erfolg des Logins gespeichert ist
	 */
	@Override
	public LoginInfo login(String requestUri) {
		System.out.println("login");
		// Google User bestimmen.
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		System.out.println("login GoogleUser");
		LoginInfo loginInfo = new LoginInfo();

		/**
		 * Logik zu teilen aus
		 * http://www.gwtproject.org/doc/latest/tutorial/appengine.html
		 */
		if (user != null) {

			try {
				JabicsUser existingJabicsUser = UserMapper.userMapper().findUserByEmail(user.getEmail());

				if (existingJabicsUser != null) {
					System.out.println("Nutzer gefunden");
					loginInfo.setCurrentUser(existingJabicsUser);
					System.out.println("6########" + existingJabicsUser.getId() + existingJabicsUser.getEmail());
					loginInfo.setLoggedIn(true);
					loginInfo.setIsNewUser(false);
					loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
				} else {
					// Nutzer existiert noch nicht im System, neuen erstellen und zurückgeben
					System.out.println("neuer Nutzer");
					JabicsUser newJabicsUser = new JabicsUser();
					newJabicsUser.setEmail(user.getEmail());
					// Temporär den Nutzernamen aus Google setzen, damit der Nutzer wenigstens
					// irgendetwas hat
					try {
						newJabicsUser.setUsername(user.getNickname());
						System.out.println(newJabicsUser.getEmail());
						System.out.println("nick:" + newJabicsUser.getUsername());
					} catch (Exception e) {
						System.err.println("Username not found" + e.toString());
						newJabicsUser.setUsername("not found");
					}
					loginInfo.setLoggedIn(true);
					loginInfo.setIsNewUser(true);
					loginInfo.setCurrentUser(newJabicsUser);
					// Für den Fall, dass etwas nicht tut oder abgebrochen wird LoginURL neu setzen
					String s = userService.createLogoutURL(requestUri);
					loginInfo.setLogoutUrl(s);
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

	/**
	 * Wenn der Nutzername eingegeben wurde wird diese Methode aufgerufen, die den
	 * neuen <code>JabicsUser</code> in die Datenbank inseriert. Darf nur
	 * ausfgerufen werden, wenn im <code>LoginInfo</code> Objekt der Nutzer mit Name
	 * existiert
	 * 
	 * @param logon
	 *            Instanz der Klasse <code>LoginInfo</code>.
	 * @param requestUri
	 *            der <code>String</code> mit der URL der Anfrage.
	 * @return <code>LoginInfo</code> (Status: Logged in) mit dem neuen Nutzer und
	 *         dessen Id
	 */
	public LoginInfo createUser(LoginInfo logon, String requestUri) {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		try {
			System.out.println("Nutzer erstellen");
			// Sicherheitshalber neues LoginInfo Objekt erstellen
			LoginInfo newLogon = new LoginInfo();
			// Neuen Nutzer setzen mit dem gesetzten Nutzernamen
			JabicsUser newUser = logon.getCurrentUser();

			// Ist es auch wirklich der gleiche Nutzer
			System.out.println(logon.getCurrentUser().getEmail() + ":" + user.getEmail());
			if (newUser.getEmail().contentEquals(user.getEmail())) {
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
			System.out.println("Beim Erstellen des neuen Nutzers ist ein Fehler aufgetreten");
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
