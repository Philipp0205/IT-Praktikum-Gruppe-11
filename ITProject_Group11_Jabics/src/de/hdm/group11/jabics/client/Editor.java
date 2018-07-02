package de.hdm.group11.jabics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.gui.EditorAdmin;
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

	LoginInfo logon;
	JabicsUser currentUser;

	EditorAdmin editor;

	LoginServiceAsync loginService;
	LoginInfo loginfo;
	
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Melden sie sich mit ihren Google-Account an, um Jabics zu nutzen.");
	private Anchor signInLink = new Anchor("Anmelden.");

	@Override
	public void onModuleLoad() {
		/*
		 * Login
		 */
		//JabicsUser u = new JabicsUser(1);
		//u.setEmail("test@mail.com");
		//u.setUsername("ein nutzer");
		
		GWT.log("Load");
		loginService = ClientsideSettings.getLoginService();
		GWT.log(GWT.getHostPageBaseURL());
		loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());

	}
	
	private void loadLogin(LoginInfo logon) {
		// Assemble login panel.
		Window.alert("3.1");
		signInLink.setHref(logon.getLoginUrl());
		Window.alert("3.2");
		loginPanel.add(loginLabel);
		Window.alert("3.3");
		loginPanel.add(signInLink);
		Window.alert("3.4");
		RootPanel.get("details").add(loginPanel);
	}
	

	/**
	 * Getter und Setter
	 */
	public void setLoginInfo(LoginInfo logon) {
		this.loginfo = logon;
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
					currentUser = logon.getCurrentUser();
					editor = new EditorAdmin(currentUser);
					editor.setLoginInfo(logon);
					editor.setJabicsUser(logon.getCurrentUser());
					// Den Editor laden
					editor.loadEditor();
				} else {
					Window.alert("User not logged in");
					loadLogin(logon);
				}
			}

		}
	}

}
