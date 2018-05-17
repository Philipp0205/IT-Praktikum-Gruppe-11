package de.hdm.group11.jabics.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListPage extends Composite {
	
	private VerticalPanel vPanel = new VerticalPanel();
	public ListPage() {
		initWidget(this.vPanel);
		
		Button btnList = new Button("zuKontakt");
		
		this.vPanel.add(btnList);
	}
}
