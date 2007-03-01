/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Relationship type model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class RelationshipType extends LabelableBean {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int relationshipTypeId;
	
	private long communityId;

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
	 * Returns the relationshipTypeId.
	 * @return the relationshipTypeId
	 */
	public int getRelationshipTypeId() {
		return relationshipTypeId;
	}

	/**
	 * Set the relationshipTypeId.
	 * @param relationshipTypeId the relationshipTypeId to set
	 */
	public void setRelationshipTypeId(int relationshipTypeId) {
		this.relationshipTypeId = relationshipTypeId;
	}
}
