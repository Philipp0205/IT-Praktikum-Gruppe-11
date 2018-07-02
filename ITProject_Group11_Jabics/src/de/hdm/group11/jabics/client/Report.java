package de.hdm.group11.jabics.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
	private Label loginLabel = new Label("Melden sie sich mit ihren Google-Account an, um Jabics zu nutzen.");
	private Anchor signInLink = new Anchor("Anmelden.");

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
