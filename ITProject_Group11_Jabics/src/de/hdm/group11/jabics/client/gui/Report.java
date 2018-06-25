package de.hdm.group11.jabics.client.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.ReportGeneratorServiceAsync;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.AllContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.HTMLReportWriter;

public class Report implements EntryPoint {

	JabicsUser currentUser;

	ReportGeneratorServiceAsync reportGenerator = null;
	Button allReportsInSystemButton = new Button("Alle Kontakte aller Nutzer im System");
	Button filteredReportButton = new Button("Alle Kontakte mit diesen Filterkriterien");
	Button allReportButton = new Button("Alle meine Kontakte");

	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel otherReportsPanel = new HorizontalPanel();
	HorizontalPanel navPanel = new HorizontalPanel();
	VerticalPanel verPanel1 = new VerticalPanel();
	VerticalPanel verPanel2 = new VerticalPanel();
//	VerticalPanel verPanel3 = new VerticalPanel();
	VerticalPanel verPanel4 = new VerticalPanel();
	Property[] userpropertys;

	TextBox valueBox = new TextBox();
//	TextBox intBox = new TextBox();
//	TextBox floatBox = new TextBox();
//	TextBox dateBox = new TextBox();
	ListBox datatypemenu = new ListBox();
	
	MultiWordSuggestOracle propertystosuggest;
	
	SuggestBox propertysb;

	Label datatypel = new Label("Eigenschaft:");
	Label valuelabel = new Label("Wert:");
//	Label floatl = new Label("Dezimalzahl:");
//	Label db = new Label("Datum:");
	DatePicker datepicker = new DatePicker();

	@Override
	public void onModuleLoad() {
		
		

		if (reportGenerator == null) {
			reportGenerator = ClientsideSettings.getReportGeneratorService();
		}
		
		/**
		 * Zunächst wird eine User-Instanz hinzugefügt.
		 * Später entfernen und dies den Login übernehmen lassen
		 */
		currentUser = new JabicsUser();
		currentUser.setEmail("stahl.alexander@live.de");
		currentUser.setId(1);
		currentUser.setUsername("Alexander Stahl");
		/**
		 * Login
		 */
		// loginService = ClientsideSettings.getLoginService();
		// GWT.log(GWT.getHostPageBaseURL());
		// loadEditor();
		// loginService.login(GWT.getHostPageBaseURL(), new loginServiceCallback());
		
		
		
		
		
		//Übergangslösung
		loadReport();
		
	}
	
	

