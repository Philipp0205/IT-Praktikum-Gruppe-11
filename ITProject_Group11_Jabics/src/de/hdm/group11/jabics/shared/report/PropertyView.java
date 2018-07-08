package de.hdm.group11.jabics.shared.report;

import java.io.Serializable;
import java.util.Date;

import de.hdm.group11.jabics.shared.bo.PValue;

/**
 * Die Klasse <code>PropertyView</code> implementiert eine generische Sicht auf
 * ein <code>PValue</code> Objekt, das für die Anzeige einer
 * Eigenschaftsausprägung in einem <code>Report</code> geeignet ist. Ein
 * <code>PropertyView</code> enthält einen String für den Namen der Eigenschaft
 * und einen String für den Wert der Ausprägung.
 */
public class PropertyView implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Name einer Instanz dieser Klasse.
	 */
	private String pname;

	/**
	 * Wert einer Instanz dieser Klasse.
	 */
	private String pvalue;

	/**
	 * Default Konstruktor
	 */
	public PropertyView() {
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit <code>pname</code> und
	 * <code>pvalue</code> zu erstellen.
	 * 
	 * @param pname
	 * @param pvalue
	 */
	public PropertyView(String pname, String pvalue) {
		this.pname = pname;
		this.pvalue = pvalue;
	}

	/**
	 * Eine neue View auf ein <code>PValue</code> Objekt erstellen.
	 * 
	 * @param pv
	 *            <code>PValue</code>, für das die <code>PropertyView</code>
	 *            erstellt werden soll.
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
				this.pvalue = "Kein Wert";
			}
		} catch (Exception e) {
			this.pvalue = "Value";
		}
	}

	/**
	 * Auslesen des <code>pname</code>.
	 * 
	 * @return String
	 */
	public String getPname() {
		return pname;
	}

	/**
	 * Setzen des <code>pname</code>.
	 * 
	 * @param pname
	 */
	public void setPname(String pname) {
		this.pname = pname;
	}

	/**
	 * Auslesen des <code>pvalue</code>.
	 * 
	 * @return String
	 */
	public String getPvalue() {
		return pvalue;
	}

	/**
	 * Setzen des <code>pvalue</code>.
	 * 
	 * @param pvalue
	 */
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}

}