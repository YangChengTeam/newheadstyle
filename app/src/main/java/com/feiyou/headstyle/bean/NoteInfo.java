package com.feiyou.headstyle.bean;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/11/27.
 */
public class NoteInfo implements MultiItemEntity {

    public static final int NOTE_NORMAL = 1;

    public static final int NOTE_AD = 2;

    @SerializedName("comment_time")
    private Long commentTime;

    @SerializedName("add_time")
    private Long addTime;

    @SerializedName("comment_num")
    private int commentNum;

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
    private int zanNum;

    @SerializedName("is_zan")
    private int isZan;

    @SerializedName("is_guan")
    private int isGuan;

    private int itemType;

    private TTNativeExpressAd ttNativeExpressAd;

    public NoteInfo(){}
    public NoteInfo(int type) {
        this.itemType = type;
    }


    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
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
        if(userId == null){
            return "";
        }
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

    public int getIsGuan() {
        return isGuan;
    }

    public void setIsGuan(int isGuan) {
        this.isGuan = isGuan;
    }

    public Long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }

    public TTNativeExpressAd getTtNativeExpressAd() {
        return ttNativeExpressAd;
    }

    public void setTtNativeExpressAd(TTNativeExpressAd ttNativeExpressAd) {
        this.ttNativeExpressAd = ttNativeExpressAd;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
