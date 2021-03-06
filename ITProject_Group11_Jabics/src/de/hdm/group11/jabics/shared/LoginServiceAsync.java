package de.hdm.group11.jabics.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface LoginServiceAsync {

	public void login(String requestUri, AsyncCallback<LoginInfo> async);

	public void createUser(LoginInfo log, String requestUri, AsyncCallback<LoginInfo> async);

}