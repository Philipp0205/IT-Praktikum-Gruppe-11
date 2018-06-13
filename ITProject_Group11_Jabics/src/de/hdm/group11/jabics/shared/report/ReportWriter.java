package de.hdm.group11.jabics.shared.report;

/**
 * Dies ist die Grundklasse der ReportWriter. Wird dafür benutzt um einen Report in eine andere Form zu übersetzen.
 * @author Kurrle
 *
 */

public abstract class ReportWriter {
	/**
	 * Übersetzt einen AllContactsInSystemReport in das Zielformat.
	 * @param r
	 */
	public abstract void process(AllContactsInSystemReport r);
	
	/**
	 * Übersetzt einen AllContactsOfUserReport in das Zielformat.
	 * @param r
	 */
	public abstract void process(AllContactsOfUserReport r);
	
	/**
	 * Übersetzt einen FilteredContactsOfUserReport in das Zielformat.
	 * @param r
	 */
	public abstract void process(FilteredContactsOfUserReport r);

}
