package morning.v1.POS4US.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import morning.v1.POS4US.Configuration.ErrorMsg;

import android.content.Context;

public class FileManager {
	
	private Context context = null;
	
	public FileManager(){}
	public FileManager(Context context) { this.context = context; }
	
	public String writeString(String strString, String strFilePath) {
		
		strString = StringUtil.notNull(strString);
		
		try {
			if(strString.length()>0) {
				FileOutputStream fos = context.openFileOutput(strFilePath, Context.MODE_PRIVATE);
				fos.write(strString.getBytes());
				fos.close();
			}
		}catch(IOException e) {
			return ErrorMsg.IO_EXCEPTION;
		}
		return ErrorMsg.FILE_CREATED;
	}
	
	public String readString(String strFilePath) {

		FileInputStream fis;
		StringBuffer content = new StringBuffer();
		String temp="";
		strFilePath = StringUtil.notNull(strFilePath);
		
		try {
			fis = context.openFileInput(strFilePath); 

			if(fis != null) {
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
				while( (temp = bufferReader.readLine()) != null ) {
					content.append(temp);
				}
			}
		} catch (FileNotFoundException e) {
			return ErrorMsg.FILE_NOT_FOUND;
		} catch (IOException e) {
			return ErrorMsg.IO_EXCEPTION;
		}
		return content.toString();
	}
	
}
