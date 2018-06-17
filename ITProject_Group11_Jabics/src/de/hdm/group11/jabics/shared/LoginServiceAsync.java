package de.hdm.group11.jabics.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.server.LoginInfo;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public interface LoginServiceAsync {
	
	public void login(String requestUri, AsyncCallback<JabicsUser> async);

}
