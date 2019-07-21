package com.feiyou.headstyle.bean;

import java.util.List;

public class CashMoneyWrapper {

    private List<CashMoneyInfo> cashlist;

    private int txstatus;//所有用户极速功能提现状态(0:打开 1：关闭)
    private int wxstatus;//微信支付接口状态(0:打开 1：关闭)
    private int alistatus;//支付宝接口状态(0:打开 1：关闭)
    private String content;//提现说明
    private String txopenid;//用户提现绑定的微信openid
    private String txnickname;//用户提现绑定的微信昵称
    private int jstx;//用户是否使用过极速提现(0：否 1：是)
    private double cash;//用户账户收益余额

    public List<CashMoneyInfo> getCashlist() {
        return cashlist;
    }

    public void setCashlist(List<CashMoneyInfo> cashlist) {
        this.cashlist = cashlist;
    }

    public int getTxstatus() {
        return txstatus;
    }

    public void setTxstatus(int txstatus) {
        this.txstatus = txstatus;
    }

    public int getWxstatus() {
        return wxstatus;
    }

    public void setWxstatus(int wxstatus) {
        this.wxstatus = wxstatus;
    }

    public int getAlistatus() {
        return alistatus;
    }

    public void setAlistatus(int alistatus) {
        this.alistatus = alistatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTxopenid() {
        return txopenid;
    }

    public void setTxopenid(String txopenid) {
        this.txopenid = txopenid;
    }

    public String getTxnickname() {
        return txnickname;
    }

    public void setTxnickname(String txnickname) {
        this.txnickname = txnickname;
    }

    public int getJstx() {
        return jstx;
    }

    public void setJstx(int jstx) {
        this.jstx = jstx;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }
}
