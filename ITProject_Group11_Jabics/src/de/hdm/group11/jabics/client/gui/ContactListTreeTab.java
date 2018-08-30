package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;

import de.hdm.group11.jabics.shared.EditorServiceAsync;
import de.hdm.group11.jabics.shared.bo.BusinessObject;
import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.ContactList;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.client.ClientsideSettings;
import de.hdm.group11.jabics.client.Editor;
import de.hdm.group11.jabics.resource.JabicsResources;

/**
 * Zeigt alle Kontaktlisten des Nutzer mit Kontakten an. Es wiedr das GWT-Widget
 * <code>CellTree</code> benutzt.
 * 
 * @author Kurrle, orientiert an @Rathke und @Thies
 */
public class ContactListTreeTab implements TreeViewModel {

	private Contact selectedContact;
	private ContactList selectedContactList;
	private EditorServiceAsync eService = ClientsideSettings.getEditorService();

	private JabicsUser jabicsUser;
	private EditorAdmin editor;

	private TreeViewMenu treeViewMenu;

	private ArrayList<ContactList> editedLists;

	/*
	 * Der DataProvider ist dafür zuständig, die Anzeige zu aktualisieren, immer
	 * wenn etwas geändert wird. Also Controller (m-v-c-Modell), zwischen der
	 * Anzeige (CellTable) und dem Modell (Liste von Objekten).
	 * 
	 * In diesem Fall werden werden Kontaktlisten bereitgestellt.
	 */
	private ListDataProvider<ContactList> contactListDataProviders;

	/*
	 * In der Map werden die ListDataProviders für die expandierten Kontakte
	 * gepespeichert.
	 * 
	 * Das Java Map Interface "mappt" einzigartige Schlüssel (keys) und den
	 * zugehörigen Wert (value), vergleichbar mit einem Wörterbuch oder
	 * Zuweisungstabellen in der DB. Die values können jeder Zeit anhand der Keys
	 * aufgerufen werden. Also ein Assoziativspeicher.
	 * 
	 * Beispiel: key: 1234 --> Value: Kontakt (Max, Mustermann, 1990, ...)
	 */
	private Map<ContactList, ListDataProvider<Contact>> contactDataProviders = null;

	private BusinessObjectKeyProvider boKeyProvider;

	private SingleSelectionModel<BusinessObject> selectionModel;

	/**
	 * Erzeugt Instanzen des KeyProvider und des selectonModel.
	 * 
	 * @param u der User für den die Kontaktlisten und Kontakte angezeigt werden
	 *          sollen.
	 */
	public ContactListTreeTab(JabicsUser u) {
		this.jabicsUser = u;

		boKeyProvider = new BusinessObjectKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time."

		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());

