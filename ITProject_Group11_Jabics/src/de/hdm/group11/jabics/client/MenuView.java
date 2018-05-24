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
		hPanel.getElement().setId("Tabellenstruktur");
		this.main = main;
		
		Anchor contactBtn = new Anchor("Kontakt");
		contactBtn.addClickHandler(new ContactButtonHandler());
		contactBtn.getElement().setId("menuButton");
		this.hPanel.add(contactBtn);
		
		Anchor listBtn = new Anchor("Liste");
		listBtn.addClickHandler(new ListButtonHandler());
		listBtn.getElement().setId("menuButton");
		this.hPanel.add(listBtn);
		
		Anchor sharedContactBtn = new Anchor("geteilte Kontakt");
		sharedContactBtn.addClickHandler(new ContactButtonHandler());
		sharedContactBtn.getElement().setId("menuButton");
		this.hPanel.add(sharedContactBtn);
		
		Anchor sharedListBtn = new Anchor("geteilte Liste");
		sharedListBtn.addClickHandler(new ListButtonHandler());
		sharedListBtn.getElement().setId("menuButton");
		this.hPanel.add(sharedListBtn);
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
