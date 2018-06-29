package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;


import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * 
 * @author Philipp
 * 
 *         Struktur von @author Thies
 *
 */

public class TreeViewMenu {
	Editor e;
	
	ContactListTreeTab contactListTab;
	ContactListTreeTab contactListTab2;
	SharedContactCellListTab sharedContactListTab;
	ContactCellListTab contactTab;
	StackPanel stackPanel;
	CellTree tree;
	ContactCellListTab cellListTab;

	public Widget onLoad() {
		// StackPanel wird erstellt.
		stackPanel = new StackPanel();
		stackPanel.add(createContactListTreeTab(), "Meine Listen");
		stackPanel.add(createContactCellListTab(), "Meine Kontakte");
		stackPanel.add(createSharedContactListTreeTab(), "Meine geteilte Kontakte");
		GWT.log("createdAllTabs");
		//stackPanel.add(new Label("Foo"), "foo");

		stackPanel.ensureDebugId("cwStackPanel");
		return stackPanel;
	}

	public void addContactList(ContactList cl) {
		contactListTab.addContactList(cl);
		for (Contact c : cl.getContacts()) {
			GWT.log("8.1 add Contact " + c.getName() + "to List " + cl.getListName());
			contactListTab.addContactOfList(cl, c);
		}

	}

	public void addContactToList(ContactList cl, Contact c) {
		contactListTab.addContactOfList(cl, c);
	}

	public void addContact(Contact c) {
		contactTab.addContact(c);
	}

	public StackPanel getStackPanel() {
		return this.stackPanel;
	}

	public void setEditor(Editor editor) {
		GWT.log("Editor setzen in tree view");
		GWT.log("Editor: " + editor.hashCode());
		this.e = editor;
		contactListTab.setEditor(editor);
		contactTab.setEditor(editor);
		sharedContactListTab.setEditor(editor);
	}

	public CellList createContactCellListTab() {
		contactTab = new ContactCellListTab();
		return contactTab.createContactTab();
	}
	
	public Widget createContactListTreeTab() {
		this.contactListTab = new ContactListTreeTab();
		CellTree tree = new CellTree(contactListTab, "Root");
		GWT.log("TreeViewMenu: createListTab");
		return tree;
	}
	
	public Widget createSharedContactListTreeTab() {
		this.sharedContactListTab = new SharedContactCellListTab();
		return sharedContactListTab.createContactTab();
		
	}


}


