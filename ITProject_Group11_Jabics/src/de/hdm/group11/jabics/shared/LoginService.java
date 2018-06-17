package de.hdm.group11.jabics.shared;

import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.group11.jabics.server.LoginInfo;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public interface LoginService extends RemoteService {
	public JabicsUser login(String requestUri);
}
