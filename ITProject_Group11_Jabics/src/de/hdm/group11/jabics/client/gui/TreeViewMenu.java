package de.hdm.group11.jabics.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.group11.jabics.client.gui.ContactCollaborationForm.CellTableResources;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Diese Klasse repräsentiert die Baum-Ansicht der Kontaktlisten und Listen.
 * 
 * @author Philipp
 * 
 *         Struktur und Styling von @author Thies
 */
public class TreeViewMenu extends VerticalPanel {
	EditorAdmin e;
	JabicsUser user;

	ContactListTreeTab contactListTab;
	SharedContactCellListTab sharedContactListTab;
	ContactCellListTab contactTab;
	StackPanel stackPanel1;
	StackPanel stackPanel2;
	CellTree tree;
	
	SafeHtmlBuilder builder;
	SafeHtml safeHtml;

	private CellTreeResources ctRes = GWT.create(CellTreeResources.class);
	
	/**
	 * Erzeugt eine neue Instanz von <code>TreeViewMenu</code> 
	 * @param u
	 */
	public TreeViewMenu(JabicsUser u) {

		String tip = new String("▶");
		Label tip2 = new Label("tip");
		
		safeHtml = SafeHtmlUtils.fromString("X ");

		stackPanel1 = new StackPanel();
		stackPanel2 = new StackPanel();
		stackPanel1.add(createContactListTreeTab(u));
		
		stackPanel2.add(new Label(""), safeHtml);
		stackPanel2.add(createContactCellListTab(u), " ▶ Alle Kontakte");
		stackPanel2.add(createSharedContactListTreeTab(u), " ▶  Mir geteilte Kontakte");
		stackPanel2.setStyleName("stackPanel2");
		stackPanel1.setStyleName("stackPanel1");
		
		stackPanel2.setWidth("250px");
		stackPanel1.setWidth("250px");
//		stackPanel1.getWidget(0).setStyleName("MeineListen");
		// stackPanel.ensureDebugId("cwStackPanel");
	}
	
	/**
	 * Wird beim ersten aufrufen der Klasse geladen. Es werden die <code>StackPanels</code> hinzugefügt.
	 */
	public void onLoad() {
		this.add(this.stackPanel1);
		this.add(this.stackPanel2);

	}
	
	/**
	 * Fügt eine <code>ContactList</code> zu allen Tabs des Stackpanels hinzu.
	 * 
	 * @param cl
	 * 			<code>ContactList</code> welche hinzugefügt werden soll.
	 */
	public void addContactList(ContactList cl) {
		contactListTab.addContactList(cl);

		for (Contact c : cl.getContacts()) {
			GWT.log("8.1 add Contact " + c.getName() + " to List " + cl.getListName());
			contactListTab.addContactOfList(cl, c);
		}
	}
	
	/**
	 * Entfernt die Anzeige einer <code>ContactList</code> aus dem Menü.
	 * 
	 * @param cl
	 * 			<code>Die ContatList</code> welche entfernt werden soll.
	 */
	public void removeContactListFromTree(ContactList cl) {
		contactListTab.removeContactList(cl);
	}
	
	/**
	 * Aktualisiert die Anzeige einer <code>ContactList</code> im Menü.
	 * 
	 * @param cl
	 * 			<code>ContactList</code> welche aktuallisiert werden soll.
	 */
	public void updateContactListInTree(ContactList cl) {
		contactListTab.updateContactList(cl);
	}
	
	/**
	 * Setzt den User der aktuellen Instant
	 * 
	 * @param u
	 * 			<code>JabicsUser</code> welcher gesetzt werden soll.
	 */
	public void setUser(JabicsUser u) {
		this.user = u;
		contactListTab.setUser(u);
		contactTab.setUser(u);
		sharedContactListTab.setUser(u);
	}
	
