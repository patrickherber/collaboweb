/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.ResourceType;


/**
 * ResourceTypeDao
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeDao extends HibernateDaoSupport {
	
	public List getList() {
		return getHibernateTemplate().find("from ResourceType");
	}
	
	public ResourceType getById(int typeId) {
		return (ResourceType) getSession().get(ResourceType.class, typeId);
	}
	
	public ResourceType create(ResourceType bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void delete(ResourceType bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void update(ResourceType bean) {
		getHibernateTemplate().update(bean);
	}
}
