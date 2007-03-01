/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Resource validation model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidation extends AbstractBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int typeId;
	private int validationTypeId;
	private ResourceValidationType validationType;
	
	/**
	 * Returns the typeId.
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}
	
	/**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
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
	public ResourceValidationType getValidationType() {
		return validationType;
	}

	/**
	 * Set the validationType.
	 * @param validationType the validationType to set
	 */
	public void setValidationType(ResourceValidationType validationType) {
		this.validationType = validationType;
		if (validationType != null) {
			this.validationTypeId = validationType.getValidationTypeId();
		}
	}

	/**
	 * Validates the given resource.
	 * @param resource	The resource to be valided
	 * @param user		The current user
	 * @return			An error message if an error occurs or null if all is OK
	 */
	public String validate(Resource resource, User user) {
		return validationType.validate(resource, user);
	}
}
