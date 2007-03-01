/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.ViewType;


/**
 * ViewTypeDao
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewTypeDao extends HibernateDaoSupport {
	
	public List getList() {
		return getHibernateTemplate().find("from ViewType");
	}
	
	public ViewType getById(int id) {
		return (ViewType) getHibernateTemplate().load(ViewType.class, new Integer(id));
	}
	
	public ViewType create(ViewType bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void delete(ViewType bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void update(ViewType bean) {
		getHibernateTemplate().update(bean);
	}
}
