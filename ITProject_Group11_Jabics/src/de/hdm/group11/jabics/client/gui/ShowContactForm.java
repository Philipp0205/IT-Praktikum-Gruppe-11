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
import com.google.gwt.user.client.ui.Label;
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

	Button editButton = new Button("✎");
	Label editLabel = new Label("Kontakt bearbeiten");
	Button shareContactButton = new Button("⋲");
	Label shareLabel = new Label("Kontakt teilen");
	Button shareExistingContactButton = new Button("✎");
	Label shareEditLabel = new Label("Teilen bearbeiten");
	Button deleteButton = new Button("🗑");
	Label deleteLabel = new Label("Kontakt löschen");

	//private CellTableResources ctRes = GWT.create(CellTableResources.class);

	public ShowContactForm() {

		HorizontalPanel horp1 = new HorizontalPanel();
		HorizontalPanel horp2 = new HorizontalPanel();
		HorizontalPanel horp3 = new HorizontalPanel();
		HorizontalPanel horp4 = new HorizontalPanel();
		HorizontalPanel haupthorp = new HorizontalPanel();
		horp1.add(editLabel);
		horp1.add(editButton);
		horp2.add(shareLabel);
		horp2.add(shareContactButton);
		horp3.add(shareEditLabel);
		horp3.add(shareExistingContactButton);
		horp4.add(deleteLabel);
		horp4.add(deleteButton);

		haupthorp.add(horp3);
		haupthorp.add(horp2);
		haupthorp.add(horp4);

		editLabel.addClickHandler(new editClickHandler());
		shareLabel.addClickHandler(new shareClickHandler());
		shareEditLabel.addClickHandler(new shareExistingClickHandler());
		deleteLabel.addClickHandler(new deleteClickHandler());

		editButton.setStyleName("editButton");
		editLabel.setStyleName("editLabel");
		shareContactButton.setStyleName("shareContactButton");
		shareLabel.setStyleName("shareLabel");
		shareExistingContactButton.setStyleName("shareExistingContactButton");
		shareEditLabel.setStyleName("shareEditLabel");
		deleteButton.setStyleName("deleteButton");
		deleteLabel.setStyleName("deleteLabel");

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

		prop.setCellStyleNames("prop");
		pval.setCellStyleNames("pval");
		shareStatus.setCellStyleNames("shareStatus");

		editButton.addClickHandler(new editClickHandler());

		deleteButton.addClickHandler(new deleteClickHandler());

		shareContactButton.addClickHandler(new shareClickHandler());

		shareExistingContactButton.addClickHandler(new shareExistingClickHandler());

		values.addColumn(prop, "Eigenschaft");
		values.setColumnWidth(prop, 50, Unit.PX);
		values.addColumn(pval, "Ausprägung");
		values.setColumnWidth(pval, 50, Unit.PX);
		values.addColumn(shareStatus, "Share");
		values.setColumnWidth(pval, 50, Unit.PX);
		values.setStyleName("Tabelle");

		try {
			GWT.log("ShowCont panels hinzufügen");

			this.add(values);
			this.add(haupthorp);
			this.add(horp1);

		} catch (Exception caught) {
			Window.alert(caught.toString());
		}

	}

	// Ressourcen für die CellTable

	public interface CellTableResources extends CellTable.Resources {

		@Override
		@Source("JabicsCellTable.css")
		CellTable.Style cellTableStyle();
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
		// (Dieser Algorithmus lässt sich wahrscheinlich deutlich effizienter
		// implementieren)
		for (PValue pv : values) {
			Integer i = new Integer(pv.getProperty().getId());
			boolean bol = true;
			// Alle bekannten P-Ids durchlaufen und schauen ob bereits vorhanden
			for (Integer ii : ids) {
				if (ii.equals(i))
					bol = false;
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
				for (PValue pVal : result) {
					if (pVal.getProperty().getId() == i && cancel) {
						result.add(iterator + 1, pv);
						cancel = false;
					}
					iterator++;
				}
			}
		}
		valueProvider.setList(result);
		// Den Kontakt mit den sortierten Werten updaten
		currentContact.setValues(result);
		GWT.log(result.get(1).getStringValue());
		valueProvider.flush();
	}

	public void userIsOwner() {
		GWT.log("userIsOwner");
		try {
			if (currentContact.getOwner() != null) {
				if (currentContact.getOwner().getId() == u.getId()) {
					userIsOwner = true;
					GWT.log("userIsOwner True");
				} else {
					userIsOwner = false;
					GWT.log("userIsOwner False");
				}
			} else {
				editorService.getOwnerOfContact(currentContact, new GetOwnerOfContactCallback());
				GWT.log("userIsOwner holt den Owner");
			}

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
			if (result != null) {
				GWT.log("Besitzer geholt!");
				currentContact.setOwner(result);
				userIsOwner();
			} else Window.alert("Besitzer konnte nicht ermittelt werden");
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

	class editClickHandler implements ClickHandler {
		public void onClick(ClickEvent ev) {
			e.editContact(currentContact);
		}
	}

	class shareExistingClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			GWT.log(currentContact.getName());
			e.showExistingContactCollab(currentContact);
		}
	}

	class shareClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			e.showContactCollab(currentContact);
		}
	}

	class deleteClickHandler implements ClickHandler {
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
	}

}
