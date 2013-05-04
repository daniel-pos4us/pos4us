package morning.v1.POS4US.Util;

import android.view.Gravity;
import android.widget.Toast;
import android.content.Context;

/*
 * Show Alert Box
 */
public class MessageBox {
	
	public static void show (String msg, int x, int y, Context context) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT); 
		toast.setGravity(Gravity.TOP|Gravity.LEFT, x, y); 
		toast.show();
	}
	
	public static void show (String msg, int x, int y, int duration, Context context) {
		Toast toast = Toast.makeText(context, msg, duration); 
		toast.setGravity(Gravity.TOP|Gravity.LEFT, x, y); 
		toast.show();
	}
}
