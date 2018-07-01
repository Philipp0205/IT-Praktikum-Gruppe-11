package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.GetAllNotCollaboratingUserCallback;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.ReportGeneratorServiceAsync;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.AllContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.CompositeReport;
import de.hdm.group11.jabics.shared.report.ContactReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.HTMLReportWriter;

public class Report implements EntryPoint {
	JabicsUser currentUser;

	ReportGeneratorServiceAsync reportGenerator = null;
	LoginServiceAsync loginService = null;
	EditorServiceAsync editorService = null;

	Button allReportsInSystemButton = new Button("Alle Kontakte aller Nutzer im System");
	Button filteredReportButton = new Button("Alle Kontakte mit diesen Filterkriterien");
	Button allReportButton = new Button("Alle meine Kontakte");

	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel otherReportsPanel = new HorizontalPanel();
	HorizontalPanel navPanel = new HorizontalPanel();
	VerticalPanel verPanel1 = new VerticalPanel();
	VerticalPanel verPanel2 = new VerticalPanel();
	VerticalPanel verPanel3 = new VerticalPanel();
	VerticalPanel verPanel4 = new VerticalPanel();

	TextBox valueBox = new TextBox();
	ListBox datatypemenu = new ListBox();
	DatePicker datepicker = new DatePicker();

	// Die mit der PropertySuggestBox zusammenhängenden Variablen
	MultiWordSuggestOracle propertyToSuggest;
	SuggestBox propertySuggest;
	PValue finalPVal;
	Label datatypel = new Label("Datentyp:");
	Label valuelabel = new Label("Wert:");
	Label propertyl = new Label("Eigenschaft:");

	// Alle mit der UserSuggestBox zusammenhängenden Variablen
	MultiWordSuggestOracle userToSuggest;
	SuggestBox userSuggest;
	Button addUserButton;
	Button removeUserButton;
	Button sharedContactsButton;

	// Alle Variablen, die ein editieren der entstehenden Liste an Nutzern
	// ermöglichen
	ArrayList<JabicsUser> allUser;
	ArrayList<JabicsUser> finalUser;
	ListDataProvider<JabicsUser> userDataProvider;
	SingleSelectionModel<JabicsUser> userSelectionModel;
	CellTable<JabicsUser> userTable;
	JabicsUser suggestedUser;
	JabicsUser selectedUser;

	@Override
	public void onModuleLoad() {
		if (reportGenerator == null || loginService == null) {
			reportGenerator = ClientsideSettings.getReportGeneratorService();
			loginService = ClientsideSettings.getLoginService();
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
		retrieveUser();

		loadReport();

	}

	public void loadReport() {
		JabicsUser u = new JabicsUser();
		u.setId(1);
		u.setEmail("stahl.alexander@live.de");
		u.setUsername("Stahlex");
//		u.setLoggedIn(true);
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
		datatypemenu.setSelectedIndex(4);

		verPanel1.add(propertyl);
		verPanel2.add(valuelabel);
		verPanel2.add(valueBox);
		verPanel3.add(datatypel);
		verPanel3.add(datatypemenu);
		// verPanel4.add(db);

		datepicker.setValue(null);
		// verPanel4.add(dateBox);
		verPanel4.add(datepicker);
		datepicker.setVisible(false);

		navPanel.add(verPanel1);
		navPanel.add(verPanel2);
		navPanel.add(verPanel3);
		navPanel.add(verPanel4);
		navPanel.add(filteredReportButton);
		// navPanel.add(allReportsInSystemButton);

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

		finalPVal = new PValue();
		Property finalProp = new Property();
		finalPVal.setProperty(finalProp);

		valueBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (finalPVal.getPointer() == 0) {
					Window.alert("Bitte zuerst Datentyp auswählen!");
				}

			}
		});

		valueBox.addValueChangeHandler(new PValueChangeHandler<String>());

		datatypemenu.addChangeHandler(new ChangeHandler() {

			Button finish = new Button("Fertig");

			@Override
			public void onChange(ChangeEvent event) {
				switch (datatypemenu.getSelectedItemText()) {
				case "Text":
					finalPVal.setPointer(2);
					finalPVal.getProperty().setType(Type.STRING);
					break;
				case "Ganzzahl":
					finalPVal.setPointer(1);
					finalPVal.getProperty().setType(Type.INT);
					break;
				case "Datum":
					datepicker.setVisible(true);
					finish.setVisible(true);
					verPanel4.add(finish);
					finish.addClickHandler(new DatePickerClickHandler(finish));
					finalPVal.setPointer(3);
					finalPVal.getProperty().setType(Type.DATE);
					break;
				case "Dezimalzahl":
					finalPVal.setPointer(4);
					finalPVal.getProperty().setType(Type.FLOAT);
					break;
				default:
					finalPVal.setPointer(0);
					finalPVal.getProperty().setType(Type.STRING);
					break;
				}

				if (datatypemenu.getSelectedItemText() != "Datum") {
					datepicker.setVisible(false);
					finish.setVisible(false);
				}
			}
		});

		/**
		 * Dem Button, der ein Filtern des Reports ermöglicht, seine Funktion zuweisen
		 */
		filteredReportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO: folgende Zeilen ersetzen
				JabicsUser u = new JabicsUser();
				u.setId(1);
				u.setEmail("stahl.alexander@live.de");
				u.setUsername("Stahlex");
