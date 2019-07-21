package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class ExchangeInfoRet extends ResultInfo {

    private List<ExchangeInfo> data;

    public List<ExchangeInfo> getData() {
        return data;
    }

    public void setData(List<ExchangeInfo> data) {
        this.data = data;
    }
}
