/**
 * collaboweb
 * 04.01.2007
 */
package ch.arpage.collaboweb.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * Utility functions
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class Utils {
	
	/**
	 * Parse the given value into a double, without throwing an exception
	 * in case it is not parsable. In this case 0 is returned.
	 * 
	 * @param value
	 * @return the parsed value (or 0 if not parsable)
	 */
	public static final double parseDouble(Object value) {
		if (value != null) {
			return parseDouble(value.toString());
		} else {
			return 0.0;
		}
	}

	/**
	 * Parse the given value into a double, without throwing an exception
	 * in case it is not parsable. In this case 0 is returned.
	 * 
	 * @param value
	 * @return the parsed value (or 0 if not parsable)
	 */
	public static final double parseDouble(String value) {
		if (StringUtils.hasText(value)) {
			try {
				if (value.indexOf('/') == -1) {
					return Double.parseDouble(value);
				} else {
					double number = 0.0; 
					int slashIndex = value.indexOf('/');
					int spaceIndex = value.indexOf(' ');
					if (spaceIndex != -1) {
						number = Double.parseDouble(value.substring(0, spaceIndex));
					}
					try {
						double over = Double.parseDouble(value.substring((spaceIndex != -1) ? spaceIndex+1 : 0, slashIndex));
						double under = Double.parseDouble(value.substring(slashIndex+1, value.length()));
						if (under != 0.0) {
							number += over/under;
						}
					} catch (Exception e) {}
					return number;
				}
			} catch (NumberFormatException nfe) {}
		} 
		return 0.0;
	}

	/**
	 * Parse the given value into a int, without throwing an exception
	 * in case it is not parsable. In this case 0 is returned.
	 * 
	 * @param value
	 * @return the parsed value (or 0 if not parsable)
	 */
	public static final int parseInt(Object value) {
		if (value != null) {
			return parseInt(value.toString());
		} else {
			return 0;
		}
	}

	/**
	 * Parse the given value into a int, without throwing an exception
	 * in case it is not parsable. In this case 0 is returned.
	 * 
	 * @param value
	 * @return the parsed value (or 0 if not parsable)
	 */
	public static final int parseInt(String value) {
		if (StringUtils.hasText(value)) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException nfe) {}
		} 
		return 0;
	}

	/**
	 * Parse the given value into a long, without throwing an exception
	 * in case it is not parsable. In this case 0 is returned.
	 * 
	 * @param value
	 * @return the parsed value (or 0 if not parsable)
	 */
	public static final long parseLong(Object value) {
		if (value != null) {
			return parseLong(value.toString());
		} else {
			return 0L;
		}
	}

	/**
	 * Parse the given value into a long, without throwing an exception
	 * in case it is not parsable. In this case 0 is returned.
	 * 
	 * @param value
	 * @return the parsed value (or 0 if not parsable)
	 */
	public static final long parseLong(String value) {
		if (StringUtils.hasText(value)) {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException nfe) {}
		} 
		return 0L;
	}
	
	/**
	 * Parse the given value into a date, without throwing an exception
	 * in case it is not parsable. In this case null is returned.
	 * 
	 * @param date	The date to parse
	 * @return 		The parsed value (or null if not parsable)
	 */
    public static final Date parseDate(String date) {
    	return parseDate(date, null, false);
    }

	/**
	 * Parse the given value into a date, without throwing an exception
	 * in case it is not parsable. In this case <code>defaultDate</code> is returned.
	 * 
	 * @param date			The date to parse
	 * @param defaultDate	The default date
	 * @return the parsed date (or <code>defaultDate</code> if not parsable)
	 */
    public static final Date parseDate(String date, Date defaultDate) {
    	return parseDate(date, defaultDate, false);
    }

	/**
	 * Parse the given value into a date, without throwing an exception
	 * in case it is not parsable. In that case, or when the syntax of the date
	 * is not correct, <code>defaultDate</code> is returned.
	 * 
	 * @param date			The date to parse
	 * @param defaultDate	The default date
	 * @param check 		Check the date syntax?
	 * @return the parsed date (or <code>defaultDate</code> if not parsable or 
	 * when the syntax of the date is not correct)
	 */
    public static final Date parseDate(String date, Date defaultDate, boolean check) {
		if (StringUtils.hasText(date)) {
			if (date.trim().length() > 11) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
				} catch (Exception e) {
					/* ignore */
					return defaultDate;
				}
			}
            String[] parts = date.split("[./-]");
            if (parts != null && parts.length > 1) {
            	if (check) {
            		for (int i = 0; i < parts.length; ++i) {
            			for (int j = 0; j < parts[i].length(); ++j) {
            				if (!Character.isDigit(parts[i].charAt(j))) {
            					return defaultDate;
            				}
            			}
            		}
            	}
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, parseInt(parts[0]));
                calendar.set(Calendar.MONTH, parseInt(parts[1]) - 1);
                if (parts.length > 2) {
                    int year = parseInt(parts[2]);
                    if (year < 51) {
                        int currentYear = calendar.get(Calendar.YEAR);
                        year += currentYear - (currentYear % 100);
                    } else if (year < 100) {
                        int currentYear = calendar.get(Calendar.YEAR);
                        year += currentYear - (100 + currentYear % 100);
                    }
                    calendar.set(Calendar.YEAR, year);
                }
                return calendar.getTime();
            }
        }
        return defaultDate;
    }
}
