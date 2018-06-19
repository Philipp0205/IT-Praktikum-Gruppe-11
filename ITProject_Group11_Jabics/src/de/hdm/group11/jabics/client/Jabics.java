package de.hdm.group11.jabics.client;

import com.google.apphosting.api.ApiProxy.LogRecord.Level;
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
import de.hdm.group11.jabics.shared.LoginService;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.ReportGeneratorServiceAsync;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class Jabics implements EntryPoint {
	/**
	 *  Folgende Nachricht wird angezeigt, wenn der Server nicht erreichbar ist: 
	 */
	private static final String SERVER_ERROR = "Der Server ist nicht erreichbar.";
	
	// Objekte die später für den Login gebraucht werden
	
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Melden sie sich mit ihren Google-Account an um Jabics nutzen zu k�nnen.");
	private Anchor signInLink = new Anchor("Anmelden.");
	
	EditorServiceAsync eService;
	LoginServiceAsync loginService;
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
	    loginService = ClientsideSettings.getLoginService();
	    String s = GWT.getHostPageBaseURL();
	    GWT.log("1");
		//loginService.login(s, new loginServiceCallback());
		
		loadJabics();
		/*
		eService = ClientsideSettings.getEditorService();
		reportGenerator = ClientsideSettings.getReportGeneratorService();
		*/
	}
	
	private class loginServiceCallback implements AsyncCallback<LoginInfo> {
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fatal! Login failed");
		}
		@Override
		public void onSuccess(LoginInfo logon) {
			GWT.log("2");
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
		GWT.log("4");
		Editor e = new Editor();
		//e.setLoginInfo(logon);
		e.onModuleLoad();
	}
	
	private void loadLogin() {
	    // Assemble login panel.
		this.checkForNewUser();
		Window.alert("3.1");
	    signInLink.setHref(logon.getLoginUrl());
	    Window.alert("3.2");
	    loginPanel.add(loginLabel);
	    Window.alert("3.3");
	    loginPanel.add(signInLink);
	    Window.alert("3.4");
	    RootPanel.get("content").add(loginPanel);
	    
	  }
	
	private void checkForNewUser() { 
		if (this.currentUser == null) {
			JabicsUser.getJabicsUser();
		} else
			return;
	}
	
	
	
}
