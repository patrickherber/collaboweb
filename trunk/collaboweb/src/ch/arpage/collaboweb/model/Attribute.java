/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Attribute model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Attribute extends LabelableBean implements Comparable<Attribute> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private int attributeId;
		
	private int typeId;
	
	private String identifier;
		
	private int dataType;
	
	private String choices;
	
	private String defaultValue;
	
	private boolean calculated;
		
	private int formOrder;
		
	private int editor;
		
	private String formatter;
		
	private boolean loadInList;

	private int searchFieldType;
	
	private boolean namePart;
	
	private List<Validation> validations;

	public Attribute() {
	}

	public Attribute(int typeId) {
		this.typeId = typeId;
	}
	
	/**
	 * Returns the attributeId.
	 * @return the attributeId
	 */
	public int getAttributeId() {
		return attributeId;
	}

	/**
	 * Set the attributeId.
	 * @param attributeId the attributeId to set
	 */
	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * Returns the choices.
	 * @return the choices
	 */
	public String getChoices() {
		return choices;
	}

	/**
	 * Set the choices.
	 * @param choices the choices to set
	 */
	public void setChoices(String choices) {
		this.choices = choices;
	}

	/**
	 * Returns the dataType.
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * Set the dataType.
	 * @param dataType the dataType to set
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * Returns the defaultValue.
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Set the defaultValue.
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Returns the searchFieldType.
	 * @return the searchFieldType
	 */
	public int getSearchFieldType() {
		return searchFieldType;
	}

	/**
	 * Set the searchFieldType.
	 * @param searchFieldType the searchFieldType to set
	 */
	public void setSearchFieldType(int searchFieldType) {
		this.searchFieldType = searchFieldType;
	}
	
	/**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	/**
	 * Returns the typeId.
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}
	
	/**
	 * Returns the calculated.
	 * @return the calculated
	 */
	public boolean isCalculated() {
		return calculated;
	}
	
	/**
	 * Set the calculated.
	 * @param calculated the calculated to set
	 */
	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}
	
	/**
	 * Returns the validations.
	 * @return the validations
	 */
	public List<Validation> getValidations() {
		return validations;
	}
	
	/**
	 * Set the validations.
	 * @param validations the validations to set
	 */
	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}
	
	public void removeValidation(int validationTypeId) {
		if (validations != null) {
			for (Validation v : validations) {
				if (v.getValidationTypeId() == validationTypeId) {
					validations.remove(v);
					break;
				}
			}
		}
	}
	
	public void addValidation(Validation validation) {
		if (validations == null) {
			validations = new LinkedList<Validation>();
		}
		boolean found = false;
		for (int i = 0; i < validations.size(); ++i) {
			if (validations.get(i).getValidationTypeId() 
					== validation.getValidationTypeId()) {
				validations.remove(i);
				validations.add(i, validation);
				found = true;
				break;
			}
		}
		if (!found) {
			validations.add(validation);
		}
	}
	
	/**
	 * Returns the editor.
	 * @return the editor
	 */
	public int getEditor() {
		return editor;
	}
	
	/**
	 * Set the editor.
	 * @param editor the editor to set
	 */
	public void setEditor(int editor) {
		this.editor = editor;
	}
	
	/**
	 * Returns the formatter.
	 * @return the formatter
	 */
	public String getFormatter() {
		return formatter;
	}
	
	/**
	 * Set the formatter.
	 * @param formatter the formatter to set
	 */
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	
	/**
	 * Set the formOrder.
	 * @param formOrder the formOrder to set
	 */
	public void setFormOrder(int formOrder) {
		this.formOrder = formOrder;
	}
	
	/**
	 * Returns the formOrder.
	 * @return the formOrder
	 */
	public int getFormOrder() {
		return formOrder;
	}
	
	/**
	 * Returns the loadInList.
	 * @return the loadInList
	 */
	public boolean isLoadInList() {
		return loadInList;
	}
	
	/**
	 * Set the loadInList.
	 * @param loadInList the loadInList to set
	 */
	public void setLoadInList(boolean loadInList) {
		this.loadInList = loadInList;
	}

	/**
	 * Returns the identifier.
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set the identifier.
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * Returns the namePart.
	 * @return the namePart
	 */
	public boolean isNamePart() {
		return namePart;
	}
	
	/**
	 * Set the namePart.
	 * @param namePart the namePart to set
	 */
	public void setNamePart(boolean namePart) {
		this.namePart = namePart;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Attribute o) {
		return formOrder - o.formOrder;
	}
}
