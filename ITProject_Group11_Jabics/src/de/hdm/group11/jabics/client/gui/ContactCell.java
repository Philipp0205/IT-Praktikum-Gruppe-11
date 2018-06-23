package de.hdm.group11.jabics.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.group11.jabics.shared.bo.Contact;

/**
 * Analog zu ContactListCell.
 * 
 * @author Kurrle
 */
public class ContactCell extends AbstractCell<Contact> {

	@Override
	public void render(Context context, Contact value, SafeHtmlBuilder sb) {
		if (value != null) {
			// sb.appendHtmlConstant("<div>");
			sb.appendEscaped(value.getName());
			// sb.appendHtmlConstant("</div>");
		}
	}

}
