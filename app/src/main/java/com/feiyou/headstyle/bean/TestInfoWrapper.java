package com.feiyou.headstyle.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myflying on 2019/2/21.
 */
public class TestInfoWrapper {

    @SerializedName("hot_list")
    private List<TestInfo> hotList;

    @SerializedName("tui_list")
    private List<TestInfo> tuiList;

    public List<TestInfo> getHotList() {
        return hotList;
    }

    public void setHotList(List<TestInfo> hotList) {
        this.hotList = hotList;
    }

    public List<TestInfo> getTuiList() {
        return tuiList;
    }

    public void setTuiList(List<TestInfo> tuiList) {
        this.tuiList = tuiList;
    }
}
