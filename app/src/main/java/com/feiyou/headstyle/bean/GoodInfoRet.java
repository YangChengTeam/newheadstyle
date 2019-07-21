package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class GoodInfoRet extends ResultInfo {

    private List<GoodInfo> data;

    public List<GoodInfo> getData() {
        return data;
    }

    public void setData(List<GoodInfo> data) {
        this.data = data;
    }
}
