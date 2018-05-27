package de.hdm.group11.jabics.shared.report;

/**
 * Paragraphen dienen der Darstellung von Filterkriteren in Reports. 
 * 
 * @author Kurrle und Anders
 *
 */

public class Paragraph {
	
	private String[] filtercriteria = new String[4];
	private String content;
	
	public Paragraph(String content) {
		super();
		this.content = content;
	}
	
	public String getFilterAsString() {
		StringBuffer result = new StringBuffer();
		result.append("Es wurde nach ");
		for (String s : filtercriteria) {
			result.append(s + ", ");
		}
		result.append(" gefiltert.");
		return result.toString();
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
