package com.feiyou.headstyle.bean;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2018/11/20.
 */
public class HeadInfo implements MultiItemEntity {

    public static final int HEAD_IMG = 1;

    public static final int HEAD_AD = 2;

    private String id;

    private String imgurl;

    private int itemType;

    @SerializedName("is_collect")
    private int isCollect;

    private TTNativeExpressAd ttNativeExpressAd;
    public HeadInfo(){}
    public HeadInfo(int type) {
        this.itemType = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public TTNativeExpressAd getTtNativeExpressAd() {
        return ttNativeExpressAd;
    }

    public void setTtNativeExpressAd(TTNativeExpressAd ttNativeExpressAd) {
        this.ttNativeExpressAd = ttNativeExpressAd;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
