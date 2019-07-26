package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.BindAccountInfoRet;
import com.feiyou.headstyle.bean.CashMoneyInfoRet;
import com.feiyou.headstyle.model.BindAccountModelImp;
import com.feiyou.headstyle.model.CashMoneyInfoModelImp;
import com.feiyou.headstyle.view.BindAccountInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class CashMoneyInfoPresenterImp extends BasePresenterImp<IBaseView, CashMoneyInfoRet> implements CashMoneyInfoPresenter {
    private Context context = null;
    private CashMoneyInfoModelImp cashMoneyInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public CashMoneyInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        cashMoneyInfoModelImp = new CashMoneyInfoModelImp(context);
    }

    @Override
    public void cashMoneyList(String uid,String openid,String imei) {
        cashMoneyInfoModelImp.cashMoneyList(uid,openid,imei, this);
    }
}
