package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
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
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.GetAllNotCollaboratingUserCallback;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
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
	EditorServiceAsync editorService = null;
	Button allReportsInSystemButton = new Button("Alle Kontakte aller Nutzer im System");
	Button filteredReportButton = new Button("Alle Kontakte mit diesen Filterkriterien");
	Button allReportButton = new Button("Alle meine Kontakte");

	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel otherReportsPanel = new HorizontalPanel();
	HorizontalPanel navPanel = new HorizontalPanel();
	VerticalPanel verPanel1 = new VerticalPanel();
	VerticalPanel verPanel2 = new VerticalPanel();
	// VerticalPanel verPanel3 = new VerticalPanel();
	VerticalPanel verPanel4 = new VerticalPanel();
	Property[] userpropertys;

	TextBox valueBox = new TextBox();
	// TextBox intBox = new TextBox();
	// TextBox floatBox = new TextBox();
	// TextBox dateBox = new TextBox();
	ListBox datatypemenu = new ListBox();

	// Die mit der PropertySuggestBox zusammenhängenden Variablen
	MultiWordSuggestOracle propertyToSuggest;
	SuggestBox propertySuggest;
	Label datatypel = new Label("Eigenschaft:");
	Label valuelabel = new Label("Wert:");

	// Alle mit der Suggest Box zusammenhängenden Variablen
	MultiWordSuggestOracle userToSuggest;
	SuggestBox userSuggest;
	Button addUserButton;
	Button removeUserButton;
	Button sharedContactsButton;

	ArrayList<JabicsUser> allUser;
	ArrayList<JabicsUser> finalUser;
	ListDataProvider<JabicsUser> userDataProvider;
	SingleSelectionModel<JabicsUser> userSelectionModel;
	CellTable<JabicsUser> userTable;
	JabicsUser suggestedUser;
	JabicsUser selectedUser;

	// Label floatl = new Label("Dezimalzahl:");
	// Label db = new Label("Datum:");
	DatePicker datepicker = new DatePicker();

	@Override
	public void onModuleLoad() {
		if (reportGenerator == null || editorService == null) {
			reportGenerator = ClientsideSettings.getReportGeneratorService();
			editorService = ClientsideSettings.getEditorService();
		}

		/**
		 * Zunächst wird eine User-Instanz hinzugefügt. Später entfernen und dies den
		 * Login übernehmen lassen
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

		// Übergangslösung
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
		// verPanel3.add(floatl);
		// verPanel3.add(floatBox);
		// verPanel4.add(db);

		datepicker.setValue(null);
		// verPanel4.add(dateBox);
		verPanel4.add(datepicker);
		datepicker.setVisible(false);

		navPanel.add(verPanel1);
		navPanel.add(verPanel2);
		// navPanel.add(verPanel3);
		navPanel.add(verPanel4);
		navPanel.add(filteredReportButton);
		//navPanel.add(allReportsInSystemButton);

		mainPanel.add(navPanel);
		otherReportsPanel.add(allReportsInSystemButton);
		otherReportsPanel.add(allReportButton);
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
				if (datatypemenu.getSelectedItemText() == "Datum") {
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
				GWT.log(propertySuggest.getValue());
				for (int i = 0; i < userpropertys.length; i++) {

					if (userpropertys[i].getLabel() == propertySuggest.getValue()) {

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
					// fsklammer
				}
			}
		});

		retrieveUser();

	}

	private void retrieveUser() {
		GWT.log("allUser");
		editorService.getAllUsers(new GetAllUserCallback());
		GWT.log("allUserfetisch");
	}

	public void createUserSuggestMenu() {
		

		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		GWT.log("SuggestBox");

		//Instantitierung
		sharedContactsButton = new Button("gemeinsame Kontakte");
		finalUser = new ArrayList<JabicsUser>();
		userSelectionModel = new SingleSelectionModel<JabicsUser>();
		userDataProvider = new ListDataProvider<JabicsUser>();
		userTable = new CellTable<JabicsUser>();
		
		userTable.setSelectionModel(userSelectionModel);
		userDataProvider.addDataDisplay(userTable);
		userDataProvider.setList(finalUser);
		
		sharedContactsButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				JabicsUser u = new JabicsUser();
				u.setId(1);
				u.setEmail("stahl.alexander@live.de");
				u.setUsername("Stahlex");
				u.setLoggedIn(true);
				System.out.println(finalUser.get(0).getUsername());
				reportGenerator.createAllSharedContactsReport(u, finalUser, new CreateAllSharedContactsReportCallback());
			}
		});

		
		userSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedUser = userSelectionModel.getSelectedObject();
			}
		});
		
		addUserButton = new Button("Nutzer hinzufügen");
		addUserButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (suggestedUser != null) {
					finalUser.add(suggestedUser);
					userSuggest.setText("");
					userDataProvider.refresh();
					userDataProvider.flush();
				}
			}
		});

		GWT.log("SuggestBox4");
		removeUserButton = new Button("Nutzer entfernen");
		removeUserButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (selectedUser != null) {
					finalUser.remove(selectedUser);
					userDataProvider.refresh();
					userDataProvider.flush();
				}
			}
		});
		GWT.log("SuggestBox5");
		TextColumn<JabicsUser> username = new TextColumn<JabicsUser>() {
			public String getValue(JabicsUser u) {
				return u.getUsername();
			}
		};
		userTable.addColumn(username, "Nutzer");

		/**
		 * SuggestBox hinzufügen und mit Optionen befüllen
		 */
		userToSuggest = new MultiWordSuggestOracle();
		userSuggest = new SuggestBox(userToSuggest);

		for (JabicsUser u : allUser) {
			GWT.log("SuggestBoxalluser");
			try {
				userToSuggest.add(u.getUsername() + " " + u.getEmail());
				GWT.log("Nutzer zu Sug hinzugefügt");
			} catch (NullPointerException e) {
				Window.alert(
						"setzen des nutzernamens oder mailadresse in sugstbox failed, Nutzer mit Id: " + u.getId());
				try {
					userToSuggest.add(u.getUsername());
				} catch (NullPointerException ex) {
					Window.alert("setzen des nutzernamens auch fehlgeschlagen, Nutzer mit Id: " + u.getId());
				}
			}
		}

		/**
		 * selectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch
		 * die suggestbox ausgewählt wurde. Dieser wird durch Klick auf den button
		 * "Nutzer hinzufügen" zur liste der zu teilenden Nutzer hinzugefügt
		 */
		userSuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> sel) {
				for (JabicsUser u : allUser) {
					if (userSuggest.getValue().contains(u.getUsername())
							&& userSuggest.getValue().contains(u.getEmail())) {
						suggestedUser = u;
					}
				}
			}
		});

		userSuggest.setLimit(5);

		navPanel.add(userSuggest);
		navPanel.add(userTable);
		navPanel.add(addUserButton);
		navPanel.add(removeUserButton);
		navPanel.add(sharedContactsButton);
		navPanel.add(new Label("sorry kam nicht mehr dazu es alles anzuordnen (es ist voll funktional, die ausgewählten nutzer aus finalReports holen wenn es an die erstellung des reports geht), zeile 375 im Report"));
	}

	public void setAllUser(ArrayList<JabicsUser> u) {
		this.allUser = u;
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
	
	private class CreateAllSharedContactsReportCallback implements AsyncCallback<FilteredContactsOfUserReport>{
		
		@Override
		public void onFailure(Throwable caught) {
			GWT.log(caught.toString());
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

	public class GetAllUserCallback implements AsyncCallback<ArrayList<JabicsUser>> {

		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}

		public void onSuccess(ArrayList<JabicsUser> user) {
			GWT.log("alleNutzergesetzt   " + user.get(1).getEmail());
			setAllUser(user);
			createUserSuggestMenu();
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
			propertyToSuggest = new MultiWordSuggestOracle();
			userpropertys = new Property[result.size()];

			for (int i = 0; i < result.size(); i++) {
				userpropertys[i] = result.get(i);
				propertyToSuggest.add(result.get(i).getLabel());
			}

			propertySuggest = new SuggestBox(propertyToSuggest);
			verPanel1.add(propertySuggest);

		}
	}
}
