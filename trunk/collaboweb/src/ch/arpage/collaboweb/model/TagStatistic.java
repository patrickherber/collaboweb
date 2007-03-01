/**
 * collaboweb
 * Feb 3, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Statistic of tag used for creating a tag-cloud
 * 
 * @see <a href="http://randomcoder.com/articles/building-a-tag-cloud-in-java">http://randomcoder.com/articles/building-a-tag-cloud-in-java</a>
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class TagStatistic extends AbstractBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String tag;
	private int count;
	private int scale;
	
	/**
	 * @param tag
	 * @param count
	 */
	public TagStatistic(String tag, int count, int max) {
		super();
		this.tag = tag;
		this.count = count;
		setScale(max);
	}
	
	/**
	 * Set the scale.
	 * @param scale the scale to set
	 */
	private void setScale(int max) {
		if (max <= 0) {
			this.scale = 0;
		} else {
			this.scale = count * 10 / max;
		}
	}

	/**
	 * Returns the tag.
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * Returns the count.
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Returns the scale.
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}
}
