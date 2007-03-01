/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.Attribute;


/**
 * AttributeDao
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class AttributeDao extends HibernateDaoSupport {
	
	public List getList() {
		return getHibernateTemplate().find("from Attribute");
	}
	
	public Attribute getById(int id) {
		return (Attribute) getHibernateTemplate().load(Attribute.class, new Integer(id));
	}
	
	public Attribute create(Attribute bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void delete(Attribute bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void update(Attribute bean) {
		getHibernateTemplate().update(bean);
	}
}
