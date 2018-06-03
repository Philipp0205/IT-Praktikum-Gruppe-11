package de.hdm.group11.jabics.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MenuView extends Composite {

	private HorizontalPanel hPanel = new HorizontalPanel();
	private MainView main;
	
	public MenuView(MainView main) {
		initWidget(this.hPanel);
		hPanel.getElement().setId("hMenuPanel");
		this.main = main;
		
		Anchor contactBtn = new Anchor("Kontakte");
		contactBtn.addClickHandler(new ContactButtonHandler());
		contactBtn.getElement().setId("menuButton");
		this.hPanel.add(contactBtn);
		
		Anchor listBtn = new Anchor("Listen");
		listBtn.addClickHandler(new ListButtonHandler());
		listBtn.getElement().setId("menuButton");
		this.hPanel.add(listBtn);
		
		Anchor searchBtn = new Anchor("Suche");
		searchBtn.addClickHandler(new ContactButtonHandler());
		searchBtn.getElement().setId("menuButton");
		this.hPanel.add(searchBtn);
		
	}
	
	private class ContactButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			main.openPage1();
		}
	}
	
	private class ListButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			main.openPage2();
		}
	}
}
