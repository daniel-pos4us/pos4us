package morning.v1.POS4US.Manager.System;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceManager {
	
	private Context context = null;
	
	public DeviceManager(){}
	public DeviceManager(Context context) { this.context = context; }
	
	// get IMEI
	public String getIMEI () {
		String strIdentifier = null;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null)
			strIdentifier = tm.getDeviceId();
		if (strIdentifier == null || strIdentifier .length() == 0)
			strIdentifier = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
		return strIdentifier;
	}
	
	// get wifi mac address
	public String getWifiMacAddress() {
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiMgr.getConnectionInfo().getMacAddress();
	}
}
