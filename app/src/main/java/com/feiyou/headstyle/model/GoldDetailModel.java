package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface GoldDetailModel<T> {
    void goldDetailList(String uid, int page, int pageSize, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