	/**
	 * Fügt der dem Menü einen neuen <code>Contact<code> innerhalbt einer Kontaktliste hinzu.
	 * 
	 * @param cl
	 * 			<code>ContactList</code> welcher der Kontakt angehört.
	 * @param c
	 * 			<code>Contact</code> welcher hinzugefügt werden soll.
 	 */
	public void addContactToList(ContactList cl, Contact c) {
		contactListTab.addContactOfList(cl, c);
	}
	
	/**
	 * Entfernt aus dem Menü einen <code>Contact<code> innerhalbt einer Kontaktliste.
	 * 
	 * @param cl
	 * @param c
	 */
	public void removeContactOfContactList(ContactList cl, Contact c) {
		contactListTab.removeContactOfContactList(cl, c);
	}
	
	/**
	 * Fügt dem Menü einen <code>Contact</code> hinzu.
	 * 
	 * @param c
	 * 			<code>Contact</code> welcher hinzugefügt werden soll.
	 */
	public void addContact(Contact c) {
		contactTab.addContact(c);
	}
	
	/**
	 * Entfernt einen <code>Contact</code> aus dem Menü .
	 * 
	 * @param c
	 * 			<code>Contact<code> welcher entfernt werden soll.
	 */
	public void removeContact(Contact c) {
		contactListTab.removeContact(c);
		contactTab.removeContact(c);
		sharedContactListTab.removeContact(c);
	}
	
	/**
	 * Aktuallisiert einen <code>Contact</code> im Menü.
	 * 
	 * @param c
	 * 			<code>Contact</code> welcher aktuallisiert werden soll.
	 */
	public void updateContact(Contact c) {
		contactListTab.updateContact(c);

		contactTab.updateContact(c);

		sharedContactListTab.updateContact(c);
	}
	
	/**
	 * Beieht das <code>StackPanel</code> in welchem die Kontaktlisten samt Kontakt 
	 * angezeigt werden.
	 * 
	 * @return das angeforderte <code>StackPanel</code>
	 */
	public StackPanel getStackPanel1() {
		return this.stackPanel1;
	}
	
	/**
	 * Bezieht das <code>StackPanel<code> in welchem Alle Kontakte des Nutzers sowie alle geteilten 
	 * Kontakt des Nutzes angezeigt werden. 
	 * 
	 * @return das angeforderte <code>StackPanel</code>
	 */
	public StackPanel getStackPanel2() {
		return this.stackPanel2;
	}
	
	/**
	 * Setzt den Editor der Instanz. 
	 * 
	 * @param editor
	 * 			<code>EditorAdmin</code> welcher gesetzt werden soll.
	 */
	public void setEditor(EditorAdmin editor) {
		GWT.log("Editor setzen in tree view");
		GWT.log("Editor: " + editor.hashCode());
		this.e = editor;

		contactListTab.setEditor(editor);
		contactTab.setEditor(editor);
		sharedContactListTab.setEditor(editor);
	}
	
	/**
	 * Erstellt eine Instanz von <code>ContactCellListTab</code> welcher innerhlabt des zwiten <code>StackPanel</code>
	 * angezeigt wird.
	 * 
	 * @param u
	 * 			der aktuelle <code>JabicsUser</code>
	 * @return ein Objekt vom Typ <code>Widget</code> welches den ContactCellListTab enthält.
	 */
	public Widget createContactCellListTab(JabicsUser u) {
		this.contactTab = new ContactCellListTab(u, this, clRes);
		contactTab.onLoad();
		return contactTab.getCellList();
	}
	
	//cellList Ressourcen
	private CellListResources clRes = GWT.create(CellListResources.class);
	public interface CellListResources extends CellList.Resources {

		@Source("JabicsCellList.css")
		CellList.Style cellListStyle();
	}
	
