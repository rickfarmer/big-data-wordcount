package wordcount.container;

import org.apache.hadoop.io.Text;

public class TextUtil {

	public static String cleanText(String s) {
		return s.replaceAll("\\W", "");
	}

	public static boolean containsNumericKey(Text key) {
		return !(key.toString()).matches("^[\\d].*");
	}
	
	public static boolean isBelowThreshold(int count, int max) {
		return count < max;
	}
	
	public static boolean isNumeric(String s) {
		return hasValue(s) && s.matches("^[\\d]*");
	}

	public static boolean hasValue(Text t) {
		return hasValue(t.toString());
	}
	
	public static boolean hasValue(String s) {
		return (s!=null && !s.matches("\\W") && s.length()>0)?true:false;
	}

	public static int getIntValue(String s) {
		return isNumeric(s)?Integer.parseInt(s):0;
	}
}
