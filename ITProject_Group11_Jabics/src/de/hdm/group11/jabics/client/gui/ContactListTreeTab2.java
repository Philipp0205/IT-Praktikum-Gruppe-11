package de.hdm.group11.jabics.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
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
import de.hdm.group11.jabics.client.gui.Editor;
import de.hdm.group11.jabics.resource.JabicsResources;

public class ContactListTreeTab2 implements TreeViewModel {
	
	private Contact selectedContact;
	private ContactList selectedContactList;
	private EditorServiceAsync eService = ClientsideSettings.getEditorService();
	private EditorServiceAsync eService2 = ClientsideSettings.getEditorService();
	//Instanziierung des Singelton-Objektes
	//private LoginInfo loginfo = LoginInfo.getloginInfo();
	JabicsUser jabicsUser;
	Editor editor;
	
	ContactList currentCL;
	
	/*
	 * Der DataProvider ist dafür zuständig, die Anzeige zu aktualisieren, immer wenn etwas geändert wird. 
	 * Also Controller (m-v-c-Modell), zwischen der Anzeige (CellTable) und dem Modell (Liste von Objekten).
	 * 
	 * In diesem Fall werden werden Kontaktlisten bereitgestellt. 
	 */
	private ListDataProvider<ContactList> contactListDataProviders =  new ListDataProvider<ContactList>();
	
