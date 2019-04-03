package com.feiyou.headstyle.bean;

public class MessageEvent {

    private String message;

    private String friendIds;

    private String friendNames;

    private NoteInfo addNoteInfo;

    private String topicId;

    private int pageIndex;

    public String getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(String friendIds) {
        this.friendIds = friendIds;
    }

    public String getFriendNames() {
        return friendNames;
    }

    public void setFriendNames(String friendNames) {
        this.friendNames = friendNames;
    }

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NoteInfo getAddNoteInfo() {
        return addNoteInfo;
    }

    public void setAddNoteInfo(NoteInfo addNoteInfo) {
        this.addNoteInfo = addNoteInfo;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
