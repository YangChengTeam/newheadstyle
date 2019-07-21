package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.WelfareInfoRet;
import com.feiyou.headstyle.bean.WordInfoRet;
import com.feiyou.headstyle.model.WelfareInfoModelImp;
import com.feiyou.headstyle.model.WordInfoModelImp;
import com.feiyou.headstyle.view.WelfareInfoView;
import com.feiyou.headstyle.view.WordInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class WelfareInfoPresenterImp extends BasePresenterImp<IBaseView, WelfareInfoRet> implements WelfareInfoPresenter {
    private Context context = null;
    private WelfareInfoModelImp welfareInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public WelfareInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        welfareInfoModelImp = new WelfareInfoModelImp(context);
    }

    @Override
    public void getWelfareData(String uid) {
        welfareInfoModelImp.getWelfareData(uid, this);
    }
}
