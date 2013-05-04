package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.DataObject.Caption;
import morning.v1.POS4US.DataObject.Menu;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Util.DocumentManager;
import morning.v1.POS4US.Util.Logger;
import morning.v1.POS4US.Util.Pos4usDocumentManager;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.widget.Button;
import android.widget.TextView;

/*
 * Manage Caption
 */
public class CaptionManager {
	
	private static int nCaptionSize = 0;
	private static Map<String,Caption> mapCaption = new Hashtable<String,Caption>();
	
	public static final Caption getCaption() { return mapCaption.get(Property.CAPTION_LANGUAGE); }

	// Change language in SelectHallTable Layout
	public static void toggleLanguage_SelectHallTable() {
		
		if(Property.MENU_LANGUAGE.equalsIgnoreCase(Property.MenuLanguage.ENGLISH)) {
			
			// hall
			for(int i=0; i<HallManager.getSize(); i++) {
				int nHallId = IdManager.getHallId(i);
				Button HallButton = (Button)ResourceManager.get(nHallId);
				HallButton.setText(HallManager.getHallMap().get(nHallId).NAME_ENG);
			}
			
		} else {
			// hall
			for(int i=0; i<HallManager.getSize(); i++) {
				int nHallId = IdManager.getHallId(i);
				Button HallButton = (Button)ResourceManager.get(nHallId);
				HallButton.setText(HallManager.getHallMap().get(nHallId).NAME_OTH);
			}
		}
	}
	
	// Change language in SelectMenu Layout
	public static void toggleLanguage_SelectMenu() {
		
		if(Property.MENU_LANGUAGE.equalsIgnoreCase(Property.MenuLanguage.ENGLISH)) {
			
			// hall
			for(int i=0; i<HallManager.getSize(); i++) {
				int nHallId = IdManager.getHallId(i);
				Button HallButton = (Button)ResourceManager.get(nHallId);
				HallButton.setText(HallManager.getHallMap().get(nHallId).NAME_ENG);
			}
			
			// category
			for(int i=0; i<CategoryManager.getSize(); i++) {
				int nCateId = IdManager.getCategoryId(i);
				Button CateButton = (Button)ResourceManager.get(nCateId);
				CateButton.setText(CategoryManager.getCategoryMap().get(nCateId).NAME_ENG);
			}
			
			// menu
			for(int i=0; i<MenuManager.getSize(); i++) {
				int nMenuTextId = IdManager.getMenuTextId(i);
				int nMenuId = MenuManager.getResourceIdByTextId(nMenuTextId);
				Menu _Menu = MenuManager.getMenuByResourceId(nMenuId);
				
				TextView tvMenuText = (TextView)ResourceManager.get(nMenuTextId);
				tvMenuText.setText(_Menu.NAME_ENG);
			}
			
		} else {
			// hall
			for(int i=0; i<HallManager.getSize(); i++) {
				int nHallId = IdManager.getHallId(i);
				Button HallButton = (Button)ResourceManager.get(nHallId);
				HallButton.setText(HallManager.getHallMap().get(nHallId).NAME_OTH);
			}
			// category
			for(int i=0; i<CategoryManager.getSize(); i++) {
				int nCateId = IdManager.getCategoryId(i);
				Button CateButton = (Button)ResourceManager.get(nCateId);
				CateButton.setText(CategoryManager.getCategoryMap().get(nCateId).NAME_OTH);
			}
			// menu
			for(int i=0; i<MenuManager.getSize(); i++) {
				int nMenuTextId = IdManager.getMenuTextId(i);
				TextView tvMenuText = (TextView)ResourceManager.get(nMenuTextId);
				int nMenuId = MenuManager.getResourceIdByTextId(nMenuTextId);
				Menu _Menu = MenuManager.getMenuByResourceId(nMenuId);
				tvMenuText.setText(_Menu.NAME_OTH);
			}

		}
	}
	
	// load caption
	public static boolean loadCaption() {

		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_Caption()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("caption", listParameter);
		NodeList nodeCaption = pdm.getXmlNodeListByHttpPost();

		if(nodeCaption != null) {
			
			init();
			
			nCaptionSize = nodeCaption.getLength();
			
			for(int i=0; i<nCaptionSize; i++) {
				
				Node nodeChild = nodeCaption.item(i);
				
				Caption caption = new Caption();
				
				caption.ID = Integer.valueOf(nodeChild.getAttributes().getNamedItem("id").getNodeValue());
				
				NodeList nlChildren = nodeChild.getChildNodes();
				
				int j = 0;
				caption.LANGUAGE = nlChildren.item(j++).getTextContent().trim();
				caption.USERID = nlChildren.item(j++).getTextContent().trim();
				caption.PASSWORD = nlChildren.item(j++).getTextContent().trim();
				caption.CONFIRM = nlChildren.item(j++).getTextContent().trim();
				caption.CURRENT_ORDER = nlChildren.item(j++).getTextContent().trim();
				caption.YES = nlChildren.item(j++).getTextContent().trim();
				caption.NOP = nlChildren.item(j++).getTextContent().trim();
				caption.Q_CONFIRM_ORDER = nlChildren.item(j++).getTextContent().trim();
				caption.Q_CANCEL_ORDER = nlChildren.item(j++).getTextContent().trim();
				caption.CANCEL = nlChildren.item(j++).getTextContent().trim();
				caption.Q_CHECK_ACCOUNT = nlChildren.item(j++).getTextContent().trim();
				caption.Q_EXIT = nlChildren.item(j++).getTextContent().trim();
				caption.MODIFY = nlChildren.item(j++).getTextContent().trim();
				caption.DETAIL = nlChildren.item(j++).getTextContent().trim();
				caption.CLOSE = nlChildren.item(j++).getTextContent().trim();
				caption.Q_LOGOUT = nlChildren.item(j++).getTextContent().trim();
				caption.Q_REMOVE = nlChildren.item(j++).getTextContent().trim();
				mapCaption.put(caption.LANGUAGE, caption);
			}
			return true;
		}
		
		return false;
	}

	public static void init() {
		if(mapCaption == null) mapCaption = new Hashtable<String,Caption>();
		else mapCaption.clear();
	}
	public static void clear() {
		mapCaption.clear();
	}
	public static void kill() {
		if(mapCaption != null) {
			mapCaption.clear();
			mapCaption = null;
		}
	}
}
