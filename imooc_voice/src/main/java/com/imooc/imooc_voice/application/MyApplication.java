package com.imooc.imooc_voice.application;

import android.app.Application;

/**
 * 初始化Application
 * @author zengx
 */
public class MyApplication extends Application {

    private static MyApplication myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
