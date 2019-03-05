package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class MyAtMessageRet extends ResultInfo {
    private List<MyAtMessage> data;

    public List<MyAtMessage> getData() {
        return data;
    }

    public void setData(List<MyAtMessage> data) {
        this.data = data;
    }
}
