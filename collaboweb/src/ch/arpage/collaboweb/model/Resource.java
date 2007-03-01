/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.model;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.common.Utils;

/**
 * Resource model class. Represents an object inside the repository.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Resource extends TimeValidityLimitedBean {

	private static final long serialVersionUID = 1L;
	
	private long resourceId;
	private long parentId;
	private long communityId;
	private int typeId;
	private String name;
	private long authorId;
	private String authorName;
	private Timestamp updateDate;
	private Timestamp createDate;
	private Timestamp visibleFrom;
	private Timestamp visibleTo;
	private boolean archived;
	private int status;
	private int visibility;
	//TODO Access Control not implemented
	//private int rightTypeId;
	
	
	private Map<Integer, ResourceAttribute> resourceAttributes = 
		new TreeMap<Integer, ResourceAttribute>();
	
	private ResourceType resourceType;
	
	private List<Relationship> relationships;
	
	private Set<TagStatistic> tags;
	
	private List ratings;
	
	private List categories;
	
	private transient String xml;
	
	/**
	 * Returns the resourceType.
	 * @return the resourceType
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}
	
	/**
	 * Set the resourceType.
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
		this.typeId = resourceType.getTypeId();
	}
	
	/**
	 * Returns the archived.
	 * @return the archived
	 */
	public boolean isArchived() {
		return archived;
	}
	/**
	 * Set the archived.
	 * @param archived the archived to set
	 */
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	/**
	 * Returns the authorId.
	 * @return the authorId
	 */
	public long getAuthorId() {
		return authorId;
	}
	/**
	 * Set the authorId.
	 * @param authorId the authorId to set
	 */
	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}
	/**
	 * Returns the communityId.
	 * @return the communityId
	 */
	public long getCommunityId() {
		return communityId;
	}
	/**
	 * Set the communityId.
	 * @param communityId the communityId to set
	 */
	public void setCommunityId(long communityId) {
		this.communityId = communityId;
	}
	/**
	 * Returns the createDate.
	 * @return the createDate
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}
	/**
	 * Set the createDate.
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the parentId.
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}
	/**
	 * Set the parentId.
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	/**
	 * Returns the resourceId.
	 * @return the resourceId
	 */
	public long getResourceId() {
		return resourceId;
	}
	/**
	 * Set the resourceId.
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	/**
	 * Returns the status.
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * Set the status.
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
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
	 * Returns the updateDate.
	 * @return the updateDate
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	/**
	 * Set the updateDate.
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * Returns the visibleFrom.
	 * @return the visibleFrom
	 */
	public Timestamp getVisibleFrom() {
		return visibleFrom;
	}
	/**
	 * Set the visibleFrom.
	 * @param visibleFrom the visibleFrom to set
	 */
	public void setVisibleFrom(Timestamp visibleFrom) {
		this.visibleFrom = visibleFrom;
	}
	/**
	 * Returns the visibleTo.
	 * @return the visibleTo
	 */
	public Timestamp getVisibleTo() {
		return visibleTo;
	}
	/**
	 * Set the visibleTo.
	 * @param visibleTo the visibleTo to set
	 */
	public void setVisibleTo(Timestamp visibleTo) {
		this.visibleTo = visibleTo;
	}
	
	/**
	 * Returns the resourceAttributes.
	 * @return the resourceAttributes
	 */
	public Map<Integer, ResourceAttribute> getResourceAttributes() {
		return resourceAttributes;
	}
	
	/**
	 * Set the resourceAttributes.
	 * @param attributes the resourceAttributes to set
	 */
	public void setResourceAttributes(Map<Integer, ResourceAttribute> attributes) {
		this.resourceAttributes = attributes;
	}
	
	/**
	 * Adds the given attribute value.
	 * @param attribute	The attribute value to be added
	 */
	public void addResourceAttribute(ResourceAttribute attribute) {
		Assert.notNull(attribute);
		this.resourceAttributes.put(attribute.getAttributeId(), 
				attribute);
	}
	
	/**
	 * Return the attribute value object for the attribute identified by the
	 * given ID
	 * @param attributeId	The ID of the attribute
	 * @return				The attribute value object for the attribute 
	 * 						identified by the given ID
	 */
	public ResourceAttribute getResourceAttribute(int attributeId) {
		return resourceAttributes.get(attributeId);
	}
	
	/**
	 * Return the attribute value object for the attribute identified by the
	 * given identifier
	 * @param identifier	The identifier of the attribute
	 * @return				The attribute value object for the attribute 
	 * 						identified by the given identifier
	 */
	public ResourceAttribute getResourceAttribute(String identifier) {
		Attribute attribute = resourceType.getAttribute(identifier);
		if (attribute != null) {
			return resourceAttributes.get(attribute.getAttributeId());
		}
		return null;
	}
	/**
	 * Returns the categories.
	 * @return the categories
	 */
	public List getCategories() {
		return categories;
	}
	/**
	 * Set the categories.
	 * @param categories the categories to set
	 */
	public void setCategories(List categories) {
		this.categories = categories;
	}
	/**
	 * Returns the ratings.
	 * @return the ratings
	 */
	public List getRatings() {
		return ratings;
	}
	/**
	 * Set the ratings.
	 * @param ratings the ratings to set
	 */
	public void setRatings(List ratings) {
		this.ratings = ratings;
	}
	/**
	 * Returns the relationships.
	 * @return the relationships
	 */
	public List<Relationship> getRelationships() {
		return relationships;
	}
	/**
	 * Set the relationships.
	 * @param relationships the relationships to set
	 */
	public void setRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}
	/**
	 * Returns the tags.
	 * @return the tags
	 */
	public Set<TagStatistic> getTags() {
		return tags;
	}
	/**
	 * Set the tags.
	 * @param tags the tags to set
	 */
	public void setTags(Set<TagStatistic> tags) {
		this.tags = tags;
	}
	
	/**
	 * Returns the visibility.
	 * @return the visibility
	 */
	public int getVisibility() {
		return visibility;
	}
	
	/**
	 * Set the visibility.
	 * @param visibility the visibility to set
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * Returns the rightTypeId.
	 * @return the rightTypeId
	 */
	public int getRightTypeId() {
		//TODO Access Control not implemented
		return Model.RIGHT_ADMIN;
	}
	
	/**
	 * Set the rightTypeId.
	 * @param rightTypeId the rightTypeId to set
	 */
	public void setRightTypeId(int rightTypeId) {
		//TODO Access Control not implemented
		//this.rightTypeId = rightTypeId;
	}
	
	/**
	 * Returns <code>true</code> if the user has at least the given rightType,
	 * <code>false</code> otherwise.
	 * @param rightType		The requested access right type 
	 * @return				<code>true</code> if the user has at least the 
	 * 						given rightType, <code>false</code> otherwise.
	 */
	public boolean isAllowed(int rightType) {
		return getRightTypeId() >= rightType;
	}
	
	/**
	 * Returns the authorName.
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}
	
	/**
	 * Set the authorName.
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	public void computeName() {
		StringBuffer name = new StringBuffer();
		for (Attribute attribute : resourceType.getAttributes()) {
			if (attribute.isNamePart()) {
				ResourceAttribute ra = 
					getResourceAttribute(attribute.getAttributeId());
				if (ra != null && ra.getValue() != null) {
					if (name.length() > 0) {
						name.append(" ");
					}
					name.append(ra.getValue());
				}
			}
		}
		this.name = (name.length() > 255) ? 
				name.substring(0, 255) : name.toString();
	}
	
	/**
	 * Returns the object structure as XML String
	 * @return the object structure as XML String
	 * @throws Exception
	 */
	public String getXml() throws Exception {
		if (xml == null) {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			StringBuffer sb = new StringBuffer();
			sb.append("<resource>");
			appendToXml("resourceId", resourceId, sb);
			appendToXml("archived", archived, sb);
			appendToXml("authorId", authorId, sb);
			appendToXml("authorName", authorName, sb);
			appendToXml("createDate", createDate, df, sb);
			appendToXml("name", name, sb);
			appendToXml("parentId", parentId, sb);
			appendToXml("status", status, sb);
			appendToXml("typeId", typeId, sb);
			appendToXml("updateDate", updateDate, df, sb);
			appendToXml("visibility", visibility, sb);
			appendToXml("visibleFrom", visibleFrom, df, sb);
			appendToXml("visibleTo", visibleTo, df, sb);
			appendToXml("typeId", typeId, sb);
			sb.append("<resourceType>");
			appendToXml("communityId", resourceType.getCommunityId(), sb);
			sb.append("</resourceType>");
			sb.append("<attributes>");
			List<Attribute> attributes = resourceType.getAttributes();
			if (attributes != null) {
				for (Attribute attribute : attributes) {
					sb.append("<attribute>");
					appendToXml("attributeId", attribute.getAttributeId(), sb);
					appendToXml("dataType", attribute.getDataType(), sb);
					appendToXml("formOrder", attribute.getFormOrder(), sb);
					appendToXml("choices", attribute.getChoices(), sb);
					appendToXml("defaultValue", attribute.getDefaultValue(), sb);
					appendToXml("identifier", attribute.getIdentifier(), sb);
					sb.append("<labels>");
					for (String key : attribute.getLabels().keySet()) {
						sb.append("<label>");
						appendToXml("key", key, sb);
						appendToXml("value", attribute.getLabel(key), sb);
						sb.append("</label>");
					}
					sb.append("</labels>");
					ResourceAttribute ra = getResourceAttribute(attribute.getAttributeId());
					if (ra != null) {
						String value = (String) ra.getValue();
						String formatter = attribute.getFormatter();
						if (StringUtils.hasText(value) && StringUtils.hasText(formatter)) {
							try {
							switch (attribute.getDataType()) {
							case Model.DATA_TYPE_DATE:
								Date date = Utils.parseDate(value);
								if (date != null) {
									value = new SimpleDateFormat(formatter).format(date);
								}
								break;
							case Model.DATA_TYPE_INTEGER:
								value = new DecimalFormat(formatter).format(Utils.parseLong(value));
								break;
							case Model.DATA_TYPE_DOUBLE:
								value = new DecimalFormat(formatter).format(Utils.parseDouble(value));
								break;
							}
							} catch (Exception e) {
								/* ignore */
							}
						}
						appendToXml("value", value, sb);
						appendToXml("contentType", ra.getContentType(), sb);
					}
					sb.append("</attribute>");
				}
			}
			sb.append("</attributes>");
			sb.append("</resource>");
			xml = sb.toString();
		}
		return xml;
    }
	
	private void appendToXml(String name, long value, StringBuffer xml) {
		xml.append('<').append(name).append('>').append(value).append("</").append(name).append('>');
	}
	
	private void appendToXml(String name, boolean value, StringBuffer xml) {
		xml.append('<').append(name).append('>').append(value).append("</").append(name).append('>');
	}
	
	private void appendToXml(String name, String value, StringBuffer xml) {
		xml.append('<').append(name).append('>');
		if (value != null) {
			xml.append(value.replaceAll("&nbsp;", "&#160;"));
		}
		xml.append("</").append(name).append('>');
	}
	
	private void appendToXml(String name, Date value, SimpleDateFormat df, StringBuffer xml) {
		xml.append('<').append(name).append('>');
		if (value != null) {
			xml.append(df.format(value));
		}
		xml.append("</").append(name).append('>');
	}
}
