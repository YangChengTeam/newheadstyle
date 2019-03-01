package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/3/1.
 */
public class FollowInfo {
    @SerializedName("is_guan")
    private int isGuan;//1:已关注 0：未关注

    public int getIsGuan() {
        return isGuan;
    }

    public void setIsGuan(int isGuan) {
        this.isGuan = isGuan;
    }
}
