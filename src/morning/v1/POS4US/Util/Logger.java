package morning.v1.POS4US.Util;

import java.util.ArrayList;
import java.util.List;

import morning.v1.POS4US.Configuration.ServerProperty;

import org.apache.http.message.BasicNameValuePair;

/*
 * Leave Log to the server
 */
public class Logger {

	/* log message to server
	 * 
	 * @param string message to log
	 */
	public static void write(String strMessage) {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path",ServerProperty.getLogURL()));
		listParameter.add(new BasicNameValuePair("message",strMessage));
		HttpManager.requestHttpPost(ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent(), listParameter);
	}
	
	/* log with method url
	 * 
	 * @param string message to log
	 */
	public static void write(String strMethodUrl, String strMessage) {
		write ( "[" + strMethodUrl + "] " + strMessage);
	}
}
