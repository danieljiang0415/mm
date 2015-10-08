package com.example.tutorial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import de.robv.android.xposed.XposedBridge;

public class Utility {
	
	 public static void setFinalStatic( String className, String fieldName, Object newValue) throws Exception {  
	    	Class<?> Clas = Class.forName(className);
	    	Field field = Clas.getDeclaredField(fieldName);
	        field.setAccessible(true);
	        field.set(null, newValue);
	    }
	 
	 public static Object getFieldValue(String className, String fieldName, Object obj ) throws Exception {
	    	Class<?> Clas = Class.forName(className);
	    	Field field = Clas.getDeclaredField(fieldName);
	        field.setAccessible(true);
	        return field.get(obj);
	    }
	 
	    public static String byte2HexStr(byte[] b) {  
	        String hs = "";  
	        String stmp = "";  
	        for (int n = 0; n < b.length; n++) {
	            stmp = (Integer.toHexString(b[n] & 0XFF));  
	            if (stmp.length() == 1)  
	                hs = hs + "0" + stmp + " ";  
	            else  
	                hs = hs + stmp + " ";  
	            
	            if ((n+1) % 16 == 0) {
	            	for (int j = n -15; j < n+1; j++ ) {
	            		char c = b[j]<32?'.':(char)b[j];
	            		hs = hs + c;
	            	}
	            	hs=hs+"\r\n"; 
	            }
	            if (n == b.length -1 && (b.length+1) % 16 != 0) {
	            	for (int j = 0; j < 16 - b.length%16; j++)
	            		hs = hs + "   ";
	            	
	            	for (int j = b.length/16*16; j < b.length; j++ ) {
	            		char c = b[j]<32?'.':(char)b[j];
	            		hs = hs + c;
	            	}
	            	hs=hs+"\r\n"; 
	            }
	        }  
	        return hs;  
	    } 
	    
	    @SuppressWarnings("rawtypes")
	    public static void getMethodInfo(Class clazz) {

			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
			    String methodName = method.getName();
			    //System.out.println("方法名称:" + methodName);
			    XposedBridge.log("方法名称:" + methodName);
			    Class<?>[] parameterTypes = method.getParameterTypes();
			    for (Class<?> clas : parameterTypes) {
			        String parameterName = clas.getName();
			        //System.out.println("参数名称:" + parameterName);
			        XposedBridge.log("参数名称:" + parameterName);
			    }
			}
	    }
	    
	    public static void writeMem2SDFile(String fileName, byte[] inputStream) {  
	        String sdStatus = Environment.getExternalStorageState();  
	        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
	            Log.d("TestFile", "SD card is not avaiable/writeable right now.");  
	            return;  
	        }  
	        try {  
	            String pathName="/storage/sdcard1/";
	            //String fileName="file.txt";  
	            File path = new File(pathName);  
	            File file = new File(pathName + fileName);  
	            if( !path.exists()) {  
	                Log.d("TestFile", "Create the path:" + pathName);  
	                path.mkdir();  
	            }  
	            if( !file.exists()) {  
	                Log.d("TestFile", "Create the file:" + fileName);  
	                file.createNewFile();  
	            }  
	            FileOutputStream stream = new FileOutputStream(file, true);  
	            //String s = "this is a p";  
	            //byte[] buf = s.getBytes();  
	            stream.write(inputStream);            
	            stream.close();  
	              
	        } catch(Exception e) {  
	            Log.e("TestFile", "Error on writeFilToSD.");  
	            e.printStackTrace();  
	        }  
	    } 
	    public synchronized static void writeLog2SDFile(String fileName, String tagInfo, String logInfo, boolean isMemdump) {  
	        String sdStatus = Environment.getExternalStorageState();  
	        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
	            Log.d("TestFile", "SD card is not avaiable/writeable right now.");  
	            return;  
	        }  
	        try {  
	            String pathName="/storage/sdcard1/";
	            //String fileName="file.txt";  
	            File path = new File(pathName);  
	            File file = new File(pathName + fileName);  
	            if( !path.exists()) {  
	                Log.d("TestFile", "Create the path:" + pathName);  
	                path.mkdir();  
	            }  
	            if( !file.exists()) {  
	                Log.d("TestFile", "Create the file:" + fileName);  
	                file.createNewFile();  
	            }  
	            FileOutputStream stream = new FileOutputStream(file, true);  
	            String trdInfo = String.format("%-8s", ("[~"+String.valueOf(Thread.currentThread().getId())+"]" ));
	            String s =  trdInfo + tagInfo +"  "+ (isMemdump? "\r\n"+logInfo : logInfo) + "\r\n";  
	            byte[] buf = s.getBytes();  
	            stream.write(buf);            
	            stream.close();  
	              
	        } catch(Exception e) {  
	            Log.e("TestFile", "Error on writeFilToSD.");  
	            e.printStackTrace();  
	        }  
	    } 
	    
	    public static String StackTrace() {
	    	String stringStackTrace = "";
	        StackTraceElement[] stackElements = new Throwable().getStackTrace();
	        if(stackElements != null)
	        {
	            for(int i = 0; i < stackElements.length; i++)
	            {
	                //System.out.println(""+ stackElements[i]);
	            	stringStackTrace += stackElements[i];
	            	stringStackTrace += "\r\n";
	            }
	        }
	        return stringStackTrace;
	    }
	    
	    public static byte[] subBytes(byte[] src, int begin, int count) {
	        byte[] bs = new byte[count];
	        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
	        return bs;
	    }

}
