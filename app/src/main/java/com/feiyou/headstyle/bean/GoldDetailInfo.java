package com.feiyou.headstyle.bean;

public class GoldDetailInfo {

    private int goldnum;//记录的金币数量
    private double cash;//记录的现金金额
    private int status;//当前记录的状态(1:获取，2：消耗)
    private Long addtime;//记录时间
    private String taskname;//当前记录的任务名称
    private int signdays;//连续签到天数(如果当前记录不是签到，则默认为0)

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getAddtime() {
        return addtime;
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public int getSigndays() {
        return signdays;
    }

    public void setSigndays(int signdays) {
        this.signdays = signdays;
    }
}
