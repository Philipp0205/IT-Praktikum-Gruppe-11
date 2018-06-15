package de.hdm.group11.jabics.client.gui;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.shared.ReportGeneratorServiceAsync;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.HTMLReportWriter;
import de.hdm.group11.jabics.shared.report.ReportWriter;

public class Report implements EntryPoint {
	
	ReportGeneratorServiceAsync reportGenerator = null;
	Button allReportsButton = new Button("Alle Kontakte aller User");
	Button searchButton = new Button("Suchen!");
	
	HorizontalPanel  navPanel = new HorizontalPanel();
	
	TextBox stringBox = new TextBox();
	TextBox intBox = new TextBox();
	TextBox floatBox = new TextBox();
	TextBox dateBox = new TextBox();
	
	

	@Override
	public void onModuleLoad() {
		
		if (reportGenerator == null) {
			reportGenerator = ClientsideSettings.getReportGeneratorService();
		}
		
		/**
		 * Das GUI soll folgendermaßen aussehen: 
		 * Oben gibt es eine Navigation mit 4 Feldern für ints, strings, floars und Dates
		 * zusätzlich gibt es einen "Suchen" Button zum starten der Suche.
		 * Sollte keines der Felder ausgefüllt worden sein werden alle Kontakte des Users ausgegeben.
		 * Des Weiteren gibt es einen AllContactsOfSystem Button
		 * 
		 * Unterhalb der Navigation wird der Report dargestellt.
		 */
		
		// Aufbauen des NavPanels
		navPanel.add(intBox);
		navPanel.add(floatBox);
		navPanel.add(stringBox);
		navPanel.add(dateBox);
		
		// Aufbauen des RootPanels
		RootPanel.get("navigator").add(navPanel);
		
		//Verhalten der Buttons
		allReportsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reportGenerator.createAllContactsInSystemReport(new CreateAllContactsInSystemReportCallback() );
				
			}
			
		});
		
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PValue pvalue = new PValue();
				JabicsUser u = null;
				
				
				if (stringBox.getText().isEmpty() == false) {
					pvalue.setStringValue(stringBox.getText());
					//TODO hier currentUser einfügen
					reportGenerator.createFilteredContactsOfUserReport(pvalue, u, new CreateFilteredContactsOfUserReportCallback() );
				} else if (intBox.getText().isEmpty() == false) {
					if (StringUtils.isNumericSpace(stringBox.getText()) == true ) {
						pvalue.setIntValue(Integer.parseInt(stringBox.getText()));
						
					} else 
						System.out.println("Eingegebener Wert ist nicht im korrekten Format (int).");
					
				} else if (floatBox.getText().isEmpty() == false) {
					if (StringUtils.isNumericSpace(floatBox.getText()) == true ) {
						pvalue.setFloatValue(Float.parseFloat(floatBox.getText()));
					}
				} else if (dateBox.getText().isEmpty() == false) {
					pvalue.setDateValue(LocalDateTime.parse(dateBox.getText()));
				}
				else System.out.println("Es konnte keine Suche durchgeführt werden.");
		
			}
			
		});		
		
	}
	
	private class CreateAllContactsInSystemReportCallback implements AsyncCallback<AllContactsInSystemReport> {

		@Override
		public void onFailure(Throwable caught) {
			// Fehler werden gelogt.
			ClientsideSettings.getLogger().severe("Erzeugen des Reports fehlgeschlagen.");
			
		}

		@Override
		public void onSuccess(AllContactsInSystemReport report) {
			if (report != null) { 
				
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("content").clear();
				RootPanel.get("content").add(new HTML(writer.getReportText()));
			}
			
		}	
		
	}
	private class CreateFilteredContactsOfUserReportCallback implements AsyncCallback<FilteredContactsOfUserReport> {

		@Override
		public void onFailure(Throwable caught) {
			ClientsideSettings.getLogger().severe("Erzeugen des Reports fehlgeschlagen.");
			
		}

		@Override
		public void onSuccess(FilteredContactsOfUserReport report) {
			if (report != null) { 
				
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("content").clear();
				RootPanel.get("content").add(new HTML(writer.getReportText()));
			}
		}
		
	}
	
	private class CreateAllContactsOfUserReportCallback implements AsyncCallback<FilteredContactsOfUserReport> {

		@Override
		public void onFailure(Throwable caught) {
			ClientsideSettings.getLogger().severe("Erzeugen des Reports fehlgeschlagen.");
			
		}

		@Override
		public void onSuccess(FilteredContactsOfUserReport report) {
			if (report != null) { 
				
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("content").clear();
				RootPanel.get("content").add(new HTML(writer.getReportText()));
			}
		}
	}
}
