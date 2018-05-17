package de.hdm.group11.jabics.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MenuView extends Composite {

	private HorizontalPanel hPanel = new HorizontalPanel();
	
	public MenuView() {
		initWidget(this.hPanel);
		
		Button contactBtn = new Button("Kontakt");
		contactBtn.addClickHandler(new ContactButtonHandler());
		this.hPanel.add(contactBtn);
		
		Button listBtn = new Button("Liste");
		listBtn.addClickHandler(new ListButtonHandler());
		this.hPanel.add(listBtn);
	}
	
	private class ContactButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class ListButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
	}
}
