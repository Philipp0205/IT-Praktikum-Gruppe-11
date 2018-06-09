package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Jabics;
import de.hdm.group11.jabics.server.EditorServiceImpl;
import de.hdm.group11.jabics.shared.EditorService;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.User;

/**
 * 
 * Dieses Formular realisiert die Darstellung von <code>Contact</code> Objekten eines Nutzers auf der grafischen Benutzeroberfläche.
 * Sie stellt für den Nutzer alle notwendigen Methoden zur Verwaltung von Kontakten zur verfügung. 
 * 
 * @author Brase
 * @author Ilg
 *
 */

public class ContactForm extends VerticalPanel {
	/**
	 * Struktur von
	 * @author Christian Rathke
	 * 
	 * Angepasst von
	 * @author Brase
	 * @author Ilg
     * 
     * 
     * @see
	 */

	EditorServiceAsync editorService = ClientsideSettings.getEditorService();
	
	User userToDisplay = null;
	Contact contactToDisplay = null;
	PValue selectedPValue = null;
	TreeViewMenu Contacttree = null;

	
	public void onLoad() {
		
		super.onLoad();
		
		//Erstellen des Haupt-Grids
		Grid userInformationGrid = new Grid(6, 1);
		
		//GRID-ZEILE 1: Vergabe des Fensternamens
		Label formName = new Label("Kontakt-Editor");
		userInformationGrid.setWidget(0, 0, formName);
		
		//GRID-ZEILE 2: Holen des Kontakt-Namens
		Label contactName = new Label(contact.getName());
		userInformationGrid.setWidget(1, 0, contactName);		

		//GRID-ZEILE 3: Erstellen des 'Eigenschafts-Grids'
		Grid propertyGrid = createPropertyGrid(contact);
		userInformationGrid.setWidget(2, 0, propertyGrid);		

		//GRID-ZEILE 4: Optionen zum hinzuf�gen einer Eigenschaft
		//Die gesamte Zeile (4) wird ein HorizontalPanel
			HorizontalPanel propertyAddBox = new HorizontalPanel();
			//in diesem Horizontal Panel gibt es 4 Felder
			// 1. eine Listbox zum Ausw�hlen des Eigenschafts-Typs (z.B. "Mail")
			ListBox selectProperty = new ListBox();
			propertyAddBox.add(selectProperty);
			// 2. ein Eingabefeld, um die Eigenschaftsauspr�gung festzulegen (z.B. "xyz@hdm...")
			TextBox propertyValue = new TextBox();
			propertyAddBox.add(propertyValue);
			// 3. einen Button zum Hinzufügen
		    Button addPropertyButton = new Button("Eigenschaft hinzufügen");
		    addPropertyButton.addClickHandler(new AddPropertyClickHandler());
		    propertyAddBox.add(addPropertyButton);
		    
		    //hinzufügen von Zeile 4 zum Hauptgrid
		    userInformationGrid.setWidget(3, 0, propertyAddBox);
		
		    
		//GRID-ZEILE 5: 
		    HorizontalPanel contactShareBox = new HorizontalPanel();
		    Label shareQuestion = new Label("Wollen Sie diesen Kontakt teilen?");
		    contactDeleteBox.add(shareQuestion);
		    
		    Button shareContactButton = new Button("Kontakt teilen") 
		    shareContactButton.addClickHandler(new shareContactClickHandler());
		    contactDeleteBox.add(shareContactButton);
		    
		    userInformationGrid.setWidget(4, 0, contactShareBox);	
		    
		    
		//GRID-ZEILE 6: 
		    HorizontalPanel contactDeleteBox = new HorizontalPanel();
		    Label deleteQuestion = new Label("Wollen Sie diesen Kontakt löschen?");
		    contactDeleteBox.add(deleteQuestion);
		    
		    Button deleteContactButton = new Button("Kontakt löschen") 
		    deleteContactButton.addClickHandler(new DeleteContactClickHandler());
		    contactDeleteBox.add(deleteContactButton);
		    
		    userInformationGrid.setWidget(5, 0, contactDeleteBox);	
}
		

	
	/**
	 * Im Folgenden Code werden Clickhandler und Asynchrone Methodenaufrufe für die Operationen Editieren, Löschen oder Teilen 
	 * eines <code>Contact</code> Objekts implementiert.
	 * @author Brase
	 * @author Ilg
	 */
	
