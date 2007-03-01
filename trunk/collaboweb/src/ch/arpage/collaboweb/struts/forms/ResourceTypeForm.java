/**
 * collaboweb
 * Jan 27, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ch.arpage.collaboweb.model.Aspect;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.Category;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.View;

/**
 * ActionForm for the resource type model class
 * 
 * @see ResourceType
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeForm extends AbstractForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private ResourceType resourceType;
	
	/**
	 * Creates a new ResourceTypeForm
	 */
	public ResourceTypeForm() {
		this(new ResourceType());
	}
	
	/**
	 * Creates a new ResourceTypeForm based of the given resourceType
	 * @param resourceType the ResourceType
	 */
	public ResourceTypeForm(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * Returns the resourceType.
	 * @return the resourceType
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}
	
	/**
	 * Returns the aspects.
	 * @return the aspects
	 * @see ch.arpage.collaboweb.model.ResourceType#getAspects()
	 */
	public List<Aspect> getAspects() {
		return resourceType.getAspects();
	}

	/**
	 * Returns the attributes.
	 * @return the attributes
	 * @see ch.arpage.collaboweb.model.ResourceType#getAttributes()
	 */
	public List<Attribute> getAttributes() {
		return resourceType.getAttributes();
	}

	/**
	 * Returns the categories.
	 * @return the categories
	 * @see ch.arpage.collaboweb.model.ResourceType#getCategories()
	 */
	public List<Category> getCategories() {
		return resourceType.getCategories();
	}

	/**
	 * Returns the communityId.
	 * @return the communityId
	 * @see ch.arpage.collaboweb.model.ResourceType#getCommunityId()
	 */
	public long getCommunityId() {
		return resourceType.getCommunityId();
	}

	/**
	 * Returns the familyId.
	 * @return the familyId
	 * @see ch.arpage.collaboweb.model.ResourceType#getFamilyId()
	 */
	public int getFamilyId() {
		return resourceType.getFamilyId();
	}

	/**
	 * Returns the label for the given language.
	 * @param language the language
	 * @return the label for the given language.
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabel(java.lang.String)
	 */
	public String getLabel(String language) {
		return resourceType.getLabel(language);
	}

	/**
	 * Returns the labels.
	 * @return the labels
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabels()
	 */
	public Map<String, String> getLabels() {
		return resourceType.getLabels();
	}

	/**
	 * Returns the supportedChildrenIds.
	 * @return the supportedChildrenIds
	 * @see ch.arpage.collaboweb.model.ResourceType#getSupportedChildrenIds()
	 */
	public Integer[] getSupportedChildrenIds() {
		List<Integer> list = resourceType.getSupportedChildrenIds();
		return (list != null) ? list.toArray(new Integer[1]) : null; 
	}
	
	/**
	 * Set the supportedChildrenIds.
	 * @param supportedChildrenIds the supportedChildrenIds to set
	 */
	public void setSupportedChildrenIds(Integer[] supportedChildrenIds) {
		if (supportedChildrenIds != null) {
			this.resourceType.setSupportedChildrenIds(new ArrayList<Integer>(
					Arrays.asList(supportedChildrenIds)));
		}
	}

	/**
	 * Returns the typeId.
	 * @return the typeId
	 * @see ch.arpage.collaboweb.model.ResourceType#getTypeId()
	 */
	public int getTypeId() {
		return resourceType.getTypeId();
	}

	/**
	 * Returns the views.
	 * @return the views
	 * @see ch.arpage.collaboweb.model.ResourceType#getViews()
	 */
	public List<View> getViews() {
		return resourceType.getViews();
	}

	/**
	 * Returns the timeView.
	 * @return the timeView
	 * @see ch.arpage.collaboweb.model.ResourceType#isTimeView()
	 */
	public boolean isTimeView() {
		return resourceType.isTimeView();
	}

	/**
	 * Set the communityId.
	 * @param communityId the communityId to set
	 * @see ch.arpage.collaboweb.model.ResourceType#setCommunityId(long)
	 */
	public void setCommunityId(long communityId) {
		resourceType.setCommunityId(communityId);
	}

	/**
	 * Set the familyId.
	 * @param familyId the familyId to set
	 * @see ch.arpage.collaboweb.model.ResourceType#setFamilyId(int)
	 */
	public void setFamilyId(int familyId) {
		resourceType.setFamilyId(familyId);
	}

	/**
	 * Set the timeView.
	 * @param timeView the timeView to set
	 * @see ch.arpage.collaboweb.model.ResourceType#setTimeView(boolean)
	 */
	public void setTimeView(boolean timeView) {
		resourceType.setTimeView(timeView);
	}

	/**
	 * Set the labels.
	 * @param labels the labels to set
	 */
	public void setLabels(Map<String, String> labels) {
		this.resourceType.setLabels(labels);
	}
	/**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 * @see ch.arpage.collaboweb.model.ResourceType#setTypeId(int)
	 */
	public void setTypeId(int typeId) {
		resourceType.setTypeId(typeId);
	}
	
	
	
}
