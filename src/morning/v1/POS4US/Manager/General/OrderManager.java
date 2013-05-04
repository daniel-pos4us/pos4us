package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.DataObject.Option;
import morning.v1.POS4US.DataObject.Order;
import morning.v1.POS4US.Manager.System.UserManager;
import morning.v1.POS4US.Util.HttpManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

/*
 * Manage Order
 */
public class OrderManager {
	
	private static Order _Order = new Order();
	private static ArrayList<Option> _OptionList = new ArrayList<Option>();
	
	public static void setId(int nId) { _Order.ID = nId; }
	public static void setOrderId(int nOrderId) { _Order.ORDER_ID = nOrderId; }
	public static void setRelId(int nRelId) { _Order.REL_ID = nRelId; }
	public static void setOptionCommonId(int nOptionCommonId) { _Order.OPTION_COMMON_ID = nOptionCommonId; }
	public static void setOptionSpecificId(int nOptionSpecificId) { _Order.OPTION_SPECIFIC_ID = nOptionSpecificId; }
	public static void setHallNo(int nHallNo) { _Order.HALL_NO = nHallNo; }
	public static void setTableNo(int nTableNo) { _Order.TABLE_NO = nTableNo; }
	public static void setCateId(int nCateId) { _Order.CATE_ID = nCateId; }
	public static void setMenuId(int nMenuId) { _Order.MENU_ID = nMenuId; }
	public static void setGst(float fGst) { _Order.GST = fGst; }
	public static void setPrice(float fPrice) { _Order.PRICE = fPrice; }
	public static void setOrderCount(int nOrderCount) { _Order.ORDER_COUNT = nOrderCount; }
	public static void setHandlingSection1(int nHandlingSection1) { _Order.HANDLING_SECTION1 = nHandlingSection1; }
	public static void setHandlingSection2(int nHandlingSection2) { _Order.HANDLING_SECTION2 = nHandlingSection2; }
	public static void setHandlingSection3(int nHandlingSection3) { _Order.HANDLING_SECTION3 = nHandlingSection3; }
	public static void setHandlingSection4(int nHandlingSection4) { _Order.HANDLING_SECTION4 = nHandlingSection4; }
	
	public static void setReciptId(String strReciptId) { _Order.RECEIPT_ID = strReciptId; }
	
	public final static int getId() { return _Order.ID; }
	public final static int getOrderId() { return _Order.ORDER_ID; }
	public final static int getRelId() { return _Order.REL_ID; }
	public final static int getHallNo() { return _Order.HALL_NO; }
	public final static int getTableNo() { return _Order.TABLE_NO; }
	public final static int getMenuId() { return _Order.MENU_ID; }
	public final static float getGst() { return _Order.GST; }
	public final static float getPrice() { return _Order.PRICE; }
	
