package de.hdm.group11.jabics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
 * In der Klasse "Editor" werden die einzelnen Teile der Gui zusammengeführt und
 * die Darstellung der einzelnen Klassen initiiert. Editor kann wie eine
 * Verwalterklasse gesehen werden, die zu jedem Zeitpunkt über das Geschehen im
 * Editor Bescheid weiß. Folgende Klassen werden verwaltet: ShowContactForm,
 * EditContactForm, ContactCollaborationForm, ExisitingContactColaborationForm,
 * ContactListForm, ContactListCollaborationForm, SearchForm, TreeViewMenu mit
 * seinen "Subtabs".
 */
public class Editor implements EntryPoint {
	
	private static final String SERVER_ERROR = "Der Server ist nicht erreichbar.";
	
	private LoginInfo logon;
	private JabicsUser currentUser;
	
	private EditorAdmin editor;
	private SignUpForm signUp;

	private LoginServiceAsync loginService;
	
	private VerticalPanel loginPanel = new VerticalPanel();
	@Override
	public void onModuleLoad() {
		/*
		 * Login
		 */
		JabicsUser u = new JabicsUser(1);
		u.setEmail("test@mail.com");
		u.setUsername("ein nutzer");
		u.setId(1);
		
		editor = new EditorAdmin(u);
//		editor.setLoginInfo(logon);
//		editor.setJabicsUser(logon.getCurrentUser());
		editor.loadEditor();		
//		GWT.log("Load");
//		loginService = ClientsideSettings.getLoginService();
//		GWT.log(GWT.getHostPageBaseURL());
//		loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());

	}
	
	private void loadLogin(LoginInfo logon) {
		// Assemble login panel.
		Label l1 = new Label("Sie sind nicht eingeloggt");
		Label l2 = new Label("Melden sie sich mit ihren Google-Account an, um Jabics zu nutzen.");
		Label tempurl = new Label(logon.getLoginUrl());
		Button b = new Button("Anmelden");
		b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ck) {
				Window.Location.assign(getLoginInfo().getLoginUrl());
			}
		});
		b.setStyleName("logbutton");
		
		loginPanel.add(l1);
		loginPanel.add(l2);
		loginPanel.add(tempurl);
		RootPanel.get("details").add(loginPanel);
		RootPanel.get("nav").add(b);
	}

	/**
	 * Getter und Setter
	 */
	public void setLoginInfo(LoginInfo logon) {
		this.logon = logon;
	}
	
	public LoginInfo getLoginInfo() {
		return this.logon;
	}
	
	public Editor getEditor() {
		return this;
	}

	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
	}

	private class loginServiceCallback implements AsyncCallback<LoginInfo> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}
		@Override
		public void onSuccess(LoginInfo logon) {
			if (logon != null) {
				if (logon.getIsLoggedIn()) {
					Window.alert("Login success");
					currentUser = logon.getCurrentUser();
					editor = new EditorAdmin(currentUser);
					setLoginInfo(logon);
					editor.setLoginInfo(logon);
					editor.setJabicsUser(logon.getCurrentUser());
					// Den Editor laden
					editor.loadEditor();
				} else if(logon.getIsLoggedIn() && logon.isNewUser()){
					signUp = new SignUpForm(logon, getEditor());
					signUp.onLoad();
				} else {
					Window.alert("User not logged in");
					setLoginInfo(logon);
					loadLogin(logon);
				}
			}
		}
	}
}
