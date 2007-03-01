/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.View;
import ch.arpage.collaboweb.model.ViewType;


/**
 * ViewTypeDao
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewDao extends HibernateDaoSupport {
	
	public List getList() {
		return getHibernateTemplate().find("from View");
	}
	
	public View getById(int typeId, int viewTypeId) {
		View view = new View();
		view.setTypeId(typeId);
		view.setViewType((ViewType) 
				getSession().get(ViewType.class, viewTypeId)); 
		return (View) getSession().get(View.class, view);
	}
	
	public View create(View bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void delete(View bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void update(View bean) {
		getHibernateTemplate().update(bean);
	}
}
