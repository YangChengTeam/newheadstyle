package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoldDetailWrapper {

    @SerializedName("gold_list")
    private List<GoldDetailInfo> goldDetailList;

    @SerializedName("cash_list")
    private List<GoldDetailInfo> cashList;

    @SerializedName("gold_num")
    private int goldNum;//用户账户金币余额

    @SerializedName("gold_total")
    private int goldTotal;//用户累计获取的金币数量

    @SerializedName("gold_today")
    private int goldToday;//当天获取的金币数量

    public List<GoldDetailInfo> getGoldDetailList() {
        return goldDetailList;
    }

    public void setGoldDetailList(List<GoldDetailInfo> goldDetailList) {
        this.goldDetailList = goldDetailList;
    }

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }

    public int getGoldTotal() {
        return goldTotal;
    }

    public void setGoldTotal(int goldTotal) {
        this.goldTotal = goldTotal;
    }

    public int getGoldToday() {
        return goldToday;
    }

    public void setGoldToday(int goldToday) {
        this.goldToday = goldToday;
    }

    public List<GoldDetailInfo> getCashList() {
        return cashList;
    }

    public void setCashList(List<GoldDetailInfo> cashList) {
        this.cashList = cashList;
    }
}
