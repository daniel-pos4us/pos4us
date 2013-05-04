package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.DataObject.Order;
import morning.v1.POS4US.Util.DocumentManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

/*
 * Manage Order Board
 */
public class OrderBoardManager {
	
	private static ArrayList<Order> _OrderList = new ArrayList<Order>();
	
	public static int getListSize() { return _OrderList.size(); }
	public static void clear() { _OrderList.clear(); }
	public static boolean isEmpty() { return _OrderList.isEmpty(); }
	public static final ArrayList<Order> getList() { return _OrderList; }
	
	public static ArrayList<Order> getTableOrderList(int nHallNo, int nTableNo) {

		 _OrderList.clear();
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_TableOrderList()));
		listParameter.add(new BasicNameValuePair("hall_no",String.valueOf(nHallNo+1)));
		listParameter.add(new BasicNameValuePair("table_no",String.valueOf(nTableNo+1)));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("order", listParameter);
		NodeList nlOrderList = pdm.getXmlNodeListByHttpPost();
		
		if(nlOrderList != null) {
			
			for(int i=0; i<nlOrderList.getLength(); i++) {
				
				String strId = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("id").getNodeValue(),"0");
				String strOrderId = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("order_id").getNodeValue(),"0");
				String strRelId = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("rel_id").getNodeValue(),"0");
				String strOptionCommonId = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("option_common_id").getNodeValue());
				String strOptionSpecificId = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("option_specific_id").getNodeValue());
				String strCateId = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("cate_id").getNodeValue());
				String strMenuId = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("menu_id").getNodeValue());
				String strSubtotalPrice = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("subtotal_price").getNodeValue(),"0");
				String strOrderCount = StringUtil.notNull(nlOrderList.item(i).getAttributes().getNamedItem("order_count").getNodeValue(),"1");
				String strNameEng = StringUtil.notNull(nlOrderList.item(i).getChildNodes().item(0).getTextContent());
				String strNameOth = StringUtil.notNull(nlOrderList.item(i).getChildNodes().item(1).getTextContent());
				
				int nId = Integer.valueOf(strId);
				int nOrderId = Integer.valueOf(strOrderId);
				int nRelId = Integer.valueOf(strRelId);
				int nOptionCommonId = Integer.valueOf(strOptionCommonId);
				int nOptionSpecificId = Integer.valueOf(strOptionSpecificId);
				int nCateId = Integer.valueOf(strCateId);
				int nMenuId = Integer.valueOf(strMenuId);
				int nOrderCount = Integer.valueOf(strOrderCount);
				
				float fSubtotalPrice = Float.valueOf(strSubtotalPrice);
				
				_OrderList.add(new Order(nId, nOrderId, nRelId, nOptionCommonId, nOptionSpecificId, nHallNo, nTableNo, nCateId, nMenuId, 
						strNameEng, strNameOth, fSubtotalPrice, nOrderCount));
			}
		}
		
		return _OrderList;
	}
}
