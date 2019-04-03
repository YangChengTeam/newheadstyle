package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.bean.WordInfoRet;
import com.feiyou.headstyle.model.VideoInfoModelImp;
import com.feiyou.headstyle.model.WordInfoModelImp;
import com.feiyou.headstyle.view.VideoInfoView;
import com.feiyou.headstyle.view.WordInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class VideoInfoPresenterImp extends BasePresenterImp<VideoInfoView, VideoInfoRet> implements VideoInfoPresenter {

    private Context context = null;

    private VideoInfoModelImp videoInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public VideoInfoPresenterImp(VideoInfoView view, Context context) {
        super(view);
        videoInfoModelImp = new VideoInfoModelImp(context);
    }

    @Override
    public void getDataList(int page,String uid) {
        videoInfoModelImp.getDataList(page,uid, this);
    }
}
