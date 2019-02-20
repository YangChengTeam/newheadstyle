package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class TestInfoRet extends ResultInfo {

    private List<TestInfo> data;

    public List<TestInfo> getData() {
        return data;
    }

    public void setData(List<TestInfo> data) {
        this.data = data;
    }
}
