package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.Date;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.LoginInfo;
import de.hdm.group11.jabics.shared.LoginServiceAsync;
import de.hdm.group11.jabics.shared.ReportGeneratorServiceAsync;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;
import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.AllContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.ContactReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.HTMLReportWriter;


/**
 * Diese Klasse realisiert die Abbildung aller zum Report Generator gehörigen
 * GUI-Elemente. Im Report Generator kann nach individuellen, vom jeweiligen
 * Nutzer angelegten Eigenschaften und spezifischen Eigenschaftsausprägungen
 * gefiltert werden. Dazu muss immer der richtige Datentyp einer Eigenschaft
 * angegeben werden. Zudem gibt es eine Ausgabe aller mit spezifisch
 * auswählbaren Nutzern geteilten Kontakte.
 *
 * @author Brase
 * @author Kurrle
 */
public class ReportAdmin {
	private JabicsUser currentUser;
	private LoginInfo loginfo;

	private ReportGeneratorServiceAsync reportGenerator = null;
	private EditorServiceAsync editorService = null;

	private Button allReportsInSystemButton = new Button("Alle Kontakte aller Nutzer im System");
	private Button filteredReportButton = new Button("Alle Kontakte mit diesen Filterkriterien");
	private Button allReportButton = new Button("Alle meine Kontakte");

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel logoutPanel = new HorizontalPanel();
	private HorizontalPanel otherReportsPanel = new HorizontalPanel();
	private HorizontalPanel navPanel = new HorizontalPanel();
	private VerticalPanel userPanel = new VerticalPanel();
	private VerticalPanel verPanel1 = new VerticalPanel();
	private VerticalPanel verPanel2 = new VerticalPanel();
	private VerticalPanel verPanel3 = new VerticalPanel();
	private VerticalPanel verPanel4 = new VerticalPanel();

	private TextBox valueBox = new TextBox();
	private ListBox datatypemenu = new ListBox();
	private DatePicker datepicker = new DatePicker();

	// Die mit der PropertySuggestBox zusammenhängenden Variablen
	private MultiWordSuggestOracle propertyToSuggest;
	private SuggestBox propertySuggest;
	private PValue finalPVal;
	private Label datatypel = new Label("Datentyp:");
	private Label valuelabel = new Label("Wert:");
	private Label propertyl = new Label("Eigenschaft:");

	// Alle mit der UserSuggestBox zusammenhängenden Variablen
	private MultiWordSuggestOracle userToSuggest;
	private SuggestBox userSuggest;
	private Button addUserButton;
	private Button removeUserButton;
	private Button sharedContactsButton;

	// Alle Variablen, die ein editieren der entstehenden Liste an Nutzern
	// ermöglichen
	private ArrayList<JabicsUser> allUser;
	private ArrayList<JabicsUser> finalUser;
	private ListDataProvider<JabicsUser> userDataProvider;
	private SingleSelectionModel<JabicsUser> userSelectionModel;
	private CellTable<JabicsUser> userTable;
	private JabicsUser suggestedUser;
	private JabicsUser selectedUser;

	/**
	 * Die Anzeige ist in zwei große Bereiche aufgeteilt. Zum einen Elemente für die
	 * Suche nach spezifischen Eigenschaften. Hierzu können der Datentyp und die
	 * dynamisch erzeugten Eigenschaften über ein Auswahlangebot gewählt werden. Ein
	 * konkreter Ausprägungswert kann anschließend in eine Wert-TextBox eingegeben
	 * werden. Zum Start der Suche, wird die Schaltfläche "Alle Kontakte mit diesen
	 * Filterkriterien" ausgewählt. Der zweite Bereich in welchem nach gemeinsamen
	 * Kontakten gefiltert werden kann, beinhaltet eine Suggestbox mit dynamisch
	 * erzeugten Schaltflächen zur Auswahl von Nutzern. Diese können durch den
	 * Button "Nutzer hinzufügen" zu den Suchkriterien hinzugefügt werden. Mit dem
	 * Button "Nutzer entfernen" kann dies nach dem Markieren des gleichen Nutzers
	 * rückgängig gemacht werden. Mit einem Klick auf die "gemeinsame
	 * Nutzer"-Schaltfläche wird die Suche nach gemeinsamen Kontakten gestartet.
	 * 
	 */
	public ReportAdmin() {

		// Instantitierung relevanter Variablen für UserSuggestion
		sharedContactsButton = new Button("gemeinsame Kontakte");
		finalUser = new ArrayList<JabicsUser>();
		userSelectionModel = new SingleSelectionModel<JabicsUser>();
		userDataProvider = new ListDataProvider<JabicsUser>();
		userTable = new CellTable<JabicsUser>();

		// Verhalten der Buttons
		createSelectionMenu();
		createUserSuggestMenu();
		
		otherReportsPanel.add(allReportsInSystemButton);
		otherReportsPanel.add(allReportButton);

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
		verPanel4.add(datepicker);
		datepicker.setVisible(false);

		navPanel.add(verPanel1);
		navPanel.add(verPanel2);
		navPanel.add(verPanel3);
		navPanel.add(verPanel4);
		navPanel.add(filteredReportButton);
		
		userPanel.add(userSuggest);
		userPanel.add(userTable);
		
		navPanel.add(userPanel);
		navPanel.add(addUserButton);
		navPanel.add(removeUserButton);
		navPanel.add(sharedContactsButton);

		mainPanel.add(navPanel);
		mainPanel.add(otherReportsPanel);

	}

	public void loadReport() {

		if (reportGenerator == null || editorService == null) {
			reportGenerator = ClientsideSettings.getReportGeneratorService();
			// TODO: Diese Zeile könnte kritisch werden, da zwei Module in einem Klasse
			editorService = ClientsideSettings.getEditorService();
		}

		// Alle Properties holen, nach denen vom Nutzer gefiltern werden kann
		reportGenerator.getPropertysOfJabicsUser(currentUser, new getPropertysOfJabicsUserCallback());

		
		// Aufbauen des RootPanels
		RootPanel.get("navigator").add(mainPanel);

	}

	/**
	 * Getter und Setter
	 */
	public void setLoginInfo(LoginInfo logon) {
		this.loginfo = logon;
	}

	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
	}
	
	public void loadLogout() {
		Button logoutButton = new Button("Abmelden");
		logoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.assign(loginfo.getLogoutUrl());
			}
		});
		logoutPanel.add(logoutButton);
		mainPanel.add(logoutPanel);
	}

	// Alle Nutzer des Systems holen
	private void retrieveUser() {
		GWT.log("allUser");
		if (editorService != null) {
			editorService.getAllUsers(new GetAllUserCallback());
		}
		GWT.log("allUserfetisch");
	}

	public void createSelectionMenu() {

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
				// u.setLoggedIn(true);

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

	public void createUserSuggestMenu() {

		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		GWT.log("SuggestBox");

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
				// u.setLoggedIn(true);
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

	/**
	 * Diese Klasse setzt das Attribut des PValue Objekts, wenn sich dieses ändert
	 * 
	 * @author Brase
	 *
	 * @param <String>
	 */
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
