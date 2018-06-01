package de.hdm.group11.jabics.shared.report;

import java.time.LocalDateTime;
import de.hdm.group11.jabics.shared.bo.PValue;


/*
 * Die Klasse PropertyView implementiert eine generische Sicht auf ein PValue Objekt, das für die Anzeige einer
 * Eigenschaftsausprägung in einem Report geeignet ist. Ein PropertyView enthält einen String für den Namen 
 * der Eigenschaft und einen String für den Wert der Ausprägung.
 */
public class PropertyView {

	
	private String pname; 
	private String pvalue;
	
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
			 case 3: LocalDateTime dt = pv.getDateValue();
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
