package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.model.CollectDataModelImp;
import com.feiyou.headstyle.model.HomeDataModelImp;
import com.feiyou.headstyle.view.CollectDataView;
import com.feiyou.headstyle.view.HomeDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class CollectDataPresenterImp extends BasePresenterImp<CollectDataView, CollectInfoRet> implements CollectDataPresenter {
    private Context context = null;
    private CollectDataModelImp collectDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public CollectDataPresenterImp(CollectDataView view, Context context) {
        super(view);
        collectDataModelImp = new CollectDataModelImp(context);
    }

    @Override
    public void getDataById(String bid) {
        collectDataModelImp.getDataById(bid, this);
    }
}
