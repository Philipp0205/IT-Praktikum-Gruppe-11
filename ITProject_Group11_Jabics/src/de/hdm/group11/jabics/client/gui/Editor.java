package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;

public class Editor implements EntryPoint {

	private VerticalPanel vPanel = new VerticalPanel();
	
	/*
	public ContactPage() {
		initWidget(this.vPanel);
		private String btnStyle = "standardButton";
		
		final Button createContactButton = new Button("Kontakte erstellen");
		createContactButton.setStylePrimaryName(btnStyle);
		this.vPanel.add(createContactButton);
		
		
	}	
	*/
	
	static interface JabicsTreeResources extends CellTree.Resources {
		//Close und Open Items einfügen
		
	}
	
	EditorServiceAsync editorAdmin = null;
	
	@Override
	public void onModuleLoad() {
		
		if(editorAdmin == null) {
			editorAdmin = ClientsideSettings.getEditorService();
		}
		
		
	}
}
