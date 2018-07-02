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
import de.hdm.group11.jabics.client.gui.Report.DatePickerClickHandler;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.bo.Type;

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

	public void onLoad() {
		ct = new ContactCellListTab();
		list = ct.createContactTabForSearchForm();
		listInfoLabel = new Label();
		ausgabeLabel = new Label();
		verPanel1 = new VerticalPanel();
		verPanel2 = new VerticalPanel();
		verPanel3 = new VerticalPanel();
		verPanel4 = new VerticalPanel();
		verPanel5 = new VerticalPanel();
		pvalueLabel = new Label("Wert:");
		propertyLabel = new Label("Eigenschaft:");
		datatypemenu = new ListBox();
		datatypeLabel = new Label("Datentyp:");
		datepicker = new DatePicker();
		finalPVal = new PValue();
		back = new Button("Zurück");

		listInfoLabel.setText("Durchsuche Liste  '" + cl.getListName() + "'.");

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
		this.add(back);
		

		ausgabeLabel.setVisible(false);

		ct.setEditor(e);
		
		back.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				e.showContactList(cl);
			}});

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
					if (valueBox.getText().isEmpty()) {
						finalPVal.setIntValue(-2147483648);
						// Aufruf des der Listensuche in der EditorServiceImpl
						editorService.searchInList(cl, finalPVal, new SearchInListCallback());
					} else {
						finalPVal.setIntValue(Integer.valueOf(valueBox.getValue()));
						// Aufruf des der Listensuche in der EditorServiceImpl
						editorService.searchInList(cl, finalPVal, new SearchInListCallback());
					}
					finalPVal.setProperty(finalProperty);
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
					if (valueBox.getText().isEmpty()) {
						finalPVal.setFloatValue(Float.MIN_VALUE);
					
						// Aufruf des der Listensuche in der EditorServiceImpl
						editorService.searchInList(cl, finalPVal, new SearchInListCallback());
					} else {
					finalPVal.setFloatValue(Float.valueOf(valueBox.getValue()));
					finalPVal.setProperty(finalProperty);
					// Aufruf des der Listensuche in der EditorServiceImpl
					editorService.searchInList(cl, finalPVal, new SearchInListCallback());
					}
					break;
				default:
					break;

				}

			}
		});

		editorService.getPropertysOfJabicsUser(currentUser, new getPropertysOfJabicsUserCallback());

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

		datepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (datepicker != null) {
					// pval.setDateValue(event.getValue());
					valueBox.setText(event.getValue().toString());
					tempDate = event.getValue();
					GWT.log(tempDate.toString());
					finalPVal.setDateValue(tempDate);
				}
			}
		});
	}

	public SearchForm() {
		sp = new StackPanel();

		sb = new Button("Finden");
		valueBox = new TextBox();
	}

	void setContactList(ContactList cl) {
		this.cl = cl;
	}

	void setEditor(EditorAdmin e) {
		this.e = e;
	}

	void setJabicsUser(JabicsUser u) {
		this.currentUser = u;
	}

	class SearchInListCallback implements AsyncCallback<ArrayList<Contact>> {
		@Override

		public void onFailure(Throwable caugth) {
			Window.alert("Der angegebene Wert wurde nicht gefunden");
		}

		@Override
		public void onSuccess(ArrayList<Contact> result) {
			if (result != null) {
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
				GWT.log(p.getLabel());
			}

			propertySuggest = new SuggestBox(propertyToSuggest);

			/**
			 * selectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch
			 * die suggestbox ausgewählt wurde. Dieser wird durch Klick auf den button
			 * "Nutzer hinzufügen" zur liste der zu teilenden Nutzer hinzugefügt
			 */
			propertySuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
				public void onSelection(SelectionEvent<SuggestOracle.Suggestion> sel) {
					finalProperty = new Property();
					finalProperty.setLabel(propertySuggest.getText());
					GWT.log(">>>>>>>>>>>>>" + finalProperty.getLabel());
					finalPVal.setProperty(finalProperty);
					// finalPVal.getProperty().setType(Type.STRING);
				}
			});
			verPanel1.add(propertySuggest);

		}
	}
}