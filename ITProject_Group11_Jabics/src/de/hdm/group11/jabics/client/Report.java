package de.hdm.group11.jabics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.gui.ReportAdmin;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class Report implements EntryPoint {
	JabicsUser currentUser;
	LoginInfo loginfo;
	
	ReportAdmin report;
	
	LoginServiceAsync loginService = null;
	
	private VerticalPanel loginPanel = new VerticalPanel();
	

	@Override
	public void onModuleLoad() {
		if (loginService == null) {
			loginService = ClientsideSettings.getLoginService();
		}
		
		/**
		 * Login
		 */
		 loginService = ClientsideSettings.getLoginService();
		 GWT.log(GWT.getHostPageBaseURL());
		 
		 loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());
	}
	public LoginInfo getLoginInfo() {
		return this.loginfo;
	}

	/**
	 * Den Login laden und einen Button anbieten, der den Login zur Verfügung stellt
	 * @param logon
	 */
	private void loadLogin(LoginInfo logon) {
		// Assemble login panel.
		Label l1 = new Label("Sie sind nicht eingeloggt");
		Label l2 = new Label("Melden sie sich mit ihren Google-Account an, um Jabics zu nutzen.");
		Button b = new Button("Anmelden");
		/*b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ck) {
				Window.Location.assign(getLoginInfo().getLoginUrl());
			}
		});*/
		loginPanel = new VerticalPanel();
		loginPanel.add(l1);
		
		loginPanel.add(l2);
		
		loginPanel.add(b);
		
		RootPanel.get("navright").add(b);
		RootPanel.get("content").add(loginPanel);
		
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
					report = new ReportAdmin();
					report.setLoginInfo(logon);
					report.setJabicsUser(logon.getCurrentUser());
					// Den Editor laden
					report.loadReport();
				} else {
					Window.alert("User not logged in");
					loadLogin(logon);
				}
			}

		}
	}

}

/*
 * private class customPropertySuggest implements SuggestOracle{
 * 
 * public void add(Property p) {
 * 
 * }
 * 
 * public void requestSugestions(Request request, Callback callback) {
 * 
 * Collection<Suggestion> suggestions = new ArrayList<Suggestion>;
 * 
 * 
 * Response response = new Response(); response.setSuggestions(suggestions);
 * callback.onSuggestionsReady(request, response);
 * 
 * } }
 */
