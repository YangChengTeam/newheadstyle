package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.AddNoteRet;
import com.feiyou.headstyle.bean.FeedBackRet;
import com.feiyou.headstyle.model.AddNoteModelImp;
import com.feiyou.headstyle.model.FeedBackModelImp;
import com.feiyou.headstyle.view.AddNoteView;
import com.feiyou.headstyle.view.FeedBackView;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public class FeedBackPresenterImp extends BasePresenterImp<FeedBackView, FeedBackRet> implements FeedBackPresenter {
    private Context context = null;
    private FeedBackModelImp feedBackModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public FeedBackPresenterImp(FeedBackView view, Context context) {
        super(view);
        feedBackModelImp = new FeedBackModelImp(context);
    }

    @Override
    public void addFeedBack(String uid, String content, String phone, List<String> filePaths) {
        feedBackModelImp.addFeedBack(uid, content, phone, filePaths, this);
    }
}
