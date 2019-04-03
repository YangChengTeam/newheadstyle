package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myflying on 2019/1/20.
 */
public class NoteItem {

    @SerializedName("add_time")
    private Long addTime;

    @SerializedName("comment_content")
    private String commentContent;

    @SerializedName("comment_id")
    private String commentId;

    @SerializedName("comment_nickname")
    private String commentNickname;

    @SerializedName("comment_num")
    private int commentNum;

    @SerializedName("comment_userimg")
    private String commentUserimg;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("zan_num")
    private Integer zanNum;

    @SerializedName("list_num")
    private Integer listNum;

    @SerializedName("is_zan")
    private int isZan;

    @SerializedName("comment_time")
    private Long commentTime;

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentNickname() {
        return commentNickname;
    }

    public void setCommentNickname(String commentNickname) {
        this.commentNickname = commentNickname;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getCommentUserimg() {
        return commentUserimg;
    }

    public void setCommentUserimg(String commentUserimg) {
        this.commentUserimg = commentUserimg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getZanNum() {
        return zanNum;
    }

    public void setZanNum(Integer zanNum) {
        this.zanNum = zanNum;
    }

    public Integer getListNum() {
        return listNum;
    }

    public void setListNum(Integer listNum) {
        this.listNum = listNum;
    }

    public int getIsZan() {
        return isZan;
    }

    public void setIsZan(int isZan) {
        this.isZan = isZan;
    }

    public Long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }
}
