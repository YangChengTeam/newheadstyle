package com.feiyou.headstyle.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/20.
 */
public class CashRecordRet extends ResultInfo {

    private List<CashRecord> data;

    public List<CashRecord> getData() {
        return data;
    }

    public void setData(List<CashRecord> data) {
        this.data = data;
    }
}
