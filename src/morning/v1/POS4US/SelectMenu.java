package morning.v1.POS4US;

import java.util.ArrayList;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.DataObject.Id;
import morning.v1.POS4US.DataObject.Menu;
import morning.v1.POS4US.DataObject.Option;
import morning.v1.POS4US.DataObject.Order;
import morning.v1.POS4US.Manager.General.CaptionManager;
import morning.v1.POS4US.Manager.General.CategoryManager;
import morning.v1.POS4US.Manager.General.IdManager;
import morning.v1.POS4US.Manager.General.MenuManager;
import morning.v1.POS4US.Manager.General.OptionManager;
import morning.v1.POS4US.Manager.General.OrderBoardManager;
import morning.v1.POS4US.Manager.General.OrderManager;
import morning.v1.POS4US.Manager.General.TitleBarManager;
import morning.v1.POS4US.Manager.System.History;
import morning.v1.POS4US.Manager.System.NavigationManager;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Util.ImageManager;
import morning.v1.POS4US.Util.Mapper;
import morning.v1.POS4US.Util.TextFormatter;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/*
 * Select Menu : Order Menu / Options
 */
public class SelectMenu extends Activity{
	
	// Finger Gesture Detector
    private GestureDetector _MenuGesture;        // menu click
    private GestureDetector _OrderBoardGesture;  // order board click 
 
    private PopupWindow _PopupWindowOfOptions;   // add options
    private PopupWindow _PopupWindowDetail;      // menu info
    
