package morning.v1.POS4US.Configuration;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

/*
 *  Settings for Developer's Convenience
 */
public class Property {

	// Menu Image Size : 320 X 216

	// Server Access Mode
	public static final int SERVER_ACCESS_FILE_MODE = 1;
	public static final int SERVER_ACCESS_INPUT_MODE = 2;
	public static int SERVER_ACCESS_MODE = SERVER_ACCESS_FILE_MODE; // default
	
	// Application Type
	public static final int APP_TYPE_MOBILE_GEAR = 1;
	public static final int APP_TYPE_EMULATOR = 2;
	public static final int CURRENT_APP_TYPE = APP_TYPE_EMULATOR; //default

	// Synchronization Period (Default: 5 secs)
	public static final long RUN_THREAD_EVERY_MILLISEC = 10000L;
		

	public final static String SYSTEM_ENCODING = "UTF-8"; // system encoding - it's for requesting http post with utf-8
	public final static String SESSION_TOKEN = "_PO4US_SESSION_"; 	// needs for saving login id & password
	public final static String HTTP_REQUEST_TOKEN = "_POS4US_HTTP_REQUEST_"; // needs for direct requestin
	
	//  Business Information : From Database
	public static String CAPTION_LANGUAGE = "";
	public static String MENU_LANGUAGE = "";
	public static String PROVIDER_NAME = "";
	public static String BUSINESS_NAME = "";
	public static String CURRENCY_UNIT = "";
	
	// Caption Language
	public static class CaptionLanguage { 	
		public static final String ENGLISH = "eng";
		public static final String KOREAN = "kor";
		public static final String JAPAN = "jpa";
		public static final String CHINESE = "chi";
	};
	
	// Menu Language
	public static class MenuLanguage { 	
		public static final String ENGLISH = "eng";
		public static final String OTHER = "oth";
	};
	
	// Language Text on Title Bar
	public static class LanguageTextOnTitleBar {
		public static final String ENGLISH = "English";
		public static final String OTHER = "Other";
	}
	
	// How to show ordered list to the Order Status Layout
	public static class OrderBoardTextFormat {
		public static final String ENGLISH = MenuLanguage.ENGLISH;// + "(" + MenuLanguage.OTHER + ")";
		public static final String OTH = MenuLanguage.OTHER + "(" + MenuLanguage.ENGLISH + ")";
	}

	// Title Bar
	public static final int TITLE_BAR_TEXT_COLOR = 0xffffffff;
	public static final int TITLE_BAR_LANG_WIDTH = 120;
	public static final int TITLE_BAR_LANG_TEXT_SIZE = 17;
	public static final int TITLE_BAR_BUSINESS_NAME_WIDTH = 400;
	public static final int TITLE_BAR_BUSINESS_NAME_TEXT_SIZE = 18;
	public static final int TITLE_BAR_GRAVITY_CENTER = Gravity.CENTER;
	public static final int TITLE_BAR_GRAVITY_RIGHT = Gravity.RIGHT;
	public static final int TITLE_BAR_ID_WIDTH = 120;
	public static final int TITLE_BAR_ID_TEXT_SIZE = 13;
	
	// Hall
	public static final int HALL_NAME_FONT_SIZE = 13;
	public static final int HALL_NAME_FONT_COLOR = 0xffffffff;
	public static final int HALL_WIDTH = LayoutParams.WRAP_CONTENT;
	public static final int HALL_HEIGHT = 75;
	public static final int HALL_BUTTON_SPACE = 10;
	public static final int HALL_DEFAULT_SHOW_NUMBER = 0;
	
	// Category
	public static final int CATE_MAX_ROWS = 2;
	public static final int CATE_BUTTON_HEIGHT = 60;
	public static final int CATE_BUTTON_WIDTH = 144;
	public static final int CATE_TEXT_SIZE = 13;
	public static final int CATE_TEXT_COLOR = 0xffffffff;
	public static final int CATE_TEXT_LENGTH_LIMIT = 14;
	
	// Menu List
	public static final int MENULIST_WIDTH = LayoutParams.MATCH_PARENT;
	public static final int MENULIST_HEIGHT = LayoutParams.WRAP_CONTENT;
	public static final int MENULIST_ORIENTATION = LinearLayout.VERTICAL;
	
	// Menu Layout
	public static final int MENULAYOUT_WIDTH = LayoutParams.MATCH_PARENT;
	public static final int MENULAYOUT_HEIGHT = LayoutParams.WRAP_CONTENT;
	public static final int MENULAYOUT_HIGHLIGHT_COLOR = 0xffeeeeee;
	
	// Menu Text
	public static final int MENU_TEXT_WIDTH = 400;
	public static final int MENU_TEXT_HEIGHT = 50;
	public static final int MENU_TEXT_SIZE = 16;
	public static final int MENU_TEXT_COLOR = 0xff333333;
	public static final int MENU_TEXT_SHOW_LIMIT_NUM = 35;
	
	// Table
	public static final int TABLE_NUM_COLOR = 0xff555555;
	public static final int TABLE_NUM_FONT_SIZE = 46;
	public static final int TABLE_NUM_OF_ORDER_FONT_SIZE = 14;
	public static final int TABLE_MAX_COLS = 4;
	public static final int TABLE_MAX_ROWS = 6;
	public static final int TABLE_WIDTH = 143;
	public static final int TABLE_HEIGHT = 137;
	public static final int TABLE_STATUS_COLOR = 0xffffffff;
	
	// Order
	public static final int ORDER_TITLE_SIZE = 13;
	public static final int ORDER_TITLE_COLOR = 0xff111111;
	public static final int ORDER_TITLE_LENGTH_LIMIT = 50;
	public static final int ORDER_TYPE_MENU = 0;
	public static final int ORDER_TYPE_OPTION = 1;
	
	// Order Board Each Layout
	public static final int ORDER_BOARD_LAYOUT_WIDTH = LayoutParams.MATCH_PARENT;
	public static final int ORDER_BOARD_LAYOUT_HEIGHT = LayoutParams.WRAP_CONTENT;

	// Order Board Text
	public static final int ORDER_BOARD_TEXT_COLOR = 0xff333333;
	public static final int ORDER_BOARD_CUT_TEXT = 16;
	public static final int ORDER_BOARD_TEXT_WIDTH = 540;
	public static final int ORDER_BOARD_TEXT_HEIGHT = 50;
	public static final int ORDER_BOARD_TEXT_SIZE = 16;

	//  Arrow Image Size in Popup
	public static final int POPUP_ARROW_IMG_WIDTH = 60;
	public static final int POPUP_ARROW_IMG_HEIGHT = 60;
	
	// MessageBox
	public static final int MSG_LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static final int MSG_LENGTH_LONG = Toast.LENGTH_LONG;
	
	// File not found 
	public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
	
}
