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

public class ContactListCollaborationForm extends HorizontalPanel{

		Editor e;
		EditorServiceAsync editorService = ClientsideSettings.getEditorService();
		
		ContactList sharedContactList;
		ArrayList<JabicsUser> allUser = new ArrayList<JabicsUser>();
		ArrayList<JabicsUser> finalUser = new ArrayList<JabicsUser>();
		
		Button shareContactList, exit, addButton, removeButton;
		
		JabicsUser selectedUserAdd, selectedUserRemove;
		
		MultiWordSuggestOracle  oracle;
		SuggestBox sug; // = new SuggestBox();
		
		TextColumn<JabicsUser> username;
		CellTable<JabicsUser> selUser;
		ListDataProvider<JabicsUser> ldp;
		SingleSelectionModel<JabicsUser> selectionModel;
		
		HashSet<Contact> finalC = new HashSet<Contact>();
		MultiSelectionModel<Contact> multiSelectionModel;
		CellTable<Contact> selValues;
		ListDataProvider<Contact> valueProvider;
		
		Column<Contact, Boolean> checkbox;
		Column<Contact, String> contact;
		
		AbsolutePanel ap;
		
		public void onLoad() {
			//SuggestOracle oracle = 
			retrieveUser();
			createSuggestBox();
			createContactBox();
			
			shareContactList = new Button("Kontaktliste freigeben");
			shareContactList.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent ev) {
					shareContactList();
					e.returnToContactListForm(sharedContactList);
				}
			});
			
			exit = new Button("Abbrechen");
			exit.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent ev) {
					e.returnToContactListForm(sharedContactList);
				 }
			});
			
			ap = new AbsolutePanel();
			ap.setSize("500px", "400px");
			ap.add(sug, 20, 10);
			ap.add(selUser, 0, 50);
			ap.add(addButton, 200, 0);
			ap.add(shareContactList, 450, 350);
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
			ldp.addDataDisplay(selUser);
			selectionModel  = new SingleSelectionModel<JabicsUser>();
			
			selectionModel.addSelectionChangeHandler(
				      new SelectionChangeEvent.Handler() {
				         public void onSelectionChange(SelectionChangeEvent event) {
				            selectedUserRemove = selectionModel.getSelectedObject();
				         }
				      });
			selUser.setSelectionModel(selectionModel);
			
			addButton = new Button("Nutzer hinzufügen");
			addButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent e) {
					if (selectedUserAdd != null) finalUser.add(selectedUserAdd);
					ldp.setList(finalUser);
					ldp.refresh();
					ldp.flush();
				}
			});
			
			removeButton = new Button("Entfernen");
			removeButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent e) {
					if(selectedUserRemove != null) {
						finalUser.remove(selectedUserRemove);
						ldp.setList(finalUser);
						ldp.flush();
					}	
				}
			});
			
			username = new TextColumn<JabicsUser>() {
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
		
		public void createContactBox() {
			//PValue selectedPV;
			selValues = new CellTable<Contact>();
			valueProvider = new ListDataProvider<Contact>();
			valueProvider.addDataDisplay(selValues);
			// Es kann sein, dass hier noch kexprovider benötigt werden
			multiSelectionModel  = new MultiSelectionModel<Contact>();
			
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		         public void onSelectionChange(SelectionChangeEvent event) {
		        	 finalC = (HashSet<Contact>) multiSelectionModel.getSelectedSet();
		        	 Window.alert("Auswahl geändert");
		         }
		      });
			
			selValues.setSelectionModel(multiSelectionModel);
			
			checkbox = new Column<Contact, Boolean>(new CheckboxCell(true, false)){
				public Boolean getValue(Contact object) {
			        return multiSelectionModel.isSelected(object);
			      }
			};
			contact = new Column<Contact, String>(new TextCell()) {
				public String getValue(Contact object) {
			        return object.toString();
			      }
			};
			
			selValues.addColumn(checkbox, "Auswahl");
			selValues.setColumnWidth(checkbox, 50, Unit.PX);
			selValues.addColumn(contact, "Kontakt");
			selValues.setColumnWidth(contact, 50, Unit.EM);
		}
		
		public void shareContactList() {
			if (!finalUser.isEmpty()) {
				/*oder aber: for (User u: ldp.getList()) {*/
				for (JabicsUser u : finalUser) {
					for(Contact c: finalC) {
						editorService.addCollaboration(c, u, new AddContactCollaborationCallback());
					}
					editorService.addCollaboration(sharedContactList, u, new AddContactListCollaborationCallback());
				}
			}
			
		}
		
		private void retrieveUser() {
			editorService.getAllUsers(new GetAllUsersCallback());
		}
		public void setAllUser(ArrayList<JabicsUser> user) {
			this.allUser = user;
		}
		public void setEditor(Editor e) {
			this.e = e;
		}
		public void setList(ContactList cl) {
			this.sharedContactList = cl;
		}
		private class AddContactCollaborationCallback implements AsyncCallback<Void>{
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
		private class AddContactListCollaborationCallback implements AsyncCallback<Void>{
			public void onFailure(Throwable arg0) {
				Window.alert("Kontaktliste konnte nicht geteilt werden");
			}
			
			public void onSuccess(Void v) {
				Window.alert("Kontaktliste erolgreich geteilt!");
				/**
				 * TODO: nach erfolgreichem teilen zur�ckkehren zur anzeige des kontakts.
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

