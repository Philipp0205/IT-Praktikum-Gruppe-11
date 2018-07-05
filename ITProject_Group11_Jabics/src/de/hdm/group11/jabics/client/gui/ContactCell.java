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
 * Repäsentation einer Zeile in der ein Kintakt angezeigt wird. Zeigt den Namen des
 * Kontaktes an. Neben der Namen ist eine Anzeige für <code>BoStatus</code>
 * plaziert, welche anzeigt ob der Kontakt geteilt wurde.
 * 
 * @author Kurrle
 */
public class ContactCell extends AbstractCell<Contact> {
	
	public ContactCell() {		
	}
	
	/**
	 *  Rendert eine Zeile als HTML-Element, welches anschließend als solches angezeigt werden kann.
	 *  Es wernden der Name des <code>Contacts</code> als auch der <code>BoStatus</code>
	 *  angezeigt.
	 *  
	 *  @param context, wird nicht benutzt.
	 *  @param c, Kontakt der angezeigt werden soll
	 *  @param sb, HtmlBuilder, welcher die Html-Repäsenation des Kontakt erstellt
	 */
	@Override
	public void render(Context context, Contact c, SafeHtmlBuilder sb) {
		
		sb.appendEscaped(c.getName());
				
		if (c != null) {
			GWT.log("ContactCell: ShareStatus: " + c.getShareStatus());
			
			      try {
			        switch (c.getShareStatus()) {
			        case IS_SHARED:
			          sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
			          break;
			        case NOT_SHARED:
			          sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.reddot()).getHTML());
			          break;
			        default:
			          sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.reddot()).getHTML());
			        }
			      } catch (Exception e) {
			        GWT.log("ShareStatus undefined for Contact" + c.getName());
			        sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendot()).getHTML());
			      }
		}

	}
}
