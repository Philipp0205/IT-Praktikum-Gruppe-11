package de.hdm.group11.jabics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Jabics implements EntryPoint {
	
	public void onModuleLoad() {
		MainView mainView = new MainView();
		RootPanel.get("content").add(mainView);
	}
	
}
