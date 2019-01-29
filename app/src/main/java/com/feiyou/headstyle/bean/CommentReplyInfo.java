package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/12/25.
 */
public class CommentReplyInfo {
    private String content;
    @SerializedName("repeat_id")
    private String repeatId;
    private String nickname;
    @SerializedName("repeat_nickname")
    private String repeatNickname;
    @SerializedName("repeat_user_id")
    private String repeatUserId;
    @SerializedName("user_id")
    private String userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(String repeatId) {
        this.repeatId = repeatId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