	public ContactListTreeTab2() {
		GWT.log("2: Konstruktor ContactListTreeTab");

		boKeyProvider = new BusinessObjectKeyProvider();
		// "A simple selection model, that allows only one item to be selected a time." 
		
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		
		/*
		 * Assoziativspeicher, bei dem Kontakte Kontaktlisten zugeordnet werden.
		 * Freunde --> Max Mustermann 
		 * 
		 * (wird weiter unten deklariert)
		 */
		contactDataProviders = new HashMap<ContactList, ListDataProvider<Contact>>();
		
	}
	

	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			BusinessObject selection = selectionModel.getSelectedObject();
			GWT.log("selectionchange");
			if (selection instanceof Contact) {;
				setSelectedContact((Contact) selection);
			} else if (selection instanceof ContactList) {
				setSelectedContactList((ContactList) selection);
			}

		}

	}
	/*
	 * In der Map werden die ListDataProviders f�r die expandierten Kontakte gepespeichert.
	 * 
	 * Das Java Map Interface "mappt" einzigartige Schl�ssel (keys) und den zugeh�rigen Wert (value), vergleichbar mit einem W�rterbuch oder 
	 * Zuweisungstabellen in der DB. Die values k�nnen jeder Zeit anhand der Keys aufgerufen werden. Also ein Assoziativspeicher. 
	 * 
	 * Beispiel: 
	 * key: 1234 --> Value: Kontakt (Max, Mustermann, 1990, ...)  
	 * 
	 */
	private Map<ContactList, ListDataProvider<Contact>> contactDataProviders = null;
	
	/**
	 * In folgender Klasse werden BusinessObjects auf eindeutige Zahlenobjekte abgebildet, die als Schl�ssel f�r Baumknoten dienen. 
	 * Dadurch werden im Selektionsmodell alle Objekte mit derselben id selektiert, wenn eines davon selektiert wird. Der
	 * Schl�ssel f�r Kontaktobjekte ist eine positive, der f�r Kundenobjekte eine
	 * negative Zahl, die sich jeweils aus der id des Objektes ergibt. Dadurch
	 * k�nnen Kunden- von Kontenobjekten unterschieden werden, auch wenn sie dieselbe id haben.
	 * 
	 * @author Thies
	 */
	private class BusinessObjectKeyProvider implements ProvidesKey<BusinessObject> {

		@Override
		public Integer getKey(BusinessObject bo) {
			if (bo ==null) {
				return null;
			}
			if (bo instanceof Contact) {
				return new Integer(bo.getId());
			} else {
				return new Integer(-bo.getId());
			}
		}
		
	}
	
	private BusinessObjectKeyProvider boKeyProvider;
	
	private SingleSelectionModel<BusinessObject> selectionModel;
	
	/**
	 * Implementation der GWT Klasse SelectionsChangeEvent. Diese Methode regelt, was passiert, wenn ein Objekt
	 * im Baum ausgew�hlt wird. Es wird zwischen ausgew�hlten Kontakten und Kontaktlisten unterschieden.
	 *
	 */
	
	public void setEditor(Editor editor) {
		GWT.log("Editor setzen in contactlisttree");
		GWT.log("Editor: " + editor.hashCode());
		this.editor = editor;
	}

	public void setSelectedContactList(ContactList cl) {
		//selectedContactList = cl;
		GWT.log("2.2 ausgewählt " + cl.getListName());
		editor.showContactList(cl);
	}
	
	public ContactList getSelectedContactList() {
		return selectedContactList;
	}
	
	public void setSelectedContact(Contact c) {
		//selectedContact	= c;
		// momentan aktiver User muss angegeben werden
		GWT.log("2.2 Zurück zum Editor: " + editor.hashCode() + c.getName());
		editor.showContact(c);
	}

	
	 public Contact getSelectedContact() {
		return selectedContact;
	}
	 
	 /**
	  * Erstellen einer neuen Kontaktliste.
	  */
	 public void addContactList(ContactList cl) { 
		 //Neue Kontaktliste wird dem DataProvider hinzugef�gt.
		 contactListDataProviders.getList().add(cl);
		 //Die neue Liste wird ausgew�hlt.
		 selectionModel.setSelected(cl, true);
		 
	 }
	 
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
	 
	 public void removeContactList(Contact cl) {
		 contactListDataProviders.getList().remove(cl);
		 contactDataProviders.remove(cl);
	 }
	 
	 /*
	  * Weiter zu den Kontakten
	  */	 
	 public void addContactOfList(ContactList cl, Contact c) {
		 // wenn es noch keinen Kontaktlisten Provider f�r den Kontakt gitb, dann wurde der Baum noch nicht ge�ffnet und es passiert nichts.
		 if (!contactDataProviders.containsKey(cl)) {
			 return;
		 }
		 ListDataProvider<Contact> contactsProvider = contactDataProviders.get(cl);
		 if (!contactsProvider.getList().contains(c)) {
			 contactsProvider.getList().add(c);
			 
		 }
		 selectionModel.setSelected(c, true);
	 }
	 
	 public void removeContactOfContactList(ContactList cl, Contact c) {
		 if (!contactDataProviders.containsKey(cl)) {
			 return;
		 }
		 contactDataProviders.get(cl).getList().remove(c);
		 selectionModel.setSelected(cl, true);
	 }
	 
	 /* 
	  * Ein altes Kontakt-Objekt wird durch einen neues mit der selbe Id ersetzt, die ID bleibt gleich! 
	  * Dies ist sinnvoll, wenn sich die Eigenschafte eines Kontakts geändert haben und im Baum
	  * noch ein veraltetets Kontaktobjekt enthalten ist.
	  * 
	  * Diese Methode funktioniert nocht nicht 
	  */
	 public void updateContact(Contact c) {
		 //eService.getContactById(c.getOwner().getId(), new UpdateAccountCallback(c));
	 }
	 
	 /*
	  *  Funktioniert so noch nicht.
	  */
	 private class UpdateContactCallback implements AsyncCallback<ContactList> {
		 
		 Contact contact = null;
		 
		 UpdateContactCallback(Contact c) {
			 contact = c;
		 }

		@Override
		public void onFailure(Throwable caught) {
			// Nix.
			
		}

		@Override
		public void onSuccess(ContactList cl) {
			List<Contact> contacts = contactDataProviders.get(cl).getList();
			for (int i=0; i < contacts.size(); i++) {
				if (contact.getId() == contacts.get(i).getId()) {
					contacts.set(i, contact);
					break;
				}
			}
			
		}
		 
	 }


	/**
     * Get the {@link NodeInfo} that provides the children of the specified
     * value.
     */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		GWT.log("2.2 TreeTab: getNodeInfo start.");
		GWT.log("2.2 TreeTab: Value: " + value.toString());
		
		if (value.equals("Root")) {
			GWT.log("2.2 TreeTab: value.equals");
			
			contactListDataProviders = new ListDataProvider<ContactList>();
			
			JabicsUser user2 = new JabicsUser(1);
			//JabicsUser jabicsUser2 = new JabicsUser();
			//GWT.log("2.2 ContatListTree: User erstellen" );
			//GWT.log(jabicsUser2.toString());
			//Der aktuelle User wird verwendet.
			GWT.log("2.2 aktueller User: " + user2.getId());
			eService2.getListsOf(user2, new AsyncCallback<ArrayList<ContactList>>() {
				
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("2.2 TreeTab: onFailure");
					
				}
				@Override
				public void onSuccess(ArrayList<ContactList> contactlists) {
					GWT.log(contactlists.toString());
					
					for (ContactList cl : contactlists) {
						currentCL = cl;
						GWT.log("2.2 Add CotactList " + cl.toString());
						contactListDataProviders.getList().add(cl);
						contactListDataProviders.flush();
					}
					GWT.log("2.2 TreeTab onSuccess fertig");
				}
				
				
				
			});
			
		// Return a node info that pairs the data with a cell.	
		GWT.log("2.2 ContactTree DefaultNodeInfo1");
		return new DefaultNodeInfo<ContactList>(contactListDataProviders, new ContactListCell(), selectionModel, null);
			
		}
		
		if (value instanceof ContactList) {
			GWT.log("2.2 TreeTab: instanceof ContactList");
			GWT.log("2.2 ContactList" + currentCL.toString());
			
			JabicsUser user2 = new JabicsUser();
			user2.setId(1);
			
			final ListDataProvider<Contact> contactProvider = new ListDataProvider<Contact>();
			contactDataProviders.put((ContactList) value, contactProvider);
			
			GWT.log("CurrentCL: " + currentCL.toString());
			
			eService.getContactsOfList(currentCL, user2, new AsyncCallback<ArrayList<Contact>>() {
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("2.2 TreeTab value instanceof ContactList onFailure");	
				}
				@Override
				public void onSuccess(ArrayList<Contact> contacts) {
					GWT.log("2.2 TreeTab value instanceof ContactList onSuccess");	
					GWT.log("Contacts" + contacts.toString());
					for (Contact c : contacts) {
						GWT.log("2.2 Add Contact " + c.toString());
						contactProvider.getList().add(c);		
					}			
					contactProvider.flush();
				}		
			});
			
			GWT.log("2.2 DefaultNodeInfo2");
			// Return a node info that pairs the data with a cell.
			return new DefaultNodeInfo<Contact>(contactProvider, new ContactCell(), selectionModel, null);
			
		}
		return null;
	}
	
	/**
	 * Überprüfen, ob ein Objekt eine Leaf-Node ist
	 */
	@Override
	public boolean isLeaf(Object value) {
		// value is of type Contact.
		return (value instanceof Contact);
	}
	
	

}
