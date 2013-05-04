package morning.v1.POS4US.Manager.System;

import java.util.Stack;

/*
 * For blocking to reload data from the server : for the best performance of the application
 */
public class NavigationManager {
	/*
	 * Page Navigation: Caller
	 */
	public static final int NULL = 0;
	public static final int NEXT = 1;
	public static final int PREVIOUS = 2;
	
	private static Stack<Integer> _Stack = new Stack<Integer>();
	
	public static void setCaller(int caller) {	
		_Stack.push(Integer.valueOf(caller));
	}
	
	public static int getCaller() {
		if(!_Stack.isEmpty())
			return _Stack.pop().intValue();
		return NULL;
	}
	
	public static boolean isCalledByPrevious() {
		if(!_Stack.isEmpty()) {
			int nPop = _Stack.pop().intValue();
			if(nPop == PREVIOUS)
				return true;
			else 
				return false;
		}
		return false;
	}
	
	public static boolean isCalledByNext() {
		if(!_Stack.isEmpty()) {
			int nPop = _Stack.pop().intValue();
			if(nPop == NEXT)
				return true;
			else 
				return false;
		}
		return false;
	}
	
	public static void clear() {
		_Stack.clear();
	}
	
	public static void kill() {
		if(_Stack != null) {
			_Stack.clear();
			_Stack = null;
		}
	}

}
