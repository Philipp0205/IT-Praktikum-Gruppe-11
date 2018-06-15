package de.hdm.group11.jabics.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.group11.jabics.shared.bo.ContactList;

/**
 *  
 */

public class ContactListCell extends AbstractCell<ContactList> {
	
	
	@Override
	public void render(Context context, ContactList cl, SafeHtmlBuilder sb) {
		if (cl == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(cl.getListName());
		sb.appendHtmlConstant("</div>");
		
	}
	


}
