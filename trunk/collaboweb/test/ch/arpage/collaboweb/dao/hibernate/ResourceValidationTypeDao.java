/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.ResourceValidationType;


/**
 * ResourceValidationTypeDao
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidationTypeDao extends HibernateDaoSupport {
	
	public List getList() {
		return getHibernateTemplate().find("from ResourceValidationType");
	}
	
	public ResourceValidationType getById(int id) {
		return (ResourceValidationType) getHibernateTemplate().load(
				ResourceValidationType.class, new Integer(id));
	}
	
	public ResourceValidationType create(ResourceValidationType bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void delete(ResourceValidationType bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void update(ResourceValidationType bean) {
		getHibernateTemplate().update(bean);
	}
}
