package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.FriendsGroupRet;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.model.FriendsDataModelImp;
import com.feiyou.headstyle.model.HeadListDataModelImp;
import com.feiyou.headstyle.view.FriendsDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class FriendsDataPresenterImp extends BasePresenterImp<FriendsDataView, FriendsGroupRet> implements FriendsDataPresenter {
    private Context context = null;
    private FriendsDataModelImp friendsDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public FriendsDataPresenterImp(FriendsDataView view, Context context) {
        super(view);
        friendsDataModelImp = new FriendsDataModelImp(context);
    }

    @Override
    public void getFriendsByUserId(String uid) {
        friendsDataModelImp.getFriendsByUserId(uid, this);
    }
}
