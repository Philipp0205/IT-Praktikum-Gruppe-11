package de.hdm.group11.jabics.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ContactPage extends Composite {
	
	private VerticalPanel vPanel = new VerticalPanel();
	
	public ContactPage() {
		initWidget(this.vPanel);
		
		final Button findAllContacts = new Button("Alle Kontakte finden");
		findAllContacts.setStylePrimaryName("standardButton");
		
		this.vPanel.add(findAllContacts);
		
		final Button showContact = new Button("ausgewählten Kontakt anzeigen");
		showContact.setStylePrimaryName("standardButton");
		this.vPanel.add(showContact);
		
	}	
}
