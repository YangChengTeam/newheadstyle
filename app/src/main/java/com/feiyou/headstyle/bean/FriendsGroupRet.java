package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class FriendsGroupRet extends ResultInfo {
    private List<FriendsGroup> data;

    public List<FriendsGroup> getData() {
        return data;
    }

    public void setData(List<FriendsGroup> data) {
        this.data = data;
    }
}
