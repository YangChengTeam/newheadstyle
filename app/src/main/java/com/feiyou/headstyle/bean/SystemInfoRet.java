package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class SystemInfoRet extends ResultInfo {
    private List<SystemInfo> data;

    public List<SystemInfo> getData() {
        return data;
    }

    public void setData(List<SystemInfo> data) {
        this.data = data;
    }
}
