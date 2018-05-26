package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;

public class AllContactsInSystemReport extends CompositeReport{
	
	private ArrayList<AllContactsOfUserReport> subReports;
	
	public void addReport(AllContactsOfUserReport cr) {
		subReports.add(cr);
	}
	
	public void removeReport(AllContactsOfUserReport cr) {
		subReports.remove(cr);
	}
	
	@Override
	public ArrayList<AllContactsOfUserReport> getSubReports() {
		return subReports;
	}

	@Override
	public void setSubReports(ArrayList<AllContactsOfUserReport> subReports) {
		this.subReports = subReports;
	}
	
}