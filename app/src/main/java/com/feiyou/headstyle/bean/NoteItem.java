package com.feiyou.headstyle.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
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
    private String commentNum;

    @SerializedName("comment_userimg")
    private String commentUserimg;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("zan_num")
    private String zanNum;

    @SerializedName("list_num")
    private Integer listNum;

    @SerializedName("is_zan")
    private int isZan;

    private List<NoteComment> comment;

    public class NoteComment {

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
        private String isZan;

        @SerializedName("zan_num")
        private String zanNum;

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

        public String getIsZan() {
            return isZan;
        }

        public void setIsZan(String isZan) {
            this.isZan = isZan;
        }

        public String getZanNum() {
            return zanNum;
        }

        public void setZanNum(String zanNum) {
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

    public class NoteRepeat {
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

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
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

    public String getZanNum() {
        return zanNum;
    }

    public void setZanNum(String zanNum) {
        this.zanNum = zanNum;
    }

    public List<NoteComment> getComment() {
        return comment;
    }

    public void setComment(List<NoteComment> comment) {
        this.comment = comment;
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
}
