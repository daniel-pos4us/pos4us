package morning.v1.POS4US.DataObject;

/*
 * Option Data Object
 */
public class Option  {

	public int DB_ID = 0;
	public int MENU_RESOURCE_ID = 0;
	public int MENU_DB_ID = 0;
	public String NAME_ENG = "";
	public String NAME_OTH = "";
	public float GST = 0;
	public float PRICE = 0;
	public int ORDER_COUNT = 0;
	
	public Option() {}
	
	public Option (int nDbId, int nMenuResourceId, int nMenuDbId, String strNameEng, String strNameOth, float fGST, float fPrice, int nOrderCount) {
		DB_ID = nDbId;
		MENU_RESOURCE_ID = nMenuResourceId;
		MENU_DB_ID = nMenuDbId;
		NAME_ENG = strNameEng;
		NAME_OTH = strNameOth;
		GST = fGST;
		PRICE = fPrice;
		ORDER_COUNT = nOrderCount;
	}
}
