package com.feiyou.headstyle.bean;

import java.io.Serializable;

public class TaskInfo implements Serializable {
    private int id; //任务id
    private int issingle; //是否单次 0：否 1：是
    private String title; //任务名称
    private String button; //任务按钮名称
    private int goldnum; //获取的金币数量
    private String content; //任务描述
    private String ico; //任务图标
    private String weburl; //网页类型任务跳转链接
    private String downaddress; //下载/网页类型下载链接
    private String appid; //小程序appid
    private String oldid; //小程序原始id
    private String publicname;//公众号名称
    private String pubulicimg;//关注公众号引导图
    private int is_finish; //是否完成 0：否 1：是
    private String cashindex;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIssingle() {
        return issingle;
    }

    public void setIssingle(int issingle) {
        this.issingle = issingle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public int getGoldnum() {
        return goldnum;
    }

    public void setGoldnum(int goldnum) {
        this.goldnum = goldnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getDownaddress() {
        return downaddress;
    }

    public void setDownaddress(String downaddress) {
        this.downaddress = downaddress;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOldid() {
        return oldid;
    }

    public void setOldid(String oldid) {
        this.oldid = oldid;
    }

    public String getPublicname() {
        return publicname;
    }

    public void setPublicname(String publicname) {
        this.publicname = publicname;
    }

    public String getPubulicimg() {
        return pubulicimg;
    }

    public void setPubulicimg(String pubulicimg) {
        this.pubulicimg = pubulicimg;
    }

    public int getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(int is_finish) {
        this.is_finish = is_finish;
    }

    public String getCashindex() {
        return cashindex;
    }

    public void setCashindex(String cashindex) {
        this.cashindex = cashindex;
    }
}
