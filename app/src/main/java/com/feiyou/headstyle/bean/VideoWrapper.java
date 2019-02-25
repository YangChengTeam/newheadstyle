package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2019/2/25.
 */
public class VideoWrapper {
    private List<VideoInfo> list;
    private int page;

    public List<VideoInfo> getList() {
        return list;
    }

    public void setList(List<VideoInfo> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
