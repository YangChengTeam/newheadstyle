package com.feiyou.headstyle.bean;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestDetailInfoWrapper {

    private String testId;

    private String desc;

    private String ico;

    private String image;

    @SerializedName("share_title")
    private String[] share_title;

    @SerializedName("share_ico")
    private String[] share_ico;

    @SerializedName("test_type")
    private String testType;

    private String title;

    private Integer sex;

    private TestDetailInfo list;

    public TestDetailInfo getList() {
        return list;
    }

    public void setList(TestDetailInfo list) {
        this.list = list;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getShare_title() {
        return share_title;
    }

    public void setShare_title(String[] share_title) {
        this.share_title = share_title;
    }

    public String[] getShare_ico() {
        return share_ico;
    }

    public void setShare_ico(String[] share_ico) {
        this.share_ico = share_ico;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }
}
