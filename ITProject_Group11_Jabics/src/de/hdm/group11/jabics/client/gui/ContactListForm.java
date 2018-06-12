package de.hdm.group11.jabics.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.User;

public class ContactListForm extends VerticalPanel {
	/**
	 * Struktur von
	 * @author Christian Rathke
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Ilg
	 */
	
	
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();
	
	User userToDisplay = null;
	ContactList listToDisplay = null;
	
	
public void onLoad() {
		
		super.onLoad();
		
		Grid contactListGrid = new Grid(6, 1);
		
		Label formName = new Label("Listen-Editor");
		contactListGrid.setWidget(0, 0, formName);
		
		Label listName = new Label(listToDisplay.getName());
		contactListGrid.setWidget(1, 0, listName);		

		HorizontalPanel listAddBox = new HorizontalPanel();
	    Button addListButton = new Button("Neue Liste");
	    addListButton.addClickHandler(new AddListClickHandler());
	    listAddBox.add(addListButton);
	    contactListGrid.setWidget(2, 0, listAddBox);
		
	    HorizontalPanel listShareBox = new HorizontalPanel();
	    Label shareQuestion = new Label("Wollen Sie diese Liste teilen?");
	    listShareBox.add(shareQuestion);
		    
	    Button shareListButton = new Button("Liste teilen");
	    shareListButton.addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		    	// TODO entsprechende Methode aus ContactCollabForm
		    	}
		    }
	    );
	    listShareBox.add(shareListButton);
	    contactListGrid.setWidget(3, 0, listShareBox);	
	    
	    HorizontalPanel listDeleteBox = new HorizontalPanel();
	    Label deleteQuestion = new Label("Wollen Sie diese Liste löschen?");
	    listDeleteBox.add(deleteQuestion);
	    
	    Button deleteListButton = new Button("Liste löschen");
	    deleteListButton.addClickHandler(new DeleteListClickHandler());
	    listDeleteBox.add(deleteListButton);
	    contactListGrid.setWidget(4, 0, listDeleteBox);	
}

	/**
	 * Im Folgenden Code werden Clickhandler und Asynchrone Methodenaufrufe für die Operationen Editieren, Löschen oder Teilen 
	 * eines <code>List</code> Objekts implementiert.
	 * @author Brase
	 * @author Ilg
	 */
	private class DeleteListClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			if(listToDisplay == null) {
				Window.alert("Keine Liste ausgewählt");
			}else {
			editorService.deleteList(listToDisplay, new deleteListCallback(listToDisplay));
			}
		}
	}
	
	

}
