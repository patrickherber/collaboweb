/**
 * collaboweb
 * Jan 12, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Community model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Community extends AbstractBean {

	private static final long serialVersionUID = 1L;

	private long communityId;
	private String name;
	private String nickname;
	
	private long peopleRootId;
	private long resourceRootId;
	
	/**
	 * Set the communityId.
	 * @param communityId the communityId to set
	 */
	public void setCommunityId(long communityId) {
		this.communityId = communityId;
	}
	
	/**
	 * Returns the communityId.
	 * @return the communityId
	 */
	public long getCommunityId() {
		return communityId;
	}
	
	/**
	 * Set the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the nickname.
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * Returns the nickname.
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Returns the peopleRootId.
	 * @return the peopleRootId
	 */
	public long getPeopleRootId() {
		return peopleRootId;
	}

	/**
	 * Set the peopleRootId.
	 * @param peopleRootId the peopleRootId to set
	 */
	public void setPeopleRootId(long peopleRootId) {
		this.peopleRootId = peopleRootId;
	}

	/**
	 * Returns the resourceRootId.
	 * @return the resourceRootId
	 */
	public long getResourceRootId() {
		return resourceRootId;
	}

	/**
	 * Set the resourceRootId.
	 * @param resourceRootId the resourceRootId to set
	 */
	public void setResourceRootId(long resourceRootId) {
		this.resourceRootId = resourceRootId;
	}
	
	
}
