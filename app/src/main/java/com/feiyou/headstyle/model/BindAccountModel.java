package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface BindAccountModel<T> {
    void bindAccount(String uid, String openid,String account, String orderNumber, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
