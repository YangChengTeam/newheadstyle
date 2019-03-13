package com.feiyou.headstyle.bean;

/**
 * Created by myflying on 2019/2/14.
 */
public class ReplyParams {

    private int modelType;//1：代表帖子回复，2代表视频列表回复

    private int type;
    private String content;
    private String repeatUserId;
    private String messageId;
    private String commentId;
    private String commentUserId;
    private String repeatId;
    private String repeatCommentUserId;

    private String atUserIds;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(String repeatId) {
        this.repeatId = repeatId;
    }

    public String getRepeatCommentUserId() {
        return repeatCommentUserId;
    }

    public void setRepeatCommentUserId(String repeatCommentUserId) {
        this.repeatCommentUserId = repeatCommentUserId;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public String getAtUserIds() {
        return atUserIds;
    }

    public void setAtUserIds(String atUserIds) {
        this.atUserIds = atUserIds;
    }
}
