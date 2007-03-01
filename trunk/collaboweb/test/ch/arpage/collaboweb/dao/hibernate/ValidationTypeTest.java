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

import ch.arpage.collaboweb.model.ValidationType;


/**
 * ViewTypeTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationTypeTest extends TestCase {
	
	ValidationTypeDao dao;
	static int id;
	
	public ValidationTypeTest() {
		try {
			BeanFactory factory = getBeanFactory();
			dao = (ValidationTypeDao) factory.getBean("validationTypeDao");
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
			print((ValidationType) o);
		}
	}
	
	public void testCreate() {
		ValidationType bean = new ValidationType();
		bean.setClassName("test");
		bean.setCommunityId(1);
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
			ValidationType bean = dao.getById(id);
			bean.setClassName("updated class");
			bean.setLabel("de", "updated label");
			bean.setMessage("fr", "updated message");
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
	
	private static void print(ValidationType a) {
		System.out.println("getValidationTypeId: " + a.getValidationTypeId());
		System.out.println("getCommunityId: " + a.getCommunityId());
		System.out.println("getClassName: " + a.getClassName());
		System.out.println("DE: " + a.getLabel("de"));
		System.out.println("EN: " + a.getLabel("en"));
		System.out.println("FR: " + a.getLabel("fr"));
		System.out.println("IT: " + a.getLabel("it"));
		System.out.println("DE: " + a.getMessage("de"));
		System.out.println("EN: " + a.getMessage("en"));
		System.out.println("FR: " + a.getMessage("fr"));
		System.out.println("IT: " + a.getMessage("it"));
	}
}
