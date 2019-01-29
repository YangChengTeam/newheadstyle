package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myflying on 2019/1/12.
 */
public class NoteTypeWrapper {
    @SerializedName("guan_num")
    private int guanNum;

    @SerializedName("is_guan")
    private int isGuan;

    private List<NoteInfo> list;

    @SerializedName("message_num")
    private String messageNum;

    private String page;

    @SerializedName("topic_arr")
    private TopicInfo topicArr;

    @SerializedName("notice_list")
    private List<NoticeInfo> noticeList;

    public class NoticeInfo {
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public int getGuanNum() {
        return guanNum;
    }

    public void setGuanNum(int guanNum) {
        this.guanNum = guanNum;
    }

    public int getIsGuan() {
        return isGuan;
    }

    public void setIsGuan(int isGuan) {
        this.isGuan = isGuan;
    }

    public List<NoteInfo> getList() {
        return list;
    }

    public void setList(List<NoteInfo> list) {
        this.list = list;
    }

    public String getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(String messageNum) {
        this.messageNum = messageNum;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public TopicInfo getTopicArr() {
        return topicArr;
    }

    public void setTopicArr(TopicInfo topicArr) {
        this.topicArr = topicArr;
    }

    public List<NoticeInfo> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeInfo> noticeList) {
        this.noticeList = noticeList;
    }
}
