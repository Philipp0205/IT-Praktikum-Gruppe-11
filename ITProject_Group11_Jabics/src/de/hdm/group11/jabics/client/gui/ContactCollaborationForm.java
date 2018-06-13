package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.*;
import de.hdm.group11.jabics.shared.bo.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.cell.client.*;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.dom.client.Style.Unit;


public class ContactCollaborationForm extends HorizontalPanel{
	
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();
	
	Contact sharedContact;
	ArrayList<JabicsUser> allUser = new ArrayList<JabicsUser>();
	ArrayList<JabicsUser> finalUser = new ArrayList<JabicsUser>();
	
	Button shareContact = new Button("Kontakt freigeben");
	Button exit = new Button("Abbrechen");
	Button addButton = new Button("Nutzer hinzufügen");
	
	
	MultiWordSuggestOracle  oracle;
	SuggestBox sug; // = new SuggestBox();
	
	CellTable<JabicsUser> selUser;
	ListDataProvider<JabicsUser> ldp;
	JabicsUser selectedUserAdd = null;
	JabicsUser selectedUserRemove = null;
	
	HashSet<PValue> finalPV = new HashSet<PValue>();
	CellTable<PValue> selValues;
	ListDataProvider<PValue> valueProvider;

	AbsolutePanel ap;

	
	public void onLoad(Contact c) {
		//SuggestOracle oracle =
		this.sharedContact = c;
		shareContact.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				shareContact();
			}
		});
		exit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {/**
			 * TODO: implement zurückkehren zur kontaktanzeige
			 */}
		});
		retrieveUser();
		createSuggestBox();
		createPValueBox(sharedContact.getValues());
		ap = new AbsolutePanel();
		ap.setSize("500px", "400px");
		ap.add(selUser, 0, 0);
		ap.add(addButton, 200, 0);
		ap.add(shareContact, 450, 350);
		ap.add(exit, 20, 350);
		ap.add(selValues, 250, 0);
	}
	
	//selUser.getResources und getRowElement
	
	public void createSuggestBox() {
		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		ldp = new ListDataProvider<JabicsUser>();
		selUser = new CellTable<JabicsUser>();
		ldp.setList(allUser);
		ldp.addDataDisplay(selUser);
		TextCell s = new TextCell();
		SingleSelectionModel<JabicsUser> selectionModel  = new SingleSelectionModel<JabicsUser>();
		
		selectionModel.addSelectionChangeHandler(
			      new SelectionChangeEvent.Handler() {
			         public void onSelectionChange(SelectionChangeEvent event) {
			            selectedUserRemove = selectionModel.getSelectedObject();
			         }
			      });
		selUser.setSelectionModel(selectionModel);
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (selectedUserAdd != null) finalUser.add(selectedUserAdd);
				ldp.setList(finalUser);
				ldp.refresh();
				ldp.flush();
			}
		});
		
		Button removeButton = new Button();
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if(selectedUserRemove != null) {
					finalUser.remove(selectedUserRemove);
					ldp.setList(finalUser);
					ldp.flush();
				}	
			}
		});
		TextColumn<JabicsUser> username = new TextColumn<JabicsUser>() {
			public String getValue(JabicsUser u) {
				return u.getUsername();
			}
		};
		selUser.addColumn(username, "Nutzer");
		
		/**
		 * SuggestBox hinzufügen und mit Optionen befüllen
		 */
		oracle = new MultiWordSuggestOracle();
		sug = new SuggestBox(oracle);
		for (JabicsUser u : allUser) {
			try {
			oracle.add(u.getUsername() + " " + u.getEmail());
			} catch (NullPointerException e){
				Window.alert("setzen des nutzernamens oder mailadresse in sugstbox failed, Nutzer mit Id: " + u.getId());
				try {
					oracle.add(u.getUsername());
				} catch (NullPointerException ex){
					Window.alert("setzen des nutzernamens auch fehlgeschlagen, Nutzer mit Id: " + u.getId());
				}
			}
		}
		/**
		 * selectionHandler, der den hinzuzufügenden Nutzer setzt, sobald einer durch die suggestbox ausgewählt wurde.
		 * Dieser wird durch Klick auf den button "Nutzer hinzufügen" zur liste der zu teilenden Nutzer hinzugefügt
		 */
		sug.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> sel){
				for (JabicsUser u : allUser) {
					if (sug.getValue().contains(u.getUsername()) && sug.getValue().contains(u.getEmail())){
							selectedUserAdd = u;
							}
					}
				}
			});
		sug.setLimit(5);
		
		//hier muss noch die Suggestbox in die form eingefügt werden.
	}
	
	public void createPValueBox(ArrayList<PValue> pv) { 
		selValues = new CellTable<PValue>();
		valueProvider = new ListDataProvider<PValue>();
		valueProvider.setList(pv);
		valueProvider.addDataDisplay(selValues);
		// Es kann sein, dass hier noch kexprovider benötigt werden
		MultiSelectionModel<PValue> selectionModel  = new MultiSelectionModel<PValue>();
		
		// Bei Auswahl ausgewählte PValues inf finalPV speichern
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			         public void onSelectionChange(SelectionChangeEvent event) {
			        	 finalPV = (HashSet<PValue>) selectionModel.getSelectedSet();
			        	 
			        	 Window.alert("Auswahl geändert");
			         }
			      });
		selValues.setSelectionModel(selectionModel);
		
		/* Wenn funktional kann dieser code gelöscht werden
		CheckboxCell check = new CheckboxCell(true, false) {
		      public Boolean getValue(PValue object) {
		        return selectionModel.isSelected(object);
		      }
		    };
		TextCell prop = new TextCell() {
			public String getValue(PValue object) {
		        return object.getProperty().getLabel();
		      }
		}; */
		
		
		Column<PValue, Boolean> checkbox = new Column<PValue, Boolean>(new CheckboxCell(true, false)){
			public Boolean getValue(PValue object) {
		        return selectionModel.isSelected(object);
		      }
		};
		Column<PValue, String> property = new Column<PValue, String>(new TextCell()) {
			public String getValue(PValue object) {
		        return object.getProperty().getLabel();
		      }
		};
		Column<PValue, String> propertyvalue = new Column<PValue, String>(new TextCell()) {
			public String getValue(PValue object) {
		        return object.toString();
		      }
		};
		
		selValues.addColumn(checkbox, "Auswahl");
		selValues.setColumnWidth(checkbox, 50, Unit.PX);
		selValues.addColumn(property, "Merkmal");
		selValues.setColumnWidth(property, 30, Unit.EM);
		selValues.addColumn(propertyvalue, "Wert");
		selValues.setColumnWidth(propertyvalue, 50, Unit.EM);
	}
	
	/**
	 * Führt den RPC zur freigabe einens Kontakts mit den ausgewählten Parametern durch
	 */
	public void shareContact() {
		if (!finalUser.isEmpty()) {
			/*
			 * oder aber: for (User u: ldp.getList()) {
			 */
			for (JabicsUser u : finalUser) {
				for(PValue pv: finalPV) {
					editorService.addCollaboration(pv, u, new AddPVCollaborationCallback());
				}
				editorService.addCollaboration(sharedContact, u, new AddContactCollaborationCallback());
			}
		}
		
	}
	
	private void retrieveUser() {
		editorService.getAllUsers(new GetAllUsersCallback());
	}
	public void setAllUser(ArrayList<JabicsUser> user) {
		this.allUser = user;
	}
	
	private class AddPVCollaborationCallback implements AsyncCallback<Void>{
		public void onFailure(Throwable arg0) {
			Window.alert("PV konnte nicht geteilt werden");
		}
		
		public void onSuccess(Void v) {
			Window.alert("PV erfolgreich geteilt!");
		}
	}
	private class AddContactCollaborationCallback implements AsyncCallback<Void>{
		public void onFailure(Throwable arg0) {
			Window.alert("Kontakt konnte nicht geteilt werden");
		}
		
		public void onSuccess(Void v) {
			Window.alert("Kontakt erolgreich geteilt!");
			/**
			 * TODO: nach erfolgreichem teilen zurückkehren zur Anzeige des kontakts.
			 */
		}
	}
	
	private class GetAllUsersCallback implements AsyncCallback<ArrayList<JabicsUser>>{
		
		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}
		
		public void onSuccess(ArrayList<JabicsUser> user) {

			setAllUser(user);
		}
	}
}
