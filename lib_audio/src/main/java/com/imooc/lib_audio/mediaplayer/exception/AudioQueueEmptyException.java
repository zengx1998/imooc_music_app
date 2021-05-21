package com.imooc.lib_audio.mediaplayer.exception;

/**
 * @Description: 播放队列为空异常
 * @Data: 2021/1/27
 * @Author: zengx
 * @UpdateRemark: 更新说明
 */
public class AudioQueueEmptyException  extends RuntimeException{

    public AudioQueueEmptyException(String error){
        super(error);
    }
}
