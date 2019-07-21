package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.GoodInfoRet;
import com.feiyou.headstyle.bean.WelfareInfoRet;
import com.feiyou.headstyle.model.GoodInfoModelImp;
import com.feiyou.headstyle.model.WelfareInfoModelImp;
import com.feiyou.headstyle.view.GoodInfoView;
import com.feiyou.headstyle.view.WelfareInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class GoodInfoPresenterImp extends BasePresenterImp<GoodInfoView, GoodInfoRet> implements GoodInfoPresenter {
    private Context context = null;
    private GoodInfoModelImp goodInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public GoodInfoPresenterImp(GoodInfoView view, Context context) {
        super(view);
        goodInfoModelImp = new GoodInfoModelImp(context);
    }

    @Override
    public void getGoodListData(int page, int pageSize) {
        goodInfoModelImp.getGoodListData(page, pageSize, this);
    }

}
