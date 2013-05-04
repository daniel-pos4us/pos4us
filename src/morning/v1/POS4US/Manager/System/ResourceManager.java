package morning.v1.POS4US.Manager.System;

import java.util.Hashtable;
import java.util.Map;

/*
 * Keep All Resource Instance : Because need to update real time data
 */
public class ResourceManager {
	
	// <Key,Value>
	
	private static Map<Integer,Object> _IdBasedResource = new Hashtable<Integer,Object>();
	private static Map<Object,Object> _ObjectBasedResource = new Hashtable<Object,Object>();
	
	public static void put (int id, Object object) { _IdBasedResource.put(id, object); }
	public static final Object get (int id) { return _IdBasedResource.get(id); }
	public static boolean exist (int id) { return (_IdBasedResource.get(id)!=null); }
	
	public static void put (Object lhs, Object rhs) { _ObjectBasedResource.put(lhs, rhs); }
	public static final Object get (Object key) { return _ObjectBasedResource.get(key); }
	public static boolean exist (Object key) { return (_ObjectBasedResource.get(key)!=null); }
	
	public static void init() {
		if(_IdBasedResource == null) _IdBasedResource = new Hashtable<Integer,Object>();
		else _IdBasedResource.clear(); 
		if(_ObjectBasedResource == null) _ObjectBasedResource = new Hashtable<Object,Object>();
		else _ObjectBasedResource.clear(); 
	}
	public static void clear() { 
		if(_IdBasedResource != null) _IdBasedResource.clear(); 
		if(_ObjectBasedResource != null) _ObjectBasedResource.clear(); 
	}
	public static void kill() { 
		if(_IdBasedResource != null) {
			_IdBasedResource.clear(); 
			_IdBasedResource = null; 
		}
		if(_ObjectBasedResource != null) {
			_ObjectBasedResource.clear();
			_ObjectBasedResource = null; 
		}
	}

}
