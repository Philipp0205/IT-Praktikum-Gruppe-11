package de.hdm.group11.jabics.server;

import java.util.ArrayList;
import java.time.LocalDateTime;

import de.hdm.group11.jabics.shared.bo.Contact;
import de.hdm.group11.jabics.shared.bo.PValue;
import de.hdm.group11.jabics.shared.report.ContactReport;
import de.hdm.group11.jabics.shared.report.PropertyView;

public class Filter {
	
	public static ArrayList<Contact> filterContactsByString(ArrayList<Contact> contacts, String pv) {
			
			ArrayList<Contact> result = new ArrayList<Contact>();
			
			for (Contact c : contacts ) {
				ArrayList<PValue> pvalues = c.getValues();
				for (PValue p : pvalues) {
					if (p.getStringValue() == pv) {
						result.add(c);			
					} 
				} 	
			} 
			return result;
	}

	public static ArrayList<Contact> filterContactsByInt(ArrayList<Contact> contacts, int pv) {
		
		ArrayList<Contact> result = new ArrayList<Contact>();
		
		for (Contact c : contacts ) {
			ArrayList<PValue> pvalues = c.getValues();
			for (PValue p : pvalues) {
				if (p.getIntValue() == pv) {
					result.add(c);			
				} 
			} 	
		} 
		return result;
	}
	
	public static ArrayList<Contact> filterContactsByDate(ArrayList<Contact> contacts, LocalDateTime pv) {
			
			ArrayList<Contact> result = new ArrayList<Contact>();
			
			for (Contact c : contacts ) {
				ArrayList<PValue> pvalues = c.getValues();
				for (PValue p : pvalues) {
					if (p.getDateValue() == pv) {
						result.add(c);			
					} 
				} 	
			} 
			return result;
	}
	
	public static ArrayList<Contact> filterContactsByFloat(ArrayList<Contact> contacts, float pv) {
		
		ArrayList<Contact> result = new ArrayList<Contact>();
		
		for (Contact c : contacts ) {
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