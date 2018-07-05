package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.resource.JabicsResources;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class SharedContactCellListTab extends Widget {
	
	EditorAdmin editor;
	JabicsUser user;
	
	TreeViewMenu treeViewMenu;

	private EditorServiceAsync eService;
	CellList<Contact> contactCell;
	ListDataProvider<Contact> contactDataProvider;
	private ContactKeyProvider keyProvider = null;

	private SingleSelectionModel<Contact> selectionModel = null;

	//private final ArrayList<Contact> allcontacts = cMapper.findAllContacts(loginfo.getCurrentUser());
	ListDataProvider<Contact> contactsProvider;
	
	public SharedContactCellListTab(JabicsUser u) {
		this.user = u;
		
		keyProvider = new ContactKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time."
		selectionModel = new SingleSelectionModel<Contact>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		GWT.log("ContactsConstructor");
		
		GWT.log("4.1 createContactTab");
		eService = ClientsideSettings.getEditorService();
		
		contactCell = new CellList<Contact>(new ContactCell(), keyProvider);
		contactDataProvider = new ListDataProvider<Contact>();
		
		contactsProvider = new ListDataProvider<Contact>();
	}
	
	public SharedContactCellListTab(JabicsUser u, TreeViewMenu tvm) {
		this.user = u;
		this.treeViewMenu = tvm;
		
		keyProvider = new ContactKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time."
		selectionModel = new SingleSelectionModel<Contact>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		GWT.log("ContactsConstructor");
		
		GWT.log("4.1 createContactTab");
		eService = ClientsideSettings.getEditorService();
		
		contactCell = new CellList<Contact>(new ContactCell(), keyProvider);
		contactDataProvider = new ListDataProvider<Contact>();
		
		contactsProvider = new ListDataProvider<Contact>();
		
		
		
	}
	
	public CellList<Contact> getCellList(){
		return this.contactCell;
	}

	public void onLoad() {
		/*
		 * Der ListDataProvider wird mit den Kontakten befüllt.
		 */
		GWT.log("2.1 User: " + user.getId());
		
		eService.getAllSharedContactsOf(user, new AsyncCallback<ArrayList<Contact>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("4.1 CellList onFailure" + caught.toString());
			}
			@Override
			public void onSuccess(ArrayList<Contact> contacts) {
				if (contacts != null) {
					GWT.log("4.1 CellList onSuccess");
					
					for (Contact c : contacts) {
						contactDataProvider.getList().add(c);
						contactDataProvider.refresh();
						contactDataProvider.flush();
					}
				}
			}
		});

		contactDataProvider.addDataDisplay(contactCell);
		contactCell.setSelectionModel(selectionModel);
		GWT.log("Contacts1");
		contactDataProvider.flush();
		contactCell.redraw();
		GWT.log("Contacts2");

	}

	private class ContactKeyProvider implements ProvidesKey<Contact> {
		/*
		 * Der Key provider für einen Kontakt sorgt dafür, dass die Auswahl in der
		 * CellList gleich bleibt auch wenn das Objekt sich ändert.
		 */
		@Override
		public Object getKey(Contact c) {
			// Zurückgeben das unique Key von dem Objekt.
			return (c == null) ? null : c.getId();
		}
	}

	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			BusinessObject selection = selectionModel.getSelectedObject();
			this.setSelectedContact((Contact) selection);
			
			treeViewMenu.clearSelectionModelContactListTab();
			treeViewMenu.clearSelectionModelContactTab();
			
			

		}

		private void setSelectedContact(Contact c) {
			GWT.log("4.1 Kontakt anzeigen" + c.getName());
			editor.showContact(c);
		}
		

	}

	public void setEditor(EditorAdmin editor) {
		GWT.log("Editor setzen in contactCellListTab");
		GWT.log("Editor: " + editor.hashCode());
		this.editor = editor;
	}
	public void setUser(JabicsUser u) {
		GWT.log("User setzen in contactCellListTab");
		this.user = u;
	}
	
	public void addContact(Contact c) {
		contactDataProvider.getList().add(c);
		contactDataProvider.flush();
		selectionModel.setSelected(c, true);
	}

	public void removeContact(Contact c) {
		contactDataProvider.getList().remove(c);
		contactDataProvider.flush();
	}

	public void updateContact(Contact c) {
		for (Contact ci : contactDataProvider.getList()) {
			if (c.getId() == ci.getId()) {
				contactDataProvider.getList().set(0, c);
				break;
			}
		}
		contactDataProvider.refresh();
	}
	
	public void clearSelectionModel() {
		if (selectionModel != null) {
			this.selectionModel.clear();
		} else return;

	}
	
	public SingleSelectionModel<Contact> getSelectionModel() {
		return this.selectionModel;
	}
	
	public class AsyncDataProvider extends AbstractCell<Contact> {

		@Override
		public void render(Context context, Contact value, SafeHtmlBuilder sb) {
			if (value == null) {
				//sb.appendHtmlConstant("<div>");
				sb.appendEscaped(value.getName());
				//sb.appendHtmlConstant("</div>");
			}
		}

	}

}
