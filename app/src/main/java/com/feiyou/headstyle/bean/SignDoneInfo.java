package com.feiyou.headstyle.bean;

public class SignDoneInfo {

    private String uid;//用户user_id
    private int signdays;//连续签到天数
    private Long signtime;//签到时间
    private int goldnum;//签到获得的金币数量
    private double cash;//签到获得的红包金额

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSigndays() {
        return signdays;
    }

    public void setSigndays(int signdays) {
        this.signdays = signdays;
    }

    public Long getSigntime() {
        return signtime;
    }

    public void setSigntime(Long signtime) {
        this.signtime = signtime;
    }

    public int getGoldnum() {
        return goldnum;
    }

    public void setGoldnum(int goldnum) {
        this.goldnum = goldnum;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }
}
