package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.VersionInfoRet;
import com.feiyou.headstyle.bean.VideoCommentRet;
import com.feiyou.headstyle.model.VersionModelImp;
import com.feiyou.headstyle.model.VideoCommentModelImp;
import com.feiyou.headstyle.view.VersionView;
import com.feiyou.headstyle.view.VideoInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class VersionPresenterImp extends BasePresenterImp<VersionView, VersionInfoRet> implements VersionPresenter {

    private Context context = null;

    private VersionModelImp versionModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public VersionPresenterImp(VersionView view, Context context) {
        super(view);
        versionModelImp = new VersionModelImp(context);
    }

    @Override
    public void getVersionInfo(String channel) {
        versionModelImp.getVersionInfo(channel,this);
    }

}
