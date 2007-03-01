/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.services;

import java.util.List;

import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.SearchQuery;
import ch.arpage.collaboweb.model.User;

/**
 * Service used to perform searches inside the repository and also to 
 * index objects, so that you can search them.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface IndexerManager {

	/**
	 * Index the given resource.
	 * 
	 * @param resource	The resource to index
	 * @param user		The current user
	 */
	void index(Resource resource, User user);

	/**
	 * Deletes the resource identified by the given resourceId from the search
	 * index.
	 * @param resourceId	The Id of the resource to be deleted
	 * @param user			The current user
	 */
	void deleteFromIndex(long resourceId, User user);
	
	/**
	 * Performs a search in the repository using the given query.
	 * 
	 * @param query			The search query
	 * @param user			The current user
	 * @return				The results of the search
	 * @throws Exception	If an error occurs
	 */
	List search(SearchQuery query, User user) throws Exception;
		
}
