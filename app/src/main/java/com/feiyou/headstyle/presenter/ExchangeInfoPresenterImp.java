package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ExchangeInfoRet;
import com.feiyou.headstyle.bean.GoodDetailInfoRet;
import com.feiyou.headstyle.model.ExchangeInfoModelImp;
import com.feiyou.headstyle.model.GoodDetailInfoModelImp;
import com.feiyou.headstyle.view.ExchangeInfoView;
import com.feiyou.headstyle.view.GoodDetailInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class ExchangeInfoPresenterImp extends BasePresenterImp<IBaseView, ExchangeInfoRet> implements ExchangeInfoPresenter {
    private Context context = null;
    private ExchangeInfoModelImp exchangeInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public ExchangeInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        exchangeInfoModelImp = new ExchangeInfoModelImp(context);
    }

    @Override
    public void exchangeGood(String gid, String uid, int type) {
        exchangeInfoModelImp.exchangeGood(gid, uid, type, this);
    }

    @Override
    public void exchangeList(String uid, String openid, int page, int pageSize, String eid) {
        exchangeInfoModelImp.exchangeList(uid, openid, page, pageSize, eid, this);
    }
}
