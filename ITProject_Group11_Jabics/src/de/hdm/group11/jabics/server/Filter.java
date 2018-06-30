package de.hdm.group11.jabics.server;

import java.util.ArrayList;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.JabicsUser;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.bo.Property;

public class Filter {

	public static ArrayList<Contact> filterContactsByProperty(ArrayList<Contact> contacts, Property p) {
		ArrayList<Contact> result = new ArrayList<Contact>();

		for (Contact c : contacts) {
			System.out.println("Kontakt filtern: " + c.getName());
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue pv : pvalues) {
				System.out.println("PValue filtern: " + p.getLabel() + "|" + pv.getProperty().getLabel());
				// Die Logik der Methode
				if (p.getLabel() != null && pv.getProperty().getLabel().equals(p.getLabel())) {
					System.err.println("Gefunden");
					result.add(c);
				}
			}
		}
		return result;
	}

	public static ArrayList<Contact> filterContactsByString(ArrayList<Contact> contacts, String pv) {
		ArrayList<Contact> result = new ArrayList<Contact>();
		
		for (Contact c : contacts) {
			Boolean bol = false;
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getStringValue() != null) {
					if (pv != null && (p.getStringValue() == pv || p.getStringValue().contains(pv))) { // zu definiert:	// p.getProperty().getLabel().contains(pv)){
						bol=true;	
					}
				}
			}
			if((!result.contains(c))&&bol)result.add(c);
		}
		return result;
	}

	public static ArrayList<Contact> filterContactsByInt(ArrayList<Contact> contacts, int pv, Property prop) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		for (Contact c : contacts) {
			Boolean bol = false;
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if(p.getPointer()==1) {
				if(pv==-2147483648) {
					System.out.println(String.valueOf(pv));
					if (p.getProperty().getLabel().equals(prop.getLabel())) {
						bol=true;
				}
				
				}else {
					Integer integ = (Integer) pv;
					if (p.getIntValue() == pv || p.getProperty().getLabel().contains(integ.toString()) ) {  //|| p.getStringValue().contains(integ.toString()) 
						bol=true;	
						System.out.println(String.valueOf(pv));
				}
				}	
				}
			}
			
			if((!result.contains(c))&&bol) {
				result.add(c);
			System.out.println("kukuk3");
			}
		}
		return result;
	}

	public static ArrayList<Contact> filterContactsByDate(ArrayList<Contact> contacts, Date pv) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		for (Contact c : contacts) {
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getDateValue() == pv) {
					result.add(c);
				}
			}
		}
		return result;
	}

	public static ArrayList<Contact> filterContactsByCollaborators(ArrayList<JabicsUser> finalUser,
			ArrayList<JabicsUser> collaborators, Contact c) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		for (int z = 0; z < finalUser.size(); z++) {
			for (int i = 0; i < collaborators.size(); i++) {
				if (finalUser.get(z).getId() == collaborators.get(i).getId()) {
					result.add(c);
				}
			}
//			System.out.println(String.valueOf(finalUser.get(i).getId()));
//			System.out.println(String.valueOf(collaborators.get(i).getId()));

		}
		return result;
	}

	public static ArrayList<Contact> filterContactsByFloat(ArrayList<Contact> contacts, float pv) {

		ArrayList<Contact> result = new ArrayList<Contact>();

		for (Contact c : contacts) {
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getFloatValue() == pv) {
					result.add(c);
				}
			}
		}
		return result;
	}
}