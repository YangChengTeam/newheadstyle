package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.model.HeadListDataModelImp;
import com.feiyou.headstyle.view.HeadListDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class HeadListDataPresenterImp extends BasePresenterImp<IBaseView, HeadInfoRet> implements HeadListDataPresenter {
    private Context context = null;
    private HeadListDataModelImp headListDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public HeadListDataPresenterImp(IBaseView view, Context context) {
        super(view);
        headListDataModelImp = new HeadListDataModelImp(context);
    }

    @Override
    public void getDataByTagId(String tagId, int page, int pageSize) {
        headListDataModelImp.getDataByTagId(tagId, page, pageSize, this);
    }

    @Override
    public void getSearchList(int page, String keyword, String userId) {
        headListDataModelImp.getSearchList(page, keyword, userId, this);
    }
}
