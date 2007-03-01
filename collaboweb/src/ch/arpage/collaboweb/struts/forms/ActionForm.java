/**
 * collaboweb
 * Feb 14, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import java.util.Map;

import ch.arpage.collaboweb.model.Action;

/**
 * ActionForm for the action model class
 * 
 * @see Action
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ActionForm extends AbstractForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Action action;
	
	/**
	 * Creates a new ActionForm.
	 */
	public ActionForm() {
		this(new Action());
	}

	/**
	 * Creates a new ActionForm based on the given action.
	 * @param action the action
	 */
	public ActionForm(Action action) {
		this.action = action;
	}
	
	/**
	 * Returns the action.
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Returns the actionId.
	 * @return the actionId
	 * @see ch.arpage.collaboweb.model.Action#getActionId()
	 */
	public int getActionId() {
		return action.getActionId();
	}

	/**
	 * Returns the className.
	 * @return the className
	 * @see ch.arpage.collaboweb.model.Action#getClassName()
	 */
	public String getClassName() {
		return action.getClassName();
	}

	/**
	 * Return the communityId.
	 * @return the communityId
	 * @see ch.arpage.collaboweb.model.Action#getCommunityId()
	 */
	public long getCommunityId() {
		return action.getCommunityId();
	}

	/**
	 * Returns the labels.
	 * @return the labels.
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabels()
	 */
	public Map<String, String> getLabels() {
		return action.getLabels();
	}

	/**
	 * Returns the parameter.
	 * @return the parameter
	 * @see ch.arpage.collaboweb.model.Action#getParameter()
	 */
	public String getParameter() {
		return action.getParameter();
	}

	/**
	 * Set the actionId.
	 * @param actionId	The actionId to set
	 * @see ch.arpage.collaboweb.model.Action#setActionId(int)
	 */
	public void setActionId(int actionId) {
		action.setActionId(actionId);
	}

	/**
	 * Set the className.
	 * @param className	The className to set
	 * @see ch.arpage.collaboweb.model.Action#setClassName(java.lang.String)
	 */
	public void setClassName(String className) {
		action.setClassName(className);
	}

	/**
	 * Set the communityId.
	 * @param communityId	The communityId to set
	 * @see ch.arpage.collaboweb.model.Action#setCommunityId(long)
	 */
	public void setCommunityId(long communityId) {
		action.setCommunityId(communityId);
	}

	/**
	 * Set the labels.
	 * @param labels	The labels to set
	 * @see ch.arpage.collaboweb.model.LabelableBean#setLabels(java.util.Map)
	 */
	public void setLabels(Map<String, String> labels) {
		action.setLabels(labels);
	}

	/**
	 * Set the parameter.
	 * @param parameter	The parameter to set
	 * @see ch.arpage.collaboweb.model.Action#setParameter(java.lang.String)
	 */
	public void setParameter(String parameter) {
		action.setParameter(parameter);
	}
}
