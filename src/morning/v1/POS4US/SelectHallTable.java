package morning.v1.POS4US;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.widget.FrameLayout;

import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.DataObject.Id;
import morning.v1.POS4US.Manager.General.CaptionManager;
import morning.v1.POS4US.Manager.General.HallManager;
import morning.v1.POS4US.Manager.General.IdManager;
import morning.v1.POS4US.Manager.General.OrderManager;
import morning.v1.POS4US.Manager.General.TableManager;
import morning.v1.POS4US.Manager.General.TableStatusManager;
import morning.v1.POS4US.Manager.General.TitleBarManager;
import morning.v1.POS4US.Manager.System.ActivityManager;
import morning.v1.POS4US.Manager.System.History;
import morning.v1.POS4US.Manager.System.NavigationManager;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Manager.System.ThreadManager;
import morning.v1.POS4US.Manager.System.UserManager;

/*
 * Select a Hall & Table
 */
public class SelectHallTable extends Activity {
	
	// Self Resource Instance
	private final Context context = this;
	
	// Finger gesture detector
    private GestureDetector _gesture;
	
	// @override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_hall_and_table);
		
		if (NavigationManager.isCalledByPrevious()) {
			
			loadTitleBar();
			loadHallLayouts();
			loadTableLayouts();
			loadDefaultHall();
			
			runThread();
			
			// For detecting Finger Gesture
			_gesture = new GestureDetector(this, new onGestureListener());
		}
	}
	
	// Continuous Listening on Gesture
    private final class onGestureListener extends GestureDetector.SimpleOnGestureListener {

    	// click - order
    	public boolean onSingleTapConfirmed (MotionEvent e) {
    		View v = History.getView();
    		onSingleTapConfirmedProc(v);
    		return false;
    	}

    	// double click - change status into 'COMPLETED'
    	public boolean onDoubleTap (MotionEvent e) {
    		View v = History.getView();
    		onDoubleTapProc(v);
    		return false;
    	}
    }
    
    // click
    public void onSingleTapConfirmedProc(View v) {
    	
    	int nTableId = v.getId();
    	int nHallNo = IdManager.getHallNoByTableId(nTableId);
		int nHallId = IdManager.getHallId(nHallNo);
    	int nTableNo = IdManager.getTableNo(nHallId, nTableId);
    
    	History.putTableResourceId(nTableId);
    	
		OrderManager.setHallNo(nHallNo + 1);
		OrderManager.setTableNo(nTableNo + 1);
		
		NavigationManager.setCaller(NavigationManager.PREVIOUS);
		
		// Call 'SelectMenu' Activity
		ActivityManager.call(this, SelectMenu.class);
    }
    
    // Change Status into 'COMPLETED'
    public void onDoubleTapProc(View v) {
    	
    	int nTableButtonId = v.getId();
    	int nHallNo = IdManager.getHallNoByTableId(nTableButtonId);
		int nHallId = IdManager.getHallId(nHallNo);
    	int nTableNo = IdManager.getTableNo(nHallId, nTableButtonId);
    
    	//Button button = (Button)ResourceManager.get(nTableButtonId);
    	
    	int nStatus = TableStatusManager.getStatus(nTableButtonId);
    	
    	if(nStatus > -1) {
    		
    		switch(nStatus) {
    		
    			case TableStatusManager.ORDERED: // Click on Ordered Menu -> Change Status to 'COMPLETED'
    				
    				requestUpdateOrderStatus(nHallNo,nTableNo,"COMPLETED");
    				//button.setBackgroundResource(R.drawable.color_table_complete);
    				TableStatusManager.setStatus(nTableButtonId,TableStatusManager.COMPLETED);
    				break;
    			
    			/*
    			case TableStatusManager.COMPLETED: // Click on Completed Menu -> Change Status to 'EMPTY'
    				
    				requestUpdateOrderStatus(nHallNo,nTableNo,"EMPTY");
    				button.setBackgroundResource(R.drawable.color_table_empty);
    				TableStatusManager.setStatus(nTableButtonId,TableStatusManager.EMPTY);
    				break;
    			*/
			}
    	}
		
    }
    
	// update table status by server data
	public void runThread() {
		
		ThreadManager.setCondition(true);
		ThreadManager.initHandler();
		ThreadManager.runThread(new Runnable() {
			public void run() {
			      while (ThreadManager.getCondition()) {
			    	  ThreadManager.callHandler();
			    	  ThreadManager.sleep();
			      }
			}
		});
		
	}
	
	// Set Visual Table Layout
	public void loadTableLayouts() { // Table Frame > Table Layout > Rows > Columns > Frame (Button, No, Seats)
		
		// Table Frame
		FrameLayout tableFrame = (FrameLayout)findViewById(R.id.table_frame);
		TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(Property.TABLE_WIDTH,Property.TABLE_HEIGHT);
		
		// Table Layout per Hall
		for(int i=0; i<HallManager.getSize(); i++) {
			
			int nTableAmounts = HallManager.getHallMap().get(Id.getHallId()+i).TABLE_NUM;
			int nRows = nTableAmounts / Property.TABLE_MAX_COLS + 1;
			int nTableNumsOfLastRow = nTableAmounts % Property.TABLE_MAX_COLS;
			
			int nTableLayoutId = IdManager.getTableLayoutId(i); 
			int nTableNo = 0;
			
			TableLayout tableLayout = new TableLayout(this);
		 	tableLayout.setId(nTableLayoutId);
			tableLayout.setVisibility(TableLayout.INVISIBLE);
		    
			// Table Rows
	        for (int j = 0; j < nRows; j++) {
	            TableRow tableRow = new TableRow(this);
	            int nColumns = Property.TABLE_MAX_COLS;
	            
	            if (j == (nRows-1))
	            	nColumns = nTableNumsOfLastRow;
	            
	            // Table Columns
	            for (int k = 0; k < nColumns; k++) {
	            	int nTableId = IdManager.getTableId(i, nTableNo);
	            	
	            	// Table Frame
	            	FrameLayout frameInRow = new FrameLayout(this);
	            	frameInRow.setLayoutParams(tableRowParams);
	            	
	            	// Table Button
	            	Button tableButton = new Button(this);
	            	tableButton.setId(nTableId);
	            	tableButton.setBackgroundResource(R.drawable.color_table_empty);
	            	
	            	// Click on Table
	            	tableButton.setOnTouchListener(new OnTouchListener() {
				        public boolean onTouch(View v, MotionEvent event) {
				        	History.putView(v);
				            _gesture.onTouchEvent(event);
				            return true;
				        }
	
					});
	            	
	            	TableStatusManager.setStatus(nTableId, TableStatusManager.EMPTY);
	            	
	            	ResourceManager.put(nTableId, tableButton); // for resource control 
	            	TableManager.putTable(nTableId, tableButton); // for update table status - never remove
	            	
	            	// Table No
	                TextView tvTableNo = new TextView(this);
	                tvTableNo.setText(String.valueOf(nTableNo+1));
	                tvTableNo.setTextSize(Property.TABLE_NUM_FONT_SIZE);
	                tvTableNo.setTextColor(Property.TABLE_NUM_COLOR);
	                tvTableNo.setPadding(40, 28, 0, 0);
	                
	                // Num of Orders
	                TextView tvNumOfOrders = new TextView(this);
	                String strOrderStatus = "0/0";
	                tvNumOfOrders.setText(strOrderStatus);
	                tvNumOfOrders.setTextSize(Property.TABLE_NUM_OF_ORDER_FONT_SIZE);
	                tvNumOfOrders.setTextColor(Property.TABLE_STATUS_COLOR);
	                tvNumOfOrders.setPadding(15, 10, 0, 0);
	                
	                ResourceManager.put(tableButton, tvNumOfOrders);
	              
	                // Attach
	                frameInRow.addView(tableButton);
	                frameInRow.addView(tvTableNo);
	                frameInRow.addView(tvNumOfOrders);
	                
	                tableRow.addView(frameInRow);
	                
	                nTableNo++;
	                
	            } // Table Columns
	            
	            tableLayout.addView(tableRow);

	        } // Table Rows
	        
			tableFrame.addView(tableLayout);
			
			ResourceManager.put(nTableLayoutId, tableLayout);
			
		} // Table Layout per Hall
	}
	
	// request order update 
	public void requestUpdateOrderStatus(int nHallNum, int nTableNum, String strStatus) {
		
		TableStatusManager.changeStatus(nHallNum, nTableNum, strStatus);	
	}
	
	// Toggle to show tables for the hall
	public void toggleTableLayout(int nUserTableLayoutId) {
		
		for(int i=0; i<HallManager.getSize(); i++) {
			
			int nTableLayoutId = Id.getTableLayoutId() + i;
			TableLayout tableLayout = (TableLayout)ResourceManager.get(nTableLayoutId);
			tableLayout.setVisibility(TableLayout.INVISIBLE);
		}
		
		TableLayout showTableLayout = (TableLayout)ResourceManager.get(nUserTableLayoutId);
		showTableLayout.setVisibility(TableLayout.VISIBLE);
	}
	
	// Set Hall
	public void loadHallLayouts() {
		
		LinearLayout hallLayout = (LinearLayout)findViewById(R.id.hall_layout);
		
		for(int i=0; i<HallManager.getSize(); i++) {
			
			// make hall button
			Button hallButton = new Button(this);
			
			int nHallId = Id.getHallId() + i;
			
			hallButton.setId(nHallId);
			hallButton.setText(HallManager.getHallNameByResourceId(nHallId));
			hallButton.setTextSize(Property.HALL_NAME_FONT_SIZE);
			hallButton.setTextColor(Property.HALL_NAME_FONT_COLOR);
			hallButton.setBackgroundResource(R.drawable.color_halls);
			
			// must use to decide width & height dynamically
			LinearLayout.LayoutParams lParam = new LinearLayout.LayoutParams(Property.HALL_WIDTH, Property.HALL_HEIGHT); 
			
			// space between hall buttons
			if(i>0) lParam.setMargins(Property.HALL_BUTTON_SPACE, 0, 0, 0);
			
			hallButton.setLayoutParams(lParam);
			
			hallButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	
                	int nHallId = v.getId();
                	int nTableLayoutId = nHallId - Id.getHallId() + Id.getTableLayoutId();
                	
                	toggleHall(v.getId());
                	toggleTableLayout(nTableLayoutId);
                	
                	History.putHallResourceId(nHallId);
                }
			});
			
			hallLayout.addView(hallButton);
			
			ResourceManager.put(nHallId, hallButton); // it needs to toggle hall menu
		
		}// for
	}
	
	// select hall
	public void toggleHall(int nTargetHallId) {
		
		for(int i=0; i<HallManager.getSize(); i++) {

			int nCurrentId =  Id.getHallId() + i;
			Button CurrenthallButton = (Button)ResourceManager.get(nCurrentId);
			CurrenthallButton.setSelected(false);
		}
		
		Button showHallButton = (Button)ResourceManager.get(nTargetHallId);
		showHallButton.setSelected(true);
	}
	
	// load Title Bar
	public void loadTitleBar() {
		
		LinearLayout llTitleBar = (LinearLayout) findViewById(R.id.title_bar);
 		
 		int nTextColor = Property.TITLE_BAR_TEXT_COLOR;
 		
 		TextView tvLanguage = new TextView(this);
 		tvLanguage.setId(IdManager.getLanguageId_SelectHallTable());
 		tvLanguage.setPadding(20, 0, 0, 0);
 		tvLanguage.setWidth(Property.TITLE_BAR_LANG_WIDTH);
 		tvLanguage.setTextColor(nTextColor);
 		tvLanguage.setTextSize(Property.TITLE_BAR_LANG_TEXT_SIZE);
 		
 		ResourceManager.put(tvLanguage.getId(), tvLanguage);
 		
 		tvLanguage.setText(TitleBarManager.getLanguage());
 		tvLanguage.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
             
             	TitleBarManager.toggleLanguage_SelectHallTable();
             }
 		});
 		
 		TextView tvBusinessName = new TextView(this);
 		tvBusinessName.setWidth(Property.TITLE_BAR_BUSINESS_NAME_WIDTH);
 		tvBusinessName.setGravity(Property.TITLE_BAR_GRAVITY_CENTER);
 		tvBusinessName.setPadding(0, 5, 0, 0);
 		tvBusinessName.setTextColor(nTextColor);
 		tvBusinessName.setTextSize(Property.TITLE_BAR_BUSINESS_NAME_TEXT_SIZE);
 		tvBusinessName.setText(TitleBarManager.getBusinessName());
 		
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
	
	// load particular hall by default
	public void loadDefaultHall() {
		
		int nHallId = Id.getHallId() + Property.HALL_DEFAULT_SHOW_NUMBER;
		int nTableLayoutId = Id.getTableLayoutId() + Property.HALL_DEFAULT_SHOW_NUMBER;
		
		Button hall = (Button) ResourceManager.get(nHallId);
		hall.setSelected(true);
		
		toggleTableLayout(nTableLayoutId);
		History.putHallResourceId(nHallId);
	}
	
	// @override
	protected void onResume() {
		super.onResume();
	}
	
	// @override
	public void onBackPressed() {

		NavigationManager.setCaller(NavigationManager.NEXT);
		
		AlertDialog.Builder ad = new AlertDialog.Builder(context);

		ad.setTitle(Property.PROVIDER_NAME);
		ad.setMessage(CaptionManager.getCaption().Q_LOGOUT);

		ad.setPositiveButton(CaptionManager.getCaption().YES, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				UserManager.clearSession();
				finish();
			}
		});
		
		ad.setNegativeButton(CaptionManager.getCaption().NOP,new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {}
		});

		ad.show();

	}
	
}