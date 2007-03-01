/**
 * collaboweb7
 * Feb 13, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.dao.ResourceTypeDao;
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
 * ResourceTypeDaoHibernate
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeDaoImpl extends HibernateDaoSupport 
	implements ResourceTypeDao {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#delete(ch.arpage.collaboweb.model.ResourceType)
	 */
	public void delete(ResourceType bean) {
		getSession().delete(bean);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	public void deleteAttribute(Attribute attribute) {
		getSession().delete(attribute);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteValidation(ch.arpage.collaboweb.model.Validation)
	 */
	public void deleteValidation(Validation validation) {
		getSession().delete(validation);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteValidationType(ch.arpage.collaboweb.model.ValidationType)
	 */
	public void deleteValidationType(ValidationType validationType) {
		getSession().delete(validationType);
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#get(int)
	 */
	public ResourceType get(int id) {
		return (ResourceType) getSession().get(ResourceType.class, id);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getAttribute(int)
	 */
	public Attribute getAttribute(int id) {
		return (Attribute) getSession().get(Attribute.class, id);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getList(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceType> getList(long communityId) {
		Query query = getSession().createQuery("from ResourceType where communityId = ? or communityId = 0")
			.setLong(0, communityId);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getValidation(int, int)
	 */
	public Validation getValidation(int attributeId, int validationTypeId) {
		Attribute attribute = (Attribute) getSession().get(Attribute.class, attributeId);
		if (attribute != null) {
			for (Validation v : attribute.getValidations()) {
				if (v.getValidationTypeId() == validationTypeId) {
					return v;
				}
			}
			throw new DataRetrievalFailureException(validationTypeId + " not found");
		} else {
			throw new DataRetrievalFailureException(attributeId + " not found");
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getValidationType(int)
	 */
	public ValidationType getValidationType(int id) {
		return (ValidationType) getHibernateTemplate().load(ValidationType.class, id);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getValidationTypes(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ValidationType> getValidationTypes(long communityId) {
		Query query = getSession().createQuery("from ValidationType where communityId = ? or communityId = 0")
			.setLong(0, communityId);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insert(ch.arpage.collaboweb.model.ResourceType)
	 */
	public void insert(ResourceType bean) {
		getHibernateTemplate().save(bean);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	public void insertAttribute(Attribute attribute) {
		getHibernateTemplate().save(attribute);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertValidationType(ch.arpage.collaboweb.model.ValidationType)
	 */
	public void insertValidationType(ValidationType validationType) {
		getHibernateTemplate().save(validationType);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#saveValidation(ch.arpage.collaboweb.model.Validation)
	 */
	public void saveValidation(Validation validation) {
		getHibernateTemplate().save(validation);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#setSupportedChildren(Integer, int[])
	 */
	public void setSupportedChildren(int parentId, Integer[] childrenIds) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#update(ch.arpage.collaboweb.model.ResourceType)
	 */
	public void update(ResourceType bean) {
		getHibernateTemplate().update(bean);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	public void updateAttribute(Attribute attribute) {
		getHibernateTemplate().update(attribute);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateValidationType(ch.arpage.collaboweb.model.ValidationType)
	 */
	public void updateValidationType(ValidationType validationType) {
		getHibernateTemplate().update(validationType);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteViewType(ch.arpage.collaboweb.model.ViewType)
	 */
	public void deleteViewType(ViewType viewType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getViewType(int)
	 */
	public ViewType getViewType(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getViewTypes(long)
	 */
	public List<ViewType> getViewTypes(long communityId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertViewType(ch.arpage.collaboweb.model.ViewType)
	 */
	public void insertViewType(ViewType viewType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateViewType(ch.arpage.collaboweb.model.ViewType)
	 */
	public void updateViewType(ViewType viewType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteView(ch.arpage.collaboweb.model.View)
	 */
	public void deleteView(View view) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getView(int, int)
	 */
	public View getView(int typeId, int viewTypeId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#saveView(ch.arpage.collaboweb.model.View)
	 */
	public void saveView(View View) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteAction(ch.arpage.collaboweb.model.Action)
	 */
	public void deleteAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getAction(int)
	 */
	public Action getAction(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getActions(long)
	 */
	public List<Action> getActions(long communityId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertAction(ch.arpage.collaboweb.model.Action)
	 */
	public void insertAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateAction(ch.arpage.collaboweb.model.Action)
	 */
	public void updateAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteRelationshipType(ch.arpage.collaboweb.model.RelationshipType)
	 */
	public void deleteRelationshipType(RelationshipType relationshipType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getRelationshipType(int)
	 */
	public RelationshipType getRelationshipType(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getRelationshipTypes(long)
	 */
	public List<RelationshipType> getRelationshipTypes(long communityId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertRelationshipType(ch.arpage.collaboweb.model.RelationshipType)
	 */
	public void insertRelationshipType(RelationshipType relationshipType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateRelationshipType(ch.arpage.collaboweb.model.RelationshipType)
	 */
	public void updateRelationshipType(RelationshipType relationshipType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteResourceValidation(ch.arpage.collaboweb.model.ResourceValidation)
	 */
	public void deleteResourceValidation(ResourceValidation resourceValidation) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteResourceValidationType(ch.arpage.collaboweb.model.ResourceValidationType)
	 */
	public void deleteResourceValidationType(ResourceValidationType resourceValidationType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getResourceValidation(int, int)
	 */
	public ResourceValidation getResourceValidation(int typeId, int validationTypeId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getResourceValidationType(int)
	 */
	public ResourceValidationType getResourceValidationType(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getResourceValidationTypes(long)
	 */
	public List<ResourceValidationType> getResourceValidationTypes(long communityId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertResourceValidationType(ch.arpage.collaboweb.model.ResourceValidationType)
	 */
	public void insertResourceValidationType(ResourceValidationType resourceValidationType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#saveResourceValidation(ch.arpage.collaboweb.model.ResourceValidation)
	 */
	public void saveResourceValidation(ResourceValidation resourceValidation) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateResourceValidationType(ch.arpage.collaboweb.model.ResourceValidationType)
	 */
	public void updateResourceValidationType(ResourceValidationType resourceValidationType) {
		// TODO Auto-generated method stub
		
	}

}
