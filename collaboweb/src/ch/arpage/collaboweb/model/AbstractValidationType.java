/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Abstract validation type model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class AbstractValidationType extends LabelableBean {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int validationTypeId;
	
	private long communityId;
	
	private String className;
	
	private Map<String, String> messages;

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

	/**
	 * Returns the validationTypeId.
	 * @return the validationTypeId
	 */
	public int getValidationTypeId() {
		return validationTypeId;
	}

	/**
	 * Set the validationTypeId.
	 * @param validationTypeId the validationTypeId to set
	 */
	public void setValidationTypeId(int validationTypeId) {
		this.validationTypeId = validationTypeId;
	}
	
	/**
	 * Returns the messages.
	 * @return the messages
	 */
	public Map<String, String> getMessages() {
		checkMessages();
		return messages;
	}
	
	/**
	 * Set the messages.
	 * @param messages the messages to set
	 */
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}
	
	/**
	 * Set a message with the given key
	 * @param key	the message key
	 * @param value	The mssage value
	 */
	public void setMessage(String key, String value) {
		checkMessages();
		this.messages.put(key, value);
	}
	
	public String getMessage(String language) {
		checkMessages();
		String label = messages.get(language);
		if (!StringUtils.hasText(label)) {
			label = messages.get(Constants.DEFAULT_LANGUAGE);
		}
		return label;
	}

	private void checkMessages() {
		if (messages == null) {
			messages = new HashMap<String, String>();
		}
	}
}
