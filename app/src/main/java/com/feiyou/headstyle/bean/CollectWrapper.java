package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/12/29.
 */
public class CollectWrapper{
    private CollectInfo info;
    private List<HeadInfo> list;

    public CollectInfo getInfo() {
        return info;
    }

    public void setInfo(CollectInfo info) {
        this.info = info;
    }

    public List<HeadInfo> getList() {
        return list;
    }

    public void setList(List<HeadInfo> list) {
        this.list = list;
    }
}
