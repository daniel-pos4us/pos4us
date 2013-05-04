package morning.v1.POS4US.Util;

import java.util.Hashtable;
import java.util.Map;

/*
 * Comfy util for saving object & data temporarily
 */
public class Mapper {
	private static Map<Object,Object> _Map = new Hashtable<Object,Object>();
	
	public static void put(Object key, Object value) { if(_Map ==null) init(); _Map.put(key, value); }
	public static Object get(Object key) { if(_Map != null) return _Map.get(key); return null;}
	public static boolean isEmpty() { if(_Map != null) return _Map.isEmpty(); else return false; }
	public static int size() { if(_Map != null) return _Map.size(); return 0;}
	public static void init() { if(_Map == null) _Map = new Hashtable<Object,Object>(); }
	public static void clear() { if(_Map != null) _Map.clear(); }
	public static void kill() { if(_Map != null) { _Map.clear(); _Map = null; }}
}
