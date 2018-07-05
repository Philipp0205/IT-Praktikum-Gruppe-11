package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementierung von zusammengesetzen Reports, diser kann aus mehren simplen
 * oder zusammengesetzen Reports bestehen.
 * 
 * @author Kurrle and Anders
 * 
 */

public class CompositeReport<T extends Report> extends Report implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Ein CompositeReport besteht aus subReports und einer Kopf und Fußzeile, in
	 * denen Informationen gegeben werden
	 */
	private ArrayList<T> subReports;
	Paragraph headline;
	Paragraph footline;

	/**
	 * Leerer Konstruktor, der ausschließlich super() aufruft. Wird für die
	 * Serialisierung benötigt
	 */
	public CompositeReport() {
	}

	/**
	 * Konstruktor, der einen neuen Report für eine generische Liste aus Subreports,
	 * eine Kopf und eine Fuzeile erstellt
	 * 
	 * @param reports,
	 *            Report Objekte, die in diesem Report gespeichert sind
	 * @param head,
	 *            Kopfzeile als Paragraph
	 * @param foot,
	 *            Fußzeile als Paragraph
	 */
	public CompositeReport(ArrayList<T> reports, Paragraph head, Paragraph foot) {
		this.subReports = reports;
		/**
		 * TODO: change Date to Calendar or whatever is not deprecated this.creationDate
		 * = ;
		 */
		this.headline = head;
		this.footline = foot;
	}

	/**
	 * Konstruktor für einen Report, der ausschließlich die Subreports bestimmt.
	 * Wenn Headline und Footline bekannt sind, diesen Konstruktor nicht verwenden
	 */
	public CompositeReport(ArrayList<T> reports) {
		this.subReports = reports;
		/**
		 * TODO: change Date to Calendar or whatever is not deprecated this.creationDate
		 * = ;
		 */
		this.headline.setContent("Report for unknownUser, containing " + reports.size() + " subreports");
		this.footline.setContent("End of Report");
	}

	/**
	 * Eine toString(), die als Ergebnis einen String mit einer textuellen Ausgabe
	 * des Reports hat. Eine Ausgabe erfolgt in dieser Art: "Report for User,
	 * containing XX subreports: Created on Date YYYY-MM-DD by name"
	 */
	public String toString() {
		if (this.headline != null && this.creationDate != null && this.creator != null) {
			return this.headline.getContent() + ": Created on " + this.creationDate.toString() + " by " + this.creator.toString();
		} else {
			return this.headline.getContent();
		}
	}

	/**
	 * Hinzufügen und Entfernen eines Reports zu einem CompositeReport
	 * 
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

	// Methode überladen, damit auch nur ein String mitgegeben werden kann.
	public void setHeadline(String s) {
		this.headline = new Paragraph(s);
	}

	public Paragraph getFootline() {
		return footline;
	}

	public void setFootline(Paragraph footline) {
		this.footline = footline;
	}

	// Ebenfalls überladen
	public void setFootline(String s) {
		this.footline = new Paragraph(s);
	}

	public ArrayList<T> getSubReports() {
		return this.subReports;
	}

	public void setSubReports(ArrayList<T> subReports) {
		this.subReports = subReports;
	}

}