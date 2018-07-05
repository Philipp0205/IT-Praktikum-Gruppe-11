package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Editor;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginServiceAsync;

/**
 * Die Signup Form stellt die Eigabemaske für das Erstellen eines neuen Kontos
 * bei Jabics bereit, in der der Vor- und Nachname eingegeben werden muss, damit
 * das Programm verwendet werden kann.
 * 
 * @author Anders
 */
public class SignUpForm extends VerticalPanel {

	private LoginInfo loginfo;
	private Editor editor;

	private LoginServiceAsync loginService;

	private TextBox surname;
	private TextBox lastname;
	private Label surnameLabel;
	private Label lastnameLabel;

	private Button loginButton;
	private Button exitButton;

	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private HorizontalPanel namePanel = new HorizontalPanel();
	private VerticalPanel surnamePanel = new VerticalPanel();
	private VerticalPanel lastnamePanel = new VerticalPanel();

	/**
	 * Erstellen einer neuen SignUpForm.
	 * 
	 * @param LoginInfo
	 *            in dem der Nutzer und die Information über den Stand des Logins
	 *            liegt
	 * @param Editor
	 *            auf den zurückgegriffen wird, wenn der Login abgeschlossen ist.
	 */
	public SignUpForm(LoginInfo logon, Editor e) {
		this.loginfo = logon;

		this.editor = e;

		exitButton = new Button("Abbruch");
		loginButton = new Button("Konto erstellen");
		surnameLabel = new Label("Vorname eingeben:");
		lastnameLabel = new Label("Nachname eingeben:");
		surname = new TextBox();
		lastname = new TextBox();
		loginButton.setStyleName("createUButton1");
		exitButton.setStyleName("createUButton2");
		
		surnameLabel.setStyleName("loginflabel");
		lastnameLabel.setStyleName("loginflabel");
		surname.setStyleName("surnameBox");
		lastname.setStyleName("surnameBox");
		
		exitButton.addClickHandler(new ExitClickHandler());
		loginButton.addClickHandler(new LoginClickHandler());

		surnamePanel.add(surnameLabel);
		surnamePanel.add(surname);
		lastnamePanel.add(lastnameLabel);
		lastnamePanel.add(lastname);
		lastnamePanel.setStyleName("lnPanel");
		namePanel.add(surnamePanel);
		namePanel.add(lastnamePanel);
		buttonPanel.add(exitButton);
		buttonPanel.add(loginButton);

		this.add(namePanel);
		this.add(buttonPanel);

		RootPanel.get("details").add(this);
	}

	public void onLoad() {
	
		if (loginService == null) {
			loginService = ClientsideSettings.getLoginService();
		}
		GWT.log("HI");

	}

	/**
	 * Überprüft, ob beide Textfelder gesetzt sind und wenn ja Nutzernamen ermitteln
	 * und speichern
	 * 
	 * @return Boolean (false = kein Nutzername gesetzt, true = Nutzername gesetzt)
	 */
	public boolean setUsername() {
		if ((surname.getText() == "") || (lastname.getText() == "")) {
			Window.alert("Bitte Vor- und Nachnamen eigeben");
			return false;
		} else {
			String sName = surname.getText();
			String lName = lastname.getText();

			String nickname = sName + " " + lName;
			loginfo.getCurrentUser().setUsername(nickname);
			return true;
		}
	}

	/**
	 * Gibt den <code>Editor</code> zuruück
	 * 
	 * @return Editor
	 */
	public Editor getEditor() {
		return this.editor;
	}

	/**
	 * Diese Klasse stellt den LoginHandler zur Verfügung, der aufgerufen wird, wenn
	 * auf "Konto erstellen" geklickt wird.
	 */
	private class LoginClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (setUsername()) {
				String s = GWT.getHostPageBaseURL();
				loginService.createUser(loginfo, s, new CreateUserCallback());
			}
		}
	}

	/**
	 * Diese Klasse stellt den ClickHandler zur Verfügung, der aufgerufen wird, wenn
	 * auf "Abbruch" geklickt wird.
	 */
	private class ExitClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			Window.Location.assign(loginfo.getLogoutUrl());
		}
	}

	/**
	 * Der CreateUserCallback wird bei Rückkehr des RPC zum Erstellen eines neuen
	 * Nutzers aufgerufen. Er "öffnet" den Editor bei erfolgreichem neuem Nutzer.
	 *
	 */
	private class CreateUserCallback implements AsyncCallback<LoginInfo> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Nutzer erstellen fehlgeschlagen. Bitte probieren Sie es später erneut.");
		}

		@Override
		public void onSuccess(LoginInfo logon) {
			if (logon != null) {
				if (logon.getIsLoggedIn() && (logon.isNewUser() == false)) {
					Window.alert("Login success");
					EditorAdmin editorAdmin = new EditorAdmin(logon.getCurrentUser());
					editorAdmin.setLoginInfo(logon);
					editorAdmin.setJabicsUser(logon.getCurrentUser());
					// Den Editor laden
					editorAdmin.loadEditor();
				} else {
					getEditor().onModuleLoad();
				}
			}
		}
	}

}
