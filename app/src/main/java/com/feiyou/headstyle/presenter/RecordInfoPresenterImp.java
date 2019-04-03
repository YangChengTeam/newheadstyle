package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.RecordInfoRet;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.model.RecordInfoModelImp;
import com.feiyou.headstyle.model.ReplyCommentModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class RecordInfoPresenterImp extends BasePresenterImp<IBaseView, RecordInfoRet> implements RecordInfoPresenter {
    private Context context = null;
    private RecordInfoModelImp replyCommentModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public RecordInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        replyCommentModelImp = new RecordInfoModelImp(context);
    }

    @Override
    public void headSetInfo(String uid, String headId) {
        replyCommentModelImp.headSetInfo(uid, headId, this);
    }

    @Override
    public void adClickInfo(String uid, String aid) {
        replyCommentModelImp.adClickInfo(uid, aid, this);
    }
}
