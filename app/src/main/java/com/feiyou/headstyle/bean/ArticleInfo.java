package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/1/9.
 */
public class ArticleInfo {

    private String id;

    private String content;

    @SerializedName("zan_num")
    private String zanNum;

    @SerializedName("comment_num")
    private String commentNum;

    private String cimg;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("add_time")
    private Long addTime;

    @SerializedName("comment_time")
    private String commentTime;

    private String nickname;

    private String userimg;

    @SerializedName("name")
    private String topicName;

    @SerializedName("image_arr")
    private String[] imageArr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getZanNum() {
        return zanNum;
    }

    public void setZanNum(String zanNum) {
        this.zanNum = zanNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getCimg() {
        return cimg;
    }

    public void setCimg(String cimg) {
        this.cimg = cimg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(String[] imageArr) {
        this.imageArr = imageArr;
    }
}
