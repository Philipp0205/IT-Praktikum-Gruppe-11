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
	 * Ein CompositeReport besteht aus subReports und einer Kopf und Fußzeile, in denen Informationen gegeben werden
	 */
	private ArrayList<Report> subReports = new ArrayList<Report>();
	Paragraph headline; 
	Paragraph footline; 
	
	
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
	 * Eine toString(), die als Ergebnis einen String mit eine textuellen Ausgabe des Reports hat. Eine Ausgabe erfolgt in dieser Art:
	 * "Report for User, containing XX subreports: Created on Date YYYY-MM-DD by name"
	 */
	public String toString() {
		return this.headline +  ": Created on " + this.creationDate.toString() + " by " + this.creator.toString();
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
	public Paragraph getHeadline() {
		return headline;
	}
	public void setHeadline(Paragraph headline) {
		this.headline = headline;
	}
	public Paragraph getFootline() {
		return footline;
	}
	public void setFootline(Paragraph footline) {
		this.footline = footline;
	}
	public ArrayList<Report> getSubReports() {
		return subReports;
	}

	public void setSubReports(ArrayList<Report> subReports) {
		this.subReports = subReports;
	}
	
}