package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/27.
 */
public class NoteInfoRet extends ResultInfo {
    private List<NoteInfo> data;

    public List<NoteInfo> getData() {
        return data;
    }

    public void setData(List<NoteInfo> data) {
        this.data = data;
    }
}
