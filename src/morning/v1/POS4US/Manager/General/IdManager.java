package morning.v1.POS4US.Manager.General;

import morning.v1.POS4US.DataObject.Id;

/*
 * Manage Id
 */
public class IdManager {
	
	public static void setTemporaryId(int nId) { 
		Id.seTemporaryId(nId); 
	}
	public static final int getTemporaryId() {  
		return Id.getTemporaryId(); 
	}
	public static final int getRandomId() {
		return (int)(Math.random()*100000000);
	}
	public static final int getLanguageId_SelectHallTable() {
		return Id.getLanguageId();
	}
	public static final int getLanguageId_SelectMenu() {
		return Id.getLanguageId()+1;
	}
	public static final int getHallNoByHallId(int nHallResourceId) {
		return nHallResourceId - Id.getHallId();
	}
	public static final int getHallNoByTableId(int nTableId) {
		return  (nTableId - Id.getTableId()) / Id.getHallId();
	}
	public static final int getMenuLayoutIdByCategoryId(int nCateId) {
		return nCateId - Id.getCateId() + Id.getMenuLayoutId();
	}
	public static final int getTableNoByTableId(int nTableId, int nHallNo) {
		return nTableId - Id.getTableId()- (Id.getHallId()*nHallNo);
	}
	public static final int getHallId(int nHallNo) {
		return Id.getHallId() + nHallNo;
	}
	public static final int getHallNo(int nHallId) {
		return nHallId - Id.getHallId();
	}
	public static final int getCategoryId(int nCategoryNo) {
		return Id.getCateId() + nCategoryNo;
	}
	public static final int getTableLayoutId(int nTableLayoutNo) {
		return Id.getTableLayoutId() + nTableLayoutNo;
	}
	public static final int getTableId(int nHallNo, int nTableNo) {
		return Id.getTableId() + (Id.getHallId()*nHallNo) + nTableNo;
	}
	public static final int getTableNo(int nHallId, int nTableId) {
		return  nTableId - Id.getTableId() - (Id.getHallId()*(nHallId - Id.getHallId()));
	}
	public static final int getMenuLayoutId(int nMenuLayoutNo) {
		return Id.getMenuLayoutId() + nMenuLayoutNo;
	}
	public static final int getMenuId (int nCategoryNo, int nMenuNo) {
		return Id.getMenuId() + (Id.getCateId()*nCategoryNo) + nMenuNo;
	}
	public static final int getMenuTextId (int nNo) {
		return Id.getMenuTextId() + nNo;
	}
	public static final int getMenuCountId(int nCategoryNo, int nMenuNo) {
		return Id.getMenuCountId() + (Id.getCateId()*nCategoryNo) + nMenuNo;
	}
	public static final int getCategoryNo(int nCategoryId) {
		return nCategoryId - Id.getCateId();
	}
	public static final int getMenuNoByMenuId(int nMenuId, int nCateNo) {
		return nMenuId - Id.getMenuId() - (Id.getCateId()*nCateNo);
	}
	public static final int getOptionCommonId(int nNo) {
		return Id.getOptionCommonId() + nNo;
	}
	public static final int getOptionSpecificId(int nNo) {
		return Id.getOptionSpecificId() + nNo;
	}
	public static final int getCommonOptionCountId(int nNo) {
		return getOptionCommonId(nNo);
	}
	public static final int getSpecificOptionCountId(int nNo) {
		return getOptionSpecificId(nNo);
	}
	public static final int getOrderBoardTextId(int nNo) {
		return Id.getOrderBoardTextId() + nNo;
	}

}
