/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.dao;

import java.util.List;
import java.util.Set;

import ch.arpage.collaboweb.model.BinaryAttribute;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.TagStatistic;
import ch.arpage.collaboweb.model.User;

/**
 * Data Access Object for accessing the resources.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface ResourceDao {

	/**
	 * Returns the resource identified by the given <code>resourceId</code>, 
	 * checking the <code>user</code> permissions.
	 * 
	 * @param resourceId The ID of the resource to be retrieved
	 * @param user		 The requesting user
	 * @return			 The requested resource
	 */
	Resource get(long resourceId, User user);
	
	/**
	 * Updates the given resource.
	 * 
	 * @param bean The resource to be updated
	 */
	void update(Resource bean);
	
	/**
	 * Inserts the given resource.
	 * 
	 * @param bean The resource to be created.
	 */
	void insert(Resource bean);
	
	/**
	 * Deletes the given resource.
	 * 
	 * @param bean The resource to be deleted.
	 */
	void delete(Resource bean);
	
	/**
	 * Returns the list of child-objects of the resource identified by the given
	 * <code>id</code>, filtered by the permissions of the given 
	 * <code>user</code>.
	 * 
	 * @param id	The ID of the father-resource.
	 * @param user	The requesting user
	 * @return		The list of child-objects of the given resource
	 */
	List<Resource> getList(long id, User user);
	
	/**
	 * Move the resource identified by <code>id</code> als child of the resource
	 * identified by <code>to</code>.
	 * 
	 * @param id	The ID of the resource to be moved
	 * @param to	The ID of the target father-resource
	 */
	void move(long id, long to);
	
	/**
	 * Adds a relationship of type <code>relationshipType</code> from the 
	 * resource identified by <code>id</code> to the IDs contained in the 
	 * <code>references</code> list.
	 * 
	 * @param id				The ID of the resource
	 * @param relationshipType	The type of relationship
	 * @param references 		The list of IDs
	 */
	void addRelationships(long id, int relationshipType, Set<Long> references);
	
	/**
	 * Deletes the relationship <code>relationshipType</code> between the 
	 * resources identified by <code>id</code> and <code>referencedId</code>.
	 * 
	 * @param id				The ID of the referencing object
	 * @param relationshipType	The type of relationship
	 * @param referencedId		The ID of the referred object
	 */
	void deleteRelationship(long id, int relationshipType, long referencedId);
	
	/**
	 * Returs the list of resources marked with the <code>tags</code>, 
	 * filtered by the permissions of the requesting user.
	 * 
	 * @param tags	The list of tags
	 * @param user	The requesting user
	 * @return		The list of resources marked with the <code>tags</code>
	 */
	List<Resource> getTagList(String[] tags, User user);
	
	/**
	 * Returns the tag cloud of the resources marked with the <code>tags</code>, 
	 * filtered by the permissions of the requesting user.
	 * 
	 * @param tags	The list of tags
	 * @param user	The requesting user
	 * @return		The tag cloud of resources marked with the <code>tags</code>
	 */
	Set<TagStatistic> getTagCloud(String[] tags, User user);
	
	/**
	 * Returns the bynary content of the attribute of the given resource.
	 * 
	 * @param resourceId			The ID of the resource
	 * @param attributeIdentifier	The identifier of the attribute
	 * @return						The binary content of the attribute
	 */
	BinaryAttribute readAttribute(long resourceId, String attributeIdentifier);
	
	/**
	 * Adds the specified <code>tag</code> to the given resource.
	 * 
	 * @param resourceId	The ID of the resource
	 * @param tag			The new tag
	 * @param userId		The ID of the author
	 */
	void addTag(long resourceId, String tag, long userId);
	
	/**
	 * Deletes the specified <code>tag</code> from the given resource.
	 * 
	 * @param resourceId	The ID of the resource
	 * @param tag			The tag to be removed
	 * @param userId		The ID of the author
	 */
	void deleteTag(long resourceId, String tag, long userId);
}
