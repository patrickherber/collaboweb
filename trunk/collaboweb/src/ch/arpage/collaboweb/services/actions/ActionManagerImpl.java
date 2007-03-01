/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.services.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ActionManager;

/**
 * Implementation of the ActionManager service, with factory functionality
 * for intstatiating actions.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ActionManagerImpl implements ActionManager, BeanFactoryAware {

	private BeanFactory beanFactory;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * Returns the action defined with the given name
	 * @param name	The name of the action as defined in the spring config file
	 * @return	The action defined with the given name
	 */
	private Action getAction(String name) {
		return (Action) beanFactory.getBean(name, Action.class);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.ActionManager#executeAction(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, ch.arpage.collaboweb.model.User, java.lang.String)
	 */
	public String executeAction(String actionName, HttpServletRequest request, 
			HttpServletResponse response, User user, String parameter) 
			throws Exception {
		Action action = getAction(actionName);
		if (action != null) {
			return action.execute(request, response, user, parameter);
		}
		throw new IllegalArgumentException(actionName);
	}
}
