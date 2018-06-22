package de.hdm.group11.jabics.client.gui;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;

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
		stackPanel.add(createContactListTreeTab(), "TreeView");
		//stackPanel.add(createContactListTreeTab(), "TreeView");
		stackPanel.add(new Label("Foo"), "foo");

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

	public Widget createTreeTab() {
		contactTab = new ContactCellListTab();
		tree = new CellTree(contactTab, "Root");
		GWT.log("TreeViewMenu: createContactTab");
		return tree;
	}
	
	public Widget createContactListTreeTab() {
		this.contactListTab = new ContactListTreeTab();
		CellTree tree = new CellTree(contactListTab, "Root");
		GWT.log("TreeViewMenu: createListTab");
		return tree;
	}
	
	public Widget createContactCellTreeTab() {
		
			/**
		   * The list of data to display.
		   */
			  final List<String> DAYS = Arrays.asList("Sunday", "Monday",
			      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
			  
			// Create a cell to render each value.
			    TextCell textCell = new TextCell();

		    // Create a CellList that uses the cell.
		    CellList<String> cellList = new CellList<String>(textCell);
		    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		    // Add a selection model to handle user selection.
		    final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		    cellList.setSelectionModel(selectionModel);
		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		        String selected = selectionModel.getSelectedObject();
		        if (selected != null) {
		          Window.alert("You selected: " + selected);
			        }
			      }
			    });
		    
		    // Set the total row count. This isn't strictly necessary, but it affects
		    // paging calculations, so its good habit to keep the row count up to date.
		    cellList.setRowCount(DAYS.size(), true);

		    // Push the data into the widget.
		    cellList.setRowData(0, DAYS);
		    
		    return cellList;		
		
	}
	
	public Widget createContactCellTreeTab2() {
		
		GWT.log("TreeView createContactCellTreeTab2");
		
//		 ProvidesKey<Contact> keyProvider = new ProvidesKey<Contact>() {
//	         public Object getKey(Contact item) {
//	            // Always do a null check.
//	            return (item == null) ? null : item.
//	         }
//	      };
		
		
		CellList<Contact> cellList = new CellList<Contact>(new ContactCell());
		
		final ListDataProvider<Contact> contactsDataProvider = new ListDataProvider<Contact>();
		contactsDataProvider.addDataDisplay(cellList);
		
		return cellList;
		
	}

	private static class CustomTreeModel implements TreeViewModel {

		/**
		 * Get the {@link NodeInfo} that provides the children of the specified value.
		 */
		public <T> NodeInfo<?> getNodeInfo(T value) {
			/*
			 * Create some data in a data provider. Use the parent value as a prefix for the
			 * next level.
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
		public boolean isLeaf(Object value) {
			// The maximum length of a value is ten characters.
			return value.toString().length() > 10;
		}
	}
	
	private class CellListExample {
		

	}


}


