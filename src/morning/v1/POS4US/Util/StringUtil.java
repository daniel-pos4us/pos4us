package morning.v1.POS4US.Util;

/*
 * String Utility
 */
public class StringUtil {

	// Return safe string
	public static String notNull(String strValue) {
		return strValue == null ? "": strValue.trim();
	}
	public static String notNull(String strValue, String strAlternative) {
		return strValue == null ? strAlternative: strValue.trim();
	}
}
