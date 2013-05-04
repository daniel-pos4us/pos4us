package morning.v1.POS4US.Util;

import morning.v1.POS4US.Configuration.Property;

/*
 * Manipulate Text
 */
public class TextFormatter {
	
	public static String getText(String strEnglish, String strOtherLang) {
		
		String strText = "";
		
		if(Property.MENU_LANGUAGE.equalsIgnoreCase(Property.MenuLanguage.ENGLISH))
			strText = Property.OrderBoardTextFormat.ENGLISH;
		else
			strText = Property.OrderBoardTextFormat.OTH;
		
		if(strEnglish != null)
			strText = strText.replaceAll("eng", strEnglish);
		
		if(strOtherLang != null)
			strText = strText.replaceAll("oth", strOtherLang);
		else
			strText = strText.replaceAll("(oth)", "");
		
		return strText;
	}
	
	public static String getText(String strEnglish, String strOtherLang, int nCut) {
		
		String strText = "";
		
		if(Property.MENU_LANGUAGE.equalsIgnoreCase(Property.MenuLanguage.ENGLISH))
			strText = Property.OrderBoardTextFormat.ENGLISH;
		else
			strText = Property.OrderBoardTextFormat.OTH;
		
		if(strEnglish != null) {
			if(strEnglish.length()>(nCut+8)) 
				strEnglish= strEnglish.substring(0, nCut-1);
			strText = strText.replaceAll("eng", strEnglish);
		} else {
			strText = strText.replaceAll("eng", "");
		}
		
		if(strOtherLang != null) {
			if(strOtherLang.length()>nCut) 
				strOtherLang = strOtherLang.substring(0, nCut-1);
			strText = strText.replaceAll("oth", strOtherLang);
		} else {
			strText = strText.replaceAll("(oth)", "");
		}
		return strText;
	}
}
