package com.feiyou.headstyle.bean;

public class CashInfo {
    private String nickname;//用户昵称
    private double cash;//提现金额

    private String uid;// 用户id
    private double amount;//提现金额
    private String orderno;//提现订单号
    private String openid;//用户登录的openid
    private String imei;//用户手机串号
    private int stype;//提现类型(1:极速，2：普通)
    private String uip;//用户IP
    private int outway;//提现方式(1:微信，2：支付宝)
    private int outstatus;//提现状态
    private Long addtime;//提现时间
    private Long uptime;//提现更新时间

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public String getUip() {
        return uip;
    }

    public void setUip(String uip) {
        this.uip = uip;
    }

    public int getOutway() {
        return outway;
    }

    public void setOutway(int outway) {
        this.outway = outway;
    }

    public int getOutstatus() {
        return outstatus;
    }

    public void setOutstatus(int outstatus) {
        this.outstatus = outstatus;
    }

    public Long getAddtime() {
        return addtime;
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }
}
