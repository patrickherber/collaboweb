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

import ch.arpage.collaboweb.model.View;


/**
 * ViewTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewTest extends TestCase {
	
	ViewDao dao;
	static int typeId;
	static int viewTypeId;
	
	public ViewTest() {
		try {
			BeanFactory factory = getBeanFactory();
			dao = (ViewDao) factory.getBean("viewDao");
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
			print((View) o);
		}
	}
	
	private static void print(View bean) {
		System.out.println("getViewType: " + bean.getViewType());
		System.out.println("getTypeId: " + bean.getTypeId());
		System.out.println("getActions: " + bean.getActions());
	}
}
