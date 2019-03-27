package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class AddNoteRet extends ResultInfo {
    private NoteInfo data;

    public NoteInfo getData() {
        return data;
    }

    public void setData(NoteInfo data) {
        this.data = data;
    }
}
