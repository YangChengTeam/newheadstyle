package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestInfo {

    private String id;
    @SerializedName("name")
    private String testTitle;

    @SerializedName("ico")
    private String testThumb;

    @SerializedName("title")
    private String testSubTitle;

    @SerializedName("test_type")
    private int testType;

    private String url;

    private String testCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public String getTestThumb() {
        return testThumb;
    }

    public void setTestThumb(String testThumb) {
        this.testThumb = testThumb;
    }

    public String getTestCount() {
        return testCount;
    }

    public void setTestCount(String testCount) {
        this.testCount = testCount;
    }

    public String getTestSubTitle() {
        return testSubTitle;
    }

    public void setTestSubTitle(String testSubTitle) {
        this.testSubTitle = testSubTitle;
    }

    public int getTestType() {
        return testType;
    }

    public void setTestType(int testType) {
        this.testType = testType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
