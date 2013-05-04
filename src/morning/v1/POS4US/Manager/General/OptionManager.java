package morning.v1.POS4US.Manager.General;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.DataObject.Id;
import morning.v1.POS4US.DataObject.Option;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Util.DocumentManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;

/*
 * Manage Option
 */
public class OptionManager {
	
	private static ArrayList<Option> _OptionCommonList = new ArrayList<Option>();
	private static ArrayList<Option> _OptionSpecificList = new ArrayList<Option>();

	public static int getCommonOptionSize() { return _OptionCommonList.size(); }
	
	public final static ArrayList<Option> getCommonOptionList() { return _OptionCommonList; }
	public final static ArrayList<Option> getSpecificOptionList(int nMenuDbId) { 
		ArrayList<Option> Newlist = new ArrayList<Option>();
		for (int i=0; i<_OptionSpecificList.size(); i++) {
			if(_OptionSpecificList.get(i).MENU_DB_ID == nMenuDbId) {
				Newlist.add(_OptionSpecificList.get(i));
			}
		}
		return Newlist;
	}
	public final static Option getCommonOption(int nIndex) { return _OptionCommonList.get(nIndex); }
	public final static Option getCommonOptionByDbId(int nCommonOptionDbId) { 
		for (int i=0; i<_OptionCommonList.size(); i++) {
			if(_OptionCommonList.get(i).DB_ID == nCommonOptionDbId) {
				return _OptionCommonList.get(i);
			}
		}
		return null;
	}
	public final static Option getSpecificOptionByDbId(int nSpecificOptionDbId) { 

		for (int i=0; i<_OptionSpecificList.size(); i++) {
			if(_OptionSpecificList.get(i).DB_ID == nSpecificOptionDbId) {
				return _OptionSpecificList.get(i);
			}
		}
		return null;
	}
	
	// load common option
	public static void loadOptionCommon() {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OptionCommon()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("option-common", listParameter);
		NodeList nlOptionCommon = pdm.getXmlNodeListByHttpPost();

		if(nlOptionCommon != null) {
			
			ResourceManager.put(Id.getOptionCommonXmlNodeId(),nlOptionCommon);
			
			int nOptionCommonSize = nlOptionCommon.getLength();
			
			for(int i=0; i<nOptionCommonSize; i++) {
				
				String strDbId = StringUtil.notNull(nlOptionCommon.item(i).getAttributes().getNamedItem("id").getNodeValue());
				int nMenuResourceId = 0;
				String strNameEng = StringUtil.notNull(nlOptionCommon.item(i).getChildNodes().item(0).getTextContent());
				String strNameOth =StringUtil.notNull(nlOptionCommon.item(i).getChildNodes().item(1).getTextContent());
				
				_OptionCommonList.add(new Option(Integer.valueOf(strDbId).intValue(), nMenuResourceId,  0, strNameEng, strNameOth, 0, 0, 0));
			}
		}
	}
	
	// load specific option
	public static void loadOptionSpecific() {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_OptionSpecific()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("option-specific", listParameter);
		NodeList nlOptionSpecific = pdm.getXmlNodeListByHttpPost();

		if(nlOptionSpecific != null) {
			
			ResourceManager.put(Id.getOptionSpecificXmlNodeId(),nlOptionSpecific);
			
			int nOptionSpecificSize = nlOptionSpecific.getLength();
			
			for(int i=0; i<nOptionSpecificSize; i++) {
				
				String strDbId = StringUtil.notNull(nlOptionSpecific.item(i).getAttributes().getNamedItem("id").getNodeValue());
				int nMenuResourceId = 0;
				String strMenuDbId = StringUtil.notNull(nlOptionSpecific.item(i).getAttributes().getNamedItem("menu_id").getNodeValue());
				String strNameEng = StringUtil.notNull(nlOptionSpecific.item(i).getChildNodes().item(0).getTextContent());
				String strNameOth = StringUtil.notNull(nlOptionSpecific.item(i).getChildNodes().item(1).getTextContent());
				String strGST = StringUtil.notNull(nlOptionSpecific.item(i).getAttributes().getNamedItem("gst").getNodeValue());
				String strPrice = StringUtil.notNull(nlOptionSpecific.item(i).getAttributes().getNamedItem("price").getNodeValue());
				float fGST = Float.valueOf(strGST);
				float fPrice = Float.valueOf(strPrice);
				
				_OptionSpecificList.add(new Option(Integer.valueOf(strDbId).intValue(), nMenuResourceId, Integer.valueOf(strMenuDbId).intValue(), strNameEng, strNameOth,fGST,fPrice,0));
			}
			
		}
	}
	
}
