package de.hdm.group11.jabics.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

public class Filter {

	/**
	 * Übergebene Kontakte nach übergebener Property filtern
	 * 
	 * @param          ArrayList<Contact> contacts
	 * @param Property p
	 * @return ArrayList<Contact>
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
	 * Übergebene Kontakte nach übergebenem String und Property filtern
	 * 
	 * @param          ArrayList<Contact> contacts
	 * @param String   s
	 * @param Property p
	 * @return ArrayList<Contact>
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

	/**
	 * Übergebene Kontakte nach übergebenem Integer und Property filtern
	 * 
	 * @param          ArrayList<Contact> contacts
	 * @param          int pv
	 * @param Property p
	 * @return ArrayList<Contact>
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
	 * Übergebene Kontakte nach übergebenem Datum und Property filtern
	 * 
	 * @param          ArrayList<Contact> contacts
	 * @param Date     pv
	 * @param Property p
	 * @return ArrayList<Contact>
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
	 * Übergebenen Kontakt mit Hilfe der übergebenen JabicsUser nach diesen filtern.
	 * Gibt den Kontakt zurück, wenn er mit einem der übergebenen finalUser geteilt
	 * ist
	 * 
	 * @param         ArrayList<JabicsUser> finalUser, die Nutzer, für die überprüft
	 *                werden soll, ob der Kontakt mit ihnen geteilt ist
	 * @param         ArrayList<JabicsUser> collaborators, die Kollaboratoren des
	 *                Kontakts
	 * @param Contact c, der Kontakt, für den herausgefunden werden soll, ob er
	 *                getielt ist
	 * @return Contact c oder null
	 */
	public static ArrayList<Contact> filterContactsByCollaborators(ArrayList<JabicsUser> finalUser,
			ArrayList<JabicsUser> collaborators, Contact c) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		boolean bol = false;
		for (int z = 0; z < finalUser.size(); z++) {
			for (int i = 0; i < collaborators.size(); i++) {
				if (finalUser.get(z).getId() == collaborators.get(i).getId()) {
					bol = true;
				}
			}
			// System.out.println(String.valueOf(finalUser.get(i).getId()));
			// System.out.println(String.valueOf(collaborators.get(i).getId()));

		}
		if(bol) result.add(c);
		return result;
	}

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
	
}