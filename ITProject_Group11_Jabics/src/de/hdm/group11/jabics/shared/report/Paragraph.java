package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;

/**
 * Paragraphen dienen der Darstellung von Filterkriteren in Reports. 
 * 
 * @author Kurrle und Anders
 *
 */

public class Paragraph implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String[] filtercriteria = new String[4];
	private String content;
	
	public Paragraph() {}
	
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
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	} 

}
