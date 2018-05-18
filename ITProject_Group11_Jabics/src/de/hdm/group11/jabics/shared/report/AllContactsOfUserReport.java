package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

public class AllContactsOfUserReport {

	private ArrayList<ContactReport> subReports;
	
	public void addReport(ContactReport cr) {
		subReports.add(cr);
	}
	
	public void removeReport(ContactReport cr) {
		subReports.remove(cr);
	}
	
	public ArrayList<ContactReport> getSubReports() {
		return subReports;
	}

	public void setSubReports(ArrayList<ContactReport> subReports) {
		this.subReports = subReports;
	}
	
}