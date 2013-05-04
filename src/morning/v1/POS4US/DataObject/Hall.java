package morning.v1.POS4US.DataObject;

/*
 * Hall Data Object
 */
public class Hall {

	public int DB_ID;
	public int RESOURCE_ID;
	public int TABLE_NUM;
	public String NAME_ENG;
	public String NAME_OTH;
	
	public Hall() {}
	
	public Hall(int nDbId, int nResourceId, int nTableNum, String strNameEng, String strNameOth) {
		DB_ID = nDbId;
		RESOURCE_ID = nResourceId;
		TABLE_NUM = nTableNum;
		NAME_ENG = strNameEng;
		NAME_OTH = strNameOth;
	}
}
