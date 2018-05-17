package de.hdm.group11.jabics.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainView extends Composite {

	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel contentPanel;
	
	public MainView() {
		initWidget(this.vPanel);
		
		MenuView menu = new MenuView();
		this.vPanel.add(menu);
		
		this.contentPanel = new VerticalPanel();
		this.vPanel.add(contentPanel);
		
	}
}
