package morning.v1.POS4US.Manager.System;

import android.app.Activity;
import android.content.Intent;

/*
 * Call Another Activity: Stage or Layout.
 */
public class ActivityManager {
	
	private static Activity theActivity = null;
	
	public static void call(Activity Caller, @SuppressWarnings("rawtypes") Class Callee) {
		Intent intent = new Intent(Caller, Callee);
		Caller.startActivity(intent);
	}
	
	public static void setActivity(Activity activity) {
		theActivity = activity;
	}
	
	public static Activity getActivity() {
		return theActivity;
	}
}
