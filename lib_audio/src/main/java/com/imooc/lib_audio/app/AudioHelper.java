package com.imooc.lib_audio.app;

import android.content.Context;

/**
 * @Description:  唯一与外界通信的帮助类
 * @Data: 2021/1/18
 * @Author: zengx
 * @UpdateRemark: 更新说明
 */
public final class AudioHelper {

    /** SDK全局Context, 供子模块用 */
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
        //初始化本地数据库
//        GreenDaoHelper.initDatabase();
    }

    public static Context getContext() {
        return mContext;
    }
}
