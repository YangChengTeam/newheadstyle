package com.feiyou.headstyle.bean;

public class MessageEvent {

    private String message;

    private String friendIds;

    private String friendNames;

    private NoteInfo addNoteInfo;

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
}
