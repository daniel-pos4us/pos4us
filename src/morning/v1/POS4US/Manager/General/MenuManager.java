package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.DataObject.Category;
import morning.v1.POS4US.DataObject.Id;
import morning.v1.POS4US.DataObject.Menu;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Util.DocumentManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

/*
 * Manage Menu
 */
public class MenuManager {
	
	private static int nMenuSize = 0;
	
	private static Map<Integer,Menu> mapMenuList = null;
	private static Map<Integer,ArrayList<Menu>> mapMenuGroupByCategory = null;
	private static Map<Integer,Integer> mapMenuResourceIdByTextId = null;

	public static void putResourceIdByTextId(int nMenuTextId, int nMenuId) { mapMenuResourceIdByTextId.put(nMenuTextId,nMenuId); }	
	public static final int getResourceIdByTextId(int nMenuTextId) { return mapMenuResourceIdByTextId.get(nMenuTextId).intValue(); }

	
	public static final int getSize() { return nMenuSize; }
	
	public static int getResourceIdByDbId(int nMenuDbId) {
		
		Integer[] key = mapMenuList.keySet().toArray(new Integer[mapMenuList.size()]);
		
		for(int i=0;i<mapMenuList.size();i++) {
			
			Menu menu = mapMenuList.get(key[i]);
			if(menu.DB_ID == nMenuDbId)
				return menu.RESOURCE_ID;
		}
		
		return 0;
	}
	// categorize menus by category
	private static void groupByCategory (Menu menu) {
		
		// link "CATE_RESOURCE_ID"
		for(int i=0; i<CategoryManager.getSize(); i++) {
			
			Map<Integer,Category> map = CategoryManager.getCategoryMap();
			Category category = map.get(IdManager.getCategoryId(i));
		
			if( category.DB_ID == menu.CATE_DB_ID ) {
				menu.CATE_RESOURCE_ID = category.RESOURCE_ID;
				break;
			}
		}
		
		int nCateResourceId = menu.CATE_RESOURCE_ID;
		ArrayList<Menu> alMenuList = mapMenuGroupByCategory.get(nCateResourceId);

		if(alMenuList!=null) {
			alMenuList.add(menu);
		} else {
			alMenuList = new ArrayList<Menu>();
			alMenuList.add(menu);
		}
		
		mapMenuGroupByCategory.put(nCateResourceId, alMenuList);
	}
	
	// allocate resource id to each menu
	private static void setMenuResourceId() {
		
		for(int i=0; i<CategoryManager.getSize(); i++) {
			
			ArrayList<Menu> alMenuList = mapMenuGroupByCategory.get(IdManager.getCategoryId(i));
			
			if(alMenuList != null) {
				for(int j=0; j<alMenuList.size(); j++) {
				
					Menu menu = alMenuList.get(j);
					int nMenuResourceId = IdManager.getMenuId(i, j);
					menu.RESOURCE_ID = nMenuResourceId;
					mapMenuList.put(nMenuResourceId, menu); // enable to get menu name by resource id with ease
					
				}
			}
			
		}
		
	}
	
	// get menu by resource id
	public static final Menu getMenuByResourceId(int nMenuResourceId) {
		return mapMenuList.get(nMenuResourceId);
	}
	
	// load menu from xml file
	public static boolean loadMenu() {
		
		if (nMenuSize == 0) {
		
			List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
			listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_Menu()));
			
			Pos4usDocumentManager pdm = new Pos4usDocumentManager("menu", listParameter);
			NodeList menuNode = pdm.getXmlNodeListByHttpPost();
			
