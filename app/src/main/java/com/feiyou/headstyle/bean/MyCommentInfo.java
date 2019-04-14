package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/3/5.
 */
public class MyCommentInfo {

    @SerializedName("vedio_info")
    private VideoInfo videoInfo;

    @SerializedName("add_time")
    private Long addTime;

    @SerializedName("message_id")
    private String messageId;

    @SerializedName("user_id")
    private String userId;

    private String nickname;

    private String userimg;

    private String content;

    @SerializedName("repeat_user_id")
    private String repeatUserId;

    @SerializedName("repeat_nickname")
    private String repeatNickname;

    @SerializedName("repeat_userimg")
    private String repeatUserimg;

    @SerializedName("repeat_content")
    private String repeatContent;

    private int type;

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRepeatUserId() {
        return repeatUserId;
    }

    public void setRepeatUserId(String repeatUserId) {
        this.repeatUserId = repeatUserId;
    }

    public String getRepeatNickname() {
        return repeatNickname;
    }

    public void setRepeatNickname(String repeatNickname) {
        this.repeatNickname = repeatNickname;
    }

    public String getRepeatUserimg() {
        return repeatUserimg;
    }

    public void setRepeatUserimg(String repeatUserimg) {
        this.repeatUserimg = repeatUserimg;
    }

    public String getRepeatContent() {
        return repeatContent;
    }

    public void setRepeatContent(String repeatContent) {
        this.repeatContent = repeatContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }
}
