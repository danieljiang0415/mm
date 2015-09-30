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
        Class<?> PByteArrary = XposedHelpers.findClass("com.tencent.mm.pointers.PByteArray", lpparam.classLoader);
        Class<?> PInt = XposedHelpers.findClass("com.tencent.mm.pointers.PInt", lpparam.classLoader);
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
            	String s = Utility.byte2HexStr((byte[]) param.args[0]);
            	XposedBridge.log("-------------->" + s );
            	Utility.writeMem2SDFile("logger.log", (byte[])param.args[0] );
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });
        
        findAndHookMethod("com.tencent.mm.protocal.MMProtocalJni", lpparam.classLoader, "unpack", PByteArrary, byte[].class, byte[].class, PByteArrary, PInt, PInt, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            	String s = Utility.byte2HexStr((byte[]) param.args[1]);
            	XposedBridge.log("<--------------" + s );
            	Utility.writeMem2SDFile("logger.log", (byte[])param.args[1] );
            }
        });
        //getMethodInfo(Class.forName("java.lang.StringBuilder"));
        //Class Xlog = XposedHelpers.findClass("com.tencent.mm.xlog.Xlog", lpparam.classLoader);
        findAndHookMethod("com.tencent.mm.xlog.Xlog", lpparam.classLoader, "logWrite2", int.class, String.class, String.class, String.class, int.class, int.class, long.class, long.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
        	   // XposedBridge.log( (String) param.args[2] + param.args[3] + param.args[8]);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });
    }
    
}