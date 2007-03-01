/**
 * collaboweb7
 * Feb 20, 2007
 */
package ch.arpage.collaboweb.struts.tags;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * TestPattern
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class TestPattern extends TestCase {

	public void testEmpty() {
		Assert.assertNull("Result is not null", replacePattern(null));
	}
	
	public void testOneMatching() {
		String text = "this is a simple text <label text=\"first.1\"/> this is a simple ${locale} text this is a simple ${locale} text this is a simple text";
		String expected = "this is a simple text FIRST.1 this is a simple en text this is a simple en text this is a simple text";
		Assert.assertEquals("Result is not null", expected, replacePattern(text));
	}
	
	public void testTwoMatching() {
		String text = "this is a <b>simple</b> text this is a simple text this <label text=\"first\"/> is a simple text this <label text=\"second\"/> is a simple text this is a simple text";
		String expected = "this is a <b>simple</b> text this is a simple text this FIRST is a simple text this SECOND is a simple text this is a simple text";
		Assert.assertEquals("Result is not null", expected, replacePattern(text));
	}
	
	public void testNullMatching() {
		String text = "this is a simple text";
		Assert.assertEquals("Result is not null", text, replacePattern(text));
	}
	
	private String replacePattern(String text) {
		if (text != null) {
			text = text.replaceAll("\\$\\{locale\\}", "en");
			Pattern pattern = Pattern.compile("<label text=\"([\\.\\w]+)\"/>");
			Matcher matcher = pattern.matcher(text);
			/*
			while (matcher.find()) {
				String group = matcher.group();
				matcher.replaceFirst(group.toUpperCase());
			}
			*/
			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				String group = matcher.group(1);
				matcher.appendReplacement(sb, group.toUpperCase());
			}
			matcher.appendTail(sb);
	
			return sb.toString();
		}
		return null;
	}
}
