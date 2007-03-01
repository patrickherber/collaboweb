/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Relationship model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Relationship extends AbstractBean {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean outgoing;
	
	private RelationshipType relationshipType;
	
	private Resource relatedResource;
	
	/**
	 * Returns the outgoing.
	 * @return the outgoing
	 */
	public boolean isOutgoing() {
		return outgoing;
	}
	
	/**
	 * Set the outgoing.
	 * @param outgoing the outgoing to set
	 */
	public void setOutgoing(boolean outgoing) {
		this.outgoing = outgoing;
	}
	
	/**
	 * Set the relatedResource.
	 * @param relatedResource the relatedResource to set
	 */
	public void setRelatedResource(Resource relatedResource) {
		this.relatedResource = relatedResource;
	}
	
	/**
	 * Set the relationshipType.
	 * @param relationshipType the relationshipType to set
	 */
	public void setRelationshipType(RelationshipType relationshipType) {
		this.relationshipType = relationshipType;
	}
	
	/**
	 * Returns the relatedResource.
	 * @return the relatedResource
	 */
	public Resource getRelatedResource() {
		return relatedResource;
	}
	
	/**
	 * Returns the relationshipType.
	 * @return the relationshipType
	 */
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

}
