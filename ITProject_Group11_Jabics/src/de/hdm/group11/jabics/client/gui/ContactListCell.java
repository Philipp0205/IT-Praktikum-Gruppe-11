package de.hdm.group11.jabics.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import de.hdm.group11.jabics.resource.JabicsResources;
import de.hdm.group11.jabics.shared.bo.ContactList;

/**
 * Repäsentation einer einer Kontaktliste. Zeigt den Namen der
 * Kontaktliste an. Neben der Namen ist eine Anzeige für <code>BoStatus</code>
 * plaziert, welche anzeigt ob die Kontaktliste geteilt wurde.
 * 
 * @author Kurrle
 */
public class ContactListCell extends AbstractCell<ContactList> {
	
	/**
	 *  Rendert eine Zeile als HTML-Element, welches anschließend als solches angezeigt werden kann.
	 *  Es wernden der Name des <code>Contacts</code> als auch der <code>BoStatus</code>
	 *  angezeigt.
	 *  
	 *  @param context, wird nicht benutzt.
	 *  @param cl, Kontaktliste, die angezeigt werden soll
	 *  @param sb, HtmlBuilder, welcher die Html-Repäsenation des Kontakt erstellt
	 */
	@Override
	public void render(Context context, ContactList cl, SafeHtmlBuilder sb) {
		sb.appendEscaped(cl.getListName());
		if (cl != null) {
			GWT.log("ContactListCell: ShareStatus: " + cl.getShareStatus());

			try {
				switch (cl.getShareStatus()) {

				case IS_SHARED:
					sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendotlist()).getHTML());
					break;
				case NOT_SHARED:
					sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.reddotlist()).getHTML());
					break;
				default:
					sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.reddotlist()).getHTML());
				}
			} catch (Exception e) {
				GWT.log("ShareStatus undefined for Contact" + cl.getListName());
				sb.appendHtmlConstant(AbstractImagePrototype.create(JabicsResources.INSTANCE.greendotlist()).getHTML());
			}
		}

		//sb.appendHtmlConstant("<div>");
		
		//sb.appendHtmlConstant("</div>");

	}

}
