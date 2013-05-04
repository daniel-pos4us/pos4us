package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.DataObject.Hall;
import morning.v1.POS4US.DataObject.Id;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Manage Hall
 */
public class HallManager {
	
	private static int nHallSize = 0;
	
	// Hall Map
	private static Map<Integer,Hall> mapHall = new Hashtable<Integer,Hall>();
	
	// get hall map 
	public static final Map<Integer,Hall> getHallMap() { return mapHall; }

	// load hall
	public static boolean loadHall() { 
		
		if (nHallSize == 0) {
		
			List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
			listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_Hall()));
			
			Pos4usDocumentManager pdm = new Pos4usDocumentManager("hall", listParameter);
			NodeList hallNode = pdm.getXmlNodeListByHttpPost();
			
			if(hallNode != null) {
				
				init();
				
				ResourceManager.put(Id.getHallXmlNodeId(),hallNode);
				
				nHallSize = hallNode.getLength();
				
				for(int i=0; i<nHallSize; i++) {
					
					Node nodeChild = hallNode.item(i);
					
					String strDbId = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("id").getNodeValue());
					String strTableNum = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("table_num").getNodeValue());
					int nHallResourceId = Id.getHallId() + i;
					String strNameEng = StringUtil.notNull(nodeChild.getChildNodes().item(0).getTextContent());
					String strNameOth = StringUtil.notNull(nodeChild.getChildNodes().item(1).getTextContent());
					
					mapHall.put(nHallResourceId,new Hall(Integer.valueOf(strDbId).intValue(), nHallResourceId, Integer.valueOf(strTableNum), strNameEng, strNameOth));
				}
				
				return true;
				
			} else {
				return false;
			}
		}
		
		return false;
	}
	
	// get hall name by resource id
	public static String getHallNameByResourceId(int nHallResourceId) {
		
		Hall hall = mapHall.get(nHallResourceId);
		
		if (hall!=null) { 
			
			if(Property.MENU_LANGUAGE.equals(Property.MenuLanguage.ENGLISH)) return hall.NAME_ENG;
			else return hall.NAME_OTH;
		}
		
		return "";
	}

	// get size of hall
	public static final int getSize() { return mapHall.size(); }
	
	// get name list of hall
	public static final String[] getHallNames() {
		
		String[] strHalls = new String[mapHall.size()];
		
		if(Property.MENU_LANGUAGE.equals(Property.MenuLanguage.ENGLISH)) {
			for(int i=0; i<mapHall.size(); i++) {
				strHalls[i] = new String (mapHall.get(Id.getHallId()+i).NAME_ENG);
			}
		}else {
			for(int i=0; i<mapHall.size(); i++) {
				strHalls[i] = new String (mapHall.get(Id.getHallId()+i).NAME_OTH);
			}
		}
		
		return strHalls;
	}
	
	public static void init() {
		if(mapHall == null) mapHall = new Hashtable<Integer,Hall>();
		else clear();
	}
	public static void clear() {
		if(mapHall != null) {
			mapHall.clear();
			nHallSize = 0;
		}
	}
	public static void kill() {
		if(mapHall != null) {
			clear();
			mapHall = null;
		}
	}
	
}
