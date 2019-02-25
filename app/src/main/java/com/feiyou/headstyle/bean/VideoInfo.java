package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/12/26.
 */
public class VideoInfo {

    private String id;
    @SerializedName("intro")
    private String name;
    @SerializedName("ico")
    private String videoCover;
    @SerializedName("path")
    private String videoPath;
    @SerializedName("userimg")
    private String userHeadImg;
    @SerializedName("nickname")
    private String userHeadName;

    @SerializedName("click_num")
    private int clickNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getUserHeadName() {
        return userHeadName;
    }

    public void setUserHeadName(String userHeadName) {
        this.userHeadName = userHeadName;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }
}
