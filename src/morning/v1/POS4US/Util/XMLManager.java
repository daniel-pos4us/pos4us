package morning.v1.POS4US.Util;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import morning.v1.POS4US.Configuration.ErrorMsg;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;

/*
 * Manage XML Document
 */
public class XMLManager {

	// get attribute value in xml resource of Android
	public static String getXmlResourceAttr(String nodeName, String nameAttr, int resource, Activity activity) {

		String url = "";
		XmlResourceParser xrp = activity.getResources().getXml(resource);
		
		int eventType = -1;
		
		try {

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {
					if(xrp.getName().equals(nodeName)) {
				
						url = xrp.getAttributeValue(null,nameAttr);
						break;
					}
				}
				
				eventType = xrp.next();
				
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return url;
	}
	
	// get document of requesting http post
	public static Document getHttpPostDocument(String strHttpUrl, List<BasicNameValuePair> listParameter) {
			return getXMLDocument(HttpManager.requestHttpPost(strHttpUrl, listParameter));
	}
	
	// get document of requesting http get
	public static Document getHttpGetDocument(String strHttpUrl) {
		return getXMLDocument(HttpManager.requestHttpGet(strHttpUrl));
	}
	
	// get document object by response data
	public static Document getXMLDocument(String strResponse) {
		
		try {
			
			if (strResponse != null) {
				if(!strResponse.equals(ErrorMsg.CLIENT_PROTOCOL_EXCEPTION) || 
						!strResponse.equals(ErrorMsg.UNSUPPORTED_ENCODING_EXCEPTION)) {
				
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
			
					if(builder != null)
						return builder.parse(new InputSource(new StringReader(strResponse)));
				}
			}
			
			return null;
			
		} catch (SAXException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ParserConfigurationException e) {
			return null;
		}
		
	}

}
