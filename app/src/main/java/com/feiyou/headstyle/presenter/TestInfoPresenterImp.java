package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.model.TestInfoModelImp;
import com.feiyou.headstyle.model.TopicDataModelImp;
import com.feiyou.headstyle.view.TestInfoView;
import com.feiyou.headstyle.view.TopicDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class TestInfoPresenterImp extends BasePresenterImp<TestInfoView, TestInfoRet> implements TestInfoPresenter {

    private Context context = null;

    private TestInfoModelImp testInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public TestInfoPresenterImp(TestInfoView view, Context context) {
        super(view);
        testInfoModelImp = new TestInfoModelImp(context);
    }

    @Override
    public void getDataList() {
        testInfoModelImp.getDataList(this);
    }

    @Override
    public void getDataListByCid(String cid) {
        testInfoModelImp.getDataListByCid(cid, this);
    }

    @Override
    public void getHotAndRecommendList(int status) {
        testInfoModelImp.getHotAndRecommendList(status, this);
    }
}
