package morning.v1.POS4US.DataObject;

/*
 * Category Data Object
 */
public class Category {
	
	public int DB_ID;
	public int RESOURCE_ID;
	public String NAME_ENG;
	public String NAME_OTH;
	
	public Category(){}
	public Category(int nDbId, int nResourceId, String strNameEng, String strNameOth) {
		DB_ID = nDbId;
		RESOURCE_ID = nResourceId;
		NAME_ENG = strNameEng;
		NAME_OTH = strNameOth;
	}
}
