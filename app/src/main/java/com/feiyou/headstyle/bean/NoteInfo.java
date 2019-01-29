package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/11/27.
 */
public class NoteInfo {

    @SerializedName("add_time")
    private Long addTime;

    @SerializedName("comment_num")
    private String commentNum;

    private String content;

    private String id;

    @SerializedName("image_arr")
    private String[] imageArr;

    private String name;

    private String nickname;

    @SerializedName("user_id")
    private String userId;

    private String userimg;

    @SerializedName("zan_num")
    private String zanNum;

    @SerializedName("is_zan")
    private String isZan;

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(String[] imageArr) {
        this.imageArr = imageArr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getZanNum() {
        return zanNum;
    }

    public void setZanNum(String zanNum) {
        this.zanNum = zanNum;
    }

    public String getIsZan() {
        return isZan;
    }

    public void setIsZan(String isZan) {
        this.isZan = isZan;
    }
}
