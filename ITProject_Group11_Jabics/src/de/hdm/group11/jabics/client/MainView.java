package de.hdm.group11.jabics.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class MainView extends Composite {

	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel contentPanel;
	
	/**
	 *  Das alles hier könnte auch direkt in die Jabics Klasse. Aber momtean ist es so auch ok. 
	 */
	
	public MainView() {
		
		/**
		 *  Erst einmal funktionelle Platzhalter! 
		 */
		
		// Div element aus der jabics html wird dem Panel verknüpft.
		RootPanel.get("Navigator").add(contentPanel);
		
		/**
		 *  Aufbau des Navigators
		 */
		
		final Button reportGeneratorButton = new Button("Report Generator");
		final Button editorButton = new Button("Editor");
		
		// CSS-Name
		reportGeneratorButton.setStylePrimaryName("jabics-menubutton");
		editorButton.setStylePrimaryName("jabics-menubutton");
		contentPanel.add(reportGeneratorButton);
		contentPanel.add(editorButton);
		
		/**
		 * Verhalten der Buttons
		 */
		
		reportGeneratorButton.addClickHandler(new ClickHandler() {
		      @Override
			public void onClick(ClickEvent event) {
		      
		    
		      }
		});
		
		
		
		
//		initWidget(this.vPanel);
//		vPanel.getElement().setId("mainTabel");
//		this.vPanel.setBorderWidth(1);
//		
//		
//		MenuView menu = new MenuView(this);
//		this.vPanel.add(menu);
//		
//		this.contentPanel = new VerticalPanel();
//		this.vPanel.add(contentPanel);
//		
//		Label textLbl = new Label("Herzlich Willkommen bei JABICS");
//		this.contentPanel.add(textLbl);
		
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
