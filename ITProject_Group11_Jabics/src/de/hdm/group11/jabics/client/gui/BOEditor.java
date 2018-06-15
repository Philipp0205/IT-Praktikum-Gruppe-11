package de.hdm.group11.jabics.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;


public class BOEditor extends Composite {
	
	/**
	 *  Aufbau des Editors
	 */
	
	private VerticalPanel Box = new VerticalPanel();

	
		public BOEditor(ArrayList<PValue> ary) {
			
			RootPanel.get("Navigator").add(Box);
			
			for (PValue pv : ary) {
			      showProp(pv);
			}

			final Button addProp = new Button("Eigenschaft hinzuf�gen");
			Box.add(addProp);
			final Button delCon = new Button("Kontakt l�schen");
			Box.add(delCon);
			
			/*
			 * RPC zu Kontakt l�schen
			 */
			
		}
		
		
		private void showProp(PValue prop){
			
			/**
			 *  F�r jedes Wertepaar (Eigenschaft & Eigenschaftsauspr�gung) erstellt die Methode
			 *  ein neues Element bestehend aus einem HorizontalPanel mit Text- und Input-Feld
			 */
			private String label = prop.label;
			private Type value = prop.type;
			
			final HorizontalPanel nextProperty = new HorizontalPanel();
			Box.add(nextProperty);
			
			Label lbl = new Label(label);
			TextBox tb = new TextBox(value);
			
			nextProperty.add(lbl);
			nextProperty.add(tb);
			
			final Button editBtn = new Button("Edit");
			final Button saveBtn = new Button("Save");
			saveBtn.setEnabled(false); 
			
			/*
			 * Switchmethode noch zu implementieren
			 */
			nextProperty.add(editBtn);
			nextProperty.add(saveBtn);
			
		}
		
		private void delProp(Property prop) {
			
			/**
			 *  Aufruf der L�schmethode f�r Eigenschaften eines Kontaktes
			 *  TODO Ersetzen von void durch R�ckgabewert ob Funktion erfolgreich
			 */
			
			deleteProperty(prop);
			

		}
		
	}
	
}