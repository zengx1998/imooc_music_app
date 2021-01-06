package com.imooc.lib_audio.mediaplayer.utils;

/**
 * @Description: 毫秒转分秒工具类
 * @Data: 2021/1/6
 * @Author: Captain
 * @UpdateRemark: 更新说明
 */
public class Utils {

    /**
     * 毫秒转分秒
     */
    public static String formatTime(long time) {
        String min = (time / (1000 * 60)) + "";
        String second = (time % (1000 * 60) / 1000) + "";
        if (min.length() < 2) {
            min = 0 + min;
        }
        if (second.length() < 2) {
            second = 0 + second;
        }
        return min + ":" + second;
    }
}
