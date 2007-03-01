/**
 * collaboweb
 * Feb 17, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import ch.arpage.collaboweb.model.ResourceValidation;
import ch.arpage.collaboweb.model.ResourceValidationType;

/**
 * ActionForm for the resource validation model class
 * 
 * @see ResourceValidation
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidationForm extends AbstractForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private ResourceValidation resourceValidation;
		
	/**
	 * Creates a new ResourceValidationForm
	 */
	public ResourceValidationForm() {
		this(new ResourceValidation());
	}

	/**
	 * Creates a new ResourceValidationForm based on the given resource 
	 * validation
	 * @param resourceValidation the resource validation
	 */
	public ResourceValidationForm(ResourceValidation resourceValidation) {
		this.resourceValidation = resourceValidation;
	}
	
	/**
	 * Returns the resourceValidation.
	 * @return the resourceValidation
	 */
	public ResourceValidation getResourceValidation() {
		return resourceValidation;
	}

	/**
	 * Returns the typeId.
	 * @return the typeId
	 * @see ch.arpage.collaboweb.model.ResourceValidation#getTypeId()
	 */
	public int getTypeId() {
		return resourceValidation.getTypeId();
	}

	/**
	 * Returns the validationType.
	 * @return the validationType
	 * @see ch.arpage.collaboweb.model.ResourceValidation#getValidationType()
	 */
	public ResourceValidationType getValidationType() {
		return resourceValidation.getValidationType();
	}

	/**
	 * Returns the validationTypeId.
	 * @return the validationTypeId
	 * @see ch.arpage.collaboweb.model.ResourceValidation#getValidationTypeId()
	 */
	public int getValidationTypeId() {
		return resourceValidation.getValidationTypeId();
	}

	/**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 * @see ch.arpage.collaboweb.model.ResourceValidation#setTypeId(int)
	 */
	public void setTypeId(int typeId) {
		resourceValidation.setTypeId(typeId);
	}

	/**
	 * Set the validationType.
	 * @param validationType the validationType to set
	 * @see ch.arpage.collaboweb.model.ResourceValidation#setValidationType(ch.arpage.collaboweb.model.ResourceValidationType)
	 */
	public void setValidationType(ResourceValidationType validationType) {
		resourceValidation.setValidationType(validationType);
	}

	/**
	 * Set the validationTypeId.
	 * @param validationTypeId the validationTypeId to set
	 * @see ch.arpage.collaboweb.model.ResourceValidation#setValidationTypeId(int)
	 */
	public void setValidationTypeId(int validationTypeId) {
		resourceValidation.setValidationTypeId(validationTypeId);
	}
}
