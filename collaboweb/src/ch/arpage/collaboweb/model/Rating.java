/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import java.sql.Timestamp;

/**
 * Rating
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Rating extends AbstractBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private long authorId;
	
	private int value;
	
	private Timestamp timestamp;

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
	 * Returns the value.
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Returns the timestamp.
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * Set the timestamp.
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
