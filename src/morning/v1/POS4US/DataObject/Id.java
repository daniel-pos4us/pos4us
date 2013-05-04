package morning.v1.POS4US.DataObject;

/*
 * Resource ID : Beginning Address
 */
public final class Id {
	
	// Temporary Id
	private static int TEMPORARY_ID = 0;
	public static void seTemporaryId(int nId) { TEMPORARY_ID = nId; }
	public static final int getTemporaryId() { return TEMPORARY_ID; }
	
	// XML Node Id
	private static final int HALL_XML_NODE_ID = 0xcafe1000;
	private static final int CATE_XML_NODE_ID = 0xcafe1001;
	private static final int MENU_XML_NODE_ID = 0xcafe1002;
	private static final int OPTION_COMMON_XML_NODE_ID = 0xcafe1003;
	private static final int OPTION_SPECIFIC_XML_NODE_ID = 0xcafe1004;

	// Resource Id
	private static final int LANGUAGE_ID = 0xbbaa0011;
	private static final int HALL_ID = 100; 
	private static final int TABLE_LAYOUT_ID = 10000;
	private static final int TABLE_ID = 1000000;
	private static final int CATE_ID = 200; 
	private static final int MENU_LAYOUT_ID = 20000;
	private static final int MENU_ID = 2000000;
	private static final int MENU_TEXT_ID = 0xc0ffee00;
	private static final int MENU_COUNT_ID = 0xbeebee00;
	private static final int POPUP_WINDOW_ID = 0x90001000;
	private static final int OPTION_COMMON_ID = 0xaa001000;
	private static final int OPTION_SPECIFIC_ID = 0xff001000;
	private static final int ORDER_BOARD_TEXT_ID = 0xaaaa0000;
	private static final int ORDER_BOARD_OPTION_COMMON_ID = 0xbbbb0000;
	private static final int ORDER_BOARD_OPTION_SPECIFIC_ID = 0xcccc0000;
	
	// get  XML Node Id
	
	public static final int getHallXmlNodeId() { return HALL_XML_NODE_ID; }
	public static final int getCateXmlNodeId() { return CATE_XML_NODE_ID; }
	public static final int getMenuXmlNodeId() { return MENU_XML_NODE_ID; }
	public static final int getOptionCommonXmlNodeId() { return OPTION_COMMON_XML_NODE_ID; }
	public static final int getOptionSpecificXmlNodeId() { return OPTION_SPECIFIC_XML_NODE_ID; }
	
	// get Resource Id (Next id always becomes plus one)
	
	public static final int getLanguageId() { return LANGUAGE_ID; }
	
	public static final int getHallId () { return HALL_ID; }
	public static final int getTableLayoutId () { return TABLE_LAYOUT_ID; }
	public static final int getTableId () { return TABLE_ID; }

	public static final int getCateId () { return CATE_ID; }
	public static final int getMenuLayoutId () { return MENU_LAYOUT_ID; }
	public static final int getMenuId () { return MENU_ID; }
	public static final int getMenuTextId () { return MENU_TEXT_ID; }
	public static final int getMenuCountId() { return MENU_COUNT_ID; }
	
	public static final int getOptionCommonId() { return OPTION_COMMON_ID; }
	public static final int getOptionSpecificId() { return OPTION_SPECIFIC_ID; }
	
	public static final int getOrderBoardTextId() { return ORDER_BOARD_TEXT_ID; }
	public static final int getOrderBoardOptionCommonId() { return ORDER_BOARD_OPTION_COMMON_ID; }
	public static final int getOrderBoardOptionSpecificId() { return ORDER_BOARD_OPTION_SPECIFIC_ID; }
	
	public static final int getPopupWindowId() { return POPUP_WINDOW_ID; }

}