	/**
	 * Erstellt eine Instanz von <code>ContactListTreeTab</code> welche innerhalb des ersten <code>StackPanel</code>
	 * angezeigt wird.
	 * 
	 * @param u
	 * 			Der aktuelle <code>JabicsUser</code>.
	 * @return Ein Objekt vom Typ <code>Widget</code> welches den <code>CellTree</code> welcher die Kontaktlisten samt Kontakten
	 * enhhält.
	 */
	public Widget createContactListTreeTab(JabicsUser u) {

		this.contactListTab = new ContactListTreeTab(u, this);
		CellTree tree = new CellTree(contactListTab, "Root", ctRes);
		tree.setAnimationEnabled(true);

		GWT.log("TreeViewMenu: createListTab");

		return tree;
	}
	
	/**
	 * Erstellt eine Instanz  von <code>SharedContactCellListTab</code> welche innerhalb des zweiteren <code>StackPanel</code>
	 * angezeigt wird.
	 * @param u 
	 * 			der aktuelle <code>JabicsUser</code>.
	 * @return Ein Objekt vom Typ <code>Widget</code> welches die <code>CellList</code> welcher die geteilten Kontakte anzeigt
	 */
	public Widget createSharedContactListTreeTab(JabicsUser u) {
		this.sharedContactListTab = new SharedContactCellListTab(u, this, clRes);
		sharedContactListTab.onLoad();
		return sharedContactListTab.getCellList();
	}
	
	/**
	 * Leert das <code>SelectionModel</code> im entsprechenenden Tab. 
	 */
	public void clearSelectionModelContactListTab() {
		contactListTab.clearSelectionModel();
	}
	
	/**
	 * Leert das <code>SelectionModel</code> im entsprechenenden Tab. 
	 */
	public void clearSelectionModelContactTab() {
		contactTab.clearSelectionModel();
	}
	
	/**
	 * Leert das Leert das <code>SelectionModel</code> im entsprechenenden Tab. SelectionModel</code> im entsprechenenden Tab. 
	 */
	public void clearSelectionModelSharedContactTab() {
		sharedContactListTab.clearSelectionModel();
	}
	
	/**
	 * Bezieht das <code>SelectionModel</code> im entsprechenden Tab.
	 */
	public SingleSelectionModel<BusinessObject> getSelectionModelContactListTab() {
		return contactListTab.getSelectionModel();
	}
	
	/**
	 * Bezieht das <code>SelectionModel</code> im entsprechenden Tab.
	 */
	public SingleSelectionModel<Contact> getSelectionModelContactsTab() {
		return contactTab.getSelectionModel();
	}
	
	/**
	 * Bezieht das <code>SelectionModel</code> im entsprechenden Tab.
	 */
	public SingleSelectionModel<Contact> getSelectionModelSharedContactsTab() {
		return sharedContactListTab.getSelectionModel();
	}
	
	/**
	 * Entfern den ContacsTab.
	 */
	public void removeContactsTab() {
		stackPanel2.remove(0);
	}
	
	/**
	 * Entfernt den SharedContactsTab.
	 */
	public void removeSharedContactsTab() {
		stackPanel2.remove(1);
	}	
	
	/**
	 * Entfernt den ContactsTab.
	 */
	public void removeAllTabsOfStackPanel2() {
		stackPanel2.remove(0);
		stackPanel2.remove(0);
	}
	
	/**
	 * Ruft den Tab im zweiten <code>StackPanel</code> welcher Lerr ist und somit der jeweils andere offene Tab geschlossen wird.
	 */
	public void showEmptyTab() {
		stackPanel2.showStack(0);
	}
	
	/**
	 * Steööt die Ressourcen für den CellTree bereit. 
	 *
	 */
	public interface CellTreeResources extends CellTree.Resources {
		@Override
		@Source("cellTreeClosedItem.png")
	    ImageResource cellTreeClosedItem();

	    @Override
		@Source("cellTreeOpenItem.png")
	    ImageResource cellTreeOpenItem();

		@Override
		@Source("JabicsCellTree.css")
		CellTree.Style cellTreeStyle();
	}

}
