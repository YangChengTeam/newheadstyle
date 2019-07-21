package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.CashInfoRet;
import com.feiyou.headstyle.bean.CashMoneyInfoRet;
import com.feiyou.headstyle.model.CashInfoModelImp;
import com.feiyou.headstyle.model.CashMoneyInfoModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class CashInfoPresenterImp extends BasePresenterImp<IBaseView, CashInfoRet> implements CashInfoPresenter {
    private Context context = null;
    private CashInfoModelImp cashInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public CashInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        cashInfoModelImp = new CashInfoModelImp(context);
    }

    @Override
    public void startCash(String uid, String openid, int money, int stype, String imei) {
        cashInfoModelImp.startCash(uid, openid, money, stype, imei, this);
    }
}
