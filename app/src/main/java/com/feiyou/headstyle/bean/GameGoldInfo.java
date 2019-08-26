package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

public class GameGoldInfo {

    private String uid;//用户user_id

    private int goldnum;//用户当天游戏累计获取的金币数量

    @SerializedName("spgold")
    private int todayVideoNum;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getGoldnum() {
        return goldnum;
    }

    public void setGoldnum(int goldnum) {
        this.goldnum = goldnum;
    }

    public int getTodayVideoNum() {
        return todayVideoNum;
    }

    public void setTodayVideoNum(int todayVideoNum) {
        this.todayVideoNum = todayVideoNum;
    }
}
