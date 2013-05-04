package morning.v1.POS4US.Manager.General;

import java.util.Hashtable;
import java.util.Map;

import morning.v1.POS4US.R;
import morning.v1.POS4US.Manager.System.ResourceManager;

import android.widget.Button;
import android.widget.TextView;

/*
 * Manage Table
 */
public class TableManager {

	private static Map<Integer,Button> mapTable = new Hashtable<Integer,Button>();
	
	public static void putTable(int nTableResourceId, Button bResource) { mapTable.put(nTableResourceId, bResource); }
	public static Button getTable(int nTableResourceId) { return mapTable.get(nTableResourceId); }
	
	public static void clearBackground() {
		
		Integer key[] = mapTable.keySet().toArray(new Integer[mapTable.size()]);
		
		for(int i=0;i<mapTable.size();i++) {
			
			int nTableResourceId = key[i];

			Button table = (Button)mapTable.get(key[i].intValue());
			table.setBackgroundResource(R.drawable.color_table_empty);

			TableStatusManager.setStatus(nTableResourceId,TableStatusManager.EMPTY);
	
			TextView tvTableOrderCount = (TextView) ResourceManager.get(table);
			tvTableOrderCount.setText("0/0");
			
		}
	}
	
	public static void init() {
		if(mapTable == null) mapTable = new Hashtable<Integer,Button>();
		else mapTable.clear();
	}
	
	public static void clear() {
		if(mapTable != null) mapTable.clear();
	}
	
	public static void kill() {
		if(mapTable != null) {
			mapTable.clear();
			mapTable = null;
		}
	}
}
