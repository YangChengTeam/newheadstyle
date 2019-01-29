package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myflying on 2018/11/27.
 */
public class NoteCommentRet extends ResultInfo {

    private List<NoteItem> data;

    public List<NoteItem> getData() {
        return data;
    }

    public void setData(List<NoteItem> data) {
        this.data = data;
    }
}
