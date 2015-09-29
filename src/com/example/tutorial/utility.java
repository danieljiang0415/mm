package com.example.tutorial;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
	                hs = hs + "0" + stmp;  
	            else  
	                hs = hs + stmp;  
	            // if (n<b.length-1) hs=hs+":";  
	        }  
	        return hs.toUpperCase();  
	    } 
	    
	    @SuppressWarnings("rawtypes")
	    public static void getMethodInfo(Class clazz) {
	    	//Class clazz = XposedHelpers.findClass(className, lpparam.classLoader);//Class.forName(pkgName);
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
			    //System.out.println("*****************************");
			}
	    }
}
