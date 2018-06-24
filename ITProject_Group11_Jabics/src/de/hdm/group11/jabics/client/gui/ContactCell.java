package de.hdm.group11.jabics.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import de.hdm.group11.jabics.resource.JabicsResources;
import de.hdm.group11.jabics.shared.bo.BoStatus;
import de.hdm.group11.jabics.shared.bo.Contact;

/**
 * Analog zu ContactListCell.
 * 
 * @author Kurrle
 */
public class ContactCell extends AbstractCell<Contact> {
	
	//rivate final String imageHtml;
	
	//final JabicsResources INSTANCE = GWT.create(JabicsResources.class);
	
	public ContactCell() {
		//this.imageHtml = AbstractImagePrototype.create(image).getHTML();
		
	}

	@Override
	public void render(Context context, Contact c, SafeHtmlBuilder sb) {
				
		if (c != null) {
			
//			switch (c.getShareStatus()) {
//				case IS_SHARED: sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
//				break;
//				case PARTIALLY_SHARED: sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
//				break; 
//				case NOT_SHARED: sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
//				break;
//				default: sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
//			}
			//GWT.log("3.3 " + c.getShareStatus().toString());
			
//			if (c.getShareStatus() == BoStatus.IS_SHARED) {
//				sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
//				
//			} else if (c.getShareStatus() == BoStatus.PARTIALLY_SHARED) {
//				sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
//				
//			} else if (c.getShareStatus() == BoStatus.NOT_SHARED) {
//				sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
//			} else  {
//				GWT.log("Kein Bild");
//			}
			
			
		
			
			sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
			// sb.appendHtmlConstant("<div>");
			//sb.appendHtmlConstant()
			sb.appendEscaped(c.getName());
			// sb.appendHtmlConstant("</div>");
		}
	}
}
