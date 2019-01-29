package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.MoreTypeInfoRet;
import com.feiyou.headstyle.model.CollectDataModelImp;
import com.feiyou.headstyle.model.MoreTypeDataModelImp;
import com.feiyou.headstyle.view.CollectDataView;
import com.feiyou.headstyle.view.MoreTypeDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class MoreTypeDataPresenterImp extends BasePresenterImp<MoreTypeDataView, MoreTypeInfoRet> implements MoreTypeDataPresenter {
    private Context context = null;
    private MoreTypeDataModelImp moreTypeDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public MoreTypeDataPresenterImp(MoreTypeDataView view, Context context) {
        super(view);
        moreTypeDataModelImp = new MoreTypeDataModelImp(context);
    }

    @Override
    public void getMoreTypeList() {
        moreTypeDataModelImp.getMoreTypeList(this);
    }
}
