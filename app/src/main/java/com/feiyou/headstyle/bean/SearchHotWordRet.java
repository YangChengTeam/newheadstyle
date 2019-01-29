package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/22.
 */
public class SearchHotWordRet extends ResultInfo {
    private List<SearchHotWord> data;

    public List<SearchHotWord> getData() {
        return data;
    }

    public void setData(List<SearchHotWord> data) {
        this.data = data;
    }
}
