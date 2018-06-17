package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

public class AllContactsInSystemReport extends CompositeReport{
	
	private ArrayList<AllContactsOfUserReport> subReports;
	
	public void addReport(AllContactsOfUserReport cr) {
		subReports.add(cr);
	}
	
	public ArrayList<AllContactsOfUserReport> getSubReports(){
		return this.subReports;
	}
	
	public void removeReport(AllContactsOfUserReport cr) {
		subReports.remove(cr);
	}
	
}