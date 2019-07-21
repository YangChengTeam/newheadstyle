package com.feiyou.headstyle.bean;

import java.io.Serializable;

public class ExchangeInfo implements Serializable {
    private String uid;//用户id
    private Long goodstime;//兑换时间
    private String goodsorder;//商品订单号
    private int status;//订单状态默认为0(虚拟商品，0：未绑定账号)
    private String goodsuserinfo;//用户收货信息ID
    private String goodsid;//商品id
    private String goodsname;//商品名称
    private String goldprice;//金币价格
    private String cashprice;//现金价格
    private String smallimg;//商品预览图(多张图片使用英文逗号隔开)
    private String content;//商品描述
    private String exchange;//兑换说明
    private String account;//充值账号

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getGoodstime() {
        return goodstime;
    }

    public void setGoodstime(Long goodstime) {
        this.goodstime = goodstime;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGoodsuserinfo() {
        return goodsuserinfo;
    }

    public void setGoodsuserinfo(String goodsuserinfo) {
        this.goodsuserinfo = goodsuserinfo;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoldprice() {
        return goldprice;
    }

    public void setGoldprice(String goldprice) {
        this.goldprice = goldprice;
    }

    public String getCashprice() {
        return cashprice;
    }

    public void setCashprice(String cashprice) {
        this.cashprice = cashprice;
    }

    public String getSmallimg() {
        return smallimg;
    }

    public void setSmallimg(String smallimg) {
        this.smallimg = smallimg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
