package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.bean.WordInfoRet;
import com.feiyou.headstyle.model.UserInfoModelImp;
import com.feiyou.headstyle.model.WordInfoModelImp;
import com.feiyou.headstyle.view.UserInfoView;
import com.feiyou.headstyle.view.WordInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class UserInfoPresenterImp extends BasePresenterImp<IBaseView, UserInfoRet> implements UserInfoPresenter {
    private Context context = null;
    private UserInfoModelImp userInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public UserInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        userInfoModelImp = new UserInfoModelImp(context);
    }

    @Override
    public void login(LoginRequest loginRequest) {
        userInfoModelImp.login(loginRequest, this);
    }

    @Override
    public void getUserSig(String userName) {
        userInfoModelImp.getUserSig(userName, this);
    }

    @Override
    public void updateUserInfo(UserInfo updateInfo) {
        userInfoModelImp.updateUserInfo(updateInfo, this);
    }

    @Override
    public void getUserInfo(String userId, String toUserId) {
        userInfoModelImp.getUserInfo(userId, toUserId, this);
    }

    @Override
    public void updateOneInfo(UserInfo updateInfo) {
        userInfoModelImp.updateOneInfo(updateInfo, this);
    }

    @Override
    public void updateTxOpenId(String uid, String txopenid, String txnickname) {
        userInfoModelImp.updateTxOpenId(uid, txopenid, txnickname, this);
    }
}
