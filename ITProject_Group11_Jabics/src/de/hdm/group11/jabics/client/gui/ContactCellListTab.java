package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;


import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;

/**
 * Repräsentiert eine CellList, in der alle Kontakte des Nutzers angezeigt werden.
 * Im Programm wird die <code>CellList</code> innerhlab eines <code>StackPanel</code> angezeigt. 
 * 
 * 
 * @author Philipp
 */
public class ContactCellListTab {

	EditorAdmin editor;
	JabicsUser user;


	private EditorServiceAsync eService = ClientsideSettings.getEditorService();
	CellList<Contact> contactCell;
	ListDataProvider<Contact> contactDataProvider;
	private ContactKeyProvider keyProvider = null;
	
	TreeViewMenu treeViewMenu;

	private SingleSelectionModel<Contact> selectionModel = null;
	private CellListResources clRes = GWT.create(CellListResources.class);
	
	/**
	 *  Erzeugt Instanzen von selectionModel, den Cells und Data Providernn. Welche für
	 *  die Klasse gebraicht werden.
	 *  Der Konstruktor ist mehrfach überladen damit auch ein TreeViewMenu mitgegebn werden kann.
	 *  
	 * @param u der Nutzer für den die Anzeige ausgegeben werden soll.
	 * @param clRes2 
	 * @param treeViewMenu2 
	 */
	public ContactCellListTab(JabicsUser u, TreeViewMenu treeViewMenu2, de.hdm.group11.jabics.client.gui.TreeViewMenu.CellListResources clRes2) {
		
		this.user = u;
		
		keyProvider = new ContactKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time."
		selectionModel = new SingleSelectionModel<Contact>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		contactCell = new CellList<Contact>(new ContactCell(),clRes, keyProvider);
		contactDataProvider = new ListDataProvider<Contact>();
		contactDataProvider.addDataDisplay(contactCell);
		contactCell.setSelectionModel(selectionModel);
	}
	
	public ContactCellListTab(JabicsUser u, TreeViewMenu tvm) {
		this.user = u;
		this.treeViewMenu = tvm;
		
		keyProvider = new ContactKeyProvider();
		selectionModel = new SingleSelectionModel<Contact>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		contactCell = new CellList<Contact>(new ContactCell(), clRes, keyProvider);
		contactDataProvider = new ListDataProvider<Contact>();
		contactDataProvider.addDataDisplay(contactCell);
		contactCell.setSelectionModel(selectionModel);
	}
	
	public interface CellListResources extends CellList.Resources {
		 @Override
			@Source("JabicsCellList.css")
		    CellList.Style cellListStyle(); 
	}

	
	/**
	 * Erstellt Eine Suche innerhalbt des ContactTabs.
	 * @return
	 */
	public CellList<Contact> createContactTabForSearchForm() {
		keyProvider = new ContactKeyProvider();
		contactCell = new CellList<Contact>(new ContactCell(),clRes, keyProvider);
		selectionModel.clear();

		contactDataProvider = new ListDataProvider<Contact>();
		contactDataProvider.addDataDisplay(contactCell);
		contactCell.setSelectionModel(selectionModel);

		contactDataProvider.flush();
		contactCell.redraw();
		return contactCell;
	}
	
	/**
	 * Auslesen der CellList.
	 * 
	 * @return CellList der Instanz
	 */
	public CellList<Contact> getCellList() {
		return this.contactCell;
	}
	
