package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/15.
 */
public class VideoInfoRet extends ResultInfo {

    private VideoWrapper data;

    public VideoWrapper getData() {
        return data;
    }

    public void setData(VideoWrapper data) {
        this.data = data;
    }
}
