package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementierung von zusammengesetzen <code>CompositeReport</code>, diser kann
 * aus mehren simplen oder zusammengesetzen <code>Report</code> bestehen.
 * 
 * @author Kurrle
 * @author Anders
 * @author Stahl
 */

public class CompositeReport<T extends Report> extends Report implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Eine Instanz von
	 * <code>CompositeReport<code> besteht aus mehreren <code>subReports</code>.
	 */
	private ArrayList<T> subReports;

	/**
	 * Kopfzeile einer Instanz dieser Klasse.
	 */
	Paragraph headline;

	/**
	 * Fusszeile einer Instanz dieser Klasse.
	 */
	Paragraph footline;

	/**
	 * Default Konstruktor.
	 */
	public CompositeReport() {
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse für eine generische Liste aus
	 * <code>subReports</code>, <code>headline</code> und einer
	 * <code>footline</code> zu erstellen.
	 * 
	 * @param reports
	 * @param headline
	 * @param footline
	 */
	public CompositeReport(ArrayList<T> reports, Paragraph headline, Paragraph footline) {
		this.subReports = reports;
		this.headline = headline;
		this.footline = footline;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse für eine generische Liste aus
	 * <code>subReports</code> zu erstellen.
	 * 
	 * @param reports
	 */
	public CompositeReport(ArrayList<T> reports) {
		this.subReports = reports;
		this.headline.setContent("Report for unknownUser, containing " + reports.size() + " subreports");
		this.footline.setContent("End of Report");
	}

	/**
	 * <p>
	 * Textuelle Repräsentation des <code>CompositeReport</code> Objekts.
	 * </p>
	 * Eine Ausgabe erfolgt in dieser Art: "Report for User, containing XX
	 * subreports: Created on Date YYYY-MM-DD by name"
	 */
	public String toString() {
		if (this.headline != null && this.creationDate != null && this.creator != null) {
			return this.headline.getContent() + ": Created on " + this.creationDate.toString() + " by "
					+ this.creator.toString();
		} else {
			return this.headline.getContent();
		}
	}

	/**
	 * <code>Report</code> zu einem <code>subReport</code> hinzufügen.
	 * 
	 * @param report
	 */
	public void addReport(T report) {
		subReports.add(report);
	}

	/**
	 * <code>Report</code> von einem <code>subReport</code> entfernen.
	 * 
	 * @param report
	 */
	public void removeReport(Report report) {
		subReports.remove(report);
	}

	/**
	 * Auslesen der <code>headline</code>.
	 * 
	 * @return headline
	 */
	public Paragraph getHeadline() {
		return headline;
	}

	/**
	 * Setzen der <code>headline</code>.
	 * 
	 * @param headline
	 */
	public void setHeadline(Paragraph headline) {
		this.headline = headline;
	}

	/**
	 * Setzen der <code>headline</code> mit einem <code>String</code>.
	 * 
	 * @param string
	 */
	public void setHeadline(String string) {
		this.headline = new Paragraph(string);
	}

	/**
	 * Auslesen der <code>footline</code>.
	 * 
	 * @return footline
	 */
	public Paragraph getFootline() {
		return footline;
	}

	/**
	 * Setzen der <code>footline</code>.
	 * 
	 * @param footline
	 */
	public void setFootline(Paragraph footline) {
		this.footline = footline;
	}

	/**
	 * Setzen der <code>footline</code> mit einem <code>String</code>.
	 * 
	 * @param string
	 */
	public void setFootline(String string) {
		this.footline = new Paragraph(string);
	}

	/**
	 * Auslesen der <code>subReports</code>.
	 * 
	 * @return subReports
	 */
	public ArrayList<T> getSubReports() {
		return this.subReports;
	}

	/**
	 * Setzen der <code>subReports</code>.
	 * 
	 * @param subReports
	 */
	public void setSubReports(ArrayList<T> subReports) {
		this.subReports = subReports;
	}

}