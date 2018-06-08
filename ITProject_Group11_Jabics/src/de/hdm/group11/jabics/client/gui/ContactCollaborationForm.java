package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

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
	ArrayList<User> allUser = new ArrayList<User>();
	ArrayList<User> finalUser = new ArrayList<User>();
	
	Button shareContact = new Button("Kontakt freigeben");
	Button exit = new Button("Abbrechen");
	Button addButton = new Button("Nutzer hinzuf�gen");
	
	
	MultiWordSuggestOracle  oracle;
	SuggestBox sug; // = new SuggestBox();
	
	CellTable<User> selUser;
	ListDataProvider<User> ldp;
	User selectedUserAdd = null;
	User selectedUserRemove = null;
	
	CellTable<PValue> selValues;
	ListDataProvider<PValue> valueProvider;



	
	public void onLoad() {
		//SuggestOracle oracle = 
		retrieveUser();
		createSuggestBox();
		createPValueBox();
	}
	
	//selUser.getResources und getRowElement
	
	public void createSuggestBox() {
		/**
		 * Tabelle erstellen, die ausgewählte Nutzer anzeigt.
		 */
		ldp = new ListDataProvider<User>();
		selUser = new CellTable<User>();
		ldp.addDataDisplay(selUser);
		TextCell s = new TextCell();
		SingleSelectionModel<User> selectionModel  = new SingleSelectionModel<User>();
		
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
		TextColumn<User> username = new TextColumn<User>() {
			public String getValue(User u) {
				return u.getUsername();
			}
		};
		selUser.addColumn(username, "Nutzer");
		
		/**
		 * SuggestBox hinzufügen und mit Optionen befüllen
		 */
		oracle = new MultiWordSuggestOracle();
		sug = new SuggestBox(oracle);
		for (User u : allUser) {
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
				for (User u : allUser) {
					if (sug.getValue().contains(u.getUsername()) && sug.getValue().contains(u.getEmail())){
							selectedUserAdd = u;
							}
					}
				}
			});
		sug.setLimit(5);
		
		//hier muss noch die Suggestbox in die form eingefügt werden.
	}
	
	public void createPValueBox() {
		PValue selectedPV;
		selValues = new CellTable<PValue>();
		valueProvider = new ListDataProvider<PValue>();
		valueProvider.addDataDisplay(selValues);
		// Es kann sein, dass hier noch kexprovider benötigt werden
		MultiSelectionModel<PValue> selectionModel  = new MultiSelectionModel<PValue>();
		
		/* wird wahrscheinlich auch nicht gebraucht
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			         public void onSelectionChange(SelectionChangeEvent event) {
			            // TODO
			        	 selectedPV = selectionModel.getSelectedObjects();
			         }
			      });
		*/
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
	
	public void shareContacts() {
		for (User u: ldp.getList()) {
			editorService.addCollaboration(sharedContact, u, new AddCollaborationCallback());
		}
	}
	
	private void retrieveUser() {
		editorService.getAllUsers(new GetAllUsersCallback());
	}
	public void setAllUser(ArrayList<User> user) {
		this.allUser = user;
	}
	
	private class AddCollaborationCallback implements AsyncCallback<Void>{
		public void onFailure(Throwable arg0) {
			Window.alert("Kontakt konnte nicht geteilt werden");
		}
		
		public void onSuccess(Void v) {
			Window.alert("Kontakt erolgreich geteilt!");
			/**
			 * TODO: nach erfolgreichem teilen zur�ckkehren zur anzeige des kontakts.
			 */
		}
	}
	
	private class GetAllUsersCallback implements AsyncCallback<ArrayList<User>>{
		
		public void onFailure(Throwable arg0) {
			Window.alert("Nutzer konnten nicht geladen werden");
		}
		
		public void onSuccess(ArrayList<User> user) {

			setAllUser(user);
		}
	}
}
