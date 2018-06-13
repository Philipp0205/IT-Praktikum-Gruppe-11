package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.server.LoginInfo;
import de.hdm.group11.jabics.server.db.ContactMapper;
import de.hdm.group11.jabics.shared.bo.Contact;

public class ContactCellListTab  {
	
	private static class ContactCell extends AbstractCell<Contact> {

		@Override
		public void render(Contact c, Object key, SafeHtmlBuilder sb) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	ContactMapper cMapper = ContactMapper.contactMapper();
	LoginInfo loginfo = new LoginInfo();
	private final ArrayList<Contact> allcontacts = cMapper.findAllContacts(loginfo.getCurrentUser());
	
	public void onLoad() {
		
	  /*
       * Define a key provider for a Contact. We use the unique ID 
       * as the key, which allows to maintain selection even if the
       * name changes.
       */
      ProvidesKey<Contact> keyProvider = new ProvidesKey<Contact>() {
         public Object getKey(Contact c) {
            // Always do a null check.
            return (c == null) ? null : c.getId();
         }
      };
      
      // Create a CellList using the keyProvider.
      CellList<Contact> cellList = new CellList<Contact>(new ContactCell(),      
      keyProvider);
      
      // Push data into the CellList.
      cellList.setRowCount(allcontacts.size(), true);
      cellList.setRowData(0, allcontacts);
      
      // Add a selection model using the same keyProvider.
      SelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>(keyProvider);
      cellList.setSelectionModel(selectionModel);
           
      /*
       * Redraw the CellList. Sarah/Sara will still be selected because we
       * identify her by ID. If we did not use a keyProvider, 
       * Sara would not be selected.
       */
      cellList.redraw();
      
	}
	
	

}
