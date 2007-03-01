/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * View type model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewType extends LabelableBean {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int viewTypeId;
	
	private long communityId;
	
	private String contentType;
	
	/**
	 * Returns the viewTypeId.
	 * @return the viewTypeId
	 */
	public int getViewTypeId() {
		return viewTypeId;
	}

	/**
	 * Set the viewTypeId.
	 * @param viewTypeId the viewTypeId to set
	 */
	public void setViewTypeId(int viewTypeId) {
		this.viewTypeId = viewTypeId;
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
	
}
