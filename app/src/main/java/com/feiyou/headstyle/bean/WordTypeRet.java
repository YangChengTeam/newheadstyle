package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/15.
 */
public class WordTypeRet extends ResultInfo {

    private List<WordTypeInfo> data;

    public List<WordTypeInfo> getData() {
        return data;
    }

    public void setData(List<WordTypeInfo> data) {
        this.data = data;
    }
}
