package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Realization of a composite reports. Can consist of multiple simple reports. 
 * @author Philipp
 * 
 */

public class CompositeReport extends Report implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 *  TODO DOKU: Die ArrayList besteht nicht aus UserReports. 
	 *  
	 */
	private ArrayList<Report> userReports = new ArrayList<Report>();
	
	public void addUserReport(Report r) { 
		userReports.add(r);
	}
	
	public void removeUserReport(Report r) { 
		userReports.remove(r); 
	}
	
	
	
	

}
