/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.dao.ResourceDao;
import ch.arpage.collaboweb.dao.ResourceTypeDao;
import ch.arpage.collaboweb.exceptions.NotAuthorizedException;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.BinaryAttribute;
import ch.arpage.collaboweb.model.Model;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceAttribute;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.TagStatistic;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.IndexerManager;
import ch.arpage.collaboweb.services.KeyGenerator;
import ch.arpage.collaboweb.services.ResourceManager;
import ch.arpage.collaboweb.services.SecurityService;

/**
 * Implementation of the Resource Manager service
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceManagerImpl implements ResourceManager {
	
	private ResourceDao dao;
	private ResourceTypeDao resourceTypeDao;
	private KeyGenerator keyGenerator;
	private IndexerManager indexerManager;
	private SecurityService securityService;
	
	/**
	 * Set the dao.
	 * @param dao the dao to set
	 */
	public void setDao(ResourceDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Set the resourceTypeDao.
	 * @param resourceTypeDao the resourceTypeDao to set
	 */
	public void setResourceTypeDao(ResourceTypeDao resourceTypeDao) {
		this.resourceTypeDao = resourceTypeDao;
	}
	
	/**
	 * Set the keyGenerator.
	 * @param keyGenerator the keyGenerator to set
	 */
	public void setKeyGenerator(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
	/**
	 * Set the indexerManager.
	 * @param indexerManager the indexerManager to set
	 */
	public void setIndexerManager(IndexerManager indexerManager) {
		this.indexerManager = indexerManager;
	}
	
	/**
	 * Set the securityService.
	 * @param securityService the securityService to set
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#create(int)
	 */
	public Resource create(long parentId, int resourceTypeId, User user) {
		securityService.checkAuthenticatedUser(user);
		Resource resource = new Resource();
		resource.setResourceType(resourceTypeDao.get(resourceTypeId));
		resource.setAuthorId(user.getResourceId());
		resource.setCommunityId(user.getCommunityId());
		resource.setParentId(parentId);
		return resource;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#delete(long, ch.arpage.collaboweb.model.User)
	 */
	public void delete(long id, User user) {
		securityService.checkAuthenticatedUser(user);
		Resource resource = get(id, user);
		if (resource.isAllowed(Model.RIGHT_ADMIN)) {
			dao.delete(resource);
			indexerManager.deleteFromIndex(resource.getResourceId(), user);
		} else {
			throw NotAuthorizedException.getInstance();
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#get(long)
	 */
	public Resource get(long id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.get(id, user);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#getList(long)
	 */
	public List<Resource> getList(long id, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getList(id, user);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#save(ch.arpage.collaboweb.model.Resource)
	 */
	public void save(Resource resource, User user) throws Exception {
		securityService.checkAuthenticatedUser(user);
		ResourceType resourceType = resourceTypeDao.get(resource.getTypeId());
		resource.setResourceType(resourceType);
		resourceType.validate(resource, user);
		resource.computeName();
		checkDefaultAttributeValues(resource, user);
		if (resource.getResourceId() == 0L) {
			Resource parent = get(resource.getParentId(), user);
			if (parent.isAllowed(Model.RIGHT_CONTRIBUTE)) {
				resource.setAuthorId(user.getResourceId());
				resource.setAuthorName(user.getFullName());
				resource.setCommunityId(user.getCommunityId());
				resource.setResourceId(keyGenerator.getNextId());
				dao.insert(resource);
			} else {
				throw NotAuthorizedException.getInstance();
			}
		} else {
			Resource oldVersion = get(resource.getResourceId(), user);
			if (oldVersion.isAllowed(Model.RIGHT_MODIFY)) {
				dao.update(resource);
			} else {
				throw NotAuthorizedException.getInstance();
			}
		}
		indexerManager.index(resource, user);
	}
	
	/**
	 * Sets the attribute without values to their default value.
	 * @param resource	The resource
	 * @param user		The current user
	 */
	private void checkDefaultAttributeValues(Resource resource, User user) {
		for (Attribute attribute : resource.getResourceType().getAttributes()) {
			if (StringUtils.hasText(attribute.getDefaultValue())) {
				ResourceAttribute ra =
					resource.getResourceAttribute(attribute.getAttributeId());
				if (ra != null) {
					setDefaultValue(ra, attribute);
				} else {
					ra = new ResourceAttribute();
					ra.setAttributeId(attribute.getAttributeId());
					resource.addResourceAttribute(ra);
					setDefaultValue(ra, attribute);
				}
			}
		}
	}

	/**
	 * Sets the default value for the given attribute
	 * @param ra		The attribute value
	 * @param attribute	The attribute
	 */
	private void setDefaultValue(ResourceAttribute ra, Attribute attribute) {
		String defaultValue = attribute.getDefaultValue();
		switch (attribute.getDataType()) {
		case Model.DATA_TYPE_DATE:
			if ("INSERT_DATE".equalsIgnoreCase(defaultValue)) {
				if (ra.getValue() == null) {
					ra.setValue(new Date().toString());
				}
			} else if ("NOW".equalsIgnoreCase(defaultValue)) {
				ra.setValue(new Date().toString());
			}
			break;
		default:
			if (ra.getValue() == null) {
				ra.setValue(defaultValue);
			}
			break;
		}
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#getTagCloud(java.lang.String[], long)
	 */
	public Set<TagStatistic> getTagCloud(String[] tags, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getTagCloud(tags, user);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#getTagList(java.lang.String[], long)
	 */
	public List<Resource> getTagList(String[] tags, User user) {
		securityService.checkAuthenticatedUser(user);
		return dao.getTagList(tags, user);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#readAttribute(long, String, ch.arpage.collaboweb.model.User)
	 */
	public BinaryAttribute readAttribute(long resourceId, String attributeIdentifier, User user) {
		securityService.checkAuthenticatedUser(user);
		checkPermission(resourceId, user);
		return dao.readAttribute(resourceId, attributeIdentifier);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#addTag(long, java.lang.String, ch.arpage.collaboweb.model.User)
	 */
	public void addTag(long resourceId, String tag, User user) {
		securityService.checkAuthenticatedUser(user);
		checkPermission(resourceId, user);
		dao.addTag(resourceId, tag, user.getResourceId());		
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#deleteTag(long, java.lang.String, ch.arpage.collaboweb.model.User)
	 */
	public void deleteTag(long resourceId, String tag, User user) {
		securityService.checkAuthenticatedUser(user);
		checkPermission(resourceId, user);
		dao.deleteTag(resourceId, tag, user.getResourceId());		
	}
	
	private void checkPermission(long resourceId, User user) {
		dao.get(resourceId, user);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#addRelationships(long, int, java.util.Set, ch.arpage.collaboweb.model.User)
	 */
	public void addRelationships(long id, int relationshipType, Set<Long> references, User user) {
		securityService.checkAuthenticatedUser(user);
		Resource toResource = get(id, user);
		if (toResource.isAllowed(Model.RIGHT_CONTRIBUTE)) {
			dao.addRelationships(id, relationshipType, references);
		} else {
			throw NotAuthorizedException.getInstance();
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#deleteRelationship(long, int, long, ch.arpage.collaboweb.model.User)
	 */
	public void deleteRelationship(long id, int relationshipType, long referencedId, User user) {
		securityService.checkAuthenticatedUser(user);
		Resource toResource = get(id, user);
		if (toResource.isAllowed(Model.RIGHT_CONTRIBUTE)) {
			dao.deleteRelationship(id, relationshipType, referencedId);
		} else {
			throw NotAuthorizedException.getInstance();
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ResourceManager#move(java.util.Set, long, ch.arpage.collaboweb.model.User)
	 */
	public void move(Set<Long> ids, long to, User user) {
		securityService.checkAuthenticatedUser(user);
		Resource toResource = get(to, user);
		List supportedChilds = 
			toResource.getResourceType().getSupportedChildrenIds();
		if (toResource.isAllowed(Model.RIGHT_CONTRIBUTE)) {
			if (supportedChilds != null) {
				for (long id : ids) {
					if (id != to) {
						Resource fromResource = get(id, user);
						if (fromResource.isAllowed(Model.RIGHT_ADMIN)) {
							if (supportedChilds.contains(fromResource.getTypeId())) {
								dao.move(id, to);
							}
						}
					}
				}
			}
		} else {
			throw NotAuthorizedException.getInstance();
		}
	}
}
