/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Aspect model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Aspect extends LabelableBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private int aspectId;
	
	private long communityId;
	
	private String className;

	/**
	 * Returns the aspectId.
	 * @return the aspectId
	 */
	public int getAspectId() {
		return aspectId;
	}

	/**
	 * Set the aspectId.
	 * @param aspectId the aspectId to set
	 */
	public void setAspectId(int aspectId) {
		this.aspectId = aspectId;
	}

	/**
	 * Returns the className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the className.
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
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
	
	

}
