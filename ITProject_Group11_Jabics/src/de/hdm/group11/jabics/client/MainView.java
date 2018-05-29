package de.hdm.group11.jabics.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainView extends Composite {

	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel contentPanel;
	
	public MainView() {
		initWidget(this.vPanel);
		vPanel.getElement().setId("mainTabel");
		this.vPanel.setBorderWidth(1);
		
		MenuView menu = new MenuView(this);
		this.vPanel.add(menu);
		
		this.contentPanel = new VerticalPanel();
		this.vPanel.add(contentPanel);
		
		Label textLbl = new Label("Herzlich Willkommen bei JABICS");
		this.contentPanel.add(textLbl);
		
	}
	
	public void openPage1() {
		this.contentPanel.clear();
		ContactPage page1 = new ContactPage();
		this.contentPanel.add(page1);
	}
	
	public void openPage2() {
		this.contentPanel.clear();
		ListPage page2 = new ListPage();
		this.contentPanel.add(page2);
	}
}
