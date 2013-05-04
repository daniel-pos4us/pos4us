package morning.v1.POS4US.Configuration;

/*
 * Server & Query Preperty
 */
public class ServerProperty {
	
	/*
	 * Server Information
	 */
	private static final String PROTOCOL = "http://";
	private static final String SERVER_DIR = "/POS4US_SERVER/";
	private static String SERVER_IP = "192.168.1.101";  // by default
	private static String SERVER_URL = PROTOCOL + SERVER_IP + SERVER_DIR; // by default
	private static String SERVER_QUERY_AGENT = "index.php";
	private static final int WIFI_WAITING_TIME = 1000; // Waiting time to access the server
	
	public static void setServerIp(String strServerIp) { 
		SERVER_IP = strServerIp; 
		SERVER_URL = PROTOCOL + SERVER_IP + SERVER_DIR;
	}
	public static final String getServerIp() { return SERVER_IP; }
	public static final String getServerUrl() { return SERVER_URL; }
	public static final String getServerQueryAgent() { return SERVER_QUERY_AGENT; }
	public static final String getImgUrl() { return "img/"; } // Img Path URL
	public static final int getWifiWaitingTime() { return WIFI_WAITING_TIME; }
	
	/*
	 * Storage for saving information
	 */
	
	private static final class STORAGE {
		private static final String SERVER_IP_ADDRESS = "server_ip_address.info"; // save server ip to the file in Android System
		private static final String DEVICE_IMEI = "local_imei.info";
	}
	public static final String getStorageNameOfServerIpAddress () { return STORAGE.SERVER_IP_ADDRESS; }
	public static final String getStorageNameOfDeviceImei () { return STORAGE.DEVICE_IMEI; }
	
	/*
	 * Database Query File Path
	 */
	private static final class URL {
		
		private static final String CHECK_SERVER_OPERATION = "/Query/check_server_operation.php";
		private static final String INSTALLATION = "/Query/installation.php";
		
		// login, log, master, caption
		private static final String LOGIN = "/Query/login.php";
		private static final String LOG = "/Query/log.php";
		private static final String MASTER = "/Query/master.php";
		private static final String CAPTION = "/Query/caption.php";
		
		// hall, category, menu
		private static final String HALL = "/Query/hall.php";
		private static final String CATEGORY = "/Query/category.php";
		private static final String MENU = "/Query/menu.php";
		
		// table status
		private static final String TABLE_ORDER_COUNT = "/Query/table_order_count.php"; // every table
		private static final String TABLE_ORDER_LIST = "/Query/table_order_list.php"; // one table

		// option given by db
		private static final String OPTION_LIST =  "/Query/option_list.php";
		private static final String OPTION_COMMON =  "/Query/option_common.php";
		private static final String OPTION_SPECIFIC = "/Query/option_specific.php";
		
		// order
		private static final String ORDER_STATUS_UPDATE = "/Query/order_status_update.php";
		private static final String ORDER_ID_NEXT_REL_ID = "/Query/order_id_next_rel_id.php";
		private static final String ORDER_NEXT_REL_ID = "/Query/order_next_rel_id.php";
		private static final String ORDER_ADD = "/Query/order_add.php";
		private static final String ORDER_NEW_LITE = "/Query/order_new_lite.php";
		
		// current order board
		private static final String ORDER_CANCEL = "/Query/order_cancel.php";
		private static final String ORDER_REMOVE_UNCONFIRMED = "/Query/order_remove_unconfirmed.php";
		
		private static final String ORDER_OPTION_UPDATE = "/Query/order_option_update.php";
		private static final String ORDER_CONFIRM = "/Query/order_confirm.php";
		private static final String ORDER_COMPLETE_REL = "/Query/order_complete_rel.php";
		
	}
	
	// Log URL
	public static final String getLogURL() { return URL.LOG; }
	
	// Check the operation of the server
	public static final String getQuery_CheckServerOperation() { return URL.CHECK_SERVER_OPERATION ;  }
	
	/*
	 * Permission of installing the appication
	 */
	public static final String getQuery_Installation() { return URL.INSTALLATION ;  }
	/*
	 * Master, Caption, Login
	 */
	public static final String getQuery_Master() { return URL.MASTER ;  }
	public static final String getQuery_Caption() { return URL.CAPTION ;  }
	public static final String getQuery_Login() { return URL.LOGIN; }
	
	/*
	 * Hall, Category, Menu
	 */
	public static final String getQuery_Hall() { return URL.HALL ; }
	public static final String getQuery_Category() { return URL.CATEGORY ; }
	public static final String getQuery_Menu() { return URL.MENU ; }
	
	/*
	 * Table Status
	 */
	public static final String getQuery_TableOrderCount() { return URL.TABLE_ORDER_COUNT ; }
	public static final String getQuery_TableOrderList() { return URL.TABLE_ORDER_LIST ; }

	/*
	 * Options
	 */
	public static final String getQuery_OptionList() { return URL.OPTION_LIST ; }
	public static final String getQuery_OptionCommon() { return URL.OPTION_COMMON ; }
	public static final String getQuery_OptionSpecific() { return URL.OPTION_SPECIFIC ; }
	
	/*
	 * Order
	 */
	public static final String getQuery_OrderStatusUpdate() { return URL.ORDER_STATUS_UPDATE; }
	public static final String getQuery_OrderIdNextRelId() { return URL.ORDER_ID_NEXT_REL_ID ; }
	public static final String getQuery_OrderNextRelId() { return URL.ORDER_NEXT_REL_ID ; }
	public static final String getQuery_OrderAdd() { return URL.ORDER_ADD; }
	public static final String getQuery_OrderNewLite() { return URL.ORDER_NEW_LITE; }
	public static final String getQuery_OrderCancel() { return URL.ORDER_CANCEL; }
	public static final String getQuery_OrderRemoveUnconfirmed() { return URL.ORDER_REMOVE_UNCONFIRMED; }
	
	public static final String getQuery_OrderOptionUpdate() { return URL.ORDER_OPTION_UPDATE; }
	public static final String getQuery_OrderConfirm() { return URL.ORDER_CONFIRM; }
	public static final String getQuery_OrderCompleteRel() { return URL.ORDER_COMPLETE_REL; }
}
