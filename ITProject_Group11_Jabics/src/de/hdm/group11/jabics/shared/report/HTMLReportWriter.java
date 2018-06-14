package de.hdm.group11.jabics.shared.report;

import java.util.ArrayList;


/**
 * Implementiert die Methoden aus ReportWriter, um Reports in das HTML-Format zu
 * übersetzen.
 * 
 * @author Anders
 *
 */
public class HTMLReportWriter extends ReportWriter {

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
	 * Konvertieren eines <code>Paragraph</code> in HTML.
	 * 
	 * @param Paragraph
	 * @return String in HTML-Format
	 */
	public String paragraph2HTML(Paragraph p) {

		return "<p>" + p.getContent() + "</p>";
	}

	public String paragraphWithFilter2HTML(Paragraph p) {
		StringBuffer filt = new StringBuffer();
		filt.append("<p>Es wurde nach ");
		for (String s : p.getFiltercriteria()) {
			if (s != null) {
				filt.append(s + ", ");
			}
		}
		filt.append(" gefiltert. </p>");
		return filt.toString();
	}

	public String createHeadOfReport(CompositeReport r) {

		if (r.getCreator() != null && r.getHeadline() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div id=\"report\">");
			sb.append("<h3> Report für " + r.getCreator().getUsername() + "</h3");
			return sb.toString();
		} else
			return "<div id=\"report\" style=\"margin-bottom: 16px\"> <h3>Report ohne Name</h3>";
	}

	public String convertContactReportsToHTML(ArrayList<ContactReport> cons) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"reportTable\"");
		sb.append("<table style=\"width:700px\">");
		// Die Spaltennamen definieren
		sb.append("<tr>");
		for (int i = 0; i < 10; i++) {
			sb.append("<td> <b>Spalte" + i + " </b> </td>");
		}
		sb.append("</tr>");
		// die Zeilen pro Kontakt füllen
		for (ContactReport c : cons) {
			sb.append("<tr>");
			if (c.getContactInfo() != null) {
				sb.append("<td> <b>" + c.getContactInfo() + "</b> </td>");
			} else sb.append("<td> <b>kein Anzeigename</b> </td>");
			for (PropertyView pv : c.getContent()) {
				int i = 0;
				if (pv.getPname() != null && pv.getPvalue() != null && i < 10) {
					sb.append("<td> <p>" + pv.getPname() + "\n" + pv.getPvalue() + " </p> </td>");
					i++;
				} else {
					if (i < 10) {
						sb.append("<td> <p>leer</p> </td>");
						i++;
					}
				}
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();

	}

	public void process(AllContactsInSystemReport r) {
		StringBuffer sb = new StringBuffer();
		/**
		 * Dem Ergebnis einen Kopf, Text unterhalb des Kopfes, Tabelle mit allen auszugebenden Kontakten
		 * und eine Fußzeile hinzufügen
		 */
		sb.append(createHeadOfReport(r));
		sb.append(paragraph2HTML(r.getHeadline()));
		/**
		 * Da ein AllContactsInSystem Report viele UserReports speichert, diese durchlaufen und für jeden 
		 * die Tabelle mit allen Kontakten ausgeben
		 */
		for(AllContactsOfUserReport acur : r.getSubReports()) {
			sb.append("<p style=\"margin-bottom: 8px\">Alle Kontakte von " + acur.getCreator().getUsername() + "</p>");
			sb.append(convertContactReportsToHTML(acur.getSubReports()));
			sb.append("<p style=\"margin-bottom: 16px\">Ende des Reports von " + acur.getCreator().getUsername() + "</p>");
		}
		sb.append(paragraph2HTML(r.getFootline()));
		sb.append("</div>");
		this.reportText = sb.toString();
	}

	public void process(AllContactsOfUserReport r) {
		StringBuffer sb = new StringBuffer();
		/**
		 * Dem Ergebnis einen Kopf, Text unterhalb des Kopfes, eine Tabelle mit allen auszugebenden Kontakten
		 * und eine Fußzeile hinzufügen
		 */
		sb.append(createHeadOfReport(r));
		sb.append(paragraph2HTML(r.getHeadline()));
		sb.append(convertContactReportsToHTML(r.getSubReports()));
		sb.append(paragraph2HTML(r.getFootline()));
		sb.append("</div>");
		this.reportText = sb.toString();
	}

	public void process(FilteredContactsOfUserReport r) {
		StringBuffer sb = new StringBuffer();
		/**
		 * Dem Ergebnis einen Kopf, Text unterhalb des Kopfes, eine Tabelle mit allen auszugebenden Kontakten
		 * und eine Fußzeile hinzufügen
		 */
		sb.append(createHeadOfReport(r));
		sb.append(paragraphWithFilter2HTML(r.getFiltercriteria()));
		sb.append(convertContactReportsToHTML(r.getSubReports()));
		sb.append(paragraph2HTML(r.getFootline()));
		sb.append("</div>");
		this.reportText = sb.toString();
	}

}