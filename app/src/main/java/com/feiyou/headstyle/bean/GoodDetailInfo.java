package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoodDetailInfo {
    @SerializedName("goods_info")
    private GoodInfo goodInfo;

    @SerializedName("is_exchange")
    private int isExchange;

    @SerializedName("record_list")
    private List<ExchangeRecord> recordList;

    public GoodInfo getGoodInfo() {
        return goodInfo;
    }

    public void setGoodInfo(GoodInfo goodInfo) {
        this.goodInfo = goodInfo;
    }

    public int getIsExchange() {
        return isExchange;
    }

    public void setIsExchange(int isExchange) {
        this.isExchange = isExchange;
    }

    public List<ExchangeRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ExchangeRecord> recordList) {
        this.recordList = recordList;
    }

    public class ExchangeRecord {
        private String nickname;//用户昵称
        private String userimg;//用户头像
        private Long addtime;//兑换时间

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

        public Long getAddtime() {
            return addtime;
        }

        public void setAddtime(Long addtime) {
            this.addtime = addtime;
        }
    }
}
