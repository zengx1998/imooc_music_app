package com.imooc.lib_audio.mediaplayer.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.imooc.lib_audio.app.AudioHelper;
import com.imooc.lib_audio.mediaplayer.events.AudioCompleteEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioErrorEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioReleaseEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioStartEvent;
import com.imooc.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


/**
 * @Description: 播放音频；对外发送各种事件：播放、暂停、加载失败、播放完毕、销毁
 * @Data: 2021/1/18
 * @Author: zengx
 * @UpdateRemark: 更新说明
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioFocusManager.AudioFocusListener {

    private static final String TAG = "AudioPlayer";
    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 100;

    /** 真正的负责音频播放 */
    private CustomMediaPlayer mMediaPlayer;
    /** 增强后台保活能力 **/
    private WifiManager.WifiLock mWifiLock;
    //音频焦点监听器
    private AudioFocusManager mAudioFocusManager;
    //判断音频焦点是否存在
    private boolean isPausedByFocusLossTransient;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TIME_MSG:
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 完成初始化
     */
    public AudioPlayer(){
        mMediaPlayer = new CustomMediaPlayer();
        // 使用唤醒锁
        mMediaPlayer.setWakeMode(AudioHelper.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnErrorListener(this);
        // 初始化wifi锁
        mWifiLock = ((WifiManager) AudioHelper.getContext()
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
        // 初始化音频焦点管理器
        mAudioFocusManager = new AudioFocusManager(AudioHelper.getContext(), this);
    }

    /**
     * 获取播放器的状态
     * @return 状态
     */
    public CustomMediaPlayer.Status getStatus(){
        if (mMediaPlayer !=null){
            return mMediaPlayer.getState();
        } else {
            return CustomMediaPlayer.Status.STOPPED;
        }
    }

    /**
     * 设置音量
     * @param left left volume scalar
     * @param right right volume scalar
     */
    public void setVolume(float left, float right) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(left, right);
        }
    }
    /**
     * 对外提供的加载音频的方法
     * @param audioBean 歌单
     */
    public void load(AudioBean audioBean){
        try {
            //正常加载逻辑
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(audioBean.mUrl);
            //异步加载 加载完成后调用onPrepared()
            mMediaPlayer.prepareAsync();
            //对外发送load事件
            EventBus.getDefault().post(new AudioLoadEvent(audioBean));
        } catch (IOException e){
            e.printStackTrace();
            //对外发送error事件
            EventBus.getDefault().post(new AudioErrorEvent());
        }
    }

    /**
     * prepare以后自动调用start方法,外部不能调用
     */
    private void start(){
        // 获取音频焦点,保证我们的播放器顺利播放
        if (!mAudioFocusManager.requestAudioFocus()){
            Log.e(TAG, "获取音频焦点失败");
        }
        mMediaPlayer.start();
        // 启用wifi锁
        mWifiLock.acquire();
        //更新进度
        mHandler.sendEmptyMessage(TIME_MSG);
        //发送start事件，UI类型处理事件
        EventBus.getDefault().post(new AudioStartEvent());
    }

    /**
     * 对外提供暂定方法
     */
    public void pause(){
        //先判断音频是否处于暂定状态
        if (mMediaPlayer.getState() == CustomMediaPlayer.Status.STARTED){
            mMediaPlayer.pause();
            //关闭WiFi锁
            if (mWifiLock.isHeld()){
                mWifiLock.release();
            }
            //取消音频焦点
            if (mAudioFocusManager != null) {
                mAudioFocusManager.abandonAudioFocus();
            }
            //发送暂定事件
            EventBus.getDefault().post(new AudioPauseEvent());
        }
    }

    /**
     * 对外提供恢复方法
     */
    public void resume(){
        if (mMediaPlayer.getState() == CustomMediaPlayer.Status.PAUSED){
            //直接调用start方法
            start();
        }
    }

    /**
     * 销毁唯一mediaplayer实例,只有在退出app时使用
     * 清空播放器占有资源
     */
    public void release() {
        if (mMediaPlayer == null){
            return;
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
        //关闭WiFi锁
        if (mWifiLock.isHeld()){
            mWifiLock.release();
        }
        //取消音频焦点
        if (mAudioFocusManager != null) {
            mAudioFocusManager.abandonAudioFocus();
        }
        mWifiLock = null;
        mAudioFocusManager = null;
        //发送release销毁事件
        EventBus.getDefault().post(new AudioReleaseEvent());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //缓存进度回调
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //播放完毕的回调
        EventBus.getDefault().post(new AudioCompleteEvent());
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //播放错误的回调
        // 备注： 可以在AudioErrorEvent()加一个code 区分业务场景
        EventBus.getDefault().post(new AudioErrorEvent());
        //true代表我们自己处理异常，不再由mediaplayer处理。
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //准备完毕
        start();
    }

    @Override
    public void audioFocusGrant() {
        //再次获取音频焦点
        setVolume(1.0f,1.0f);
        if (isPausedByFocusLossTransient){
            resume();
        }
        isPausedByFocusLossTransient = false;
    }

    @Override
    public void audioFocusLoss() {
        //永久失去焦点，暂停
        if (mMediaPlayer != null) {
            pause();
        }
    }

    @Override
    public void audioFocusLossTransient() {
        //短暂失去焦点，暂停
        if (mMediaPlayer != null) {
            pause();
        }
        isPausedByFocusLossTransient = true;
    }

    @Override
    public void audioFocusLossDuck() {
        //瞬间失去焦点,
        setVolume(0.5f, 0.5f);
    }
}
