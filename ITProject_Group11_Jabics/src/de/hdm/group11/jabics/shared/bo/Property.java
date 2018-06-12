package de.hdm.group11.jabics.shared.bo;


/**
 * Diese Klasse definiert die abstrakte Version einer Eigenschaft, also wie diese hei�t und welcher Datentyp diese repr�sentiert.
 * Zusätzlich ist hier gespeichert, ob eine Eigenschaft zu den Standardeigenschaften von Kontakten in Jabics gehört.
 * Eine <code>Property</code> kann nur erstellt werden, wenn mindestens ein Name und ein Datentyp (von Enum Type) bekannt ist.
 * @author Anders
 */
public class Property extends BusinessObject {
	
	private String label;
	private Type type;
	private boolean isStandard = false;
	
	public Property(String label, Type type) {
		super();
		this.label = label;
		this.type = type;
	}
	
	//Leerer Konstruktor
	public Property() { 
		super();
	}
	
	
	/**
	 * Getter und Setter.
	 */
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public void setType(String type) {
		this.type = Type.valueOf(type.toUpperCase());
	}
	public boolean isStandard() {
		return isStandard;
	}
	public void setStandard(boolean isStandard) {
		this.isStandard = isStandard;
	}
}