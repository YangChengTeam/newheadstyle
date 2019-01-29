package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class WordInfoRet extends ResultInfo{

    private List<WordInfo> data;

    public List<WordInfo> getData() {
        return data;
    }

    public void setData(List<WordInfo> data) {
        this.data = data;
    }
}
