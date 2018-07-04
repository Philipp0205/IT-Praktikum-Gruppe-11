package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;


/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * 
 * @author Philipp
 * 
 * Struktur und Styling von @author Thies
 */
public class TreeViewMenu extends VerticalPanel {
	EditorAdmin e;
	JabicsUser user;

	ContactListTreeTab contactListTab;
	SharedContactCellListTab sharedContactListTab;
	ContactCellListTab contactTab;
	StackPanel stackPanel;
	CellTree tree;
	ContactCellListTab cellListTab;
	
	private CellTreeResources ctRes = GWT.create(CellTreeResources.class);

	public TreeViewMenu(JabicsUser u) {
		stackPanel = new StackPanel();
		stackPanel.add(createContactListTreeTab(u), "Meine Listen");
		stackPanel.add(createContactCellListTab(u), "Alle Kontakte");

		stackPanel.add(createSharedContactListTreeTab(u), "Mir geteilte Kontakte");
		
		stackPanel.setWidth("250px");
		// stackPanel.ensureDebugId("cwStackPanel");

	}

	public void onLoad() {
		this.add(this.stackPanel);
	}

	public void addContactList(ContactList cl) {
		contactListTab.addContactList(cl);

		for (Contact c : cl.getContacts()) {
			GWT.log("8.1 add Contact " + c.getName() + " to List " + cl.getListName());
			contactListTab.addContactOfList(cl, c);
		}
	}

	public void removeContactListFromTree(ContactList cl) {
		contactListTab.removeContactList(cl);
	}

	public void setUser(JabicsUser u) {
		this.user = u;
		// contactListTab.setUser(u);
		contactTab.setUser(u);
		sharedContactListTab.setUser(u);
	}

	public void addContactToList(ContactList cl, Contact c) {
		contactListTab.addContactOfList(cl, c);
	}

	public void removeContactOfContactList(ContactList cl, Contact c) {
		contactListTab.removeContactOfContactList(cl, c);
	}

	public void addContact(Contact c) {
		contactTab.addContact(c);
	}

	public void removeContact(Contact c) {
		contactTab.removeContact(c);
	}

	public void updateContact(Contact c) {
		contactTab.updateContact(c);
		contactListTab.updateContact(c);
		sharedContactListTab.updateContact(c);
	}

	public StackPanel getStackPanel() {
		return this.stackPanel;
	}

	public void setEditor(EditorAdmin editor) {
		GWT.log("Editor setzen in tree view");
		GWT.log("Editor: " + editor.hashCode());
		this.e = editor;
		contactListTab.setEditor(editor);
		contactTab.setEditor(editor);
		sharedContactListTab.setEditor(editor);
	}

	public CellList<Contact> createContactCellListTab(JabicsUser u) {
		this.contactTab = new ContactCellListTab(u, this);
		contactTab.onLoad();
		return contactTab.getCellList();
	}

	public Widget createContactListTreeTab(JabicsUser u) {

		this.contactListTab = new ContactListTreeTab(u, this);
		CellTree tree = new CellTree(contactListTab, "Root", ctRes);	
		tree.setAnimationEnabled(true);

		GWT.log("TreeViewMenu: createListTab");
		
		tree.setStyleName("cellTree");
		return tree;
	}

	public CellList<Contact> createSharedContactListTreeTab(JabicsUser u) {
		this.sharedContactListTab = new SharedContactCellListTab(u, this);
		sharedContactListTab.onLoad();
		return sharedContactListTab.getCellList();
	}

	public void flushContactListsProvider() {
		contactListTab.flushContactListProvider();
	}

	public void clearSelectionModelContactListTab() {
		contactListTab.clearSelectionModel();

	}

	public void clearSelectionModelContactTab() {
		contactTab.clearSelectionModel();
	}

	public void clearSelectionModelSharedContactTab() {
		sharedContactListTab.clearSelectionModel();
	}
	
	public SingleSelectionModel<BusinessObject> getSelectionModelContactListTab() {
		return contactListTab.getSelectionModel();
	}
	
	public SingleSelectionModel<Contact> getSelectionModelContactsTab() {
		return contactTab.getSelectionModel();
	}
	
	public SingleSelectionModel<Contact> getSelectionModelSharedContactsTab() {
		 return sharedContactListTab.getSelectionModel();
	}
	
	public interface CellTreeResources extends CellTree.Resources {
//		@Override
//		@Source("cellTreeClosedItem.gif")
//	    ImageResource cellTreeClosedItem();
//
//	    @Override
//		@Source("cellTreeOpenItem.gif")
//	    ImageResource cellTreeOpenItem();

	    @Override
		@Source("JabicsCellTree.css")
	    CellTree.Style cellTreeStyle(); 
	}
	

}
