package morning.v1.POS4US.Util;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * Manage Image
 */
public class ImageManager {
	
	public Bitmap LoadImage(String strImgUrl) { 
		
		Bitmap bitmap = null;
		InputStream in = null;       
		
		try {
			BitmapFactory.Options bmOptions;
    	    bmOptions = new BitmapFactory.Options();
    	    bmOptions.inJustDecodeBounds = false;
    	    bmOptions.inSampleSize = 1;
			
    	    in = HttpManager.OpenHttpConnection(strImgUrl,"GET");
    	    bitmap = BitmapFactory.decodeStream(in, null, bmOptions);
    	    in.close();
		} catch (IOException e1) {}
		
       return bitmap;               
   }
}
