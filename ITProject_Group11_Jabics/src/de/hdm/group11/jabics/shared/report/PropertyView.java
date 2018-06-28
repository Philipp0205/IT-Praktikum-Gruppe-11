package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.PValue;

/*
 * Die Klasse PropertyView implementiert eine generische Sicht auf ein PValue Objekt, das für die Anzeige einer
 * Eigenschaftsausprägung in einem Report geeignet ist. Ein PropertyView enthält einen String für den Namen 
 * der Eigenschaft und einen String für den Wert der Ausprägung.
 */
public class PropertyView implements Serializable {
	private static final long serialVersionUID = 1L;

	private String pname;
	private String pvalue;

	public PropertyView() {}

	public PropertyView(String p, String v) {
		this.pname = p;
		this.pvalue = v;
	}

	public PropertyView(PValue pv) throws IllegalArgumentException {
		this.pname = pv.getProperty().getLabel();
		System.err.println("KontsruktorPropView");
		switch (pv.getPointer()) {

		case 1:
			System.err.println("KontsruktorPropView1");
			Integer i = pv.getIntValue();
			this.pvalue = i.toString();
			break;
		case 2:
			System.err.println("KontsruktorPropView2");
			this.pvalue = pv.getStringValue();
			break;
		case 3:
			System.err.println("KontsruktorPropView3");
			Date dt = pv.getDateValue();
			this.pvalue = dt.toString();
			break;
		case 4:
			System.err.println("KontsruktorPropView4");
			Float f = pv.getFloatValue();
			this.pvalue = f.toString();
			break;
		default:
			System.out.println("PropertyValue could not be found by PropertyView");
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
