package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.EveryDayHbRet;
import com.feiyou.headstyle.model.AddCollectionModelImp;
import com.feiyou.headstyle.model.EveryDayHongBaoModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class EveryDayHongBaoPresenterImp extends BasePresenterImp<IBaseView, EveryDayHbRet> implements EveryDayHongBaoPresenter {
    private Context context = null;
    private EveryDayHongBaoModelImp everyDayHongBaoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public EveryDayHongBaoPresenterImp(IBaseView view, Context context) {
        super(view);
        everyDayHongBaoModelImp = new EveryDayHongBaoModelImp(context);
    }

    @Override
    public void everyDayHongBaoInfo(String uid, String openid, String imei) {
        everyDayHongBaoModelImp.everyDayHongBaoInfo(uid, openid, imei, this);
    }
}
