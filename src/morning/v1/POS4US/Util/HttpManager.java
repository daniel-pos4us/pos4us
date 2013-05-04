package morning.v1.POS4US.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import morning.v1.POS4US.Configuration.ErrorMsg;
import morning.v1.POS4US.Configuration.Property;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/*
 * Manage Http Communication
 */
public class HttpManager {

	private static HttpClient httpClient = new DefaultHttpClient();
	private static HttpGet httpGet = null;
	private static HttpPost httpPost = null;
	
	private static void setTimeOut() {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 1000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 1000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httpClient = new DefaultHttpClient(httpParameters);
	}
	
	// init http post
	public static void openHttpPost (String strHttpUrl) {
		setTimeOut();
		httpPost = new HttpPost(strHttpUrl);
	}
	
	// init http get
	public static void openHttpGet (String strHttpUrl) {
		setTimeOut();
		httpGet = new HttpGet(strHttpUrl);
	}
	
	// request http get, retrieve string
	public static String requestHttpGet (String strHttpUrl) {
		
		openHttpGet (strHttpUrl);
		
		String strResponse = "";
		
		try {
			
			ResponseHandler<String> rh = new BasicResponseHandler();
			strResponse = httpClient.execute(httpGet, rh);
			
		} catch (ClientProtocolException e) {
			return ErrorMsg.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IOException e) {
			return ErrorMsg.IO_EXCEPTION;
		}
		
		return strResponse;
	}

	// request http post with parameters, retrieve string
	public static String requestHttpPost (List<BasicNameValuePair> listParameter) {
		
		String strResponse = "";
		
		try {

			if(httpPost != null) {
				
				ResponseHandler<String> rh = new BasicResponseHandler();
				httpPost.setEntity(new UrlEncodedFormEntity(listParameter,Property.SYSTEM_ENCODING));
				strResponse = httpClient.execute(httpPost,rh);
			}
			
		} catch (UnsupportedEncodingException e) {
			return ErrorMsg.UNSUPPORTED_ENCODING_EXCEPTION;
		} catch (ClientProtocolException e) {
			return ErrorMsg.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IOException e) {
			return ErrorMsg.IO_EXCEPTION;
		}
		
		return strResponse;
	}

	// request http post with url and parameters, retrieve string  
	public static String requestHttpPost(String strHttpUrl, List<BasicNameValuePair> listParameter) {
		
		openHttpPost (strHttpUrl);
		
		String strResponse = "";
		
		try {
			
			ResponseHandler<String> rh = new BasicResponseHandler();
			httpPost.setEntity(new UrlEncodedFormEntity(listParameter,Property.SYSTEM_ENCODING));
			strResponse = httpClient.execute(httpPost,rh);
			
		} catch (UnsupportedEncodingException e) {
			return ErrorMsg.UNSUPPORTED_ENCODING_EXCEPTION;
		} catch (ClientProtocolException e) {
			return ErrorMsg.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IOException e) {
			return ErrorMsg.IO_EXCEPTION;
		}
		
		return strResponse;
	}
	
	// open http connection
	public static InputStream OpenHttpConnection(String strURL, String strMethod) throws IOException{
		
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();
	
		try{
			
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			httpConn.setRequestMethod(strMethod);
			httpConn.connect();
	
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {}
		
		return inputStream;
	}
	
	
}
