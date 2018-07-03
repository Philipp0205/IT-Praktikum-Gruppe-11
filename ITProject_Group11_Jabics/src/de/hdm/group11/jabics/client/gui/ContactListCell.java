package de.hdm.group11.jabics.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import de.hdm.group11.jabics.resource.JabicsResources;
import de.hdm.group11.jabics.shared.bo.ContactList;

/**
 *  
 */

public class ContactListCell extends AbstractCell<ContactList> {

	@Override
	public void render(Context context, ContactList cl, SafeHtmlBuilder sb) {

		if (cl != null) {

			try {
				switch (cl.getShareStatus()) {
				case IS_SHARED:
					sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
					break;
				case NOT_SHARED:
					sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.reddot()).getHTML());
					break;
				default:
					sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
				}
			} catch (Exception e) {
				GWT.log("ShareStatus undefined for Contact" + cl.getListName());
				sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
			}
		}

		//sb.appendHtmlConstant("<div>");
		sb.appendEscaped(cl.getListName());
		//sb.appendHtmlConstant("</div>");

	}

}
