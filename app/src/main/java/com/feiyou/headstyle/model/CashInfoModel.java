package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface CashInfoModel<T> {
    void startCash(String uid, String openid, int money, int stype, String imei, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
