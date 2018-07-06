package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;

/**
 * Dies ist das Interface der Klasse <code>HTMLReportWriter</code>.
 * <p>
 * Wird dafür benutzt um einen Report in eine andere Form zu übersetzen.
 * </p>
 * 
 * @author Kurrle
 */
public abstract class ReportWriter implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Übersetzt einen AllContactsInSystemReport in das Zielformat.
	 * 
	 * @param r
	 *            AllContactsInSystemReport
	 */
	public abstract void process(AllContactsInSystemReport r);

	/**
	 * Übersetzt einen AllContactsOfUserReport in das Zielformat.
	 * 
	 * @param r
	 *            AllContactsOfUserReport
	 */
	public abstract void process(AllContactsOfUserReport r);

	/**
	 * Übersetzt einen FilteredContactsOfUserReport in das Zielformat.
	 * 
	 * @param r
	 *            FilteredContactsOfUserReport
	 */
	public abstract void process(FilteredContactsOfUserReport r);

}
