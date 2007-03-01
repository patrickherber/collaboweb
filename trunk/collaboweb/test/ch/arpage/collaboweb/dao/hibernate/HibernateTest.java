/**
 * collaboweb7
 * Feb 11, 2007
 */
package ch.arpage.collaboweb.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ch.arpage.collaboweb.model.Action;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.Validation;
import ch.arpage.collaboweb.model.ValidationType;


/**
 * HibernateTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class HibernateTest extends HibernateDaoSupport {

	public static void main(String[] args) {
		try {
			BeanFactory factory = getBeanFactory();
			HibernateTest test = 
				(HibernateTest) factory.getBean("hibernateTest");
			test.testAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testActions() {
		Action action = getActionById(1);
		printAction(action);
		action = createAction();
		printAction(action);
		action.setClassName("updated");
		action.setLabel("de", "updated label");
		getHibernateTemplate().saveOrUpdate(action);
		printAction(action);
		deleteAction(action);
		List actions = getActionList();
		for (Object o : actions) {
			printAction((Action) o);
		}
	}

	private static BeanFactory getBeanFactory() throws Exception {
		XmlBeanFactory factory = new XmlBeanFactory(
				new FileSystemResource("build/classes/applicationContext-hibernate.xml"));
		return factory;
	}
	
	private static void printAction(Action a) {
		System.out.println("Action ID: " + a.getActionId());
		System.out.println("ClassName: " + a.getClassName());
		System.out.println("CommunityId: " + a.getCommunityId());
		System.out.println("DE: " + a.getLabel("de"));
		System.out.println("EN: " + a.getLabel("en"));
		System.out.println("FR: " + a.getLabel("fr"));
		System.out.println("IT: " + a.getLabel("it"));
	}
	
	public List getActionList() {
		return getHibernateTemplate().find("from Action");
	}
	
	public Action getActionById(int id) {
		return (Action) getHibernateTemplate().load(Action.class, new Integer(id));
	}
	
	public Action createAction() {
		Action action = new Action();
		//action.setActionId(7);
		action.setClassName("ch.arpage.collaboweb.Test");
		action.setCommunityId(1);
		action.setLabel("de", "Tedesco");
		action.setLabel("en", "Inglese");
		action.setLabel("fr", "Francese");
		action.setLabel("it", "Italiano");
		getHibernateTemplate().persist(action);
		return action;
	}
	
	public void deleteAction(Action action) {
		getHibernateTemplate().delete(action);
	}
	
	public void testAttributes() {
		Attribute bean = getAttributeById(1);
		printAttribute(bean);
		bean = createAttribute();
		printAttribute(bean);
		bean.setCalculated(true);
		bean.setLabel("de", "updated label");
		getHibernateTemplate().saveOrUpdate(bean);
		printAttribute(bean);
		deleteAttribute(bean);
		List list = getAttributeList();
		for (Object o : list) {
			printAttribute((Attribute) o);
		}
	}
	
	private static void printAttribute(Attribute a) {
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
	
	public List getAttributeList() {
		return getHibernateTemplate().find("from Attribute");
	}
	
	public Attribute getAttributeById(int id) {
		return (Attribute) getHibernateTemplate().load(Attribute.class, 
				new Integer(id));
	}
	
	public Attribute createAttribute() {
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
		v.setValidationType(getValidationTypeById(1));
		List<Validation> list = new ArrayList<Validation>(1);
		list.add(v);
		bean.setValidations(list);
		getHibernateTemplate().persist(bean);
		return bean;
	}
	
	public void deleteAttribute(Attribute bean) {
		getHibernateTemplate().delete(bean);
	}
	
	public void testValidationTypes() {
		ValidationType bean = getValidationTypeById(1);
		printValidationType(bean);
		bean = createValidationType();
		printValidationType(bean);
		bean.setClassName("updated class");
		bean.setLabel("de", "updated label");
		bean.setMessage("fr", "updated message");
		getHibernateTemplate().saveOrUpdate(bean);
		printValidationType(bean);
		deleteValidationType(bean);
		List list = getValidationTypeList();
		for (Object o : list) {
			printValidationType((ValidationType) o);
		}
	}
	
	private static void printValidationType(ValidationType a) {
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
	
	public List getValidationTypeList() {
		return getHibernateTemplate().find("from ValidationType");
	}
	
	public ValidationType getValidationTypeById(int id) {
		return (ValidationType) getHibernateTemplate().load(ValidationType.class, 
				new Integer(id));
	}
	
	public ValidationType createValidationType() {
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
		getHibernateTemplate().save(bean);
		return bean;
	}
	
	public void deleteValidationType(ValidationType bean) {
		getHibernateTemplate().delete(bean);
	}
}
