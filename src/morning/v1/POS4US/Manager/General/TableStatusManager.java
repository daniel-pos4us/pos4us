package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import morning.v1.POS4US.R;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.HttpManager;
import morning.v1.POS4US.Util.StringUtil;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.widget.Button;
import android.widget.TextView;

/*
 * Manage Table Order Status
 */
public class TableStatusManager {
	
	// Temporary Storage
	private static Map<Integer,Integer> mapTableStatus = new Hashtable<Integer,Integer>();
	
	public static final int EMPTY = 1;
	public static final int ORDERED = 2;
	public static final int COMPLETED = 3;
	public static final int RESERVED = 4;
	
	// clear storage
	public static void clearStatus() {
		Integer key[] = mapTableStatus.keySet().toArray(new Integer[mapTableStatus.size()]);
		for(int i=0; i<mapTableStatus.size(); i++) {
			mapTableStatus.put(key[i], EMPTY);
		}
	}
	
	// set status
	public static void setStatus(int nTableResourceId, int nTableStatus) {
		mapTableStatus.put(Integer.valueOf(nTableResourceId), Integer.valueOf(nTableStatus));
	}
	
	// get status
	public static int getStatus(int nTableResourceId) {
		int nStatus = -1;
		Integer Status = mapTableStatus.get(Integer.valueOf(nTableResourceId));
		if (Status != null) 
			nStatus = Status.intValue();
		return nStatus;
	}
	
	// change status of DB
	public static void changeStatus(int nHallNum, int nTableNum, String strStatus) {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		
		listParameter.add(new BasicNameValuePair("path",ServerProperty.getQuery_OrderStatusUpdate()));
		listParameter.add(new BasicNameValuePair("hall_no",String.valueOf(nHallNum+1)));
		listParameter.add(new BasicNameValuePair("table_no",String.valueOf(nTableNum+1)));
		listParameter.add(new BasicNameValuePair("status",strStatus));
		
		HttpManager.openHttpPost(ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent());
		HttpManager.requestHttpPost(listParameter);	
	}
	
	// update status by server data
	public static void updateStatus() {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_TableOrderCount()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("table-order", listParameter);
		NodeList TableOrderCountList = pdm.getXmlNodeListByHttpPost();
		
		int nListCount = 0;
		
		if(TableOrderCountList != null) {
			
			TableManager.clearBackground();
			
			nListCount = TableOrderCountList.getLength();
			
			for(int i=0; i<nListCount; i++) {
				
				Node nodeChild = TableOrderCountList.item(i);
				
				String strHallNo = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("hall_no").getNodeValue());
				String strTableNo = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("table_no").getNodeValue());
				String strOrderedCount = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("ordered_count").getNodeValue());
				String strCompletedCount = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("completed_count").getNodeValue());
				String strReserved = StringUtil.notNull(nodeChild.getAttributes().getNamedItem("reserved").getNodeValue());
				
				int nHallNo = Integer.valueOf(strHallNo) - 1;
				int nTableNo = Integer.valueOf(strTableNo) - 1;
				int nOrderedCount = Integer.valueOf(strOrderedCount);
				int nCompletedCount = Integer.valueOf(strCompletedCount);
				int nReserved = Integer.valueOf(strReserved);
				
				int nTableResourceId = IdManager.getTableId(nHallNo, nTableNo); 
				
				Button table = (Button)ResourceManager.get(nTableResourceId);
				TextView tvTableOrderCount = (TextView) ResourceManager.get(table);
				tvTableOrderCount.setText(String.valueOf(nOrderedCount + "/" + (nOrderedCount+nCompletedCount)));
				
				if (table != null) {
					if (nOrderedCount > 0 || nCompletedCount > 0) {
						table.setBackgroundResource(R.drawable.color_table_order);
						TableStatusManager.setStatus(nTableResourceId,TableStatusManager.ORDERED);
					} else {
						if (nReserved > 0) {
							table.setBackgroundResource(R.drawable.color_table_reserved);
							TableStatusManager.setStatus(nTableResourceId,TableStatusManager.RESERVED);
						}
					}
				}
				
			}
		}
	}
	
	public static void init() {
		if(mapTableStatus == null) mapTableStatus = new Hashtable<Integer,Integer>();
		else mapTableStatus.clear();
	}
	
	public static void clear() {
		if(mapTableStatus != null) mapTableStatus.clear();
	}
	
	public static void kill() {
		if(mapTableStatus != null) {
			mapTableStatus.clear();
			mapTableStatus = null;
		}
	}

}
