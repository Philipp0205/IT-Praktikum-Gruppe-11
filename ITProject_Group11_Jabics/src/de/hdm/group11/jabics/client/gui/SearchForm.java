package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

public class SearchForm extends VerticalPanel{
	
	EditorServiceAsync editorService = ClientsideSettings.getEditorService();
	
	StackPanel sp;
	ContactCellListTab ct;
	CellList<Contact> list;
	TextBox tb;
	Button sb;
	Label l;
	ContactList cl ;
	Editor e;
	Label ausgabeLabel;
	
	public void onLoad() {
		ct = new ContactCellListTab();
		list = ct.createContactTabForSearchForm();
		ausgabeLabel = new Label();
		this.add(l);
		this.add(tb);
		this.add(sb);
		this.add(sp);
		this.add(list);
		this.add(ausgabeLabel);
		ausgabeLabel.setVisible(false);
		ct.setEditor(e);
		l.setText("Durchsuche Liste \"" + cl.getListName() + "\" nach Wert: ");
		
		sb.addClickHandler((new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				sp.setVisible(false);
				ausgabeLabel.setVisible(false);
				editorService.searchInList(tb.getText(), cl, new SearchInListCallback());
			}
		}));
	}
	
	public SearchForm() {
		sp = new StackPanel();

		sb = new Button("Finden");
		tb = new TextBox();
		l = new Label("Durchsuche Liste:");
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
 			if (result != null) {
 	 			for(Contact c : result) {
 	 			ct.addsearchedContact(c);
 	 			}
 	 			sp.setVisible(true);
 	 			sp.add(list, "Ausgabe");
 	 			ausgabeLabel.setText("Es wurde nach '" + tb.getValue() + "' gesucht.");
 	 			ausgabeLabel.setVisible(true);
 	 			tb.setText("");
 	 			
 	 			
 			}
}
 		}
	}