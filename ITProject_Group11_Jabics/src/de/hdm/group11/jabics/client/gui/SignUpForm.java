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

public class SignUpForm extends VerticalPanel{

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
	private VerticalPanel surnamePanel= new VerticalPanel();
	private VerticalPanel lastnamePanel = new VerticalPanel();
	
	public SignUpForm(LoginInfo logon, Editor e){
		this.loginfo = logon;
		
		this.editor = e;
		
		exitButton = new Button("Abbruch");
		exitButton.addClickHandler(new ExitClickHandler());
		loginButton = new Button("Konto erstellen");
		loginButton.addClickHandler(new LoginClickHandler());
		
		surnameLabel = new Label("Vorname eingeben");
		lastnameLabel = new Label("Nachname eingeben");
		surname = new TextBox();
		lastname = new TextBox();
		
		surnamePanel.add(surnameLabel);
		surnamePanel.add(surname);
		lastnamePanel.add(lastnameLabel);
		lastnamePanel.add(lastname);
		namePanel.add(surnamePanel);
		namePanel.add(lastnamePanel);
		buttonPanel.add(exitButton);
		buttonPanel.add(loginButton);
		
		this.add(namePanel);
		this.add(buttonPanel);
		
		RootPanel.get("details").add(this);
	}
	
	public void onLoad() {
		if(loginService == null) {
			loginService = ClientsideSettings.getLoginService();
		}
		GWT.log("HI");
		
	}
	
	public void setUsername() {
		String sName = surname.getText();
		String lName = lastname.getText();
		
		String nickname = sName + " " + lName;
		loginfo.getCurrentUser().setUsername(nickname);
	}
	
	public Editor getEditor() {
		return this.editor;
	}
	
	private class LoginClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			setUsername();
			String s = GWT.getHostPageBaseURL();
			loginService.createUser(loginfo, s, new CreateUserCallback());
		}
	}
	
	private class ExitClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			Window.Location.assign(loginfo.getLogoutUrl());
		}
	}
	
	private class CreateUserCallback implements AsyncCallback<LoginInfo>{

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
