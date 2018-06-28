package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

public class AllContactsInSystemReport extends CompositeReport implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public AllContactsInSystemReport() {
		subReports = new ArrayList<AllContactsOfUserReport>();
	}
	
	private ArrayList<AllContactsOfUserReport> subReports;
	
	public void addReport(AllContactsOfUserReport cr) {
		this.subReports.add(cr);
	}
	
	public ArrayList<AllContactsOfUserReport> getSubReports(){
		return this.subReports;
	}
	
	public void removeReport(AllContactsOfUserReport cr) {
		subReports.remove(cr);
	}
	
}