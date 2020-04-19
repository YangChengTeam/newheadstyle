package com.feiyou.headstyle.bean;

public class HongBaoInfo {
    private int hbvideo;
    private int type;//弹红包类型 0不弹出 1新人红包 2登录红包
    private String cashindex;//"0.05/0.15"

    public int getHbvideo() {
        return hbvideo;
    }

    public void setHbvideo(int hbvideo) {
        this.hbvideo = hbvideo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCashindex() {
        return cashindex;
    }

    public void setCashindex(String cashindex) {
        this.cashindex = cashindex;
    }
}