		/*
		 * Assoziativspeicher, bei dem Kontakte Kontaktlisten zugeordnet werden. Freunde
		 * --> Max Mustermann
		 * 
		 * (wird weiter unten deklariert)
		 */
		contactDataProviders = new HashMap<ContactList, ListDataProvider<Contact>>();
	}

	/**
	 * Erzeugt Instanzen des KeyProvider und des selectonModel.
	 * 
	 * @param u   der User für den die Kontaktlisten und Kontakte angezeigt werden
	 *            sollen.
	 * @param tvm das TreeViewModel welches später für das Selection-Handling
	 *            gebraucht wird.
	 */
	public ContactListTreeTab(JabicsUser u, TreeViewMenu tvm) {
		this.jabicsUser = u;
		this.treeViewMenu = tvm;

		boKeyProvider = new BusinessObjectKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time."

		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());

		/*
		 * Assoziativspeicher, bei dem Kontakte Kontaktlisten zugeordnet werden. Freunde
		 * --> Max Mustermann
		 * 
		 * (wird weiter unten deklariert)
		 */
		contactDataProviders = new HashMap<ContactList, ListDataProvider<Contact>>();

	}

	/**
	 * Sorgt dafür, dass verschiedene Elemente des CellTrees ausgewählt werden
	 * können. Dabei wird zwischen Kontakten und Kontaktlisten unterschieden.
	 *
	 */
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {

			BusinessObject selection = selectionModel.getSelectedObject();

			if (selection != null) {
				if (selection instanceof Contact) {
					setSelectedContact((Contact) selection);
				} else if (selection instanceof ContactList) {
					setSelectedContactList((ContactList) selection);
					treeViewMenu.showEmptyTab();
				}
				treeViewMenu.clearSelectionModelContactTab();
				treeViewMenu.clearSelectionModelSharedContactTab();
			}
		}
	}

	/*
	 * In der Klasse werden alle BussinessObject auf Zhlenobjekte abgebildet.
	 * Dadruch glebt die Selektion gleich auch wenn sich das Objekt ändert. Kontakte
	 * bekommen einen positiven und Kontaklisten Schlüssel. Dadurch können Kontakte
	 * von Kontaktlisten untschieden werden,
	 * 
	 * @author Thies beareitet von @author Kurrle
	 */
	private class BusinessObjectKeyProvider implements ProvidesKey<BusinessObject> {

		@Override
		public Integer getKey(BusinessObject bo) {
			if (bo == null) {
				return null;
			}
			if (bo instanceof Contact) {
				return new Integer(bo.getId());
			} else {
				return new Integer(-bo.getId());
			}
		}
	}

	/**
	 * Setzt den Editor der Instanz.
	 * 
	 * @param editor <code>Editor</code> der gesetzt werden soll.
	 */
	public void setEditor(EditorAdmin editor) {
		this.editor = editor;
	}

	/**
	 * Setzt den User.
	 * 
	 * @param user <code>JabicsUser</code> der gesetzt werden soll.
	 */
	public void setUser(JabicsUser user) {
		this.jabicsUser = user;
	}

	/**
	 * aktualisiert den contactListDataProviders. TODO wird wahrscheinlich nicht
	 * mehr gebraucht.
	 */
	public void flushContactListProvider() {
		contactListDataProviders.refresh();
	}

	/**
	 * Setzt die momentan selktierte Kontaktliste.
	 * 
	 * @param cl <code>ContactList</code> die gesetzt werden soll.
	 */
	public void setSelectedContactList(ContactList cl) {
		if (cl != null) {
			editor.showContactList(cl);
		}
	}

	/**
	 * Auslesen der akutell selektieren <code>ContactList</code>
	 * 
	 * @return die aktuelle seletierte <code>ContactList</code>
	 */
	public ContactList getSelectedContactList() {
		return selectedContactList;
	}

	/**
	 * Setzt einen neuen Selelktieren Kontakt.
	 * 
	 * @param c Kontakt der selektiert werden soll.
	 */
	public void setSelectedContact(Contact c) {
		if (c != null) {
			editor.showContact(c);
		}
	}

	/**
	 * Ausgabe des momentan selektieren Kontaktes.
	 * 
	 * @return der momanten selektierte Kontakt.
	 */
	public Contact getSelectedContact() {
		return selectedContact;
	}

	/**
	 * Erstellen einer neuen Kontaktliste. Fügt eine neue Kotantkliste dem CellTree
	 * hinzu. Die Selektion und die Anzeige werden entsprechend aktuallisiert.
	 * 
	 * @param cl <code>ContactList</code> die dem <code>CellTree</code> hinzugefügt
	 *           werden soll.
	 */
	public void addContactList(ContactList cl) {
		// Neue Kontaktliste wird dem DataProvider hinzugefügt.
		contactListDataProviders.getList().add(cl);
		contactDataProviders.put((ContactList) cl, new ListDataProvider<Contact>());
		// Die neue Liste wird ausgew�hlt.
		selectionModel.setSelected(cl, true);
		contactListDataProviders.flush();
		contactDataProviders.get(cl).flush();

	}

	/**
	 * Kontakteliste wird aus dem CellTree Widget entfernt. Die Selektion und die
	 * Dataprovider werden ebenfalls entsprechend angepasst.
	 * 
	 * @param cl <code>ContactList</code> die entfernt werden soll.
	 */
	public void removeContactList(ContactList cl) {

		contactListDataProviders.getList().remove(cl);
		contactDataProviders.put((ContactList) cl, new ListDataProvider<Contact>());
		// Die neue Liste wird ausgew�hlt.
		selectionModel.setSelected(cl, true);
		contactListDataProviders.flush();
		contactDataProviders.get(cl).flush();
	}

	/**
	 * Aktualisiert eine Kontaktliste im CellTree-Widget.
	 * 
	 * @param cl <code>ContactList</code> welche aktuallisiert werden soll.
	 */
	public void updateContactList(ContactList cl) {
		List<ContactList> contactlists = contactListDataProviders.getList();
		int i = 0;
		for (ContactList cl2 : contactlists) {
			if (cl2.getId() == cl.getId()) {
				contactlists.set(i, cl);
				break;
			} else {
				i++;
			}
		}
		contactListDataProviders.refresh();
	}

	/**
	 * Entfernt einen Kontakt aus dem CellTree Widget.
	 * 
	 * @param c <code>Contact</code> der entfernt werden soll.
	 */
	public void removeContact(Contact c) {

		ListDataProvider<Contact> cProvider;

		// Kontaktlisten werden durchsucht
		for (ContactList cl : contactListDataProviders.getList()) {
			cProvider = contactDataProviders.get(cl);

			for (Contact c2 : cProvider.getList()) {
				// Wenn in allen Kontakten der Liste Kontakt c ist...
				if (c2.getId() == c.getId()) {
					int ii = cProvider.getList().indexOf(c2);
					cProvider.getList().remove(ii);
					contactDataProviders.get(cl).refresh();

				}
			}
		}
		return;
	}

	/*
	 * Ein altes Kontakt-Objekt wird durch einen neues mit der selbe Id ersetzt, die
	 * ID bleibt gleich! Dies ist sinnvoll, wenn sich die Eigenschafte eines
	 * Kontakts geändert haben und im Baum noch ein veraltetets Kontaktobjekt
	 * enthalten ist.
	 * 
	 */
	public void updateContact(Contact c) {

	    if (c != null) {
	      ListDataProvider<Contact> cProvider = new ListDataProvider<Contact>();
	      // Kontaktlisten werden durchsucht
	      for (ContactList cl : contactListDataProviders.getList()) {
	        try {
	          cProvider = contactDataProviders.get(cl);
	          if (cProvider.getList() != null) {

	            for (Contact c2 : cProvider.getList()) {

	              // Wenn in allen Kontakten der Liste Kontakt c ist...
	              if (c2.getId() == c.getId()) {
	                int i = cProvider.getList().indexOf(c2);
	                cProvider.getList().set(i, c);
	              }
	            }
	           //	        contactDataProviders.get(cl).refresh();
	            contactDataProviders.get(cl).flush();
	          }
	          // contactListDataProviders.flush();
	        } catch (Exception e) {
	        }
	      }
	    }
	  }

	/**
	 * Ein <code>Conact</code> wird einer bestimmen <code>ContactList</code>
	 * hinzugefügt.
	 * 
	 * @param cl <code>ContactList</code> dem der Kontakt hinzugefügt werden soll.
	 * @param c  <code>Contact</code> der Liste der hinzugeügt werden soll.
	 */
	public void addContactOfList(ContactList cl, Contact c) {

		ListDataProvider<Contact> contactsProvider = contactDataProviders.get(cl);

		contactsProvider.getList().add(c);

		contactsProvider.flush();
	}

	/**
	 * Ein <code>Conact</code> wird einer <code>ContactList</code> hinzugefügt.
	 * 
	 * @param cl <code>ContactList</code> in welcher der Kontakt liegt.
	 * @param c  <code>Contact</code> der hinzugefügt werden soll.
	 */
	public void removeContactOfContactList(ContactList cl, Contact c) {
		ListDataProvider<Contact> contactsProvider = contactDataProviders.get(cl);

		contactsProvider.getList().remove(c);

		contactDataProviders.get(cl).flush();

	}

	/**
	 * Die aktiellen Selektion wird entfernt.
	 */
	public void clearSelectionModel() {

		if (selectionModel != null) {
			this.selectionModel.clear();
		} else
			return;

	}

	public SingleSelectionModel<BusinessObject> getSelectionModel() {
		return this.selectionModel;
	}

	/**
	 * Der Inhalt des CellTrees wird hier befüllgt. Dabei dienen Kontaktlisten als
	 * obere Knoten und Kontakte als untergeordnete Knoten.
	 * 
	 * @param value der Root werd des <code>CellTres</code> .
	 */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {


		if (value.equals("Root")) {

	

			contactListDataProviders = new ListDataProvider<ContactList>();

			// Der aktuelle User wird verwendet.
			eService.getListsOf(jabicsUser, new AsyncCallback<ArrayList<ContactList>>() {

				@Override
				public void onFailure(Throwable caught) {

				}

				@Override
				public void onSuccess(ArrayList<ContactList> contactlists) {
					if (contactlists != null) {

						for (ContactList cl : contactlists) {
							// currentCL = cl;
							contactListDataProviders.getList().add(cl);

						}
						contactListDataProviders.flush();
					}

				}

			});

			// Return a node info that pairs the data with a cell.
			return new DefaultNodeInfo<ContactList>(contactListDataProviders, new ContactListCell(), selectionModel,
					null);

		}
		if (value instanceof ContactList) {

			final ListDataProvider<Contact> contactProvider = new ListDataProvider<Contact>();

			contactDataProviders.put((ContactList) value, contactProvider);


			eService.getContactsOfList((ContactList) value, jabicsUser, new AsyncCallback<ArrayList<Contact>>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(ArrayList<Contact> contacts) {
					if (contacts != null) {
						
						for (Contact c : contacts) {
							contactProvider.getList().add(c);
						}
						contactProvider.flush();
					}

				}

			});

			// Return a node info that pairs the data with a cell.
			return new DefaultNodeInfo<Contact>(contactProvider, new ContactCell(), selectionModel, null);

		}
		return null;
	}

	/**
	 * Überprüfen, ob ein Objekt eine Leaf-Node ist.
	 * 
	 * value <code>Object</code> welches überpüft werden soll.
	 */
	@Override
	public boolean isLeaf(Object value) {
		// value is of type Contact.
		return (value instanceof Contact);
	}

}
