package morning.v1.POS4US.Manager.System;

import java.net.InetAddress;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/*
 * Connection Manager
 */
public class ConnectionManager {
	
	// check server ip
	public boolean IsServerAccessible() {
	
		InetAddress in = null;
		
		try {
		    in = InetAddress.getByName(ServerProperty.getServerIp());
		    if (in.isReachable(ServerProperty.getWifiWaitingTime())) {
		    	 return true;
		    } 
		} catch(Exception e) {
		   return false;
		}
		return false;
	}
	
	// check local network
	public boolean IsWifiAvailable(Context context) {
		return (IsWifiConnected(context) || (Property.CURRENT_APP_TYPE == Property.APP_TYPE_EMULATOR));
	}
	
	// check wifi connection
	private boolean IsWifiConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connMgr != null) {
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if ( (networkInfo != null) && (networkInfo.isAvailable() == true) ) {
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					if ( (networkInfo.getState() == State.CONNECTED) || (networkInfo.getState() == State.CONNECTING) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