    // Init
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_menu);
		
		if(NavigationManager.isCalledByPrevious()) {
			
			loadTitleBar();
			loadCategoryLayouts();
			loadMenuLayouts();
			loadOrderBoard();
			loadBottomButton();
			
			toggleCategory( Id.getCateId() + 0 ); // load default category ( 0 means 1st category )
			toggleMenuLayout( Id.getMenuLayoutId() + 0 ); // load default menu layout ( 0 means 1st menu layout )
			
			_MenuGesture = new GestureDetector(this, new onMenuGestureListener());
			_OrderBoardGesture = new GestureDetector(this, new onOrderBoardGestureListener()); 
		}
		
	}
        
    // Single tap on Menu : New & Update Order
    private final class onMenuGestureListener extends GestureDetector.SimpleOnGestureListener {
    	public boolean onSingleTapConfirmed (MotionEvent e) {
    	
    		View v = History.getView();
    		
    		int nMenuTextId = v.getId();
	    	int nMenuResourceId = MenuManager.getResourceIdByTextId(nMenuTextId);
			Menu mMenu = MenuManager.getMenuByResourceId(nMenuResourceId);
			
        	OrderManager.setCateId(mMenu.CATE_DB_ID);
        	OrderManager.setMenuId(mMenu.DB_ID); 
			
        	OrderManager.requestUpdateMenu();
			loadOrderBoard();
			OrderManager.clearOption();
    		
    		return false;
    	}
    }

    // Tap on Order Board List : Modify Order
    private final class onOrderBoardGestureListener extends GestureDetector.SimpleOnGestureListener {

    	// open option dialogue
    	public boolean onSingleTapConfirmed (MotionEvent e) {
    		
    		View v = History.getView();
    		popupWindowOfOptions(v);
    		    		
    		return false;
    	}
    	
    	// Set status of an order clicked as 'COMPLETE' 
    	public boolean onDoubleTap (MotionEvent e) {

    		View v = History.getView();
    		
    		int nOrderBoardTextId = v.getId();
        	Order _Order = (Order) ResourceManager.get(nOrderBoardTextId);
        	int nOrderDbId = _Order.ORDER_ID;
        	int nRelId = _Order.REL_ID;

        	//if(_Order.OPTION_COMMON_ID==0 && _Order.OPTION_SPECIFIC_ID==0) { // order completed
        		OrderManager.completeOrderRel(nOrderDbId, nRelId);
        		loadOrderBoard();
        	//}

    		return false;
    	}

    	// Cancel Order
    	public void onLongPress (MotionEvent e) {
    		View v = History.getView();
    		cancelOrder(v);
    	}
    	
    }
    
    // popup : select option
 	private void popupWindowOfOptions(View v) {
 	    
 		OrderManager.clearOption();
 		
 		int nOrderBoardTextId = v.getId();
     	Order _Order = (Order) ResourceManager.get(nOrderBoardTextId);
     	int nMenuDbId = _Order.MENU_ID;
     	int nMenuResourceId = MenuManager.getResourceIdByDbId(nMenuDbId);
     	Menu mMenu = MenuManager.getMenuByResourceId(nMenuResourceId);
     	
     	int nTempIdForCurrentOrder = IdManager.getRandomId();
     	IdManager.setTemporaryId(nTempIdForCurrentOrder);
     	ResourceManager.put(nTempIdForCurrentOrder, _Order);
     	
         try {
         	
         	int nPopupHeight = 760;
     		int nPopupWith = 520;
         	
         	int nSubTitleHeight = 40;
         	int nOptionListLayoutHeight = 210;
         	
         	String strMenuNameEng = mMenu.NAME_ENG;
         	String strMenuNameOth = mMenu.NAME_OTH;
         	
         	// Popup layout
             LinearLayout popLayout = new LinearLayout(this);
             popLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
             popLayout.setGravity(Gravity.CENTER);
             popLayout.setBackgroundColor(0x22000000);
             
 	            // Inner layout
 	            LinearLayout innerLayout = new LinearLayout(this);
 	            innerLayout.setLayoutParams(new LayoutParams(nPopupWith-20,nPopupHeight-20));
 	            innerLayout.setPadding(10,10,10,10);
 	            innerLayout.setOrientation(LinearLayout.VERTICAL);
 	            innerLayout.setGravity(Gravity.CENTER);
 	            innerLayout.setBackgroundColor(0xddffffff);
 		            
 		            // title eng
 		            TextView tvTitleEng = new TextView(this);
 		            tvTitleEng.setGravity(Gravity.CENTER);
 		            tvTitleEng.setText(strMenuNameEng);
 		            tvTitleEng.setTextColor(0xff000000);
 		            tvTitleEng.setTextSize(23);
 		            tvTitleEng.setHeight(50);
 		            tvTitleEng.setWidth(470);
 		            
 		            // title oth
 		            TextView tvTitleOth = new TextView(this);
 		            tvTitleOth.setGravity(Gravity.TOP|Gravity.CENTER);
 		            tvTitleOth.setText(strMenuNameOth);
 		            tvTitleOth.setTextColor(0xff000000);
 		            tvTitleOth.setTextSize(13);
 		            tvTitleOth.setHeight(55);
 		            tvTitleOth.setWidth(470);
 		            
 		            // common option title
 		            TextView tvCO = new TextView(this);
 		            tvCO.setText("* Common Option");
 		            tvCO.setPadding(10,0,0,0);
 		            tvCO.setTextSize(15);
 		            tvCO.setTextColor(0xff5f2d09);
 		            tvCO.setHeight(nSubTitleHeight);
 		            tvCO.setWidth(470);
 		            
 		            // common option layout
 		            ScrollView svCO = new ScrollView(this);
 		            svCO.setLayoutParams(new LayoutParams(470,nOptionListLayoutHeight));
 		            
 		            LinearLayout llCO = new LinearLayout(this);
 		            llCO.setLayoutParams(new LayoutParams(470,LayoutParams.WRAP_CONTENT));
 		            llCO.setPadding(15,10,10,10);
 		            llCO.setOrientation(LinearLayout.VERTICAL);
 		            llCO.setGravity(Gravity.TOP);
 		            
 		            showCommonOptions (mMenu, llCO);
 		           	
 		           	svCO.addView(llCO);
 		           	
 			        // specific option title
 			            
 		            TextView tvSO = new TextView(this);
 		            tvSO.setPadding(10,0,0,0);
 		            tvSO.setText("* Specific Option");
 		            tvSO.setTextSize(15);
 		            tvSO.setTextColor(0xff5f2d09);
 		            tvSO.setHeight(nSubTitleHeight);
 		            tvSO.setWidth(470);
 	            
 		            ScrollView svSO = new ScrollView(this);
 		            svSO.setLayoutParams(new LayoutParams(470,nOptionListLayoutHeight));
 		            
 		            LinearLayout llSO = new LinearLayout(this);
 		            llSO.setLayoutParams(new LayoutParams(470,nOptionListLayoutHeight));
 		            llSO.setPadding(15,10,10,10);
 		            llSO.setOrientation(LinearLayout.VERTICAL);
 		            llSO.setGravity(Gravity.TOP);
 	
 		            showSpecificOptions (mMenu, llSO);
 		            
 		            svSO.addView(llSO);
 		            
 		            // button group
 		            
 		            int nButtonHeight = 50;
 		            int nButtonWidth = 100;
 		            
 		            LinearLayout llButtonGroup = new LinearLayout(this);
 		            llButtonGroup.setLayoutParams(new LayoutParams(470,80));
 		            llButtonGroup.setPadding(10, 0, 10, 4);
 		            llButtonGroup.setOrientation(LinearLayout.HORIZONTAL);
 		            llButtonGroup.setGravity(Gravity.CENTER|Gravity.BOTTOM);
 		            
 		            // confirm button
 	            
 	            	int nConfirmId = IdManager.getRandomId();
 		            Button btnConfirm = new Button(this);
 		            btnConfirm.setId(nConfirmId);
 		            btnConfirm.setBackgroundResource(R.drawable.color_button_basic);
 		            btnConfirm.setLayoutParams(new LayoutParams(nButtonWidth,nButtonHeight));
 		            btnConfirm.setText(CaptionManager.getCaption().CONFIRM);
 		            btnConfirm.setTextSize(13);
 		            
 		            // add options to an order
 		            btnConfirm.setOnClickListener(new OnClickListener() {
 			            public void onClick(View v) {
 			            	
 			            	Order currentOrder = (Order)ResourceManager.get(IdManager.getTemporaryId());
 			            	
 			            	ArrayList<Order> orderList = addOptionsToOrder(currentOrder);
 			            	OrderManager.requestUpdateOptions(currentOrder,orderList);
 	        				loadOrderBoard();
 	        				OrderManager.clearOption();
 	        				_PopupWindowOfOptions.dismiss();
 			            }
 		            });
 		            
 		            // cancel button
 		            Button btnCancel = new Button(this);
 		            btnCancel.setBackgroundResource(R.drawable.color_button_basic);
 		            btnCancel.setLayoutParams(new LayoutParams(nButtonWidth,nButtonHeight));
 		            btnCancel.setText(CaptionManager.getCaption().CANCEL);
 		            btnCancel.setTextSize(13);
 		            btnCancel.setOnClickListener(new OnClickListener() {
 			            public void onClick(View v) {
 			            	OrderManager.clearOption();
 			            	_PopupWindowOfOptions.dismiss();
 			            }
 		            });
 		            
 		            // detail button
 		            final int nTempId = 808080;
 		            Mapper.put(Integer.valueOf(nTempId), mMenu);
 		            
 		            Button btnDetail = new Button(this);
 		            btnDetail.setBackgroundResource(R.drawable.color_button_basic);
 		            btnDetail.setLayoutParams(new LayoutParams(nButtonWidth,nButtonHeight));
 		            btnDetail.setText(CaptionManager.getCaption().DETAIL);
 		            btnDetail.setTextSize(13);
 		            btnDetail.setOnClickListener(new OnClickListener() {
 			            public void onClick(View v) {
 			           
 			            	Menu menu = (Menu)Mapper.get(Integer.valueOf(808080));
 			            	int nMenuRscId = menu.RESOURCE_ID;
 			            	popupWindowDetail(nMenuRscId);
 			            }
 		            });
 			            
 	            llButtonGroup.addView(btnConfirm);
 	            llButtonGroup.addView(btnCancel);
 	            llButtonGroup.addView(btnDetail);
 	                
 		        innerLayout.addView(tvTitleEng);
 	            innerLayout.addView(tvTitleOth);
 	            innerLayout.addView(tvCO);
 	            innerLayout.addView(svCO);
 	            innerLayout.addView(tvSO);
 	            innerLayout.addView(svSO);
 	            innerLayout.addView(llButtonGroup);
 	            
             popLayout.addView(innerLayout);

             // show popup
             _PopupWindowOfOptions = new PopupWindow(popLayout, nPopupWith, nPopupHeight, true);
             _PopupWindowOfOptions.showAtLocation(popLayout, Gravity.CENTER, 0, 0);
         	
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 	
    // popup detail
 	private void popupWindowDetail (int nMenuResourceId) {
 	    
         try {
         	
        	Menu mMenu = MenuManager.getMenuByResourceId(nMenuResourceId);
        	int nPopupHeight = 750;
     		int nPopupWith = 520;
     	 	String strMenuNameEng = mMenu.NAME_ENG;
         	String strMenuNameOth = mMenu.NAME_OTH;
         	
         	// Popup layout
         	
             LinearLayout popLayout = new LinearLayout(this);
             popLayout.setLayoutParams(new LayoutParams(nPopupWith,nPopupHeight));
             popLayout.setPadding(10,10,10,10);
             popLayout.setBackgroundColor(0x22000000);
             
             // Inner layout
             LinearLayout innerLayout = new LinearLayout(this);
             innerLayout.setLayoutParams(new LayoutParams(nPopupWith-20,nPopupHeight-20));
             innerLayout.setPadding(10,10,10,10);
             innerLayout.setOrientation(LinearLayout.VERTICAL);
             innerLayout.setGravity(Gravity.CENTER);
             innerLayout.setBackgroundColor(0xffffffff);
 	            
             // title eng
             TextView tvTitle = new TextView(this);
             tvTitle.setGravity(Gravity.CENTER);
             tvTitle.setPadding(0, 10, 0, 0);
             tvTitle.setText(strMenuNameEng);
             tvTitle.setTextColor(0xff000000);
             tvTitle.setTextSize(23);
             tvTitle.setHeight(55);
             tvTitle.setWidth(470);
             
             // title oth
             TextView tvTitle1 = new TextView(this);
             tvTitle1.setGravity(Gravity.TOP|Gravity.CENTER);
             tvTitle1.setText(strMenuNameOth);
             tvTitle1.setTextColor(0xff000000);
             tvTitle1.setTextSize(13);
             tvTitle1.setHeight(50);
             tvTitle1.setWidth(470);
             
             // img layout
             
             LinearLayout llImage = new LinearLayout(this);
             llImage.setLayoutParams(new LayoutParams(470,220));
             llImage.setOrientation(LinearLayout.VERTICAL);
             llImage.setGravity(Gravity.TOP);
             
             // img
             ImageView ivImage = new ImageView(this);
         	
         	if(!mMenu.IMG_NAME.isEmpty()) {
         		String strImgUrl = ServerProperty.getImgUrl() + mMenu.IMG_NAME;
         		ImageManager im = new ImageManager();
         	    ivImage.setImageBitmap(im.LoadImage(strImgUrl));
         	}
             
             llImage.addView(ivImage);
             
             // desc layout
             
             ScrollView svDesc = new ScrollView(this);
             svDesc.setLayoutParams(new LayoutParams(470,300));
             svDesc.setPadding(0,10,10,0);
             
             LinearLayout llDesc = new LinearLayout(this);
             llDesc.setLayoutParams(new LayoutParams(470,LayoutParams.WRAP_CONTENT));
             llDesc.setPadding(20,10,10,10);
             llDesc.setOrientation(LinearLayout.VERTICAL);
             llDesc.setGravity(Gravity.TOP);
           
             svDesc.addView(llDesc);
             
             // desc
             
             String strDescEng = mMenu.DESCRIPTION_ENG;
             String strDescOth = mMenu.DESCRIPTION_OTH;
             
             TextView tvDescriptionEng = new TextView(this);
         	 tvDescriptionEng.setLayoutParams(new LayoutParams(470,LayoutParams.WRAP_CONTENT));
         	 tvDescriptionEng.setGravity(Gravity.LEFT);
         	 tvDescriptionEng.setText(strDescEng+"\n");
         	 tvDescriptionEng.setTextColor(0xff000000);
         	 tvDescriptionEng.setTextSize(15);
             
             TextView tvDescriptionOth = new TextView(this);
             tvDescriptionOth.setLayoutParams(new LayoutParams(470,LayoutParams.WRAP_CONTENT));
             tvDescriptionOth.setGravity(Gravity.LEFT);
             tvDescriptionOth.setText(strDescOth);
             tvDescriptionOth.setTextColor(0xff000000);
             tvDescriptionOth.setTextSize(15);
             
             llDesc.addView(tvDescriptionEng);
             llDesc.addView(tvDescriptionOth);
           
             // button group
             
             int nButtonHeight = 50;
             int nButtonWidth = 100;
             
             LinearLayout llButtonGroup = new LinearLayout(this);
             llButtonGroup.setLayoutParams(new LayoutParams(470,LayoutParams.WRAP_CONTENT));
             llButtonGroup.setPadding(10, 20, 10, 4);
             llButtonGroup.setOrientation(LinearLayout.HORIZONTAL);
             llButtonGroup.setGravity(Gravity.CENTER);
        
             // close button
             
             Button btnClose = new Button(this);
             btnClose.setBackgroundResource(R.drawable.color_button_basic);
             btnClose.setLayoutParams(new LayoutParams(nButtonWidth,nButtonHeight));
             btnClose.setText(CaptionManager.getCaption().CLOSE);
             btnClose.setTextSize(13);
             btnClose.setOnClickListener(new OnClickListener() {
 	            public void onClick(View v) {
 	            	
 	            	_PopupWindowDetail.dismiss();
 	            	_PopupWindowDetail = null;
 	            }
             });
 	            
             llButtonGroup.addView(btnClose);

             innerLayout.addView(tvTitle);
             innerLayout.addView(tvTitle1);
             innerLayout.addView(llImage);
             innerLayout.addView(svDesc);
             innerLayout.addView(llButtonGroup);
 	            
             popLayout.addView(innerLayout);

             // show popup
             _PopupWindowDetail = new PopupWindow(popLayout, nPopupWith, nPopupHeight, true);
             _PopupWindowDetail.showAtLocation(popLayout, Gravity.CENTER, 0, 0);
             
         }catch(Exception e) {}
     }
 	
 	// add options to an order
 	public ArrayList<Order> addOptionsToOrder(Order currentOrder) {
		
 		int nId = currentOrder.ID;
 		int nOrderId = currentOrder.ORDER_ID;
		int nRelId = currentOrder.REL_ID;
		int nHallNo = currentOrder.HALL_NO;
		int nTableNo = currentOrder.TABLE_NO;
		int nMenuId = currentOrder.MENU_ID;
		
		ArrayList<Option> _OptionList = OrderManager.getOptionList();
		ArrayList<Order> _OrderList = new ArrayList<Order>();
		
		// option
		for(int i=0; i<_OptionList.size(); i++) {
			Option _Option = _OptionList.get(i);
			int nOrderCount = _Option.ORDER_COUNT;
			
			if(nOrderCount>0) {
				int nOptionCommonId = 0;
				int nOptionSpecificId = 0;
				if(_Option.MENU_DB_ID > 0) {
					nOptionSpecificId = _Option.DB_ID;
				} else {
					nOptionCommonId = _Option.DB_ID;
				}
				_OrderList.add(new Order(nId, nOrderId, nRelId, nOptionCommonId, nOptionSpecificId, nHallNo, nTableNo, nMenuId, nOrderCount));
			}
		}
		
		return _OrderList;
	}
    
 	// show common option list
 	private void showCommonOptions (Menu mMenu, LinearLayout llCO) {
 		
         for(int i=0; i<OptionManager.getCommonOptionSize(); i++) {
         	
         	Option _Option = OptionManager.getCommonOption(i);
         	
         	LinearLayout llEachLine = new LinearLayout(this);
			llEachLine.setLayoutParams(new LayoutParams(500,LayoutParams.WRAP_CONTENT));
			llEachLine.setPadding(0, 0, 10, 4);
			llEachLine.setOrientation(LinearLayout.HORIZONTAL);
			llEachLine.setGravity(Gravity.LEFT);
			 
			int nCountId = IdManager.getCommonOptionCountId(i);
			int nDownId = nCountId - 1000;
			int nUpId = nCountId + 1000;
			     
			// option name
			 
			String strTextTitle = TextFormatter.getText(_Option.NAME_ENG, _Option.NAME_OTH,26);
			 
			TextView tvOptionName = new TextView(this);
			tvOptionName.setGravity(Gravity.LEFT|Gravity.CENTER);
			tvOptionName.setText(strTextTitle);
			tvOptionName.setTextSize(14);
			tvOptionName.setTextColor(0xff000000);
			tvOptionName.setHeight(50);
			tvOptionName.setWidth(270);
			 
			// option count 
			 
			TextView tvCount = new TextView(this); 
			tvCount.setGravity(Gravity.CENTER);
			tvCount.setId(nCountId);
			tvCount.setText(String.valueOf("0"));
			tvCount.setTextColor(0xff000000);
			tvCount.setWidth(50);
			tvCount.setHeight(30);
			 
			_Option.MENU_RESOURCE_ID = mMenu.RESOURCE_ID;
			 
			ResourceManager.put(nCountId, tvCount);
			ResourceManager.put(tvCount, _Option);
			 
			// Button Down
			 
			ImageButton btDown = new ImageButton(this);
			btDown.setId(nDownId);
			btDown.setBackgroundDrawable(getResources().getDrawable(R.drawable.down));
			btDown.setLayoutParams(new LayoutParams(Property.POPUP_ARROW_IMG_WIDTH,Property.POPUP_ARROW_IMG_HEIGHT));
			btDown.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
 	            	
					int nDownId = v.getId();
 	            	int nCountId = nDownId+1000;
 	            	int nCount = 0;
 	            	
 	            	TextView tvCount = (TextView) ResourceManager.get(nCountId);
 	            	Option _Option = (Option)ResourceManager.get(tvCount);
 	            	
             		String strCount = (String) tvCount.getText();
             		nCount = Integer.valueOf(strCount);
 	            	
 	            	if(nCount>0) {
         				nCount -= 1;
 	                   	tvCount.setText(String.valueOf(nCount));
 	                   	_Option.ORDER_COUNT = nCount;
 	                    OrderManager.setOptionCount(_Option.DB_ID, nCount);
 	            	}
 	            }
             });
             
             // Button Up
             
             ImageButton btUp = new ImageButton(this);
             btUp.setId(nUpId);
             btUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.up));
             btUp.setLayoutParams(new LayoutParams(Property.POPUP_ARROW_IMG_WIDTH,Property.POPUP_ARROW_IMG_HEIGHT));
             btUp.setOnClickListener(new OnClickListener() {
 	            public void onClick(View v) {
 	            	
 	            	int nUpId = v.getId();
 	            	int nCountId = nUpId - 1000;
 	            	int nCount = 0;
 	            	
 	            	TextView tvCount = (TextView) ResourceManager.get(nCountId);
 	            	Option _Option = (Option)ResourceManager.get(tvCount);	
 	            	
 	            	String strCount = (String) tvCount.getText();
             		nCount = Integer.valueOf(strCount);
         		
 	            	nCount += 1;
 	            	
 	            	tvCount.setText(String.valueOf(nCount));
 	            	_Option.ORDER_COUNT = nCount;
                    OrderManager.setOptionCount(_Option.DB_ID, nCount);
  	            }
             });
             
             llEachLine.addView(tvOptionName);
             llEachLine.addView(tvCount);
             llEachLine.addView(btDown);
             llEachLine.addView(btUp);
             
             llCO.addView(llEachLine);
         }
 		
 	}
 	
 	// show common option list
 	private void showSpecificOptions (Menu mMenu, LinearLayout llCO) {
 		
 		ArrayList<Option> _SpecificOptionList = OptionManager.getSpecificOptionList(mMenu.DB_ID);
 		
         for(int i=0; i<_SpecificOptionList.size(); i++) {
         	
         	Option _Option = _SpecificOptionList.get(i);
         
         	LinearLayout llEachLine = new LinearLayout(this);
             llEachLine.setLayoutParams(new LayoutParams(500,LayoutParams.WRAP_CONTENT));
             llEachLine.setPadding(0, 0, 10, 4);
             llEachLine.setOrientation(LinearLayout.HORIZONTAL);
             llEachLine.setGravity(Gravity.LEFT);
         
             int nCountId = IdManager.getSpecificOptionCountId(i);
             int nDownId = nCountId - 1000;
             int nUpId = nCountId + 1000;
             
             // option name
             
             String strTextTitle = TextFormatter.getText(_Option.NAME_ENG, _Option.NAME_OTH,26);
             
             TextView tvOptionName = new TextView(this);
             tvOptionName.setGravity(Gravity.LEFT|Gravity.CENTER);
             tvOptionName.setText(strTextTitle);
             tvOptionName.setTextSize(14);
             tvOptionName.setTextColor(0xff000000);
             tvOptionName.setHeight(50);
             tvOptionName.setWidth(270);
             
             // option count 
             
             TextView tvCount = new TextView(this); 
             tvCount.setGravity(Gravity.CENTER);
             tvCount.setId(nCountId);
             tvCount.setText(String.valueOf("0"));
             tvCount.setTextColor(0xff000000);
             tvCount.setWidth(50);
             tvCount.setHeight(30);
             
             ResourceManager.put(nCountId, tvCount);
             ResourceManager.put(tvCount, _Option);
             
             _Option.MENU_RESOURCE_ID = mMenu.RESOURCE_ID;
             	
             // Button Down
             
             ImageButton btDown = new ImageButton(this);
             btDown.setId(nDownId);
             btDown.setBackgroundDrawable(getResources().getDrawable(R.drawable.down));
             btDown.setLayoutParams(new LayoutParams(60,60));
             btDown.setOnClickListener(new OnClickListener() {
 	            public void onClick(View v) {
 	            	
 	            	int nDownId = v.getId();
 	            	int nCountId = nDownId+1000;
 	            	int nCount = 0;
 	            	
 	            	TextView tvCount = (TextView) ResourceManager.get(nCountId);
 	            	Option _Option = (Option)ResourceManager.get(tvCount);
 	            	
             		String strCount = (String) tvCount.getText();
             		nCount = Integer.valueOf(strCount);
 	            	
 	            	if(nCount>0) {
         				nCount -= 1;
 	            		tvCount.setText(String.valueOf(nCount));
 	            		_Option.ORDER_COUNT = nCount;
 	            		OrderManager.setOptionCount(_Option.DB_ID, nCount);
 	            	}
 	            }
             });
             
             // Button Up
             
             ImageButton btUp = new ImageButton(this);
             btUp.setId(nUpId);
             btUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.up));
             btUp.setLayoutParams(new LayoutParams(60,60));
             btUp.setOnClickListener(new OnClickListener() {
 	            public void onClick(View v) {
 	            	
 	            	int nUpId = v.getId();
 	            	int nCountId = nUpId - 1000;
 	            	int nCount = 0;
 	            	
 	            	TextView tvCount = (TextView) ResourceManager.get(nCountId);
 	            	Option _Option = (Option)ResourceManager.get(tvCount);
 	            		
            			String strCount = (String) tvCount.getText();
             		nCount = Integer.valueOf(strCount);
 	            	nCount += 1;
 	            	
 	            	tvCount.setText(String.valueOf(nCount));
             		_Option.ORDER_COUNT = nCount;
             		OrderManager.setOptionCount(_Option.DB_ID, nCount);
 	            }
             });
             
             llEachLine.addView(tvOptionName);
             llEachLine.addView(tvCount);
             llEachLine.addView(btDown);
             llEachLine.addView(btUp);
             
             llCO.addView(llEachLine);
         }
 		
 	}
    // load Title Bar
  	public void loadTitleBar() {
  		
  		LinearLayout llTitleBar = (LinearLayout) findViewById(R.id.title_bar);
  		
  		int nTextColor = Property.TITLE_BAR_TEXT_COLOR;
  		
  		TextView tvLanguage = new TextView(this);
  		tvLanguage.setId(IdManager.getLanguageId_SelectMenu());
  		tvLanguage.setPadding(20, 0, 0, 0);
  		tvLanguage.setWidth(Property.TITLE_BAR_LANG_WIDTH);
  		tvLanguage.setTextColor(nTextColor);
  		tvLanguage.setTextSize(Property.TITLE_BAR_LANG_TEXT_SIZE);
  		
  		ResourceManager.put(tvLanguage.getId(), tvLanguage);
  		
  		tvLanguage.setText(TitleBarManager.getLanguage());
  		tvLanguage.setOnClickListener(new OnClickListener() { // Click on Language Button
              public void onClick(View v) {
              
              	TitleBarManager.toggleLanguage_SelectMenu(); // change language of menu
              	loadOrderBoard(); // Update language of ordered list
              }
  		});
  		
  		// Business Name
  		TextView tvBusinessName = new TextView(this);
  		tvBusinessName.setWidth(Property.TITLE_BAR_BUSINESS_NAME_WIDTH);
  		tvBusinessName.setGravity(Property.TITLE_BAR_GRAVITY_CENTER);
  		tvBusinessName.setPadding(0, 5, 0, 0);
  		tvBusinessName.setTextColor(nTextColor);
  		tvBusinessName.setTextSize(Property.TITLE_BAR_BUSINESS_NAME_TEXT_SIZE);
  		tvBusinessName.setText(TitleBarManager.getBusinessName());
  		
  		// User Id
  		TextView tvId = new TextView(this);
  		tvId.setWidth(Property.TITLE_BAR_ID_WIDTH);
  		tvId.setPadding(0, 0, 20, 0);
  		tvId.setGravity(Property.TITLE_BAR_GRAVITY_RIGHT);
  		tvId.setTextColor(nTextColor);
  		tvId.setTextSize(Property.TITLE_BAR_ID_TEXT_SIZE);
  		tvId.setText(TitleBarManager.getUserId());
  		
  		llTitleBar.addView(tvLanguage);
  		llTitleBar.addView(tvBusinessName);
  		llTitleBar.addView(tvId);
  	}
 	
 	// load category
 	public void loadCategoryLayouts() {
 		
 		LinearLayout categoryLayout = (LinearLayout)findViewById(R.id.category);
 		
 		int nTotalCate = CategoryManager.getSize();
 		int nMaxRows = 2;
 		int nMaxColumns = 4;
 		int nMaxTables = nTotalCate / 8;
 		
 		if ( (nTotalCate % 8) > 0 ) nMaxTables += 1;
     	
 		// for increasing category sequence
 		int nCounter = 0;
 		
 		// Max Table Nums
 		for (int i = 0; i < nMaxTables; i++) {
 			
 			TableLayout tableLayout = new TableLayout(this);
 			tableLayout.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
 			
      		// Table Rows
 	        for (int j = 0; j < nMaxRows; j++) {
 	        	
 	            TableRow tableRow = new TableRow(this);
 	            
 	            // Table Columns
 	            for (int k = 0; k < nMaxColumns; k++) {
 	            	
 	            	if (nCounter==nTotalCate) break;
 	            	
 	            	Button categoryButton = new Button(this);
 	        		
 	    			int nCateId = IdManager.getCategoryId(nCounter);
 	    			
 	    			categoryButton.setId(nCateId);
 	    			
 	    			String strTitle = CategoryManager.getCategoryNameByResourceId(nCateId);
 	
 	    			if(strTitle.length()>Property.CATE_TEXT_LENGTH_LIMIT) 
 	    				strTitle = strTitle.substring(0, Property.CATE_TEXT_LENGTH_LIMIT);
 	    			
 	    			categoryButton.setText(strTitle);
 	    			categoryButton.setTextSize(Property.CATE_TEXT_SIZE);
 	    			categoryButton.setTextColor(Property.CATE_TEXT_COLOR);
 	    			categoryButton.setHeight(Property.CATE_BUTTON_HEIGHT);
 	    			categoryButton.setWidth(Property.CATE_BUTTON_WIDTH);
 	    			categoryButton.setBackgroundResource(R.drawable.color_button_category);
 	    			
 	    			// click category
 	    			categoryButton.setOnClickListener(new OnClickListener() {
 	    	            public void onClick(View v) {
 	    	            	
 	    	            	int nCategoryId = v.getId();
 	    	            	int nMenuLayoutId = IdManager.getMenuLayoutIdByCategoryId(nCategoryId);
 	    	            	
 	    	            	toggleCategory(nCategoryId);
 	    	            	toggleMenuLayout(nMenuLayoutId);
 	    	            }
 	    			});
 	            	
 	    			ResourceManager.put(nCateId, categoryButton);
 	    			
 	    			nCounter++;
 	    			
 	    			tableRow.addView(categoryButton);
 	    			
 	            } // Table Columns
 	            
 	            tableLayout.addView(tableRow);
 	
 	        } // Table Rows
 	        
 	        categoryLayout.addView(tableLayout);

 		} // Table Nums
 			
 	}
 	
 	// load menu list
 	public void loadMenuLayouts() {
 		
 		FrameLayout MenuFrame = (FrameLayout)findViewById(R.id.frame_menu_list);
 		
 		int nMenuCounter = 0;
 		
 		// Menu Wrapper Layout
 		for(int i=0; i<CategoryManager.getSize(); i++) {
 			
 			LinearLayout llMenuLayout = new LinearLayout(this);
 			
 			int nCateId = IdManager.getCategoryId(i); 
 			int nMenuLayoutId = IdManager.getMenuLayoutId(i);
 			
 			llMenuLayout.setId(nMenuLayoutId);
 			llMenuLayout.setLayoutParams(new LayoutParams(Property.MENULIST_WIDTH, Property.MENULIST_HEIGHT));
 			llMenuLayout.setOrientation(Property.MENULIST_ORIENTATION);
 			llMenuLayout.setVisibility(LinearLayout.INVISIBLE);
 			llMenuLayout.setPadding(15, 15, 5, 5);
 			
 			ResourceManager.put(nMenuLayoutId, llMenuLayout);
 			
 			// Each Menu Layout
 			
 			ArrayList<Menu> listMenu = MenuManager.getListByCategory(nCateId);
 			
 			if(listMenu != null) {
 				
 				for(int j=0; j<listMenu.size(); j++) {
 					
 					Menu menu = listMenu.get(j);
 					
 					LinearLayout llMenu = new LinearLayout(this);
 					llMenu.setId(menu.RESOURCE_ID);
 					llMenu.setLayoutParams(new LayoutParams(Property.MENULAYOUT_WIDTH, Property.MENULAYOUT_HEIGHT));
 	
 					int nMenuTextId = IdManager.getMenuTextId(nMenuCounter);
 					
 					// Menu Text
 					
 					String strMenuName = "";
 					
 					if(Property.MENU_LANGUAGE.equals(Property.MenuLanguage.ENGLISH)) strMenuName = menu.NAME_ENG;
 					else	strMenuName = menu.NAME_OTH;
 					
 					TextView tvMenuText = new TextView(this);
 					tvMenuText.setId(nMenuTextId);
 					tvMenuText.setText(strMenuName);
 					tvMenuText.setTextSize(Property.MENU_TEXT_SIZE);
 					tvMenuText.setTextColor(Property.MENU_TEXT_COLOR);
 					tvMenuText.setWidth(Property.MENU_TEXT_WIDTH);
 					tvMenuText.setHeight(Property.MENU_TEXT_HEIGHT);
 					
 					MenuManager.putResourceIdByTextId(nMenuTextId, menu.RESOURCE_ID);
 					ResourceManager.put(nMenuTextId, tvMenuText);
 					
 					// Click
 					tvMenuText.setOnTouchListener(new OnTouchListener() {
 				        public boolean onTouch(View v, MotionEvent event) {
 				        	History.putView(v);
 				            _MenuGesture.onTouchEvent(event);
 				            return true;
 				        }
 					});
 
 					llMenu.addView(tvMenuText);
 					llMenuLayout.addView(llMenu);			
 					
 					nMenuCounter++;
 				}
 			}

 			MenuFrame.addView(llMenuLayout);
 		}
 		
 	}
 	
 	// load Order Board
 	public void loadOrderBoard() {
 		
 		LinearLayout llOrderBoardLayout = (LinearLayout)findViewById(R.id.order_layout);
 		
 		llOrderBoardLayout.removeAllViews();
 		
 		int nHallNo = IdManager.getHallNo(History.getHallResourceId());
 		int nTableNo = IdManager.getTableNo(History.getHallResourceId(), History.getTableResourceId());
 		
 		ArrayList<Order> _OrderList = OrderBoardManager.getTableOrderList(nHallNo, nTableNo);
 		
 		for(int i=0; i<_OrderList.size(); i++) {
 			
 			Order _Order = _OrderList.get(i);
 			
 			// layer
 			LinearLayout llEachOrderLayout = new LinearLayout(this);
 			llEachOrderLayout.setLayoutParams(new LayoutParams(Property.ORDER_BOARD_LAYOUT_WIDTH, Property.ORDER_BOARD_LAYOUT_HEIGHT));
 			
 				// order text
 				TextView tvOrderBoardText = new TextView(this);
 				
 				int nOrderBoardTextId = IdManager.getOrderBoardTextId(i);
 				
 				ResourceManager.put(nOrderBoardTextId, _Order);
 				
 				tvOrderBoardText.setId(nOrderBoardTextId);
 				tvOrderBoardText.setHeight(Property.ORDER_BOARD_TEXT_HEIGHT);
 				tvOrderBoardText.setWidth(Property.ORDER_BOARD_TEXT_WIDTH);
 				tvOrderBoardText.setTextSize(Property.ORDER_BOARD_TEXT_SIZE);
 				tvOrderBoardText.setTextColor(Property.ORDER_BOARD_TEXT_COLOR);
 				tvOrderBoardText.setOnTouchListener(new OnTouchListener() {
 			        public boolean onTouch(View v, MotionEvent event) {
 			        	History.putView(v);
 			            _OrderBoardGesture.onTouchEvent(event);
 			            return true;
 			        }
 				});
 				
 				StringBuffer strFormat = new StringBuffer();
 				
 				int nOrderId = _Order.ORDER_ID;
 				int nOptionCommonId = _Order.OPTION_COMMON_ID;
 				int nOptionSpecificId = _Order.OPTION_SPECIFIC_ID;
 				
 				String strNameEng = "";
 				String strNameOth = "";
 				int nOrderCount = _Order.ORDER_COUNT;
 				
 				if(Property.MENU_LANGUAGE.equalsIgnoreCase(Property.MenuLanguage.ENGLISH)) {
 					strNameEng = _Order.NAME_ENG.replaceAll("\\s", "");
 					strNameOth = _Order.NAME_OTH.replaceAll("\\s|\\*", "");;
 					if(nOptionSpecificId>0 || nOptionCommonId>0) strNameEng = " -- " + strNameEng;
 				} else {
 					strNameOth = _Order.NAME_OTH.replaceAll("\\s", "");
 					strNameEng = _Order.NAME_ENG.replaceAll("\\s|\\*", "");;
 					if(nOptionSpecificId>0 || nOptionCommonId>0) strNameOth = " -- " + strNameOth;
 				}
 				
 				if(nOptionCommonId == 0 && nOptionSpecificId==0) strFormat.append( "[" + nOrderId + "] ");
 				
 				strFormat.append( TextFormatter.getText(strNameEng,strNameOth,Property.ORDER_BOARD_CUT_TEXT) );
 				
 				tvOrderBoardText.setText(strFormat.toString());
 				
 				// order count
 				
 				TextView tvOrderBoardCount = new TextView(this);
 				tvOrderBoardCount.setTextSize(Property.ORDER_BOARD_TEXT_SIZE);
 				tvOrderBoardCount.setTextColor(Property.ORDER_BOARD_TEXT_COLOR);
 				tvOrderBoardCount.setWidth(40);
 				tvOrderBoardCount.setHeight(Property.ORDER_BOARD_TEXT_HEIGHT);
 				tvOrderBoardCount.setText(String.valueOf(nOrderCount));
 		
 			llEachOrderLayout.addView(tvOrderBoardText);
 			llEachOrderLayout.addView(tvOrderBoardCount);
 			
 			llOrderBoardLayout.addView(llEachOrderLayout);
 			
 					
 		} // for

 	}
 	
 	// load Navigation Bar & Back Button
 	public void loadBottomButton() {
 		
 		Button confirmButton = (Button)findViewById(R.id.confirm);
 		confirmButton.setText(CaptionManager.getCaption().CONFIRM);
 		
 		// confirm order
 		confirmButton.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
            	 
             	OrderManager.confirmOrder(OrderManager.getOrderId());
            	finish();
             }
 		});
 	}
    
    // Cancel Order
    private void cancelOrder(View v) {
    	
    	int nOrderBoardTextId = v.getId();
    	Order order = (Order)ResourceManager.get(nOrderBoardTextId);
    	
    	if(order != null) {
    		OrderManager.cancelOrder(order);
    		OrderManager.setOrderId(order.ORDER_ID);
    	}
		loadOrderBoard();
    }
    
	// toggle category
	public void toggleCategory (int nCateLayoutId) {
		
		for(int i=0; i<CategoryManager.getSize(); i++) {

			int nCurrentId =  Id.getCateId() + i;
			Button currentCategoryButton = (Button)ResourceManager.get(nCurrentId);
			currentCategoryButton.setSelected(false);
		}
		
		Button showCategoryButton = (Button)ResourceManager.get(nCateLayoutId);
		showCategoryButton.setSelected(true);
	}

	// toggle menu
	public void toggleMenuLayout(int nMenuLayoutId) {
		
		for(int i=0; i<CategoryManager.getSize(); i++) {
			LinearLayout MenuLayout = (LinearLayout)findViewById(Id.getMenuLayoutId() + i);
			MenuLayout.setVisibility(LinearLayout.INVISIBLE);
		}
		
		LinearLayout MenuLayout = (LinearLayout)findViewById(nMenuLayoutId);
		MenuLayout.setVisibility(LinearLayout.VISIBLE);
		
		// each menu layout must show a top of list
		ScrollView sv = (ScrollView)findViewById(R.id.menu_scroll);
		sv.fullScroll(ScrollView.FOCUS_UP);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onBackPressed(){
		OrderManager.removeUnconfirmedOrder(OrderManager.getOrderId());
		super.onBackPressed();
	}
}