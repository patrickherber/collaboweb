/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model;

import java.text.MessageFormat;

/**
 * Validation model object
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Validation extends AbstractBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int attributeId;
	private int validationTypeId;
	private ValidationType validationType;
	private String parameter;
	
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
	 * Returns the validationTypeId.
	 * @return the validationTypeId
	 */
	public int getValidationTypeId() {
		return validationTypeId;
	}
	/**
	 * Set the validationTypeId.
	 * @param validationTypeId the validationTypeId to set
	 */
	public void setValidationTypeId(int validationTypeId) {
		this.validationTypeId = validationTypeId;
	}
	/**
	 * Returns the validationType.
	 * @return the validationType
	 */
	public ValidationType getValidationType() {
		return validationType;
	}
	/**
	 * Set the validationType.
	 * @param validationType the validationType to set
	 */
	public void setValidationType(ValidationType validationType) {
		this.validationType = validationType;
		if (validationType != null) {
			this.validationTypeId = validationType.getValidationTypeId();
		}
	}
	
	/**
	 * Returns the parameter.
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
	
	/**
	 * Set the parameter.
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	/**
	 * Validates the given attribute
	 * @param attribute		The attribute to be validated
	 * @param value			The attribue value
	 * @param language		The language of the user
	 * @return				An error message if an error occurs or null if all
	 * 						is OK
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public String validate(Attribute attribute, Object value, String language) throws InstantiationException, 
			IllegalAccessException, ClassNotFoundException {
		if (!validationType.getValidator().isValid(value, parameter)) {
			String message = validationType.getMessage(language);
			if (message == null) {
				message = validationType.getClassName();
			}
			return MessageFormat.format(message, 
					new Object[] {attribute.getLabel(language), parameter, value});
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) return true;
		if (!(other instanceof Validation)) return false;
		final Validation bean = (Validation) other;
		if (!(bean.getValidationTypeId() == getValidationTypeId())) return false;
		return (bean.getAttributeId() == getAttributeId());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result;
		result = getClass().getName().hashCode();
		result = 29 * result + getValidationTypeId();
		result = 29 * result + getAttributeId();
		return result;
	}
}
