package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.resource.Resources;
import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.server.LoginInfo;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * @author Philipp
 * 
 * Struktur von @author Thies
 *
 */

public class TreeViewMenu  {
	
	//Neues StackLayoutPanel
	StackLayoutPanel stackPanel = new StackLayoutPanel(Unit.EM);
	
	// Hinzufügen des KontaktlistenTabs
	Widget mailHeade = createHeaderWidget("Kontaktlisten");
			
	/**
	 * Es wird ein Widget also in diesem Beispiel einer "Tab" wie Kontaktlisten erzeugt.
	 * 		
	 * @return
	 */
	private Widget createHeaderWidget(String text, ImageResource image) {
		// Hinzufügen von Bild unt Text zu einem Horizotalen Panel
		HorizontalPanel hPanel = new HorizontalPanel();
	    hPanel.setHeight("100%");
	    hPanel.setSpacing(0);
	    hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    hPanel.add(new Image(image));
	    HTML headerText = new HTML(text);
	    headerText.setStyleName("cw-StackPanelHeader");
	    hPanel.add(headerText);
	    return new SimplePanel(hPanel);
		
	}
	
	private Widget createContactListItem() {
		TreeViewModel model = new ContactListTreeTab();
		CellTree tree = new CellTree(model, "null");
		tree.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		RootLayoutPanel.get().add(tree);

	} 
	
	/**
	 *  Bilder die oben in den Tabs angezeigt werden. 
	 *  
	 */
	
	public interface Images extends Resources {
		
		/*
		 * Hier kommen später die Bilder rein.
		 */
		ImageResource contactLists();
	}
	
	
	
	
	
	

}