	private class DeleteContactClickHandler implements ClickHandler {
				@Override
				public void onClick(ClickEvent event) {
					
					if(contactToDisplay == null) {
						Window.alert("Kein Kontakt ausgewählt");
					}else {
					editorService.deleteContact(contactToDisplay, new deleteContactCallback(contactToDisplay));
					}
				}
			}


	private class deleteContactCallback implements AsyncCallback<Void> {

		private Contact contact = null;
		
		deleteContactCallback(Contact c) {
			contact = c;
		}
		public void onFailure(Throwable caugth) {
		}
		@Override
		public void onSuccess(Void result) {
			if(c != null) {
				//Contacttree.removeContact(contact);
			}
		}
	}
	
	
	private class DeletePValueClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			if(contactToDisplay == null) {
				Window.alert("Kein Kontakt ausgewählt");
			}else {
			editorService.deletePValue(contactToDisplay, new deletePValueCallback(selectedPValue));
			}
		}
	}


	private class deletePValueCallback implements AsyncCallback<Void> {

			private PValue pvalue = null;

			deletePValueCallback(PValue pv) {
				pvalue = pv;
			}
			public void onFailure(Throwable caugth) {
			}
			@Override
			public void onSuccess(Void result) {
				if(c != null) {
		//update Contact
				}
			}
	}
	

	   void setSelected (Contact c, User u) {
			if (c != null) {
				contactToDisplay = c;
				userToDisplay = u;
				deleteContactButton.setEnabled(true);
				
				editorService.getPValueOf(c, userToDisplay, new GetPValuesCallback());
			} else {
				contactToDisplay = null;
				deleteContactButton.setEnabled(false);
			}
	   }

	   private class GetPValuesCallback implements AsyncCallback<ArrayList<PValue>>{
		   public void onFailure(Throwable caught) {
			   
			   Window.alert("Fehler in GetPValuesCallback");
			   
		   }
		   public void onSuccess(ArrayList<PValue> result) {
  			   
			   	CheckBox[] CheckBox = 		new CheckBox[result.size()];
				Label[] PropertyLabels = 	new Label[result.size()];
				TextBox[] PValueTextBox = 	new TextBox[result.size()];
				Button[] SaveButton = 		new Button[result.size()];
				Button[] DeleteButton = 	new Button[result.size()];
				
				Grid contactGrid = new Grid(result.size(), 1);
			   
			   for (int i = result.size(); i>0; i--) {
				   
				   int pointer = i;
			   
				   CheckBox[pointer] = new CheckBox();
				   PropertyLabels[pointer] = new Label(result.get(pointer).getProperty().toString());
				   PValueTextBox[pointer] = new TextBox();
				   PValueTextBox[pointer].setText(result.get(pointer).toString());
				   
				   SaveButton[pointer] = new Button("Save");
				   
				   SaveButton[pointer].addClickHandler(new ClickHandler() {
					    public void onClick(ClickEvent event) {
					    	PValue currentPV = result.get(pointer);
					    	currentPV.setStringValue(PValueTextBox[pointer].getValue().toString());
					    	
					    	Window.alert("Wert" + PValueTextBox[pointer].getValue().toString() + "gespeichert");
					    }}
				   );
				   
				   DeleteButton[pointer] = new Button("Delete");
				   
				   DeleteButton[pointer].addClickHandler(new ClickHandler() {
					    public void onClick(ClickEvent event) {
					    	contactToDisplay.removePValue(result.get(pointer));
					    	
					    	Window.alert("Wert" + PValueTextBox[pointer].getValue().toString() + "gelöscht");
					    }}
				   );
				   
				   //Arryname.indexOf(1)
				   }
				   
			   for (int j = PropertyLabels.length; j > 0 ; j--) {
				   
				   contactGrid.setWidget(j,0,CheckBox[j]);
				   contactGrid.setWidget(j,1,PropertyLabels[j]);
				   contactGrid.setWidget(j,2,PValueTextBox[j]);
				   contactGrid.setWidget(j,3,SaveButton[j]);
				   contactGrid.setWidget(j,4,DeleteButton[j]);
				   
			   }
			   }
		   }
	   
	   
	   //Wenn der DeletePValueButton gedrückt wird, wird das PValue Objekt daneben selected.
	   void setSelected (PValue pv) {
			if (pv != null) {
				selectedPValue = pv;
				idValueLabel.setText("Konto: " + Integer.toString(contactToDisplay.getId()));
				editorService.getPValueOf(c, userToDisplay, callback);
			} else {
				contactToDisplay = null;
				deleteContactButton.setEnabled(false);
			}
		
	 }

	
	
	}




    
	



