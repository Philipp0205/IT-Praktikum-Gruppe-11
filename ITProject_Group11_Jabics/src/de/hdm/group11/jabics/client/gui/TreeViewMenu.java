package de.hdm.group11.jabics.client.gui;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

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
	StackPanel stackPanel;
	ContactListTreeTab2 contactListTab2 = new ContactListTreeTab2();
	
//	public Widget onLoad() {
//		
//		//StackLayoutpanel wird erstellt.
//		stackPanel = new StackPanel();
//		
//		contactsTab = new ContactCellListTab();
//		contactListTab = new ContactListTreeTab();
//		
//		stackPanel.add(contactListTab.createTab(), "Meine Listen");
//		stackPanel.add(contactsTab.createTab(), "Meine Kontakte");
//		
//	    stackPanel.ensureDebugId("cwStackLayoutPanel");
//	    return stackPanel;
//	}
	
	private Widget createCLTT2() {
		
		TreeViewModel model = new ContactListTreeTab();
		
		CellTree tree = new CellTree(model, null);
		tree.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		return tree;
		
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
	public StackPanel getStackLayoutPanel() {
		return this.stackPanel;
	}
	public StackPanel createMenu() {
		//StackLayoutpanel wird erstellt.
		stackPanel = new StackPanel();
		
//		contactsTab = new ContactCellListTab();
//		contactListTab = new ContactListTreeTab();
		
		
		stackPanel.add(createCLTT2(), "Meine Listen");
		//stackPanel.add(contactsTab.createTab(), "Meine Kontakte");
		
		stackPanel.add(new Label("111111"), "222222");
		stackPanel.add(new Label("333333"), "4444444");
		
	    return stackPanel;
	}
	
	
	
	
	
	
	
	
	private class ContactListTreeTab2 implements TreeViewModel {
		
		@Override
		public <T> NodeInfo<?> getNodeInfo(T value) {
			  /*
		       * Create some data in a data provider. Use the parent value as a prefix
		       * for the next level.
		       */
		      ListDataProvider<String> dataProvider = new ListDataProvider<String>();
		      for (int i = 0; i < 2; i++) {
		        dataProvider.getList().add(value + "." + String.valueOf(i));
		      }

		      // Return a node info that pairs the data with a cell.
		      return new DefaultNodeInfo<String>(dataProvider, new TextCell());
		    }

		    /**
		     * Check if the specified value represents a leaf node. Leaf nodes cannot be
		     * opened.
		     */

		@Override
		public boolean isLeaf(Object value) {
			// The maximum length of a value is ten characters.
		    return value.toString().length() > 10;
		}
		
	}
}


