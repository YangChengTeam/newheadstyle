package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SeeVideoInfo implements Serializable {
    private int id;// 任务id(固定17)
    private int goldnum;//  单次完成获得的金币数量
    private int goldtotal;//  当前任务当天可获取的金币总数
    @SerializedName("is_finish")
    private int isFinish;// 是否超过限制的金币总数 0：否 1：是

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoldnum() {
        return goldnum;
    }

    public void setGoldnum(int goldnum) {
        this.goldnum = goldnum;
    }

    public int getGoldtotal() {
        return goldtotal;
    }

    public void setGoldtotal(int goldtotal) {
        this.goldtotal = goldtotal;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }
}
