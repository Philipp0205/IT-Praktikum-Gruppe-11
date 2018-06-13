package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Implementierung von zusammengesetzen Reports, diser kann aus mehren simplen oder zusammengesetzen
 * Reports bestehen.
 * @author Kurrle and Anders
 * 
 */

public class CompositeReport<T> extends Report implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Ein CompositeReport besteht aus subReports und einer Kopf und Fußzeile, in denen Informationen gegeben werden
	 */
	private ArrayList<T> subReports = new ArrayList<T>();
	Paragraph headline; 
	Paragraph footline; 
	
	
	/**
	 * Constructors:
	 */
	public CompositeReport() {
		super();
	}
	public CompositeReport(ArrayList<T> reports, Paragraph head, Paragraph foot) {
		this.subReports = reports;
		/**
		 * TODO: change Date to Calendar or whatever is not deprecated
		 * this.creationDate = ;
		 */
		this.headline = head;
		this.footline = foot;
	}
	
	/**
	 * Wenn Headline und Footline bekannt sind, diesen Konstruktor nicht verwenden
	 */
	public CompositeReport(ArrayList<T> reports) {
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
	 * Hinzufügen und Entfernen eines Reports zu einem CompositeReport
	 * @param r
	 */
	
	public void addReport(T report) { 
		subReports.add(report);
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

	// Methode �berladen damit auch nur ein String mitgegeben werden kann. 
	public void setHeadline(String s) {
		this.headline = new Paragraph(s);
	}
	public Paragraph getFootline() {
		return footline;
	}
	public void setFootline(Paragraph footline) {
		this.footline = footline;
	}
	// Ebenfalls �berladen
	public void setFootline(String s) {
		this.footline = new Paragraph(s);
	}
	public ArrayList<T> getSubReports() {
		return subReports;
	}
	public void setSubReports(ArrayList<T> subReports) {
		this.subReports = subReports;
	}
	
}