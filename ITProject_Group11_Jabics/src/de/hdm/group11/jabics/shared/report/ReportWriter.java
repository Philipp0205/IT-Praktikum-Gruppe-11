package de.hdm.group11.jabics.shared.report;

/**
 * Dies ist die Grundklasse der ReportWriter. Wird dagür benutzt um in dem Report Objekte des Clients
 * in einem Menschenlesbaren Format wiederzugeben.
 * @author Philipp
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

}
