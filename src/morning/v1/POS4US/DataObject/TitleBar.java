package morning.v1.POS4US.DataObject;

/*
 * Titlebar Data Object
 */
public class TitleBar {
	
	public String LANGUAGE = "";
	public String BUSINESS_NAME = "";
	public String USER_ID = "Unidentified";
	
	public TitleBar() {}
	public TitleBar(String strLang, String strBusinessName, String strUserId) {
		LANGUAGE = strLang;
		BUSINESS_NAME = strBusinessName;
		USER_ID = strUserId;
	}
	
	public void setLanguage(String strLang) { LANGUAGE = strLang; }
	public void setBusinessName(String strBusinessName) { BUSINESS_NAME = strBusinessName; }
	public void setUserId(String strUserId) { USER_ID = strUserId; }

	public final String getLanguage() { return LANGUAGE; }
	public final String getBusinessName() { return BUSINESS_NAME; }
	public final String getUserId() { return USER_ID; }
}
