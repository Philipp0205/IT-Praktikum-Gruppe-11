package de.hdm.group11.jabics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.server.LoginInfo;
import de.hdm.group11.jabics.server.db.UserMapper;
import de.hdm.group11.jabics.shared.LoginService;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.bo.User;

public class Jabics implements EntryPoint {
	/**
	 *  Folgende Nachricht wird angezeigt, wenn der Server nicht erreichbar ist: 
	 */
	private static final String SERVER_ERROR = "Der Server ist nicht erreichbar.";
	
	// Objekte die später für den Login gebraucht werden
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Bitte melden sie sich mit ihren Google-Account an um Jabics nutzen zu können.");
	private Anchor signInLink = new Anchor("Anmelden.");
	
	/**
	 *  NEU: 
	 */
	UserMapper uMapper = UserMapper.userMapper();
	private User currentUser = null;
	
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	

	/*
	 *
	 */
	
	public void onModuleLoad() {
		MainView mainView = new MainView();
		// Content is die ID des Body-Elements von Stockwatcher 
		RootPanel.get("content").add(mainView);
		
		
		// Login-Status überüfen
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  System.out.println("loginService.loging failed.");
	      }

	      public void onSuccess(LoginInfo result) {
	    	
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	          //loadJabics();
	        } else {        	
	          loadLogin();
	        }
	      }
	    });	
	}
	
	private void loadLogin() {
	    // Assemble login panel.
		this.checkForNewUser();
		
	    signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("stockList").add(loginPanel);
	    
	  }
	
	private void checkForNewUser() { 
		if (this.currentUser == null) {
			
			currentUser.setEmail(this.loginInfo.getEmailAddress());
			currentUser.setUsername(this.loginInfo.getNickname());
      	  	uMapper.insertUser(currentUser);
		} else
			return;
	}
	
	
	
}
