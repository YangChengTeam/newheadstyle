package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.TestResultInfoRet;
import com.feiyou.headstyle.bean.TestResultParams;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.model.TestResultInfoModelImp;
import com.feiyou.headstyle.model.TopicDataModelImp;
import com.feiyou.headstyle.view.TopicDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class TestResultInfoPresenterImp extends BasePresenterImp<IBaseView, TestResultInfoRet> implements TestResultInfoPresenter {
    private Context context = null;
    private TestResultInfoModelImp testResultInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public TestResultInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        testResultInfoModelImp = new TestResultInfoModelImp(context);
    }

    @Override
    public void createImage(TestResultParams testResultParams) {
        testResultInfoModelImp.createImage(testResultParams, this);
    }
}
