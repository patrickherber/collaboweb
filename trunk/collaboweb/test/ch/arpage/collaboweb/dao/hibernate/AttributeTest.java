/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.Validation;


/**
 * ViewTypeTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class AttributeTest extends TestCase {
	
	AttributeDao dao;
	ValidationTypeDao vDao;
	static int id;
	
	public AttributeTest() {
		try {
			BeanFactory factory = getBeanFactory();
			dao = (AttributeDao) factory.getBean("attributeDao");
			vDao = (ValidationTypeDao) factory.getBean("validationTypeDao");
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
			print((Attribute) o);
		}
	}
	
	public void testCreate() {
		Attribute bean = new Attribute();
		bean.setIdentifier("test");
		bean.setCalculated(false);
		bean.setDataType(3);
		bean.setDefaultValue("3");
		bean.setEditor(1);
		bean.setFormOrder(5);
		bean.setLoadInList(true);
		bean.setTypeId(4);
		bean.setLabel("de", "Test de");
		bean.setLabel("en", "Test en");
		bean.setLabel("fr", "Test fr");
		bean.setLabel("it", "Test it");
		Validation v = new Validation();
		v.setParameter("5");
		v.setValidationType(vDao.getById(1));
		List<Validation> list = new ArrayList<Validation>(1);
		list.add(v);
		bean.setValidations(list);
		dao.create(bean);
		print(bean);
		id = bean.getAttributeId();
	}
	
	public void testUpdate() {
		if (id != 0) {
			Attribute bean = dao.getById(id);
			bean.setCalculated(true);
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
	
	private static void print(Attribute a) {
		System.out.println("getAttributeId: " + a.getAttributeId());
		System.out.println("getIdentifier: " + a.getIdentifier());
		System.out.println("getChoices: " + a.getChoices());
		System.out.println("getDataType: " + a.getDataType());
		System.out.println("getDefaultValue: " + a.getDefaultValue());
		System.out.println("getEditor: " + a.getEditor());
		System.out.println("getFormatter: " + a.getFormatter());
		System.out.println("getFormOrder: " + a.getFormOrder());
		System.out.println("isCalculated: " + a.isCalculated());
		System.out.println("isLoadInList: " + a.isLoadInList());
		System.out.println("getTypeId: " + a.getTypeId());
		System.out.println("DE: " + a.getLabel("de"));
		System.out.println("EN: " + a.getLabel("en"));
		System.out.println("FR: " + a.getLabel("fr"));
		System.out.println("IT: " + a.getLabel("it"));
		if (a.getValidations() != null) {
			for (Validation v : a.getValidations()) {
				System.out.println("Validation getAttributeId: " + v.getAttributeId());
				System.out.println("Validation getParameter: " + v.getParameter());
				System.out.println("Validation getValidationTypeId: " + v.getValidationTypeId());
				System.out.println("Validation getValidationType: " + v.getValidationType());
			}
		}
	}
}
