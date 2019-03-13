package com.feiyou.headstyle.presenter;

import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.UserInfo;

/**
 * Created by iflying on 2018/1/9.
 */

public interface UserInfoPresenter {
    void login(LoginRequest loginRequest);
    void getUserSig(String userName);
    void updateUserInfo(UserInfo updateInfo);
    void getUserInfo(String userId);
    void updateOneInfo(UserInfo updateInfo);
}
