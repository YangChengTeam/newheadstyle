package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface CashRecordModel<T> {
    void cashList(String uid, int page, int pageSize, IBaseRequestCallBack<T> iBaseRequestCallBack);
}