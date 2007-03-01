/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.Action;


/**
 * ActionDao
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ActionDao extends HibernateDaoSupport {
	
	public List getList() {
		return getHibernateTemplate().find("from Action");
	}
	
	public Action getById(int id) {
		return (Action) getHibernateTemplate().load(Action.class, new Integer(id));
	}
	
	public Action create(Action bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void delete(Action bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void update(Action bean) {
		getHibernateTemplate().update(bean);
	}
}
