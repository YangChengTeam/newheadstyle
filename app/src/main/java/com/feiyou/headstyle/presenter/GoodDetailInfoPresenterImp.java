package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.GoodDetailInfoRet;
import com.feiyou.headstyle.bean.GoodInfoRet;
import com.feiyou.headstyle.model.GoodDetailInfoModelImp;
import com.feiyou.headstyle.model.GoodInfoModelImp;
import com.feiyou.headstyle.view.GoodDetailInfoView;
import com.feiyou.headstyle.view.GoodInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class GoodDetailInfoPresenterImp extends BasePresenterImp<IBaseView, GoodDetailInfoRet> implements GoodDetailInfoPresenter {
    private Context context = null;
    private GoodDetailInfoModelImp goodDetailInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public GoodDetailInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        goodDetailInfoModelImp = new GoodDetailInfoModelImp(context);
    }

    @Override
    public void getGoodDetail(String gid, String uid) {
        goodDetailInfoModelImp.getGoodDetail(gid, uid, this);
    }
}
