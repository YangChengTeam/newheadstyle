package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.model.TopicDataModelImp;
import com.feiyou.headstyle.view.CollectDataView;
import com.feiyou.headstyle.view.TopicDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class TopicDataPresenterImp extends BasePresenterImp<TopicDataView, TopicInfoRet> implements TopicDataPresenter {
    private Context context = null;
    private TopicDataModelImp topicDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public TopicDataPresenterImp(TopicDataView view, Context context) {
        super(view);
        topicDataModelImp = new TopicDataModelImp(context);
    }

    @Override
    public void getTopicDataList() {
        topicDataModelImp.getTopicDataList(this);
    }
}
