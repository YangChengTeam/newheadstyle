package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.UserInfo;

/**
 * Created by iflying on 2018/1/9.
 */

public interface UserInfoModel<T> {
    void login(LoginRequest loginRequest, IBaseRequestCallBack<T> iBaseRequestCallBack);
    void getUserSig(String userName, IBaseRequestCallBack<T> iBaseRequestCallBack);
    void updateUserInfo(UserInfo updateInfo, IBaseRequestCallBack<T> iBaseRequestCallBack);
    void getUserInfo(String userId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
