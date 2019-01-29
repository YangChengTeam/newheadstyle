package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2019/1/24.
 */
public class FriendsGroup {

    private String name;

    private List<FriendsInfo> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FriendsInfo> getList() {
        return list;
    }

    public void setList(List<FriendsInfo> list) {
        this.list = list;
    }
}
