/**
 * collaboweb
 * Feb 18, 2007
 */
package ch.arpage.collaboweb.struts.tags;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.taglibs.standard.resources.Resources;
import org.apache.taglibs.standard.tag.common.core.ImportSupport;
import org.apache.taglibs.standard.tag.common.core.Util;
import org.apache.taglibs.standard.tag.common.xml.ParseSupport;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Abstract class implemented by tags, used to transform a resource or a list
 * of resources using the stylesheet for the view type gived as parameter.<b>
 * This class is based on the Transform Tag of the Apache Jakarta Commons 
 * Tag Libs Project.
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class AbstractTransformTag extends BodyTagSupport {
	
	private static final Log LOG = LogFactory.getLog(AbstractTransformTag.class);

	// *********************************************************************
	// Private state
	
	protected Resource resource; // attribute

	protected int viewId; // attribute

	protected Result result; // attribute

	protected String var; // 'var' attribute

	protected int scope; // processed 'scope' attr

	protected Transformer t; // actual Transformer

	protected TransformerFactory tf; // reusable factory

	protected DocumentBuilder db; // reusable factory

	protected DocumentBuilderFactory dbf; // reusable factory
	
	protected Pattern pattern = Pattern.compile("<label key=\"([\\.\\w]+)\"/>");
	
	protected String styleSheet; // the stylesheet
	
	// *********************************************************************
	// Constructor and initialization

	/**
	 * Creates a ResourceTransformTag
	 */
	public AbstractTransformTag() {
		super();
		init();
	}

	/**
	 * Initialize the tag
	 */
	protected void init() {
		resource = null;
		viewId = 0;
		var = null;
		result = null;
		tf = null;
		scope = PageContext.PAGE_SCOPE;
	}

	// *********************************************************************
	// Accessor methods
	
	/**
	 * Set the resource.
	 * @param resource the resource to set
	 */
	public void setResource(Object resource) {
		this.resource = (Resource) resource;
	}
	
	/**
	 * Set the viewId.
	 * @param viewId the viewId to set
	 */
	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	/**
	 * Set the result
	 * @param result	The result to set
	 * @throws JspTagException
	 */
	public void setResult(Result result) throws JspTagException {
		this.result = result;
	}

	// *********************************************************************
	// Tag logic

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		/*
		 * We can set up our Transformer here, so we do so, and we let it
		 * receive parameters directly from subtags (instead of caching them.
		 */
		try {
			if (this.resource != null) {
				styleSheet = 
					resource.getResourceType().getViewStylesheet(viewId);
				if (styleSheet != null) {
					styleSheet = replaceLabels(styleSheet);
					// set up our DocumentBuilderFactory if necessary
					if (dbf == null) {
						dbf = DocumentBuilderFactory.newInstance();
						dbf.setNamespaceAware(true);
						dbf.setValidating(false);
					}
					if (db == null)
						db = dbf.newDocumentBuilder();
		
					// set up the TransformerFactory if necessary
					if (tf == null)
						tf = TransformerFactory.newInstance();
		
					return EVAL_BODY_BUFFERED;
				}
			}
			return EVAL_BODY_INCLUDE;
		} catch (ParserConfigurationException ex) {
			throw new JspException(ex);
		}
	}

	// parse 'xml' or body, transform via our Transformer,
	// and store as 'var' or through 'result'
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		try {
			if (this.resource != null && this.styleSheet != null) {
				String xml = getXml();
				Source source = getSource(xml, null);
	
				Source s = getSource(styleSheet, null);
				tf.setURIResolver(new JstlUriResolver(pageContext));
				t = tf.newTransformer(s);
	
				// we can assume at most one of 'var' or 'result' is specified
				if (result != null)
					// we can write directly to the Result
					t.transform(source, result);
				else if (var != null) {
					// we need a Document
					Document d = db.newDocument();
					Result doc = new DOMResult(d);
					t.transform(source, doc);
					pageContext.setAttribute(var, d, scope);
				} else {
					Result page = new StreamResult(new SafeWriter(
							pageContext.getOut()));
					t.transform(source, page);
				}
				return EVAL_PAGE;
			}
		} catch (Exception ex) {
			LOG.warn(ex);
		}
		return EVAL_BODY_AGAIN;
	}

	/**
	 * The source XML.
	 * @return the source XML
	 */
	protected abstract String getXml() throws Exception;

	/**
	 * Releases any resources we may have (or inherit)
	 */
	public void release() {
		init();
	}

	// *********************************************************************
	// Utility methods

	/**
	 * Wraps systemId with a "jstl:" prefix to prevent the parser from thinking
	 * that the URI is truly relative and resolving it against the current
	 * directory in the filesystem.
	 */
	private static String wrapSystemId(String systemId) {
		if (systemId == null)
			return "jstl:";
		else if (ImportSupport.isAbsoluteUrl(systemId))
			return systemId;
		else
			return ("jstl:" + systemId);
	}

	/**
	 * Retrieves a Source from the given Object, whether it be a String, Reader,
	 * Node, or other supported types (even a Source already). If 'url' is true,
	 * then we must be passed a String and will interpret it as a URL. A null
	 * input always results in a null output.
	 */
	private Source getSource(Object o, String systemId) throws SAXException,
			ParserConfigurationException, IOException {
		if (o == null)
			return null;
		else if (o instanceof Source) {
			return (Source) o;
		} else if (o instanceof String) {
			// if we've got a string, chain to Reader below
			return getSource(new StringReader((String) o), systemId);
		} else if (o instanceof Reader) {
			// explicitly go through SAX to maintain control
			// over how relative external entities resolve
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setEntityResolver(new ParseSupport.JstlEntityResolver(
					pageContext));
			InputSource s = new InputSource((Reader) o);
			s.setSystemId(wrapSystemId(systemId));
			Source result = new SAXSource(xr, s);
			result.setSystemId(wrapSystemId(systemId));
			return result;
		} else if (o instanceof Node) {
			return new DOMSource((Node) o);
		} else if (o instanceof List) {
			// support 1-item List because our XPath processor outputs them
			List l = (List) o;
			if (l.size() == 1) {
				return getSource(l.get(0), systemId); // unwrap List
			} else {
				throw new IllegalArgumentException(Resources
						.getMessage("TRANSFORM_SOURCE_INVALID_LIST"));
			}
		} else {
			throw new IllegalArgumentException(Resources
					.getMessage("TRANSFORM_SOURCE_UNRECOGNIZED")
					+ o.getClass());
		}
	}

	// *********************************************************************
	// Tag attributes

	/**
	 * Set the var
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * Set the scope
	 * @param scope	The scope to set
	 */
	public void setScope(String scope) {
		this.scope = Util.getScope(scope);
	}

	// *********************************************************************
	// Private utility classes

	/**
	 * A Writer based on a wrapped Writer but ignoring requests to close() and
	 * flush() it. (Someone must have wrapped the toilet in my office
	 * similarly...)
	 */
	private static class SafeWriter extends Writer {
		private Writer w;

		public SafeWriter(Writer w) {
			this.w = w;
		}

		public void close() {
		}

		public void flush() {
		}

		public void write(char[] cbuf, int off, int len) throws IOException {
			w.write(cbuf, off, len);
		}
	}

	// *********************************************************************
	// JSTL-specific URIResolver class

	/** 
	 * Lets us resolve relative external entities. 
	 */
	private static class JstlUriResolver implements URIResolver {
		private final PageContext ctx;

		public JstlUriResolver(PageContext ctx) {
			this.ctx = ctx;
		}

		public Source resolve(String href, String base)
				throws TransformerException {

			// pass if we don't have a systemId
			if (href == null)
				return null;

			// remove "jstl" marker from 'base'
			if (base != null && base.startsWith("jstl:"))
				base = base.substring(5);

			// we're only concerned with relative URLs
			if (ImportSupport.isAbsoluteUrl(href)
					|| (base != null && ImportSupport.isAbsoluteUrl(base)))
				return null;

			// base is relative; remove everything after trailing '/'
			if (base == null || base.lastIndexOf("/") == -1)
				base = "";
			else
				base = base.substring(0, base.lastIndexOf("/") + 1);

			// concatenate to produce the real URL we're interested in
			String target = base + href;

			// for relative URLs, load and wrap the resource.
			// don't bother checking for 'null' since we specifically want
			// the parser to fail if the resource doesn't exist
			InputStream s;
			if (target.startsWith("/")) {
				s = ctx.getServletContext().getResourceAsStream(target);
				if (s == null)
					throw new TransformerException(Resources.getMessage(
							"UNABLE_TO_RESOLVE_ENTITY", href));
			} else {
				String pagePath = ((HttpServletRequest) ctx.getRequest())
						.getServletPath();
				String basePath = pagePath.substring(0, pagePath
						.lastIndexOf("/"));
				s = ctx.getServletContext().getResourceAsStream(
						basePath + "/" + target);
				if (s == null)
					throw new TransformerException(Resources.getMessage(
							"UNABLE_TO_RESOLVE_ENTITY", href));
			}
			return new StreamSource(s);
		}
	}

	/**
	 * Replaces the labels
	 * @param styleSheet	The stylesheet
	 * @return				The stylesheet with labels replaced
	 */
	private String replaceLabels(String styleSheet) {
		if (StringUtils.hasText(styleSheet)) {
			String language = (String) 
				pageContext.getSession().getAttribute(Constants.LANGUAGE);
			if (language == null) {
				language = Constants.DEFAULT_LANGUAGE;
			}
			styleSheet = styleSheet.replaceAll("\\$\\{language\\}", language);
			Matcher matcher = pattern.matcher(styleSheet);
			StringBuffer sb = new StringBuffer(styleSheet.length());
			while (matcher.find()) {
				String key = matcher.group(1);
				try {
					String message = TagUtils.getInstance().message(pageContext, 
							Globals.MESSAGES_KEY, Globals.LOCALE_KEY, key);
					if (message != null) {
						matcher.appendReplacement(sb, message);
					}
				} catch (JspException e) {
					/* ignore */
				}
			}
			matcher.appendTail(sb);
			return sb.toString();
		}
		return null;
	}
}
