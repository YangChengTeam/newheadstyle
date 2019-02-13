package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/2/13.
 */
public class ReplyResultInfo {
    @SerializedName("comment_id")
    private String commentId;

    @SerializedName("repead_id")
    private String repeadId;

    @SerializedName("comment_num")
    private String commentNum;

    private String content;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getRepeadId() {
        return repeadId;
    }

    public void setRepeadId(String repeadId) {
        this.repeadId = repeadId;
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
}
