package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.cellview.client.CellList;

import com.google.gwt.resources.client.ImageResource;

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
import de.hdm.group11.jabics.resource.JabicsResources;

import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BoStatus;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;

/**
 * Eine <code>ShowContactListForm</code> welche einen <code>Contact</code> zur Anzeige bringt.
 * 
 * @author Anders, Kurrle, Brase
 */
public class ShowContactForm extends VerticalPanel {

	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	EditorAdmin e;
	JabicsUser u;
	Contact currentContact = new Contact();
	Boolean userIsOwner = false;

	CellTable<PValue> values;
	ListDataProvider<PValue> valueProvider;

	Column<PValue, String> prop;
	Column<PValue, String> pval;
	Column<PValue, ImageResource> shareStatus;

	HorizontalPanel editPanel = new HorizontalPanel();
	HorizontalPanel sharePanel = new HorizontalPanel();
	HorizontalPanel shareSubPanel1 = new HorizontalPanel();
	HorizontalPanel shareSubPanel2 = new HorizontalPanel();
	HorizontalPanel deletePanel = new HorizontalPanel();
	HorizontalPanel mainPanel = new HorizontalPanel();

	Button editButton = new Button("✎");
	Button editLabel = new Button("Kontakt bearbeiten");
	Button shareContactButton = new Button("⋲");
	Button shareLabel = new Button("Kontakt teilen");
	Button shareExistingContactButton = new Button("✎");
	Button shareEditLabel = new Button("Teilen bearbeiten");
	Button deleteButton = new Button("🗑");
	Button deleteLabel = new Button("Kontakt löschen");
	
	//cellTable Ressourcen	
	public interface CellTableResources extends CellTable.Resources {

		@Source("JabicsCellTable.css")
		CellTable.Style cellTableStyle();
	}
	
	/**
	 * Konstruktor welcher eine Instanz von <code>ShowContactForm</code> erzeugt.
	 * Die <code>Property</code>s und die <code>PValue</code>s werden in einer Tabelle ausgebegen.
	 */
	public ShowContactForm() {

		editPanel.add(editLabel);
		editPanel.add(editButton);
		editPanel.add(deleteLabel);
		editPanel.add(deleteButton);
		
		shareSubPanel1.add(shareLabel);
		shareSubPanel1.add(shareContactButton);
		shareSubPanel2.add(shareEditLabel);
		shareSubPanel2.add(shareExistingContactButton);
		
		sharePanel.add(shareSubPanel1);
		sharePanel.add(shareSubPanel2);
		
		
		mainPanel.add(sharePanel);
		mainPanel.add(deletePanel);

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


		shareStatus = new Column<PValue, ImageResource>(new ImageResourceCell()) {
			@Override
			public ImageResource getValue(PValue object) {
				if (object.getShareStatus() == BoStatus.IS_SHARED) {
					return JabicsResources.INSTANCE.isshared();
				}
				if (object.getShareStatus() == BoStatus.NOT_SHARED) {
					return JabicsResources.INSTANCE.isnotshared();
				}
				return null; 
				
			}
		};
		shareStatus.setHorizontalAlignment(ALIGN_CENTER);

		shareStatus.setHorizontalAlignment(ALIGN_CENTER);


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
		values.addColumn(shareStatus, "Status");
		values.setColumnWidth(pval, 50, Unit.PX);
		values.setStyleName("Tabelle");

		try {
			GWT.log("ShowCont panels hinzufügen");

			this.add(values);
			this.add(mainPanel);
			this.add(editPanel);

		} catch (Exception caught) {
			Window.alert(caught.toString());
		}
	}
	
