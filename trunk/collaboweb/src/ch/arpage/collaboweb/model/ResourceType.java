/**
 * CollaboWeb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.model;

import java.util.LinkedList;
import java.util.List;

import ch.arpage.collaboweb.exceptions.ValidationException;

/**
 * Resource type model class that represents a type of object in the repository
 * (for example the type "Contact", "Member", "File", etc.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceType extends LabelableBean {

	private static final long serialVersionUID = 1L;
	
	private int typeId;
	private long communityId;
	private int familyId;
	private boolean timeView;
	
	private List<Attribute> attributes;
	
	private List<View> views;
	
	private List<Integer> supportedChildrenIds;
	
	private List<Aspect> aspects;
	
	private List<Category> categories;
	
	private List<ResourceValidation> resourceValidations;
	
	
	/**
	 * Returns the communityId.
	 * @return the communityId
	 */
	public long getCommunityId() {
		return communityId;
	}
	/**
	 * Set the communityId.
	 * @param communityId the communityId to set
	 */
	public void setCommunityId(long communityId) {
		this.communityId = communityId;
	}
	/**
	 * Returns the familyId.
	 * @return the familyId
	 */
	public int getFamilyId() {
		return familyId;
	}
	/**
	 * Set the familyId.
	 * @param familyId the familyId to set
	 */
	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}
	/**
	 * Returns the timeView.
	 * @return the timeView
	 */
	public boolean isTimeView() {
		return timeView;
	}
	/**
	 * Set the timeView.
	 * @param timeView the timeView to set
	 */
	public void setTimeView(boolean timeView) {
		this.timeView = timeView;
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
	 * Returns the aspects.
	 * @return the aspects
	 */
	public List<Aspect> getAspects() {
		return aspects;
	}
	/**
	 * Set the aspects.
	 * @param aspects the aspects to set
	 */
	public void setAspects(List<Aspect> aspects) {
		this.aspects = aspects;
	}
	/**
	 * Returns the attributes.
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * Return the attribute identified by the given ID
	 * @param attributeId	The ID of the attribute
	 * @return				The attribute identified by the given ID
	 */
	public Attribute getAttribute(int attributeId) {
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				if (attribute.getAttributeId() == attributeId) {
					return attribute;
				}
			}
		}
		return null;
	}
	
	/**
	 * Return the attribute identified by the given identifier
	 * @param identifier	The identifier of the attribute
	 * @return				The attribute identified by the given identifier
	 */
	public Attribute getAttribute(String identifier) {
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				if (identifier.equals(attribute.getIdentifier())) {
					return attribute;
				}
			}
		}
		return null;
	}
	
	/**
	 * Set the attributes.
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	/**
	 * Returns the categories.
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}
	/**
	 * Set the categories.
	 * @param categories the categories to set
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	/**
	 * Returns the supportedChildrenIds.
	 * @return the supportedChildrenIds
	 */
	public List<Integer> getSupportedChildrenIds() {
		return supportedChildrenIds;
	}
	/**
	 * Set the supportedChildrenIds.
	 * @param supportedChildrenIds the supportedChildrenIds to set
	 */
	public void setSupportedChildrenIds(List<Integer> supportedChildrenIds) {
		this.supportedChildrenIds = supportedChildrenIds;
	}
	/**
	 * Returns the resourceValidations.
	 * @return the resourceValidations
	 */
	public List<ResourceValidation> getResourceValidations() {
		return resourceValidations;
	}
	/**
	 * Set the resourceValidations.
	 * @param resourceValidations the resourceValidations to set
	 */
	public void setResourceValidations(
			List<ResourceValidation> resourceValidations) {
		this.resourceValidations = resourceValidations;
	}
	/**
	 * Returns the views.
	 * @return the views
	 */
	public List<View> getViews() {
		return views;
	}
	/**
	 * Set the views.
	 * @param views the views to set
	 */
	public void setViews(List<View> views) {
		this.views = views;
	}
	
	/**
	 * Adds the given view to the views' collection.
	 * @param view the view to be added
	 */
	public void addView(View view) {
		if (views == null) {
			views = new LinkedList<View>();
		}
		boolean found = false;
		for (int i = 0; i < views.size(); ++i) {
			if (views.get(i).getViewTypeId() == view.getViewTypeId()) {
				views.remove(i);
				views.add(i, view);
				found = true;
				break;
			}
		}
		if (!found) {
			views.add(view);
		}
	}
	
	/**
	 * Removes the given view.
	 * @param view 	The view to remove
	 */
	public void removeView(View view) {
		if (views != null) {
			for (View v : views) {
				if (v.getViewTypeId() == view.getViewTypeId()) {
					views.remove(v);
					break;
				}
			}
		}
	}
	
	/**
	 * Returns the stylesheet of the view identified by the given viewTypeId.
	 * @param viewTypeId The view identified by the given viewTypeId.
	 */
	public String getViewStylesheet(int viewTypeId) {
		if (views != null) {
			for (View view : views) {
				if (view.getViewType().getViewTypeId() == viewTypeId) {
					return view.getStyleSheet();
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the view identified by the given viewTypeId.
	 * @param viewTypeId The view identified by the given viewTypeId.
	 */
	public View getView(int viewTypeId) {
		if (views != null) {
			for (View view : views) {
				if (view.getViewType().getViewTypeId() == viewTypeId) {
					return view;
				}
			}
		}
		return null;
	}
	
	/**
	 * Validates the given resource.
	 * @param resource the resource to validate
	 * @param user the current user
	 * @throws ValidationException if the validation fails
	 * @throws Exception if an error occurs
	 */
	public void validate(Resource resource, User user) 
			throws ValidationException, Exception {
		List<String> validationErrors = new LinkedList<String>();
		validateResource(resource, user, validationErrors);
		validateAttributes(resource, user, validationErrors);
		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
	}
	
	/**
	 * Validates the attributes of the given resource
	 * @param resource the resource to validate
	 * @param user the current user
	 * @param validationErrors the list of errors to be eventually filled
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private void validateAttributes(Resource resource, User user, 
			List<String> validationErrors) throws InstantiationException, 
			IllegalAccessException, ClassNotFoundException {
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				List<Validation> validations = attribute.getValidations();
				if (validations != null) {
					ResourceAttribute ra = resource.getResourceAttribute(
							attribute.getAttributeId());
					for (Validation validation : validations) {
						String error = validation.validate(attribute,
								(ra != null) ? ra.getValue() : null, 
								user.getLanguage());
						if (error != null) {
							validationErrors.add(error);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Validate the given resource.
	 * @param resource the resource to validate
	 * @param user the current user
	 * @param validationErrors the list of errors to be eventually filled
	 */
	private void validateResource(Resource resource, User user, 
			List<String> validationErrors) {
		if (resourceValidations != null) {
			for (ResourceValidation validation : resourceValidations) {
				String error = validation.validate(resource, user);
				if (error != null) {
					validationErrors.add(error);
				}
			}
		}
	}
	
	/**
	 * Removes the given resourceValidation
	 * @param resourceValidation The resourceValidation to remove
	 */
	public void removeResourceValidation(ResourceValidation resourceValidation) {
		if (resourceValidations != null) {
			for (ResourceValidation v : resourceValidations) {
				if (v.getValidationTypeId() == 
					resourceValidation.getValidationTypeId()) {
					resourceValidations.remove(v);
					break;
				}
			}
		}
	}
	
	/**
	 * Adds the given resource validation to the resource validations' collection.
	 * @param resourceValidation the resource validation to be added
	 */
	public void addResourceValidation(ResourceValidation resourceValidation) {
		if (resourceValidations == null) {
			resourceValidations = new LinkedList<ResourceValidation>();
		}
		boolean found = false;
		for (int i = 0; i < resourceValidations.size(); ++i) {
			if (resourceValidations.get(i).getValidationTypeId() == 
				resourceValidation.getValidationTypeId()) {
				resourceValidations.remove(i);
				resourceValidations.add(i, resourceValidation);
				found = true;
				break;
			}
		}
		if (!found) {
			resourceValidations.add(resourceValidation);
		}
	}
}
