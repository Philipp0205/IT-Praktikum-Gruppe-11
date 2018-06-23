package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.PValue;

public class SearchForm extends VerticalPanel{
	
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();

	TreeViewMenu tvm;
	StackPanel sp;
	ContactCellListTab ct;
	TextBox tb;
	Button sb;
	Label l;
	ContactList cl ;
	Editor e;
	
	public void onLoad() {
		tvm = new TreeViewMenu();
		sp = new StackPanel();
		ct= new  ContactCellListTab();
		sb = new Button("Finden");
		tb = new TextBox();
		l = new Label("Wert:");
		this.add(l);
		this.add(tb);
		this.add(sb);
		this.add(sp);
		sb.addClickHandler((new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				GWT.log(cl.getListName());
				editorService.searchInList(tb.getValue(), cl, new SearchInListCallback());
			}
		}));
	}
	void setContactList(ContactList cl) {
		this.cl =cl;
	}
	void setEditor(Editor e) {
		this.e=e;
	}
	
	class SearchInListCallback implements AsyncCallback<ArrayList<Contact>> {
		@Override 
		
 		public void onFailure(Throwable caugth) {
			Window.alert("Der angegebene Wert wurde nicht gefunden");
 		}
 		@Override
 		public void onSuccess(ArrayList<Contact> result) {
 			GWT.log("OnSuccess");
 			for(Contact c : result) {
 				GWT.log(c.getValues().get(0).getStringValue());
 			ct.addContact(c);
 			}
 			sp.add(ct.getCellList(), "Ausgabe");
 			}
 		}
	}

