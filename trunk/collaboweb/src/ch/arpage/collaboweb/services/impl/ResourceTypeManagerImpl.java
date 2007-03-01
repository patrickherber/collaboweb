/**
 * collaboweb5
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.services.impl;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.dao.ResourceTypeDao;
import ch.arpage.collaboweb.exceptions.NotAuthorizedException;
import ch.arpage.collaboweb.exceptions.ValidationException;
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
import ch.arpage.collaboweb.model.validation.Validator;
import ch.arpage.collaboweb.services.ResourceTypeManager;
import ch.arpage.collaboweb.services.SecurityService;
import ch.arpage.collaboweb.services.validation.ResourceValidator;

/**
 * Implementation of the Resource Type Manager service.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeManagerImpl implements ResourceTypeManager, BeanFactoryAware {

	private BeanFactory beanFactory;
	
	private ResourceTypeDao dao;
	private ResourceBundleMessageSource messageSource;
	private SecurityService securityService;
	
	/**
	 * Set the dao.
	 * @param dao the dao to set
	 */
	public void setDao(ResourceTypeDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Set the messageSource.
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * Set the securityService.
	 * @param securityService the securityService to set
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#delete(int, ch.arpage.collaboweb.model.User)
	 */
	public void delete(int id, User user) {
		ResourceType bean = get(id, user); 
		checkCommunityAdministrationRight(user, bean.getCommunityId());
		dao.delete(bean);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#get(int)
	 */
	public ResourceType get(int id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.get(id);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getList(ch.arpage.collaboweb.model.User)
	 */
	public List<ResourceType> getList(User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getList(user.getCommunityId());
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#save(ch.arpage.collaboweb.model.ResourceType)
	 */
	public void save(ResourceType bean, User user) {
		securityService.checkAuthenticatedUser(user);
		if (bean.getTypeId() == 0) {
			bean.setCommunityId(user.getCommunityId());
			checkCommunityAdministrationRight(user, bean.getCommunityId());
			dao.insert(bean);
		} else {
			checkCommunityAdministrationRight(user, bean.getCommunityId());
			dao.update(bean);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#setSupportedChildren(int, Integer[], ch.arpage.collaboweb.model.User)
	 */
	public void setSupportedChildren(int parentId, Integer[] childrenIds, User user) {
		securityService.checkAuthenticatedUser(user);
		dao.setSupportedChildren(parentId, childrenIds);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getAttribute(int, int, ch.arpage.collaboweb.model.User)
	 */
	public Attribute getAttribute(int attributeId, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getAttribute(attributeId);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteAttribute(int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteAttribute(int id, User user) {
		Attribute attribute = getAttribute(id, user);
		securityService.checkAuthenticatedUser(user);
		ResourceType resourceType = get(attribute.getTypeId(), user);
		checkCommunityAdministrationRight(user, resourceType.getCommunityId());
		dao.deleteAttribute(attribute);
		resourceType.getAttributes().remove(attribute);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveAttribute(ch.arpage.collaboweb.model.Attribute, ch.arpage.collaboweb.model.User)
	 */
	public void saveAttribute(Attribute attribute, User user) {
		securityService.checkAuthenticatedUser(user);
		validate(attribute, user);
		if (attribute.getAttributeId() == 0) {
			dao.insertAttribute(attribute);
		} else {
			dao.updateAttribute(attribute);
		}
	}
	
	/**
	 * @param attribute
	 */
	private void validate(Attribute attribute, User user) {
		if (!StringUtils.hasText(attribute.getIdentifier())) {
			throw new ValidationException(messageSource.getMessage(
					"errors.attribute.identifier.empty", 
					null, user.getLocale()));
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteValidationType(int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteValidationType(int id, User user) {
		ValidationType validationType = getValidationType(id, user);
		checkCommunityAdministrationRight(user, 
				validationType.getCommunityId());
		dao.deleteValidationType(validationType);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getValidationType(int, ch.arpage.collaboweb.model.User)
	 */
	public ValidationType getValidationType(int id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getValidationType(id);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getValidationTypes(ch.arpage.collaboweb.model.User)
	 */
	public List<ValidationType> getValidationTypes(User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getValidationTypes(user.getCommunityId());
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveValidationType(ch.arpage.collaboweb.model.ValidationType, ch.arpage.collaboweb.model.User)
	 */
	public void saveValidationType(ValidationType validationType, User user) {
		securityService.checkAuthenticatedUser(user);
		if (validationType.getValidationTypeId() == 0) {
			validationType.setCommunityId(user.getCommunityId());
			checkCommunityAdministrationRight(user, 
					validationType.getCommunityId());
			dao.insertValidationType(validationType);
		} else {
			checkCommunityAdministrationRight(user, 
					validationType.getCommunityId());
			dao.updateValidationType(validationType);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteValidation(int, int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteValidation(int attributeId, int validationTypeId, User user) {
		Validation validation = getValidation(attributeId, validationTypeId, user);
		checkCommunityAdministrationRight(user, 
				validation.getValidationType().getCommunityId());
		dao.deleteValidation(validation);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getValidation(int, int, ch.arpage.collaboweb.model.User)
	 */
	public Validation getValidation(int attributeId, int validationTypeId, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getValidation(attributeId, validationTypeId);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveValidation(ch.arpage.collaboweb.model.Validation, ch.arpage.collaboweb.model.User)
	 */
	public void saveValidation(Validation validation, User user) {
		securityService.checkAuthenticatedUser(user);
		ValidationType vt = validation.getValidationType();
		if (vt == null) {
			vt = getValidationType(validation.getValidationTypeId(), user);
			validation.setValidationType(vt);
		}
		checkCommunityAdministrationRight(user, vt.getCommunityId());
		Validator validator = null;
		try {
			validator = vt.getValidator();
		} catch (Exception e) {
			throw new ValidationException(messageSource.getMessage(
					"errors.validation.className.invalid", 
					null, user.getLocale()));
		}
		if (!validator.isValidParameter(validation.getParameter())) {
			throw new ValidationException(messageSource.getMessage(
					"errors.validation.parameter.invalid", 
					null, user.getLocale()));
		}
		dao.saveValidation(validation);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteViewType(int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteViewType(int id, User user) {
		ViewType viewType = getViewType(id, user);
		checkCommunityAdministrationRight(user, viewType.getCommunityId());
		dao.deleteViewType(viewType);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getViewType(int, ch.arpage.collaboweb.model.User)
	 */
	public ViewType getViewType(int id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getViewType(id);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getViewTypes(long, ch.arpage.collaboweb.model.User)
	 */
	public List<ViewType> getViewTypes(User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getViewTypes(user.getCommunityId());
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveViewType(ch.arpage.collaboweb.model.ViewType, ch.arpage.collaboweb.model.User)
	 */
	public void saveViewType(ViewType viewType, User user) {
		securityService.checkAuthenticatedUser(user);
		if (!StringUtils.hasText(viewType.getContentType())) {
			viewType.setContentType("text/html");
		}
		if (viewType.getViewTypeId() == 0) {
			viewType.setCommunityId(user.getCommunityId());
			checkCommunityAdministrationRight(user, 
					viewType.getCommunityId());
			dao.insertViewType(viewType);
		} else {
			checkCommunityAdministrationRight(user, 
					viewType.getCommunityId());
			dao.updateViewType(viewType);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteView(int, int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteView(int typeId, int viewTypeId, User user) {
		View view = getView(typeId, viewTypeId, user);
		checkCommunityAdministrationRight(user, 
				view.getViewType().getCommunityId());
		dao.deleteView(view);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getView(int, int, ch.arpage.collaboweb.model.User)
	 */
	public View getView(int typeId, int viewTypeId, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getView(typeId, viewTypeId);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveView(ch.arpage.collaboweb.model.View, ch.arpage.collaboweb.model.User)
	 */
	public void saveView(View view, User user) {
		securityService.checkAuthenticatedUser(user);
		if (view.getViewType() == null) {
			view.setViewType(getViewType(view.getViewTypeId(), user));
		}
		checkCommunityAdministrationRight(user, 
				view.getViewType().getCommunityId());
		if (!StringUtils.hasText(view.getStyleSheet())) {
			throw new ValidationException(messageSource.getMessage(
					"errors.validation.styleSheet.empty", 
					null, user.getLocale()));
		}
		validateStyleSheet(view.getStyleSheet(), user);
		dao.saveView(view);
	}

	/**
	 * Validates the given styleSheet, using an XML Validator
	 * @param styleSheet	The stylesheet to be validated
	 * @param user			The current user
	 * @see javax.xml.parsers.DocumentBuilder
	 */
	private void validateStyleSheet(String styleSheet, User user) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    try {        
	         DocumentBuilder parser = factory.newDocumentBuilder();
	         parser.parse(new ByteArrayInputStream(styleSheet.getBytes()));
	    } catch (Exception e){
			throw new ValidationException(messageSource.getMessage(
					"errors.validation.styleSheet.invalid", 
					new Object[] { e }, user.getLocale()));
	    }
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteAction(int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteAction(int id, User user) {
		Action action = getAction(id, user);
		checkCommunityAdministrationRight(user, action.getCommunityId());
		dao.deleteAction(action);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getAction(int, ch.arpage.collaboweb.model.User)
	 */
	public Action getAction(int id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getAction(id);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getActions(ch.arpage.collaboweb.model.User)
	 */
	public List<Action> getActions(User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getActions(user.getCommunityId());
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveAction(ch.arpage.collaboweb.model.Action, ch.arpage.collaboweb.model.User)
	 */
	public void saveAction(Action action, User user) {
		securityService.checkAuthenticatedUser(user);
		if (!StringUtils.hasText(action.getClassName())) {
			throw new ValidationException(messageSource.getMessage(
					"errors.action.className.empty", 
					null, user.getLocale()));
		}
		try {
			beanFactory.getBean(action.getClassName(), 
					ch.arpage.collaboweb.services.actions.Action.class);
		} catch (Exception e) {
			throw new ValidationException(messageSource.getMessage(
					"errors.action.className.invalid", 
					new Object[] { action.getClassName() }, user.getLocale()));
		}
		if (action.getActionId() == 0) {
			action.setCommunityId(user.getCommunityId());
			checkCommunityAdministrationRight(user, action.getCommunityId());
			dao.insertAction(action);
		} else {
			checkCommunityAdministrationRight(user, action.getCommunityId());
			dao.updateAction(action);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteRelationshipType(int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteRelationshipType(int id, User user) {
		RelationshipType relationshipType = getRelationshipType(id, user);
		checkCommunityAdministrationRight(user, 
				relationshipType.getCommunityId());
		dao.deleteRelationshipType(relationshipType);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getRelationshipType(int, ch.arpage.collaboweb.model.User)
	 */
	public RelationshipType getRelationshipType(int id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getRelationshipType(id);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getRelationshipTypes(ch.arpage.collaboweb.model.User)
	 */
	public List<RelationshipType> getRelationshipTypes(User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getRelationshipTypes(user.getCommunityId());
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveRelationshipType(ch.arpage.collaboweb.model.RelationshipType, ch.arpage.collaboweb.model.User)
	 */
	public void saveRelationshipType(RelationshipType relationshipType, User user) {
		securityService.checkAuthenticatedUser(user);
		if (relationshipType.getRelationshipTypeId() == 0) {
			relationshipType.setCommunityId(user.getCommunityId());
			checkCommunityAdministrationRight(user, 
					relationshipType.getCommunityId());
			dao.insertRelationshipType(relationshipType);
		} else {
			checkCommunityAdministrationRight(user, 
					relationshipType.getCommunityId());
			dao.updateRelationshipType(relationshipType);
		}
	}
	
	/**
	 * Checks that the given user is the administrator of the community identified
	 * by the given ID
	 * @param user			The current user
	 * @param communityId	The community's ID
	 */
	private void checkCommunityAdministrationRight(User user, long communityId) {
		if (user.getCommunityId() != communityId || 
				!user.isCommunityAdministrator()) {
			throw NotAuthorizedException.getInstance();
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteResourceValidation(int, int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteResourceValidation(int typeId, int validationTypeId, User user) {
		ResourceValidation validation = getResourceValidation(typeId, validationTypeId, user);
		checkCommunityAdministrationRight(user, 
				validation.getValidationType().getCommunityId());
		dao.deleteResourceValidation(validation);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#deleteResourceValidationType(int, ch.arpage.collaboweb.model.User)
	 */
	public void deleteResourceValidationType(int id, User user) {
		ResourceValidationType type = getResourceValidationType(id, user);
		checkCommunityAdministrationRight(user, type.getCommunityId());
		dao.deleteResourceValidationType(type);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getResourceValidation(int, int, ch.arpage.collaboweb.model.User)
	 */
	public ResourceValidation getResourceValidation(int typeId, int validationTypeId, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getResourceValidation(typeId, validationTypeId);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getResourceValidationType(int, ch.arpage.collaboweb.model.User)
	 */
	public ResourceValidationType getResourceValidationType(int id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getResourceValidationType(id);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#getResourceValidationTypes(ch.arpage.collaboweb.model.User)
	 */
	public List<ResourceValidationType> getResourceValidationTypes(User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getResourceValidationTypes(user.getCommunityId());
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveResourceValidation(ch.arpage.collaboweb.model.ResourceValidation, ch.arpage.collaboweb.model.User)
	 */
	public void saveResourceValidation(ResourceValidation validation, User user) {
		securityService.checkAuthenticatedUser(user);
		ResourceValidationType vt = validation.getValidationType();
		if (vt == null) {
			vt = getResourceValidationType(validation.getValidationTypeId(), user);
			validation.setValidationType(vt);
		}
		checkCommunityAdministrationRight(user, vt.getCommunityId());
		dao.saveResourceValidation(validation);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceTypeManager#saveResourceValidationType(ch.arpage.collaboweb.model.ResourceValidationType, ch.arpage.collaboweb.model.User)
	 */
	public void saveResourceValidationType(ResourceValidationType type, User user) {
		securityService.checkAuthenticatedUser(user);
		try {
			beanFactory.getBean(type.getClassName(), ResourceValidator.class);
		} catch (Exception e) {
			throw new ValidationException(messageSource.getMessage(
					"errors.resourceValidator.className.invalid", 
					new Object[] { type.getClassName() }, user.getLocale()));
		}
		if (type.getValidationTypeId() == 0) {
			type.setCommunityId(user.getCommunityId());
			checkCommunityAdministrationRight(user, type.getCommunityId());
			dao.insertResourceValidationType(type);
		} else {
			checkCommunityAdministrationRight(user, type.getCommunityId());
			dao.updateResourceValidationType(type);
		}
	}
}
