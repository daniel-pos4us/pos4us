package morning.v1.POS4US.Manager.System;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

import morning.v1.POS4US.POS4US;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.Manager.General.CaptionManager;
import morning.v1.POS4US.Manager.General.CategoryManager;
import morning.v1.POS4US.Manager.General.HallManager;
import morning.v1.POS4US.Manager.General.MenuManager;
import morning.v1.POS4US.Manager.General.OptionManager;
import morning.v1.POS4US.Manager.General.OrderManager;
import morning.v1.POS4US.Manager.General.TableManager;
import morning.v1.POS4US.Manager.General.TableStatusManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

public class LoadManager {
	
	private static boolean bLoaded = false;
	
	// load xml data to memory
	public static boolean loadBusinessData() {
		
		if(!bLoaded) {
			ResourceManager.init();
			TableStatusManager.init();
			TableManager.init();
			
			// from db
			CaptionManager.loadCaption();
			HallManager.loadHall();
			CategoryManager.loadCategory();
			MenuManager.loadMenu();
			OptionManager.loadOptionCommon();
			OptionManager.loadOptionSpecific();
			
			OrderManager.loadOptionList(); // it needs for ordering option
			
			bLoaded = true;
		}
		return bLoaded;
	}
		
	public static void loadAutomatically() {
		int nOperation = checkServerOperation();
		
		if(nOperation == 1) {
			TableStatusManager.updateStatus(); // load table status periodically
	
		} else {
			ThreadManager.stopThread();
			ActivityManager.call(ActivityManager.getActivity(), POS4US.class);
		}
	}
	
	public static int checkServerOperation() {
		
		String strOperation = "";
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_CheckServerOperation()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("operation", listParameter);
		NodeList nlCheck = pdm.getXmlNodeListByHttpPost();
		
		if(nlCheck != null) {
			if(nlCheck.item(0) != null) {
				strOperation = StringUtil.notNull(nlCheck.item(0).getChildNodes().item(0).getTextContent());

				if(strOperation.equals("")) 
					strOperation = "0";
				
				return Integer.valueOf(strOperation);
			}
		}
		
		return 0;
	}
	
	public static void unloadAllManagers() {
		ThreadManager.kill();
		UserManager.kill();
		HallManager.kill();
		CategoryManager.kill();
		MenuManager.kill();
		TableManager.kill();
		TableStatusManager.kill();
		NavigationManager.kill();
		ResourceManager.kill();
	}
}