			if(menuNode != null) {
				
				init();
				
				ResourceManager.put(Id.getMenuXmlNodeId(),menuNode);
				
				nMenuSize = menuNode.getLength();
				
				for(int i=0; i<nMenuSize; i++) {
					
					Node node = menuNode.item(i);
					
					String strDbId = StringUtil.notNull(node.getAttributes().getNamedItem("id").getNodeValue());
					String strDbCateId = StringUtil.notNull(node.getAttributes().getNamedItem("cate_id").getNodeValue());
					String strHandlingSection1 = StringUtil.notNull(node.getAttributes().getNamedItem("handling_section1").getNodeValue());
					String strHandlingSection2 = StringUtil.notNull(node.getAttributes().getNamedItem("handling_section2").getNodeValue());
					String strHandlingSection3 = StringUtil.notNull(node.getAttributes().getNamedItem("handling_section3").getNodeValue());
					String strHandlingSection4 = StringUtil.notNull(node.getAttributes().getNamedItem("handling_section4").getNodeValue());
					
					NodeList children = node.getChildNodes();
					int j = 0;
					String strNameEng = StringUtil.notNull(children.item(j++).getTextContent());
					String strNameOth = StringUtil.notNull(children.item(j++).getTextContent());
					String strImgName = StringUtil.notNull(children.item(j++).getTextContent());
					String strPrice = StringUtil.notNull(children.item(j++).getTextContent());
					String strGst = StringUtil.notNull(children.item(j++).getTextContent());
					String strDescriptionEng = StringUtil.notNull(children.item(j++).getTextContent());
					String strDescriptionOth = StringUtil.notNull(children.item(j++).getTextContent());
					
					int nLimit = Property.MENU_TEXT_SHOW_LIMIT_NUM;
					
					if(strNameEng.length() > nLimit) strNameEng = strNameEng.substring(0,(nLimit-1)).concat(" ..");
					if(strNameOth.length() > nLimit) strNameOth = strNameOth.substring(0,(nLimit-1)).concat(" ..");
					
					int nDbId = Integer.valueOf(strDbId).intValue();
					int nDbCateId = Integer.valueOf(strDbCateId).intValue();
					
					float fPrice = Float.valueOf(strPrice).floatValue();
					float fGst = Float.valueOf(strGst).floatValue();
					
					int nHandlingSection1 = Integer.valueOf(strHandlingSection1);
					int nHandlingSection2 = Integer.valueOf(strHandlingSection2);
					int nHandlingSection3 = Integer.valueOf(strHandlingSection3);
					int nHandlingSection4 = Integer.valueOf(strHandlingSection4);
					
					groupByCategory(new Menu(nDbId, 0, nDbCateId, 0, strNameEng, strNameOth, strImgName, fPrice, fGst, strDescriptionEng, strDescriptionOth,nHandlingSection1,nHandlingSection2,nHandlingSection3,nHandlingSection4));
	
				} 
				
				setMenuResourceId();
				
				return true;
			
			} else {
				return false;
			}
			
		}
		
		return false;
	}
	
	// get menu array list by category
	public static ArrayList<Menu> getListByCategory (int nCateResourceId) {
		return mapMenuGroupByCategory.get(nCateResourceId);
	}
	
	public static void init() {
		if(mapMenuGroupByCategory == null) mapMenuGroupByCategory = new Hashtable<Integer,ArrayList<Menu>>();
		else mapMenuGroupByCategory.clear();
		if(mapMenuList == null) mapMenuList = new Hashtable<Integer,Menu>();
		else mapMenuList.clear();
		if(mapMenuResourceIdByTextId == null) mapMenuResourceIdByTextId = new Hashtable<Integer,Integer>();
		else mapMenuResourceIdByTextId.clear();
	}
	public static void clear() {
		if(mapMenuList != null) mapMenuList.clear();
		if(mapMenuGroupByCategory != null) mapMenuGroupByCategory.clear();
		if(mapMenuResourceIdByTextId != null) mapMenuResourceIdByTextId.clear();
	}
	public static void kill() {
		if(mapMenuList != null) { mapMenuList.clear(); mapMenuList = null; }
		if(mapMenuGroupByCategory != null) { mapMenuGroupByCategory.clear(); mapMenuGroupByCategory = null; }
		if(mapMenuResourceIdByTextId != null) { mapMenuResourceIdByTextId.clear(); mapMenuResourceIdByTextId = null; }
	}
	
}
