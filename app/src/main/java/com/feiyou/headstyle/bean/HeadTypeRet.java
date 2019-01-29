package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class HeadTypeRet extends ResultInfo{
    private List<HeadType> data;

    public List<HeadType> getData() {
        return data;
    }

    public void setData(List<HeadType> data) {
        this.data = data;
    }
}
