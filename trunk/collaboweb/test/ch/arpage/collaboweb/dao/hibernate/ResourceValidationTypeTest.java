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

import ch.arpage.collaboweb.model.ResourceValidationType;


/**
 * ViewTypeTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidationTypeTest extends TestCase {
	
	ResourceValidationTypeDao dao;
	static int id;
	
	public ResourceValidationTypeTest() {
		try {
			BeanFactory factory = getBeanFactory();
			dao = (ResourceValidationTypeDao) 
				factory.getBean("resourceValidationTypeDao");
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
			print((ResourceValidationType) o);
		}
	}
	
	public void testCreate() {
		ResourceValidationType bean = new ResourceValidationType();
		bean.setCommunityId(1);
		bean.setClassName("Test class");
		bean.setLabel("de", "Test de");
		bean.setLabel("en", "Test en");
		bean.setLabel("fr", "Test fr");
		bean.setLabel("it", "Test it");
		bean.setMessage("de", "Test Message de");
		bean.setMessage("en", "Test Message en");
		bean.setMessage("fr", "Test Message fr");
		bean.setMessage("it", "Test Message it");
		dao.create(bean);
		print(bean);
		id = bean.getValidationTypeId();
	}
	
	public void testUpdate() {
		if (id != 0) {
			ResourceValidationType bean = dao.getById(id);
			bean.setCommunityId(2);
			bean.setClassName("Updated class");
			bean.setLabel("en", "Updated");
			bean.setMessage("en", "Message Updated");
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
	
	private static void print(ResourceValidationType bean) {
		System.out.println("getValidationTypeId: " + bean.getValidationTypeId());
		System.out.println("getClassName: " + bean.getClassName());
		System.out.println("getCommunityId: " + bean.getCommunityId());
		System.out.println("DE: " + bean.getLabel("de"));
		System.out.println("EN: " + bean.getLabel("en"));
		System.out.println("FR: " + bean.getLabel("fr"));
		System.out.println("IT: " + bean.getLabel("it"));
		System.out.println("DE: " + bean.getMessage("de"));
		System.out.println("EN: " + bean.getMessage("en"));
		System.out.println("FR: " + bean.getMessage("fr"));
		System.out.println("IT: " + bean.getMessage("it"));
	}
}
