package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.CashInfoRet;
import com.feiyou.headstyle.bean.CashRecordRet;
import com.feiyou.headstyle.model.CashInfoModelImp;
import com.feiyou.headstyle.model.CashRecordModelImp;
import com.feiyou.headstyle.view.CashRecordView;

/**
 * Created by iflying on 2018/1/9.
 */

public class CashRecordPresenterImp extends BasePresenterImp<CashRecordView, CashRecordRet> implements CashRecordPresenter {
    private Context context = null;
    private CashRecordModelImp cashRecordModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public CashRecordPresenterImp(CashRecordView view, Context context) {
        super(view);
        cashRecordModelImp = new CashRecordModelImp(context);
    }

    @Override
    public void cashList(String uid,String openid, int page, int pageSize) {
        cashRecordModelImp.cashList(uid, openid,page, pageSize, this);
    }
}
