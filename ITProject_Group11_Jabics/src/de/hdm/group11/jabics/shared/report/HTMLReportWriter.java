package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

/**
 * Implementiert die Methoden aus <code>ReportWriter</code>, um Reports in das
 * HTML-Format zu übersetzen.
 * 
 * @author Anders
 *
 */
public class HTMLReportWriter extends ReportWriter implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Speichert das Ergebnis einer process Methode. Format: HTML
	 */
	private String reportText = "";

	/**
	 * Zurücksetzen der Variable <code>reportText</code>.
	 */
	public void resetReportText() {
		this.reportText = "";
	}

	/**
	 * Auslesen des <code>reportText</code>.
	 * 
	 * @return String
	 */
	public String getReportText() {
		return this.reportText;
	}

	/**
	 * Konvertieren eines <code>Paragraph</code> in HTML.
	 * 
	 * @param p
	 *            das <code>Paragraph</code> Objekt.
	 * 
	 * @return den <code>String</code> in HTML-Format
	 */
	public String paragraph2HTML(Paragraph p) {

		return "<p>" + p.getContent() + "</p>";
	}

	/**
	 * Konvertieren eines <code>Paragraph</code> mit Filter in HTML.
	 * 
	 * @param p
	 * 
	 * @return String
	 */
	public String paragraphWithFilter2HTML(Paragraph p) {
		StringBuffer filt = new StringBuffer();
		filt.append("<p>Es wurde nach \"");
		for (String s : p.getFiltercriteria()) {
			if (s != null) {
				filt.append(s + ", ");
			}
		}
		filt.replace(0, filt.length(), filt.substring(0, filt.length() - 2));
		filt.append("\" gefiltert. </p>");
		return filt.toString();
	}

	/**
	 * Den Kopf eines Reports in HTML überführen.
	 * 
	 * @param r
	 *            CompositeReport
	 * @return String in HTML-Format
	 */
	public String createHeadOfReport(CompositeReport r) {

		if (r.getCreator() != null && r.getHeadline() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div id=\"report\">");
			sb.append("<h3>" + r.getHeadline().getContent() + "</h3>");
			sb.append("<h5> Report für " + r.getCreator().getContent()
					 + ", erstellt am " + r.getCreationDateAsString() + "</h5>");
			return sb.toString();
		} else
			return "<div id=\"report\" style=\"margin-bottom: 16px\"> <h3>Report</h3>";
	}

	/**
	 * Mehrere ContactReports in HTML überführen. Die Reports werden in einer
	 * Tabellenstruktur ausgegeben
	 * 
	 * @param cons
	 *            Liste von <code>ContactReport</code>, die in HTML überführt werden
	 *            sollen
	 * @return String im korrekten HTML Format.
	 */
	public String convertContactReportsToHTML(ArrayList<ContactReport> cons) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"report\">");
		sb.append("<table class=\"reportTable\">");
		// die Zeilen pro Kontakt füllen
		for (ContactReport c : cons) {
			if (c.getContactInfo() != null) {
				sb.append(" <tr class=\"contactReport1\"> <td> <b>" + c.getContactInfo().getContent() + "</b> </td>");
			} else {
				sb.append("<tr class=\"contactReport1\"><td> <b>Kein Anzeigename</b> </td>");
			}
			sb.append("<td class=\"reportCell\"> <p>Besitzer: " + c.getUserInfo().getContent() + "</p> </td>");
			sb.append("<td class=\"reportCell\"> <p>Teilhaber: " + c.getCollaboratorInfo().getContent() + "</p> </td>");
			sb.append(
					"<td class=\"reportCell\"> <p>Erstellt am " + c.getCreationInfo().getContent() + "</p>" + "</td>");
			sb.append("<td class=\"reportCell\"> <p>Zuletzt geändert: " + c.getUpdateInfo().getContent() + "</p>"
					+ "</td>");

			sb.append("</tr><tr class=\"contactReport2\">");
			for (PropertyView pv : c.getContent()) {
				if (pv.getPname() != null && pv.getPvalue() != null) {
					sb.append("<td class=\"reportCell\"> <b>" + pv.getPname() + "</b> \n <p>" + pv.getPvalue()
							+ " </p> </td>");
				} else if (pv.getPname() != null) {
					sb.append("<td class=\"reportCell\"> <p> " + pv.getPname() + "</p> </td>");
				} else if (pv.getPvalue() != null) {
					sb.append("<td class=\"reportCell\"> <p> " + pv.getPvalue() + "</p> </td>");
				}
			}
			sb.append("</tr><tr class=\"reportSpacer\"> <td> <b>&nbsp</b> </td>");
		}

		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * Einen AllContactsInSystemReport in HTML überführen. Speichert das Ergebnis in
	 * der Instanzenvariable "reportText", die über getReportText() abgerufen werden
	 * kann.
	 */
	public void process(AllContactsInSystemReport r) {
		StringBuffer sb = new StringBuffer();
		/*
		 * Dem Ergebnis einen Kopf, Text unterhalb des Kopfes, Tabelle mit allen
		 * auszugebenden Kontakten und eine Fußzeile hinzufügen
		 */
		sb.append(createHeadOfReport(r));
		sb.append(paragraph2HTML(r.getHeadline()));
		/*
		 * Da ein AllContactsInSystem Report viele UserReports speichert, diese
		 * durchlaufen und für jeden die Tabelle mit allen Kontakten ausgeben
		 */
		for (AllContactsOfUserReport acur : r.getSubReports()) {
			sb.append("<p style=\"margin-bottom: 8px\">Alle Kontakte von " + acur.getCreator().getContent() + "</p>");
			sb.append(convertContactReportsToHTML(acur.getSubReports()));
			sb.append(
					"<p style=\"margin-bottom: 16px\">Ende des Reports von " + acur.getCreator().getContent() + "</p>");
		}
		sb.append(paragraph2HTML(r.getFootline()));
		sb.append("</div>");
		this.reportText = sb.toString();
	}

	/**
	 * Einen AllContactsOfUserReport in HTML überführen. Speichert das Ergebnis in
	 * der Instanzenvariable "reportText", die über getReportText() abgerufen werden
	 * kann.
	 * 
	 * @param r AllContactsOfUserReport der konvertiert werden soll
	 */
	public void process(AllContactsOfUserReport r) {
		StringBuffer sb = new StringBuffer();
		/**
		 * Dem Ergebnis einen Kopf, Text unterhalb des Kopfes, eine Tabelle mit allen
		 * auszugebenden Kontakten und eine Fußzeile hinzufügen
		 */
		sb.append(createHeadOfReport(r));
		sb.append(paragraph2HTML(r.getHeadline()));
		sb.append(convertContactReportsToHTML(r.getSubReports()));
		sb.append(paragraph2HTML(r.getFootline()));
		sb.append("</div>");
		this.reportText = sb.toString();
	}

	/**
	 * Einen FilteredContactsOfUserReport in HTML überführen. Speichert das Ergebnis
	 * in der Instanzenvariable "reportText", die über getReportText() abgerufen
	 * werden kann.
	 * 
	 * @param r FilteredContactsOfUserReport der konvertiert werden soll
	 */
	public void process(FilteredContactsOfUserReport r) {
		StringBuffer sb = new StringBuffer();
		/**
		 * Dem Ergebnis einen Kopf, Text unterhalb des Kopfes, eine Tabelle mit allen
		 * auszugebenden Kontakten und eine Fußzeile hinzufügen
		 */
		sb.append(createHeadOfReport(r));
		sb.append(paragraphWithFilter2HTML(r.getFiltercriteria()));
		sb.append(convertContactReportsToHTML(r.getSubReports()));
		sb.append(paragraph2HTML(r.getFootline()));
		sb.append("</div>");
		this.reportText = sb.toString();
	}

}
