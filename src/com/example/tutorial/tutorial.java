package com.example.tutorial;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class tutorial implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        //XposedBridge.log("Loaded app: " + lpparam.packageName);
        if (!lpparam.packageName.equals("com.tencent.mm"))
            return;
        XposedBridge.log(lpparam.packageName);
        Class PByteArrary = XposedHelpers.findClass("com.tencent.mm.pointers.PByteArray", lpparam.classLoader);
        //Field [] fields=PByteArrary.getDeclaredFields();
    	//for(Field field:fields){
	    //	field.setAccessible(true);//设置访问权限
	    //	System.out.println(field.getName());
	    //	XposedBridge.log(field.getName());
    	//}
    	//XposedBridge.log("----------------------------------------------------");
    	//getMethodInfo(XposedHelpers.findClass("com.tencent.mm.protocal.MMProtocalJni", lpparam.classLoader));
    	//XposedBridge.log("***************************************************====>");
    	
        findAndHookMethod("com.tencent.mm.protocal.MMProtocalJni", lpparam.classLoader, "pack", byte[].class, PByteArrary, byte[].class, byte[].class, String.class, int.class, int.class, int.class, byte[].class, byte[].class, byte[].class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            	Field [] fields=param.args[1].getClass().getDeclaredFields();
            	for(Field field:fields){
        	    	field.setAccessible(true);//设置访问权限
        	    	//if(field.getName().equals("value")){
        	    	//	XposedBridge.log(byte2HexStr( (byte[]) field.get(param.args[1])));
        	    	XposedBridge.log(field.getName());
        	    	byte[] packet = (byte[])field.get(param.args[1]);
        	    	XposedBridge.log(Arrays.toString(packet));
        	    	//XposedBridge.log(field.get(param.args[1]));
        	    	//}
        	    	//XposedBridge.log(field.get(obj).toString());
        	    	//XposedBridge.log(field.getName());
            	}
            	
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });
        
        //getMethodInfo(Class.forName("java.lang.StringBuilder"));
        //Class Xlog = XposedHelpers.findClass("com.tencent.mm.xlog.Xlog", lpparam.classLoader);
        findAndHookMethod("com.tencent.mm.xlog.Xlog", lpparam.classLoader, "logWrite2", int.class, String.class, String.class, String.class, int.class, int.class, long.class, long.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
        	    XposedBridge.log("logWrite2"+ String.valueOf(param.args[0]) + param.args[1] + param.args[2] + param.args[3] + param.args[8]);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });
    }
    
    public static String byte2HexStr(byte[] b) {    
        String stmp="";    
        StringBuilder sb = new StringBuilder("");    
        for (int n=0;n<b.length;n++)    
        {    
            stmp = Integer.toHexString(b[n] & 0xFF);    
            sb.append((stmp.length()==1)? "0"+stmp : stmp);    
            sb.append(" ");    
        }    
        return sb.toString().toUpperCase().trim();    
    }    
    
    public static void getObject(Object obj,String fieldName) throws IllegalArgumentException, IllegalAccessException{
    	Field [] fields=obj.getClass().getDeclaredFields();
    	for(Field field:fields){
	    	field.setAccessible(true);//设置访问权限
	    	XposedBridge.log(field.get(obj).toString());
	    	XposedBridge.log(field.getName());
    	}
    }
    
    @SuppressWarnings("rawtypes")
    private static void getMethodInfo(Class clazz) {
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