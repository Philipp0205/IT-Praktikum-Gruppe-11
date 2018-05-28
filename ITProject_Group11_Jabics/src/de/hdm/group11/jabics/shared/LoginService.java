package de.hdm.group11.jabics.shared;

import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.group11.jabics.server.LoginInfo;

public interface LoginService extends RemoteService {
	public LoginInfo login(String requestUri);
}
