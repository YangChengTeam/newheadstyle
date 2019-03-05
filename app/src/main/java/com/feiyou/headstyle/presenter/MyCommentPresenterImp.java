package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.MyCommentRet;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.model.MyCommentModelImp;
import com.feiyou.headstyle.model.NoteCommentDataModelImp;
import com.feiyou.headstyle.view.MyCommentView;

/**
 * Created by iflying on 2018/1/9.
 */

public class MyCommentPresenterImp extends BasePresenterImp<MyCommentView, MyCommentRet> implements MyCommentPresenter {
    private Context context = null;
    private MyCommentModelImp myCommentModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public MyCommentPresenterImp(MyCommentView view, Context context) {
        super(view);
        myCommentModelImp = new MyCommentModelImp(context);
    }

    @Override
    public void getMyCommentList(String uid, int type, int page) {
        myCommentModelImp.getMyCommentList(uid, type, page, this);
    }
}
