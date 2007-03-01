/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Category model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class Category extends AbstractBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int categoryId;
	
	private long communtiyId;
	
	private String name;
	
	private Category parent;

	/**
	 * Returns the categoryId.
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * Set the categoryId.
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Returns the communtiyId.
	 * @return the communtiyId
	 */
	public long getCommuntiyId() {
		return communtiyId;
	}

	/**
	 * Set the communtiyId.
	 * @param communtiyId the communtiyId to set
	 */
	public void setCommuntiyId(long communtiyId) {
		this.communtiyId = communtiyId;
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the parent.
	 * @return the parent
	 */
	public Category getParent() {
		return parent;
	}

	/**
	 * Set the parent.
	 * @param parent the parent to set
	 */
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	
}
