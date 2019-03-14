package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.StarPosterRet;
import com.feiyou.headstyle.model.PosterModelImp;
import com.feiyou.headstyle.model.ReportInfoModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class ReportInfoPresenterImp extends BasePresenterImp<IBaseView, ResultInfo> implements ReportInfoPresenter {
    private ReportInfoModelImp reportInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public ReportInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        reportInfoModelImp = new ReportInfoModelImp(context);
    }

    @Override
    public void takeReport(String userId, String rid, int type, String content, String intro) {
        reportInfoModelImp.takeReport(userId, rid, type, content, intro, this);
    }
}
