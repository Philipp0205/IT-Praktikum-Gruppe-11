package de.hdm.group11.jabics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.gui.Editor;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.ReportGeneratorServiceAsync;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class Jabics implements EntryPoint {
	/**
	 *  Folgende Nachricht wird angezeigt, wenn der Server nicht erreichbar ist: 
	 */
	private static final String SERVER_ERROR = "Der Server ist nicht erreichbar.";
	
	// Objekte die sp�ter f�r den Login gebraucht werden
	
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Melden sie sich mit ihren Google-Account an um Jabics nutzen zu k�nnen.");
	private Anchor signInLink = new Anchor("Anmelden.");
	
	EditorServiceAsync eService;
	LoginServiceAsync loginSevice;
	ReportGeneratorServiceAsync reportGenerator;
	
	LoginInfo logon;
	
	JabicsUser currentUser;
	
	public void setLoginInfo(LoginInfo logon) {
		this.logon = logon;
	}
	
	public void onModuleLoad() {
		
		/*
		 * Zunächst wird eine Editor-Instanz hinzugefügt.
		 */
	    loginSevice = ClientsideSettings.getLoginService();
		eService = ClientsideSettings.getEditorService();
		reportGenerator = ClientsideSettings.getReportGeneratorService();
		loadLogin();
		loginSevice.login(GWT.getHostPageBaseURL(), new loginServiceCallback());

	}
	
	private class loginServiceCallback implements AsyncCallback<LoginInfo> {
		
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(LoginInfo logon) {
			currentUser = logon.getCurrentUser(); 
			setLoginInfo(logon);
			
			if(currentUser.getIsLoggedIn()) {
				eService.setJabicsUser(logon.getCurrentUser(), new SetJabicsUserCallback() );
				loadJabics();
			}
			else {
				Window.alert("User not logged in");
			}
		}	
	}
	
	private class SetJabicsUserCallback implements AsyncCallback<JabicsUser> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(JabicsUser u) {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
		} 
	}
	
	

	private void loadJabics() {
		Editor e = new Editor();
		e.setLoginInfo(logon);
		e.onModuleLoad();
	}
	
	private void loadLogin() {
	    // Assemble login panel.
		this.checkForNewUser();

	    signInLink.setHref(logon.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("content").add(loginPanel);
	    
	  }
	
	private void checkForNewUser() { 
		if (this.currentUser == null) {
			JabicsUser.getJabicsUser();
		} else
			return;
	}
	
	
	
}
