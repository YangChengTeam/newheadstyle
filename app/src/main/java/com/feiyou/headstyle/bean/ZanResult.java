package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/2/16.
 */
public class ZanResult {

    @SerializedName("zan_num")
    private Integer zanNum;

    @SerializedName("is_zan")
    private Integer isZan;

    public Integer getZanNum() {
        return zanNum;
    }

    public void setZanNum(Integer zanNum) {
        this.zanNum = zanNum;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
    }
}
