package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/27.
 */
public class TaskInfoRet extends ResultInfo {
    private List<TaskInfo> data;

    public List<TaskInfo> getData() {
        return data;
    }

    public void setData(List<TaskInfo> data) {
        this.data = data;
    }
}
