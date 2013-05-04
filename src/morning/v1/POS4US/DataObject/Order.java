package morning.v1.POS4US.DataObject;

/*
 * Order Data Object
 */
public class Order {
	public int ID = 0;
	public int ORDER_ID = 0;
	public int REL_ID = 0;
	public int OPTION_COMMON_ID = 0;
	public int OPTION_SPECIFIC_ID = 0;
	public int HALL_NO = 0;
	public int TABLE_NO = 0;
	public int CATE_ID = 0;
	public int MENU_ID = 0;
	public String NAME_ENG = "";
	public String NAME_OTH = "";
	public float GST = 0;
	public float PRICE = 0;
	public int ORDER_COUNT = 1;
	public float SUBTOTAL_GST = 0;
	public float SUBTOTAL_PRICE = 0;
	public int HANDLING_SECTION1 = 0;
	public int HANDLING_SECTION2 = 0;
	public int HANDLING_SECTION3 = 0;
	public int HANDLING_SECTION4 = 0;
	public String RECEIPT_ID = "";
	public String ORDER_PRT_ID = "";

	public Order() {}
	// for menu order
	public Order(int nOrderId, int nRelId, int nOptionCommonId, int nOptionSpecificId, int nHallNo, int nTableNo, int nCateId, int nMenuId, float fGst, float fPrice,
			int nOrderCount, int nHandlingSection1, int nHandlingSection2, int nHandlingSection3, int nHandlingSection4, String strReceiptId, String strOrderPrtId) {
		 ORDER_ID = nOrderId;
		 REL_ID = nRelId;
		 OPTION_COMMON_ID = nOptionCommonId;
		 OPTION_SPECIFIC_ID = nOptionSpecificId;
		 HALL_NO = nHallNo;
		 TABLE_NO = nTableNo;
		 CATE_ID = nCateId;
		 MENU_ID = nMenuId;
		 GST = fGst;
		 PRICE = fPrice;
		 ORDER_COUNT = nOrderCount;
		 HANDLING_SECTION1 = nHandlingSection1;
		 HANDLING_SECTION2 = nHandlingSection2;
		 HANDLING_SECTION3 = nHandlingSection3;
		 HANDLING_SECTION4 = nHandlingSection4;
		 RECEIPT_ID = strReceiptId;
		 ORDER_PRT_ID = strOrderPrtId;
	}
	// for option order
	public Order(int nId, int nOrderId, int nRelId, int nOptionCommonId, int nOptionSpecificId, int nHallNo, int nTableNo, int nMenuId, int nOrderCount) {
		 ID = nId;
		 ORDER_ID = nOrderId;
		 REL_ID = nRelId;
		 OPTION_COMMON_ID = nOptionCommonId;
		 OPTION_SPECIFIC_ID = nOptionSpecificId;
		 HALL_NO = nHallNo;
		 TABLE_NO = nTableNo;
		 MENU_ID = nMenuId;
		 ORDER_COUNT = nOrderCount;
	}
	// for order status
	public Order(int nId, int nOrderId, int nRelId, int nOptionCommonId, int nOptionSpecificId, int nHallNo, int nTableNo, int nCateId, int nMenuId, 
			String strNameEng, String strNameOth, float fSubtotalPrice, int nOrderCount) {
		ID = nId;
		ORDER_ID = nOrderId;
    	REL_ID = nRelId;
		OPTION_COMMON_ID = nOptionCommonId;
		OPTION_SPECIFIC_ID = nOptionSpecificId;
		HALL_NO = nHallNo;
		TABLE_NO = nTableNo;
		CATE_ID = nCateId;
		MENU_ID = nMenuId;
		NAME_ENG = strNameEng;
		NAME_OTH = strNameOth;
		SUBTOTAL_PRICE = fSubtotalPrice;
		ORDER_COUNT = nOrderCount;
	}
}
