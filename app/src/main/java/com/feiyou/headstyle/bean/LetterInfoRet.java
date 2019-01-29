package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/15.
 */
public class LetterInfoRet extends ResultInfo {

    private List<LetterInfo> data;

    public List<LetterInfo> getData() {
        return data;
    }

    public void setData(List<LetterInfo> data) {
        this.data = data;
    }
}
