package de.hdm.group11.jabics.client.gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.ReportGeneratorServiceAsync;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.HTMLReportWriter;

public class Report implements EntryPoint {
	
	ReportGeneratorServiceAsync reportGenerator = null;
	Button allReportsButton = new Button("Alle Kontakte aller User");
	Button searchButton = new Button("Suchen");
	
	HorizontalPanel  navPanel = new HorizontalPanel();
	VerticalPanel  verPanel1 = new VerticalPanel();
	VerticalPanel  verPanel2 = new VerticalPanel();
	VerticalPanel  verPanel3 = new VerticalPanel();
	VerticalPanel  verPanel4 = new VerticalPanel();
	
	TextBox stringBox = new TextBox();
	TextBox intBox = new TextBox();
	TextBox floatBox = new TextBox();
	
	Label stringl = new Label("Text:");
	Label intl = new Label("Ganzzahl:");
	Label floatl = new Label("Dezimalzahl:");
	Button db = new Button("Datum:");
	DatePicker dp = new DatePicker();
	
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
		
		verPanel1.add(stringl);
		verPanel1.add(stringBox);
		verPanel2.add(intl);
		verPanel2.add(intBox);
		verPanel3.add(floatl);
		verPanel3.add(floatBox);
		verPanel4.add(db);
//		String sDate1 =("31/12/1998");
//		Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		dp.setValue(null);
		verPanel4.add(dp);
	//	verPanel4.add(dateBox);
		dp.setVisible(false);
		
		
		navPanel.add(verPanel1);
		navPanel.add(verPanel2);
		navPanel.add(verPanel3);
		navPanel.add(verPanel4);
		navPanel.add(searchButton);
		
		// Aufbauen des RootPanels
		RootPanel.get("navigator").add(navPanel);
		
		//Verhalten der Buttons
		allReportsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reportGenerator.createAllContactsInSystemReport(new CreateAllContactsInSystemReportCallback() );
			}
		});
		
		db.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dp.setVisible(true);
			}
		});
		
		
		/**
		 * Der DatePicker brauch keinen Valuechangehandler, da der Wert nicht mehr in eine Textbox gespeichert wird, 
		 * sondern direkt nach dem Wert gesucht wird.
		 */
//		dp.addValueChangeHandler(new ValueChangeHandler<Date>() {
//
//	        @Override
//	        public void onValueChange(ValueChangeEvent<Date> event) {
//	        	if (dp != null) {
//					//pval.setDateValue(event.getValue());
//					dateBox.setText(event.getValue().toString());
//				}
//	        }
//	        });
		
		
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
					if (stringBox.getText() != null ) {
						pvalue.setIntValue(Integer.parseInt(stringBox.getText()));
						
					} else 
						System.out.println("Eingegebener Wert ist nicht im korrekten Format (int).");
					
				} else if (floatBox.getText().isEmpty() == false) {
					if (floatBox.getText() != null ) {
						pvalue.setFloatValue(Float.parseFloat(floatBox.getText()));
					}
				} else if (dp.getValue() != null) {
					pvalue.setDateValue(dp.getValue());
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
