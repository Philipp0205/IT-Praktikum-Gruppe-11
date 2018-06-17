package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

public class AllContactsOfUserReport extends CompositeReport{

	private ArrayList<ContactReport> subReports;
	
	public AllContactsOfUserReport() {
		super();
	}
	
	public AllContactsOfUserReport(ArrayList<ContactReport> reports) {
		this();
		this.subReports = reports;
	}
	
	public void addReport(ContactReport cr) {
		subReports.add(cr);
	}
	
	public void removeReport(ContactReport cr) {
		subReports.remove(cr);
	}
	
}