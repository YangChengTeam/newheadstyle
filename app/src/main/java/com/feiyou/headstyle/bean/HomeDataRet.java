package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class HomeDataRet extends ResultInfo {
    private HomeDataWrapper data;

    public HomeDataWrapper getData() {
        return data;
    }

    public void setData(HomeDataWrapper data) {
        this.data = data;
    }
}
