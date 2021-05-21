package com.imooc.imooc_voice.application;

import android.app.Application;

import com.imooc.lib_audio.app.AudioHelper;

/**
 * 初始化Application
 * @author zengx
 */
public class MyApplication extends Application {

    private static MyApplication myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //音频SDK初始化
        AudioHelper.init(this);
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
