package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/27.
 */
public class NoteSubCommentRet extends ResultInfo {

    private List<NoteSubComment> data;

    public List<NoteSubComment> getData() {
        return data;
    }

    public void setData(List<NoteSubComment> data) {
        this.data = data;
    }
}
