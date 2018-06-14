package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;

/*
 * In der folgenden Klasse "Editor" wird die Darstellung einzelnen Klassen bestimmt:
 * ContactForm, ContactListForm
 */


public class Editor implements EntryPoint {

	
	/*
	public ContactPage() {
		initWidget(this.vPanel);
		private String btnStyle = "standardButton";
		
		final Button createContactButton = new Button("Kontakte erstellen");
		createContactButton.setStylePrimaryName(btnStyle);
		this.vPanel.add(createContactButton);
		
		
	}	
	*/
	
	
	/*
	 * Im folgenden Interface werden die Items, öffnen und schlie0en, hinzugefügt.
	 */
	static interface JabicsTreeResources extends CellTree.Resources {
		@Override
		@Source("cellTreeClosedItem.gif")
		ImageResource cellTreeClosedItem();
		
		@Override
		@Source("cellTreeOpenItem.gif")
		ImageResource cellTreeOpenItem();
		
		@Override
		@Source("JabicsCellTree.css")
		CellTree.Style cellTreeStyle();
		
	}
	
	EditorServiceAsync editorAdmin = null;
	
	HorizontalPanel hPanel = new HorizontalPanel();
	VerticalPanel buttonPanel = new VerticalPanel();
	VerticalPanel contactDetailPanel = new VerticalPanel();
	
	//BOForm bForm = new BOForm();
	//ContactCollaborationForm ccForm = new ContactCollaborationForm();
	//ContactListCollaborationForm clcForm = new ContactListCollaborationForm();

	ContactForm cForm = new ContactForm();
	ContactListForm clForm = new ContactListForm();
	SearchForm sForm = new SearchForm();
	TreeViewMenu treeViewMenu = new TreeViewMenu();
	
	@Override
	public void onModuleLoad() {
		
		if(editorAdmin == null) {
			editorAdmin = ClientsideSettings.getEditorService();
		}
		
		
		hPanel.add(buttonPanel);
		
		Button showCForm = new Button("Kontakt anzeigen");
		showCForm.addClickHandler(new ShowCFormClickHandler());
		Button showClForm = new Button("Liste Anzeigen");
		showClForm.addClickHandler(new ShowClFormClickHandler());
		
		buttonPanel.add(showCForm);
		buttonPanel.add(showClForm);
		
		
	}
	
	/*
	 * ClickHandler um die ContactForm anzuzeigen
	 */
	
	private class ShowCFormClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			treeViewMenu.setContactForm(cForm);
			//cForm.setTreeViewMenu(treeViewMenu);
			
			hPanel.add(contactDetailPanel);
			
			contactDetailPanel.clear();
			contactDetailPanel.add(cForm);
			
			RootPanel.get("details").add(contactDetailPanel);
			
		}
	}
	
	/*
	 * ClickHandler um die ContactListForm anzuzeigen
	 */
	
	private class ShowClFormClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			treeViewMenu.setContactListForm(clForm);
			//clForm.setTreeViewMenu(treeViewMenu);
			
			hPanel.add(contactDetailPanel);
			
			contactDetailPanel.clear();
			contactDetailPanel.add(clForm);
			
			RootPanel.get("details").add(contactDetailPanel);
			
		}
	}

}
