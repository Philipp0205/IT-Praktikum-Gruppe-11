package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;


/**
 * Implementiert die Methoden aus ReportWriter, um Reports in das HTML-Format zu
 * übersetzen.
 * 
 * @author Anders
 *
 */
public class HTMLReportWriter extends ReportWriter implements Serializable{
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
	
	public String getReportText() {
		return this.reportText;
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
		GWT.log("HeadOfReport erstellen");

		if (r.getCreator() != null && r.getHeadline() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div id=\"report\">");
			sb.append("<h3> Report für " + r.getCreator().getUsername() + "</h3>");
			sb.append("<h5> Erstellt am " + r.getCreationDate() + "</h5>");
			return sb.toString();
		} else
			return "<div id=\"report\" style=\"margin-bottom: 16px\"> <h3>Report ohne Name</h3>";
	}

	public String convertContactReportsToHTML(ArrayList<ContactReport> cons) {
		GWT.log("Tabelle erstellen");
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"reportTable\">");
		sb.append("<table style=\"width:700px; border: 1px solid black;\">");
		// Die Spaltennamen definieren
		sb.append("<tr>");
		GWT.log("Tabelle erstellen");
		for (int i = 0; i < 10; i++) {
			sb.append("<th> <b>Spalte" + i + " </b> </th>");
		}
		GWT.log("Tabelle erstellen");
		sb.append("</tr>");
		// die Zeilen pro Kontakt füllen
		for (ContactReport c : cons) {
			GWT.log("HTML Writer: neuer Kontakt Report für " + c.getContactInfo());
			if (c.getContactInfo() != null) {
				sb.append(" <tr> <td> <b>" + c.getContactInfo() + "</b> </td>");
			} else {
				GWT.log("Keinanzeigename");
				sb.append("<tr><td> <b>kein Anzeigename</b> </td> </tr>");
			}
			sb.append("<td> <p>Erstellt am " + c.getCreationDate() + "</p> </td>");
			sb.append("<td> <p>Besitzer: " + c.getUserInfo() + "</p> </td>");
			sb.append("</tr><tr>");
			for (PropertyView pv : c.getContent()) {
				int i = 0;
				if (pv.getPname() != null && pv.getPvalue() != null && i < 10) {
					sb.append("<td> <b>" + pv.getPname() + "</b> \n <p>" + pv.getPvalue() + " </p> </td>");
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
		GWT.log("Tabelle erstellenfertig" + sb.toString());
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
		GWT.log("Report zu HTML machen");
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
		GWT.log("Report zu HTML machen fertig");
	}

}
