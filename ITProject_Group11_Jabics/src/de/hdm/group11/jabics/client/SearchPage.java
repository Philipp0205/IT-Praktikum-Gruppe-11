package de.hdm.group11.jabics.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class SearchPage extends Composite {
	
	private VerticalPanel vPanel = new VerticalPanel();
	public SearchPage() {
		initWidget(this.vPanel);
		
		TextBox searchBox = new TextBox();
		Button btnSearch = new Button("suchen");
		
		this.vPanel.add(searchBox);
		this.vPanel.add(btnSearch);
	}
}
