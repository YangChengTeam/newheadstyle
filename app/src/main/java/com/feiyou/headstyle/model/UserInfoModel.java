package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.LoginRequest;

/**
 * Created by iflying on 2018/1/9.
 */

public interface UserInfoModel<T> {
    void login(LoginRequest loginRequest, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
