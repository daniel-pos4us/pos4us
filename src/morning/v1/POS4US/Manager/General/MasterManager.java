package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.List;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

/*
 * Manage Master Table
 */
public class MasterManager {

	public static boolean loadMaster() {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_Master()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("master", listParameter);
		NodeList nodeMaster = pdm.getXmlNodeListByHttpPost();

		if(nodeMaster != null) {
			
			if(nodeMaster.item(0) != null) {
				
				NodeList nodeChildren = nodeMaster.item(0).getChildNodes();
				
				if(nodeChildren != null) {
					
					String strCaptionLanguage = StringUtil.notNull(nodeChildren.item(0).getTextContent());
					String strMenuLanguage = StringUtil.notNull(nodeChildren.item(1).getTextContent());
					String strProviderName = StringUtil.notNull(nodeChildren.item(2).getTextContent());
					String strBusinessName = StringUtil.notNull(nodeChildren.item(3).getTextContent());
					String strCurrencyUnit = StringUtil.notNull(nodeChildren.item(4).getTextContent());
					
					Property.CAPTION_LANGUAGE = strCaptionLanguage;
					Property.MENU_LANGUAGE = strMenuLanguage;
					Property.PROVIDER_NAME = strProviderName;
					Property.BUSINESS_NAME = strBusinessName;
					Property.CURRENCY_UNIT = strCurrencyUnit;
					
					String strLangTextOnTitleBar = Property.LanguageTextOnTitleBar.ENGLISH;
					if(strMenuLanguage.equalsIgnoreCase(Property.MenuLanguage.OTHER))
						strLangTextOnTitleBar = Property.LanguageTextOnTitleBar.OTHER;
					
					TitleBarManager.setBusinessName(strBusinessName);
					TitleBarManager.setLanguage(strLangTextOnTitleBar);
					
					return true;
				}
			}
		}
		
		return false;
	}
}
