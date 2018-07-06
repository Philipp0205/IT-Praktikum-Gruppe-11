package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;

/**
 * Diese Klasse <code>Paragraph</code> dienen der Darstellung von Informationen
 * wie Besitzern, Erstelldaten oder Filterkriteren in Reports in Textform.
 * 
 * @author Kurrle
 * @author Anders
 * @author Stahl
 *
 */
public class Paragraph implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Array aus <code>String</code>, die Filterkriterin einer Instanz dieser
	 * Klasse.
	 */
	private String[] filtercriteria = new String[4];

	/**
	 * Dieser String wird mit den Informationen befüllt, die der Paragraph anzeigen
	 * soll.
	 */
	private String content;

	/**
	 * Default Konstruktor.
	 */
	public Paragraph() {
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit <code>content</code> zu
	 * erstellen.
	 * 
	 * @param content
	 */
	public Paragraph(String content) {
		super();
		this.content = content;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit <code>filtercriteria</code> zu
	 * erstellen.
	 * 
	 * @param filtercriteria
	 */
	public Paragraph(String[] filtercriteria) {
		super();
		this.filtercriteria = filtercriteria;
	}

	/**
	 * Auslesen der <code>filtercriteria</code>.
	 * 
	 * @return <code>filtercriteria</code>
	 */
	public String[] getFiltercriteria() {
		return filtercriteria;
	}

	/**
	 * Setzen der <code>filtercriteria</code>.
	 * 
	 * @param <code>filtercriteria</code>
	 */
	public void setFiltercriteria(String[] filtercriteria) {
		this.filtercriteria = filtercriteria;
	}

	/**
	 * Auslesen des <code>content</code>.
	 * 
	 * @param content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Setzen des <code>content</code>.
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
}