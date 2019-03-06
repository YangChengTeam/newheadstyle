package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.model.FollowModelImp;
import com.feiyou.headstyle.view.FollowInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class FollowInfoPresenterImp extends BasePresenterImp<IBaseView, FollowInfoRet> implements FollowInfoPresenter {
    private Context context = null;
    private FollowModelImp followModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public FollowInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        followModelImp = new FollowModelImp(context);
    }

    @Override
    public void addFollow(String owenUserId, String userId) {
        followModelImp.addFollow(owenUserId, userId, this);
    }

    @Override
    public void followTopic(String userId, String topicId) {
        followModelImp.followTopic(userId, topicId, this);
    }
}
