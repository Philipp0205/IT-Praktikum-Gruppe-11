package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
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
	ContactCellListTab contactTab;
	StackPanel stackPanel;
	CellTree tree;

	public Widget onLoad() {
		// StackPanel wird erstellt.
		stackPanel = new StackPanel();
		stackPanel.add(createContactListTreeTab(), "Meine Listen");
		//stackPanel.add(createContactCellListTab(), "Meine Kontakte");
		GWT.log("createdAllTabs");
		//stackPanel.add(new Label("Foo"), "foo");

		stackPanel.ensureDebugId("cwStackPanel");
		return stackPanel;
	}

	public void addContactList(ContactList cl) {
		contactListTab.addContactList(cl);
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
		//contactsTab.setEditor(editor);
	}

	public Widget createContactCellListTab() {
		contactTab = new ContactCellListTab();
		return contactTab.createContactTab();
	}
	
	public Widget createContactListTreeTab() {
		this.contactListTab = new ContactListTreeTab();
		CellTree tree = new CellTree(contactListTab, "Root");
		GWT.log("TreeViewMenu: createListTab");
		return tree;
	}

}


