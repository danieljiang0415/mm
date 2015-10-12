package com.example.tutorial;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
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
        final Class<?> PByteArrary = XposedHelpers.findClass("com.tencent.mm.pointers.PByteArray", lpparam.classLoader);
        Class<?> PInt = XposedHelpers.findClass("com.tencent.mm.pointers.PInt", lpparam.classLoader);
    	
        findAndHookMethod("com.tencent.mm.protocal.MMProtocalJni", lpparam.classLoader, "pack", byte[].class, PByteArrary, byte[].class, byte[].class, String.class, int.class, int.class, int.class, byte[].class, byte[].class, byte[].class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            	byte[] packBuf = (byte[]) param.args[0];
            	String s = Utility.byte2HexStr(packBuf);
            	Utility.writeLog2SDFile("logger.log", "MMProtocalJni->pack", s,  true);
            	Utility.writeLog2SDFile("logger.log", "CallStack", Utility.StackTrace(), false);
            	String sendID = "12314";
            	String repSendID = "1235";
            	Utility.replaceSubString(packBuf, sendID.getBytes(), repSendID.getBytes());
            	param.args[0] = packBuf;
            	s = Utility.byte2HexStr(packBuf);
                Utility.writeLog2SDFile("logger.log", "MMProtocalJni->packM", s,  true);
                
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });//[B[B[BLcom/tencent/mm/protocal/h$c;Ljava/io/ByteArrayOutputStream;Z
        
        //final Class<?> clas = XposedHelpers.findClass("com.tencent.mm.q.p", lpparam.classLoader);
        //Utility.getMethodInfo(clas);
        /*
        findAndHookMethod("com.tencent.mm.q.p", lpparam.classLoader, "a", int.class, byte[].class, byte[].class, byte[].class, int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            	byte[] packBuf = (byte[]) param.args[1];
            	String s = Utility.byte2HexStr(packBuf);
            	Utility.writeLog2SDFile("logger.log", s,  String.format("[com.tencent.mm.q.p->a1(%d)]", packBuf.length));
            	packBuf = (byte[]) param.args[2];
            	s = Utility.byte2HexStr(packBuf);
            	Utility.writeLog2SDFile("logger.log", s,  String.format("[com.tencent.mm.q.p->a2(%d)]", packBuf.length));
            	packBuf = (byte[]) param.args[3];
            	s = Utility.byte2HexStr(packBuf);
            	Utility.writeLog2SDFile("logger.log", s,  String.format("[com.tencent.mm.q.p->a3(%d)]", packBuf.length));
            	Utility.writeLog2SDFile("logger.log",  String.format("[com.tencent.mm.q.p->a(%d, %d)]", param.args[0],param.args[4]), "[com.tencent.mm.q.p->a]");
            	//Utility.writeLog2SDFile("logger.log", Utility.StackTrace(), "CallStack-->");
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            	Utility.writeLog2SDFile("logger.log", (String)param.getResult(), "getPassword-->");
            }
        });
        */
        findAndHookMethod("com.tencent.mm.protocal.MMProtocalJni", lpparam.classLoader, "unpack", PByteArrary, byte[].class, byte[].class, PByteArrary, PInt, PInt, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
    	    	Field field = PByteArrary.getDeclaredField("value");
    	        field.setAccessible(true);
    	        byte[] unpackBuf = (byte[]) field.get(param.args[0]);
            	//Utility.writeLog2SDFile("logger.log", "unpack size---->"+String.valueOf(buf.length) );		
            	//String s = Utility.byte2HexStr(unpackBuf);
            	//Utility.writeLog2SDFile("logger.log", "MMProtocalJni->unpack", s,  true);
            	//Utility.writeLog2SDFile("logger.log", "CallStack",Utility.StackTrace(), false);
            	//XposedBridge.log("<--------------" + s );
            	//s = "<--packet start";
            	//Utility.writeMem2SDFile("logger.log", s.getBytes() );
            	//Utility.writeMem2SDFile("logger.log", (byte[])param.args[1] );
            }
        });
        //getMethodInfo(Class.forName("java.lang.StringBuilder"));
        //Class Xlog = XposedHelpers.findClass("com.tencent.mm.xlog.Xlog", lpparam.classLoader);
        findAndHookMethod("com.tencent.mm.xlog.Xlog", lpparam.classLoader, "logWrite2", int.class, String.class, String.class, String.class, int.class, int.class, long.class, long.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
        	   // XposedBridge.log( (String) param.args[2] + param.args[3] + param.args[8]);
            	//Utility.writeLog2SDFile("logger.log", "logWrite2", (String)param.args[8], false );
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });
    }
    
}