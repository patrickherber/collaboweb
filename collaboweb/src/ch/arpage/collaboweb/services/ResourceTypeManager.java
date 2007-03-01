/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.services;

import java.util.List;

import ch.arpage.collaboweb.model.Action;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.RelationshipType;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.ResourceValidation;
import ch.arpage.collaboweb.model.ResourceValidationType;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.model.Validation;
import ch.arpage.collaboweb.model.ValidationType;
import ch.arpage.collaboweb.model.View;
import ch.arpage.collaboweb.model.ViewType;

/**
 * Service used to manage the object definitions (resource types, actions,
 * validations, views, etc.).
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface ResourceTypeManager {
	
	/**
	 * Returns the resource type identified by the given <code>id</code>.
	 * @param id	The ID of the resource type
	 * @param user	The current type
	 * @return		The requested resource type
	 */
	ResourceType get(int id, User user);
	
	/**
	 * Saves the given resource type
	 * @param bean	The resource type
	 * @param user	The current user
	 */
	void save(ResourceType bean, User user);

	/**
	 * Deletes the resource type identified by the given <code>id</code>.
	 * @param id	The ID of the resource type
	 * @param user	The current user
	 */
	void delete(int id, User user);

	/**
	 * Returns the list of resource types visible for the given user
	 * (either public or defined inside the community).
	 * @param user	The current user
	 * @return	The list of resource types visible for the given user
	 */
	List<ResourceType> getList(User user);

	/**
	 * Sets the supported children of the resource type identified by the given
	 * <code>parentId</code>.
	 * @param parentId		The ID of the parent resource
	 * @param childrenIds	The list of IDs of the supported children 
	 * @param user	The current user
	 */
	void setSupportedChildren(int parentId, Integer[] childrenIds, User user);
	

	/**
	 * Returns the attribute identified by the given <code>id</code>.
	 * @param id	The ID of the attribute
	 * @param user	The current user
	 * @return		The requested attribute.
	 */
	Attribute getAttribute(int id, User user);
	
	/**
	 * Saves the given attribute
	 * @param attribute	The attribute to be saved
	 * @param user		The current user
	 */
	void saveAttribute(Attribute attribute, User user);

	/**
	 * Deletes the attribute identified by the given <code>id</code>.
	 * @param id		The ID of the attribute
	 * @param user		The current user
	 */
	void deleteAttribute(int id, User user);

	/**
	 * Returns the validation identified by the given <code>attributeId</code>
	 * and <code>validationTypeId</code>.
	 * @param attributeId		The ID of the attribute
	 * @param validationTypeId	The ID of the validation type
	 * @param user				The current user
	 * @return					The requested validation.
	 */
	Validation getValidation(int attributeId, int validationTypeId, User user);

	/**
	 * Saves the given validation
	 * @param validation	The validation to be saved
	 * @param user			The current user
	 */
	void saveValidation(Validation validation, User user);

	/**
	 * Deletes the validation identified by the given <code>attributeId</code>
	 * and <code>validationTypeId</code>.
	 * @param attributeId		The ID of the attribute
	 * @param validationTypeId	The ID of the validation type
	 * @param user				The current user
	 */
	void deleteValidation(int attributeId, int validationTypeId, User user);


	/**
	 * Returns the validation type identified by the given <code>id</code>.
	 * @param id	The ID of the validation type
	 * @param user	The current user
	 * @return		The requested validation type.
	 */
	ValidationType getValidationType(int id, User user);

	/**
	 * Saves the given validation type
	 * @param validationType	The validation type to be saved
	 * @param user				The current user
	 */
	void saveValidationType(ValidationType validationType, User user);
	
	/**
	 * Deletes the validation type identified by the given <code>id</code>.
	 * @param id		The ID of the validation type
	 * @param user		The current user
	 */
	void deleteValidationType(int id, User user);

	/**
	 * Returns the list of validation types visible for the given user
	 * (either public or defined inside the community).
	 * @param user	The current user
	 * @return	The list of validation types visible for the given user
	 */
	List<ValidationType> getValidationTypes(User user);


	/**
	 * Returns the resource validation identified by the given <code>typeId</code>
	 * and <code>validationTypeId</code>.
	 * @param typeId			The ID of the resource type
	 * @param validationTypeId	The ID of the resource validation type
	 * @param user				The current user
	 * @return					The requested resource validation.
	 */
	ResourceValidation getResourceValidation(int typeId, int validationTypeId, User user);

	/**
	 * Saves the given resource validation
	 * @param validation	The resource validation to be saved
	 * @param user			The current user
	 */
	void saveResourceValidation(ResourceValidation validation, User user);

	/**
	 * Deletes the resource validation identified by the given <code>typeId</code>
	 * and <code>validationTypeId</code>.
	 * @param typeId			The ID of the resource type
	 * @param validationTypeId	The ID of the resource validation type
	 * @param user				The current user
	 */
	void deleteResourceValidation(int typeId, int validationTypeId, User user);


	/**
	 * Returns the resource validation type identified by the given <code>id</code>.
	 * @param id	The ID of the resource validation type
	 * @param user	The current user
	 * @return		The requested resource validation type.
	 */
	ResourceValidationType getResourceValidationType(int id, User user);

	/**
	 * Saves the given resource validation type
	 * @param resourceValidationType	The resource validation type to be saved
	 * @param user						The current user
	 */
	void saveResourceValidationType(ResourceValidationType resourceValidationType, User user);

	/**
	 * Deletes the resource validation type identified by the given <code>id</code>.
	 * @param id		The ID of the resource validation type
	 * @param user		The current user
	 */
	void deleteResourceValidationType(int id, User user);

	/**
	 * Returns the list of resource validation types visible for the given user
	 * (either public or defined inside the community).
	 * @param user	The current user
	 * @return	The list of resource validation types visible for the given user
	 */
	List<ResourceValidationType> getResourceValidationTypes(User user);
	

	/**
	 * Returns the view type identified by the given <code>id</code>.
	 * @param id	The ID of the view type
	 * @param user	The current user
	 * @return		The requested view type.
	 */
	ViewType getViewType(int id, User user);

	/**
	 * Saves the given view type
	 * @param viewType	The view type to be saved
	 * @param user		The current user
	 */
	void saveViewType(ViewType viewType, User user);

	/**
	 * Deletes the view type identified by the given <code>id</code>.
	 * @param id		The ID of the view type
	 * @param user		The current user
	 */
	void deleteViewType(int id, User user);

	/**
	 * Returns the list of view types visible for the given user
	 * (either public or defined inside the community).
	 * @param user	The current user
	 * @return		The list of view types visible for the given user
	 */
	List<ViewType> getViewTypes(User user);


	/**
	 * Returns the view identified by the given <code>typeId</code>
	 * and <code>viewTypeId</code>.
	 * @param typeId		The ID of the resource type
	 * @param viewTypeId	The ID of the view type
	 * @param user			The current user
	 * @return				The requested view.
	 */
	View getView(int typeId, int viewTypeId, User user);

	/**
	 * Saves the given view
	 * @param view			The view to be saved
	 * @param user			The current user
	 */
	void saveView(View view, User user);

	/**
	 * Deletes the view identified by the given <code>typeId</code>
	 * and <code>viewTypeId</code>.
	 * @param typeId		The ID of the resource type
	 * @param viewTypeId	The ID of the view type
	 * @param user			The current user
	 */
	void deleteView(int typeId, int viewTypeId, User user);


	/**
	 * Returns the action identified by the given <code>id</code>.
	 * @param id	The ID of the action
	 * @param user	The current user
	 * @return		The requested action.
	 */
	Action getAction(int id, User user);

	/**
	 * Saves the given action
	 * @param action	The action to be saved
	 * @param user		The current user
	 */
	void saveAction(Action action, User user);

	/**
	 * Deletes the action identified by the given <code>id</code>.
	 * @param id		The ID of the action
	 * @param user		The current user
	 */
	void deleteAction(int id, User user);

	/**
	 * Returns the list of action visible for the given user
	 * (either public or defined inside the community).
	 * @param user	The current user
	 * @return		The list of action visible for the given user
	 */
	List<Action> getActions(User user);
	


	/**
	 * Returns the relationship type identified by the given <code>id</code>.
	 * @param id	The ID of the relationship type
	 * @param user	The current user
	 * @return		The requested relationship type.
	 */
	RelationshipType getRelationshipType(int id, User user);

	/**
	 * Saves the given relationship type
	 * @param relationshipType	The relationship type to be saved
	 * @param user				The current user
	 */
	void saveRelationshipType(RelationshipType relationshipType, User user);

	/**
	 * Deletes the relationship type identified by the given <code>id</code>.
	 * @param id		The ID of the relationship type
	 * @param user		The current user
	 */
	void deleteRelationshipType(int id, User user);

	/**
	 * Returns the list of relationship types visible for the given user
	 * (either public or defined inside the community).
	 * @param user	The current user
	 * @return		The list of relationship types visible for the given user
	 */
	List<RelationshipType> getRelationshipTypes(User user);
}
