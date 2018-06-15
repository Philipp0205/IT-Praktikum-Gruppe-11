package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.server.LoginInfo;
import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;

public class ContactCellListTab  {

	
	ContactMapper cMapper = ContactMapper.contactMapper();
	private EditorServiceAsync eService = null;
	LoginInfo loginfo = new LoginInfo();
	//private final ArrayList<Contact> allcontacts = cMapper.findAllContacts(loginfo.getCurrentUser());
	ListDataProvider<Contact> contactsProvider = new ListDataProvider<Contact>();
	
	// ursprünglich onLoad();
	public Widget createTab() {
	
		/*
		 * Der ListDataProvider wird mit den Kontakten befüllt.
		 */
		eService.getContactsOf(loginfo.getCurrentUser(), new AsyncCallback<ArrayList<Contact>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Nix.
				
			}

			@Override
			public void onSuccess(ArrayList<Contact> contacts) {
				for (Contact c : contacts) {
					contactsProvider.getList().add(c);
				}
				
			}
					
		});
	
	
	/*
	 * Der Key provider für einen Kontakt sorgt dafür, dass die Auswahl in der CellList gleich bleibt
	 * auch wenn das Objekt sich ändert. 
	 */	
      ProvidesKey<Contact> keyProvider = new ProvidesKey<Contact>() {
         public Object getKey(Contact c) {
            // Zurückgeben das unique Key von dem Objekt.
            return (c == null) ? null : c.getId();
         }
      };
      
      // Create a CellList using the keyProvider.
      CellList<Contact> cellList = new CellList<Contact>(new ContactCell(),      
      keyProvider);
      
      /*
       * Das SelectionModel implementiert die Selektion einer Zeile in der CellList. 
       * Das SelectionModel wird mit dem keyProvider initalisiert aus oben genannten gründen.
       */
      SelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>(keyProvider);
      cellList.setSelectionModel(selectionModel);
           
      /*
       * Redraw the CellList. Sarah/Sara will still be selected because we
       * identify her by ID. If we did not use a keyProvider, 
       * Sara would not be selected.
       */
      cellList.redraw();
      
      return cellList;
      
	}
	
	

}
