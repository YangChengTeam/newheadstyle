package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class MyCommentRet extends ResultInfo {
    private List<MyCommentInfo> data;

    public List<MyCommentInfo> getData() {
        return data;
    }

    public void setData(List<MyCommentInfo> data) {
        this.data = data;
    }
}
