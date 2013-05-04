package morning.v1.POS4US.Manager.System;

import java.util.Hashtable;
import java.util.Map;

import morning.v1.POS4US.Configuration.Property;

import android.os.Handler;
import android.os.Message;

public class ThreadManager {
	
	private static int nThreadId = 0xbabe2012;
	private static int nThreadCounter = 0;
	private static Map<Integer,Thread> mapThreads = new Hashtable<Integer,Thread>();
	
	private static boolean bRunnableCondition = true; // to turn on/off running thread
	private static Handler handler;
	
	private static int getId() {
		nThreadCounter++;
		return nThreadId + nThreadCounter;
	}
	
	// for talking with the outside world
	public static void initHandler() {

		handler = new Handler() {
	        public void handleMessage(Message msg) {
	        	LoadManager.loadAutomatically();
	            super.handleMessage(msg);
	        }
		};
	}
	
	// send message to the handler
	public static void callHandler() {
		handler.sendEmptyMessage(0);
	}
	
	public static void runThread(Runnable rRunnable) {
		
		Thread thread = new Thread(rRunnable);
		thread.start();
		
		mapThreads.put(getId(), thread);
	}
	
	public static void stopThread() {
		setCondition(false);
	}
	
	public static void setCondition(boolean bValue) {
		bRunnableCondition = bValue;
	}
	
	public static final boolean getCondition() { 
		return bRunnableCondition;
	}
	
	public static void sleep() {
		try {
			Thread.sleep(Property.RUN_THREAD_EVERY_MILLISEC);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void kill() {
		stopThread();
		mapThreads = null;
	}
}
