package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.MyCommentRet;
import com.feiyou.headstyle.bean.SystemInfoRet;
import com.feiyou.headstyle.model.MyCommentModelImp;
import com.feiyou.headstyle.model.SystemInfoModelImp;
import com.feiyou.headstyle.view.MyCommentView;
import com.feiyou.headstyle.view.SystemInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class SystemInfoPresenterImp extends BasePresenterImp<SystemInfoView, SystemInfoRet> implements SystemInfoPresenter {
    private Context context = null;
    private SystemInfoModelImp systemInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public SystemInfoPresenterImp(SystemInfoView view, Context context) {
        super(view);
        systemInfoModelImp = new SystemInfoModelImp(context);
    }

    @Override
    public void getSystemInfoList(String uid, int type, int page) {
        systemInfoModelImp.getSystemInfoList(uid, type, page, this);
    }
}
