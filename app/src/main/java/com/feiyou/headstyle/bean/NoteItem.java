package com.feiyou.headstyle.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myflying on 2019/1/20.
 */
public class NoteItem {

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

    private List<NoteComment> comment;

    public class NoteComment implements MultiItemEntity {

        public static final int TYPE_COMMENT = 1;
        public static final int TYPE_REPEAT = 2;

        private int itemType;

        @SerializedName("add_time")
        private String addTime;

        @SerializedName("comment_id")
        private String commentId;

        private String content;

        private String nickname;

        private String repeatNickName;

        @SerializedName("user_id")
        private String userId;

        private List<NoteRepeat> repeat;

        public NoteComment(int itemType) {
            this.itemType = itemType;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public List<NoteRepeat> getRepeat() {
            return repeat;
        }

        public void setRepeat(List<NoteRepeat> repeat) {
            this.repeat = repeat;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getRepeatNickName() {
            return repeatNickName;
        }

        public void setRepeatNickName(String repeatNickName) {
            this.repeatNickName = repeatNickName;
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
}
