package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.EveryDayHbRet;
import com.feiyou.headstyle.bean.HongBaoInfoRet;
import com.feiyou.headstyle.model.EveryDayHongBaoModelImp;
import com.feiyou.headstyle.model.HongBaoInfoModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class HongBaoInfoPresenterImp extends BasePresenterImp<IBaseView, HongBaoInfoRet> implements HongBaoInfoPresenter {
    private Context context = null;
    private HongBaoInfoModelImp hongBaoInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public HongBaoInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        hongBaoInfoModelImp = new HongBaoInfoModelImp(context);
    }

    @Override
    public void getHBInfo(String uid, String openid, String imei) {
        hongBaoInfoModelImp.getHBInfo(uid, openid, imei, this);
    }
}
