package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayGameInfo implements Serializable {

    private int id;//任务id(固定16)

    private int goldnum;//单次完成获得的金币数量

    private int goldtotal;// 当前任务当天可获取的金币总数

    private int tasktime;//小游戏任务一圈的时间(单位：秒)

    @SerializedName("is_finish")
    private int isFinish;//否超过限制的金币总数 0：否 1：是

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

    public int getTasktime() {
        return tasktime;
    }

    public void setTasktime(int tasktime) {
        this.tasktime = tasktime;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }
}
