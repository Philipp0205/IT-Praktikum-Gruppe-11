package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;

/**
 * Ein simpler Paragraph, also Text. Paragraphen dienen der Darstellung von
 * Informationen wie Besitzern, Erstelldaten oder Filterkriteren in Reports.
 * 
 * @author Kurrle und Anders
 *
 */
public class Paragraph implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * Dieses Array aus Strings wird für den Fall verwendet, dass in dem Paragraphen
	 * die Filterkriterien gespeichert sind
	 */
	private String[] filtercriteria = new String[4];

	/**
	 * Dieser String wird im Normalfall mit den Informationen befüllt, die der
	 * Paragraph anzeigen soll
	 */
	private String content;

	public Paragraph() {
	}

	public Paragraph(String content) {
		super();
		this.content = content;
	}

	public Paragraph(String[] filtercriteria) {
		super();
		this.filtercriteria = filtercriteria;
	}

	public String[] getFiltercriteria() {
		return filtercriteria;
	}

	public void setFiltercriteria(String[] filtercriteria) {
		this.filtercriteria = filtercriteria;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
