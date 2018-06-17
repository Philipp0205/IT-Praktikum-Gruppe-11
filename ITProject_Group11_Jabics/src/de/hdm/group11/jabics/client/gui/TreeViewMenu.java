package de.hdm.group11.jabics.client.gui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * @author Philipp
 * 
 * Struktur von @author Thies
 *
 */

public class TreeViewMenu  {
	ContactListTreeTab contactListTab;
	ContactCellListTab contactsTab;
	StackLayoutPanel stackPanel;
	
	public Widget onLoad() {
		
		//StackLayoutpanel wird erstellt.
		stackPanel = new StackLayoutPanel(Unit.EM);
		
		contactsTab = new ContactCellListTab();
		contactListTab = new ContactListTreeTab();
		
		stackPanel.add(new HTML("Meine Listen"), new HTML("Meine Listen"), 4);
		stackPanel.add(contactListTab.createTab(), new HTML("Meine Listen"), 4);
		stackPanel.add(contactsTab.createTab(), new HTML("Meine Kontakte"), 4);
		stackPanel.add(new HTML("Meine geteilten Kontakte"), new HTML("Meine geteilten Kontakte"), 4);
		
	    stackPanel.ensureDebugId("cwStackLayoutPanel");
	    return stackPanel;
	}
	
	public void addContactList(ContactList cl) {
		contactListTab.addContactList(cl);
	}
	public void addContactToList(ContactList cl, Contact c) {
		contactListTab.addContactOfList(cl, c);
	}
	public void addContact(Contact c) {
		contactsTab.addContact(c);
	}
	public StackLayoutPanel getStackLayoutPanel() {
		return this.stackPanel;
	}
}
