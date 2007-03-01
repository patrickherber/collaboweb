/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import java.sql.Timestamp;

/**
 * Tag
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Tag extends AbstractBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private long authorId;
	
	private String tag;
	
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
	 * Returns the tag.
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Set the tag.
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
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
