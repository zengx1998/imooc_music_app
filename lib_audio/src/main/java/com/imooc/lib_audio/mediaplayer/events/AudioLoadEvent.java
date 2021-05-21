package com.imooc.lib_audio.mediaplayer.events;

import com.imooc.lib_audio.mediaplayer.model.AudioBean;

/**
 * 加载监听
 * @author zengx
 */
public class AudioLoadEvent {
  public AudioBean mAudioBean;

  public AudioLoadEvent(AudioBean audioBean) {
    this.mAudioBean = audioBean;
  }
}
