package com.imooc.lib_audio.mediaplayer.core;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * @Description: 带状态的MediaPlayer
 * @Data: 2021/1/18
 * @Author: zengx
 * @UpdateRemark: MediaPlayer的介绍
 * https://developer.android.com/reference/android/media/MediaPlayer#valid-and-invalid-states
 */
public class CustomMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

    public enum Status {
        //状态
        IDLE, INITIALIZED, STARTED, PAUSED, STOPPED, COMPLETED
    }

    private Status mState;

    private OnCompletionListener mOnCompletionListener;

    public CustomMediaPlayer(){
        super();
        mState = Status.IDLE;
        super.setOnCompletionListener(this);
    }

    @Override
    public void reset() {
        super.reset();
        mState = Status.IDLE;
    }

    @Override
    public void setDataSource(String path)
            throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        mState = Status.INITIALIZED;
    }


    @Override
    public void start() throws IllegalStateException {
        super.start();
        mState = Status.STARTED;
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        mState = Status.STOPPED;
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        mState = Status.PAUSED;
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener= listener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mState = Status.COMPLETED;
        if (mOnCompletionListener != null){
            mOnCompletionListener.onCompletion(mp);
        }
    }

    public Status getState() {
        return mState;
    }

    public void setState(Status mState) {
        this.mState = mState;
    }

    public boolean isComplete() {
        return mState == Status.COMPLETED;
    }
}
