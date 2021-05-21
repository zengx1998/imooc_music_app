package com.imooc.lib_audio.mediaplayer.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.imooc.lib_audio.R;
import com.imooc.lib_common_ui.base.BaseActivity;

/**
 * 播放音乐Activity
 * @author zengx
 */
public class MusicPlayerActivity extends BaseActivity {

    public static void start(Activity context) {
        Intent intent = new Intent(context, MusicPlayerActivity.class);
        ActivityCompat.startActivity(context, intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(context).toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_service_layout);
    }
}