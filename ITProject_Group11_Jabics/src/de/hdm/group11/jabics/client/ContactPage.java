package de.hdm.group11.jabics.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ContactPage extends Composite {
	
	private VerticalPanel vPanel = new VerticalPanel();
	
	public ContactPage() {
		initWidget(this.vPanel);
		private String btnStyle = "standardButton";
		
		final Button createContactButton = new Button("Kontakte erstellen");
		createContactButton.setStylePrimaryName(btnStyle);
		this.vPanel.add(createContactButton);
		
		final Button editContactButton = new Button("Kontakt bearbeiten");
		editContactButton.setStylePrimaryName(btnStyle);
		this.vPanel.add(editContactButton);
		
		final Button showContactButton = new Button("Kontakt anzeigen");
		showContactButton.setStylePrimaryName(btnStyle);
		this.vPanel.add(showContactButton);
		
		final Button deleteContactButton = new Button("Kontakt löschen");
		deleteContactButton.setStylePrimaryName(btnStyle);
		
	}	
}
