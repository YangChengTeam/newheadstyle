package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/12/26.
 */
public class AdInfo {
    private String id;
    private String ico;
    @SerializedName("jump_path")
    private String jumpPath;
    private String type;
    @SerializedName("origin_id")
    private String originOd;
    private String appid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getJumpPath() {
        return jumpPath;
    }

    public void setJumpPath(String jumpPath) {
        this.jumpPath = jumpPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginOd() {
        return originOd;
    }

    public void setOriginOd(String originOd) {
        this.originOd = originOd;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
