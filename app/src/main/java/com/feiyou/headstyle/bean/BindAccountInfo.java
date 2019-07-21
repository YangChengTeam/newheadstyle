package com.feiyou.headstyle.bean;

public class BindAccountInfo {

    private String account;//绑定的账号
    private String goodsorder;//订单信息
    private int status;//相关订单信息状态

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGoodsorder() {
        return goodsorder;
    }

    public void setGoodsorder(String goodsorder) {
        this.goodsorder = goodsorder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
