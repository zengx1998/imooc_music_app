package com.imooc.lib_audio.mediaplayer.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooc.lib_audio.R;
import com.imooc.lib_audio.mediaplayer.core.AudioController;
import com.imooc.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioStartEvent;
import com.imooc.lib_audio.mediaplayer.model.AudioBean;
import com.imooc.lib_image_loader.app.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Description: 播放器底部View
 * @Data: 2021/1/6
 * @Author: zengx
 * @UpdateRemark: 更新说明
 */
public class BottomMusicView extends RelativeLayout {

    private Context mContext;

    /**
     * 专辑图片
     */
    private ImageView mLeftView;
    /**
     * 歌曲名字
     */
    private TextView mTitleView;
    /**
     * 歌曲作者名字
     */
    private TextView mAlbumView;
    /**
     * 暂定播放按钮
     */
    private ImageView mPlayView;
    /**
     * 暂定播放按钮
     */
    private ImageView mRightView;
    /**
     * 显示歌曲列表按钮
     */
    private AudioBean mAudioBean;

    public BottomMusicView(Context context) {
        this(context,null);
    }

    public BottomMusicView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BottomMusicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        EventBus.getDefault().register(this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.bottom_view, this);
        rootView.setOnClickListener(v -> {
            //跳到音乐播放Activity
            MusicPlayerActivity.start((Activity) mContext);
        });
        mLeftView = rootView.findViewById(R.id.album_view);
        //属性动画 让图片不停旋转
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLeftView, View.ROTATION.getName(), 0f, 360);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.start();

        mTitleView = rootView.findViewById(R.id.audio_name_view);
        mAlbumView = rootView.findViewById(R.id.audio_album_view);
        mPlayView = rootView.findViewById(R.id.play_view);
        mPlayView.setOnClickListener(v -> {
            //处理播放暂停事件
            AudioController.getInstance().playOrPause();
        });
        mRightView = rootView.findViewById(R.id.show_list_view);
        mRightView.setOnClickListener(v -> {
            //显示音乐列表对话框
//                MusicListDialog dialog = new MusicListDialog(mContext);
//                dialog.show();
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioLoadEvent(AudioLoadEvent event) {
        //监听加载事件
        mAudioBean = event.mAudioBean;
        showLoadingView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStartEvent(AudioStartEvent event) {
        showPlayView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPauseEvent(AudioPauseEvent event) {
        showPauseView();
    }

    private void showLoadingView() {
        if (mAudioBean != null) {
            ImageLoaderManager.getInstance().displayImageForCircle(mLeftView, mAudioBean.albumPic);
            mTitleView.setText(mAudioBean.name);
            mAlbumView.setText(mAudioBean.album);
            mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
        }
    }

    private void showPlayView() {
        if (mAudioBean != null) {
            mPlayView.setImageResource(R.mipmap.note_btn_play_white);
        }
    }

    private void showPauseView() {
        if (mAudioBean != null) {
            mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }
}
