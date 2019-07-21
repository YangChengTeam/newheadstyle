package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface SignDoneInfoModel<T> {
    void signDone(String uid, double cash, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
