package com.feiyou.headstyle.bean;

public class CashRecord {

    private int stype;//提现类型(1:极速，2：普通)
    private int outway;//提现方式(1:微信，2：支付宝)
    private double amount;
    private Long addtime;//提现发起时间
    private Long uptime;//提现到账时间
    private int outstatus;//提现状态(1:待审核，2：已到账  ,99:提现失败)

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getOutway() {
        return outway;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOutway(int outway) {
        this.outway = outway;
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

    public int getOutstatus() {
        return outstatus;
    }

    public void setOutstatus(int outstatus) {
        this.outstatus = outstatus;
    }
}