	/**
	 * Wird beim ersten laden der ContactListForm ausgeführt.
	 */
	public void onLoad() {
		userIsOwner();
		// den Status des Boolschen Werts userIsOwner ermitteln
		if (userIsOwner) {
			sharePanel.setVisible(true);
		} else {
			sharePanel.setVisible(false);
		}

		if (valueProvider.getList().isEmpty()) {
			editorService.getPValueOf(currentContact, u, new GetPValuesCallback());
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

		if (values != null) {
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
			// Den Kontakt mit den sortierten Werten updaten
			currentContact.setValues(result);
		}
		valueProvider.setList(result);
		valueProvider.flush();
	}
	
	/**
	 *  Setzt den aktuellen User als Besitzer des Kontaktes.
	 */
	public void userIsOwner() {
		try {
			if (currentContact.getOwner() != null) {
				if (currentContact.getOwner().getId() == u.getId()) {
					userIsOwner = true;
				} else {
					userIsOwner = false;
				}
			} else {
				editorService.getOwnerOfContact(currentContact, new GetOwnerOfContactCallback());
			}

		} catch (Exception e) {
			editorService.getOwnerOfContact(currentContact, new GetOwnerOfContactCallback());
		}
	}
	
	/**
	 * Setzt den aktuellen Kontakt.
	 * 
	 * @param c
	 * 			<code>Contact</code>welcher gesetzt werden soll.
	 */
	public void setContact(Contact c) {
		if (c != null) {
			this.currentContact = c;
			if (valueProvider != null) {
				if (c.getValues() != null) {
					valueProvider.setList(c.getValues());
				}
				valueProvider.flush();
			}
		} else {
			Window.alert("kontakt nicht bekannt");
		}
	}
  

	public void removeContact(){
		e.removeContact(currentContact);
		e.showMenuOnly();
	}

  /**
	 * Setzt den User der ContactListForm
	 * 
	 * @param u 
	 * 			<code>User</code> der gesetzt werden soll.
	 */
	public void setUser(JabicsUser u) {
		if (u != null) {
			this.u = u;
		}
	}
	
	/**
	 * Setzt den Editor.
	 * 
	 * @param e 
	 * 		<code>Editor</code> der gesetzt werden soll.
	 */
	public void setEditor(EditorAdmin e) {
		if (e != null) {
			this.e = e;
		} else
			Window.alert("editor null");
	}
	
	/**
	 * Callback welcher ausgelöst weg wenn der Owner des Kontakts bezogen wird.
	 * 
	 */
	class GetOwnerOfContactCallback implements AsyncCallback<JabicsUser> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		}

		public void onSuccess(JabicsUser result) {
			if (result != null) {
				currentContact.setOwner(result);
				userIsOwner();
			} else
				Window.alert("Besitzer konnte nicht ermittelt werden");
		}
	}
	
	/**
	 * Callback welcher ausgelöst wird, wenn die <code>PValues</code> des Kontakts beozogen werden.
	 *
	 */
	class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>> {
		public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
			Window.alert("Values holen fail");
		}

		public void onSuccess(ArrayList<PValue> result) {
			if (result != null) {
				currentContact.setValues(result);
				renderTable(result);
			}
		}
	}
	
	/**
	 * <code>CLickHandler</code> welcher für das Editieren eines Kontaktes verantwortlich ist.
	 *
	 */
	class editClickHandler implements ClickHandler {
		public void onClick(ClickEvent ev) {
			e.editContact(currentContact);
		}
	}
	
	/**
	 * <code>ClickHandler</code> welcher das Anzeigen des <code>ExisitingContactCollaborationForm</code> auslöst.
	 *
	 */
	class shareExistingClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			GWT.log(currentContact.getName());
			e.showExistingContactCollab(currentContact);
		}
	}
	
	/**
	 * <code>ClickHandler</code> welcher das Anzeigen der <code>ContactForm</code> auslöst.
	 */
	class shareClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			e.showContactCollab(currentContact);
		}
	}
	
	/**
	 * <code>ClickHanlder</code>Welcher für das Löschen eines Kontaktst verantwortlich ist.
	 *
	 */
	class deleteClickHandler implements ClickHandler {
		public void onClick(ClickEvent ec) {
			editorService.deleteContact(currentContact, u, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					Window.alert("Löschen fehlgeschlagen");
				}

				public void onSuccess(Void v) {
					Window.alert("kontakt gelöscht");
					removeContact();
				}
			});
		}
	}

}
