package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WelfareInfo {

    @SerializedName("gold_user")
    public GoldUser goldUser;

    @SerializedName("gold_info")
    public GoldScale goldScale;

    @SerializedName("task_list")
    public List<TaskInfo> taskInfoList;

    @SerializedName("goods_list")
    public List<GoodInfo> goodList;

    @SerializedName("sign_list")
    public List<SignSetInfo> signInfoList;

    @SerializedName("sign_info")
    public UserSignInfo userSignInfo;

    @SerializedName("cashout_list")
    public List<CashInfo> cashInfoList;

    public class GoldUser {
        private int goldnum;
        private double cash;

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

    public class GoldScale {
        private int goldnum;
        private int cash;
        private String content;

        public int getGoldnum() {
            return goldnum;
        }

        public void setGoldnum(int goldnum) {
            this.goldnum = goldnum;
        }

        public int getCash() {
            return cash;
        }

        public void setCash(int cash) {
            this.cash = cash;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public class SignSetInfo {
        private int days;//连续签到天数
        private int gold;//金币奖励数量
        private String cash;//现金奖励区间

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }
    }

    public class UserSignInfo {
        private int signdays;//用户连续签到天数
        private Long lastsigntime;//用户上次签到时间

        public int getSigndays() {
            return signdays;
        }

        public void setSigndays(int signdays) {
            this.signdays = signdays;
        }

        public Long getLastsigntime() {
            return lastsigntime;
        }

        public void setLastsigntime(Long lastsigntime) {
            this.lastsigntime = lastsigntime;
        }
    }

    public GoldUser getGoldUser() {
        return goldUser;
    }

    public void setGoldUser(GoldUser goldUser) {
        this.goldUser = goldUser;
    }

    public GoldScale getGoldScale() {
        return goldScale;
    }

    public void setGoldScale(GoldScale goldScale) {
        this.goldScale = goldScale;
    }


    public List<TaskInfo> getTaskInfoList() {
        return taskInfoList;
    }

    public void setTaskInfoList(List<TaskInfo> taskInfoList) {
        this.taskInfoList = taskInfoList;
    }

    public List<GoodInfo> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodInfo> goodList) {
        this.goodList = goodList;
    }

    public List<SignSetInfo> getSignInfoList() {
        return signInfoList;
    }

    public void setSignInfoList(List<SignSetInfo> signInfoList) {
        this.signInfoList = signInfoList;
    }

    public UserSignInfo getUserSignInfo() {
        return userSignInfo;
    }

    public void setUserSignInfo(UserSignInfo userSignInfo) {
        this.userSignInfo = userSignInfo;
    }

    public List<CashInfo> getCashInfoList() {
        return cashInfoList;
    }

    public void setCashInfoList(List<CashInfo> cashInfoList) {
        this.cashInfoList = cashInfoList;
    }
}
