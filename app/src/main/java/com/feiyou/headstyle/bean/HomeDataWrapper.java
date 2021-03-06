package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/12/26.
 */
public class HomeDataWrapper {

    @SerializedName("banner_list")
    private List<BannerInfo> bannerList;

    @SerializedName("images_tags_list")
    private List<CategoryInfo> categoryInfoList;

    @SerializedName("images_list")
    private List<HeadInfo> imagesList;

    @SerializedName("ads_list")
    private List<AdInfo> adList;

    @SerializedName("message_list")
    private List<ArticleInfo> messageList;

    @SerializedName("suspend_ad")
    private AdInfo suspendAdInfo;

    @SerializedName("open_ad")
    private AdInfo openAdInfo;

    @SerializedName("message_ad")
    private AdInfo messageAdInfo;

    private int page;

    @SerializedName("my_notice_num")
    private int myTotalNum;

    public List<BannerInfo> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerInfo> bannerList) {
        this.bannerList = bannerList;
    }

    public List<CategoryInfo> getCategoryInfoList() {
        return categoryInfoList;
    }

    public void setCategoryInfoList(List<CategoryInfo> categoryInfoList) {
        this.categoryInfoList = categoryInfoList;
    }

    public List<HeadInfo> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<HeadInfo> imagesList) {
        this.imagesList = imagesList;
    }

    public List<AdInfo> getAdList() {
        return adList;
    }

    public void setAdList(List<AdInfo> adList) {
        this.adList = adList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ArticleInfo> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<ArticleInfo> messageList) {
        this.messageList = messageList;
    }

    public int getMyTotalNum() {
        return myTotalNum;
    }

    public void setMyTotalNum(int myTotalNum) {
        this.myTotalNum = myTotalNum;
    }

    public AdInfo getSuspendAdInfo() {
        return suspendAdInfo;
    }

    public void setSuspendAdInfo(AdInfo suspendAdInfo) {
        this.suspendAdInfo = suspendAdInfo;
    }

    public AdInfo getOpenAdInfo() {
        return openAdInfo;
    }

    public void setOpenAdInfo(AdInfo openAdInfo) {
        this.openAdInfo = openAdInfo;
    }

    public AdInfo getMessageAdInfo() {
        return messageAdInfo;
    }

    public void setMessageAdInfo(AdInfo messageAdInfo) {
        this.messageAdInfo = messageAdInfo;
    }
}
