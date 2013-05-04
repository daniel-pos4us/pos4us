package morning.v1.POS4US;

import morning.v1.POS4US.Configuration.ErrorMsg;
import morning.v1.POS4US.Configuration.Property;
import morning.v1.POS4US.Configuration.ServerProperty;
import morning.v1.POS4US.Manager.General.CaptionManager;
import morning.v1.POS4US.Manager.General.IdManager;
import morning.v1.POS4US.Manager.General.MasterManager;
import morning.v1.POS4US.Manager.System.ActivityManager;
import morning.v1.POS4US.Manager.System.ConnectionManager;
import morning.v1.POS4US.Manager.System.LoadManager;
import morning.v1.POS4US.Manager.System.NavigationManager;
import morning.v1.POS4US.Manager.System.PermissionManager;
import morning.v1.POS4US.Manager.System.ResourceManager;
import morning.v1.POS4US.Manager.System.UserManager;
import morning.v1.POS4US.Util.FileManager;
import morning.v1.POS4US.Util.MessageBox;
import morning.v1.POS4US.Util.StringUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Context;
import android.content.DialogInterface;

/*
 * Main Interface (Log In Screen)
 */
public class POS4US extends Activity implements RadioGroup.OnCheckedChangeListener {
	
	private Context context = this; // self pointer
	private RadioGroup radioGroup1; // how to get access the server : file or input
	private GestureDetector _gestureNormal; // gesture detector
	private PermissionManager permissionManager = new PermissionManager(this);
	private FileManager fileManager = new FileManager(this); // file to save or get the server IP address
	private ConnectionManager connectionManager = new ConnectionManager(); // manage network & server access
	
	@Override
	public void onCreate(Bundle savedInstanceState) { // called 1st automatically
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // remove system title bar
		setContentView(R.layout.main); // load xml setting of main layout
		
		ActivityManager.setActivity(this);
		
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup1.setOnCheckedChangeListener(this);
		
		TextView tvServerIp = (TextView) findViewById(R.id.input_ip);
		tvServerIp.setEnabled(false);
		
		int TmpId = IdManager.getRandomId();
		IdManager.setTemporaryId(TmpId);
		ResourceManager.put(IdManager.getTemporaryId(), this);
	
		getServerIpFromFile();
		
		// For detecting Finger Gesture
        _gestureNormal = new GestureDetector(this, new onGestureListener());

        // Android System Calls 'onResume' Automatically
	}
	
	@Override
    protected void onResume() { // called 2ndly automatically
        super.onResume();

        if(connectionManager.IsWifiAvailable(this)) {
        	Button confirm = (Button) findViewById(R.id.button_confirm); // confirm button
    		confirm.setOnTouchListener(new OnTouchListener() {
    	        public boolean onTouch(View v, MotionEvent event) {
    	        	_gestureNormal.onTouchEvent(event);
    	            return true;
    	        }
    		});	
    		
        }else {
          MessageBox.show("Please Check WiFi Connection", 100, 200, this);
        }
	}
	
	// get server ip from file
	private void getServerIpFromFile() {
		
		String strServerIp = StringUtil.notNull(fileManager.readString(ServerProperty.getStorageNameOfServerIpAddress()));
		
		if(strServerIp.equals(ErrorMsg.FILE_NOT_FOUND) || strServerIp.equals(ErrorMsg.IO_EXCEPTION)) {
			strServerIp = ServerProperty.getServerIp();
		}
		ServerProperty.setServerIp(strServerIp);
		
		TextView tvInputIp = (TextView) findViewById(R.id.input_ip);
		tvInputIp.setText(strServerIp);
	}
	
	// get server ip from input editor
	private void getServerIpFromInput() {
		
		TextView tvInputIp = (TextView) findViewById(R.id.input_ip);
		String strServerIp = tvInputIp.getText().toString();
		
		fileManager.writeString(strServerIp, ServerProperty.getStorageNameOfServerIpAddress());
		ServerProperty.setServerIp(strServerIp);
	}
	
	// go to login
    private final class onGestureListener extends GestureDetector.SimpleOnGestureListener {
    	public boolean onSingleTapConfirmed (MotionEvent e) {
    		
    		// get ip
    		switch(Property.SERVER_ACCESS_MODE) {
		    	case Property.SERVER_ACCESS_FILE_MODE:
		    		getServerIpFromFile();
		    		break;
		    	case Property.SERVER_ACCESS_INPUT_MODE:
		    		getServerIpFromInput();
	    		break;
	    	}
    		goLogin();
    		
    		return false;
    	}
    }
    
    // go login
    private void goLogin() {

		EditText id = (EditText) findViewById(R.id.input_id);
		EditText password = (EditText) findViewById(R.id.input_password);
		
		String strId = StringUtil.notNull(id.getText().toString());
		String strPassword = StringUtil.notNull(password.getText().toString());
		
		int nResult = UserManager.login(strId,strPassword);
		
    	switch(nResult) {
			case ErrorMsg.LOGIN_SUCCESS:
				Context con = (Context) ResourceManager.get(IdManager.getTemporaryId());
	    		if(permissionManager.haveAccessPermission()) {
	    			if(MasterManager.loadMaster()) {  // load master table info
	    				LoadManager.loadBusinessData(); // load hall/table/category/menu/option etc
	    			}
	    			ActivityManager.call(this, SelectHallTable.class);
	    			NavigationManager.setCaller(NavigationManager.PREVIOUS);
	    		}else{
	    			MessageBox.show("Cannot launch on an unregistered device", 100, 100, con);
	    		}
			break;
			
			case ErrorMsg.AUTHENTICATION_FAILURE:
				 MessageBox.show("Please check your ID or Password",250,320,context); 
				 id.requestFocus(); 
			break;
			
			case ErrorMsg.SERVER_OPERATION_CEASED:
				MessageBox.show("Please turn on the POS Server",250,320,context); 
				id.requestFocus(); 
			break;
			
			case ErrorMsg.DEAD_SERVER_IP_OR_STATUS:
				MessageBox.show("Please check the server IP or status",250,320,context); 
			break;
		}
    }
    
	@Override
	// if pressed on back button
	public void onBackPressed(){
		
		AlertDialog.Builder ad = new AlertDialog.Builder(this);

		ad.setTitle(Property.PROVIDER_NAME)
			.setMessage(CaptionManager.getCaption().Q_EXIT)
			.setPositiveButton(CaptionManager.getCaption().YES, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
	
					LoadManager.unloadAllManagers();
					
					moveTaskToBack(true);
					finish();
					android.os.Process.killProcess(android.os.Process.myPid()); // kill this application
				}
			})
			.setNegativeButton(CaptionManager.getCaption().NOP,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {}
			})
			.show();
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		TextView tvServerIp = (TextView) findViewById(R.id.input_ip);
		
		if(group == radioGroup1) {
			switch(checkedId) {
				case R.id.radio_file:
					getServerIpFromFile();
					Property.SERVER_ACCESS_MODE = Property.SERVER_ACCESS_FILE_MODE;
					tvServerIp.setEnabled(false);
					break;
				case R.id.radio_input:
					Property.SERVER_ACCESS_MODE = Property.SERVER_ACCESS_INPUT_MODE;
					tvServerIp.setEnabled(true);
					break;
			}
		}
		
	}
}