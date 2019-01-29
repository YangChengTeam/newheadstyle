package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    private int page;

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
}
