package cyano.basemetals.utils;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class StringUtilities {

	/**
	 *
	 * @param value
	 * @return
	 */
	public static String upperCaseFirst(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}