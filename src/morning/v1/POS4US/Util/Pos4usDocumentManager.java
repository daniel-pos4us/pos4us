package morning.v1.POS4US.Util;

import java.util.List;

import morning.v1.POS4US.Configuration.ServerProperty;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

/*
 * Take XML Data
 */
public class Pos4usDocumentManager extends DocumentManager {

	public Pos4usDocumentManager(String strNodeName, List<BasicNameValuePair> listParameter) {
		super(strNodeName, listParameter);
	}

	// get xml nodelist by 'post' method via http
	public NodeList getXmlNodeListByHttpPost() {
		return super.getXmlNodeListByHttpPost( 
			ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent() 
		);
	}
	
	// get xml nodelist by 'post' method via http
	public NodeList getXmlNodeListByHttpGet() {
		return super.getXmlNodeListByHttpGet( 
			ServerProperty.getServerUrl() + ServerProperty.getServerQueryAgent() 
		);
	}
}
