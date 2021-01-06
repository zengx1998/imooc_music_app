package com.imooc.imooc_voice.model;

/**
 * 首页枚举类型
 * @author zengx
 */
public enum CHANNEL {

  /**
   * 我的
   */
  MY("我的", 0x01),

  /**
   *发现
   */
  FIND("发现", 0x02),
  /**
   *朋友
   */
  FRIEND("朋友", 0x03),
  /**
   *视频
   */
  VIDEO("视频", 0x04);

  //所有类型标识
  public static final int MINE_ID = 0x01;
  public static final int FIND_ID = 0x02;
  public static final int FRIEND_ID = 0x03;
  public static final int VIDEO_ID = 0x04;

  private final String key;
  private final int value;

  CHANNEL(String key, int value) {
    this.key = key;
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public String getKey() {
    return key;
  }
}
