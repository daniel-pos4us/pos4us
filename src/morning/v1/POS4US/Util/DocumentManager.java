package morning.v1.POS4US.Util;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Take XML Data
 */
abstract public class DocumentManager {
	
	protected String strNodeName;
	protected List<BasicNameValuePair> listParameter;
	
	public DocumentManager(String strNodeName) {
		this.strNodeName = strNodeName;
	}
	
	public DocumentManager(String strNodeName, List<BasicNameValuePair> listParameter) {
		this.strNodeName = strNodeName;
		this.listParameter = listParameter;
	}
	
	public NodeList getXmlNodeListByHttpPost(String strHttpUrl) {
		if(this.strNodeName != null && this.listParameter != null) {
			return this.getXmlNodeListByHttpPost(
				this.strNodeName, 
				strHttpUrl,
				this.listParameter);
		}
		return null;
	}
	
	public NodeList getXmlNodeListByHttpGet(String strHttpUrl) {
		if(strNodeName != null) {
			return this.getXmlNodeListByHttpGet(
				this.strNodeName, 
				strHttpUrl
			);
		}
		return null;
	}
	
	// get xml nodelist by 'post' method via http
	protected NodeList getXmlNodeListByHttpPost(String strNodeName, String strHttpUrl, List<BasicNameValuePair> listParameter) {

		Document document = XMLManager.getHttpPostDocument(strHttpUrl, listParameter);

		if(document!=null)	
			return document.getElementsByTagName(strNodeName);
		return null;
	}
	
	// get xml nodelist by 'get' method via http
	protected NodeList getXmlNodeListByHttpGet(String strNodeName, String strHttpUrl) {

		Document document = XMLManager.getHttpGetDocument( strHttpUrl);

		if(document!=null)	
			return document.getElementsByTagName(strNodeName);
		return null;
	}
	
	// get value of attribute of node
	public String getAttrValue(String strAttrName, int nIndex, NodeList myNodeList) {
		
		String strRetVal = "";
		Node myNode = myNodeList.item(nIndex);
		
		if(myNode != null) {
			Node myItem = myNode.getAttributes().getNamedItem(strAttrName);
			if(myItem != null) {
				strRetVal = myItem.getNodeValue();
			}
		}
		return strRetVal;
	}
		
}
