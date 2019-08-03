package com.feiyou.headstyle.bean;

public class ReceiveUserInfo {
    private String nickname;//用户昵称
    private String userimg;//用户头像
    private double cash;//随机后的红包金额
    private Long addtime;//随机后的时间

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public Long getAddtime() {
        return addtime;
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }
}