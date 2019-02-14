package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.model.ReplyCommentModelImp;
import com.feiyou.headstyle.model.TopicDataModelImp;
import com.feiyou.headstyle.view.ReplyCommentView;
import com.feiyou.headstyle.view.TopicDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class ReplyCommentPresenterImp extends BasePresenterImp<IBaseView, ReplyResultInfoRet> implements ReplyCommentPresenter {
    private Context context = null;
    private ReplyCommentModelImp replyCommentModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public ReplyCommentPresenterImp(IBaseView view, Context context) {
        super(view);
        replyCommentModelImp = new ReplyCommentModelImp(context);
    }

    @Override
    public void addReplyInfo(ReplyParams replyParams) {
        replyCommentModelImp.addReplyInfo(replyParams, this);
    }
}
