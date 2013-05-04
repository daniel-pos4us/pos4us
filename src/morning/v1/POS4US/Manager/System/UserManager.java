package morning.v1.POS4US.Manager.System;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import morning.v1.POS4US.Configuration.ErrorMsg;
import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.Manager.General.TitleBarManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

/*
 * User Manager
 */
public class UserManager {
	
	// Session Storage
	private static Map<String,String> Session = new Hashtable<String,String>();
	
	// Get User
	public static String getSessionUser() {
		if(!Session.isEmpty()) return Session.get(Property.SESSION_TOKEN);
		return "Anonymous";
	}
	
	// Initialize
	public static void initSesison() { 
		if(Session == null) Session= new Hashtable<String,String>();
		else Session.clear();
	}
	
	// Clear Session Storage
	public static void clearSession() { Session.clear(); }
	public static void kill() {
		clearSession();
		Session = null;
	}

	// Process User Login
	public static int login(String strId, String strPassword) {

		int nResult = 0;
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
				
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_Login()));
		listParameter.add(new BasicNameValuePair("user_id", strId));
		listParameter.add(new BasicNameValuePair("user_password", strPassword));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("exist", listParameter);
		NodeList node = pdm.getXmlNodeListByHttpPost();
		
		if(node != null) {
			if(node.item(0) != null) {
		
				nResult = Integer.valueOf(StringUtil.notNull(node.item(0).getTextContent(),"0"));
				
				if(nResult == 1) {
					initSesison();
					TitleBarManager.setUserId(strId);
					Session.put(Property.SESSION_TOKEN,strId);
				}
			} else {
				return ErrorMsg.DEAD_SERVER_IP_OR_STATUS;
			}
		} else {
			return ErrorMsg.DEAD_SERVER_IP_OR_STATUS;
		}
		
		return nResult;
	}

}
