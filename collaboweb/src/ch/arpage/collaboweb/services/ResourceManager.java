/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.services;

import java.util.List;
import java.util.Set;

import ch.arpage.collaboweb.model.BinaryAttribute;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.TagStatistic;
import ch.arpage.collaboweb.model.User;

/**
 * Service used to manage resources.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface ResourceManager {

	/**
	 * Creates a resource of the given type.
	 * 
	 * @param parentId			The ID of the parent resource
	 * @param resourceTypeId	The ID of the resource type
	 * @param user				The current user
	 * @return					The created resource.
	 */
	Resource create(long parentId, int resourceTypeId, User user);

	/**
	 * Returns the resource identified by the given <code>id</code> 
	 * @param id		The ID of the resource
	 * @param user		The current user
	 * @return			The resource identified by the given <code>id</code> 
	 */
	Resource get(long id, User user);
	
	/**
	 * Saves the given resource
	 * @param resource	The resource to be saved
	 * @param user		The current user
	 * @throws Exception
	 */
	void save(Resource resource, User user) throws Exception;
	
	/**
	 * Deletes the resource identified by the given <code>id</code> 
	 * @param id		The ID of the resource
	 * @param user		The current user
	 */
	void delete(long id, User user);
	
	/**
	 * Returns the list of resources whose parent is the resource identified by
	 * the given <code>id</code> 
	 * @param id		The ID of the parent resource
	 * @param user		The current user
	 * @return			The list of resources whose parent is the resource
	 * 					identified by the given <code>id</code>
	 */
	List<Resource> getList(long id, User user);
	
	/**
	 * Moves the given resources identified by the given set of ids under the
	 * resource identified by <code>to</code>.
	 * @param ids		The IDs of the resources to be moved
	 * @param to		The target parent ID
	 * @param user		The current user
	 */
	void move(Set<Long> ids, long to, User user);
	
	/**
	 * Adds a relationship of type <code>relationshipType</code> from the 
	 * resource identified by <code>id</code> to the resources identified by the
	 * IDs in <code>references</code>.
	 * @param id				The ID of the resource
	 * @param relationshipType	The type of relationship
	 * @param references		The target IDs
	 * @param user				The current user
	 */
	void addRelationships(long id, int relationshipType, Set<Long> references, 
			User user);
	
	/**
	 * Deletes the relationship of type <code>relationshipType</code> between 
	 * the resource identified by <code>id</code> and the resource identified 
	 * by <code>referencedId</code>
	 * @param id				The ID of the resource
	 * @param relationshipType	The type of relationship
	 * @param referencedId		The ID of the target resource
	 * @param user				The current user
	 */
	void deleteRelationship(long id, int relationshipType, long referencedId, 
			User user);
	
	/**
	 * Reads the value of a binary attribute.
	 * 
	 * @param resourceId			The ID of the resource
	 * @param attributeIdentifier	The identifier of the attribute
	 * @param user					The current user
	 * @return						A wrapper over the binary content of the 
	 * 								attribute
	 */
	BinaryAttribute readAttribute(long resourceId, String attributeIdentifier, 
			User user);
	
	/**
	 * Returns the list of resources having the given set of tags.
	 * @param tags		The set of tags
	 * @param user		The current user
	 * @return			The list of resources having the given set of tags.
	 */
	List<Resource> getTagList(String[] tags, User user);
	
	/**
	 * Returns the tag-cload of the resources  having the given set of tags.
	 * @param tags		The set of tags
	 * @param user		The current user
	 * @return			The tag-cload of the resources having the given set 
	 * 					of tags.
	 */
	Set<TagStatistic> getTagCloud(String[] tags, User user);
	
	/**
	 * Adds the given tag to the given resource
	 * @param resourceId	The ID of the resource
	 * @param tag			The new tag to be added
	 * @param user			The current user
	 */
	void addTag(long resourceId, String tag, User user);

	/**
	 * Deletes the given tag from the given resource
	 * @param resourceId	The ID of the resource
	 * @param tag			The tag to be deleted
	 * @param user			The current user
	 */
	void deleteTag(long resourceId, String tag, User user);
}
