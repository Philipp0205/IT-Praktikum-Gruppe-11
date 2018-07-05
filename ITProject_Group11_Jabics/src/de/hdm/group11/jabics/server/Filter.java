package de.hdm.group11.jabics.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

/**
 * <p>
 * Diese Klasse stellt Filter Methoden bereit, um Objekt des Package
 * <code>de.hdm.group11.jabics.shared.bo</code> zu sortieren.
 * </p>
 * 
 * @author Anders
 * @author Brase
 */
public class Filter {

	/**
	 * Übergebenes <code>Contact</code> Objekt mit Hilfe der übergebenen
	 * <code>JabicsUser</code>, Kollaboratoren nach diesen filtern. Gibt den
	 * <code>Contact</code> zurück, wenn er mit einem der übergebenen
	 * <code>JabicsUser</code> geteilt ist.
	 * 
	 * @param finalUser
	 *            <code>JabicsUser</code> Objekte, für die das <code>Contact</code>
	 *            Objekte angefragt werden.
	 * @param collaborator
	 *            <code>JabicsUser</code> Objekte, welche eine Collaboration
	 *            besitzen.
	 * @param c
	 *            angefragtes <code>Contact</code> Objekt.
	 * @return Liste der <code>Contact</code> Objekte.
	 */
	public static ArrayList<Contact> filterContactsByCollaborators(ArrayList<JabicsUser> finalUser,
			ArrayList<JabicsUser> collaborators, Contact c) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		for (int z = 0; z < finalUser.size(); z++) {
			boolean bol = false;
			for (int i = 0; i < collaborators.size(); i++) {
				if (finalUser.get(z).getId() == collaborators.get(i).getId()) {
					bol = true;
				}
			}
			if (bol)
				result.add(c);
		}
		return result;
	}

	/**
	 * Eine Liste von <code>Contact</code> Objekten nach einem <code>Date</code>
	 * filtern.
	 * 
	 * @param contacts
	 *            die <code>Contact</code> Objekte, welche gefiltert werden sollen.
	 * @param pv
	 *            das <code>Date</code> nach dem gefiltert werden soll.
	 * @return Die Liste aus gefilterten <code>Contact</code> Objekten.
	 */
	public static ArrayList<Contact> filterContactsByDate(ArrayList<Contact> contacts, Date pv) {

		ArrayList<Contact> result = new ArrayList<Contact>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (Contact c : contacts) {
			boolean bol = false;
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getPointer() == 3) {
					if (dateFormat.format(p.getDateValue()).equals(dateFormat.format(pv))) {
						bol = true;
						for (Contact c2 : result) {
							if (c2.getId() != c.getId()) {
								bol = false;
							}
						}
					}
				}
			}
			if (bol) {
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * Eine Liste von <code>Contact</code> Objekten nach einem <code>float</code>
	 * filtern.
	 * 
	 * @param contacts
	 *            die <code>Contact</code> Objekte, welche gefiltert werden sollen.
	 * @param pv
	 *            das <code>float</code> nach dem gefiltert werden soll.
	 * @return Die Liste aus gefilterten <code>Contact</code> Objekten.
	 */
	public static ArrayList<Contact> filterContactsByFloat(ArrayList<Contact> contacts, float pv) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		for (Contact c : contacts) {
			boolean bol = false;
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getPointer() == 4) {
					if (p.getFloatValue() == pv) {
						bol = true;
						for (Contact c2 : result) {
							if (c2.getId() != c.getId()) {
								bol = false;
							}
						}
					}
				}
			}
			if (bol) {
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * Eine Liste von <code>Contact</code> Objekten nach einem <code>int</code>
	 * filtern.
	 * 
	 * @param contacts
	 *            die <code>Contact</code> Objekte, welche gefiltert werden sollen.
	 * @param pv
	 *            das <code>int</code> nach dem gefiltert werden soll.
	 * @return Die Liste aus gefilterten <code>Contact</code> Objekten.
	 */
	public static ArrayList<Contact> filterContactsByInt(ArrayList<Contact> contacts, int pv) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		for (Contact c : contacts) {
			boolean bol = false;
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getPointer() == 1) {
					if (pv == p.getIntValue()) {
						bol = true;
						for (Contact c2 : result) {
							if (c2.getId() != c.getId()) {
								bol = false;
							}
						}
					}
				}
			}
			if (bol) {
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * Eine Liste von <code>Contact</code> Objekten nach <code>Property</code>
	 * Objekte filtern.
	 * 
	 * @param contacts
	 *            die <code>Contact</code> Objekte, welche gefiltert werden sollen.
	 * @param p
	 *            das <code>Property</code> Objekt nach dem gefiltert werden soll.
	 * @return Die Liste aus gefilterten <code>Contact</code> Objekten.
	 */
	public static ArrayList<Contact> filterContactsByProperty(ArrayList<Contact> contacts, Property p) {
		ArrayList<Contact> result = new ArrayList<Contact>();

		for (Contact c : contacts) {
			boolean bol = false;
			System.out.println("Kontakt filtern: " + c.getName());
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue pv : pvalues) {
				System.out.println("PValue filtern: " + p.getLabel() + "|" + pv.getProperty().getLabel());
				// Die Logik der Methode
				if (p.getLabel() != null && pv.getProperty().getLabel().equals(p.getLabel())) {
					System.err.println("Gefunden");
					bol = true;
					for (Contact c2 : result) {
						if (c.getId() == c2.getId())
							bol = false;
					}
				}
			}
			if (bol)
				result.add(c);
		}
		return result;
	}

	/**
	 * Eine Liste von <code>Contact</code> Objekten nach einem <code>String</code>
	 * filtern.
	 * 
	 * @param contacts
	 *            die <code>Contact</code> Objekte, welche gefiltert werden sollen.
	 * @param pv
	 *            der <code>String</code> nach dem gefiltert werden soll.
	 * @return Die Liste aus gefilterten <code>Contact</code> Objekten.
	 */
	public static ArrayList<Contact> filterContactsByString(ArrayList<Contact> contacts, String pv) {
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (Contact c : contacts) {
			boolean bol = false;
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getPointer() == 2) {
					if (p.getStringValue() == pv || p.getStringValue().contains(pv)) { // zu definiert:
						bol = true;
						for (Contact c2 : result) {
							if (c2.getId() != c.getId()) {
								bol = false;
							}
						}
					}
				}
			}
			if (bol) {
				result.add(c);
			}
		}
		return result;
	}
}