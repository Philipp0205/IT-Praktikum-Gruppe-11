package de.hdm.group11.jabics.shared.report;

/**
 * TODO
 * @author 
 *
 */

public class Paragraph {
	
	private String[] filtercriteria = new String[4];
	private String content;
	
	public Paragraph(String content) {
		super();
		this.content = content;
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
