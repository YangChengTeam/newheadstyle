package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.GoldDetailRet;
import com.feiyou.headstyle.bean.GoodDetailInfoRet;
import com.feiyou.headstyle.model.GoldDetailModelImp;
import com.feiyou.headstyle.model.GoodDetailInfoModelImp;
import com.feiyou.headstyle.view.GoldDetailView;

/**
 * Created by iflying on 2018/1/9.
 */

public class GoldDetailPresenterImp extends BasePresenterImp<GoldDetailView, GoldDetailRet> implements GoldDetailPresenter {
    private Context context = null;
    private GoldDetailModelImp goldDetailModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public GoldDetailPresenterImp(GoldDetailView view, Context context) {
        super(view);
        goldDetailModelImp = new GoldDetailModelImp(context);
    }

    @Override
    public void goldDetailList(String uid, String openid,int page, int pageSize) {
        goldDetailModelImp.goldDetailList(uid, openid,page, pageSize, this);
    }
}
