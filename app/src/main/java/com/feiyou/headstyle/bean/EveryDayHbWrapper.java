package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EveryDayHbWrapper {

    @SerializedName("hbvideo")
    private int hbvideo;//1,  红包看视频开关控制(1：打开。2：关闭)

    @SerializedName("is_first")
    private int isFirst;//当天是否打开过红包,0为未打开，1为已打开过

    @SerializedName("is_finish")
    private int isFinish;//当天是否完成红包任务,0为未完成，1为已完成

    private String cashindex;//领取的红包区间

    @SerializedName("hb_list")
    public List<ReceiveUserInfo> receiveUserList;

    @SerializedName("game_info")
    private PlayGameInfo gameInfo;

    @SerializedName("sp_info")
    private SeeVideoInfo seeVideoInfo;

    public int getHbvideo() {
        return hbvideo;
    }

    public void setHbvideo(int hbvideo) {
        this.hbvideo = hbvideo;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public String getCashindex() {
        return cashindex;
    }

    public void setCashindex(String cashindex) {
        this.cashindex = cashindex;
    }

    public List<ReceiveUserInfo> getReceiveUserList() {
        return receiveUserList;
    }

    public void setReceiveUserList(List<ReceiveUserInfo> receiveUserList) {
        this.receiveUserList = receiveUserList;
    }

    public PlayGameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(PlayGameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public SeeVideoInfo getSeeVideoInfo() {
        return seeVideoInfo;
    }

    public void setSeeVideoInfo(SeeVideoInfo seeVideoInfo) {
        this.seeVideoInfo = seeVideoInfo;
    }
}
