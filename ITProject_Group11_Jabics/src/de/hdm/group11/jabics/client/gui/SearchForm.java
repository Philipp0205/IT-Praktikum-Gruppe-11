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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;



/**
 * Diese Klasse realisiert die Abbildung einer Suchoberfläche für Kontaktlisten
 * auf das GUI. Es kann nach individuellen, vom jeweiligen Nutzer angelegten
 * Eigenschaften und spezifischen Eigenschaftsausprägungen gefiltert werden.
 * Dazu muss immer der richtige Datentyp einer Eigenschaft angegeben werden.
 *
 * @author Brase
 */
public class SearchForm extends VerticalPanel {

	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	StackPanel sp;
	ContactCellListTab ct;
	CellList<Contact> list;
	TextBox valueBox;
	Button sb;
	Button back;
	Label listInfoLabel;
	ContactList cl;
	EditorAdmin e;

	DatePicker datepicker;

	Label ausgabeLabel;
	Label pvalueLabel;
	Label propertyLabel;
	Label datatypeLabel;
	ListBox datatypemenu;
	Label noResultLabel;

	MultiWordSuggestOracle propertyToSuggest;
	SuggestBox propertySuggest;
	PValue finalPVal;
	Property finalProperty;
	VerticalPanel verPanel1;
	VerticalPanel verPanel2;
	VerticalPanel verPanel3;
	VerticalPanel verPanel4;
	VerticalPanel verPanel5;
	JabicsUser currentUser;
	ArrayList<Property> PropertyArrayList;
	Date tempDate;
	HorizontalPanel mainpanel = new HorizontalPanel();

	DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd") ;
			 
	
	/**
	 * Diese Methode bringt die GWT-Widgets der SearchForm Klasse zur
	 * Anzeige. Die einzelnen Widgets werden in drei Hauptbereiche (verPanel1, 2 und
	 * 3) gegliedert.
	 */

