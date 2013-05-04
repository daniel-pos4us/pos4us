package morning.v1.POS4US.Manager.System;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

import morning.v1.POS4US.Configuration.ErrorMsg;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.Util.DocumentManager;
import morning.v1.POS4US.Util.FileManager;
import morning.v1.POS4US.Util.Pos4usDocumentManager;
import morning.v1.POS4US.Util.StringUtil;
import android.content.Context;

/*
 * Permission Manager to install and access this application
 */
public class PermissionManager {

	private Context context = null;
	private DeviceManager deviceManager = null;
	private FileManager fileManager = null;
	
	public PermissionManager(){}
	public PermissionManager(Context context) { 
		this.context = context; 
		if(deviceManager == null) {
			deviceManager = new DeviceManager(this.context);
		}
		if(fileManager == null) {
			fileManager = new FileManager(this.context);
		}
	}
	
	// get permission of access to the app
	public boolean haveAccessPermission() {
		
		boolean bAccessPermission = false;
		String strDeviceIMEIFromFile = StringUtil.notNull(
			fileManager.readString(
					ServerProperty.getStorageNameOfDeviceImei()
			));
		if(ErrorMsg.FILE_NOT_FOUND.equals(strDeviceIMEIFromFile)) {
			if(this.haveInstallationPermission()) {
				fileManager.writeString(deviceManager.getIMEI(), ServerProperty.getStorageNameOfDeviceImei());
				bAccessPermission = true;
			}
		}else{
			if(strDeviceIMEIFromFile.equals(deviceManager.getIMEI())) {
				bAccessPermission = true;
			}
		}
		return bAccessPermission;
	}
	
	/*
	 *  get a installation permission from db : COMSET_MST
	 *  
	 *  value - 1: permit, 0: prohibit
	 */	
	public boolean haveInstallationPermission() {
		
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("path", ServerProperty.getQuery_Installation()));
		
		Pos4usDocumentManager pdm = new Pos4usDocumentManager("apps", listParameter);
		NodeList nlPermission = pdm.getXmlNodeListByHttpPost();
		
		if(nlPermission != null) {
			String strDbId = null;
			if(nlPermission.item(0) != null) {
				strDbId = StringUtil.notNull(nlPermission.item(0).getAttributes().getNamedItem("installation").getNodeValue(),"0");
			}
			if("1".equals(strDbId)) {
				return true;
			}
		}
		return false;
	}
}
