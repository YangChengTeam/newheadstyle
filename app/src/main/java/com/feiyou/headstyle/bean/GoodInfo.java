package com.feiyou.headstyle.bean;

import java.math.BigDecimal;

public class GoodInfo {

    private int id;// 商品id
    private String goodsname;//商品名称
    private String tagsname;//商品标签
    private double goldprice;//金币价格
    private double cashprice;//现金价格
    private int falsenum;//已兑换人数(后台填写的)
    private int allstock;//商品库存数量
    private int othnum;//剩余兑换次数
    private String smallimg;//商品预览图(多张图片使用英文逗号隔开)
    private String detailimg;//商品详情图片(多张图片使用英文逗号隔开)
    private String content;//商品描述
    private String exchange;
    private int total;//用户可兑换的次数
    private int truenum;//实际兑换人数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getTagsname() {
        return tagsname;
    }

    public void setTagsname(String tagsname) {
        this.tagsname = tagsname;
    }

    public double getGoldprice() {
        return goldprice;
    }

    public void setGoldprice(double goldprice) {
        this.goldprice = goldprice;
    }

    public double getCashprice() {
        return cashprice;
    }

    public void setCashprice(double cashprice) {
        this.cashprice = cashprice;
    }

    public int getFalsenum() {
        return falsenum;
    }

    public void setFalsenum(int falsenum) {
        this.falsenum = falsenum;
    }

    public int getOthnum() {
        return othnum;
    }

    public void setOthnum(int othnum) {
        this.othnum = othnum;
    }

    public String getSmallimg() {
        return smallimg;
    }

    public void setSmallimg(String smallimg) {
        this.smallimg = smallimg;
    }

    public String getDetailimg() {
        return detailimg;
    }

    public void setDetailimg(String detailimg) {
        this.detailimg = detailimg;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTruenum() {
        return truenum;
    }

    public void setTruenum(int truenum) {
        this.truenum = truenum;
    }

    public int getAllstock() {
        return allstock;
    }

    public void setAllstock(int allstock) {
        this.allstock = allstock;
    }
}
