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

import ch.arpage.collaboweb.model.ViewType;


/**
 * ViewTypeTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewTypeTest extends TestCase {
	
	ViewTypeDao dao;
	static int id;
	
	public ViewTypeTest() {
		try {
			BeanFactory factory = getBeanFactory();
			dao = (ViewTypeDao) factory.getBean("viewTypeDao");
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
			print((ViewType) o);
		}
	}
	
	public void testCreate() {
		ViewType bean = new ViewType();
		bean.setCommunityId(1);
		bean.setContentType("text/html");
		bean.setLabel("de", "Test de");
		bean.setLabel("en", "Test en");
		bean.setLabel("fr", "Test fr");
		bean.setLabel("it", "Test it");
		dao.create(bean);
		print(bean);
		id = bean.getViewTypeId();
	}
	
	public void testUpdate() {
		if (id != 0) {
			ViewType bean = dao.getById(id);
			bean.setCommunityId(2);
			bean.setContentType("application/pdf");
			bean.setLabel("en", "Updated");
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
	
	private static void print(ViewType bean) {
		System.out.println("Action ID: " + bean.getViewTypeId());
		System.out.println("ClassName: " + bean.getContentType());
		System.out.println("CommunityId: " + bean.getCommunityId());
		System.out.println("DE: " + bean.getLabel("de"));
		System.out.println("EN: " + bean.getLabel("en"));
		System.out.println("FR: " + bean.getLabel("fr"));
		System.out.println("IT: " + bean.getLabel("it"));
	}
}
