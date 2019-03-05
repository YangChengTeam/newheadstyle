package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.MyAtMessageRet;
import com.feiyou.headstyle.model.MyAtMessageModelImp;
import com.feiyou.headstyle.view.MyAtMessageView;

/**
 * Created by iflying on 2018/1/9.
 */

public class MyAtMessagePresenterImp extends BasePresenterImp<MyAtMessageView, MyAtMessageRet> implements MyAtMessagePresenter {
    private Context context = null;
    private MyAtMessageModelImp myAtMessageModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public MyAtMessagePresenterImp(MyAtMessageView view, Context context) {
        super(view);
        myAtMessageModelImp = new MyAtMessageModelImp(context);
    }

    @Override
    public void getMyAtMessageList(String uid, int type, int page) {
        myAtMessageModelImp.getMyAtMessageList(uid, type, page, this);
    }
}
