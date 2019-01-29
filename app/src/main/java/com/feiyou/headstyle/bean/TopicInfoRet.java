package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class TopicInfoRet extends ResultInfo{

    private List<TopicInfo> data;

    public List<TopicInfo> getData() {
        return data;
    }

    public void setData(List<TopicInfo> data) {
        this.data = data;
    }
}
