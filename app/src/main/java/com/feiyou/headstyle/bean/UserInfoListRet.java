package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class UserInfoListRet extends ResultInfo {

    private List<UserInfo> data;

    public List<UserInfo> getData() {
        return data;
    }

    public void setData(List<UserInfo> data) {
        this.data = data;
    }
}
