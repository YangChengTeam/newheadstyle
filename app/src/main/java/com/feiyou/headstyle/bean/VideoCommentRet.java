package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/15.
 */
public class VideoCommentRet extends ResultInfo {

    private List<NoteItem> data;

    public List<NoteItem> getData() {
        return data;
    }

    public void setData(List<NoteItem> data) {
        this.data = data;
    }
}
