package com.imooc.lib_audio.mediaplayer.core;

import com.imooc.lib_audio.mediaplayer.exception.AudioQueueEmptyException;
import com.imooc.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

/**
 * @Description: 控制播放逻辑类
 * @Data: 2021/1/19
 * @Author: zengx
 * @UpdateRemark: 更新说明
 */
public class AudioController {

    private static final String TAG = "AudioController";

    /**
     * 播放模式
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LOOP,
        /**
         * 随机
         */
        RANDOM,
        /**
         * 单曲循环
         */
        REPEAT
    }

    /**
     * 播放器
     **/
    private AudioPlayer mAudioPlayer;
    /**
     * 播放队列
     **/
    private ArrayList<AudioBean> mQueue;
    /**
     * 播放模式
     **/
    private PlayMode mPlayMode;
    /**
     * 当前播放索引
     **/
    private int mQueueIndex;

    public static AudioController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static AudioController INSTANCE = new AudioController();
    }


    private AudioController() {
        mAudioPlayer = new AudioPlayer();
        mQueue = new ArrayList<>();
        mQueueIndex = 0;
        mPlayMode = PlayMode.LOOP;
    }

    public ArrayList<AudioBean> getQueue() {
        return mQueue == null ? new ArrayList<AudioBean>() : mQueue;
    }

    /**
     * 设置播放队列
     *
     * @param bean 数据源
     */
    public void setQueue(ArrayList<AudioBean> bean) {
        this.setQueue(bean, 0);
    }

    public void setQueue(ArrayList<AudioBean> bean, int queueIndex) {
        mQueue.addAll(bean);
        mQueueIndex = queueIndex;
    }

    /**
     * 添加单一歌曲到指定位置
     * @param bean
     */
    public void addAudio(AudioBean bean){
        this.addAudio(0,bean);
    }

    /**
     * 添加单一歌曲到指定位置
     * @param index
     * @param bean
     */
    public void addAudio(int index,AudioBean bean){
        if (mQueue == null){
            throw new AudioQueueEmptyException("当前播放队列为空");
        }
        int query = queryAudio(bean);
        if (query <= -1) {
            //没添加过此id的歌曲，添加且直播番放
            addCustomAudio(index, bean);
            setPlayIndex(index);
        } else {
            AudioBean currentBean = getNowPlaying();
            if (!currentBean.id.equals(bean.id)) {
                //添加过且不是当前播放，播，否则什么也不干
                setPlayIndex(query);
            }
        }
    }

    private int queryAudio(AudioBean bean) {
        return mQueue.indexOf(bean);
    }

    private void addCustomAudio(int index, AudioBean bean) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mQueue.add(index, bean);
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    /**
     * 对外提供设置模式
     *
     * @param playMode 播放模式
     */
    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    public void setPlayIndex(int index) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mQueueIndex = index;
        play();
    }

    public int getPlayIndex() {
        return mQueueIndex;
    }

    public void play() {
        AudioBean bean = getNowPlaying();
        mAudioPlayer.load(bean);
    }

    public void resume() {
        mAudioPlayer.resume();
    }

    public void pause() {
        mAudioPlayer.pause();
    }

    public void release() {
        mAudioPlayer.release();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 加载next index歌曲
     */
    public void next() {
        AudioBean bean = getNextPlaying();
        mAudioPlayer.load(bean);
    }

    /**
     * 加载previous index歌曲
     */
    public void previous() {
        AudioBean bean = getPreviousPlaying();
        mAudioPlayer.load(bean);
    }

    /**
     * 对外提供的获取当前歌曲信息
     */
    private AudioBean getNowPlaying() {
        return getPlaying(mQueueIndex);
    }

    private AudioBean getNextPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex + 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT:
                return getPlaying(mQueueIndex);
            default:
                break;
        }
        return null;
    }

    private AudioBean getPreviousPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex + mQueue.size() - 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT:
                return getPlaying(mQueueIndex);
            default:
                break;
        }
        return null;
    }

    private AudioBean getPlaying(int index) {
        if (mQueue != null && !mQueue.isEmpty() && index >= 0 && index < mQueue.size()) {
            return mQueue.get(index);
        } else {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
    }

    /**
     * 自动切换播放/暂停
     */
    public void playOrPause() {
        if (isStartState()) {
            pause();
        } else if (isPauseState()) {
            resume();
        }
    }

    /**
     * 对外提供是否播放状态
     */
    public boolean isStartState() {
        return CustomMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 对外提供是否暂停状态
     */
    public boolean isPauseState() {
        return CustomMediaPlayer.Status.PAUSED == getStatus();
    }

    private CustomMediaPlayer.Status getStatus() {
        return mAudioPlayer.getStatus();
    }
}
