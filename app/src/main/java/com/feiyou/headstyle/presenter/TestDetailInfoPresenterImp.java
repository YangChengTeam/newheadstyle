package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.TestDetailInfoRet;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.model.TestDetailInfoModelImp;
import com.feiyou.headstyle.model.TestInfoModelImp;
import com.feiyou.headstyle.view.TestDetailInfoView;
import com.feiyou.headstyle.view.TestInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class TestDetailInfoPresenterImp extends BasePresenterImp<TestDetailInfoView, TestDetailInfoRet> implements TestDetailInfoPresenter {

    private Context context = null;

    private TestDetailInfoModelImp testDetailInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public TestDetailInfoPresenterImp(TestDetailInfoView view, Context context) {
        super(view);
        testDetailInfoModelImp = new TestDetailInfoModelImp(context);
    }

    @Override
    public void getTestDetail(String tid, int type) {
        testDetailInfoModelImp.getTestDetail(tid, type, this);
    }
}
