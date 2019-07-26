package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface ExchangeInfoModel<T> {
    void exchangeGood(String gid, String uid, int type, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void exchangeList(String uid, String openid,int page, int pageSize, String eid, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
