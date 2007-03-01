/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.services.validation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Factory class for instantiating ResourceValidator objects.
 *
 * @see ResourceValidator
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidatorManager implements BeanFactoryAware {

	private static BeanFactory beanFactory;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory factory) throws BeansException {
		beanFactory = factory;
	}

	/**
	 * Returns the Resource Validator object identified by the given name
	 * @param name	The name of the resorce validator as defined in the spring
	 * 				config file.
	 * @return		The Resource Validator object identified by the given name
	 */
	public static ResourceValidator getResourceValidator(String name) {
		return (ResourceValidator) beanFactory.getBean(name, ResourceValidator.class);
	}
}
