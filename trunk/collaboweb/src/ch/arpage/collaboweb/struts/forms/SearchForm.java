/**
 * collaboweb
 * Jan 28, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import java.util.Map;

import ch.arpage.collaboweb.model.SearchQuery;

/**
 * Form used for searching inside the repository.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class SearchForm extends AbstractForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
		
	private SearchQuery searchQuery = new SearchQuery();
	
	/**
	 * Returns the searchQuery.
	 * @return the searchQuery
	 */
	public SearchQuery getSearchQuery() {
		return searchQuery;
	}

	/**
	 * Returns the fields.
	 * @return the fields
	 * @see ch.arpage.collaboweb.model.SearchQuery#getFields()
	 */
	public Map<String, String> getFields() {
		return searchQuery.getFields();
	}

	/**
	 * Returns the name.
	 * @return the name
	 * @see ch.arpage.collaboweb.model.SearchQuery#getName()
	 */
	public String getName() {
		return searchQuery.getName();
	}

	/**
	 * Returns the quickSearch.
	 * @return the quickSearch
	 * @see ch.arpage.collaboweb.model.SearchQuery#getQuickSearch()
	 */
	public String getQuickSearch() {
		return searchQuery.getQuickSearch();
	}

	/**
	 * Returns the typeId.
	 * @return the typeId
	 * @see ch.arpage.collaboweb.model.SearchQuery#getTypeId()
	 */
	public int getTypeId() {
		return searchQuery.getTypeId();
	}

	/**
	 * Returns the updateDate.
	 * @return the updateDate
	 * @see ch.arpage.collaboweb.model.SearchQuery#getUpdateDate()
	 */
	public String getUpdateDate() {
		return searchQuery.getUpdateDate();
	}

	/**
	 * Returns the updateDateComparator.
	 * @return the updateDateComparator
	 * @see ch.arpage.collaboweb.model.SearchQuery#getUpdateDateComparator()
	 */
	public int getUpdateDateComparator() {
		return searchQuery.getUpdateDateComparator();
	}

	/**
	 * Set the name.
	 * @param name the name to set
	 * @see ch.arpage.collaboweb.model.SearchQuery#setName(java.lang.String)
	 */
	public void setName(String name) {
		searchQuery.setName(name);
	}

	/**
	 * Set the quickSearch.
	 * @param quickSearch the quickSearch to set
	 * @see ch.arpage.collaboweb.model.SearchQuery#setQuickSearch(java.lang.String)
	 */
	public void setQuickSearch(String quickSearch) {
		searchQuery.setQuickSearch(quickSearch);
	}

	/**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 * @see ch.arpage.collaboweb.model.SearchQuery#setTypeId(int)
	 */
	public void setTypeId(int typeId) {
		searchQuery.setTypeId(typeId);
	}

	/**
	 * Set the updateDate.
	 * @param updateDate the updateDate to set
	 * @see ch.arpage.collaboweb.model.SearchQuery#setUpdateDate(java.lang.String)
	 */
	public void setUpdateDate(String updateDate) {
		searchQuery.setUpdateDate(updateDate);
	}

	/**
	 * Set the updateDateComparator.
	 * @param updateDateComparator the updateDateComparator to set
	 * @see ch.arpage.collaboweb.model.SearchQuery#setUpdateDateComparator(int)
	 */
	public void setUpdateDateComparator(int updateDateComparator) {
		searchQuery.setUpdateDateComparator(updateDateComparator);
	}

	/**
	 * Returns the free.
	 * @return the free
	 * @see ch.arpage.collaboweb.model.SearchQuery#getFree()
	 */
	public String getFree() {
		return searchQuery.getFree();
	}

	/**
	 * Set the free.
	 * @param free the free to set
	 * @see ch.arpage.collaboweb.model.SearchQuery#setFree(java.lang.String)
	 */
	public void setFree(String free) {
		searchQuery.setFree(free);
	}
	
}
