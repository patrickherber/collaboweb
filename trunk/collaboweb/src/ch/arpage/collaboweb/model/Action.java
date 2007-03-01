/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Action model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Action extends LabelableBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private int actionId;
	
	private long communityId;
	
	private String className;
	
	private String parameter;
	
	/**
	 * Returns the actionId.
	 * @return the actionId
	 */
	public int getActionId() {
		return actionId;
	}

	/**
	 * Set the actionId.
	 * @param actionId the actionId to set
	 */
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	/**
	 * Set the className.
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Returns the className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	
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
	 * Returns the parameter.
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
	
	/**
	 * Set the parameter.
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	/*
	public boolean equals(Object other) {
		if (this == other) return true;
		if ( !(other instanceof Action) ) return false;
		final Action action = (Action) other;
		return action.getActionId() == getActionId();
	}
	
	public int hashCode() {
		int result;
		result = getClass().getName().hashCode();
		result = 29 * result + getActionId();
		return result;
	}
	*/
}
