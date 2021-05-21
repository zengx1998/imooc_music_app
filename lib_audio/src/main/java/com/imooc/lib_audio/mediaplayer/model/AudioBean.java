package com.imooc.lib_audio.mediaplayer.model;


import java.io.Serializable;

/**
 * @Description: 歌曲实体类
 * @Data: 2021/1/18
 * @Author: zengx
 * @UpdateRemark: 更新说明
 */

public class AudioBean implements Serializable {

    private static final long serialVersionUID = -8849228294348905620L;


    public AudioBean(String id, String mUrl,String name, String author,
                     String album, String albumInfo,  String albumPic,
                     String totalTime) {
        this.id = id;
        this.mUrl = mUrl;
        this.name = name;
        this.author = author;
        this.album = album;
        this.albumInfo = albumInfo;
        this.albumPic = albumPic;
        this.totalTime = totalTime;
    }
//
//    @Generated(hash = 1628963493) public AudioBean() {
//    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMUrl() {
        return this.mUrl;
    }

    public void setMUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPic() {
        return this.albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getAlbumInfo() {
        return this.albumInfo;
    }

    public void setAlbumInfo(String albumInfo) {
        this.albumInfo = albumInfo;
    }

    public String getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String id;
    //地址
   public String mUrl;

    //歌名
    public String name;

    //作者
     public String author;

    //所属专辑
   public String album;

 public String albumInfo;

    //专辑封面
  public String albumPic;

    //时长
   public String totalTime;

    @Override public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof AudioBean)) {
            return false;
        }
        return ((AudioBean) other).id.equals(this.id);
    }
}
