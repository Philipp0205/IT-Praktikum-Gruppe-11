package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

public class ShowContactForm extends VerticalPanel {

	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	Editor e;
	JabicsUser u;
	Contact currentContact;

	CellTable<PValue> values;
	ListDataProvider<PValue> valueProvider;
	
	Column<PValue, String> prop;
	Column<PValue, String> pval;
	
	Button editButton = new Button("Kontakt bearbeiten");
	Button shareContactButton = new Button("Kontakt neu teilen");
	Button shareExistingContactButton = new Button("Teilen bearbeiten");
	Button deleteButton = new Button("Kontakt löschen");

	public ShowContactForm() {
		
		GWT.log("SHOWContact Construct");
		values = new CellTable<PValue>();
		
		prop = new Column<PValue, String>(new TextCell()) {
			public String getValue(PValue object) {
				return object.getProperty().getLabel();
			}
		};
		pval = new Column<PValue, String>(new TextCell()) {
			public String getValue(PValue object) {
				return object.toString();
			}
		};
		
		values.addColumn(prop, "Eigenschaft");
		values.setColumnWidth(prop, 50, Unit.PX);
		values.addColumn(pval, "Ausprägung");
		values.setColumnWidth(pval, 50, Unit.PX);
		
		try {
			GWT.log("ShowCont panels hinzufügen");
			this.add(editButton);
			this.add(values);
			this.add(shareContactButton);
			this.add(shareExistingContactButton);
			this.add(deleteButton);
		} catch (Exception caught) {
			Window.alert(caught.toString());
		}
		
	}
	
	public void onLoad() {
		
		valueProvider = new ListDataProvider<PValue>();
		valueProvider.addDataDisplay(values);

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
		shareExistingContactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log(currentContact.getName());
				e.showExistingContactCollab(currentContact);
			}
		});
		editorService.getPValueOf(currentContact, u, new GetPValuesCallback());
		GWT.log("4OnLoad SHOWContact");
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
			currentContact.setValues(result);
			valueProvider.setList(result);
			GWT.log(result.get(1).getStringValue());
			valueProvider.flush();
		}
	}

}