//				u.setLoggedIn(true);

				if (finalPVal.getProperty().getType() != null || finalPVal.containsValue()) {

					GWT.log("Gefilterten Report erstellen");
					reportGenerator.createFilteredContactsOfUserReport(finalPVal, u,
							new CreateFilteredContactsOfUserReportCallback());
				} else
					Window.alert("Bitte in mindestens ein Feld ein Filterkriterium eingeben");
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

	}

	class PValueChangeHandler<String> implements ValueChangeHandler {
		@Override
		public void onValueChange(ValueChangeEvent event) {
			GWT.log("Änderungen in pValue: " + event.getValue());
			try {
				GWT.log("Pointer: " + finalPVal.getPointer());
				switch (finalPVal.getPointer()) {
				case 1:
					finalPVal.setIntValue(Integer.parseInt((java.lang.String) event.getValue()));
					break;
				case 2:
					finalPVal.setStringValue((java.lang.String) event.getValue());
					break;
				case 3:
					GWT.log("Datum wird durch DatePicker gesetzt");
					break;
				case 4:
					finalPVal.setFloatValue(Float.parseFloat((java.lang.String) event.getValue()));
					break;
				default:
					Window.alert("Bitte Datentyp angeben und erneut versuchen");
				}
			} catch (Exception e) {
				Window.alert("Konnte Wert nicht lesen, bitte im richtigen Format eingeben! (Kommazahlen mit Punkten!) "
						+ e.toString());
			}
		}
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

		// Instantitierung
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
			//	u.setLoggedIn(true);
				System.out.println(finalUser.get(0).getUsername());
				reportGenerator.createAllSharedContactsReport(u, finalUser,
						new CreateAllSharedContactsReportCallback());
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

	private class CreateAllSharedContactsReportCallback implements AsyncCallback<FilteredContactsOfUserReport> {
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
			GWT.log(caught.toString());
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
			GWT.log(caught.toString());
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
				for (ContactReport c : report.getSubReports()) {
					GWT.log(c.getContactInfo().getContent());
				}

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

			ArrayList<Property> userproperties = result;

			for (Property p : userproperties) {
				propertyToSuggest.add(p.getLabel());
			}

			propertySuggest = new SuggestBox(propertyToSuggest);

			/**
			 * selectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch
			 * die suggestbox ausgewählt wurde. Dieser wird durch Klick auf den button
			 * "Nutzer hinzufügen" zur liste der zu teilenden Nutzer hinzugefügt
			 */
			propertySuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
				public void onSelection(SelectionEvent<SuggestOracle.Suggestion> sel) {

					finalPVal.getProperty().setLabel(propertySuggest.getValue());
					GWT.log("Wert geändert " + finalPVal.getProperty().getLabel());
				}
			});
			verPanel1.add(propertySuggest);

		}
	}

}

/*
 * private class customPropertySuggest implements SuggestOracle{
 * 
 * public void add(Property p) {
 * 
 * }
 * 
 * public void requestSugestions(Request request, Callback callback) {
 * 
 * Collection<Suggestion> suggestions = new ArrayList<Suggestion>;
 * 
 * 
 * Response response = new Response(); response.setSuggestions(suggestions);
 * callback.onSuggestionsReady(request, response);
 * 
 * } }
 */
