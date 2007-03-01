/**
 * collaboweb
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import ch.arpage.collaboweb.model.Validation;
import ch.arpage.collaboweb.model.ValidationType;

/**
 * ActionForm for the validation model class
 * 
 * @see Validation
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationForm extends AbstractForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Validation validation;
	
	/**
	 * Creates a new ValidationForm
	 */
	public ValidationForm() {
		this(new Validation());
	}
	
	/**
	 * Creates a new ValidationForm based of the given validation
	 * @param validation	The validation object
	 */
	public ValidationForm(Validation validation) {
		this.validation = validation;
	}
	
	/**
	 * Returns the validation.
	 * @return the validation
	 */
	public Validation getValidation() {
		return validation;
	}
	
	/**
	 * Returns the validationTypeId.
	 * @return the validationTypeId
	 */
	public int getValidationTypeId() {
		return validation.getValidationTypeId();
	}
	
	/**
	 * Set the validationTypeId.
	 * @param validationTypeId the validationTypeId to set
	 */
	public void setValidationTypeId(int validationTypeId) {
		validation.setValidationTypeId(validationTypeId);
	}

	/**
	 * Returns the attributeId.
	 * @return the attributeId
	 * @see ch.arpage.collaboweb.model.Validation#getAttributeId()
	 */
	public int getAttributeId() {
		return validation.getAttributeId();
	}

	/**
	 * Returns the parameter.
	 * @return the parameter
	 * @see ch.arpage.collaboweb.model.Validation#getParameter()
	 */
	public String getParameter() {
		return validation.getParameter();
	}

	/**
	 * Returns the validationType.
	 * @return the validationType
	 * @see ch.arpage.collaboweb.model.Validation#getValidationType()
	 */
	public ValidationType getValidationType() {
		return validation.getValidationType();
	}

	/**
	 * Set the attributeId.
	 * @param attributeId the attributeId to set
	 * @see ch.arpage.collaboweb.model.Validation#setAttributeId(int)
	 */
	public void setAttributeId(int attributeId) {
		validation.setAttributeId(attributeId);
	}

	/**
	 * Set the parameter.
	 * @param parameter the parameter to set
	 * @see ch.arpage.collaboweb.model.Validation#setParameter(java.lang.String)
	 */
	public void setParameter(String parameter) {
		validation.setParameter(parameter);
	}

	/**
	 * Set the validationType.
	 * @param validationType the validationType to set
	 * @see ch.arpage.collaboweb.model.Validation#setValidationType(ch.arpage.collaboweb.model.ValidationType)
	 */
	public void setValidationType(ValidationType validationType) {
		validation.setValidationType(validationType);
	}
	
	

}
