package morning.v1.POS4US.Manager.System;
import android.view.View;

/*
 * Need to remember previous stage for processing data
 */
public class History {
	
	private static View VIEW;
	private static int HALL_RESOURCE_ID;
	private static int TABLE_RESOURCE_ID;
	
	public static void putView(View v) { VIEW = v; }
	public static void putHallResourceId(int nHallResourceId) { HALL_RESOURCE_ID = nHallResourceId; }	
	public static void putTableResourceId(int nTableResourceId) { TABLE_RESOURCE_ID = nTableResourceId; }

	public static final View getView() { return VIEW; }
	public static final int getHallResourceId() { return HALL_RESOURCE_ID; }
	public static final int getTableResourceId() { return TABLE_RESOURCE_ID; }
}
