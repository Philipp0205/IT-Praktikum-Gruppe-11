package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTree;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;

public class Jabics implements EntryPoint {

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
