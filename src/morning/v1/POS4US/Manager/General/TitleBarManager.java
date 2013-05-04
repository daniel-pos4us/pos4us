package morning.v1.POS4US.Manager.General;

import android.widget.TextView;
import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.DataObject.Id;
import morning.v1.POS4US.DataObject.TitleBar;
import morning.v1.POS4US.Manager.System.ResourceManager;

/*
 * Manage Title Bar
 */
public class TitleBarManager {
	
	private static TitleBar _TitleBar = new TitleBar();
	
	public static final String getLanguage() {	return _TitleBar.getLanguage(); }
	public static final String getBusinessName() { return _TitleBar.getBusinessName(); }
	public static final String getUserId() { return _TitleBar.getUserId(); }
	
	public static void setLanguage(String strLang) { _TitleBar.setLanguage(strLang); }
	public static void setBusinessName(String strBusinessName) { _TitleBar.setBusinessName(strBusinessName); }
	public static void setUserId(String strUserId) { _TitleBar.setUserId(strUserId); }
	
	private static void toggleLanguage_Common() {

		if(Property.MENU_LANGUAGE.equals(Property.MenuLanguage.ENGLISH)) {
			Property.MENU_LANGUAGE = Property.MenuLanguage.OTHER;
			setLanguage(Property.LanguageTextOnTitleBar.OTHER);
			
		} else {
			Property.MENU_LANGUAGE = Property.MenuLanguage.ENGLISH;
			setLanguage(Property.LanguageTextOnTitleBar.ENGLISH);
		}		
	}
	
	public static void toggleLanguage_SelectHallTable() {
		toggleLanguage_Common();
		TextView tvLanguage = (TextView)ResourceManager.get(Id.getLanguageId());
		tvLanguage.setText(getLanguage());
		CaptionManager.toggleLanguage_SelectHallTable();
	}
	
	public static void toggleLanguage_SelectMenu() {
		toggleLanguage_Common();
		TextView tvLanguage = (TextView)ResourceManager.get(Id.getLanguageId()+1);
		tvLanguage.setText(getLanguage());
		CaptionManager.toggleLanguage_SelectMenu();
	}

}
