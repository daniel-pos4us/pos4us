package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.DataObject.Category;
import morning.v1.POS4US.DataObject.Id;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Util.DocumentManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;

/*
 * Manage Category
 */
public class CategoryManager {
	
	private static int nCateSize = 0;
	private static Map<Integer,Category> mapCategory = new Hashtable<Integer,Category>();
	
	// Load Category from the server
	public static boolean loadCategory() { 
		
		if (nCateSize == 0) {
		
			List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
			listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_Category()));
			
			Pos4usDocumentManager pdm = new Pos4usDocumentManager("category", listParameter);
			NodeList nodeCate = pdm.getXmlNodeListByHttpPost();
			
			if(nodeCate != null) {
				
				init();
				
				ResourceManager.put(Id.getCateXmlNodeId(),nodeCate);
				
				nCateSize = nodeCate.getLength();
				
				for(int i=0; i<nCateSize; i++) {
					
					Node nodeChild = nodeCate.item(i);
					
					String strDbId = nodeChild.getAttributes().getNamedItem("id").getNodeValue().trim();
					int nResourceId = IdManager.getCategoryId(i);
					String strNameEng = nodeChild.getChildNodes().item(0).getTextContent().trim();
					String strNameOth = nodeChild.getChildNodes().item(1).getTextContent().trim();
					
					mapCategory.put(nResourceId,new Category(Integer.valueOf(strDbId).intValue(), nResourceId, strNameEng, strNameOth));
				}
			
				return true;
				
			} else {
				return false;
				
			}
		}
		
		return false;
	}
	
	public static final Map<Integer,Category> getCategoryMap() {
		return mapCategory;
	}
	
	public static final String getCategoryNameByResourceId (int nCateResourceId) {
		
		String strName = "";
		Category cate = mapCategory.get(nCateResourceId);
		
		if(Property.MENU_LANGUAGE.equals(Property.MenuLanguage.ENGLISH)) strName = cate.NAME_ENG;
		else strName = cate.NAME_OTH;
		
		return strName;
	}
	
	public static final int getSize() { return nCateSize; }
	
	public static final String[] getCategoryNames() {
		
		String[] strCategoryNames = new String[nCateSize];
		
		if(Property.MENU_LANGUAGE.equals(Property.MenuLanguage.ENGLISH)) {
			for(int i=0; i<nCateSize; i++) {
				int nCateId = IdManager.getCategoryId(i); 
				strCategoryNames[i] = new String(mapCategory.get(nCateId).NAME_ENG);
			}
		}else {
			for(int i=0; i<nCateSize; i++) {
				int nCateId = IdManager.getCategoryId(i);
				strCategoryNames[i] = new String(mapCategory.get(nCateId).NAME_OTH);
			}
		}
	
		return strCategoryNames;
	}
	
	public static void init() {
		if(mapCategory == null) mapCategory = new Hashtable<Integer,Category>();
		else clear();
	}
	public static void clear() {
		mapCategory.clear();
		nCateSize = 0;
	}
	public static void kill() {
		if(mapCategory != null) {
			clear();
			mapCategory = null;
		}
	}
	
}

