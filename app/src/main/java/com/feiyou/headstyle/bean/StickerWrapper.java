package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2019/2/19.
 */
public class StickerWrapper {

    private List<String> type;

    private List<List<StickerInfo>> list;

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<List<StickerInfo>> getList() {
        return list;
    }

    public void setList(List<List<StickerInfo>> list) {
        this.list = list;
    }
}
