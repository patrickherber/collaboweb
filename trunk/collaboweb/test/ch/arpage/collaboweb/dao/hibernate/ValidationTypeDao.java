/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.ValidationType;


/**
 * ValidationTypeDao
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationTypeDao extends HibernateDaoSupport {
	
	public List getList() {
		return getHibernateTemplate().find("from ValidationType");
	}
	
	public ValidationType getById(int id) {
		return (ValidationType) getHibernateTemplate().load(ValidationType.class, new Integer(id));
	}
	
	public ValidationType create(ValidationType bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void delete(ValidationType bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void update(ValidationType bean) {
		getHibernateTemplate().update(bean);
	}
}