	/**
	 *  Wird beim erstellen des ContactTabs aufgerufen. 
	 *  Es werden alle Kontakte des Nutzers aus de Datenbank geoholt um diese
	 *  später anzeigen zu können.
	 *  
	 *  Des Weiteren werden die DataProvider aktuallisiert damit die Anzeige
	 *  aktuallisiert wird.
	 */
	public void onLoad() {

		GWT.log("3.1 createContactTab");

		/*
		 * Der ListDataProvider wird mit den Kontakten befüllt.
		 */
		GWT.log("2.1 User: " + user.getId());
		eService.getContactsOf(user, new AsyncCallback<ArrayList<Contact>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("3.1 CellList onFailure" + caught.toString());
			}

			@Override
			public void onSuccess(ArrayList<Contact> contacts) {

				if (contacts != null) {
					GWT.log("3.1 CellList onSuccess");

					for (Contact c : contacts) {
						contactDataProvider.getList().add(c);
						contactDataProvider.refresh();
						contactCell.redraw();
						contactDataProvider.flush();
					}
				}

			}
		});
	}

	/**
	 * Wenn ein Kontakt geändert wird bleibt der Key, welcher in folgender privater Klasse
	 * festgelegt wird gleich, weshalb die Selektion des Kontaktes auch dann erhalten bleibt.
	 * @author Kurrle
	 *
	 */
	private class ContactKeyProvider implements ProvidesKey<Contact> {

		@Override
		public Object getKey(Contact c) {
			// Zurückgeben das unique Key von dem Objekt.
			return (c == null) ? null : c.getId();
		}
	}
	
	/**
	 * Implementiert das Verhalten der Selektion verschiedener Kontakte. Wenn ein anderer Kontakt
	 * selektiert wird, wird das selectionModel aktallisiert. 
	 * Des Weiteren werden die Selektionen in den anderen Tabs entfern, damit Bugs vermieden werden.
	 * 
	 * @author Kurrle
	 *
	 */
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			BusinessObject selection = selectionModel.getSelectedObject();
			this.setSelectedContact((Contact) selection);


			treeViewMenu.clearSelectionModelSharedContactTab();	
			//treeViewMenu.clearSelectionModelContactListTab();

		}
		
		/**
		 * Setzt den selektierten Kontakt.
		 * 
		 * @param c, der Kontakt der seletkiert werden soll.
		 */
		private void setSelectedContact(Contact c) {
			GWT.log("3.1 Kontakt anzeigen " + c.getName());
			Window.alert("Kontakt anzeigen" + c.getName());
			editor.showContact(c);

		}
	}
	
	/**
	 * Setzt den Editor.
	 * 
	 * @param editor, der gesetzt werden soll.
	 */
	public void setEditor(EditorAdmin editor) {
		GWT.log("Editor setzen in contactCellListTab");
		GWT.log("Editor: " + editor.hashCode());
		this.editor = editor;
	}
	
	/**
	 * Setzt den User
	 * @param u, User der gesetzt werden soll.
	 */
	public void setUser(JabicsUser u) {
		GWT.log("User setzen in contactCellListTab");
		this.user = u;
	}
	
	/**
	 * Fügt einen Kontakt zu der CellList hinzu.
	 * @param c, der Kontakt der hinzugefügt werden soll.
	 */
	public void addContact(Contact c) {
		contactDataProvider.getList().add(c);
		selectionModel.setSelected(c, true);
	}
	
	/**
	 * Der Anzeige der gesuchten Kontakte wird ein Kontakt hinzugefügt.
	 * @param c, der hinzuzufügende Kontakt,
	 */
	public void addsearchedContact(Contact c) {
		contactDataProvider.getList().add(c);
		contactDataProvider.flush();
	}
	
	/**
	 * Kontakt wird aus der CellList entfernt.
	 * @param c, der zu entfernende Kontakt.
	 */
	public void removeContact(Contact c) {
		contactDataProvider.getList().remove(c);
		contactDataProvider.flush();
	}
	
	/**
	 * Kontakt wird in der CellList aktualisiert.
	 * @param c, der zu aktualisierende Kontakt.
	 */
	public void updateContact(Contact c) {
		for (Contact ci : contactDataProvider.getList()) {
			if (c.getId() == ci.getId()) {
				contactDataProvider.getList().set(0, c);
				break;
			}
		}
		contactDataProvider.refresh();
	}
	
	/**
	 * Gibt den <code>ListDataProvider</code> zurück.
	 * @return den contactDataProvider
	 */
	public ListDataProvider<Contact> getContactDataProvider() {
		return this.contactDataProvider;
	}
	
	/**
	 * Gibt das <code>SingleSelectionModel</code> zurück.
	 * @return
	 */
	public SingleSelectionModel<Contact> getSelectionModel() {
		return this.selectionModel;
	}
	
	/**
	 * Alle aktuellen Selektionen  werden entfernt.
	 */
	public void clearSelectionModel() {
		if (selectionModel != null) {
			this.selectionModel.clear();
		} else return;

	}
	
	/**
	 * TODO wird eigentlich nicht mehr gebraucht.
	 * @author Kurrle
	 *
	 */
	public class AsyncDataProvider extends AbstractCell<Contact> {
		@Override
		public void render(Context context, Contact value, SafeHtmlBuilder sb) {
			if (value == null) {
				// sb.appendHtmlConstant("<div>");
				sb.appendEscaped(value.getName());
				// sb.appendHtmlConstant("</div>");
			}
		}

	}
}
