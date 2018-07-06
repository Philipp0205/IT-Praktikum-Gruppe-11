package de.hdm.group11.jabics.shared.bo;

import java.io.Serializable;

/**
 * <p>
 * Die Klasse <code>Property</code> implementiert Eigenschaften in Jabics und
 * definiert die abstrakte Version eines <code>PValue</code>, also wie diese
 * heißt und welcher Datentyp diese repräsentiert.
 * </p>
 * 
 * @author Anders
 * @author Stahl
 */
public class Property extends BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Bezeichner einer Instanz dieser Klasse.
	 */
	private String label;

	/**
	 * Type einer Instanz dieser Klasse.
	 */
	private Type type;

	/**
	 * Boolescher Wert, ob eine Instanz dieser Klasse eine Standardeigenschaft ist.
	 */
	private boolean isStandard = false;

	/**
	 * Konstruktor, welcher den Konstruktor seiner Superklasse aufruft.
	 */
	public Property() {
		super();
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Bezeichner und Type zu
	 * erzeugen.
	 * 
	 * @param label
	 * @param type
	 */
	public Property(String label, Type type) {
		super();
		this.label = label;
		this.type = type;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Bezeichner, Type und isStandard
	 * zu erzeugen.
	 * 
	 * @param label
	 * @param type
	 * @param isStandard
	 */
	public Property(String label, Type type, boolean isStandard) {
		super();
		this.label = label;
		this.type = type;
		this.isStandard = isStandard;
	}

	/**
	 * Konstruktor um eine Instanz dieser Klasse mit Bezeichner, Type, isStandard
	 * und ID zu erzeugen.
	 * 
	 * @param label
	 * @param type
	 * @param isStandard
	 * @param id
	 */
	public Property(String label, Type type, boolean isStandard, int id) {
		super();
		this.label = label;
		this.type = type;
		this.isStandard = isStandard;
		this.id = id;
	}

	/**
	 * Auslesen des Bezeichners.
	 * 
	 * @return label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Auslesen des Types.
	 * 
	 * @return type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Auslesen des Types als String und in Kleinbuchstaben.
	 * 
	 * @return type
	 */
	public String getTypeInString() {
		return type.toString().toLowerCase();
	}

	/**
	 * Auslesen des booleschen Wertes, ob eine Eigenschaft Standard ist.
	 * 
	 * @return isStandard
	 */
	public boolean isStandard() {
		return this.isStandard;
	}

	/**
	 * Setzen des Bezeichners.
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Setzen des booleschen Wertes, ob eine Eigenschaft Standard ist.
	 * 
	 * @param isStandard
	 */
	public void setStandard(boolean isStandard) {
		this.isStandard = isStandard;
	}

	/**
	 * Setzen des Types in Großbuchstaben aus einem String.
	 * 
	 * @param type
	 *            Der type in einem String
	 */
	public void setType(String type) {
		this.type = Type.valueOf(type.toUpperCase());
	}

	/**
	 * Setzen des Types.
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		this.type = type;
	}
}