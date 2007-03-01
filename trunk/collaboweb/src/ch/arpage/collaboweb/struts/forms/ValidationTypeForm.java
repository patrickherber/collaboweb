/**
 * collaboweb
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import java.util.Map;

import ch.arpage.collaboweb.model.ValidationType;

/**
 * ActionForm for the validation type model class
 * 
 * @see ValidationType
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationTypeForm extends AbstractForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private ValidationType validationType;
	
	/**
	 * Creates a new ValidationTypeForm
	 */
	public ValidationTypeForm() {
		this(new ValidationType());
	}

	/**
	 * Creates a new ValidationTypeForm based on the given validationType
	 * @param validationType the validationType object
	 */
	public ValidationTypeForm(ValidationType validationType) {
		this.validationType = validationType;
	}
	
	/**
	 * Returns the validationType.
	 * @return the validationType
	 */
	public ValidationType getValidationType() {
		return validationType;
	}

	/**
	 * Returns the validationType.
	 * @return the validationType
	 * @see ch.arpage.collaboweb.model.ValidationType#getClassName()
	 */
	public String getClassName() {
		return validationType.getClassName();
	}

	/**
	 * Returns the validationType.
	 * @return the validationType
	 * @see ch.arpage.collaboweb.model.ValidationType#getCommunityId()
	 */
	public long getCommunityId() {
		return validationType.getCommunityId();
	}

	/**
	 * Returns the validationType.
	 * @return the validationType
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabels()
	 */
	public Map<String, String> getLabels() {
		return validationType.getLabels();
	}

	/**
	 * Returns the validationType.
	 * @return the validationType
	 * @see ch.arpage.collaboweb.model.ValidationType#getValidationTypeId()
	 */
	public int getValidationTypeId() {
		return validationType.getValidationTypeId();
	}

	/**
	 * Set the className.
	 * @param className	the className to set
	 * @see ch.arpage.collaboweb.model.ValidationType#setClassName(java.lang.String)
	 */
	public void setClassName(String className) {
		validationType.setClassName(className);
	}

	/**
	 * Set the communityId.
	 * @param communityId	the communityId to set
	 * @see ch.arpage.collaboweb.model.ValidationType#setCommunityId(long)
	 */
	public void setCommunityId(long communityId) {
		validationType.setCommunityId(communityId);
	}

	/**
	 * Set the labels.
	 * @param labels	the labels to set
	 * @see ch.arpage.collaboweb.model.LabelableBean#setLabels(java.util.Map)
	 */
	public void setLabels(Map<String, String> labels) {
		validationType.setLabels(labels);
	}

	/**
	 * Set the validationTypeId.
	 * @param validationTypeId	the validationTypeId to set
	 * @see ch.arpage.collaboweb.model.ValidationType#setValidationTypeId(int)
	 */
	public void setValidationTypeId(int validationTypeId) {
		validationType.setValidationTypeId(validationTypeId);
	}

	/**
	 * Returns the messages.
	 * @return the messages
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#getMessages()
	 */
	public Map<String, String> getMessages() {
		return validationType.getMessages();
	}

	/**
	 * Set the messages.
	 * @param messages	the messages to set
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#setMessages(java.util.Map)
	 */
	public void setMessages(Map<String, String> messages) {
		validationType.setMessages(messages);
	}

	
}
