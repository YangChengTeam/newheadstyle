package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/2/26.
 */
public class VideoComment {
    @SerializedName("add_time")
    private Long addTime;
    @SerializedName("list_num")
    private int listNum;
    @SerializedName("comment_content")
    private String commentContent;
    @SerializedName("comment_id")
    private String commentId;
    @SerializedName("comment_nickname")
    private String commentNickname;
    @SerializedName("comment_num")
    private int commentNum;
    @SerializedName("comment_userimg")
    private String commenUserimg;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("zan_num")
    private int zanNum;
    @SerializedName("is_zan")
    private int isZan;

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public int getListNum() {
        return listNum;
    }

    public void setListNum(int listNum) {
        this.listNum = listNum;
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

    public String getCommenUserimg() {
        return commenUserimg;
    }

    public void setCommenUserimg(String commenUserimg) {
        this.commenUserimg = commenUserimg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    public int getIsZan() {
        return isZan;
    }

    public void setIsZan(int isZan) {
        this.isZan = isZan;
    }
}
