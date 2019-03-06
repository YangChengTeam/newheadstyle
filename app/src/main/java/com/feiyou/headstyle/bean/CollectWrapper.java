package com.feiyou.headstyle.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myflying on 2018/12/29.
 */
public class CollectWrapper{
    private CollectInfo info;
    private ArrayList<HeadInfo> list;

    public CollectInfo getInfo() {
        return info;
    }

    public void setInfo(CollectInfo info) {
        this.info = info;
    }

    public ArrayList<HeadInfo> getList() {
        return list;
    }

    public void setList(ArrayList<HeadInfo> list) {
        this.list = list;
    }
}
