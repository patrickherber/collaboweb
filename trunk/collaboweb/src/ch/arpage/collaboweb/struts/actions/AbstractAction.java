package ch.arpage.collaboweb.struts.actions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import ch.arpage.collaboweb.exceptions.NotAuthorizedException;
import ch.arpage.collaboweb.exceptions.ValidationException;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.MailService;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Abstract sction, superclass of all application struts' actions, which offers
 * common functionalities. 
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber </a>
 */
public abstract class AbstractAction extends Action {
	
	protected static final Log LOG = LogFactory.getLog(AbstractAction.class);
	
	private MailService mailService;
	
	/**
	 * Set the mailService.
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	/**
	 * Returns the mailService.
	 * @return the mailService
	 */
	protected MailService getMailService() {
		return mailService;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		if (LOG.isTraceEnabled()) {
            logStart(mapping, request);
		}
		checkSessionMessage(request);
		try {
			return executeAction(mapping, form, request, response);
		} catch (Exception e) {
			addGlobalError(request, e);
			return mapping.findForward(Constants.FAILURE_KEY);
		} finally {
			if (LOG.isTraceEnabled()) {
				logEnd(mapping, request);
			}
		}
	}
	
	/**
	 * Logs the start of the execution of an action
	 * @param mapping	The action mapping
	 * @param request	The request
	 */
    private void logStart(ActionMapping mapping, HttpServletRequest request) {
        LOG.trace(new StringBuffer(128).append("Requested Path [")
                .append(mapping.getPath()).append("] - Start [")
                .append(mapping.getType()).append("]"));
    }

	/**
	 * Logs the end of the execution of an action
	 * @param mapping	The action mapping
	 * @param request	The request
	 */
    private void logEnd(ActionMapping mapping, HttpServletRequest request) {
        LOG.trace(new StringBuffer(128).append("Requested Path [")
                .append(mapping.getPath()).append("] - End [")
                .append(mapping.getType()).append("]"));
    }

	/**
	 * Execute method that all the subclass of this class should implement
	 * to perform their functionality.
	 * 
	 * @param mapping	The action mapping
	 * @param form		The current action form
	 * @param request	The request
	 * @param response	The response
	 * @return			The action forward indicating the target of the request
	 * @throws Exception	In case an unandled exception occurs
	 */
	protected abstract ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * Stores the given language code and the corresponding locale in the 
	 * session.
	 * @param request	The request
	 * @param language	The language code
	 */
	protected void setLanguage(HttpServletRequest request, String language) {
		if (language != null) {
			WebUtils.setSessionAttribute(request, Constants.LANGUAGE,
					language.toLowerCase());
			setLocale(request, getLocale(language));
			User user = getUserInfo(request);
			if (user != null) {
				user.setLanguage(language);
			}
		}
	}

	/**
	 * Returns the current language stored in the session.
	 * @param request	The request
	 * @return			The current language stored in the session.
	 */
	protected String getLanguage(HttpServletRequest request) {
		String language = (String) WebUtils.getSessionAttribute(request,
				Constants.LANGUAGE);
		return (language != null) ? language : Constants.DEFAULT_LANGUAGE;
	}

	/**
	 * Stores the user object in the session.
	 * @param request	The request
	 * @param user		The user object
	 */
	protected void setUserInfo(HttpServletRequest request, User user) {
		WebUtils.setSessionAttribute(request, Constants.USER_INFO, user);
	}

	/**
	 * Returns the user object from the session
	 * @param request	The request
	 * @return 			The user object
	 */
	protected User getUserInfo(HttpServletRequest request) {
		return (User) WebUtils.getSessionAttribute(request, Constants.USER_INFO);
	}

	/**
	 * Returns the locale matching the given language.
	 * 
	 * @param language	The language code
	 * @return			The locale matching the given language.
	 */
	protected Locale getLocale(String language) {
		if ("fr".equalsIgnoreCase(language)) {
			return Locale.FRENCH;
		} else if ("it".equalsIgnoreCase(language)) {
			return Locale.ITALIAN;
		} else if ("de".equalsIgnoreCase(language)) {
			return Locale.GERMAN;
		} else {
			return Locale.ENGLISH;
		}
	}

