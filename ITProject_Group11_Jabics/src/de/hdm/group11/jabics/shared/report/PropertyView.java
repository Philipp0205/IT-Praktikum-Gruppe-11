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

	public PropertyView() {
	}

	public PropertyView(String p, String v) {
		this.pname = p;
		this.pvalue = v;
	}

	/**
	 * Eine neue View auf ein PValue Objekt erstellen
	 * 
	 * @param pv,
	 *            PValue, für das die PropertyView erstellt werden soll
	 * @throws IllegalArgumentException
	 */
	public PropertyView(PValue pv) throws IllegalArgumentException {
		if (pv.getProperty() != null) {
			this.pname = pv.getProperty().getLabel();
		} else {
			this.pname = "Property";
		}
		try {
			switch (pv.getPointer()) {

			case 1:
				Integer i = pv.getIntValue();
				this.pvalue = i.toString();
				break;
			case 2:
				this.pvalue = pv.getStringValue();
				break;
			case 3:
				Date dt = pv.getDateValue();
				this.pvalue = dt.toString();
				break;
			case 4:
				Float f = pv.getFloatValue();
				this.pvalue = f.toString();
				break;
			default:
				System.out.println("PropertyValue could not be found by PropertyView");
			}
		} catch (Exception e) {
			this.pvalue = "Value";
		}
	}

	/**
	 * Den Namen der Property dieser PropertyView erhalten
	 * @return s, String mit Name der Eigenschaft
	 */
	public String getPname() {
		return pname;
	}

	/**
	 * Den Namen der Property dieser PropertyView setzen
	 * @param s, String mit Name der Eigenschaft
	 */
	public void setPname(String pname) {
		this.pname = pname;
	}

	/**
	 * Den Namen der Ausprägung dieser PropertyView erhalten
	 * @return s, String mit Name der Eigenschaftsausprägung
	 */
	public String getPvalue() {
		return pvalue;
	}

	/**
	 * Den Namen der Ausprägung dieser PropertyView setzen
	 * @param s, String mit Name der Eigenschaftsausprägung
	 */
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}

}
