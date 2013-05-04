package morning.v1.POS4US.Manager.System;

import morning.v1.POS4US.Util.Mapper;

/*
 * Setting Manager
 */
public class SettingManager {
	
	// notice changed status
	public static void noticeChanged() { 
		Mapper.put("SETTING_IS_CHANGED", "TRUE");
	}
	// is changed
	public static boolean IsChanged() {
		if(Mapper.get("SETTING_IS_CHANGED") != null) {
			String strReturns = (String)Mapper.get("SETTING_IS_CHANGED");
			return strReturns.equals("TRUE");
		}
		return false;
	}
	
	public static void clearChanged() {
		Mapper.put("SETTING_IS_CHANGED", "");
	}
}
