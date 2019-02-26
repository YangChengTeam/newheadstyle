package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.VideoCommentRet;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.model.VideoCommentModelImp;
import com.feiyou.headstyle.model.VideoInfoModelImp;
import com.feiyou.headstyle.view.VideoInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class VideoCommentPresenterImp extends BasePresenterImp<VideoInfoView, VideoCommentRet> implements VideoCommentPresenter {

    private Context context = null;

    private VideoCommentModelImp videoCommentModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public VideoCommentPresenterImp(VideoInfoView view, Context context) {
        super(view);
        videoCommentModelImp = new VideoCommentModelImp(context);
    }

    @Override
    public void getCommentList(int page, String videoId, String userId) {
        videoCommentModelImp.getCommentList(page, videoId, userId, this);
    }
}
