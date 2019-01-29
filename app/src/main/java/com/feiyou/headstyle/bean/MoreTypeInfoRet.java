package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/15.
 */
public class MoreTypeInfoRet extends ResultInfo {

    private List<MoreTypeInfo> data;

    public List<MoreTypeInfo> getData() {
        return data;
    }

    public void setData(List<MoreTypeInfo> data) {
        this.data = data;
    }
}
