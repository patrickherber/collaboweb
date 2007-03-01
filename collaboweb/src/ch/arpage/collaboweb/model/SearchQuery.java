/**
 * collaboweb
 * Feb 20, 2007
 */
package ch.arpage.collaboweb.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * Search query object
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class SearchQuery extends AbstractBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String updateDate;
	private int updateDateComparator;
	private String quickSearch;
	private String name;
	private String free;
	private int typeId;
	private final Map<String, String> fields = new TreeMap<String, String>();
	
	/**
	 * Returns the fields.
	 * @return the fields
	 */
	public Map<String, String> getFields() {
		return fields;
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
	 * Returns the quickSearch.
	 * @return the quickSearch
	 */
	public String getQuickSearch() {
		return quickSearch;
	}
	/**
	 * Set the quickSearch.
	 * @param quickSearch the quickSearch to set
	 */
	public void setQuickSearch(String quickSearch) {
		this.quickSearch = quickSearch;
	}
	/**
	 * Returns the typeId.
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}
	/**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	/**
	 * Returns the updateDate.
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}
	/**
	 * Set the updateDate.
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * Returns the updateDateComparator.
	 * @return the updateDateComparator
	 */
	public int getUpdateDateComparator() {
		return updateDateComparator;
	}
	/**
	 * Set the updateDateComparator.
	 * @param updateDateComparator the updateDateComparator to set
	 */
	public void setUpdateDateComparator(int updateDateComparator) {
		this.updateDateComparator = updateDateComparator;
	}
	/**
	 * Returns the free.
	 * @return the free
	 */
	public String getFree() {
		return free;
	}
	/**
	 * Set the free.
	 * @param free the free to set
	 */
	public void setFree(String free) {
		this.free = free;
	}
}
