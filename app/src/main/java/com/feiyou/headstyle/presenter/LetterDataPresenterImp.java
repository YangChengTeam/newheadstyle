package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.LetterInfoRet;
import com.feiyou.headstyle.model.LetterDataModelImp;
import com.feiyou.headstyle.view.LetterDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class LetterDataPresenterImp extends BasePresenterImp<LetterDataView, LetterInfoRet> implements LetterDataPresenter {
    private Context context = null;
    private LetterDataModelImp letterDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public LetterDataPresenterImp(LetterDataView view, Context context) {
        super(view);
        letterDataModelImp = new LetterDataModelImp(context);
    }

    @Override
    public void getLetterDataByPage(int page) {
        letterDataModelImp.getLetterDataByPage(page, this);
    }
}
