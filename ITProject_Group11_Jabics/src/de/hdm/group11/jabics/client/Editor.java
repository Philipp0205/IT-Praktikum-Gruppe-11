package de.hdm.group11.jabics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.gui.EditorAdmin;
import de.hdm.group11.jabics.client.gui.SignUpForm;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * In der Klasse <code>Editor</code> liegt die onModuleLoad() Methode und mit
 * dieser der Login.
 * 
 * @author Kurrle
 * @author Anders
 */
public class Editor implements EntryPoint {

	private static final String SERVER_ERROR = "Der Server ist nicht erreichbar.";

	/**
	 * <code>LoginInfo</code> einer Instanz dieser Klasse.
	 */
	private LoginInfo logon;

	/**
	 * Aktueller <code>JabicsUser</code> einer Instanz dieser Klasse.
	 */
	private JabicsUser currentUser;

	/**
	 * <code>EditorAdmin</code> einer Instanz dieser Klasse.
	 */
	private EditorAdmin editor;

	/**
	 * <code>SignUpForm</code> einer Instanz dieser Klasse.
	 */
	private SignUpForm signUp;

	/**
	 * <code>LoginServiceAsync</code> einer Instanz dieser Klasse.
	 */
	private LoginServiceAsync loginService;

	/**
	 * <code>VerticalPanel</code> einer Instanz dieser Klasse.
	 */
	private VerticalPanel loginPanel = new VerticalPanel();

	/**
	 * Die Methode, die aufgerufen wird, wenn die Seite neu geladen wird. Tätigt
	 * ausschließlich den Login, durch diesen wird weiter entschieden, was passiert.
	 */
	@Override
	public void onModuleLoad() {
    
		loginService = ClientsideSettings.getLoginService();
		loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());
    
	}

	/**
	 * Den Login laden und anzeigen.
	 * 
	 * @param logon,
	 *            <code>LoginInfo</code> in der die <code>loginUrl</code> liegt.
	 */
	private void loadLogin(LoginInfo logon) {
		// Assemble login panel.
		Label l1 = new Label("Sie sind nicht eingeloggt");
		Label l2 = new Label("Melden sie sich mit ihren Google-Account an, um Jabics zu nutzen.");
		Button b = new Button("Anmelden");
		b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ck) {
				Window.Location.assign(getLoginInfo().getLoginUrl());
			}
		});
		b.setStyleName("loginbutton");

		loginPanel.add(l1);
		loginPanel.add(l2);
		RootPanel.get("details").add(loginPanel);
		RootPanel.get("nav").add(b);
	}

	/**
	 * Setzen der <code>LoginInfo</code>.
	 * 
	 * @param logon
	 */
	public void setLoginInfo(LoginInfo logon) {
		this.logon = logon;
	}

	/**
	 * Auslesen der <code>LoginInfo</code>.
	 * 
	 * @return <code>LoginInfo</code>
	 */
	public LoginInfo getLoginInfo() {
		return this.logon;
	}

	/**
	 * Auslesen des <code>Editor</code>.
	 * 
	 * @return Das <code>Editor</code> Objekt.
	 */
	public Editor getEditor() {
		return this;
	}

	/**
	 * Den aktuellen <code>JabicsUser</code> setzen.
	 * 
	 * @param u
	 */
	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
	}

	/**
	 * Diese Klasse fertigt den ankommenden Callback, wenn der Login beim Laden der
	 * Webseite oder bei KLick auf anmelden erfolgt, ab.
	 */
	private class loginServiceCallback implements AsyncCallback<LoginInfo> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		@Override
		public void onSuccess(LoginInfo logon) {
			if (logon != null) {
				if (logon.getIsLoggedIn() && !logon.isNewUser()) {
					currentUser = logon.getCurrentUser();
					editor = new EditorAdmin(currentUser);
					setLoginInfo(logon);
					editor.setLoginInfo(logon);
					editor.setJabicsUser(logon.getCurrentUser());
					// Den Editor laden
					editor.loadEditor();
				} else if (logon.getIsLoggedIn() && logon.isNewUser()) {
					signUp = new SignUpForm(logon, getEditor());
				} else {
					setLoginInfo(logon);
					loadLogin(logon);
				}
			}
		}
	}
}
