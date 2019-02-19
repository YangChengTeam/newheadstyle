package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

public class NoteSubComment {

    @SerializedName("add_time")
    private Long addTime;

    @SerializedName("repeat_content")
    private String repeatContent;

    @SerializedName("repeat_id")
    private String repeatId;

    @SerializedName("repeat_nickname")
    private String repeatNickname;

    @SerializedName("repeat_user_id")
    private String repeatUserId;

    @SerializedName("repeat_userimg")
    private String repeatUserimg;

    @SerializedName("repeat_num")
    private String repeatNum;

    @SerializedName("is_zan")
    private Integer isZan;

    @SerializedName("zan_num")
    private Integer zanNum;

    @SerializedName("content")
    private String oldContent;

    @SerializedName("nickname")
    private String oldNickname;

    @SerializedName("user_id")
    private String oldUserId;

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getRepeatContent() {
        return repeatContent;
    }

    public void setRepeatContent(String repeatContent) {
        this.repeatContent = repeatContent;
    }

    public String getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(String repeatId) {
        this.repeatId = repeatId;
    }

    public String getRepeatNickname() {
        return repeatNickname;
    }

    public void setRepeatNickname(String repeatNickname) {
        this.repeatNickname = repeatNickname;
    }

    public String getRepeatUserId() {
        return repeatUserId;
    }

    public void setRepeatUserId(String repeatUserId) {
        this.repeatUserId = repeatUserId;
    }

    public String getRepeatUserimg() {
        return repeatUserimg;
    }

    public void setRepeatUserimg(String repeatUserimg) {
        this.repeatUserimg = repeatUserimg;
    }

    public String getRepeatNum() {
        return repeatNum;
    }

    public void setRepeatNum(String repeatNum) {
        this.repeatNum = repeatNum;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
    }

    public Integer getZanNum() {
        return zanNum;
    }

    public void setZanNum(Integer zanNum) {
        this.zanNum = zanNum;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getOldNickname() {
        return oldNickname;
    }

    public void setOldNickname(String oldNickname) {
        this.oldNickname = oldNickname;
    }

    public String getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(String oldUserId) {
        this.oldUserId = oldUserId;
    }
}