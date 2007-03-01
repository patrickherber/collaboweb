/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.dao;

import java.util.List;

import ch.arpage.collaboweb.model.Action;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.RelationshipType;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.ResourceValidation;
import ch.arpage.collaboweb.model.ResourceValidationType;
import ch.arpage.collaboweb.model.Validation;
import ch.arpage.collaboweb.model.ValidationType;
import ch.arpage.collaboweb.model.View;
import ch.arpage.collaboweb.model.ViewType;

/**
 * Data Access Object for accessing the definition objects.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface ResourceTypeDao {

	/**
	 * Returns the resource type identified by the given <code>id</code>.
	 * @param id	The ID of the resource type
	 * @return		The requested resource type.
	 */
	ResourceType get(int id);
	
	/**
	 * Inserts the given resource type.
	 * @param resourceType	The resource type to be inserted
	 */
	void insert(ResourceType resourceType);

	/**
	 * Updates the given resource type.
	 * @param resourceType	The resource type to be updated
	 */
	void update(ResourceType resourceType);

	/**
	 * Deletes the given resource type.
	 * @param resourceType	The resource type to be deleted
	 */
	void delete(ResourceType resourceType);
	
	/**
	 * Returns the list of resource types visible for the given community
	 * (either public or defined inside the community).
	 * @param communityId	The communiy's ID 
	 * @return	The list of resource types visible for the given community
	 */
	List<ResourceType> getList(long communityId);
	
	/**
	 * Sets the supported children of the resource type identified by the given
	 * <code>parentId</code>.
	 * @param parentId		The ID of the parent resource
	 * @param childrenIds	The list of IDs of the supported children 
	 */
	void setSupportedChildren(int parentId, Integer[] childrenIds);

	/**
	 * Returns the attribute identified by the given <code>id</code>.
	 * @param id	The ID of the attribute
	 * @return		The requested attribute.
	 */
	Attribute getAttribute(int id);
	
	/**
	 * Inserts the given attribute.
	 * @param attribute	The attribute to be inserted.
	 */
	void insertAttribute(Attribute attribute);

	/**
	 * Updates the given attribute.
	 * @param attribute	The attribute to be updated.
	 */
	void updateAttribute(Attribute attribute);

	/**
	 * Deletes the given attribute.
	 * @param attribute	The attribute to be deleted.
	 */
	void deleteAttribute(Attribute attribute);
	

	/**
	 * Returns the validation type identified by the given <code>id</code>.
	 * @param id	The ID of the validation type
	 * @return		The requested validation type.
	 */
	ValidationType getValidationType(int id);

	/**
	 * Returns the list of validation types visible for the given community
	 * (either public or defined inside the community).
	 * @param communityId	The community's ID
	 * @return	the list of validation types visible for the given community
	 */
	List<ValidationType> getValidationTypes(long communityId);
	
	/**
	 * Inserts the given validation type
	 * @param validationType	The validation type to be inserted
	 */
	void insertValidationType(ValidationType validationType);

	/**
	 * Updates the given validation type
	 * @param validationType	The validation type to be updated
	 */
	void updateValidationType(ValidationType validationType);

	/**
	 * Deletes the given validation type
	 * @param validationType	The validation type to be deleted
	 */
	void deleteValidationType(ValidationType validationType);

	/**
	 * Returns the validation with the given <code>attributeId</code> and
	 * <code>validationTypeId</code>.
	 * @param attributeId		The ID of the attribute
	 * @param validationTypeId	The ID of the validation type
	 * @return					The requested validation
	 */
	Validation getValidation(int attributeId, int validationTypeId);
	
	/**
	 * Saves the given validation.
	 * @param validation	The validation to be saved.
	 */
	void saveValidation(Validation validation);
	
	/**
	 * Deletes the given validation.
	 * @param validation	The validation to be deleted.
	 */
	void deleteValidation(Validation validation);
	

	/**
	 * Returns the resource validation type identified by the given 
	 * <code>id</code>.
	 * @param id	The ID of the resource validation type
	 * @return		The requested resource validation type.
	 */
	ResourceValidationType getResourceValidationType(int id);

	/**
	 * Returns the list of resource validation types visible for the given 
	 * community (either public or defined inside the community).
	 * @param communityId	The community's ID
	 * @return	the list of resource validation types visible for the given 
	 * community
	 */
	List<ResourceValidationType> getResourceValidationTypes(long communityId);

	/**
	 * Inserts the given resource validation type
	 * @param validationType	The resource validation type to be inserted
	 */
	void insertResourceValidationType(ResourceValidationType validationType);

	/**
	 * Updates the given resource validation type
	 * @param validationType	The resource validation type to be updated
	 */
	void updateResourceValidationType(ResourceValidationType validationType);

	/**
	 * Deletes the given resource validation type
	 * @param validationType	The resource validation type to be deleted
	 */
	void deleteResourceValidationType(ResourceValidationType validationType);


	/**
	 * Returns the resource validation with the given <code>typeId</code> and
	 * <code>validationTypeId</code>.
	 * @param typeId			The ID of the resource type
	 * @param validationTypeId	The ID of the resource validation type
	 * @return					The requested resource validation
	 */
	ResourceValidation getResourceValidation(int typeId, int validationTypeId);

	/**
	 * Saves the given resource validation.
	 * @param resourceValidation	The resource validation to be saved.
	 */
	void saveResourceValidation(ResourceValidation resourceValidation);

	/**
	 * Deletes the given resource validation.
	 * @param resourceValidation	The resource validation to be deleted.
	 */
	void deleteResourceValidation(ResourceValidation resourceValidation);


	/**
	 * Returns the view with the given <code>typeId</code> and
	 * <code>viewTypeId</code>.
	 * @param typeId			The ID of the resource type
	 * @param viewTypeId		The ID of the view type
	 * @return					The requested view
	 */
	View getView(int typeId, int viewTypeId);

	/**
	 * Saves the given view.
	 * @param view	The view to be saved.
	 */
	void saveView(View view);

	/**
	 * Deletes the given view.
	 * @param view	The view to be deleted.
	 */
	void deleteView(View view);
	

	/**
	 * Returns the view type identified by the given <code>id</code>.
	 * @param id	The ID of the view type
	 * @return		The requested view type.
	 */
	ViewType getViewType(int id);

	/**
	 * Returns the list of view types visible for the given 
	 * community (either public or defined inside the community).
	 * @param communityId	The community's ID
	 * @return	the list of view types visible for the given community
	 */
	List<ViewType> getViewTypes(long communityId);

	/**
	 * Inserts the given view type
	 * @param viewType	The view type to be inserted
	 */
	void insertViewType(ViewType viewType);

	/**
	 * Updates the given view type
	 * @param viewType	The view type to be updated
	 */
	void updateViewType(ViewType viewType);

	/**
	 * Deletes the given view type
	 * @param viewType	The view type to be deleted
	 */
	void deleteViewType(ViewType viewType);
	

	/**
	 * Returns the action identified by the given <code>id</code>.
	 * @param id	The ID of the action
	 * @return		The requested action.
	 */
	Action getAction(int id);

	/**
	 * Returns the list of actions visible for the given 
	 * community (either public or defined inside the community).
	 * @param communityId	The community's ID
	 * @return	the list of actions visible for the given community
	 */
	List<Action> getActions(long communityId);

	/**
	 * Inserts the given action
	 * @param action	The action to be inserted
	 */
	void insertAction(Action action);

	/**
	 * Updates the given action
	 * @param action	The action to be updated
	 */
	void updateAction(Action action);

	/**
	 * Deletes the given action
	 * @param action	The action to be deleted
	 */
	void deleteAction(Action action);
	

	/**
	 * Returns the relationship type identified by the given <code>id</code>.
	 * @param id	The ID of the relationship type
	 * @return		The requested relationship type.
	 */
	RelationshipType getRelationshipType(int id);

	/**
	 * Returns the list of relationship types visible for the given 
	 * community (either public or defined inside the community).
	 * @param communityId	The community's ID
	 * @return	the list of relationship types visible for the given community
	 */
	List<RelationshipType> getRelationshipTypes(long communityId);

	/**
	 * Inserts the given relationship type
	 * @param relationshipType	The relationship type to be inserted
	 */
	void insertRelationshipType(RelationshipType relationshipType);

	/**
	 * Updates the given relationship type
	 * @param relationshipType	The relationship type to be updated
	 */
	void updateRelationshipType(RelationshipType relationshipType);

	/**
	 * Delete the given relationship type
	 * @param relationshipType	The relationship type to be deleted
	 */
	void deleteRelationshipType(RelationshipType relationshipType);
	
}
