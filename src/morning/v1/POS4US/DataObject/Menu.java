package morning.v1.POS4US.DataObject;

/*
 * Menu Data Object
 */
public class Menu {
	
	public int DB_ID = 0;
	public int RESOURCE_ID = 0;
	public int CATE_DB_ID = 0;
	public int CATE_RESOURCE_ID = 0;
	public String NAME_ENG = "";
	public String NAME_OTH = "";
	public String IMG_NAME = "";
	public float PRICE = 0;
	public float GST = 0;
	public String DESCRIPTION_ENG = "";
	public String DESCRIPTION_OTH = "";
	public int HANDLING_SECION1 = 0;
	public int HANDLING_SECION2 = 0;
	public int HANDLING_SECION3 = 0;
	public int HANDLING_SECION4 = 0;
	
	public Menu(){}
	public Menu(int nDbId, int nResourceId, int nDbCateId, int nCateResourceId, String strNameEng, String strNameOth, String strImgName, float fPrice, float fGst, String strDescriptionEng, String strDescriptionOth,
			int nHandlingSection1, int nHandlingSection2, int nHandlingSection3, int nHandlingSection4) {
		DB_ID = nDbId;
		RESOURCE_ID = nResourceId;
		CATE_DB_ID = nDbCateId;
		CATE_RESOURCE_ID = nCateResourceId;
		NAME_ENG = strNameEng;
		NAME_OTH = strNameOth;
		IMG_NAME = strImgName;
		PRICE = fPrice;
		GST = fGst;
		DESCRIPTION_ENG = strDescriptionEng;
		DESCRIPTION_OTH = strDescriptionOth;
		HANDLING_SECION1 = nHandlingSection1;
		HANDLING_SECION2 = nHandlingSection2;
		HANDLING_SECION3 = nHandlingSection3;
		HANDLING_SECION4 = nHandlingSection4;
		
	}
}
