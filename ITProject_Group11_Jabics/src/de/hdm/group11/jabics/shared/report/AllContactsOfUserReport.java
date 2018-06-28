package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

public class AllContactsOfUserReport extends CompositeReport implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ContactReport> subReports = new ArrayList<ContactReport>();
	
	public AllContactsOfUserReport() {}
	
	public AllContactsOfUserReport(ArrayList<ContactReport> reports) {
		this();
		this.subReports = reports;
	}
	
	public void addReport(ContactReport cr) {
		this.subReports.add(cr);
	}
	
	public void removeReport(ContactReport cr) {
		subReports.remove(cr);
	}
	
	public ArrayList<ContactReport> getSubReports() {
		return this.subReports;
	}
	
}