	public void loadReport() {
		JabicsUser u = new JabicsUser();
		u.setId(1);
		u.setEmail("stahl.alexander@live.de");
		u.setUsername("Stahlex");
		u.setLoggedIn(true);
		/**
		 * Das GUI soll folgendermaßen aussehen: Oben gibt es eine Navigation mit 4
		 * Feldern für ints, strings, floars und Dates zusätzlich gibt es einen "Suchen"
		 * Button zum starten der Suche. Sollte keines der Felder ausgefüllt worden sein
		 * werden alle Kontakte des Users ausgegeben. Des Weiteren gibt es einen
		 * AllContactsOfSystem Button
		 * 
		 * Unterhalb der Navigation wird der Report dargestellt.
		 */

		// Aufbauen des NavPanels
		
		
		reportGenerator.getPropertysOfJabicsUser(u, new getPropertysOfJabicsUserCallback());

		
		datatypemenu.addItem("Text");
		datatypemenu.addItem("Datum");
		datatypemenu.addItem("Dezimalzahl");
		datatypemenu.addItem("Ganzzahl");
		verPanel1.add(datatypel);

		verPanel2.add(valuelabel);
		verPanel2.add(valueBox);
//		verPanel3.add(floatl);
//		verPanel3.add(floatBox);
//		verPanel4.add(db);
		
		datepicker.setValue(null);
//		verPanel4.add(dateBox);
		verPanel4.add(datepicker);
		datepicker.setVisible(false);

		navPanel.add(verPanel1);
		navPanel.add(verPanel2);
//		navPanel.add(verPanel3);
		navPanel.add(verPanel4);
		navPanel.add(filteredReportButton);
		navPanel.add(allReportButton);
		navPanel.add(allReportsInSystemButton);

		mainPanel.add(navPanel);
		otherReportsPanel.add(allReportsInSystemButton);
		mainPanel.add(otherReportsPanel);

		// Aufbauen des RootPanels
		RootPanel.get("navigator").add(mainPanel);

		// Verhalten der Buttons
		allReportsInSystemButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reportGenerator.createAllContactsInSystemReport(new CreateAllContactsInSystemReportCallback());
			}
		});

		// Verhalten der Buttons
		allReportButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reportGenerator.createAllContactsOfUserReport(currentUser, new CreateAllContactsOfUserReportCallback());
			}
		});

		datatypemenu.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(datatypemenu.getSelectedItemText()=="Datum") {
				datepicker.setVisible(true);
				Button finish = new Button("Fertig");
				verPanel4.add(finish);
				finish.addClickHandler(new DatePickerClickHandler(finish));
				}
			}
		});

		/**
		 * Der DatePicker brauch keinen Valuechangehandler, da der Wert nicht mehr in
		 * eine Textbox gespeichert wird, sondern direkt nach dem Wert gesucht wird.
		 */
		datepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (datepicker != null) {
					// pval.setDateValue(event.getValue());
					valueBox.setText(event.getValue().toString());
				}
			}
		});

		filteredReportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				JabicsUser u = new JabicsUser();
				u.setId(1);
				u.setEmail("stahl.alexander@live.de");
				u.setUsername("Stahlex");
				u.setLoggedIn(true);
				GWT.log(propertysb.getValue());
				for (int i = 0; i< userpropertys.length; i++) {
				
					if(userpropertys[i].getLabel() == propertysb.getValue()) {
				
				if (userpropertys[i].getType() == Type.STRING) {
					Property p = new Property(null, Type.STRING, false);
					PValue pvalue = new PValue(p, valueBox.getText(), u);
					// TODO hier currentUser einfügen
					reportGenerator.createFilteredContactsOfUserReport(pvalue, u,
							new CreateFilteredContactsOfUserReportCallback());
				} else if (userpropertys[i].getType() == Type.INT) {
					Property p = new Property(null, Type.INT, false);
					if (valueBox.getText() != null) {
						PValue pvalue = new PValue(p, valueBox.getText(), u);
						reportGenerator.createFilteredContactsOfUserReport(pvalue, u,
								new CreateFilteredContactsOfUserReportCallback());
					} else
						System.out.println("Eingegebener Wert ist nicht im korrekten Format (int).");

				} else if (userpropertys[i].getType() == Type.FLOAT) {
					Property p = new Property(null, Type.FLOAT, false);
					if (valueBox.getText() != null) {
						PValue pvalue = new PValue(p, valueBox.getText(), u);
						reportGenerator.createFilteredContactsOfUserReport(pvalue, u,
								new CreateFilteredContactsOfUserReportCallback());
					}
				} else if (userpropertys[i].getType() == Type.DATE) {
					Property p = new Property(null, Type.DATE, false);
					PValue pvalue = new PValue(p, datepicker.getValue(), u);
					reportGenerator.createFilteredContactsOfUserReport(pvalue, u,
							new CreateFilteredContactsOfUserReportCallback());
				} else
					Window.alert("Bitte in mindestens ein Feld ein Filterkriterium eingeben");

			}
				//fsklammer
					}
			}
		});

	}

	class DatePickerClickHandler implements ClickHandler {
		Button p;

		DatePickerClickHandler(Button p) {
			this.p = p;
		}

		public void onClick(ClickEvent event) {
			datepicker.setVisible(false);
			p.setVisible(false);
		}
	}

	private class CreateAllContactsInSystemReportCallback implements AsyncCallback<AllContactsInSystemReport> {

		@Override
		public void onFailure(Throwable caught) {
			// Fehler werden gelogt.
			ClientsideSettings.getLogger().severe("Erzeugen des Reports fehlgeschlagen.");
		}

		@Override
		public void onSuccess(AllContactsInSystemReport report) {
			GWT.log("Report zurück!");
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
			GWT.log("Filtered Report zurück!");
			if (report != null) {
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("content").clear();
				RootPanel.get("content").add(new HTML(writer.getReportText()));
			}
		}
	}

	private class CreateAllContactsOfUserReportCallback implements AsyncCallback<AllContactsOfUserReport> {

		@Override
		public void onFailure(Throwable caught) {
			ClientsideSettings.getLogger().severe("Erzeugen des Reports fehlgeschlagen.");
		}

		@Override
		public void onSuccess(AllContactsOfUserReport report) {
			if (report != null) {
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("content").clear();
				RootPanel.get("content").add(new HTML(writer.getReportText()));
			}
		}
	}
	
	private class getPropertysOfJabicsUserCallback implements AsyncCallback<ArrayList<Property>> {

		@Override
		public void onFailure(Throwable caught) {
			ClientsideSettings.getLogger().severe("Fehler beim Laden der Eigenschaften");
		}

		@Override
		public void onSuccess(ArrayList<Property> result) {
			propertystosuggest = new MultiWordSuggestOracle();
			userpropertys = new Property[result.size()];
			
			for (int i = 0; i< result.size(); i++ ) {
				userpropertys[i] = result.get(i);
				propertystosuggest.add(result.get(i).getLabel());
			}
			
			propertysb = new SuggestBox(propertystosuggest);
			verPanel1.add(propertysb);
			
			}
		}
	}


