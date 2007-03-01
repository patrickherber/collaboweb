/**
 * collaboweb
 * Feb 17, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import java.util.Map;

import ch.arpage.collaboweb.model.ResourceValidationType;

/**
 * ActionForm for the resource validation type model class
 * 
 * @see ResourceValidationType
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidationTypeForm extends AbstractForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private ResourceValidationType resourceValidationType;

	/**
	 * Creates a new ResourceValidationTypeForm
	 */
	public ResourceValidationTypeForm() {
		this(new ResourceValidationType());
	}

	/**
	 * Creates a new ResourceValidationTypeForm based on the given resource 
	 * validation type
	 * @param resourceValidationType the resource validation type
	 */
	public ResourceValidationTypeForm(ResourceValidationType resourceValidationType) {
		this.resourceValidationType = resourceValidationType;
	}
	
	/**
	 * Returns the resourceValidationType.
	 * @return the resourceValidationType
	 */
	public ResourceValidationType getResourceValidationType() {
		return resourceValidationType;
	}

	/**
	 * Returns the className.
	 * @return the className
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#getClassName()
	 */
	public String getClassName() {
		return resourceValidationType.getClassName();
	}

	/**
	 * Returns the communityId.
	 * @return the communityId
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#getCommunityId()
	 */
	public long getCommunityId() {
		return resourceValidationType.getCommunityId();
	}

	/**
	 * Returns the labels.
	 * @return the labels
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabels()
	 */
	public Map<String, String> getLabels() {
		return resourceValidationType.getLabels();
	}

	/**
	 * Returns the messages.
	 * @return the messages
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#getMessages()
	 */
	public Map<String, String> getMessages() {
		return resourceValidationType.getMessages();
	}

	/**
	 * Returns the validationTypeId.
	 * @return the validationTypeId
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#getValidationTypeId()
	 */
	public int getValidationTypeId() {
		return resourceValidationType.getValidationTypeId();
	}

	/**
	 * Set the className.
	 * @param className the className to set
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#setClassName(java.lang.String)
	 */
	public void setClassName(String className) {
		resourceValidationType.setClassName(className);
	}

	/**
	 * Set the communityId.
	 * @param communityId the communityId to set
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#setCommunityId(long)
	 */
	public void setCommunityId(long communityId) {
		resourceValidationType.setCommunityId(communityId);
	}

	/**
	 * Set the labels.
	 * @param labels the labels to set
	 * @see ch.arpage.collaboweb.model.LabelableBean#setLabels(java.util.Map)
	 */
	public void setLabels(Map<String, String> labels) {
		resourceValidationType.setLabels(labels);
	}

	/**
	 * Set the messages.
	 * @param messages the messages to set
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#setMessages(java.util.Map)
	 */
	public void setMessages(Map<String, String> messages) {
		resourceValidationType.setMessages(messages);
	}

	/**
	 * Set the validationTypeId.
	 * @param validationTypeId the validationTypeId to set
	 * @see ch.arpage.collaboweb.model.AbstractValidationType#setValidationTypeId(int)
	 */
	public void setValidationTypeId(int validationTypeId) {
		resourceValidationType.setValidationTypeId(validationTypeId);
	}
	
	
}
