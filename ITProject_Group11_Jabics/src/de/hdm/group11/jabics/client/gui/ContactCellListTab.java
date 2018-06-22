package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

public class ContactCellListTab{

	private Contact selectedContact;
	Editor editor;
	JabicsUser user;

	private EditorServiceAsync eService;
	CellList<Contact> contactCell;
	ListDataProvider<Contact> contactDataProvider;
	private ContactKeyProvider keyProvider = null;

	private SingleSelectionModel<Contact> selectionModel = null;

	//private final ArrayList<Contact> allcontacts = cMapper.findAllContacts(loginfo.getCurrentUser());
	ListDataProvider<Contact> contactsProvider = null;
	
	public ContactCellListTab() {

		keyProvider = new ContactKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time."
		selectionModel = new SingleSelectionModel<Contact>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		GWT.log("ContactsConstruct");
		
	}

	public CellList createContactTab() {
		eService = ClientsideSettings.getEditorService();
		
		contactCell = new CellList<Contact>(new ContactCell(), keyProvider);
		
		contactDataProvider = new ListDataProvider<Contact>();
		
		user = new JabicsUser(1);

		/*
		 * Der ListDataProvider wird mit den Kontakten befüllt.
		 */
		JabicsUser user2 = new JabicsUser();
		user2.setId(1);
		eService.getContactsOf(user2, new AsyncCallback<ArrayList<Contact>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Nix.
			}
			@Override
			public void onSuccess(ArrayList<Contact> contacts) {
				GWT.log("CellList: onSuccess");
				
				for (Contact c : contacts) {
					contactDataProvider.getList().add(c);
				}

			}
		});

		contactDataProvider.addDataDisplay(contactCell);
		contactCell.setSelectionModel(selectionModel);
		GWT.log("Contacts1");
		contactDataProvider.flush();
		contactCell.redraw();
		GWT.log("Contacts2");
		return contactCell;
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

	public CellList getCellList() {
		return this.contactCell;
	}
	

	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			BusinessObject selection = selectionModel.getSelectedObject();
			this.setSelectedContact((Contact) selection);
		}

		private void setSelectedContact(Contact c) {
			selectedContact = c;
			editor.showContact(c);
		}
	}

	public void setEditor(Editor editor) {
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
