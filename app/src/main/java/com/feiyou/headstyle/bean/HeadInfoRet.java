package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class HeadInfoRet extends ResultInfo {
    private List<HeadInfo> data;

    public List<HeadInfo> getData() {
        return data;
    }

    public void setData(List<HeadInfo> data) {
        this.data = data;
    }
}