	/**
	 * Adds a corresponding error message for the given exception to be 
	 * displayed to the user, logs the errors and sends a notification to
	 * the defined administrator email address. 
	 * @param request	The request
	 * @param t			The exception
	 */
	protected void addGlobalError(HttpServletRequest request, Throwable t) {
		if (t instanceof NotAuthorizedException) {
			addGlobalError(request, "errors.notAuthorized");
		} else if (t instanceof ValidationException) {
			addGlobalErrors(request, ((ValidationException) t).getErrors());
		} else {
			StringBuffer message = new StringBuffer(300);
			message.append("An exception occurred serving the URL [").append(
					request.getRequestURI()).append("]");
			LOG.error(message, t);
			message.append("\n\n").append(toString(t));
			try {
				mailService.sendMessage("Application Exception", 
						message.toString());
			} catch (Exception ex) {
				LOG.error("Could not send exception alert", ex);
			}
			addGlobalError(request, "exception", t);
		}
	}

	/**
	 * Adds an error message identified by the given key to be displayed to the 
	 * user.
	 * @param request	The request
	 * @param key		The key of error message
	 */
	protected void addGlobalError(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
		saveErrors(request, messages);
	}

	/**
	 * Adds an error message identified by the given key to be displayed to the 
	 * user. The message should by customized using the given parameter.
	 * 
	 * @param request	The request
	 * @param key		The key of the error message
	 * @param parameter	The parameter
	 */
	protected void addGlobalError(HttpServletRequest request, String key,
			Object parameter) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key,
				parameter));
		saveErrors(request, messages);
	}

	/**
	 * Adds the given list of error messages to be displayed to the user.
	 * @param request	The request
	 * @param errors	The list of error messages
	 */
	protected void addGlobalErrors(HttpServletRequest request, List<String> errors) {
		ActionMessages messages = new ActionMessages();
		for (String error : errors) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, 
					new ActionMessage("output", error));
		}
		saveErrors(request, messages);
	}

	/**
	 * Adds an error message identified by the given key to be displayed to the 
	 * user. The message should by customized using the given parameters.
	 * 
	 * @param request		The request
	 * @param key			The key of the error message
	 * @param parameters	The parameters
	 */
	protected void addGlobalError(HttpServletRequest request, String key,
			Object[] parameters) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key,
				parameters));
		saveErrors(request, messages);
	}

	/**
	 * Adds an info message identified by the given key to be displayed to the 
	 * user. The message should by customized using the given parameter.
	 * 
	 * @param request		The request
	 * @param key			The key of the info message
	 * @param parameters	The parameters
	 */
	protected void addGlobalMessage(HttpServletRequest request, String key,
			Object[] parameters) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key,
				parameters));
		saveMessages(request, messages);
	}

	/**
	 * Checks if messages are stored in the session. In case move these messages
	 * from the session to the request object.
	 * @param request	The request
	 */
	private void checkSessionMessage(HttpServletRequest request) {
		ActionMessages messages = 
			(ActionMessages) WebUtils.getSessionAttribute(request,
				ActionMessages.GLOBAL_MESSAGE);
		if (messages != null) {
			WebUtils.setSessionAttribute(request, 
					ActionMessages.GLOBAL_MESSAGE, null);
			saveMessages(request, messages);
		}
        messages = (ActionMessages) WebUtils.getSessionAttribute(request,
        		ActionErrors.class.getName());
        if (messages != null) {
        	WebUtils.setSessionAttribute(request, ActionErrors.class.getName(), 
        			null);
            saveErrors(request, messages);
        }
	}

	/**
	 * Add an error message for the given exception in the session
	 * @param request	The request
	 * @param e			The exception
	 */
    protected void addSessionError(HttpServletRequest request, Exception e) {
        addSessionError(request, e, "exception");
    }

    /**
	 * Add an error message identified by the given key for the given exception 
	 * in the session.
	 * @param request	The request
	 * @param e			The exception
     * @param key		The key of the error message
     */
    protected void addSessionError(HttpServletRequest request, Exception e, String key) {
        addGlobalError(request, e);
        addSessionError(request, key, e);
    }

	/**
	 * Adds an error message identified by the given key to the session. 
	 * The message should by customized using the given parameter.
	 * @param request	The request
	 * @param key		The key of the error message
	 * @param parameter	The parameter
	 */
    protected void addSessionError(HttpServletRequest request, String key,
            Object parameter) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                key, parameter));
        request.getSession().setAttribute(ActionErrors.class.getName(),
                actionMessages);
    }

	/**
	 * Adds an info message identified by the given key to the session. 
	 * @param request	The request
	 * @param key		The key of the info message
	 */
	protected void addSessionMessage(HttpServletRequest request, String key) {
		addSessionMessage(request, key, (Object) null);
	}

	/**
	 * Adds an info message identified by the given key to the session. 
	 * The message should by customized using the given parameter.
	 * @param request	The request
	 * @param key		The key of the info message
	 * @param parameter	The parameter
	 */
	protected void addSessionMessage(HttpServletRequest request, String key,
			Object parameter) {
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				key, parameter));
		WebUtils.setSessionAttribute(request, ActionMessages.GLOBAL_MESSAGE,
				actionMessages);
	}

	/**
	 * Adds an info message identified by the given key to the session. 
	 * The message should by customized using the given parameters.
	 * @param request		The request
	 * @param key			The key of the info message
	 * @param parameters	The parameters
	 */
	protected void addSessionMessage(HttpServletRequest request, String key,
			Object[] parameters) {
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				key, parameters));
		WebUtils.setSessionAttribute(request, ActionMessages.GLOBAL_MESSAGE,
				actionMessages);
	}

	/**
	 * Redirects the response to the given forward
	 * @param request	The request
	 * @param response	The response
	 * @param mapping	The action mapping
	 * @param forward	The action forward
	 * @param params	The query string
	 * @return			The new target action forward 
	 * @throws IOException
	 */
	protected ActionForward redirect(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping,
			String forward, String params) throws IOException {
		response.sendRedirect(response.encodeURL(request.getContextPath()
				+ mapping.findForward(forward).getPath())
				+ ((params != null) ? params : ""));
		return null;
	}
	
	/**
	 * Forwards the response to the given forward
	 * 
	 * @param mapping	The action mapping
	 * @param forward	The action forward
	 * @param params	The query string
	 * @param redirect	Should the response be redirected?
	 * @return			The new target action forward 
	 */
	protected ActionForward forward(ActionMapping mapping, String forward, 
			String params, boolean redirect) {
		ActionForward retVal = new ActionForward(mapping.findForward(forward));
		retVal.setModule(mapping.getModuleConfig().getPrefix());
		if (params != null) {
			retVal.setPath(retVal.getPath() + params);
		}
		retVal.setRedirect(redirect);
		return retVal;
	}
	
	/**
	 * Redirects the response to the request referer URL
	 * @param request	The request
	 * @param response	The response
	 * @param mapping	The action mapping
	 * @return			The new target action forward 
	 * @throws IOException
	 */
	protected ActionForward redirectToReferer(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping) 
			throws IOException {	
		String referer = request.getHeader("Referer");
		if (StringUtils.hasText(referer)) {
			response.sendRedirect(referer);
			return null;
		} else {
			return mapping.getInputForward();
		}
	}
	
	/**
	 * Returns a string representation of the given throwable.
	 * @param t		The exception
	 * @return		A string representation of the given throwable.
	 */
	private String toString(Throwable t) {
		if (t != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("Exception: ").append(t.getClass().getName()).append("\n");
			if (t.getMessage() != null) {
			    sb.append("Message: ").append(t.getMessage()).append("\n");
			}
			StackTraceElement[] stack = t.getStackTrace();
		    sb.append("\nStack trace:\n");
			for (int i = 0; i < stack.length; i++) {
				sb.append(stack[i].toString()).append("\n");
			}
			Throwable cause = t.getCause();
			while (cause != null) {
			    sb.append("\nCause: ").append(cause).append("\nStack trace:\n");
				stack = cause.getStackTrace();
				for (int i = 0; i < stack.length; i++) {
					sb.append(stack[i].toString()).append("\n");
				}
				cause = cause.getCause();
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}