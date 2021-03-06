package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/2/16.
 */
public class VersionInfo {

    @SerializedName("version_code")
    private int versionCode;

    @SerializedName("version_name")
    private String versionName;

    @SerializedName("version_desc")
    private String versionDesc;

    @SerializedName("version_url")
    private String versionUrl;

    @SerializedName("version_is_change")
    private int versionIsChange;

    @SerializedName("version_date")
    private String versionDate;

    @SerializedName("app_openad")
    private int appOpenad;//APP开屏广告,1:打开。2：关闭

    @SerializedName("app_adtype")
    private int startType;//1腾讯，2头条

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public int getVersionIsChange() {
        return versionIsChange;
    }

    public void setVersionIsChange(int versionIsChange) {
        this.versionIsChange = versionIsChange;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(String versionDate) {
        this.versionDate = versionDate;
    }

    public int getAppOpenad() {
        return appOpenad;
    }

    public void setAppOpenad(int appOpenad) {
        this.appOpenad = appOpenad;
    }

    public int getStartType() {
        return startType;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }
}
