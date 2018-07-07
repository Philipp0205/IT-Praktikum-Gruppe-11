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
import de.hdm.group11.jabics.client.gui.ReportAdmin;
import de.hdm.group11.jabics.client.gui.SignUpForm;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * @author Kurrle
 * @author Anders
 */
public class Report implements EntryPoint {

	/**
	 * Aktueller <code>JabicsUser</code> einer Instanz dieser Klasse.
	 */
	JabicsUser currentUser;

	/**
	 * <code>LoginInfo</code> einer Instanz dieser Klasse.
	 */
	LoginInfo loginfo;

	/**
	 * <code>ReportAdmin</code> einer Instanz dieser Klasse.
	 */
	ReportAdmin report;

	/**
	 * <code>LoginServiceAsync</code> einer Instanz dieser Klasse.
	 */
	LoginServiceAsync loginService = null;

	/**
	 * <code>VerticalPanel</code> einer Instanz dieser Klasse.
	 */
	private VerticalPanel loginPanel = new VerticalPanel();

	/**
	 * Die onModuleLoad welche beom ersten aufrufen der Klasse aufgerufen wird.
	 */
	@Override
	public void onModuleLoad() {
		if (loginService == null) {
			loginService = ClientsideSettings.getLoginService();
		}
		login();
	}

	/**
	 * Der Login welcher nötig ist bevor der Reprt geladen wird. 
	 * Ein Nutzer muss sich zuerst einloggen und wird anschließend zum Report weitgergeleitet.
	 */
	public void login() {
		loginService = ClientsideSettings.getLoginService();
		loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());
	}

	/**
	 * Auslesen der <code>LoginInfo</code>.
	 * 
	 * @return <code>LoginInfo</code>
	 */
	public LoginInfo getLoginInfo() {
		return this.loginfo;
	}

	/**
	 * Den Login laden und einen Button anbieten, der den Login zur Verfügung stellt
	 * 
	 * @param logon
	 */
	private void loadLogin(LoginInfo logon) {
		// Assemble login panel.
		Label l1 = new Label("Sie sind nicht eingeloggt");
		Label l2 = new Label("Melden sie sich mit ihren Google-Account an, um Jabics zu nutzen.");

		Button b = new Button("Anmelden");
		b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ck) {
				//Window.Location.assign(logon.getLoginUrl());
			}
		});
		b.setStyleName("loginbutton");
		
		loginPanel = new VerticalPanel();
		loginPanel.add(l1);
		loginPanel.add(l2);
		loginPanel.add(b);

		RootPanel.get("nav").add(b);
		RootPanel.get("content").add(loginPanel);
	}

	/**
	 *  Callback welcher beim ausführen eines Logins ausgelöst wird. 
	 *  Bei einem erfolgreichen Callback wird die <code>LoginInfo</code> gesetzt und der Report wird geladen.
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
					report = new ReportAdmin();
					report.setLoginInfo(logon);
					report.setJabicsUser(logon.getCurrentUser());
					report.loadReport();
				} else {
					Window.alert("Sie sind nicht angemeldet oder haben noch kein Konto bei Jabics");
					loadLogin(logon);
				}
			}
		}
	}
}
