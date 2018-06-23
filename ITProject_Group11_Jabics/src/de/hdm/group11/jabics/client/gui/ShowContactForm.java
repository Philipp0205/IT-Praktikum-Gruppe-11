package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;


import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;

public class ShowContactForm extends VerticalPanel{

	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Editor e;
	JabicsUser u;
	Contact currentContact;
	
	CellTable<PValue> values;
	ListDataProvider<PValue> valueProvider;
	
	Button editButton = new Button("Kontakt bearbeiten");
	Button shareContactButton = new Button("Kontakt teilen");
	Button deleteButton = new Button("Kontakt löschen");

	public void onLoad() {
		GWT.log("OnLoad SHOWContact");
		try {
		this.add(editButton);
		this.add(values);
		this.add(shareContactButton);
		this.add(deleteButton);
		}
		catch(Exception caught) {
			Window.alert(caught.toString());
		}
		GWT.log("2OnLoad SHOWContact");
		CellTable<PValue> values = new CellTable<PValue>();
		valueProvider = new ListDataProvider<PValue>();
		valueProvider.addDataDisplay(values);
		GWT.log("3OnLoad SHOWContact");
		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				e.editContact(currentContact);
			}
		});
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				editorService.deleteContact(currentContact, u, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						Window.alert("Löschen fehlgeschlagen");
					}
					public void onSuccess(Void v) {
						GWT.log("Löschen fehlgeschlagen");
					}
				});
			}
		});
		shareContactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log(currentContact.getName());
				e.showContactCollab(currentContact);
			}
		});
		GWT.log("4OnLoad SHOWContact");
	}
	
	/**
	 * Diese MEthode macht die Anzeige der PV möglich
	 * @param c
	 */
	public void showContact(Contact c) {
		this.setContact(c);
		this.onLoad();
		editorService.getPValueOf(currentContact, u, new GetPValuesCallback());
	}
	
	public void setContact(Contact c) {
		this.currentContact = c;
		
	}
	public void setUser(JabicsUser u) {
		this.u = u;
		
	}
	public void setEditor(Editor e) {
		this.e = e;
		
	}

	class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		public void onSuccess(ArrayList<PValue> result) {
				valueProvider.setList(result);
				valueProvider.flush();
		}
	}

}
