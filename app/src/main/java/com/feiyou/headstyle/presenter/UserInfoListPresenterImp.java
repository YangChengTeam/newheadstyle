package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.UserInfoListRet;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.model.UserInfoListModelImp;
import com.feiyou.headstyle.model.UserInfoModelImp;
import com.feiyou.headstyle.view.UserInfoListView;
import com.feiyou.headstyle.view.UserInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class UserInfoListPresenterImp extends BasePresenterImp<UserInfoListView, UserInfoListRet> implements UserInfoListPresenter {
    private Context context = null;
    private UserInfoListModelImp userInfoListModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public UserInfoListPresenterImp(UserInfoListView view, Context context) {
        super(view);
        userInfoListModelImp = new UserInfoListModelImp(context);
    }

    @Override
    public void addFriendsList(int page) {
        userInfoListModelImp.addFriendsList(page, this);
    }

    @Override
    public void searchFriendsList(int page, String keyWord) {
        userInfoListModelImp.searchFriendsList(page, keyWord, this);
    }

    @Override
    public void getMyGuanFenList(int page, String userId, int type) {
        userInfoListModelImp.getMyGuanFenList(page, userId, type, this);
    }
}
