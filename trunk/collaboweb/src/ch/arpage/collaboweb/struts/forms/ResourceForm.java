/**
 * collaboweb
 * 06.01.2007
 */
package ch.arpage.collaboweb.struts.forms;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceAttribute;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.TagStatistic;

/**
 * ActionForm for the resource model class
 * 
 * @see Resource
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceForm extends AbstractForm {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Resource resource;

	private Map attributeValues = new HashMap();
	
	/**
	 * Creates a new ResourceForm 
	 */
	public ResourceForm() {
		this.resource = new Resource();
	}
	
	/**
	 * Creates a new ResourceForm based on the given resource
	 * @param resource	The basis resource
	 */
	public ResourceForm(Resource resource) {
		this.resource = resource;
		initAttributeValues();
	}
	
	/**
	 * Returns the resource.
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}
	
	/**
	 * Returns the attributeValues.
	 * @return the attributeValues
	 */
	public Map getAttributeValues() {
		return attributeValues;
	}
	
	/**
	 * Set the attributeValues.
	 * @param attributeValues the attributeValues to set
	 */
	public void setAttributeValues(Map attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	/**
	 * Initializes the attribute values
	 */
	@SuppressWarnings("unchecked")
	private void initAttributeValues() {
		Map<Integer, ResourceAttribute> attributes = 
			resource.getResourceAttributes();
		if (attributes != null) {
			for (int key : attributes.keySet()) {
				attributeValues.put(Integer.toString(key), 
						attributes.get(key).getValue());
			}
		}
	}
	
	/**
	 * Updates the attribute values.
	 */
	public void updateAttributeValues() {
		Map<Integer, ResourceAttribute> attributes = 
			resource.getResourceAttributes();
		for (Object key : attributeValues.keySet()) {
			Object value = attributeValues.get(key);
			if (value != null) {
				ResourceAttribute attribute = new ResourceAttribute();
				int id = Integer.parseInt((String) key);
				attribute.setAttributeId(id);
				attribute.setValue(value);
				attributes.put(id, attribute);
			}
		}
	}

	/**
	 * Returns the authorId
	 * @return the authorId
	 * @see ch.arpage.collaboweb.model.Resource#getAuthorId()
	 */
	public long getAuthorId() {
		return resource.getAuthorId();
	}

	/**
	 * Returns the categories
	 * @return the categories
	 * @see ch.arpage.collaboweb.model.Resource#getCategories()
	 */
	public List getCategories() {
		return resource.getCategories();
	}

	/**
	 * Returns the communityId
	 * @return the communityId
	 * @see ch.arpage.collaboweb.model.Resource#getCommunityId()
	 */
	public long getCommunityId() {
		return resource.getCommunityId();
	}

	/**
	 * Returns the createDate
	 * @return the createDate
	 * @see ch.arpage.collaboweb.model.Resource#getCreateDate()
	 */
	public Timestamp getCreateDate() {
		return resource.getCreateDate();
	}

	/**
	 * Returns the name
	 * @return the name
	 * @see ch.arpage.collaboweb.model.Resource#getName()
	 */
	public String getName() {
		return resource.getName();
	}

	/**
	 * Returns the parentId
	 * @return the parentId
	 * @see ch.arpage.collaboweb.model.Resource#getParentId()
	 */
	public long getParentId() {
		return resource.getParentId();
	}

	/**
	 * Returns the ratings
	 * @return the ratings
	 * @see ch.arpage.collaboweb.model.Resource#getRatings()
	 */
	public List getRatings() {
		return resource.getRatings();
	}

	/**
	 * Returns the relationships
	 * @return the relationships
	 * @see ch.arpage.collaboweb.model.Resource#getRelationships()
	 */
	public List getRelationships() {
		return resource.getRelationships();
	}

	/**
	 * Returns the resourceAttributes
	 * @return the resourceAttributes
	 * @see ch.arpage.collaboweb.model.Resource#getResourceAttributes()
	 */
	public Map<Integer, ResourceAttribute> getResourceAttributes() {
		return resource.getResourceAttributes();
	}

	/**
	 * Returns the resourceId
	 * @return the resourceId
	 * @see ch.arpage.collaboweb.model.Resource#getResourceId()
	 */
	public long getResourceId() {
		return resource.getResourceId();
	}

	/**
	 * Returns the resourceType
	 * @return the resourceType
	 * @see ch.arpage.collaboweb.model.Resource#getResourceType()
	 */
	public ResourceType getResourceType() {
		return resource.getResourceType();
	}

	/**
	 * Returns the status
	 * @return the status
	 * @see ch.arpage.collaboweb.model.Resource#getStatus()
	 */
	public int getStatus() {
		return resource.getStatus();
	}

	/**
	 * Returns the tags
	 * @return the tags
	 * @see ch.arpage.collaboweb.model.Resource#getTags()
	 */
	public Set<TagStatistic> getTags() {
		return resource.getTags();
	}

	/**
	 * Returns the typeId
	 * @return the typeId
	 * @see ch.arpage.collaboweb.model.Resource#getTypeId()
	 */
	public int getTypeId() {
		return resource.getTypeId();
	}

	/**
	 * Returns the updateDate
	 * @return the updateDate
	 * @see ch.arpage.collaboweb.model.Resource#getUpdateDate()
	 */
	public Timestamp getUpdateDate() {
		return resource.getUpdateDate();
	}

	/**
	 * Returns the validFrom
	 * @return the validFrom
	 * @see ch.arpage.collaboweb.model.TimeValidityLimitedBean#getValidFrom()
	 */
	public Timestamp getValidFrom() {
		return resource.getValidFrom();
	}

	/**
	 * Returns the validTo
	 * @return the validTo
	 * @see ch.arpage.collaboweb.model.TimeValidityLimitedBean#getValidTo()
	 */
	public Timestamp getValidTo() {
		return resource.getValidTo();
	}

	/**
	 * Returns the visibleFrom
	 * @return the visibleFrom
	 * @see ch.arpage.collaboweb.model.Resource#getVisibleFrom()
	 */
	public Timestamp getVisibleFrom() {
		return resource.getVisibleFrom();
	}

	/**
	 * Returns the visibleTo
	 * @return the visibleTo
	 * @see ch.arpage.collaboweb.model.Resource#getVisibleTo()
	 */
	public Timestamp getVisibleTo() {
		return resource.getVisibleTo();
	}

	/**
	 * Returns the archived
	 * @return the archived
	 * @see ch.arpage.collaboweb.model.Resource#isArchived()
	 */
	public boolean isArchived() {
		return resource.isArchived();
	}

	/**
	 * Returns the valid
	 * @return the valid
	 * @see ch.arpage.collaboweb.model.TimeValidityLimitedBean#isValid()
	 */
	public boolean isValid() {
		return resource.isValid();
	}

	/**
	 * Set the archived.
	 * @param archived	The archived to set.
	 * @see ch.arpage.collaboweb.model.Resource#setArchived(boolean)
	 */
	public void setArchived(boolean archived) {
		resource.setArchived(archived);
	}

	/**
	 * Set the authorId.
	 * @param authorId	The authorId to set.
	 * @see ch.arpage.collaboweb.model.Resource#setAuthorId(long)
	 */
	public void setAuthorId(long authorId) {
		resource.setAuthorId(authorId);
	}

	/**
	 * Set the communityId.
	 * @param communityId	The communityId to set.
	 * @see ch.arpage.collaboweb.model.Resource#setCommunityId(long)
	 */
	public void setCommunityId(long communityId) {
		resource.setCommunityId(communityId);
	}

	/**
	 * Set the createDate.
	 * @param createDate	The createDate to set.
	 * @see ch.arpage.collaboweb.model.Resource#setCreateDate(java.sql.Timestamp)
	 */
	public void setCreateDate(Timestamp createDate) {
		resource.setCreateDate(createDate);
	}

	/**
	 * Set the name.
	 * @param name	The name to set.
	 * @see ch.arpage.collaboweb.model.Resource#setName(java.lang.String)
	 */
	public void setName(String name) {
		resource.setName(name);
	}

	/**
	 * Set the parentId.
	 * @param parentId	The parentId to set.
	 * @see ch.arpage.collaboweb.model.Resource#setParentId(long)
	 */
	public void setParentId(long parentId) {
		resource.setParentId(parentId);
	}

	/**
	 * Set the ratings.
	 * @param ratings	The ratings to set.
	 * @see ch.arpage.collaboweb.model.Resource#setRatings(java.util.List)
	 */
	public void setRatings(List ratings) {
		resource.setRatings(ratings);
	}

	/**
	 * Set the resourceId.
	 * @param resourceId	The resourceId to set.
	 * @see ch.arpage.collaboweb.model.Resource#setResourceId(long)
	 */
	public void setResourceId(long resourceId) {
		resource.setResourceId(resourceId);
	}

	/**
	 * Set the status.
	 * @param status	The status to set.
	 * @see ch.arpage.collaboweb.model.Resource#setStatus(int)
	 */
	public void setStatus(int status) {
		resource.setStatus(status);
	}

	/**
	 * Set the typeId.
	 * @param typeId	The typeId to set.
	 * @see ch.arpage.collaboweb.model.Resource#setTypeId(int)
	 */
	public void setTypeId(int typeId) {
		resource.setTypeId(typeId);
	}

	/**
	 * Set the updateDate.
	 * @param updateDate	The updateDate to set.
	 * @see ch.arpage.collaboweb.model.Resource#setUpdateDate(java.sql.Timestamp)
	 */
	public void setUpdateDate(Timestamp updateDate) {
		resource.setUpdateDate(updateDate);
	}

	/**
	 * Set the validFrom.
	 * @param validFrom	The validFrom to set.
	 * @see ch.arpage.collaboweb.model.TimeValidityLimitedBean#setValidFrom(java.sql.Timestamp)
	 */
	public void setValidFrom(Timestamp validFrom) {
		resource.setValidFrom(validFrom);
	}

	/**
	 * Set the validTo.
	 * @param validTo	The validTo to set.
	 * @see ch.arpage.collaboweb.model.TimeValidityLimitedBean#setValidTo(java.sql.Timestamp)
	 */
	public void setValidTo(Timestamp validTo) {
		resource.setValidTo(validTo);
	}

	/**
	 * Set the visibleFrom.
	 * @param visibleFrom	The visibleFrom to set.
	 * @see ch.arpage.collaboweb.model.Resource#setVisibleFrom(java.sql.Timestamp)
	 */
	public void setVisibleFrom(Timestamp visibleFrom) {
		resource.setVisibleFrom(visibleFrom);
	}

	/**
	 * Set the visibleTo.
	 * @param visibleTo	The visibleTo to set.
	 * @see ch.arpage.collaboweb.model.Resource#setVisibleTo(java.sql.Timestamp)
	 */
	public void setVisibleTo(Timestamp visibleTo) {
		resource.setVisibleTo(visibleTo);
	}

	/**
	 * Returns the authorName.
	 * @return the authorName.
	 * @see ch.arpage.collaboweb.model.Resource#getAuthorName()
	 */
	public String getAuthorName() {
		return resource.getAuthorName();
	}

	/**
	 * Returns the ightTypeId
	 * @return the rightTypeId
	 * @see ch.arpage.collaboweb.model.Resource#getRightTypeId()
	 */
	public int getRightTypeId() {
		return resource.getRightTypeId();
	}

	/**
	 * Returns the visibility
	 * @return the visibility
	 * @see ch.arpage.collaboweb.model.Resource#getVisibility()
	 */
	public int getVisibility() {
		return resource.getVisibility();
	}

	/**
	 * Set the visibility.
	 * @param visibility	The visibility to set.
	 * @see ch.arpage.collaboweb.model.Resource#setVisibility(int)
	 */
	public void setVisibility(int visibility) {
		resource.setVisibility(visibility);
	}
	
}
