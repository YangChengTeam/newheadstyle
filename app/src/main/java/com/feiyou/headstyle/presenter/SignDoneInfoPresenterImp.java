package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.SignDoneInfoRet;
import com.feiyou.headstyle.bean.WelfareInfoRet;
import com.feiyou.headstyle.model.SignDoneInfoModelImp;
import com.feiyou.headstyle.model.WelfareInfoModelImp;
import com.feiyou.headstyle.view.SignDoneInfoView;
import com.feiyou.headstyle.view.WelfareInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class SignDoneInfoPresenterImp extends BasePresenterImp<IBaseView, SignDoneInfoRet> implements SignDoneInfoPresenter {
    private Context context = null;
    private SignDoneInfoModelImp signDoneInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public SignDoneInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        signDoneInfoModelImp = new SignDoneInfoModelImp(context);
    }

    @Override
    public void signDone(String uid, double cash) {
        signDoneInfoModelImp.signDone(uid,cash,this);
    }
}
