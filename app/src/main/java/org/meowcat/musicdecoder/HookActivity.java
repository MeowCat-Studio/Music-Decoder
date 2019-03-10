package org.meowcat.musicdecoder;

import android.app.Application;
import android.app.Instrumentation;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookActivity implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.appInfo == null) {
            return;
        }
        String packageName = lpparam.packageName;
        ClassLoader classLoader = lpparam.classLoader;
        if(packageName.equals("org.meowcat.musicdecoder")){
            findAndHookMethod("org.meowcat.musicdecoder.MainActivity", classLoader,
                    "getXposedStatus", boolean.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                        }
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[0]=true;
                            super.beforeHookedMethod(param);
                        }
                    });
        }
        if(packageName.equals("com.tencent.qqmusic")) {
            findAndHookMethod(Instrumentation.class.getName() , classLoader, "callApplicationOnCreate", Application.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    try{
                        Application al=(Application)param.args[0];
                        ClassLoader classloader=al.getClassLoader();
                        findAndHookMethod("com.tencent.qqmusic.business.musicdownload.vipdownload.PayProcessor", classloader, "encrypt", int.class, byte[].class, int.class, new XC_MethodReplacement() {
                            @Override
                            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                return 0;
                            }
                        });
                        }catch(Throwable ignored){
                    }
                }
            });
        }
    }
}
