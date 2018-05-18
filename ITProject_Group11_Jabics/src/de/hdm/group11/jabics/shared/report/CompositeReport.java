package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Realization of a composite reports. Can consist of multiple simple or composite reports. 
 * @author Kurrle and Anders
 * 
 */

public class CompositeReport extends Report implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Composite report consists of Simple Reports or Composite Reports, which are both subclasses of Report
	 */
	private ArrayList<Report> subReports = new ArrayList<Report>();
	
	/**
	 * Constructors:
	 */
	public CompositeReport() {
		super();
	}
	public CompositeReport(ArrayList<Report> reports, Paragraph head, Paragraph foot) {
		this.subReports = reports;
		/**
		 * TODO: change Date to Calendar or whatever is not deprecated
		 * this.creationDate = ;
		 */
		this.headline = head;
		this.footline = foot;
	}
	
	/**
	 * If Paragraphs headline and footline are obtainable, do not use this constructor
	 */
	public CompositeReport(ArrayList<Report> reports) {
		this.subReports = reports;
		/**
		 * TODO: change Date to Calendar or whatever is not deprecated
		 * this.creationDate = ;
		 */
		this.headline.setContent("Report for unknownUser, containing " + reports.size() + " subreports");
		this.footline.setContent("End of Report");
	}
	
	
	/**
	 * adding and removing a report to the composite Report
	 * @param r
	 */
	
	public void addReport(Report r) { 
		subReports.add(r);
	}
	
	public void removeReport(Report r) { 
		subReports.remove(r); 
	}
	
	/**
	 * Getter and Setter
	 */
	public ArrayList<Report> getSubReports() {
		return subReports;
	}

	public void setSubReports(ArrayList<Report> subReports) {
		this.subReports = subReports;
	}
	
}