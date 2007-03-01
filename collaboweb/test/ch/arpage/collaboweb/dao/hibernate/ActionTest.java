/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import ch.arpage.collaboweb.model.Action;


/**
 * ViewTypeTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ActionTest extends TestCase {
	
	ActionDao dao;
	static int id;
	
	public ActionTest() {
		try {
			BeanFactory factory = getBeanFactory();
			dao = (ActionDao) factory.getBean("actionDao");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static BeanFactory getBeanFactory() throws Exception {
		XmlBeanFactory factory = new XmlBeanFactory(
				new FileSystemResource("build/classes/applicationContext-hibernate.xml"));
		return factory;
	}
	
	public void testList() {
		List list = dao.getList();
		for (Object o : list) {
			print((Action) o);
		}
	}
	
	public void testCreate() {
		Action bean = new Action();
		//action.setActionId(7);
		bean.setClassName("ch.arpage.collaboweb.Test");
		bean.setCommunityId(1);
		bean.setLabel("de", "Tedesco");
		bean.setLabel("en", "Inglese");
		bean.setLabel("fr", "Francese");
		bean.setLabel("it", "Italiano");
		dao.create(bean);
		print(bean);
		id = bean.getActionId();
	}
	
	public void testUpdate() {
		if (id != 0) {
			Action bean = dao.getById(id);
			bean.setClassName("updated");
			bean.setLabel("de", "updated label");
			dao.update(bean);
			print(bean);
		} else {
			Assert.fail(id + " is null");
		}
	}
	
	public void testDelete() {
		if (id != 0) {
			dao.delete(dao.getById(id));
		} else {
			Assert.fail(id + " is null");
		}
	}
	
	private static void print(Action a) {
		System.out.println("Action ID: " + a.getActionId());
		System.out.println("ClassName: " + a.getClassName());
		System.out.println("CommunityId: " + a.getCommunityId());
		System.out.println("DE: " + a.getLabel("de"));
		System.out.println("EN: " + a.getLabel("en"));
		System.out.println("FR: " + a.getLabel("fr"));
		System.out.println("IT: " + a.getLabel("it"));
	}
}
