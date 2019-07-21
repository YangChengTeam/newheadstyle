package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.BindAccountInfoRet;
import com.feiyou.headstyle.bean.GoodDetailInfoRet;
import com.feiyou.headstyle.model.BindAccountModelImp;
import com.feiyou.headstyle.model.GoodDetailInfoModelImp;
import com.feiyou.headstyle.view.BindAccountInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class BindAccountPresenterImp extends BasePresenterImp<BindAccountInfoView, BindAccountInfoRet> implements BindAccountPresenter {
    private Context context = null;
    private BindAccountModelImp bindAccountModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public BindAccountPresenterImp(BindAccountInfoView view, Context context) {
        super(view);
        bindAccountModelImp = new BindAccountModelImp(context);
    }

    @Override
    public void bindAccount(String uid, String account, String orderNumber) {
        bindAccountModelImp.bindAccount(uid, account, orderNumber, this);
    }

}
