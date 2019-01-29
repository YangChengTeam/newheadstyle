package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/11/22.
 */
public class SearchHotWord {

    private String appid;

    private String id;

    @SerializedName("jump_path")
    private String jumpPath;

    private String name;

    @SerializedName("origin_id")
    private String originId;

    private String type;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJumpPath() {
        return jumpPath;
    }

    public void setJumpPath(String jumpPath) {
        this.jumpPath = jumpPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
