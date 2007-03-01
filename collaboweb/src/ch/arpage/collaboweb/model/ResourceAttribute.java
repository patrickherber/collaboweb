/**
 * collaboweb
 * Jan 14, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * The Resource Attribute model class that represents the value of an attribute
 * for a given resource.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceAttribute extends TimeValidityLimitedBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private int attributeId;
	
	private Object value;
	
	private String contentType;

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
	 * Returns the contentType.
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Set the contentType.
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the value.
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

}
