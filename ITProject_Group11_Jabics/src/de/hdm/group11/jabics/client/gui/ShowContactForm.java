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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BoStatus;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;

public class ShowContactForm extends VerticalPanel {

	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	EditorAdmin e;
	JabicsUser u;
	Contact currentContact;
	Boolean userIsOwner = false;

	CellTable<PValue> values;
	ListDataProvider<PValue> valueProvider;

	Column<PValue, String> prop;
	Column<PValue, String> pval;
	Column<PValue, String> shareStatus;

	HorizontalPanel sharePanel = new HorizontalPanel();

	Button editButton = new Button("Kontakt bearbeiten");
	Button shareContactButton = new Button("Kontakt neu teilen");
	Button shareExistingContactButton = new Button("Teilen bearbeiten");
	Button deleteButton = new Button("Kontakt löschen");

	public ShowContactForm() {

		GWT.log("SHOWContact Construct");
		values = new CellTable<PValue>();
		valueProvider = new ListDataProvider<PValue>();
		valueProvider.addDataDisplay(values);

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

		shareStatus = new Column<PValue, String>(new TextCell()) {
			public String getValue(PValue object) {
				if (object.getShareStatus() == BoStatus.IS_SHARED) {
					return "Geteilt";
				}
				if (object.getShareStatus() == BoStatus.NOT_SHARED) {
					return "Nicht Geteilt";
				}
				return "NoStatus";
			}
		};

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
						if (v != null) {
							GWT.log("Löschen fehlgeschlagen");
						}
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

		values.addColumn(prop, "Eigenschaft");
		values.setColumnWidth(prop, 50, Unit.PX);
		values.addColumn(pval, "Ausprägung");
		values.setColumnWidth(pval, 50, Unit.PX);
		values.addColumn(shareStatus, "Share");
		values.setColumnWidth(pval, 50, Unit.PX);

		sharePanel.add(shareContactButton);
		sharePanel.add(shareExistingContactButton);
		try {
			GWT.log("ShowCont panels hinzufügen");
			this.add(editButton);
			this.add(values);
			this.add(sharePanel);

			this.add(deleteButton);
		} catch (Exception caught) {
			Window.alert(caught.toString());
		}

	}

	public void onLoad() {

		userIsOwner();
		// den Status des Boolschen Werts userIsOwner ermitteln
		if (userIsOwner) {
			sharePanel.setVisible(true);
		} else {
			sharePanel.setVisible(false);
		}

		GWT.log("Kontakte holen");
		if (valueProvider.getList().isEmpty()) {
			editorService.getPValueOf(currentContact, u, new GetPValuesCallback());
			GWT.log("4OnLoad SHOWContact");
		} else {
			renderTable(currentContact.getValues());
		}
	}

	/**
	 * Die PValues in die richtige Reihenfolge bringen und zur Anzeige bringen.
	 * 
	 * @param values
	 */
	public void renderTable(ArrayList<PValue> values) {
		ArrayList<PValue> result = new ArrayList<PValue>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		//(Dieser Algorithmus lässt sich wahrscheinlich deutlich effizienter implementieren)
		for (PValue pv : values) {
			Integer i = new Integer(pv.getProperty().getId());
			boolean bol = true;
			// Alle bekannten P-Ids durchlaufen und schauen ob bereits vorhanden
			for (Integer ii : ids) {
				if (ii.equals(i)) bol = false;
			}
			// Wenn Id noch nicht gefunden, einfach hinzufügen
			if (bol) {
				ids.add(i);
				result.add(pv);
			} else { // Wenn id schonmal gefunden, an der stelle des PValue mit der gleichen ID im
				// result array einfügen
				pv.getProperty().setLabel("");
				int iterator = 0;
				boolean cancel = true;
				for(PValue pVal : result) {
					if(pVal.getProperty().getId() == i && cancel) {
						result.add(iterator, pv);
						cancel = false;
					}
					iterator++;
				}
			}
		}
		valueProvider.setList(result);
		GWT.log(result.get(1).getStringValue());
		valueProvider.flush();
	}

	public void userIsOwner() {
		GWT.log("userIsOwner");
		try {
			GWT.log("userIsOwner1");
			if (currentContact.getOwner() != null) {
				GWT.log("userIsOwner2");
				if (currentContact.getOwner().getId() == u.getId()) {
					userIsOwner = true;
					GWT.log("userIsOwner3");
				} else
					userIsOwner = false;
				GWT.log("userIsOwner4");
			} else
				editorService.getOwnerOfContact(currentContact, new GetOwnerOfContactCallback());
			GWT.log("userIsOwner fertig");
		} catch (Exception e) {
			GWT.log("Besitzer in Kontakt nicht gesetzt");
			editorService.getOwnerOfContact(currentContact, new GetOwnerOfContactCallback());
		}
	}

	public void setContact(Contact c) {
		this.currentContact = c;
		if (valueProvider != null) {
			valueProvider.setList(c.getValues());
			valueProvider.flush();
		}
	}

	public void setUser(JabicsUser u) {
		this.u = u;
	}

	public void setEditor(EditorAdmin e) {
		this.e = e;
	}

	class GetOwnerOfContactCallback implements AsyncCallback<JabicsUser> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		public void onSuccess(JabicsUser result) {
			GWT.log("Besitzer geholt!");
			currentContact.setOwner(result);
			userIsOwner();
		}
	}

	class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		public void onSuccess(ArrayList<PValue> result) {

			if (result != null) {
				currentContact.setValues(result);
				renderTable(result);
			}
		}
	}

}
