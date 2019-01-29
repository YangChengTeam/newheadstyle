package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/1/11.
 */
public class MoreTypeInfo {

    private String appid;

    private String id;

    @SerializedName("jump_path")
    private String jumpPath;

    @SerializedName("origin_id")
    private String originId;

    private String tagsimg;

    private String tagsname;

    private String type;

    private String desc;

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

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getTagsimg() {
        return tagsimg;
    }

    public void setTagsimg(String tagsimg) {
        this.tagsimg = tagsimg;
    }

    public String getTagsname() {
        return tagsname;
    }

    public void setTagsname(String tagsname) {
        this.tagsname = tagsname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