	public void onLoad() {
		listInfoLabel = new Label();
		ausgabeLabel = new Label();
		noResultLabel = new Label("Keine Ergebnisse.");
		verPanel1 = new VerticalPanel();
		verPanel2 = new VerticalPanel();
		verPanel3 = new VerticalPanel();
		verPanel4 = new VerticalPanel();
		verPanel5 = new VerticalPanel();
		
		pvalueLabel = new Label("Wert:");
		pvalueLabel.setStyleName("wertlabel");
		propertyLabel = new Label("Eigenschaft:");
		propertyLabel.setStyleName("eigenschaftlabel");
		datatypemenu = new ListBox();
		datatypemenu.setStyleName("datatypeMenu");
		datatypeLabel = new Label("Datentyp:");
		datatypeLabel.setStyleName("datentyplabel");
		datepicker = new DatePicker();
		finalPVal = new PValue();
		finalProperty = new Property();
		back = new Button("↩");
		back.setStyleName("sfback");
		ct = new ContactCellListTab(currentUser, null);
		list = ct.createContactTabForSearchForm();

		listInfoLabel.setText("Durchsuche Liste  '" + cl.getListName() + "'.");
		listInfoLabel.setStyleName("contactListHeadline");
		
		noResultLabel.setStyleName("reslabel");
		ausgabeLabel.setStyleName("successfulresultl");
		
		verPanel1.add(propertyLabel);
		mainpanel.add(verPanel1);

		verPanel2.add(pvalueLabel);
		verPanel2.add(valueBox);
		mainpanel.add(verPanel2);

		verPanel3.add(datatypeLabel);
		verPanel3.add(datatypemenu);
		datatypemenu.addItem("Text");
		datatypemenu.addItem("Datum");
		datatypemenu.addItem("Dezimalzahl");
		datatypemenu.addItem("Ganzzahl");
		datatypemenu.setSelectedIndex(4);
		mainpanel.add(verPanel3);

		verPanel4.add(datepicker);
		datepicker.setVisible(false);
		// Set the default value
		mainpanel.add(verPanel4);

		verPanel5.add(sb);
		mainpanel.add(verPanel5);

		this.add(listInfoLabel);
		this.add(mainpanel);
		this.add(sp);
		this.add(ausgabeLabel);
		this.add(noResultLabel);
		this.add(back);

		ausgabeLabel.setVisible(false);
		noResultLabel.setVisible(false);

		ct.setEditor(e);

		/**
		 * Bei der Aktivierung des "Zurück" Buttons, gelangt der Systemnutzer zurück in
		 * die Listenansicht (ContactListForm).
		 * 
		 */

		back.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				e.showContactList(cl);
			}
});

		/**
		 * Bei der Aktivierung des "Finden" Buttons wird die Service Methode
		 * "searchInList" aufgerufen, welche die als Klassenvariable angelegte
		 * Kontaktliste serverseitig durchsucht. Anschließend wird die Ausgabe
		 * angezeigt.
		 * 
		 */
		sb.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				sp.setVisible(false);
				ausgabeLabel.setVisible(false);

				// }
				// }));
				//
				// valueBox.addValueChangeHandler(new ValueChangeHandler<String>() {
				// @Override
				// public void onValueChange(ValueChangeEvent<String> event) {

				switch (datatypemenu.getSelectedItemText()) {
				case "Text":
					if (valueBox.getValue() != "") {
						finalPVal.setStringValue(valueBox.getValue());
					} else {
						finalPVal.setStringValue(null);
					}
					finalPVal.setProperty(finalProperty);
					GWT.log(finalProperty.getLabel());
					// Aufruf des der Listensuche in der EditorServiceImpl+
					editorService.searchInList(cl, finalPVal, new SearchInListCallback());
					break;
				case "Ganzzahl":
					if (valueBox.getValue() != "") {
						finalPVal.setIntValue(Integer.valueOf(valueBox.getValue()));
					} else {
						finalPVal.setIntValue(-2147483648);
					}
					finalPVal.setProperty(finalProperty);
					// Aufruf des der Listensuche in der EditorServiceImpl
					editorService.searchInList(cl, finalPVal, new SearchInListCallback());

					break;
				case "Datum":
					if (valueBox.getValue() != "") {
						finalPVal.setDateValue(datepicker.getValue());
					} else {
						finalPVal.setDateValue(null);
					}
					finalPVal.setProperty(finalProperty);
					// Aufruf des der Listensuche in der EditorServiceImpl
					editorService.searchInList(cl, finalPVal, new SearchInListCallback());

					break;
				case "Dezimalzahl":
					if (valueBox.getValue() != "") {
						finalPVal.setFloatValue(Float.valueOf(valueBox.getValue()));
					} else {
						finalPVal.setFloatValue(-99999997952f);
					}
					finalPVal.setProperty(finalProperty);
					// Aufruf des der Listensuche in der EditorServiceImpl
					editorService.searchInList(cl, finalPVal, new SearchInListCallback());
					break;
				default:
					break;

				}

			}
});
		
		
		/**
		 * Durch diesen ServiceAufruf werden die spezifisch mit dem Systemnutzer in
		 * Verbindung stehenden Eigenschaften in eine SuggestBox geladen.
		 * @param JabicsUser currentUser
		 * @param getPropertysOfJabicsUserCallback
		 */
		editorService.getPropertysOfJabicsUser(currentUser, new getPropertysOfJabicsUserCallback());

		/**
		 * Sofern im Datentypmenü ein "Datum" ausgewählt wurde, erscheint nach einem
		 * Klick in die Wert-TextBox ein Datepicker zur bequemen Eingabe eines Datums.
		 * Außerdem wird ein Button zum wieder Schließen des Datepickers erzeugt.
		 */
		valueBox.addClickHandler(new ClickHandler() {
			Button finish = new Button("Fertig");

			public void onClick(ClickEvent event) {
				if (datatypemenu.getSelectedItemText() == "Datum") {
					datepicker.setVisible(true);
					finish.setVisible(true);
					verPanel4.add(finish);

					finish.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							datepicker.setVisible(false);
							finish.setVisible(false);
						}
					});
				}
				if (datatypemenu.getSelectedItemText() != "Datum") {
					datepicker.setVisible(false);
					finish.setVisible(false);
				}
			}
});

		/**
		 * Nach dem Auswählen eines Datentyps wird der Klassenvariable
		 * <code>finalPValue</code> ein Pointer und der richtige Datentyp zugewiesen.
		 */
		datatypemenu.addChangeHandler(new ChangeHandler() {

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

			}
});

		/**
		 * Sobald sich ein Wert im Datepicker verändert, wird dieser der Klassenvariable
		 * <code>finalPValue</code> und der Wert-TextBox zugewiesen.
		 */
		datepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (datepicker != null) {
					// pval.setDateValue(event.getValue());
					valueBox.setText(dateTimeFormat.format(event.getValue()));
					tempDate = event.getValue();
					GWT.log(tempDate.toString());
					finalPVal.setDateValue(tempDate);
				}
			}
		});
}

	/**
	 * Konstruktor der SearchForm. Ein neues StackPanel, ein "Finden" Button und
	 * eine Eingabebox werden instanziiert.
	 */
	public SearchForm() {
		sp = new StackPanel();
		sp.setStyleName("sp");

		sb = new Button("Finden");
		sb.setStyleName("finden");
		valueBox = new TextBox();
		valueBox.setStyleName("TextBox");
	}

	
	public void showNoResults() {
		noResultLabel.setVisible(true);
	}
	/**
	 * Eine Methode zum Setzen der zu durchsuchenden Kontaktliste.
	 */
	public void setContactList(ContactList cl) {
		this.cl = cl;
	}

	/**
	 * Eine Methode zum Setzen der Editorklasse. Dies ist wichtig, wenn der Nutzer
	 * wieder zurück zur Kontaktlisten Ansicht gelangen will.
	 */
	public void setEditor(EditorAdmin e) {
		this.e = e;
	}

	/**
	 * Eine Methode zum Setzen des Nutzers der Aktiven Sitzung.
	 */
	public void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
}

	/**
	 * Dies ist die Callback-Klasse, welche die Aktionen nach einer Suche bestimmt.
	 * Das StackPanel wird geleert, mit den Ergebnissen erfüllt und sichtbar
	 * gemacht. Zudem werden die Suchinformationen erstellt.	 
	 */
	class SearchInListCallback implements AsyncCallback<ArrayList<Contact>> {
		@Override

		public void onFailure(Throwable caugth) {
			Window.alert("Der angegebene Wert wurde nicht gefunden");
		}

		@Override
		public void onSuccess(ArrayList<Contact> result) {
			noResultLabel.setVisible(false);
			if (result != null) {
				if(result.isEmpty()) {
					showNoResults();
				} else {
					list = ct.createContactTabForSearchForm();
					for (Contact c : result) {
						ct.addsearchedContact(c);
					}
					sp.setVisible(true);
					sp.clear();
					sp.add(list, "Ergebnis:");
					if (valueBox.getText().equals("")) {
						ausgabeLabel.setText("Es wurde nach '" + propertySuggest.getText() + "' gesucht.");
					} else {
						ausgabeLabel.setText("Es wurde nach '" + valueBox.getValue() + "' gesucht.");
					}
					ausgabeLabel.setVisible(true);
				}
			} else showNoResults();
		}
}

	/**
	 * Eine Callback-Klasse, welche die Aktionen nach dem Laden von
	 * Nutzerspezifischen Eigenschaften bestimmt. Alle dem Nutzerverfügbaren
	 * Eigenschaften werden in eine <code>SuggestBox</code> geladen.
	 */
	private class getPropertysOfJabicsUserCallback implements AsyncCallback<ArrayList<Property>> {

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(ArrayList<Property> result) {
			GWT.log("result!");
			propertyToSuggest = new MultiWordSuggestOracle();
			

			ArrayList<Property> userproperties = result;
			for (Property p : userproperties) {
				propertyToSuggest.add(p.getLabel());
				GWT.log(p.getLabel());
			}

			propertySuggest = new SuggestBox(propertyToSuggest);
			propertySuggest.setStyleName("TextBox");

			/**
			 * selectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch
			 * die suggestbox ausgewählt wurde. Dieser wird durch Klick auf den button
			 * "Nutzer hinzufügen" zur liste der zu teilenden Nutzer hinzugefügt
			 */
			propertySuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
				public void onSelection(SelectionEvent<SuggestOracle.Suggestion> sel) {
					finalProperty.setLabel(propertySuggest.getText());
					finalPVal.setProperty(finalProperty);
					// finalPVal.getProperty().setType(Type.STRING);
				}
			});
			verPanel1.add(propertySuggest);

		}
	}
}