	// get 'order id' and 'next relevant id'
	public static ArrayList<String> getOrderId_RelNextId(int nHallNo, int nTableNo) {
		
		ArrayList<String> alId = new ArrayList<String>();
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderIdNextRelId()));
		listParameter.add(new BasicNameValuePair("hall_no",String.valueOf(nHallNo+1)));
		listParameter.add(new BasicNameValuePair("table_no",String.valueOf(nTableNo+1)));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("id", listParameter);
		NodeList nlOrderList = pdm.getXmlNodeListByHttpPost();
		
		if(nlOrderList != null) {
			String strOrderId = StringUtil.notNull(nlOrderList.item(0).getAttributes().getNamedItem("order_id").getNodeValue().trim(),"1");
			String strNextRelId = StringUtil.notNull(nlOrderList.item(0).getAttributes().getNamedItem("next_rel_id").getNodeValue().trim(),"1");
			
			alId.add(strOrderId);
			alId.add(strNextRelId);
		}
		
		return alId;
	}
	
	// get 'order id' and 'next relevant id'
	public static String getNextRelId(Order currentOrder) {
		
		String strNextRelId = "1";
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderNextRelId()));
		listParameter.add(new BasicNameValuePair("hall_no",String.valueOf(currentOrder.HALL_NO + 1)));
		listParameter.add(new BasicNameValuePair("table_no",String.valueOf(currentOrder.TABLE_NO + 1)));
		listParameter.add(new BasicNameValuePair("order_id",String.valueOf(currentOrder.ORDER_ID)));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("id", listParameter);
		NodeList nlOrderList = pdm.getXmlNodeListByHttpPost();
		
		if(nlOrderList != null) {
			strNextRelId = StringUtil.notNull(nlOrderList.item(0).getAttributes().getNamedItem("next_rel_id").getNodeValue().trim());
		}
		
		return strNextRelId;
	}
	
	// Cancel Order
	public static void cancelOrder(Order order) {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderCancel()));
		listParameter.add(new BasicNameValuePair("id",String.valueOf(order.ID)));
		listParameter.add(new BasicNameValuePair("order_id",String.valueOf(order.ORDER_ID)));
		listParameter.add(new BasicNameValuePair("rel_id",String.valueOf(order.REL_ID)));

		HttpManager.requestHttpPost(ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent(), listParameter);
	}

	// Remove Unconfirmed Order
	public static void removeUnconfirmedOrder(int nOrderId) {
	
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderRemoveUnconfirmed()));
		listParameter.add(new BasicNameValuePair("order_id",String.valueOf(nOrderId)));
	
		HttpManager.requestHttpPost(ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent(), listParameter);

	}
	
	// Update Order Option
	public static void updateOrderOption(int nId) {
			
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderOptionUpdate()));
		listParameter.add(new BasicNameValuePair("id",String.valueOf(nId)));
	
		HttpManager.requestHttpPost(ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent(), listParameter);
	}
	
	// Confirm Order
	public static void confirmOrder(int nOrderDbId) {
			
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderConfirm()));
		listParameter.add(new BasicNameValuePair("order_id",String.valueOf(nOrderDbId)));
	
		HttpManager.requestHttpPost(ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent(), listParameter);
	}

	// Complete Order Rel
	public static void completeOrderRel(int nOrderDbId, int nRelDbId) {
			
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderCompleteRel()));
		listParameter.add(new BasicNameValuePair("order_id",String.valueOf(nOrderDbId)));
		listParameter.add(new BasicNameValuePair("rel_id",String.valueOf(nRelDbId)));
		
		HttpManager.requestHttpPost(ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent(), listParameter);
	}
	
	// make an order
	public static void requestUpdateMenu() { 
		
		int nMenuId = OrderManager.getMenuId();
		int nHallNo = OrderManager.getHallNo();
		int nTableNo = OrderManager.getTableNo();
		String userId = UserManager.getSessionUser();
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderNewLite()));
		listParameter.add(new BasicNameValuePair("type",String.valueOf(Property.ORDER_TYPE_MENU)));
		listParameter.add(new BasicNameValuePair("menu_id",String.valueOf(nMenuId)));
		listParameter.add(new BasicNameValuePair("hall_no",String.valueOf(nHallNo)));
		listParameter.add(new BasicNameValuePair("table_no",String.valueOf(nTableNo)));
		listParameter.add(new BasicNameValuePair("user_id",userId));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("order", listParameter);
		NodeList nodeList = pdm.getXmlNodeListByHttpPost();
		
		if(nodeList != null) {
			Node nodeChild = nodeList.item(0);
			String strOrderId = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("order_id").getNodeValue(),"0");
			String strRelId = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("rel_id").getNodeValue(),"0");
			OrderManager.setOrderId(Integer.parseInt(strOrderId));
			OrderManager.setRelId(Integer.parseInt(strRelId));
		}
	}
	
	// add options to an order
	public static void requestUpdateOptions(Order currentOrder, ArrayList<Order> orderAddedOptions) { 
		
		JSONArray jsonArray = new JSONArray();
		int nBlockToCreateNewOrder = 0;
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OrderNewLite()));
		listParameter.add(new BasicNameValuePair("type",String.valueOf(Property.ORDER_TYPE_OPTION)));
		listParameter.add(new BasicNameValuePair("id",String.valueOf(currentOrder.ID)));
		listParameter.add(new BasicNameValuePair("order_id",String.valueOf(currentOrder.ORDER_ID)));
		listParameter.add(new BasicNameValuePair("rel_id",String.valueOf(currentOrder.REL_ID)));
		listParameter.add(new BasicNameValuePair("next_rel_id",getNextRelId(currentOrder)));
		
		for(int i=0; i<orderAddedOptions.size(); i++) {
			if(i > 0) {
				nBlockToCreateNewOrder = 1;
			}
			Order currentOrderAddedOptions = (Order)orderAddedOptions.get(i);

			try {
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("menu_id", currentOrderAddedOptions.MENU_ID);
				jsonObject.put("option_common_id", currentOrderAddedOptions.OPTION_COMMON_ID);
				jsonObject.put("option_specific_id", currentOrderAddedOptions.OPTION_SPECIFIC_ID);
				jsonObject.put("order_count", currentOrderAddedOptions.ORDER_COUNT);
				jsonObject.put("_block_to_create_new_order", nBlockToCreateNewOrder);
				jsonArray.put(jsonObject);
				
			} catch (JSONException e) {}
		}
		listParameter.add(new BasicNameValuePair("options_json_array",jsonArray.toString()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("order", listParameter);
		pdm.getXmlNodeListByHttpPost();
	}

	public static ArrayList<Option> getOptionList() {
		return _OptionList;
	}

	public static void setOptionCount (int nOptionDbId, int nCount) {
		for(int i=0; i<_OptionList.size(); i++) {
			Option _Option = _OptionList.get(i);
			if(_Option.DB_ID == nOptionDbId) {
				_Option.ORDER_COUNT = nCount;
				break;
			}
		}
	}
	public static void clearOption() {
		for(int i=0; i<_OptionList.size(); i++) {
			_OptionList.get(i).ORDER_COUNT = 0;
		}
	}
	
	// Load Option List
	public static void loadOptionList() {

		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OptionList()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("option", listParameter);
		NodeList nlOptionList = pdm.getXmlNodeListByHttpPost();
		
		if(nlOptionList != null) {
			for(int i=0; i<nlOptionList.getLength(); i++) {

				String strId = nlOptionList.item(i).getAttributes().getNamedItem("id").getNodeValue().trim();
				String strMenuId = nlOptionList.item(i).getAttributes().getNamedItem("menu_id").getNodeValue().trim();

				_OptionList.add(new Option(Integer.valueOf(strId),0,Integer.valueOf(strMenuId),"","",0,0,0));
			}
		}
	}
	
}
