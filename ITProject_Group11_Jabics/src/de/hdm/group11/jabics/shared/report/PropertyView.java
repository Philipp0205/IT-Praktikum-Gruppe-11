package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.PValue;


/*
 * Die Klasse PropertyView implementiert eine generische Sicht auf ein PValue Objekt, das f�r die Anzeige einer
 * Eigenschaftsauspr�gung in einem Report geeignet ist. Ein PropertyView enth�lt einen String f�r den Namen 
 * der Eigenschaft und einen String f�r den Wert der Auspr�gung.
 */
public class PropertyView implements Serializable{
	private static final long serialVersionUID = 1L;

	
	private String pname; 
	private String pvalue;
	
	public PropertyView() {}
	
	public PropertyView(String p, String v) {
		this.pname = p;
		this.pvalue = v;
	}

	
	public PropertyView (PValue pv) throws IllegalArgumentException {
		this.pname = pv.getProperty().getLabel();
		switch(pv.getPointer()) {
		
			 case 1: Integer i = pv.getIntValue();
			 		 this.pvalue = i.toString();
					 break;
			 case 2: this.pvalue = pv.getStringValue();
			 		 break;
			 case 3: Date dt = pv.getDateValue();
			         this.pvalue = dt.toString();
			         break;
			 case 4: Float f = pv.getFloatValue();
			 		 this.pvalue = f.toString();
			 		 break;
			default: System.out.println("PropertyValue could not be found by PropertyView");
		}
	}
	
	/**
	 * Getter und Setter 
	 */
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPvalue() {
		return pvalue;
	}
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	} 

}
