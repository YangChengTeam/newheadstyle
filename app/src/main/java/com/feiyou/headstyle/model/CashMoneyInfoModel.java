package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface CashMoneyInfoModel<T> {
    void cashMoneyList(String uid,String openid,String imei, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
