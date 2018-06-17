package de.hdm.group11.jabics.client.gui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * @author Philipp
 * 
 * Struktur von @author Thies
 *
 */

public class TreeViewMenu  {
	
	public Widget onLoad() {
		
		//StackLayoutpanel wird erstellt.
		StackLayoutPanel stackPanel = new StackLayoutPanel(Unit.EM);
		
		ContactCellListTab contactsTab = new ContactCellListTab();
		ContactListTreeTab contactListTab = new ContactListTreeTab();
		
		stackPanel.add(new HTML("Meine Listen"), new HTML("Meine Listen"), 4);
		stackPanel.add(contactListTab.createTab(), new HTML("Meine Listen"), 4);
		stackPanel.add(contactsTab.createTab(), new HTML("Meine Kontakte"), 4);
		stackPanel.add(new HTML("Meine geteilten Kontakte"), new HTML("Meine geteilten Kontakte"), 4);
		
	    stackPanel.ensureDebugId("cwStackLayoutPanel");
	    return stackPanel;
	}
	
	

	

	
	
	
	
	
	

}
