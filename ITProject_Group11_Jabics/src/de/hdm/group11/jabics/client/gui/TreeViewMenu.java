package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.CellTableResources;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * 
 * @author Philipp
 * 
 *         Struktur und Styling von @author Thies
 */
public class TreeViewMenu extends VerticalPanel {
	EditorAdmin e;
	JabicsUser user;

	ContactListTreeTab contactListTab;
	SharedContactCellListTab sharedContactListTab;
	ContactCellListTab contactTab;
	StackPanel stackPanel1;
	StackPanel stackPanel2;
	CellTree tree;
	ContactCellListTab cellListTab;
	
	SafeHtmlBuilder builder;
	SafeHtml safeHtml;

	private CellTreeResources ctRes = GWT.create(CellTreeResources.class);

	public TreeViewMenu(JabicsUser u) {

		String tip = new String("▶");
		Label tip2 = new Label("tip");
		
		safeHtml = SafeHtmlUtils.fromString("X ");

		stackPanel1 = new StackPanel();
		stackPanel2 = new StackPanel();
		stackPanel1.add(createContactListTreeTab(u));
		
		stackPanel2.add(new Label(""), safeHtml);
		stackPanel2.add(createContactCellListTab(u), " ▶ Alle Kontakte");
		stackPanel2.add(createSharedContactListTreeTab(u), " ▶  Mir geteilte Kontakte");
		stackPanel2.setStyleName("stackPanel2");
		stackPanel1.setStyleName("stackPanel1");
		
		stackPanel2.setWidth("250px");
		stackPanel1.setWidth("250px");
//		stackPanel1.getWidget(0).setStyleName("MeineListen");
		// stackPanel.ensureDebugId("cwStackPanel");
	}

	public void onLoad() {
		this.add(this.stackPanel1);
		this.add(this.stackPanel2);

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
	
	public void updateContactListInTree(ContactList cl) {
		contactListTab.updateContactList(cl);
	}

	public void setUser(JabicsUser u) {
		this.user = u;
		contactListTab.setUser(u);
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

	public StackPanel getStackPanel1() {
		return this.stackPanel1;
	}

	public StackPanel getStackPanel2() {
		return this.stackPanel2;

	}

	public void setEditor(EditorAdmin editor) {
		GWT.log("Editor setzen in tree view");
		GWT.log("Editor: " + editor.hashCode());
		this.e = editor;

		contactListTab.setEditor(editor);
		contactTab.setEditor(editor);
		sharedContactListTab.setEditor(editor);
	}

	public Widget createContactCellListTab(JabicsUser u) {
		this.contactTab = new ContactCellListTab(u, this, clRes);
		contactTab.onLoad();
		return contactTab.getCellList();
	}
	
	//cellList Ressourcen
	private CellListResources clRes = GWT.create(CellListResources.class);
	public interface CellListResources extends CellList.Resources {

		@Source("JabicsCellList.css")
		CellList.Style cellListStyle();
	}

	public Widget createContactListTreeTab(JabicsUser u) {

		this.contactListTab = new ContactListTreeTab(u, this);
		CellTree tree = new CellTree(contactListTab, "Root", ctRes);
		tree.setAnimationEnabled(true);

		GWT.log("TreeViewMenu: createListTab");

		return tree;
	}

	public Widget createSharedContactListTreeTab(JabicsUser u) {
		this.sharedContactListTab = new SharedContactCellListTab(u, this, clRes);
		sharedContactListTab.onLoad();
		return sharedContactListTab.getCellList();
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

	public void removeContactsTab() {
		stackPanel2.remove(0);
	}

	public void removeSharedContactsTab() {
		stackPanel2.remove(1);
	}

	public void removeAllTabsOfStackPanel2() {
		stackPanel2.remove(0);
		stackPanel2.remove(0);
	}

	public void showEmptyTab() {
		stackPanel2.showStack(0);
	}

	public interface CellTreeResources extends CellTree.Resources {
		@Override
		@Source("cellTreeClosedItem.png")
	    ImageResource cellTreeClosedItem();

	    @Override
		@Source("cellTreeOpenItem.png")
	    ImageResource cellTreeOpenItem();

		@Override
		@Source("JabicsCellTree.css")
		CellTree.Style cellTreeStyle();
	}

}
