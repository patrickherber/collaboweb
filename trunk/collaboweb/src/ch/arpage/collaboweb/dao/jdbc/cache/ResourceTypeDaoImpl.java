/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.dao.jdbc.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.ResourceValidation;
import ch.arpage.collaboweb.model.Validation;
import ch.arpage.collaboweb.model.View;

/**
 * Resource Type Data Access Object that provides caching functionalities
 * on each definition objects
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeDaoImpl 
	extends ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl {

	private final Map<Integer, ResourceType> resourceTypes = 
		new TreeMap<Integer, ResourceType>();
	
	private final Set<Long> loadedCommunities = new HashSet<Long>();
	
	/* (non-Javadoc)
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	@Override
	protected void initDao() throws Exception {
		super.initDao();
		loadCommunity(0L);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#delete(ch.arpage.collaboweb.model.ResourceType)
	 */
	@Override
	public void delete(ResourceType bean) {
		super.delete(bean);
		resourceTypes.remove(bean.getTypeId());
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#get(int)
	 */
	@Override
	public ResourceType get(int id) {
		ResourceType resourceType = resourceTypes.get(id);
		if (resourceType == null) {
			resourceType = super.get(id);
			resourceTypes.put(resourceType.getTypeId(), resourceType);
		}
		return resourceType;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#getList(long)
	 */
	@Override
	public List<ResourceType> getList(long communityId) {
		if (!loadedCommunities.contains(communityId)) {
			return loadCommunity(communityId);
		} else {
			List<ResourceType> list = new LinkedList<ResourceType>();
			for (ResourceType type : resourceTypes.values()) {
				if (type.getCommunityId() == 0 || 
						type.getCommunityId() == communityId) {
					list.add(type);
				}
			}
			return list;
		}
	}

	/**
	 * Load the resource types for the given community.
	 * @param communityId	The community's ID
	 * @return				The list of resource types for the given community.
	 */
	private List<ResourceType> loadCommunity(long communityId) {
		List<ResourceType> list = super.getList(communityId);
		for (ResourceType type : list) {
			resourceTypes.put(type.getTypeId(), type);
		}
		loadedCommunities.add(communityId);
		return list;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#insert(ch.arpage.collaboweb.model.ResourceType)
	 */
	@Override
	public void insert(ResourceType bean) {
		super.insert(bean);
		resourceTypes.put(bean.getTypeId(), bean);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#update(ch.arpage.collaboweb.model.ResourceType)
	 */
	@Override
	public void update(ResourceType bean) {
		super.update(bean);
		resourceTypes.put(bean.getTypeId(), bean);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#setSupportedChildren(int, Integer[])
	 */
	@Override
	public void setSupportedChildren(int parentId, Integer[] childrenIds) {
		super.setSupportedChildren(parentId, childrenIds);
		ResourceType type = get(parentId);
		List<Integer> children = type.getSupportedChildrenIds();
		if (children == null) {
			children = new LinkedList<Integer>();
			type.setSupportedChildrenIds(children);
		} else {
			children.clear();
		}
		if (childrenIds != null) {
			Collections.addAll(children, childrenIds);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#deleteAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	@Override
	public void deleteAttribute(Attribute attribute) {
		super.deleteAttribute(attribute);
		ResourceType resourceType = resourceTypes.get(attribute.getTypeId());
		if (resourceType != null) {
			removeAttribute(attribute.getAttributeId(), resourceType);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#deleteValidation(ch.arpage.collaboweb.model.Validation)
	 */
	@Override
	public void deleteValidation(Validation validation) {
		super.deleteValidation(validation);
		Attribute attribute = getAttribute(validation.getAttributeId());
		if (attribute != null) {
			ResourceType resourceType = get(attribute.getTypeId());
			attribute = 
				resourceType.getAttribute(validation.getAttributeId());
			attribute.removeValidation(validation.getValidationTypeId());
		}
	}

	/**
	 * @param attributeId
	 * @param typeId
	 */
	private int removeAttribute(int attributeId, ResourceType resourceType) {
		List<Attribute> list = resourceType.getAttributes();
		if (list != null) {
			int index = 0;
			for (Attribute attribute : list) {
				if (attribute.getAttributeId() == attributeId) {
					list.remove(attribute);
					return index;
				}
				++index;
			}
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#insertAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	@Override
	public void insertAttribute(Attribute attribute) {
		super.insertAttribute(attribute);
		saveCachedAttribute(attribute);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#updateAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	@Override
	public void updateAttribute(Attribute attribute) {
		super.updateAttribute(attribute);
		saveCachedAttribute(attribute);
	}

	/**
	 * Saves the attribute in cache
	 * @param attribute	The attribute to be saved in cache.
	 */
	private void saveCachedAttribute(Attribute attribute) {
		ResourceType resourceType = resourceTypes.get(attribute.getTypeId());
		if (resourceType != null) {
			removeAttribute(attribute.getAttributeId(), resourceType);
			List<Attribute> attributes = resourceType.getAttributes();
			if (attributes == null) {
				attributes = new LinkedList<Attribute>();
				resourceType.setAttributes(attributes);
			} 
			attributes.add(attribute);
			Collections.sort(attributes);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#saveValidation(ch.arpage.collaboweb.model.Validation)
	 */
	@Override
	public void saveValidation(final Validation validation) {
		super.saveValidation(validation);
		Attribute attribute = getAttribute(validation.getAttributeId());
		if (attribute != null) {
			ResourceType resourceType = get(attribute.getTypeId());
			attribute = resourceType.getAttribute(validation.getAttributeId());
			attribute.addValidation(validation);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#saveView(ch.arpage.collaboweb.model.View)
	 */
	@Override
	public void saveView(final View view) {
		super.saveView(view);
		ResourceType resourceType = get(view.getTypeId());
		resourceType.addView(view);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#deleteView(ch.arpage.collaboweb.model.View)
	 */
	@Override
	public void deleteView(final View view) {
		super.deleteView(view);
		ResourceType resourceType = get(view.getTypeId());
		resourceType.removeView(view);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#deleteResourceValidation(ch.arpage.collaboweb.model.ResourceValidation)
	 */
	@Override
	public void deleteResourceValidation(ResourceValidation resourceValidation) {
		super.deleteResourceValidation(resourceValidation);
		ResourceType resourceType = get(resourceValidation.getTypeId());
		resourceType.removeResourceValidation(resourceValidation);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.jdbc.ResourceTypeDaoImpl#saveResourceValidation(ch.arpage.collaboweb.model.ResourceValidation)
	 */
	@Override
	public void saveResourceValidation(ResourceValidation resourceValidation) {
		super.saveResourceValidation(resourceValidation);
		ResourceType resourceType = get(resourceValidation.getTypeId());
		resourceType.addResourceValidation(resourceValidation);
	}
}
