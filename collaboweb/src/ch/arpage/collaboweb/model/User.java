/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.model;

import java.util.Locale;

/**
 * User model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class User extends AbstractBean {

	private static final long serialVersionUID = 1L;
	
	private long resourceId;
	private String email;
	private String lastName;
	private String firstName;
	private String language;
	private Community community;
	private String lastLogin;
	
	/**
	 * Returns the email.
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the firstName.
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the firstName.
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the language.
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * Returs the locale of the user
	 * @return the locale of the user
	 */
	public Locale getLocale() {
		if ("fr".equalsIgnoreCase(language)) {
			return Locale.FRENCH;
		} else if ("it".equalsIgnoreCase(language)) {
			return Locale.ITALIAN;
		} else if ("de".equalsIgnoreCase(language)) {
			return Locale.GERMAN;
		} else {
			return Locale.ENGLISH;
		}
	}

	/**
	 * Set the language.
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Returns the lastName.
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Set the email.
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Set the lastName.
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Returns the full name of the user
	 * @return	The full name of the user
	 */
	public String getFullName() {
		StringBuffer sb = new StringBuffer();
		if (firstName != null) {
			sb.append(firstName);
		}
		if (lastName != null) {
			if (firstName != null) {
				sb.append(' ');
			}
			sb.append(lastName);
		}
		return sb.toString();
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
	 * Returns the community.
	 * @return the community
	 */
	public Community getCommunity() {
		return community;
	}
	
	/**
	 * Set the community.
	 * @param community the community to set
	 */
	public void setCommunity(Community community) {
		this.community = community;
	}
	
	/**
	 * Returns the lastLogin.
	 * @return the lastLogin
	 */
	public String getLastLogin() {
		return lastLogin;
	}
	
	/**
	 * Set the lastLogin.
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * Returns the communityId
	 * @return	the communityId
	 * @see ch.arpage.collaboweb.model.Community#getCommunityId()
	 */
	public long getCommunityId() {
		return (community != null) ? community.getCommunityId() : 0L;
	}

	/**
	 * Returns <code>true</code> if this user is the community administrator,
	 * <code>false</code> otherwise
	 * @return	<code>true</code> if this user is the community administrator,
	 * 			<code>false</code> otherwise
	 */
	public boolean isCommunityAdministrator() {
		//TODO Dummy implementation
		return true;
	}
	
	
}
