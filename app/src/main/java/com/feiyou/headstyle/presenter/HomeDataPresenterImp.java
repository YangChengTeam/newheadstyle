package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.LetterInfoRet;
import com.feiyou.headstyle.model.HomeDataModelImp;
import com.feiyou.headstyle.model.LetterDataModelImp;
import com.feiyou.headstyle.view.HomeDataView;
import com.feiyou.headstyle.view.LetterDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class HomeDataPresenterImp extends BasePresenterImp<HomeDataView, HomeDataRet> implements HomeDataPresenter {
    private Context context = null;
    private HomeDataModelImp homeDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public HomeDataPresenterImp(HomeDataView view, Context context) {
        super(view);
        homeDataModelImp = new HomeDataModelImp(context);
    }

    @Override
    public void getData(String page, String pageSize, String change) {
        homeDataModelImp.getData(page, pageSize, change, this);
    }
}
