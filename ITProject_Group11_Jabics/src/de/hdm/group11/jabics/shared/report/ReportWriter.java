package de.hdm.group11.jabics.shared.report;

/**
 * Dies ist die Grundklasse der ReportWriter. Wird dag�r benutzt um in dem Report Objekte des Clients
 * in einem Menschenlesbaren Format wiederzugeben.
 * @author Philipp
 *
 */

public abstract class ReportWriter {
	/**
	 * �bersetzt einen AllContactsInSystemReport in das Zielformat.
	 * @param r
	 */
	public abstract void process(AllContactsInSystemReport r);
	
	/**
	 * �bersetzt einen AllContactsOfUserReport in das Zielformat.
	 * @param r
	 */
	public abstract void process(AllContactsOfUserReport r);

}
