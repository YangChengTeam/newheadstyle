package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/12/25.
 */
public class CommentInfo {

    private String name;

    private List<CommentReplyInfo> replyList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommentReplyInfo> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<CommentReplyInfo> replyList) {
        this.replyList = replyList;
    }
}
