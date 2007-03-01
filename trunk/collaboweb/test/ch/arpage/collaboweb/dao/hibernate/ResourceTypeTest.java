/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import ch.arpage.collaboweb.model.ResourceType;


/**
 * ResourceTypeTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeTest extends TestCase {
	
	ResourceTypeDao dao;
	static int typeId;
	static int viewTypeId;
	
	public ResourceTypeTest() {
		try {
			BeanFactory factory = getBeanFactory();
			dao = (ResourceTypeDao) factory.getBean("resourceTypeDao");
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
			print((ResourceType) o);
		}
	}
	
	private static void print(ResourceType bean) {
		System.out.println("getTypeId: " + bean.getTypeId());
		System.out.println("getCommunityId: " + bean.getCommunityId());
		System.out.println("getFamilyId: " + bean.getFamilyId());
		System.out.println("getAttributes: " + bean.getAttributes());
		System.out.println("getViews: " + bean.getViews());
		System.out.println("getResourceValidations: " + bean.getResourceValidations());
		System.out.println("DE: " + bean.getLabel("de"));
		System.out.println("EN: " + bean.getLabel("en"));
		System.out.println("FR: " + bean.getLabel("fr"));
		System.out.println("IT: " + bean.getLabel("it"));
	}
}
