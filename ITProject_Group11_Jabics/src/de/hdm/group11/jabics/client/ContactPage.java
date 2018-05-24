package de.hdm.group11.jabics.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactPage extends Composite {
	private VerticalPanel vPanel = new VerticalPanel();
	
	public ContactPage() {
		initWidget(this.vPanel);
		
		Image contactPage = new Image("/images/Unbenannt-1.png");
		contactPage.setWidth("600px");
		
		this.vPanel.add(contactPage);
	}
}
